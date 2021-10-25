package com.piramide.elwis.cmd.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 * System template constant values, used to generate document
 *
 * @author Miky
 * @version $Id: SystemTemplateValues.java 11-dic-2008 10:47:23 $
 */
public class SystemTemplateValues {
    public static final String DEFAULT_ISOLANG = "en";

    /**
     * Invoice voucher type translation map: key= language ISO code, value= translation
     */
    public static Map<String, String> invoiceVoucherType = new HashMap<String, String>();

    /**
     * Credit note voucher type translation map: key= language ISO code, value= translation
     */
    public static Map<String, String> creditNoteVoucherType = new HashMap<String, String>();

    public static String getInvoiceVoucherType(String isoLanguage) {
        if (invoiceVoucherType.containsKey(isoLanguage)) {
            return invoiceVoucherType.get(isoLanguage);
        } else {
            return invoiceVoucherType.get(DEFAULT_ISOLANG);
        }
    }

    public static String getCreditNoteVoucherType(String isoLanguage) {
        if (creditNoteVoucherType.containsKey(isoLanguage)) {
            return creditNoteVoucherType.get(isoLanguage);
        } else {
            return creditNoteVoucherType.get(DEFAULT_ISOLANG);
        }
    }


}
