package org.census.commons.service;

import org.apache.commons.lang3.tuple.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.TimeMeasurementDto;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.census.commons.dto.personnel.employees.EmployeeTimedDto;
import org.census.commons.reports.TimesheetReport;
import org.census.commons.utils.CommonDatesUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 08.07.2015)
 */

@Service
@Transactional
public class TimesheetsService {

    private static final Log log = LogFactory.getLog(TimesheetsService.class);

    @Autowired
    private SessionFactory sessionFactory; // DBMS connection session factory

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /***/
    private static List<Pair<ImmutablePair<Date, Date>, MutablePair<Date, Date>>> getDatesIntervalsList(
            final Date start, final Date finish) throws ParseException {

        log.debug("TimesheetsService.getDatesIntervalsList() working.");

        // check input dates
        if (start == null || finish == null || start.after(finish)) {
            throw new IllegalStateException(String.format("Invalid dates (start [%s] or finish [%s])!", start, finish));
        }

        // reset interval dates
        Date startDate = CommonDatesUtils.resetToDayStart(start);
        Date endDate   = CommonDatesUtils.resetToDayStart(finish);
        // dates intervals list
        final List<Pair<ImmutablePair<Date, Date>, MutablePair<Date, Date>>> intervalsList = new ArrayList<>();
        final long diffInDays = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
        // generate dates intervals list
        final Calendar calendar = Calendar.getInstance(); // for operating with dates
        for (int i = 0; i <= diffInDays; i++) { // iterating over dates interval and produce result list
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, i);
            // concrete date (immutable)
            ImmutablePair<Date, Date> concreteDateInterval = new ImmutablePair<>(
                    CommonDatesUtils.resetToDayStart(calendar.getTime()), CommonDatesUtils.resetToDayEnd(calendar.getTime()));
            // min-max time values for concrete date (mutable)
            MutablePair<Date, Date> concreteDateMinMax = new MutablePair<>(
                    new Date(concreteDateInterval.getRight().getTime()), new Date(concreteDateInterval.getLeft().getTime()));
            intervalsList.add(new ImmutablePair<>(concreteDateInterval, concreteDateMinMax));
        }
        // returns intervals list
        return intervalsList;
    }

    /***/
    private static List<Triple<Date, Date, Long>> getTimeEntriesList(final Date start, final Date finish, Set<TimeMeasurementDto> measurements)
            throws ParseException {
        List<Triple<Date, Date, Long>> timeEntries = new ArrayList<>();

        if (measurements != null && !measurements.isEmpty()) {
            Date tmpDate; // tmp variable
            List<Pair<ImmutablePair<Date, Date>, MutablePair<Date, Date>>> intervalsList = TimesheetsService.getDatesIntervalsList(start, finish);
            for (TimeMeasurementDto measurement : measurements) { // iterate over measurements and generate draft list
                tmpDate = measurement.getMeasurement();
                for (Pair<ImmutablePair<Date, Date>, MutablePair<Date, Date>> oneDateInterval : intervalsList) { // iterate over intervals list
                    if (tmpDate.after(oneDateInterval.getLeft().getLeft()) &&
                            tmpDate.before(oneDateInterval.getLeft().getRight())) { // check - does measurement belong to current interval?
                        // check for min/max values for current date (day) interval
                        MutablePair<Date, Date> minMaxDatePair = oneDateInterval.getRight();
                        if (tmpDate.after(minMaxDatePair.getRight())) { // check max timestamp for current interval
                            minMaxDatePair.setRight(tmpDate);
                        }
                        if (tmpDate.before(minMaxDatePair.getLeft())) { // check min timestamp for current interval
                            minMaxDatePair.setLeft(tmpDate);
                        }
                    }
                } // end of FOR (iterating over dates intervals)
            } // end of FOR (measurements)

            Date minDate, maxDate;
            long diffInMillis;
            // generate time entries list and reset intervals list
            for (Pair<ImmutablePair<Date, Date>, MutablePair<Date, Date>> oneDateInterval : intervalsList) {
                ImmutablePair<Date, Date> date   = oneDateInterval.getLeft();  // processing date
                MutablePair<Date, Date>   minMax = oneDateInterval.getRight(); // min-max pair for processing date
                minDate = minMax.getLeft();
                maxDate = minMax.getRight();
                diffInMillis = maxDate.getTime() - minDate.getTime();
                // update time entries list
                timeEntries.add(new ImmutableTriple<>(minDate, maxDate, diffInMillis));
                // reset for processing date
                minMax.setLeft(date.getRight());
                minMax.setRight(date.getLeft());
            }
        } else {
            log.warn("Received empty time measurements list!");
        }

        return timeEntries;
    }

    /***/
    //@Transactional
    // todo: method should return report object for export and don't perform export itself!
    public void getReportByDepartments(final Date start, final Date finish, final String storagePath) throws ParseException {
        log.debug("TimesheetsEngine.getReportByDepartments() working.");

        // select all departments, except <test department> with id=14 and empty (with no employees) departments
        List depts = this.sessionFactory.getCurrentSession().createQuery("from DepartmentDto dept where dept.id <> 14 and dept.employees is not empty").list();

        if (depts != null && !depts.isEmpty()) { // if there are depts -> processing

            // iterating over found departments
            for (final Object object : depts) {
                final DepartmentDto dept = (DepartmentDto) object; // current processing department
                log.debug(String.format("Processing department [%s].", dept.getName()));

                Set<EmployeeDto> employees = dept.getEmployees();
                List<Pair<EmployeeDto, List<Triple<Date, Date, Long>>>> employeesTimeEntries = new ArrayList<>();
                // iterating over found employees
                if (employees != null && !employees.isEmpty()) {

                    List<Triple<Date, Date, Long>> timeEntries;

                    for (EmployeeDto employee : employees) { // iterate over employees
                        // check type
                        if (!(employee instanceof EmployeeTimedDto)) {
                            throw new IllegalStateException(String.format("Invalid type [%s] instead of [%s]!", EmployeeDto.class, EmployeeTimedDto.class));
                        }
                        // generate time entries list
                        timeEntries = TimesheetsService.getTimeEntriesList(start, finish, ((EmployeeTimedDto) employee).getTimeMeasurements());
                        // add current processed employee time data to list
                        employeesTimeEntries.add(new ImmutablePair<>(employee, timeEntries));
                    } // end of FOR (employees)

                } else {
                    log.warn(String.format("No employees for department [%s]!", dept.getName()));
                }

                // generate report for current department and export it
                TimesheetReport report = new TimesheetReport(dept, start, finish, employeesTimeEntries);
                report.exportDepartmentReportToExcel(storagePath);

            } // end of FOR (departments)

        } else {
            log.warn("No departments found!");
        }

    }

}