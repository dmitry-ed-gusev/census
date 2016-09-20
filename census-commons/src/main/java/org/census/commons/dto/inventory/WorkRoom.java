package org.census.commons.dto.inventory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.census.commons.dto.AbstractEntity;

/**
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 07.11.2014)
*/

public class WorkRoom extends AbstractEntity {

    private String building;
    private int    floor;
    private String number;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("building", building)
                .append("floor", floor)
                .append("number", number)
                .toString();
    }

}