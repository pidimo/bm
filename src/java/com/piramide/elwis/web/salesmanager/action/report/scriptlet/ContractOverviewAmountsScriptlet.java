package com.piramide.elwis.web.salesmanager.action.report.scriptlet;

import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.web.financemanager.action.report.scriptlet.ContractInvoiceAmountsScriplet;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import java.math.BigDecimal;
import java.util.Map;


/**
 * Jatun S.R.L.
 * Scriptlet to sum contract  price amount row by currency
 *
 * @author Miky
 * @version $Id: ContractOverviewAmountsScriptlet.java 08-ene-2009 13:49:05 $
 */
public class ContractOverviewAmountsScriptlet extends JRDefaultScriptlet {
    private final static Log log = LogFactory.getLog(ContractOverviewAmountsScriptlet.class);

    private static String TOTALPRICE_KEY = "NET";

    public void afterReportInit() throws JRScriptletException {
    }

    public void beforeDetailEval() throws JRScriptletException {

        Integer currencyId = getFieldAsInteger("currencyId_FK");
        BigDecimal price = getFieldAsBigDecimal("price");
        BigDecimal discount = getFieldAsBigDecimal("discount");

        Integer contractToBeInvoiced = getFieldAsInteger("toBeInvoiced");
        if (ContractInvoiceAmountsScriplet.isContractNotToBeInvoiced(contractToBeInvoiced, getParameterAsString("CONTRACTTYPENAME_PARAM"))) {
            //contract that not be to invoiced should be negative
            price = price.multiply(BigDecimal.valueOf(-1));
        }

        BigDecimal contractPriceNet = applyDiscount(price, discount);

        //add income value
        String incomeVariableName = "overviewIncomeVar";
        this.setVariableValue(incomeVariableName, contractPriceNet);

        //add in total variable
        String totalVariableName = "overviewTotalVar";
        Map amountTotalVar = (Map) this.getVariableValue(totalVariableName);

        String totalNetKey = TOTALPRICE_KEY + currencyId;
        if (amountTotalVar.containsKey(totalNetKey)) {
            BigDecimal total = ((BigDecimal) amountTotalVar.get(totalNetKey)).add(contractPriceNet);
            amountTotalVar.put(totalNetKey, total);
        } else {
            amountTotalVar.put(totalNetKey, contractPriceNet);
        }

        this.setVariableValue(totalVariableName, amountTotalVar);
    }

    private BigDecimal getFieldAsBigDecimal(String fieldName) throws JRScriptletException {
        BigDecimal bigDecimalValue = null;
        Object fieldObj = this.getFieldValue(fieldName);
        if (fieldObj != null && !GenericValidator.isBlankOrNull(fieldObj.toString())) {
            bigDecimalValue = new BigDecimal(fieldObj.toString());
        }
        return bigDecimalValue;
    }

    private Integer getFieldAsInteger(String fieldName) throws JRScriptletException {
        Integer integerValue = null;
        Object fieldObj = this.getFieldValue(fieldName);
        if (fieldObj != null && !GenericValidator.isBlankOrNull(fieldObj.toString())) {
            integerValue = new Integer(fieldObj.toString());
        }
        return integerValue;
    }

    private String getParameterAsString(String fieldName) throws JRScriptletException {
        String value = null;
        Object parameterObj = this.getParameterValue(fieldName);
        if (parameterObj != null) {
            value = parameterObj.toString();
        }
        return value;
    }

    public static BigDecimal getTotalPrice(String currencyId, Map amountTotalVarMap) {
        String key = TOTALPRICE_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    private static BigDecimal getTotal(Map amountTotalVarMap, String key) {
        BigDecimal total = null;
        if (amountTotalVarMap.containsKey(key)) {
            total = (BigDecimal) amountTotalVarMap.get(key);
        }
        return total;
    }

    private BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercent) {
        if (discountPercent != null) {
            BigDecimal discounted = BigDecimalUtils.getPercentage(amount, discountPercent);
            return BigDecimalUtils.subtract(amount, discounted);
        }
        return amount;
    }

}
