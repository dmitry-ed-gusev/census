package org.census.commons.dto.personnel;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Simple "smoke" tests for PositionDto class.
 * Created by vinnypuhh on 09.08.2016.
 */

public class PositionDtoTest {

    @Test
    public void testEquals() {
        PositionDto position1 = new PositionDto(0, "position", 0);
        PositionDto position2 = new PositionDto(0, "position", 0);
        assertTrue("position equals #1", position1.equals(position2));
        assertTrue("position equals #2", position2.equals(position1));
        assertTrue("position equals #3", position1.hashCode() == position2.hashCode());
    }

    @Test
    public void testEqualsWithEmpty() {
        PositionDto emptyPosition1 = new PositionDto();
        PositionDto emptyPosition2 = new PositionDto();
        assertTrue("position equals with empty #1", emptyPosition1.equals(emptyPosition2));
        assertTrue("position equals with empty #2", emptyPosition2.equals(emptyPosition1));
        assertTrue("position equals with empty #3", emptyPosition1.hashCode() == emptyPosition2.hashCode());
    }

    @Test
    public void testNotEquals() {
        // # case 1
        PositionDto position1 = new PositionDto(1, "position", 0);
        PositionDto position2 = new PositionDto(2, "position", 0);
        assertTrue("position not equals #1", !position1.equals(position2));
        assertTrue("position not equals #2", !position2.equals(position1));
        assertTrue("position not equals #3", position1.hashCode() != position2.hashCode());
        // # case 2
        PositionDto position3 = new PositionDto(1, "position111", 2);
        PositionDto position4 = new PositionDto(1, "position222", 14);
        assertTrue("position not equals #4", !position3.equals(position4));
        assertTrue("position not equals #5", !position4.equals(position3));
        assertTrue("position not equals #6", position3.hashCode() != position4.hashCode());
        // # case 3
        PositionDto position5 = new PositionDto(1, "position111", 44);
        PositionDto position6 = new PositionDto(2, "position222", 44);
        assertTrue("position not equals #7", !position5.equals(position6));
        assertTrue("position not equals #8", !position6.equals(position5));
        assertTrue("position not equals #9", position5.hashCode() != position6.hashCode());
    }

}
