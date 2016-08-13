package org.census.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 12.07.2015)
 */

public final class CommonStringUtils {

    private CommonStringUtils() {
        // non-instanceability
    }

    // this static member is used for transliteration method
    private static final Map<Character, String> CHARS_MAP = new HashMap<>();

    static { // fills in characters map for transliteration
        // uppercase letters pairs
        CHARS_MAP.put('А', "A");  CHARS_MAP.put('Б', "B");  CHARS_MAP.put('В', "V");  CHARS_MAP.put('Г', "G");
        CHARS_MAP.put('Д', "D");  CHARS_MAP.put('Е', "E");  CHARS_MAP.put('Ё', "E");  CHARS_MAP.put('Ж', "Zh");
        CHARS_MAP.put('З', "Z");  CHARS_MAP.put('И', "I");  CHARS_MAP.put('Й', "I");  CHARS_MAP.put('К', "K");
        CHARS_MAP.put('Л', "L");  CHARS_MAP.put('М', "M");  CHARS_MAP.put('Н', "N");  CHARS_MAP.put('О', "O");
        CHARS_MAP.put('П', "P");  CHARS_MAP.put('Р', "R");  CHARS_MAP.put('С', "S");  CHARS_MAP.put('Т', "T");
        CHARS_MAP.put('У', "U");  CHARS_MAP.put('Ф', "F");  CHARS_MAP.put('Х', "H");  CHARS_MAP.put('Ц', "C");
        CHARS_MAP.put('Ч', "Ch"); CHARS_MAP.put('Ш', "Sh"); CHARS_MAP.put('Щ', "Sh"); CHARS_MAP.put('Ъ', "'");
        CHARS_MAP.put('Ы', "Y");  CHARS_MAP.put('Ь', "'");  CHARS_MAP.put('Э', "E");  CHARS_MAP.put('Ю', "U");
        CHARS_MAP.put('Я', "Ya");
        //lowercase letters pairs
        CHARS_MAP.put('а', "a");  CHARS_MAP.put('б', "b");  CHARS_MAP.put('в', "v");  CHARS_MAP.put('г', "g");
        CHARS_MAP.put('д', "d");  CHARS_MAP.put('е', "e");  CHARS_MAP.put('ё', "e");  CHARS_MAP.put('ж', "zh");
        CHARS_MAP.put('з', "z");  CHARS_MAP.put('и', "i");  CHARS_MAP.put('й', "i");  CHARS_MAP.put('к', "k");
        CHARS_MAP.put('л', "l");  CHARS_MAP.put('м', "m");  CHARS_MAP.put('н', "n");  CHARS_MAP.put('о', "o");
        CHARS_MAP.put('п', "p");  CHARS_MAP.put('р', "r");  CHARS_MAP.put('с', "s");  CHARS_MAP.put('т', "t");
        CHARS_MAP.put('у', "u");  CHARS_MAP.put('ф', "f");  CHARS_MAP.put('х', "h");  CHARS_MAP.put('ц', "c");
        CHARS_MAP.put('ч', "ch"); CHARS_MAP.put('ш', "sh"); CHARS_MAP.put('щ', "sh"); CHARS_MAP.put('ъ', "'");
        CHARS_MAP.put('ы', "y");  CHARS_MAP.put('ь', "'");  CHARS_MAP.put('э', "e");  CHARS_MAP.put('ю', "u");
        CHARS_MAP.put('я', "ya");
    }

    /**
     * Method makes short russian name (Family N.P.) for full russian name (Family Name Patronymic).
     * If received full name is empty or null, returns empty string (""). If there are tokens with english
     * letters or mixed with rus/eng letters - they will be ignored.
     * Method assumes: token #0 -> family, #1 -> name, #2 and further -> patronymic.
     * @param str String cyrillic string with employee russian full name
     * @return String short russian name
     */
    public static String getShortRusName(String str) {
        StringBuilder shortRusName = new StringBuilder();

        if (!StringUtils.isBlank(str)) { // input string is OK - processing
            Matcher matcher = Pattern.compile("\\b(\\p{InCyrillic}+)\\b").matcher(str);
            int counter = 0;
            while (matcher.find()) {
                counter++;
                if (counter == 1) {
                    shortRusName.append(matcher.group().replaceFirst(matcher.group().substring(0, 1),
                            matcher.group().substring(0, 1).toUpperCase())).append(" ");
                } else if (counter > 1) {
                    shortRusName.append(matcher.group().substring(0, 1).toUpperCase()).append(".");
                }
            } // end of while
        }

        return StringUtils.trimToEmpty(shortRusName.toString());
    }

    /**
     * Returns transliterated russian string. If string is empty or null - returns empty string. All leading and trailing
     * spaces will be removed. If string contains mix of rus-eng letters - only russian letter will be processed (other remain
     * untouched).
     * @param str String source for transliteration
     * @return String transliteration result
     */
    public static String getTranslitOfString(String str) {
        StringBuilder result = new StringBuilder();

        if (!StringUtils.isBlank(str)) {
            for (int i = 0; i < str.length(); i++) {
                Character ch = str.charAt(i);
                String charFromMap = CHARS_MAP.get(ch);
                if (charFromMap == null) {
                    result.append(ch);
                } else {
                    result.append(charFromMap);
                }
            }
        }

        return StringUtils.trimToEmpty(result.toString());
    }

}