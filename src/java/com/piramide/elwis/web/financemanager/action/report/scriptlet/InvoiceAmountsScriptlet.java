package com.piramide.elwis.web.financemanager.action.report.scriptlet;

import com.piramide.elwis.utils.FinanceConstants;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Scriptlet to sum invoce amount row by currency
 *
 * @author Miky
 * @version $Id: InvoiceAmountsScriptlet.java 26-sep-2008 18:11:16 $
 */
public class InvoiceAmountsScriptlet extends JRDefaultScriptlet {
    private final static Log log = LogFactory.getLog(InvoiceAmountsScriptlet.class);

    private static String AMOUNTGROSS_KEY = "GROSS";
    private static String AMOUNTNET_KEY = "NET";
    private static String VAT_KEY = "VAT";
    private static String OPENAMOUNT_KEY = "OPENAMOUNT";

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
        Object amountGrossObj = this.getFieldValue("totalAmountGross");
        if (amountGrossObj != null && !GenericValidator.isBlankOrNull(amountGrossObj.toString())) {
            bigDecimalValue = new BigDecimal(amountGrossObj.toString());
        }
        return bigDecimalValue;
    }

    private BigDecimal getAmountNetField() throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object amountNetObj = this.getFieldValue("totalAmountNet");
        if (amountNetObj != null && !GenericValidator.isBlankOrNull(amountNetObj.toString())) {
            bigDecimalValue = new BigDecimal(amountNetObj.toString());
        }
        return bigDecimalValue;
    }

    private BigDecimal getOpenAmountField() throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object amountObj = this.getFieldValue("openAmount");
        if (amountObj != null && !GenericValidator.isBlankOrNull(amountObj.toString())) {
            bigDecimalValue = new BigDecimal(amountObj.toString());
        }
        return bigDecimalValue;
    }

    private void setValuesInTotalVariable(String variableName) throws JRScriptletException {
        Map amountTotalVar = (Map) this.getVariableValue(variableName);

        Integer currencyId = new Integer(this.getFieldValue("currencyId_FK").toString());

        BigDecimal amountGross = getAmountGrossField();
        BigDecimal amountNet = getAmountNetField();
        BigDecimal openAmountValue = getOpenAmountField();

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

        if (amountGross != null && amountNet != null) {
            String vatKey = VAT_KEY + currencyId;
            BigDecimal vatTotal = amountGross.subtract(amountNet);

            if (amountTotalVar.containsKey(vatKey)) {
                BigDecimal total = ((BigDecimal) amountTotalVar.get(vatKey)).add(vatTotal);
                amountTotalVar.put(vatKey, total);
            } else {
                amountTotalVar.put(vatKey, vatTotal);
            }
        }

        if (openAmountValue != null) {
            String openAmountKey = OPENAMOUNT_KEY + currencyId;
            if (amountTotalVar.containsKey(openAmountKey)) {
                BigDecimal total = ((BigDecimal) amountTotalVar.get(openAmountKey)).add(openAmountValue);
                amountTotalVar.put(openAmountKey, total);
            } else {
                amountTotalVar.put(openAmountKey, openAmountValue);
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

    public static BigDecimal getVatTotal(String currencyId, Map amountTotalVarMap) {
        String key = VAT_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    public static BigDecimal getOpenAmountTotal(String currencyId, Map amountTotalVarMap) {
        String key = OPENAMOUNT_KEY + currencyId;
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
