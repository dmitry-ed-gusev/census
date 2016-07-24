package org.census.commons.dto.personnel.employees;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.hr.TimeMeasurementDto;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Extending domain object Employee by adding time measurements.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 06.08.2015)
 */

public class EmployeeTimedDto extends EmployeeDto {

    private Set<TimeMeasurementDto> timeMeasurements = new LinkedHashSet<>(0); // time measurements for employee (work start/finish)

    public Set<TimeMeasurementDto> getTimeMeasurements() {
        return timeMeasurements;
    }

    public void setTimeMeasurements(Set<TimeMeasurementDto> timeMeasurements) {
        this.timeMeasurements = timeMeasurements;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("timeMeasurements", timeMeasurements)
                .toString();
    }

}