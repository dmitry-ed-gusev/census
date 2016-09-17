package org.census.commons.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for ContactType enumeration object.
 * Created by vinnypuhh on 10.09.2016.
 */

public class ContactTypeTest {

    @Test
    public void testGetTypeByEmptyString() {
        assertEquals("test empty #1", ContactType.OTHER, ContactType.getContactTypeByString(""));
        assertEquals("test empty #2", ContactType.OTHER, ContactType.getContactTypeByString("     "));
        assertEquals("test empty #3", ContactType.OTHER, ContactType.getContactTypeByString(null));
    }

    @Test
    public void testGetTypeByString() {
        assertEquals("test by string #1", ContactType.OTHER, ContactType.getContactTypeByString(" sky pe  "));
        assertEquals("test by string #2", ContactType.SKYPE, ContactType.getContactTypeByString(" skype  "));
        assertEquals("test by string #3", ContactType.JABBER, ContactType.getContactTypeByString("    jabber  "));
        assertEquals("test by string #4", ContactType.OTHER, ContactType.getContactTypeByString(" other  "));
    }

}
