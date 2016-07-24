package org.census.commons.dto.docs;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.dto.AbstractEntityDto;
import org.census.commons.dto.personnel.departments.DepartmentWithDocsDto;
import org.census.commons.dto.personnel.employees.EmployeeWithDocsDto;
import org.hibernate.annotations.BatchSize;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.census.commons.CensusDefaults.*;

/**
 * Domain object - electronic document.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 11.03.14)
*/

@Entity
@Table(name = "DOCUMENTS")

// todo: map one entity to two tables?
// http://stackoverflow.com/questions/22668350/how-to-map-one-class-with-multiple-tables-in-hibernate-javax-persistance
// @SecondaryTable(name = "docstexts")

@Indexed // <- Hibernate search indexed entity annotation
public class DocumentDto extends AbstractEntityDto {

    @Column(name = "parentId") // todo: create self-joined field (link to parent document)
    private long            parentId;      // parent document ID

    // Mapping many-to-one to Documents Types (both entity and db table). This relation is UNI-directional
    // (document -> document type).
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "docTypeId", nullable = false)
    private DocumentTypeDto docType;       // document type

    @Column(name = "createDate")
    private Date            createDate;    // document create date
    @Column(name = "number")
    private String          number;        // document internal number
    @Column(name = "subject")
    private String          subject;       // document subject (description)
    @Column(name = "sendDate")
    private Date            sendDate;      // document sending date
    @Column(name = "executionDate")
    private Date            executionDate; // document execution date

    // Mapping one-to-many to Files (both entity and db table). Relation is UNI-directional (document->files).
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_documents_2_files",
            joinColumns        = {@JoinColumn(name = "documentId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "fileId", nullable = false, updatable = false)})
    private Set<FileDto> files = new HashSet<>(0); // attached files

    // Mapping many-to-many to Employees (both entity and db table). This side (DocumentDto) is
    // relation slave. Relation is BI-directional (documents<->employees).
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "documents", cascade = CascadeType.ALL)
    @BatchSize(size = CS_BATCH_SIZE)
    private Set<EmployeeWithDocsDto> employees = new HashSet<>(0);

    // Mapping many-to-many to Departments (both entity and db table). This side (DocumentDto) is
    // relation slave. Relation is BI-directional (documents<->departments).
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "documents", cascade = CascadeType.ALL)
    @BatchSize(size = CS_BATCH_SIZE)
    private Set<DepartmentWithDocsDto> departments = new HashSet<>(0);

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public DocumentTypeDto getDocType() {
        return docType;
    }

    public void setDocType(DocumentTypeDto docType) {
        this.docType = docType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Set<FileDto> getFiles() {
        return files;
    }

    public void setFiles(Set<FileDto> files) {
        this.files = files;
    }

    public Set<EmployeeWithDocsDto> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeWithDocsDto> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("parentId", parentId)
                .append("docType", docType)
                .append("createDate", createDate)
                .append("number", number)
                .append("subject", subject)
                .append("sendDate", sendDate)
                .append("executionDate", executionDate)
                .append("files", files)
                .append("employees", employees)
                .append("departments", departments)
                .toString();
    }

}