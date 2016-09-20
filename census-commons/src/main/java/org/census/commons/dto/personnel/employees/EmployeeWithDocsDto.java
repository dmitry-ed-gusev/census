package org.census.commons.dto.personnel.employees;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.docs.DocumentDto;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.census.commons.CensusDefaults.CS_BATCH_SIZE;
import static org.census.commons.CensusDefaults.CS_DOMAIN_OBJECTS_STYLE;

/**
 * Extending domain object Employee by adding documents list, related to employee.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 13.08.2015)
 */

//@Entity
//@Table(name = "EMPLOYEES2")
//@Indexed // <- Hibernate search indexed entity annotation
public class EmployeeWithDocsDto extends EmployeeDto {

    // Mapping many-to-many to Documents (both entity and db table). This side (Employee) is
    // relation owner. Relation is BI-directional.
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_documents_2_employees",
            joinColumns        = {@JoinColumn(name = "employeeId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "documentId", nullable = false, updatable = false)})
    @BatchSize(size = CS_BATCH_SIZE)
    private Set<DocumentDto> documents = new HashSet<>(0);

    public Set<DocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentDto> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("documents", documents)
                .toString();
    }

}