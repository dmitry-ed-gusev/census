package org.census.commons.dto.personnel.departments;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.AbstractEntityDto;
import org.census.commons.dto.personnel.ContactDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.census.commons.CensusDefaults.CS_DOMAIN_OBJECTS_STYLE;

/**
 * Domain object - company's department.
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 02.05.12)
 */

@Entity
@Indexed
@Table(name = "DEPARTMENTS")
public class DepartmentDto extends AbstractEntityDto {

    @Column(name = "code", nullable = false)
    private String           code;        // department inner code (not null)
    @Column(name = "name")
    private String           name;        // department name
    @Column(name = "description")
    private String           description; // department description
    @Column(name = "leftValue")
    private int              leftValue;   // storing hierarchical data
    @Column(name = "rightValue")
    private int              rightValue;  // storing hierarchical data

    // Mapping one-to-many to Employees (both entity and db table) for department chief.
    // This relation is UNI-directional (department->chief employee).
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chiefId", nullable = false)
    private EmployeeDto chief;       // department's chief

    // Mapping many-to-many to Employees (both entity and db table). This side (DepartmentDto) is
    // relation slave. Relation is BI-directional (departments<->employees).
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "departments", cascade = CascadeType.ALL)
    private Set<EmployeeDto> employees = new HashSet<>(0); // employees of this department

    // Mapping one-to-many to Contacts (both entity and db table) with extra table "departments_2_contacts".
    // Relation is UNI-directional (department->contacts).
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_departments_2_contacts",
            joinColumns        = {@JoinColumn(name = "departmentId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "contactId",  nullable = false, updatable = false)})
    private Set<ContactDto>  contacts  = new HashSet<>(0); // contacts of this department

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(int leftValue) {
        this.leftValue = leftValue;
    }

    public int getRightValue() {
        return rightValue;
    }

    public void setRightValue(int rightValue) {
        this.rightValue = rightValue;
    }

    public EmployeeDto getChief() {
        return chief;
    }

    public void setChief(EmployeeDto chief) {
        this.chief = chief;
    }

    public Set<EmployeeDto> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeDto> employees) {
        this.employees = employees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactDto> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("code", code)
                .append("name", name)
                .append("description", description)
                .append("leftValue", leftValue)
                .append("rightValue", rightValue)
                .append("chief", chief)
                .append("employees", employees)
                .append("contacts", contacts)
                .toString();
    }

}