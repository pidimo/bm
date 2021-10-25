package com.piramide.elwis.web.common.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Jatun S.R.L.
 * <p/>
 * Encode as "escape" JavaScript function
 *
 * @author Fernando Montaño
 * @version $Id: JavaScriptEncoder.java 10405 2013-11-08 01:03:41Z miguel $
 */

public class JavaScriptEncoder {
    private static Log log = LogFactory.getLog(JavaScriptEncoder.class);

    /**
     * Used when the string to be encoded for the jscript is not called from a url and is called
     * directly.
     * DO NOT USE IF YOU ARE ENCODING A PARAM TO BE USED FOR a href=javascript...., for that check encodeForParam(string)
     * method.
     * REMEMBER, the function which will catch this value must use the unescape function.
     *
     * This method translates the passed in string into x-www-form-urlencoded
     * format using the "escape" funtion of JavaScript.
     *
     * @param text the string to be encoded
     * @return the jscript encoded string.
     */
    public static String encode(String text) {
        text = text.replaceAll("\\\\", "\\\\\\\\"); // if "\" characater is found, replace with scaped javascript character "\\", required to eval escape funtion
        text = text.replaceAll("\'", "\\\\\'"); // if " ' " characater is found, replace with scaped javascript character "\'", required to eval escape funtion

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Object encodedText;
        try {
            encodedText = engine.eval("escape('" + text + "')");
        } catch (ScriptException e) {
            // Should never happen
            log.error("Error in apply scape javascript function to:" + text, e);
            encodedText = text;
        }
        return encodedText.toString();
    }

    /**
     * This is used for all places where the string is passed as param to another jscript function
     * and also they param is invoked from window browser, e.g. href=javascript.selectField(....)
     * Only in that case it is required to use this method.
     * REMEMBER, the function which is catching the value must always unescape the parameter before using it.
     *
     * @param s the string to be encoded for param
     * @return the jscript encoded string
     */
    public static String encodeForParam(String s) {
        return encode(encode(s));
    }

    /**
     * Private constructor that does nothing. Included to avoid a default
     * public constructor being created by the compiler.
     */
    private JavaScriptEncoder() {
    }

    /**
     * Apply "unescape" javascript function
     *
     * NOTE: ONLY SHOULD BE USED WHEN EXPLICIT "escape" javascript function has been used to encode
     *
     * @param encodedText "escape" javascript encoded text
     * @return unescape result
     */
    public static String unescape(String encodedText) {
        String unescapeText = encodedText;

        if (encodedText != null) {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            try {
                Object evalText = engine.eval("unescape('" + encodedText + "')");
                if (evalText != null) {
                    unescapeText = evalText.toString();
                }
            } catch (ScriptException e) {
                // Should never happen
                log.error("Error in apply unescape javascript function to:" + encodedText, e);
            }
        }
        return unescapeText;
    }

}
