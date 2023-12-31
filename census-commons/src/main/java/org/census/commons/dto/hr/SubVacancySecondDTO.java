package org.census.commons.dto.hr;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * hhpilot sub vacancy 2 DTO
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 24.10.12)
 */

public class SubVacancySecondDTO extends AbstractDictionary {

    public SubVacancySecondDTO() {
    }

    /*
    public SubVacancySecondDTO(int id, int deleted, int updateUser, String timestamp, String nameRus, String nameEng) {
        super(id, deleted, updateUser, timestamp, nameRus, nameEng);
    }
    */

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                appendSuper(super.toString()).toString();
    }
}

