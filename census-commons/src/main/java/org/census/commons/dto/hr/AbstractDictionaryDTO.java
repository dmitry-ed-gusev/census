package org.census.commons.dto.hr;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.census.commons.dto.AbstractEntityDto;

/**
 * hhpilot base vacancy DTO for all tables
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 24.10.12)
 */

public abstract class AbstractDictionaryDTO extends AbstractEntityDto {

    private String nameRus;
    private String nameEng;

    protected AbstractDictionaryDTO() {
    }

    /*
    protected BaseDictionaryDTO(int id, int deleted, int updateUser, String timestamp, String nameRus, String nameEng) {
        super(id, deleted, updateUser, timestamp);
        this.nameRus = nameRus;
        this.nameEng = nameEng;
    }
    */

    public String getNameRus() {
        return nameRus;
    }

    public void setNameRus(String nameRus) {
        this.nameRus = nameRus;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                appendSuper(super.toString()).
                append(" nameRus = ", nameRus).
                append(" nameEng = ", nameEng).
                toString();
    }
}
