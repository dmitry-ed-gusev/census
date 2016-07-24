package org.census.commons.dto.docs;

import org.census.commons.dto.AbstractEntityDto;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Domain object - uploaded file.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 12.08.2015)
 */

@Entity
@Indexed
@Table(name = "FILES")
public class FileDto extends AbstractEntityDto {

    @Column(name = "storedName")
    private String storedName; // under this name file will be stored in storage
    @Column(name = "realName")
    private String realName;   // received 'real' name of the file

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}