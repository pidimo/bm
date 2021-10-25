package com.piramide.elwis.web.financemanager.action.report.scriptlet;

import com.piramide.elwis.cmd.salesmanager.util.ProcessContractReportPayMethodUtil;
import com.piramide.elwis.utils.SalesConstants;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Scriptlet to sum contract to invoced price amount row by currency
 *
 * @author Miky
 * @version $Id: ContractInvoiceAmountsScriplet.java 18-dic-2008 13:20:20 $
 */
public class ContractInvoiceAmountsScriplet extends JRDefaultScriptlet {
    private final static Log log = LogFactory.getLog(ContractInvoiceAmountsScriplet.class);

    private static String AMOUNTNET_KEY = "NET";

    public void afterReportInit() throws JRScriptletException {
    }

    public void beforeDetailEval() throws JRScriptletException {

        Integer fromDate = getParameterAsInteger("fromDate");
        Integer toDate = getParameterAsInteger("toDate");
        Integer totalIncomeType = getParameterAsInteger("totalIncomeType");

        Integer currencyId = getFieldAsInteger("currencyId_FK");

        BigDecimal price = getFieldAsBigDecimal("price");
        BigDecimal discount = getFieldAsBigDecimal("discount");
        Integer payMethod = getFieldAsInteger("payMethod");

        Integer contractToBeInvoiced = getFieldAsInteger("toBeInvoiced");
        if (isContractNotToBeInvoiced(contractToBeInvoiced, getParameterAsString("CONTRACTTYPENAME_PARAM"))) {
            //contract that not be to invoiced should be negative
            price = price.multiply(BigDecimal.valueOf(-1));
        }

        //periodic fields
        Integer payStartDate = getFieldAsInteger("payStartDate");
        Integer payPeriod = getFieldAsInteger("payPeriod");
        Integer pricePeriod = getFieldAsInteger("pricePeriod");
        BigDecimal pricePerMonth = getFieldAsBigDecimal("pricePerMonth");

        Integer invoiceDelay = getFieldAsInteger("invoiceDelay");

        Integer invoicedUntil = null;
        Integer nextInvoiceDate = null;
        if ("true".equals(this.getParameterValue("onlyNotInvoiced"))) {
            invoicedUntil = getFieldAsInteger("invoicedUntil");
            nextInvoiceDate = getFieldAsInteger("nextInvoiceDate");
        }

        ProcessContractReportPayMethodUtil reportPayMethodUtil = new ProcessContractReportPayMethodUtil(price, discount, fromDate, toDate);


        if (SalesConstants.PayMethod.Single.getConstant() == payMethod) {
            reportPayMethodUtil.processSingleMethod();

        } else if (SalesConstants.PayMethod.Periodic.getConstant() == payMethod) {
            Integer contractEndDate = getFieldAsInteger("contractEndDate");
            Integer matchCalendarPeriod = getFieldAsInteger("matchCalPeriod");
            reportPayMethodUtil.processPeriodicMethod(payStartDate, contractEndDate, invoicedUntil, payPeriod, matchCalendarPeriod, totalIncomeType, pricePeriod, pricePerMonth, nextInvoiceDate, invoiceDelay);

        } else if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == payMethod) {
            Integer installment = getFieldAsInteger("installment");
            reportPayMethodUtil.processPartialPeriodicMethod(payStartDate, invoicedUntil, payPeriod, installment, totalIncomeType);

        } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == payMethod) {
            Integer amountType = getFieldAsInteger("amounType");
            BigDecimal payAmount = getFieldAsBigDecimal("payAmount");

            reportPayMethodUtil.processPartialFixedMethod(amountType, payAmount);
        }

        BigDecimal contractTotalNet = reportPayMethodUtil.getTotalNet();
        //add income value in contractIncomeVar
        String incomeVariableName = "contractIncomeVar";
        this.setVariableValue(incomeVariableName, contractTotalNet);

        //add in total variable
        String totalVariableName = "contractTotalVar";
        Map amountTotalVar = (Map) this.getVariableValue(totalVariableName);

        String totalNetKey = AMOUNTNET_KEY + currencyId;
        if (amountTotalVar.containsKey(totalNetKey)) {
            BigDecimal total = ((BigDecimal) amountTotalVar.get(totalNetKey)).add(contractTotalNet);
            amountTotalVar.put(totalNetKey, total);
        } else {
            amountTotalVar.put(totalNetKey, contractTotalNet);
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

    private Integer getParameterAsInteger(String fieldName) throws JRScriptletException {
        Integer integerValue = null;
        Object parameterObj = this.getParameterValue(fieldName);
        if (parameterObj != null && !GenericValidator.isBlankOrNull(parameterObj.toString())) {
            integerValue = new Integer(parameterObj.toString());
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

    public static BigDecimal getTotalNet(String currencyId, Map amountTotalVarMap) {
        String key = AMOUNTNET_KEY + currencyId;
        return getTotal(amountTotalVarMap, key);
    }

    private static BigDecimal getTotal(Map amountTotalVarMap, String key) {
        BigDecimal total = null;
        if (amountTotalVarMap.containsKey(key)) {
            total = (BigDecimal) amountTotalVarMap.get(key);
        }
        return total;
    }

    /**
     * contracttype tobeinvoiced DDBB field is boolean representation: tobeinvoiced = 1 isToInvoiced, tobeinvoiced = 0 isToNotInvoiced,
     *
     * @param toBeInvoicedField
     * @return
     */
    public static boolean isContractNotToBeInvoiced(Object toBeInvoicedField, String contractTypeNameParam) {
        //only when contract type is not selected as filter evaluate this: contractTypeNameParam = "" 
        return ("".equals(contractTypeNameParam) && toBeInvoicedField != null && Integer.valueOf(toBeInvoicedField.toString()) == 0);
    }

}
