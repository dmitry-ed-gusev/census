package org.census.commons.dto.docs;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.AbstractEntity;

import javax.persistence.Column;

import static org.census.commons.CensusDefaults.CS_DOMAIN_OBJECTS_STYLE;

/**
 * Domain object - document type (internal/external/order).
 * @author Gusev Dmitry
 * @version 2.0 (DATE: 25.03.2014)
*/

//@Entity
//@Table(name = "DOCUMENTSTYPES")
//@Indexed
public class DocumentTypeDto extends AbstractEntity {

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .toString();
    }

}