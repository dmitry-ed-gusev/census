package org.census.commons.dto.hr;

import java.util.Date;

/**
 * Domain object - time measurement for employee. Object can be sorted according to 'natural'
 * ordering (it is Comparable).
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 07.07.2015)
*/

public class TimeMeasurementDto /*implements Comparable<TimeMeasurementDto>*/ {

    private Date measurement;

    public Date getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Date measurement) {
        this.measurement = measurement;
    }

    /*
    @Override
    // todo: null-check for argument and self value!!!
    public int compareTo(TimeMeasurementDto o) {
        return this.measurement.compareTo(o.measurement);
    }
   */

}