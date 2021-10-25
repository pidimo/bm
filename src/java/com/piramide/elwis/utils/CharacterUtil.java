package com.piramide.elwis.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util to process special characters
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.4.1
 */
public class CharacterUtil {

    private static Log log = LogFactory.getLog(CharacterUtil.class);

    /**
     * Mysql with utf8 character encoding not support some symbol characters like "http://www.fileformat.info/info/unicode/char/1f680/index.htm".
     * So clean this special symbols and replace with '?' character
     * @param value string
     * @return cleaned value
     */
    public static String cleanMysqlUnsupportedSymbols(String value) {

        String cleanedValue = value;
        if (value != null) {
            char[] charArray = value.toCharArray();
            boolean isCleaned = false;

            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                int codepoint = Character.codePointAt(charArray, i);

                if (Character.isSupplementaryCodePoint(codepoint)) {

                    int totalSymbolChars = Character.charCount(codepoint);
                    int count = 0;
                    while (count < totalSymbolChars) {
                        if ((i + count) < charArray.length) {
                            charArray[i + count] = '?';
                        }
                        count++;
                    }
                    isCleaned = true;
                }
            }

            if (isCleaned) {
                cleanedValue = new String(charArray);
            }
        }
        return cleanedValue;
    }

    /**
     * Truncate the text if this is more greater that limit
     * @param text text
     * @param limit limit
     * @return String
     */
    public static String truncate(String text, int limit) {
        if (text != null && text.length() > limit) {
            text = text.substring(0, limit);
        }
        return text;
    }

}
