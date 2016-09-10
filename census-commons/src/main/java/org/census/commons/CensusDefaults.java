package org.census.commons;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Common defaults for whole application.
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 02.05.12)
*/

public interface CensusDefaults {

    /***/
    ToStringStyle CS_DOMAIN_OBJECTS_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;
    /***/
    //String        CS_INIT_PROPERTIES      = "census-init.properties";
    /** Common system date format (date without time). */
    String        CS_DATE_FORMAT          = "dd.MM.yyyy";
    /** Common system time format (date without date - just time). */
    String        CS_TIME_FORMAT          = "HH:mm:ss";
    /** Common system datetime format (date and time). */
    String        CS_DATETIME_FORMAT      = "dd.MM.yyyy HH:mm:ss";
    /** Batch size for collections (Hibernate). */
    int           CS_BATCH_SIZE           = 20;
}