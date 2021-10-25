package com.piramide.elwis.cmd.utils;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Mar 16, 2005
 * Time: 12:05:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomEncrypt {
    //private Log log = LogFactory.getLog(this.getClass());
    /**
     * Singleton instance.
     */
    public static final CustomEncrypt i = new CustomEncrypt();

    private static final char[] CHARACTER = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', ':'};
    private static final char[] CODE = {'X', '4', 'A', 'Y', 'C', '8', 'B', 'J', 'F', 'O', 'R', 'W'};

    private CustomEncrypt() {
    }

    private char getEncode(char c) {
        for (int j = 0; j < CHARACTER.length; j++) {
            if (CHARACTER[j] == c) {
                return CODE[j];
            }
        }
        return ' ';
    }

    private char getDecode(char c) {
        for (int j = 0; j < CODE.length; j++) {
            if (CODE[j] == c) {
                return CHARACTER[j];
            }
        }
        return ' ';
    }

    public String encode(String cad) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < cad.length(); i++) {
            buffer.append(getEncode(cad.charAt(i)));
        }
        return buffer.toString();
    }

    public String decode(String cad) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < cad.length(); i++) {
            buffer.append(getDecode(cad.charAt(i)));
        }
        return buffer.toString();
    }

}
