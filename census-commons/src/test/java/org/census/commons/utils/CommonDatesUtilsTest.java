package org.census.commons.utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 03.08.2015)
*/

public class CommonDatesUtilsTest {

    @Test
    public void getHHMMSSfromMSTest() {
        assertEquals("Invalid time value!", "00:00:00", CommonDatesUtils.getHHMMSSfromMS(0));
        assertEquals("Invalid time value!", "00:00:00", CommonDatesUtils.getHHMMSSfromMS(-10L));
        assertEquals("Invalid time value!", "07:08:12", CommonDatesUtils.getHHMMSSfromMS(25692000L));
        assertEquals("Invalid time value!", "00:00:09", CommonDatesUtils.getHHMMSSfromMS(9000L));
    }

}