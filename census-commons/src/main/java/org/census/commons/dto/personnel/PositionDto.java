package org.census.commons.dto.personnel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.AbstractEntity;

import javax.persistence.Column;

import static org.census.commons.CensusDefaults.CS_DOMAIN_OBJECTS_STYLE;

/**
 * Domain object - one position. One employee may have zero or more positions. One position
 * may belong to zero or more employees - relation employees-positions is many-to-many.
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 02.05.12)
*/

//@Entity
//@Table(name = "POSITIONS")
//@Indexed
public class PositionDto extends AbstractEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name; // position name is unique
    @Column(name = "weight")
    private int    weight;

    /** Default constructor. Usually used by frameworks (Spring/Hibernate). */
    public PositionDto() {}

    /***/
    public PositionDto(long id, String name) {
        super(id);
        this.name = name;
    }

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
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        PositionDto other = (PositionDto) obj;

        return name != null ? name.equals(other.name) : other.name == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
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