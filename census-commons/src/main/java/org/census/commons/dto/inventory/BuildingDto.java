package org.census.commons.dto.inventory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.census.commons.dto.AbstractEntity;

import javax.persistence.Column;

/**
 * Domain object (inventory) - one building.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 10.11.2014)
*/

//@Entity
//@Table(name = "TB_ERP_BUILDINGS")
//@SequenceGenerator(initialValue = 1, name = "id_generator", sequenceName = "ERP_BUILDINGS_SEQUENCE")
//@Indexed
public class BuildingDto extends AbstractEntity {

    @Column(name = "BUILDING_NAME")
    private String buildingName;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("buildingName", buildingName)
                .toString();
    }

}