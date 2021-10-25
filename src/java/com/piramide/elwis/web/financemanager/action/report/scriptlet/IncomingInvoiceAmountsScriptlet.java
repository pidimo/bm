package com.piramide.elwis.web.financemanager.action.report.scriptlet;

import com.piramide.elwis.utils.FinanceConstants;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import org.apache.commons.validator.GenericValidator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceAmountsScriptlet.java 26-feb-2009 12:20:49
 */
public class IncomingInvoiceAmountsScriptlet extends JRDefaultScriptlet {

    private static String AMOUNTGROSS_KEY = "GROSS";
    private static String AMOUNTNET_KEY = "NET";
    private static String OPENAMOUNT_KEY = "OPEN";
    private static String VATAMOUNT_KEY = "VAT";

    public void afterReportInit() throws JRScriptletException {
    }

    public void beforeDetailEval() throws JRScriptletException {

        String type = this.getFieldValue("type").toString();

        if (FinanceConstants.InvoiceType.Invoice.getConstantAsString().equals(type)) {
            setValuesInTotalVariable("invoiceTotalVar");
        }

        if (FinanceConstants.InvoiceType.CreditNote.getConstantAsString().equals(type)) {
            setValuesInTotalVariable("creditNoteTotalVar");
        }
    }

    private BigDecimal getAmountGrossField() throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object amountGrossObj = this.getFieldValue("amountGross");
        if (amountGrossObj != null && !GenericValidator.isBlankOrNull(amountGrossObj.toString())) {
            bigDecimalValue = new BigDecimal(amountGrossObj.toString());
        }
        return bigDecimalValue;
    }

    private BigDecimal getAmountNetField() throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object amountNetObj = this.getFieldValue("amountNet");
        if (amountNetObj != null && !GenericValidator.isBlankOrNull(amountNetObj.toString())) {
            bigDecimalValue = new BigDecimal(amountNetObj.toString());
        }
        return bigDecimalValue;
    }

    private BigDecimal getOpenAmountField() throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object openAmountObj = this.getFieldValue("openAmount");
        if (openAmountObj != null && !GenericValidator.isBlankOrNull(openAmountObj.toString())) {
            bigDecimalValue = new BigDecimal(openAmountObj.toString());
        }
        return bigDecimalValue;
    }

    private BigDecimal getVatAmountField() throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object amountNetObj = this.getFieldValue("amountNet");
        Object amountGrossObj = this.getFieldValue("amountGross");
        if (amountGrossObj != null && amountGrossObj.toString().length() > 0 &&
                amountNetObj != null && amountNetObj.toString().length() > 0) {
            bigDecimalValue = new BigDecimal(amountGrossObj.toString()).subtract(new BigDecimal(amountNetObj.toString()));
        }
        return bigDecimalValue;
    }

    private void setValuesInTotalVariable(String variableName) throws JRScriptletException {
        Map amountTotalVar = (Map) this.getVariableValue(variableName);

        Integer currencyId = new Integer(this.getFieldValue("currencyId_FK").toString());

        BigDecimal amountGross = getAmountGrossField();
        BigDecimal amountNet = getAmountNetField();
        BigDecimal openAmount = getOpenAmountField();
        BigDecimal vatAmount = getVatAmountField();
        if (amountGross != null) {
            String grossKey = AMOUNTGROSS_KEY + currencyId;
            if (amountTotalVar.containsKey(grossKey)) {
                BigDecimal total = ((BigDecimal) amountTotalVar.get(grossKey)).add(amountGross);
                amountTotalVar.put(grossKey, total);
            } else {
                amountTotalVar.put(grossKey, amountGross);
            }
        }

        if (amountNet != null) {
            String netKey = AMOUNTNET_KEY + currencyId;
            if (amountTotalVar.containsKey(netKey)) {
                BigDecimal total = ((BigDecimal) amountTotalVar.get(netKey)).add(amountNet);
                amountTotalVar.put(netKey, total);
            } else {
                amountTotalVar.put(netKey, amountNet);
            }
        }

        if (openAmount != null) {
            String openKey = OPENAMOUNT_KEY + currencyId;
            if (amountTotalVar.containsKey(openKey)) {
                BigDecimal total = ((BigDecimal) amountTotalVar.get(openKey)).add(openAmount);
                amountTotalVar.put(openKey, total);
            } else {
                amountTotalVar.put(openKey, openAmount);
            }
        }

        if (vatAmount != null) {
            String vatKey = VATAMOUNT_KEY + currencyId;
            if (amountTotalVar.containsKey(vatKey)) {
                BigDecimal total = ((BigDecimal) amountTotalVar.get(vatKey)).add(vatAmount);
                amountTotalVar.put(vatKey, total);
            } else {
                amountTotalVar.put(vatKey, vatAmount);
            }
        }

        this.setVariableValue(variableName, amountTotalVar);
    }

    public static BigDecimal getAmountGrossTotal(String currencyId, Map amountTotalVarMap) {
        String key = AMOUNTGROSS_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    public static BigDecimal getAmountNetTotal(String currencyId, Map amountTotalVarMap) {
        String key = AMOUNTNET_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    public static BigDecimal getOpenAmountTotal(String currencyId, Map amountTotalVarMap) {
        String key = OPENAMOUNT_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    public static BigDecimal getVatAmountTotal(String currencyId, Map amountTotalVarMap) {
        String key = VATAMOUNT_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    private static BigDecimal getTotal(Map amountTotalVarMap, String key) {
        BigDecimal total = null;
        if (amountTotalVarMap.containsKey(key)) {
            total = (BigDecimal) amountTotalVarMap.get(key);
        }
        return total;
    }

}