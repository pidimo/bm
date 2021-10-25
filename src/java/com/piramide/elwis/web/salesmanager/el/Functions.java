package com.piramide.elwis.web.salesmanager.el;

import com.piramide.elwis.cmd.financemanager.*;
import com.piramide.elwis.cmd.salesmanager.*;
import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.financemanager.InvoiceVatDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.dto.salesmanager.*;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.financemanager.form.ContractInvoiceCreateForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.math.BigDecimal;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    public static List getSequeceRuleResetTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> invoiceTypes = new ArrayList<LabelValueBean>();
        invoiceTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Invoice.type.invoice"),
                        FinanceConstants.InvoiceType.Invoice.getConstantAsString())
        );
        invoiceTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Invoice.type.creditNote"),
                        FinanceConstants.InvoiceType.CreditNote.getConstantAsString())
        );

        return invoiceTypes;
    }


    public static List getInvoiceVats(String invoiceIdAsString, javax.servlet.ServletRequest servletRequest) {
        List<InvoiceVatDTO> result = new ArrayList<InvoiceVatDTO>();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Integer invoiceId = null;

        if (null != invoiceIdAsString &&
                !"".equals(invoiceIdAsString.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdAsString);
            } catch (NumberFormatException e) {
                log.debug("-> Parse invoiceId=" + invoiceIdAsString + " FAIL");
                return result;
            }
        }

        User user = RequestUtils.getUser(request);
        InvoiceVatCmd invoiceVatCmd = new InvoiceVatCmd();
        invoiceVatCmd.putParam("invoiceId", invoiceId);
        invoiceVatCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        invoiceVatCmd.setOp("getInvoiceVats");
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceVatCmd, request);
            result = (List<InvoiceVatDTO>) resultDTO.get("getInvoiceVats");
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceVatCmd.class.getName() + " FAIL", e);
        }

        return result;
    }

    public static boolean hasZeroOpenAmount(String invoiceIdStr, javax.servlet.ServletRequest servletRequest) {

        Integer invoiceId = null;
        if (null != invoiceIdStr && !"".equals(invoiceIdStr.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdStr);
            } catch (NumberFormatException e) {
                log.debug("-> Parse invoiceId=" + invoiceIdStr + " FAIL");
                return false;
            }
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.putParam("invoiceId", invoiceId);
        invoicePaymentCmd.setOp("hasZeroOpenAmount");
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePaymentCmd, request);
            return (Boolean) resultDTO.get("hasZeroOpenAmount");
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePaymentCmd.class.getName() + " FAIL", e);
        }

        return false;
    }

    public static boolean canPayInvoice(String invoiceIdStr, javax.servlet.ServletRequest servletRequest) {

        Integer invoiceId = null;
        if (null != invoiceIdStr && !"".equals(invoiceIdStr.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdStr);
            } catch (NumberFormatException e) {
                log.debug("-> Parse invoiceId=" + invoiceIdStr + " FAIL");
                return false;
            }
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.putParam("invoiceId", invoiceId);
        invoicePaymentCmd.setOp("canPayInvoice");


        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePaymentCmd, request);
            return (Boolean) resultDTO.get("canPayInvoice");
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePaymentCmd.class.getName() + " FAIL", e);
        }

        return false;
    }


    public static boolean existsInvoiceReminders(String invoiceIdStr, javax.servlet.ServletRequest servletRequest) {
        Integer invoiceId = null;
        if (null != invoiceIdStr && !"".equals(invoiceIdStr.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdStr);
            } catch (NumberFormatException e) {
                log.debug("-> Parse invoiceId=" + invoiceIdStr + " FAIL");
                return false;
            }
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);


        InvoiceReminderCmd invoiceReminderCmd = new InvoiceReminderCmd();
        invoiceReminderCmd.putParam("invoiceId", invoiceId);
        invoiceReminderCmd.putParam("companyId", (Integer) user.getValue(Constants.COMPANYID));
        invoiceReminderCmd.setOp("existsInvoiceReminders");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReminderCmd, request);
            return (Boolean) resultDTO.get("existsInvoiceReminders");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + InvoiceReminderCmd.class.getName() + " FAIL", e);
        }

        return false;
    }

    public static String getReminderLevelName(String invoiceLevel, javax.servlet.ServletRequest servletRequest) {
        if (null == invoiceLevel || "".equals(invoiceLevel.trim())) {
            return "";
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        ReminderLevelCmd reminderLevelCmd = new ReminderLevelCmd();
        reminderLevelCmd.putParam("reminderLevel", Integer.valueOf(invoiceLevel));
        reminderLevelCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        reminderLevelCmd.setOp("getReminderLevelName");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reminderLevelCmd, request);
            String reminderLevelName = (String) resultDTO.get("getReminderLevelName");
            return reminderLevelName;
        } catch (AppLevelException e) {
            log.error("-> Execute " + ReminderLevelCmd.class.getName());
        }
        return "";
    }

    public static boolean existsInvoice(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                FinanceConstants.TABLE_INVOICE,
                "invoiceid",
                keyValue,
                errors, new ActionError("Invoice.NotFound"));
        return errors.isEmpty();
    }

    public static boolean existsSalesProcess(Object processId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_SALESPROCESS,
                "processid",
                processId,
                errors, new ActionError("SalesProcess.NotFound"));

        return errors.isEmpty();
    }

    public static ActionErrors existSalesProcess(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (null == request.getParameter("processId")) {
            errors.add("processid", new ActionError("SalesProcess.NotFound"));
            return errors;
        }

        String processId = request.getParameter("processId");
        if (!Functions.existsSalesProcess(processId)) {
            errors.add("processid", new ActionError("SalesProcess.NotFound"));
            return errors;
        }

        return null;
    }

    public static boolean existsSale(Object saleId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                SalesConstants.TABLE_SALE,
                "saleid",
                saleId,
                errors, new ActionError("Sale.NotFound"));
        return errors.isEmpty();
    }

    public static ActionErrors existsSale(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (null == request.getParameter("saleId")) {
            errors.add("SaleNotFound", new ActionError("Sale.NotFound"));
            return errors;
        }
        if (!Functions.existsSale(request.getParameter("saleId"))) {
            errors.add("SaleNotFound", new ActionError("Sale.NotFound"));
            return errors;
        }

        return null;
    }

    public static ActionErrors validateSalePositionPayMethodValue(String salePositionId, HttpServletRequest request) {
        if (GenericValidator.isBlankOrNull(salePositionId)) {
            return null;
        }

        SalePositionDTO salePositionDTO = Functions.getSalePositionDTO(salePositionId, request);
        if (null == salePositionDTO) {
            return null;
        }

        Integer salePositionPayMethod = (Integer) salePositionDTO.get("payMethod");
        if (null == salePositionPayMethod) {
            return null;
        }

        if (SalesConstants.PayMethod.SingleWithoutContract.getConstant() == salePositionPayMethod) {
            ActionErrors errors = new ActionErrors();
            errors.add("validateSalePositionPayMethodValue",
                    new ActionError("ProductContract.error.salePositionPayMethodChange",
                            JSPHelper.getMessage(request, "Contract.payMethod.singleWithoutContract")));
            return errors;
        }

        return null;
    }

    public static ActionErrors existsSalePosition(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!Functions.existsSalePosition(request.getParameter("salePositionId"))) {
            String productName = request.getParameter("productName");
            if (null != productName && !"".equals(productName.trim())) {
                errors.add("SalePositionNotFound", new ActionError("msg.NotFound", productName));
            } else {
                errors.add("SalePositionNotFound", new ActionError("SalePosition.NotFound"));
            }

            return errors;
        }
        return null;
    }

    public static boolean existsSalePosition(Object salePositionId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                SalesConstants.TABLE_SALEPOSITION,
                "salepositionid",
                salePositionId,
                errors, new ActionError("SalePosition.NotFound"));
        return errors.isEmpty();
    }

    public static boolean hasChangeCustomer(Object saleId,
                                            Object customerId,
                                            HttpServletRequest request) {
        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.setOp("hasChangeCustomer");
        saleReadCmd.putParam("saleId", saleId);
        saleReadCmd.putParam("customerId", customerId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);
            return ((Boolean) resultDTO.get("hasChangeCustomer"));
        } catch (AppLevelException e) {
            log.error("-> Execute " + SaleReadCmd.class.getName() + " FAIL ", e);
        }
        return false;
    }

    public static boolean existsProductContract(Object contractId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                SalesConstants.TABLE_PRODUCTCONTRACT,
                "contractid",
                contractId,
                errors, new ActionError("ProductContract.NotFound"));
        return errors.isEmpty();
    }

    /**
     * initialize only if ContractInvoiceCreateForm in request is NULL
     *
     * @param request
     * @return ContractInvoiceCreateForm
     */
    public static ContractInvoiceCreateForm initializeContractInvoiceCreateForm(HttpServletRequest request) {
        ContractInvoiceCreateForm contractInvoiceCreateForm = (ContractInvoiceCreateForm) request.getAttribute("contractInvoiceCreateForm");

        if (null == contractInvoiceCreateForm) {
            contractInvoiceCreateForm = new ContractInvoiceCreateForm();
            //set in request
            request.setAttribute("contractInvoiceCreateForm", contractInvoiceCreateForm);

            if (GenericValidator.isBlankOrNull((String) contractInvoiceCreateForm.getDto("type"))) {
                Functions.setDefaultOptionsForInvoice(contractInvoiceCreateForm, request);
            }

            Object contractDateFilter = request.getAttribute("dateFilterList");
            if (contractDateFilter != null) {
                contractInvoiceCreateForm.setDto("contractDateFilter", contractDateFilter);
                contractInvoiceCreateForm.setDto("invoiceDate", DateUtils.parseDate(new Integer(contractDateFilter.toString()), JSPHelper.getMessage(request, "datePattern")));
            }
        }

        return contractInvoiceCreateForm;
    }

    public static void setDefaultOptionsForInvoice(DefaultForm defaultForm, HttpServletRequest request) {
        defaultForm.setDto("type", FinanceConstants.InvoiceType.Invoice.getConstantAsString());

        CurrencyDTO basicCurrencyDTO =
                com.piramide.elwis.web.catalogmanager.el.Functions.getBasicCurrency(request);

        if (null != basicCurrencyDTO) {
            defaultForm.setDto("currencyId", basicCurrencyDTO.get("currencyId"));
        }

        CompanyDTO companyDTO =
                com.piramide.elwis.web.contactmanager.el.Functions.getCompanyConfiguration(request);
        defaultForm.setDto("sequenceRuleId", companyDTO.get("sequenceRuleIdForInvoice"));
        defaultForm.setDto("netGross", companyDTO.get("netGross"));
    }

    /**
     * recovery the max reminder level defined in the company
     *
     * @param servletRequest
     * @return integer
     */
    public static Integer getMaxReminderLevel(ServletRequest servletRequest) {
        Integer maxReminderLevel = null;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        MaxReminderLevelCmd maxReminderLevelCmd = new MaxReminderLevelCmd();
        maxReminderLevelCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(maxReminderLevelCmd, request);
            if (!resultDTO.isFailure() && resultDTO.get("maxLevel") != null) {
                maxReminderLevel = (Integer) resultDTO.get("maxLevel");
            }
        } catch (AppLevelException e) {
            log.error("Error in execute cmd...", e);
        }
        return maxReminderLevel;
    }

    /**
     * Get invoice type abbreviation
     *
     * @param type
     * @param isoLanguage
     * @return String
     */
    public static String getInvoiceTypeName(String type, String isoLanguage) {
        String result = "";
        Locale locale = new Locale(isoLanguage);
        if (FinanceConstants.InvoiceType.Invoice.getConstantAsString().equals(type)) {
            result = JSPHelper.getMessage(locale, "Invoice.type.invoice.abbr");
        } else if (FinanceConstants.InvoiceType.CreditNote.getConstantAsString().equals(type)) {
            result = JSPHelper.getMessage(locale, "Invoice.type.creditNote.abbr");
        }
        return result;
    }

    /**
     * Get contract pay method name
     *
     * @param constant       pay method constant
     * @param servletRequest request
     * @return String
     */
    public static String getPayMethodName(String constant, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String result = "";
        if (SalesConstants.PayMethod.Single.getConstantAsString().equals(constant)) {
            result = JSPHelper.getMessage(request, "Contract.payMethod.single");
        } else if (SalesConstants.PayMethod.Periodic.getConstantAsString().equals(constant)) {
            result = JSPHelper.getMessage(request, "Contract.payMethod.periodic");
        } else if (SalesConstants.PayMethod.PartialPeriodic.getConstantAsString().equals(constant)) {
            result = JSPHelper.getMessage(request, "Contract.payMethod.partialPeriodic");
        } else if (SalesConstants.PayMethod.PartialFixed.getConstantAsString().equals(constant)) {
            result = JSPHelper.getMessage(request, "Contract.payMethod.partialFixed");
        }
        return result;
    }

    /**
     * Compose session key to save list of ids to merge
     * invoice documents generated
     *
     * @param servletRequest request
     * @return String
     */
    public static String composeMergeInvoiceDocumentsSessionKey(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        String key = "MERGEDOCUMENT_USER_" + user.getValue("userId");
        return key;
    }

    /**
     * Contract income types to calculate totals in report
     *
     * @param servletRequest
     * @return List
     */
    public static List getContractIncomeTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> list = new ArrayList<LabelValueBean>();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "ContractToInvoice.realIncome"), SalesConstants.ContractIncome.REAL_INCOME.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "ContractToInvoice.virtualIncome"), SalesConstants.ContractIncome.VIRTUAL_INCOME.getConstantAsString()));

        return list;
    }

    /**
     * get invoice info as MAP if sale has only one invoice.
     *
     * @param saleId
     * @param servletRequest
     * @return Map
     */
    public static Map getOnlyOneSaleInvoiceInfoMap(String saleId, javax.servlet.ServletRequest servletRequest) {
        log.debug("Verify if sale has only one invoice...");
        Map invoiceInfoMap = new HashMap();

        if (!GenericValidator.isBlankOrNull(saleId)) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String listName = "saleInvoicedDocumentList";

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            String companyId = user.getValue(Constants.COMPANYID).toString();

            //parameters to execute list of manual form
            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, companyId);
            parameters.addSearchParameter("saleId", saleId);

            FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), "/finance");

            //Execute the list
            ListStructure list = null;
            try {
                list = fantabulousManager.getList(listName);
            } catch (ListStructureNotFoundException e) {
                log.debug("->Read List " + listName + " In Fantabulous structure Fail");
            }

            if (list != null) {
                Collection result = Controller.getList(list, parameters);

                //set sale invoices size
                invoiceInfoMap.put("saleInvoiceSize", result.size());

                //set info if sale has only one invoice created
                if (result.size() == 1) {
                    org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) result.iterator().next();

                    //get column defined in fantabulous list
                    invoiceInfoMap.put("documentId", fieldHash.get("documentId"));
                    invoiceInfoMap.put("invoiceId", fieldHash.get("invoiceId"));
                }
            }
        }

        return invoiceInfoMap;
    }

    public static boolean hasAssociatedSale(String processIdStr, ServletRequest servletRequest) {
        Integer processId = null;
        if (null != processIdStr && !"".equals(processIdStr.trim())) {
            try {
                processId = Integer.valueOf(processIdStr);
            } catch (NumberFormatException e) {
                log.debug("-> Parse processId=" + processIdStr + " FAIL");
                return false;
            }
        }

        boolean hasAssociatedSale = false;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.setOp("hasAssociatedSale");
        saleReadCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        saleReadCmd.putParam("processId", processId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);
            hasAssociatedSale = (Boolean) resultDTO.get("hasAssociatedSale");
            if (hasAssociatedSale) {
                request.setAttribute("saleName", resultDTO.get("title"));
                request.setAttribute("saleId", resultDTO.get("saleId"));
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + SaleReadCmd.class.getName() + " FAIL", e);
        }
        return hasAssociatedSale;
    }

    public static List getPayMethods(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> payMethods = new ArrayList<LabelValueBean>();

        payMethods.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Contract.payMethod.single"),
                        SalesConstants.PayMethod.Single.getConstantAsString())
        );

        payMethods.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Contract.payMethod.periodic"),
                        SalesConstants.PayMethod.Periodic.getConstantAsString())
        );

        payMethods.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Contract.payMethod.partialFixed"),
                        SalesConstants.PayMethod.PartialFixed.getConstantAsString())
        );
        payMethods.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Contract.payMethod.partialPeriodic"),
                        SalesConstants.PayMethod.PartialPeriodic.getConstantAsString())
        );

        return payMethods;
    }

    public static List getSalePositionPayMethods(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> payMethods = new ArrayList<LabelValueBean>();

        payMethods.add(
                new LabelValueBean(JSPHelper.getMessage(request, "Contract.payMethod.singleWithoutContract"),
                        SalesConstants.PayMethod.SingleWithoutContract.getConstantAsString())
        );
        payMethods.addAll(getPayMethods(request));
        return payMethods;
    }

    public static List getMatchCalendarPeriodOptions(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> matchCalendarList = new ArrayList<LabelValueBean>();
        matchCalendarList.add(
                new LabelValueBean(JSPHelper.getMessage(request, "ProductContract.matchCalendarPeriod.yes"),
                        SalesConstants.MatchCalendarPeriod.YES.getConstantAsString())
        );
        matchCalendarList.add(
                new LabelValueBean(JSPHelper.getMessage(request, "ProductContract.matchCalendarPeriod.no"),
                        SalesConstants.MatchCalendarPeriod.NO.getConstantAsString())
        );

        return matchCalendarList;
    }

    public static List getAmountTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> amountTypes = new ArrayList<LabelValueBean>();

        amountTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "ProductContract.amountType.amount"),
                        SalesConstants.AmounType.AMOUNT.getConstantAsString())
        );

        amountTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "ProductContract.amountType.percentage"),
                        SalesConstants.AmounType.PERCENTAGE.getConstantAsString())
        );

        return amountTypes;
    }

    public static void updateNetGrossPricesInProductContractForm(DefaultForm defaultForm,
                                                                 String productContractNetGross,
                                                                 String salePositionId,
                                                                 HttpServletRequest request) {
        if (GenericValidator.isBlankOrNull(productContractNetGross)) {
            return;
        }

        if (GenericValidator.isBlankOrNull(salePositionId)) {
            return;
        }

        BigDecimal priceToUpdate = null;

        SalePositionDTO salePositionDTO = getSalePositionDTO(salePositionId, request);
        if (null == salePositionDTO) {
            return;
        }
        if (FinanceConstants.NetGrossFLag.GROSS.equal(productContractNetGross)) {
            priceToUpdate = (BigDecimal) salePositionDTO.get("totalPriceGross");
        }

        if (FinanceConstants.NetGrossFLag.NET.equal(productContractNetGross)) {
            priceToUpdate = (BigDecimal) salePositionDTO.get("totalPrice");
        }

        if (null != priceToUpdate) {
            defaultForm.setDto("price", priceToUpdate);
            defaultForm.setDto("openAmount", priceToUpdate);
        }
    }

    public static void setProductContractDefaultValues(DefaultForm defaultForm,
                                                       String salePositionId,
                                                       HttpServletRequest request) {

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userAddressId");
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        defaultForm.setDto("sellerId", userId);

        CurrencyDTO basicCurrencyDTO =
                com.piramide.elwis.web.catalogmanager.el.Functions.getBasicCurrency(request);

        if (null != basicCurrencyDTO) {
            defaultForm.setDto("currencyId", basicCurrencyDTO.get("currencyId"));
        }

        CompanyDTO companyDTO =
                com.piramide.elwis.web.contactmanager.el.Functions.getCompanyConfiguration(request);
        Integer netGross = (Integer) companyDTO.get("netGross");
        defaultForm.setDto("netGross", netGross);

        SalePositionDTO salePositionDTO = getSalePositionDTO(salePositionId, request);

        if (null != salePositionDTO) {
            Integer saleId = (Integer) salePositionDTO.get("saleId");
            if (null != saleId) {
                SaleDTO saleDTO = getSaleDTO(saleId.toString(), request);
                netGross = (Integer) saleDTO.get("netGross");
                defaultForm.setDto("netGross", netGross);
            }

            defaultForm.setDto("addressName", salePositionDTO.get("customerName"));
            defaultForm.setDto("addressId", salePositionDTO.get("customerId"));
            defaultForm.setDto("productName", salePositionDTO.get("productName"));
            defaultForm.setDto("vatId", salePositionDTO.get("vatId"));
            defaultForm.setDto("productId", salePositionDTO.get("productId"));
            if (null != netGross) {
                BigDecimal contractPrice = null;
                BigDecimal contractOpenAmount = null;
                if (FinanceConstants.NetGrossFLag.NET.equal(netGross)) {
                    contractPrice = (BigDecimal) salePositionDTO.get("totalPrice");
                    contractOpenAmount = (BigDecimal) salePositionDTO.get("totalPrice");
                }

                if (FinanceConstants.NetGrossFLag.GROSS.equal(netGross)) {
                    contractPrice = (BigDecimal) salePositionDTO.get("totalPriceGross");
                    contractOpenAmount = (BigDecimal) salePositionDTO.get("totalPriceGross");
                }

                if (null != contractPrice) {
                    defaultForm.setDto("price", contractPrice);
                }

                if (null != contractOpenAmount) {
                    defaultForm.setDto("openAmount", contractOpenAmount);
                }
            }

            defaultForm.setDto("salePositionId", request.getParameter("salePositionId"));
            defaultForm.setDto("payMethod", SalesConstants.PayMethod.Single.getConstantAsString());
            if (null != salePositionDTO.get("payMethod")) {
                defaultForm.setDto("payMethod", salePositionDTO.get("payMethod"));
            }


            CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(
                    (Integer) salePositionDTO.get("customerId"), request);

            setCustomerProductContractDefaultValues(defaultForm, customerDTO);

            String customerNumber = null;
            if (null != customerDTO.get("number") &&
                    !"".equals(customerDTO.get("number").toString().trim())) {
                customerNumber = (String) customerDTO.get("number");
            }

            String productContractNumber = generateProductContractNumber(companyId, customerNumber, request);
            defaultForm.setDto("contractNumber", productContractNumber);
        }
    }

    public static void setProductContractDefaultValues(DefaultForm defaultForm,
                                                       String saleId,
                                                       String processId,
                                                       String salePositionId,
                                                       HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userAddressId");
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        defaultForm.setDto("sellerId", userId);

        SaleDTO saleDTO = getSaleDTO(saleId, request);
        defaultForm.setDto("orderDate", saleDTO.get("saleDate"));
        defaultForm.setDto("addressName", saleDTO.get("addressName"));
        defaultForm.setDto("addressId", saleDTO.get("customerId"));
        defaultForm.setDto("contactPersonId", saleDTO.get("contactPersonId"));
        defaultForm.setDto("currencyId", saleDTO.get("currencyId"));
        defaultForm.setDto("netGross", saleDTO.get("netGross"));

        CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(
                (Integer) saleDTO.get("customerId"), request);

        setCustomerProductContractDefaultValues(defaultForm, customerDTO);

        SalePositionDTO salePositionDTO = getSalePositionDTO(salePositionId, request);

        if (null != salePositionDTO) {
            defaultForm.setDto("productId", salePositionDTO.get("productId"));
            defaultForm.setDto("productName", salePositionDTO.get("productName"));
            defaultForm.setDto("vatId", salePositionDTO.get("vatId"));
            defaultForm.setDto("payMethod", salePositionDTO.get("payMethod"));
            defaultForm.setDto("price", salePositionDTO.get("totalPrice"));
            defaultForm.setDto("salePositionId", request.getParameter("salePositionId"));

            String customerNumber = null;
            if (null != customerDTO.get("number") &&
                    !"".equals(customerDTO.get("number").toString().trim())) {
                customerNumber = (String) customerDTO.get("number");
            }

            String productContractNumber = generateProductContractNumber(companyId, customerNumber, request);
            defaultForm.setDto("contractNumber", productContractNumber);
        }
    }

    public static void setCustomerProductContractDefaultValues(DefaultForm defaultForm, CustomerDTO customerDTO) {
        if (customerDTO != null) {

            if (null != customerDTO.get("payConditionId") && !"".equals(customerDTO.get("payConditionId").toString().trim())) {
                defaultForm.setDto("payConditionId", customerDTO.get("payConditionId"));
            }

            defaultForm.setDto("sentAddressId", null != customerDTO.get("invoiceAddressId") ? customerDTO.get("invoiceAddressId").toString() : "");
            defaultForm.setDto("sentContactPersonId", null != customerDTO.get("invoiceContactPersonId") ? customerDTO.get("invoiceContactPersonId").toString() : "");
            defaultForm.setDto("additionalAddressId", null != customerDTO.get("additionalAddressId") ? customerDTO.get("additionalAddressId").toString() : "");
        }
    }

    public static SalePositionDTO getSalePositionDTO(String salePositionId, HttpServletRequest request) {
        SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
        salePositionReadCmd.setOp("readSalePosition");
        salePositionReadCmd.putParam("salePositionId", salePositionId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(salePositionReadCmd, request);
            return (SalePositionDTO) resultDTO.get("readSalePosition");
        } catch (AppLevelException e) {
            log.error("-> Execute " + SalePositionReadCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    public static Integer getMaxDeliveryDateSalePosition(Integer saleId, HttpServletRequest request) {
        Integer deliveryDateMax = null;
        SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
        salePositionReadCmd.setOp("getMaxDeliveryDatePosition");
        salePositionReadCmd.putParam("saleId", saleId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(salePositionReadCmd, request);
            if (resultDTO.get("maxDeliveryDate") != null) {
                deliveryDateMax = (Integer) resultDTO.get("maxDeliveryDate");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + SalePositionReadCmd.class.getName() + " FAIL", e);
        }
        return deliveryDateMax;
    }

    private static String generateProductContractNumber(Integer companyId,
                                                        String customerNumber,
                                                        HttpServletRequest request) {
        ProductContractNumberGeneratorCmd cmd = new ProductContractNumberGeneratorCmd();
        cmd.putParam("companyId", companyId);
        cmd.putParam("customerNumber", customerNumber);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (String) resultDTO.get("generateContractNumber");

        } catch (AppLevelException e) {
            log.error("-> Execute " + ProductContractNumberGeneratorCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    public static void setSalePositionDefaultValues(DefaultForm defaultForm,
                                                    String saleId,
                                                    HttpServletRequest request) {
        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.putParam("saleId", saleId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);

            defaultForm.setDto("customerId", resultDTO.get("customerId"));
            defaultForm.setDto("active", "true");
            defaultForm.setDto("quantity", "1");
            defaultForm.setDto("payMethod", SalesConstants.PayMethod.SingleWithoutContract.getConstant());

            CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer((Integer) resultDTO.get("customerId"), request);
            if (null != customerDTO) {
                defaultForm.setDto("discount", customerDTO.get("defaultDiscount"));
            }

            //default delivery date
            if (saleId != null && saleId.length() > 0) {
                Integer deliveryDate = getMaxDeliveryDateSalePosition(Integer.valueOf(saleId), request);
                if (deliveryDate == null) {
                    deliveryDate = DateUtils.dateToInteger(new Date());
                }
                defaultForm.setDto("deliveryDate", deliveryDate);
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + SaleReadCmd.class.getName() + " FAIL", e);
        }
    }

    public static void setSaleCustomerDefaultValues(DefaultForm defaultForm, Integer addressId, HttpServletRequest request) {
        if (addressId != null) {
            CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(addressId, request);
            if (null != customerDTO) {

                defaultForm.setDto("sentAddressId", null != customerDTO.get("invoiceAddressId") ? customerDTO.get("invoiceAddressId").toString() : "");
                defaultForm.setDto("sentContactPersonId", null != customerDTO.get("invoiceContactPersonId") ? customerDTO.get("invoiceContactPersonId").toString() : "");
                defaultForm.setDto("additionalAddressId", null != customerDTO.get("additionalAddressId") ? customerDTO.get("additionalAddressId").toString() : "");
            }
        }
    }

    public static SaleDTO getSaleDTO(String saleId, HttpServletRequest request) {
        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.putParam("saleId", saleId);
        saleReadCmd.setOp("getSaleDTO");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);
            if (null != resultDTO.get("getSaleDTO")) {
                return (SaleDTO) resultDTO.get("getSaleDTO");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + SaleReadCmd.class.getName() + " FAIL", e);
        }
        return null;
    }

    public static SalesProcessDTO getSalesProcessDTO(String processId, HttpServletRequest request) {
        SalesProcessReadCmd salesProcessReadCmd = new SalesProcessReadCmd();
        salesProcessReadCmd.setOp("getSalesProcessDTO");
        salesProcessReadCmd.putParam("salesProcessId", processId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(salesProcessReadCmd, request);
            return (SalesProcessDTO) resultDTO.get("getSalesProcessDTO");
        } catch (AppLevelException e) {
            log.error("-> Execute " + SalePositionReadCmd.class.getName() + " FAIL ", e);
        }

        return null;
    }

    public static ActionDTO getActionDTO(String contactId, String processId, HttpServletRequest request) {
        ActionReadCmd actionReadCmd = new ActionReadCmd();
        actionReadCmd.putParam("contactId", contactId);
        actionReadCmd.putParam("processId", processId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionReadCmd, request);
            if (resultDTO.isFailure()) {
                return null;
            }

            ActionDTO dto = new ActionDTO();
            dto.putAll(resultDTO);
            return dto;
        } catch (AppLevelException e) {
            log.error("-> Execute " + ActionReadCmd.class.getName() + " FAIL ", e);
        }

        return null;
    }

    public static String getNetGrossFieldFromAction(String contactId, String processId, ServletRequest servletRequest) {
        ActionDTO actionDTO = getActionDTO(contactId, processId, (HttpServletRequest) servletRequest);
        if (null == actionDTO) {
            return "";
        }

        if (null == actionDTO.get("netGross")) {
            return FinanceConstants.NetGrossFLag.NET.getConstantAsString();
        }

        return actionDTO.get("netGross").toString();
    }

    public static String getNetGrossFieldFromSale(String saleId, ServletRequest servletRequest) {
        SaleDTO saleDTO = getSaleDTO(saleId, (HttpServletRequest) servletRequest);
        if (null == saleDTO) {
            return "";
        }

        if (null == saleDTO.get("netGross")) {
            return FinanceConstants.NetGrossFLag.NET.getConstantAsString();
        }

        return saleDTO.get("netGross").toString();
    }

    public static List<ProductDTO> getActionPositionProductsWithoutVat(String contactId, String processId, HttpServletRequest request) {
        ActionHelperCmd actionHelperCmd = new ActionHelperCmd();
        actionHelperCmd.setOp("getActionPositionProductsWithoutVat");
        actionHelperCmd.putParam("contactId", contactId);
        actionHelperCmd.putParam("processId", processId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionHelperCmd, request);
            return (List<ProductDTO>) resultDTO.get("getActionPositionProductsWithoutVat");
        } catch (AppLevelException e) {
            log.error("-> Execute " + ActionHelperCmd.class.getName() + " FAIL ", e);
        }

        return new ArrayList<ProductDTO>();
    }


    public static void updateActionPositionPrice(DefaultForm defaultForm, HttpServletRequest request) {
        String processId = (String) defaultForm.getDto("processId");
        String contactId = (String) defaultForm.getDto("contactId");

        ActionDTO actionDTO = getActionDTO(contactId, processId, request);
        if (null == actionDTO) {
            return;
        }

        updateProductPricesInForm(defaultForm, "price", "unitPriceGross", request);
    }

    public static void updateProductPricesInForm(DefaultForm defaultForm,
                                                 String priceNetKey,
                                                 String priceGrossKey,
                                                 HttpServletRequest request) {
        String productId = (String) defaultForm.getDto("productId");
        if (GenericValidator.isBlankOrNull(productId)) {
            defaultForm.setDto(priceNetKey, "");
            defaultForm.setDto(priceGrossKey, "");
            defaultForm.setDto("vatId", "");
            return;
        }

        ProductDTO productDTO = com.piramide.elwis.web.productmanager.el.Functions.getProductDTO(productId, request);
        if (null == productDTO) {
            return;
        }

        if (null != productDTO.get("priceGross")) {
            defaultForm.setDto(priceGrossKey, productDTO.get("priceGross"));
        }

        if (null != productDTO.get("price")) {
            defaultForm.setDto(priceNetKey, productDTO.get("price"));
        }

        if (null != productDTO.get("vatId")) {
            defaultForm.setDto("vatId", productDTO.get("vatId"));
        }

        defaultForm.setDto("productName", productDTO.get("productName"));
    }

    public static ProductContractDTO getProductContract(String contractId, HttpServletRequest request) {
        ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
        productContractUtilCmd.setOp("getProductContractDTO");
        productContractUtilCmd.putParam("contractId", contractId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productContractUtilCmd, request);
            return (ProductContractDTO) resultDTO.get("getProductContractDTO");
        } catch (AppLevelException e) {
            log.error("Unexpected error was happen in the ProductContractUtilCmd execution", e);
        }

        return null;
    }

    public static List getUnInvoicedPayments(String contractId, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        User user = RequestUtils.getUser(request);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.setModule("/sales");
        fantabulousUtil.addSearchParameter("companyId", user.getValue(Constants.COMPANYID).toString());
        fantabulousUtil.addSearchParameter("contractId", contractId);
        List<Map> data = fantabulousUtil.getData(request, "paymentStepList");
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        if (null == data) {
            return result;
        }

        for (Map element : data) {
            String paymentStepId = (String) element.get("paystepId");
            String payDate = (String) element.get("payDate");
            String formattedPayDate = DateUtils.parseDate(Integer.valueOf(payDate),
                    JSPHelper.getMessage(request, "datePattern"));

            String payAmount = (String) element.get("payAmount");

            String label = FormatUtils.formatDecimal(
                    new BigDecimal(payAmount),
                    locale,
                    JSPHelper.getMessage(locale, "numberFormat.2DecimalPlaces"));

            String amountType = (String) element.get("amounType");
            if (SalesConstants.AmounType.PERCENTAGE.equal(amountType)) {
                label += "%";
            }
            label += " - " + formattedPayDate;

            LabelValueBean labelValueBean = new LabelValueBean(label, paymentStepId);
            result.add(labelValueBean);
        }

        return result;
    }

    public static String getFormattedPaymentStepLabel(String paymentStepId, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        PaymentStepDTO paymentStepDTO = getPaymentStep(paymentStepId, request);
        if (null == paymentStepDTO) {
            return null;
        }

        String formattedPayDate = DateUtils.parseDate((Integer) paymentStepDTO.get("payDate"),
                JSPHelper.getMessage(request, "datePattern"));

        String label = FormatUtils.formatDecimal(
                (BigDecimal) paymentStepDTO.get("payAmount"),
                locale,
                JSPHelper.getMessage(locale, "numberFormat.2DecimalPlaces"));

        Integer amountType = (Integer) paymentStepDTO.get("amounType");
        if (SalesConstants.AmounType.PERCENTAGE.equal(amountType)) {
            label += "%";
        }
        label += " - " + formattedPayDate;

        return label;
    }

    public static PaymentStepDTO getPaymentStep(String paymentStepId, HttpServletRequest request) {
        ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
        productContractUtilCmd.setOp("getPaymentStepDTO");
        productContractUtilCmd.putParam("paymentStepId", paymentStepId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productContractUtilCmd, request);
            return (PaymentStepDTO) resultDTO.get("getPaymentStepDTO");
        } catch (AppLevelException e) {
            log.error("Unexpexted error was happen when trying execute ProductContractUtilCmd ", e);
        }

        return null;
    }

    public static List<PaymentStepDTO> getPaymentSteps(String contractId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
        productContractUtilCmd.setOp("getPaymentStepDTOs");
        productContractUtilCmd.putParam("contractId", contractId);
        productContractUtilCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productContractUtilCmd, request);
            return (List<PaymentStepDTO>) resultDTO.get("getPaymentStepDTOs");
        } catch (AppLevelException e) {
            log.error("Unexpexted error was happen when trying execute ProductContractUtilCmd ", e);
        }

        return new ArrayList<PaymentStepDTO>();
    }

    public static void updateActionPositionDescription(DefaultForm defaultForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        String productId = (String) defaultForm.getDto("productId");
        String addressId = (String) defaultForm.getDto("addressId");
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        if (GenericValidator.isBlankOrNull(productId)) {
            defaultForm.setDto("description", "");
            return;
        }

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("productId", Integer.valueOf(productId));
        invoicePositionCmd.putParam("addressId", Integer.valueOf(addressId));
        invoicePositionCmd.putParam("companyId", companyId);
        invoicePositionCmd.setOp("getProductTextByAddress");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            String text = (String) resultDTO.get("getProductTextByAddress");
            defaultForm.setDto("description", text);
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePositionCmd.class.getName() + " FAIL");
        }
    }

    public static Integer getNextActionPositionNumber(String processId, String contactId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        ActionPositionCmd actionPositionCmd = new ActionPositionCmd();
        actionPositionCmd.setOp("getLastPositionNumber");
        actionPositionCmd.putParam("processId", Integer.valueOf(processId));
        actionPositionCmd.putParam("contactId", Integer.valueOf(contactId));
        actionPositionCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionPositionCmd, request);
            Integer maxNumber = (Integer) resultDTO.get("getLastPositionNumber");
            if (null == maxNumber) {
                return 1;
            }

            return maxNumber + 1;
        } catch (AppLevelException e) {
            log.error("Can't execute " + ActionPositionCmd.class.getName(), e);
        }

        return 0;
    }

    public static List<ActionError> checkNetGrossChange(DefaultForm defaultForm, HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();

        String showNetGrossWarningMessage = (String) defaultForm.getDto("showNetGrossWarningMessage");
        if (!GenericValidator.isBlankOrNull(showNetGrossWarningMessage) &&
                "true".equals(showNetGrossWarningMessage)) {
            return errors;
        }

        String op = (String) defaultForm.getDto("op");
        if (!"update".equals(op)) {
            return errors;
        }

        String pageNetGross = (String) defaultForm.getDto("netGross");
        if (GenericValidator.isBlankOrNull(pageNetGross)) {
            return errors;
        }

        String saleId = (String) defaultForm.getDto("saleId");
        SaleDTO actualSaleDTO = Functions.getSaleDTO(saleId, request);
        if (null == actualSaleDTO) {
            return errors;
        }

        Integer actualNetGross = (Integer) actualSaleDTO.get("netGross");
        if (actualNetGross.toString().equals(pageNetGross)) {
            return errors;
        }

        errors = showNetGrossWarningMessage(saleId, request);
        if (errors.isEmpty()) {
            return errors;
        }

        defaultForm.setDto("showNetGrossWarningMessage", true);
        return errors;
    }

    /**
     * This method execute 'checkNetGrossChange' operation in SaleReadCmd and acording the
     * reply build the warning messages,
     * <p/>
     * The message was showing when the netgross field in the sale are changed.
     * The messages:
     * A) Sale.salePositionsRelatedWithInvoicePositions.msg
     * B) Sale.salePositionsRelatedWithProductContract.msg
     * C) Sale.productContractRelatedWithInvoicePositions.msg
     * <p/>
     * If  some sale position of sale are related with invoice position ( this when the sale position type is 'with out contract' )
     * the method returns (A).
     * <p/>
     * If some sale position are related with product contracts, the method return (B).
     * <p/>
     * If some sale position are related with product contracts and his product contracts are related with invoice positions
     * the method returns (C)
     * <p/>
     * The method also will be return A, B and C messages. The three together
     *
     * @param saleId  Sale idenfier
     * @param request HttpServletRequest Object used to recover resources and execute the command
     * @return List of ActionErrors
     */
    public static List<ActionError> showNetGrossWarningMessage(String saleId, HttpServletRequest request) {

        List<ActionError> errors = new ArrayList<ActionError>();

        User user = RequestUtils.getUser(request);

        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.setOp("checkNetGrossChange");
        saleReadCmd.putParam("saleId", saleId);
        saleReadCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);
            Boolean salePositionsRelatedWithInvoicePositions =
                    (Boolean) resultDTO.get("salePositionsRelatedWithInvoicePositions");
            Boolean salePositionsRelatedWithProductContracts =
                    (Boolean) resultDTO.get("salePositionsRelatedWithProductContracts");
            Boolean productContractRelatedWithInvoicePositions =
                    (Boolean) resultDTO.get("productContractRelatedWithInvoicePositions");

            if (null != salePositionsRelatedWithInvoicePositions && salePositionsRelatedWithInvoicePositions) {
                errors.add(new ActionError("Sale.salePositionsRelatedWithInvoicePositions.msg",
                        JSPHelper.getMessage(request, "Sale.netGross")));
            }

            if (null != salePositionsRelatedWithProductContracts && salePositionsRelatedWithProductContracts) {
                errors.add(new ActionError("Sale.salePositionsRelatedWithProductContract.msg",
                        JSPHelper.getMessage(request, "Sale.netGross")));
                if (null != productContractRelatedWithInvoicePositions && productContractRelatedWithInvoicePositions) {
                    errors.add(new ActionError("Sale.productContractRelatedWithInvoicePositions.msg",
                            JSPHelper.getMessage(request, "Sale.netGross")));
                }
            }

        } catch (AppLevelException e) {
            log.error(Functions.class.getName() + " can't execute " + SaleReadCmd.class.getName(), e);
        }

        return errors;
    }

    public static ActionError validateNetGrossField(DefaultForm defaultForm, HttpServletRequest request) {
        String pageNetGross = (String) defaultForm.getDto("netGross");
        if (GenericValidator.isBlankOrNull(pageNetGross)) {
            return null;
        }

        String op = (String) defaultForm.getDto("op");
        if ("create".equals(op)) {
            String processId = (String) defaultForm.getDto("processId");
            String contactId = (String) defaultForm.getDto("contactId");

            if (!GenericValidator.isBlankOrNull(processId) && !GenericValidator.isBlankOrNull(contactId)) {
                boolean containValidActionPositions = containValidActionPositions(contactId,
                        processId, pageNetGross, request);
                if (!containValidActionPositions) {
                    return new ActionError("Sale.invalidActionPosition.error");
                }
            }

            return null;
        }

        String saleId = (String) defaultForm.getDto("saleId");
        SaleDTO actualSaleDTO = Functions.getSaleDTO(saleId, request);
        if (null == actualSaleDTO) {
            return null;
        }

        User user = RequestUtils.getUser(request);

        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.setOp("changeNetGross");
        saleReadCmd.putParam("saleId", saleId);
        saleReadCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        saleReadCmd.putParam("pageNetGross", pageNetGross);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);
            Integer result = (Integer) resultDTO.get("changeNetGross");
            if (null != result && -1 == result) {
                return new ActionError("Sale.netGrossChange.invalidSalePosition.error",
                        JSPHelper.getMessage(request, "Sale.netGross"));
            }
        } catch (AppLevelException e) {
            log.error(Functions.class.getName() + " can't execute " + SaleReadCmd.class.getName(), e);
        }

        return null;
    }

    /**
     * This method executes 'containValidActionPositions' operation in ActionCmd and return a boolean
     * value that is returned from the command.
     * <p/>
     * returns true if the all action positions asociated to action ( with contactId and processId as key )
     * have the 'pageNetGross'  prices defined, false in another case.
     *
     * @param contactId    contactId of action
     * @param processId    processId of action
     * @param pageNetGross netGross field selected in the jsp page
     * @param request      HttpServletRequest Object to execute the command
     * @return true if the all action positions asociated to action ( with contactId and processId as key )
     *         have the selected 'pageNetGross'  prices defined, false in another case.
     */
    public static boolean containValidActionPositions(String contactId,
                                                      String processId,
                                                      String pageNetGross,
                                                      HttpServletRequest request) {
        ActionCmd actionCmd = new ActionCmd();
        actionCmd.setOp("containValidActionPositions");
        actionCmd.putParam("processId", processId);
        actionCmd.putParam("contactId", contactId);
        actionCmd.putParam("pageNetGross", pageNetGross);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionCmd, request);
            Boolean containValidActionPositions = (Boolean) resultDTO.get("containValidActionPositions");

            return null != containValidActionPositions && containValidActionPositions;
        } catch (AppLevelException e) {
            log.error(Functions.class.getName() + " can't execute " + ActionCmd.class.getName(), e);
        }

        return false;
    }

    public static BigDecimal sumInvoicePositions(String contractId, String paymentStepId, HttpServletRequest request) {
        ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
        productContractUtilCmd.setOp("sumInvoicePositions");
        productContractUtilCmd.putParam("contractId", contractId);
        productContractUtilCmd.putParam("paymentStepId", paymentStepId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productContractUtilCmd, request);
            return (BigDecimal) resultDTO.get("sumInvoicePositions");
        } catch (AppLevelException e) {
            log.error("Unexpexted error was happen when tried execute ProductContractUtilCmd ", e);
        }


        return null;
    }

    public static List<LabelValueBean> getActionForwardSelectOptions(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        if (com.piramide.elwis.web.common.el.Functions.hasAccessRight(request, "TASK", "CREATE")) {
            result.add(new LabelValueBean(JSPHelper.getMessage(request, "Action.option.newTask"), "1"));
        }

        if (com.piramide.elwis.web.common.el.Functions.hasAccessRight(request, "SALESPROCESSACTION", "CREATE")) {
            result.add(new LabelValueBean(JSPHelper.getMessage(request, "Action.option.newAction"), "2"));
        }

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }
}
