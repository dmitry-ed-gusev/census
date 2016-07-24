package org.census.commons.reports;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.census.commons.utils.report.TextTable;
import org.census.commons.utils.report.TextTableModel;
import org.census.commons.utils.CommonDatesUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.census.commons.CensusDefaults.CS_DATE_FORMAT;
import static org.census.commons.CensusDefaults.CS_TIME_FORMAT;

/**
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 13.07.2015)
 */

// todo: make class immutable?
public final class TimesheetReport {

    // class logger component
    private final static Log log = LogFactory.getLog(TimesheetReport.class);

    // length of lunch (minutes)
    private static final int      LUNCH_LENGTH_MINUTES      = 30;
    // length of lunch (milliseconds)
    private static final int      LUNCH_LENGTH_MILLISECONDS = LUNCH_LENGTH_MINUTES * 60 * 1000;
    //
    private static final String   META_HEADER               = "%s. Учет рабочего времени. Период: %s - %s.";
    //
    private static final String   DATASHEET_NAME            = "Учет РВ %s-%s";
    //
    private static final String[] HEADER                    = {"№", "ФИО Сотрудника", "Дата", "Начало\nрабочего дня",
            "Окончание\nрабочего дня", "Кол-во\nучтенных часов\nза день", "Кол-во\nотработанных\nчасов*",
            "Примечание", "Общее кол-во\nучтенных часов", "Общее кол-во\nотработанных\nчасов"};
    //
    private static final String   LUNCH_TIME_COMMENT        = "*Исключая время\nобеда (0:30)";

    // department for current report
    private DepartmentDto                                           department;
    // employees list with time measurements for report
    private List<Pair<EmployeeDto, List<Triple<Date, Date, Long>>>> timesheets;
    // report start date
    private Date                                                    startDate;
    // report finish date
    private Date                                                    finishDate;

    /***/
    public TimesheetReport(DepartmentDto department, Date startDate, Date finishDate, List<Pair<EmployeeDto, List<Triple<Date, Date, Long>>>> timesheets) {
        this.department = department;
        this.startDate  = startDate;
        this.finishDate = finishDate;
        this.timesheets = timesheets;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public List<Pair<EmployeeDto, List<Triple<Date, Date, Long>>>> getTimesheets() {
        return timesheets;
    }

    public void setTimesheets(List<Pair<EmployeeDto, List<Triple<Date, Date, Long>>>> timesheets) {
        this.timesheets = timesheets;
    }

    /***/
    public void exportDepartmentReportToExcel(final String storagePath) {

        final List<String[]> reportLines = new ArrayList<>(); // temporary storage for report

        // temporary values
        int       counter = 1;
        String[]  reportLine;
        long      totalTime;
        long      workedTime;
        final SimpleDateFormat date = new SimpleDateFormat(CS_DATE_FORMAT);
        final SimpleDateFormat time = new SimpleDateFormat(CS_TIME_FORMAT);
        // generating report
        for (final Pair<EmployeeDto, List<Triple<Date, Date, Long>>> timesheet : timesheets) {
            // reset some tmp data (before next employee processing)
            totalTime  = 0;
            workedTime = 0;
            // report lines for one employee
            reportLine  = new String[HEADER.length]; // number and employee name line
            reportLine[0] = String.valueOf(counter);
            reportLine[1] = timesheet.getLeft().getShortRusName();
            reportLines.add(reportLine);
            for (final Triple<Date, Date, Long> timeEntry : timesheet.getRight()) {
                reportLine = new String[HEADER.length]; // data line
                reportLine[2] = date.format(timeEntry.getLeft());   // DATE
                reportLine[3] = (timeEntry.getRight() > 0 ? time.format(timeEntry.getLeft())   : "-"); // START TIME
                reportLine[4] = (timeEntry.getRight() > 0 ? time.format(timeEntry.getMiddle()) : "-"); // END TIME

                if (timeEntry.getRight() > 0) { // there are measurements
                    totalTime  += timeEntry.getRight();
                    workedTime += (timeEntry.getRight() - LUNCH_LENGTH_MILLISECONDS);
                    // get counted time for current date (total/work)
                    reportLine[5] = CommonDatesUtils.getHHMMSSfromMS(timeEntry.getRight());                             // total time
                    reportLine[6] = CommonDatesUtils.getHHMMSSfromMS(timeEntry.getRight() - LUNCH_LENGTH_MILLISECONDS); // work time
                } else { // no measurements
                    reportLine[5] = "-";
                    reportLine[6] = "-";
                }
                reportLines.add(reportLine);
            } // end of FOR (report for one employee)
            reportLine = new String[HEADER.length]; // line with total and counted hours
            reportLine[8] = CommonDatesUtils.getHHMMSSfromMS(totalTime);  // Общее кол-во часов
            reportLine[9] = CommonDatesUtils.getHHMMSSfromMS(workedTime); // Отработано часов
            reportLines.add(reportLine);

            counter++;
        } // end of FOR (whole report)

        // add last two lines: one with comment (exclude lunch time) and one before next employee
        reportLine = new String[HEADER.length];
        reportLine[7] = LUNCH_TIME_COMMENT;
        reportLines.add(reportLine); // <- with comment
        reportLines.add(new String[HEADER.length]); // <- empty

        // generating text model and export it to Excel
        TextTableModel model = new TextTableModel(
                String.format(META_HEADER, this.department.getName(), date.format(startDate), date.format(finishDate)),
                HEADER, reportLines.toArray(new String[reportLines.size()][])
        );

        TextTable      table = new TextTable(model);
        // todo: move logic for creating dirs to export to Excel method?
        try {
            if (!new File(storagePath).exists()) {
                boolean result = new File(storagePath).mkdirs();
                if (result) {
                    log.debug(String.format("Reports catalog [%s] created!", storagePath));
                } else {
                    log.error(String.format("Can't create reports catalog [%s]!", storagePath));
                }
            }
            table.exportToExcelFile(
                    storagePath + "/" + this.department.getName() + ".xls",
                    String.format(DATASHEET_NAME, date.format(startDate), date.format(finishDate)),
                    true
            );
        } catch (IOException e) {
            log.error("Can't export timesheet report! Reason: " + e.getMessage());
        }
    }

}