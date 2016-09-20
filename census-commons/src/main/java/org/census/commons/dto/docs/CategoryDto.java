package org.census.commons.dto.docs;

import org.census.commons.dto.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object - one universal category (for documents, etc.)
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 14.03.14)
 * @deprecated we don't need this class...
*/

//@Entity
//@Table(name = "CATEGORIES")
//@Indexed
public class CategoryDto extends AbstractEntity {

    @Column(name = "CATEGORY_NAME")
    private String           name;
    @Column(name = "CATEGORY_LEFT_VALUE")
    private int              leftValue;
    @Column(name = "CATEGORY_RIGHT_VALUE")
    private int              rightValue;

    // 1. This is a bidirectional relationship. One of the sides (and only one) has to be the owner: the owner is
    //    responsible for the association column(s) update. To declare a side as not responsible for the relationship,
    //    the attribute mappedBy is used. mappedBy refers to the property name of the association on the owner side.
    //    In our case, this is property (field) "category" of DocumentDto entity. And we don’t have to (must not) declare
    //    the join column since it has already been declared on the owners side.
    // 2. Fetch type is LAZY (all related documents will not be extracted by default
    // 3. If mappedBy is present, two tables will be created by Hibernate - CATEGORIES and DOCUMENTS, if not -
    //    three tables will be created - CATEGORIES, DOCUMENTS, CATEGORIES_DOCUMENTS
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
    private Set<DocumentDto> documents = new HashSet<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(int left) {
        this.leftValue = left;
    }

    public int getRightValue() {
        return rightValue;
    }

    public void setRightValue(int right) {
        this.rightValue = right;
    }

    public Set<DocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentDto> documents) {
        this.documents = documents;
    }

}