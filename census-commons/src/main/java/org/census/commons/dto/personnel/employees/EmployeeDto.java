package org.census.commons.dto.personnel.employees;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.census.commons.CensusDefaults;
import org.census.commons.dto.AbstractEntityDto;
import org.census.commons.dto.admin.LogicUserDto;
import org.census.commons.dto.personnel.ContactDto;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.PositionDto;
import org.census.commons.utils.CommonStringUtils;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object - one employee. Employee should have family and name (mandatory). Other fields may be empty.
 * @author Gusev Dmitry
 * @version 1.0 (DATE: 02.05.12)
*/

// todo: implement sorting (Comparable/Comparator)
// todo: use internal exception instead of runtime?
// todo: implement equals/hashCode - like in PositionDto
@Entity
@Table(name = "EMPLOYEES")
@Indexed // <- Hibernate search indexed entity annotation
public class EmployeeDto extends AbstractEntityDto {

    @Column(name = "name")
    private String        name;        // mandatory field, shouldn't be empty!
    @Column(name = "family")
    private String        family;      // mandatory field, shouldn't be empty!
    @Column(name = "patronymic")
    private String        patronymic;  // optional field, may be empty
    @Column(name = "sex")
    private int           sex;         // sex = 0 -> male, sex !=0 -> female
    @Column(name = "birthDate")
    private Date          birthDate;   // employee birth date
    @Column(name = "clockNumber")
    private String        clockNumber; // employee's own clock number (табельный номер)

    // Mapping many-to-many to Departments (both entity and db table). This side (EmployeeDto) is
    // relation owner. Relation is BI-directional.
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_employees_2_departments",
            joinColumns        = {@JoinColumn(name = "employeeId",   nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "departmentId", nullable = false, updatable = false)})
    private Set<DepartmentDto> departments = new HashSet<>(0); // department object

    // Mapping many-to-many to Positions (both entity and db table). This side (EmployeeDto) is
    // relation owner. Relation is UNI-directional (employee->positions).
    @ManyToMany(fetch = FetchType.EAGER/*, cascade = CascadeType.ALL*/)
    @JoinTable(name = "link_employees_2_positions",
            joinColumns        = {@JoinColumn(name = "employeeId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "positionId", nullable = false, updatable = false)})
    private Set<PositionDto> positions = new HashSet<>(0);     // employee's positions list (zero or more)

    // Mapping one-to-many to LogicUsers (both entity and db table) with extra table "link_employees_2_logicusers".
    // Relation is UNI-directional (employee->logic users). This side is relation owner.
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_employees_2_logicusers",
            joinColumns        = {@JoinColumn(name = "employeeId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "logicUserId", nullable = false, updatable = false)})
    private Set<LogicUserDto> logicUsers = new HashSet<>(0);

    // Mapping one-to-many to Contacts (both entity and db table) with extra table "employees_2_contacts".
    // Relation is UNI-directional (employee->contacts).
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_employees_2_contacts",
            joinColumns        = {@JoinColumn(name = "employeeId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "contactId",  nullable = false, updatable = false)})
    private Set<ContactDto>         contacts         = new HashSet<>(0); // contacts for employee

    // --- auto calculated fields (are not persisted)
    //@Transient
    //private String        shortRusName; // derived field (from fullName), not persisted
    //@Transient
    //private String        shortEngName; // derived field (transliteration from shortRusName), not persisted
    //@Transient
    //private String        fullName;     // merged FAMILY+NAME+PATRONYMIC fields

    /** Default constructor. Usually used by frameworks like Spring/Hibernate. */
    public EmployeeDto() {}

    /**
     * Create object with specified id and fullName. Full name will be split into
     * family+name+patronymic fields with space as a separator. By tokens: #0 -> family,
     * #1 -> name, #2 and further -> patronymic.
     */
    public EmployeeDto(long id, String fullName) {
        this.setId(id);
        if (!StringUtils.isBlank(fullName)) {
            String[] nameArray = StringUtils.trimToNull(fullName).split("\\s+");
            this.family     = nameArray.length > 0 ? nameArray[0] : null;
            this.name       = nameArray.length > 1 ? nameArray[1] : null;
            this.patronymic = nameArray.length > 2 ?
                    StringUtils.join(Arrays.copyOfRange(nameArray, 2, nameArray.length), " ") : null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getClockNumber() {
        return clockNumber;
    }

    public void setClockNumber(String clockNumber) {
        this.clockNumber = clockNumber;
    }

    public String getShortRusName() {
        return CommonStringUtils.getShortRusName(StringUtils.join(new String[] {this.family, this.name, this.patronymic}, " "));
    }

    /*
    public String getShortRusName() {
        if (StringUtils.isBlank(this.shortRusName)) {
            this.initInternalFields();
        }
        return shortRusName;
    }

    public String getShortEngName() { // todo: rename to getShortTranslitName ???
        if (StringUtils.isBlank(this.shortEngName)) {
            this.initInternalFields();
        }
        return shortEngName;
    }

    public String getFullName() {
        if (StringUtils.isBlank(this.fullName)) {
            this.initInternalFields();
        }
        return fullName;
    */

    public Set<ContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactDto> contacts) {
        this.contacts = contacts;
    }

    public boolean isMale() {
        return (this.sex == 0);
    }

    public Set<PositionDto> getPositions() {
        return positions;
    }

    public void setPositions(Set<PositionDto> positions) {
        this.positions = positions;
    }

    public Set<DepartmentDto> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<DepartmentDto> departments) {
        this.departments = departments;
    }

    public Set<LogicUserDto> getLogicUsers() {
        return logicUsers;
    }

    public void setLogicUsers(Set<LogicUserDto> logicUsers) {
        this.logicUsers = logicUsers;
    }

    // todo: for relation between departments and employees one-to-many (one department for employee)
    public void setOnlyDepartment(DepartmentDto department) {
        // reset this departments list
        this.departments = new HashSet<>(0);
        // add only department to list
        this.departments.add(department);
    }

    // todo: for relation between departments and employees one-to-many (one department for employee)
    public DepartmentDto getOnlyDepartment() {
        return this.departments.isEmpty() ? null : this.departments.iterator().next();
    }

    // todo: for relation between positions and employees one-to-many (one position for employee)
    public void setOnlyPosition(PositionDto position) {
        // reset this positions list
        this.positions = new HashSet<>(0);
        // add only position to list
        this.positions.add(position);
    }

    // todo: for relation between positions and employees one-to-many (one position for employee)
    public PositionDto getOnlyPosition() {
        return this.positions.isEmpty() ? null : this.positions.iterator().next();
    }

    @Override
    public String toString() {
        //LogFactory.getLog(EmployeeDto.class).debug("EmployeeDto.toString().");
        return new ToStringBuilder(this, CensusDefaults.CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("family", family)
                .append("patronymic", patronymic)
                .append("sex", sex)
                .append("birthDate", birthDate)
                .append("clockNumber", clockNumber)
                .append("departments", departments)
                .append("positions", positions)
                .append("logicUsers", logicUsers)
                .append("contacts", contacts)
                //.append("shortRusName", this.getShortRusName())
                //.append("shortEngName", this.getShortEngName())
                //.append("fullName", this.getFullName())
                .toString();
    }

    /** Method initializes internal fields -> shortRusName, shortEngName, fullName. */
    private void initInternalFields() {
        // check internal state
        if (StringUtils.isBlank(this.family) || StringUtils.isBlank(this.name)) {
            throw new IllegalStateException(
                    String.format("Empty family [%s] or name [%s]!", this.family, this.name));
        }
        // build full name (Family Name Patronymic)
        StringBuilder builder = new StringBuilder(this.family).append(" ").append(this.name);
        if (!StringUtils.isBlank(this.patronymic)) {
            builder.append(" ").append(this.patronymic);
        }
        // init internal field - full name
        //this.fullName = builder.toString();
        // get short names (rus and translit)
        Pair<String, String> shortNames = CommonStringUtils.getShortAndTranslit(builder.toString());
        //this.shortRusName = shortNames.getLeft();
        //this.shortEngName = shortNames.getRight();
    }

}