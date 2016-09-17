package org.census.commons.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * Contact type - enmeration.
 * Created by vinnypuhh on 10.09.2016.
 */
public enum ContactType {
    SKYPE, JABBER, TWITTER, VKONTAKTE, FACEBOOK, WHATSUP, VIBER, EMAIL, ADDRESS, MOBILE, PHONE, OTHER;

    /**
     * If parameter is null or empty, returns OTHER.
     * Case insensitive.
     */
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public static ContactType getContactTypeByString(String type) {
        if (StringUtils.isBlank(type)) {
            return OTHER;
        }
        for (ContactType contactType : ContactType.values()) {
            if (contactType.name().equals(StringUtils.trimToEmpty(type).toUpperCase())) {
                return contactType;
            }
        }
        return OTHER;
    }

}
