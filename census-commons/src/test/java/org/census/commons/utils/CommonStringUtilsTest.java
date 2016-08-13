package org.census.commons.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for common string utils.
 * Created by vinnypuhh on 11.08.2016.
 */
public class CommonStringUtilsTest {

    @Test
    public void testShortRusNameWithEmpty() {
        assertTrue("".equals(CommonStringUtils.getShortRusName("")));
    }

    @Test
    public void testShortRusNameWithNull() {
        assertTrue("".equals(CommonStringUtils.getShortRusName(null)));
    }

    @Test
    public void testShortRusName() {
        assertTrue("Гусев Д.Э.".equals(CommonStringUtils.getShortRusName("Гусев   Дмитрий     Эдуардович")));
        assertTrue("Гусев Д.".equals(CommonStringUtils.getShortRusName("   Гусев       Дмитрий")));
        assertTrue("Гусев".equals(CommonStringUtils.getShortRusName("   Гусев           ")));
        assertTrue("Саласар Х.К.М.".equals(CommonStringUtils.getShortRusName("  Саласар   Хуан  Карлос     Мартинес")));
    }

    @Test
    public void testShortRusNameWithEnglish() {
        assertTrue("".equals(CommonStringUtils.getShortRusName("English Name")));
        assertTrue("Рашн".equals(CommonStringUtils.getShortRusName("English Рашн Zzzzz")));
        assertTrue("".equals(CommonStringUtils.getShortRusName("Engяяяяяяlish Naыыыыыыыыme")));
    }

    @Test
    public void testTranslitWithEmpty() {
        assertTrue("".equals(CommonStringUtils.getTranslitOfString("")));
        assertTrue("".equals(CommonStringUtils.getTranslitOfString("     ")));
    }

    @Test
    public void testTranslitWithNull() {
        assertTrue("".equals(CommonStringUtils.getTranslitOfString(null)));
    }

    @Test
    public void testTranslit() {
        assertTrue("privet".equals(CommonStringUtils.getTranslitOfString("  привет     ")));
        assertTrue("privet".equals(CommonStringUtils.getTranslitOfString(" приvet   ")));
        // todo: add more test cases!
    }

}
