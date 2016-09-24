package org.census.commons.dto.personnel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.AbstractEntity;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static org.census.commons.CensusDefaults.CS_DOMAIN_OBJECTS_STYLE;

/**
 * Domain entity object - one position. One employee may have zero or more (n) positions.
 * One position may belong to zero or more (n) employees - relation employees-positions
 * is many-to-many. Relation owner - employee domain entity object.
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 02.05.12)
*/

@Entity
@Indexed
@Table(name = "POSITIONS")
public class PositionDto extends AbstractEntity {

    @Column(name = "NAME", unique = true, nullable = false)
    private String name; // position name, mandatory, unique, not nullable
    @Column(name = "WEIGHT")
    private int    weight; // position weight, nullable, not unique

    /** Default constructor. Usually used by frameworks (Spring/Hibernate). */
    public PositionDto() {}

    /***/
    public PositionDto(long id, String name, int weight) {
        super(id);
        this.name = name;
        this.weight = weight;
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PositionDto)) return false;
        if (!super.equals(obj)) return false;

        PositionDto other = (PositionDto) obj;

        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("weight", weight)
                .toString();
    }

}