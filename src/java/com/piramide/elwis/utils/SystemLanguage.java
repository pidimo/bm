package com.piramide.elwis.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Language Application constants, used for i18n of UI.
 *
 * @author Yumi
 * @version $Id: SystemLanguage.java 7936 2007-10-27 16:08:39Z fernando $
 */

public abstract class SystemLanguage {

    public static final String SYSTEM_CONSTANT_KEY = "SYSTEMLANGUAGE";
    public static final String SYSTEM_TRANSLATION = "SYST";
    public static final String USER_TRANSLATIONS = "USRT";

    /**
     * System language map key= language ISO code, value= Resource key name
     */
    public static Map systemLanguages = new HashMap();
}
