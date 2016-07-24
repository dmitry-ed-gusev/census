package org.census.commons.dto.personnel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.AbstractEntityDto;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static org.census.commons.CensusDefaults.CS_DOMAIN_OBJECTS_STYLE;

/**
 * Domain object - one position. One employee may have zero or more positions. One position
 * may belong to zero or more employees - relation employees-positions is many-to-many.
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 02.05.12)
*/

@Entity
@Table(name = "POSITIONS")
@Indexed
public class PositionDto extends AbstractEntityDto {

    @Column(name = "name")
    private String name;
    @Column(name = "weight")
    private int    weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        //LogFactory.getLog(PositionDto.class).debug("PositionDto.toString().");
        return new ToStringBuilder(this, CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("weight", weight)
                .toString();
    }

}