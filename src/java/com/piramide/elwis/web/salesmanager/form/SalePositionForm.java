package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.cmd.financemanager.InvoicePositionCmd;
import com.piramide.elwis.cmd.salesmanager.SalePositionReadCmd;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.form.CategoryFieldValueForm;
import com.piramide.elwis.web.catalogmanager.form.CategoryFormUtil;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SalePositionForm extends DefaultForm {
    protected SaleDTO saleDTO;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if (!existMainElement(request)) {
            return new ActionErrors();
        }

        if (!isSaveButtonPressed(request)) {
            return updateForm(request);
        }

        return customValidate(mapping, request);
    }

    protected void updatePositionText(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        Integer customerId = null;
        String customerIdFromForm = (String) getDto("customerId");
        if (!GenericValidator.isBlankOrNull(customerIdFromForm)) {
            try {
                customerId = Integer.valueOf(customerIdFromForm);
            } catch (NumberFormatException e) {
                //
            }
        }
        if (null != saleDTO) {
            customerId = (Integer) saleDTO.get("customerId");
        }

        String productId = (String) getDto("productId");
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        if (null == productId || "".equals(productId)) {
            setDto("text", "");
            setDto("unitPrice", "");
            setDto("unitPriceGross", "");
            setDto("totalPrice", "");
            setDto("totalPriceGross", "");
            setDto("vatId", "");
            setDto("productName", "");
            setDto("unitId", "");
            setDto("versionNumber", "");
            return;
        }

        Functions.updateProductPricesInForm(this, "unitPrice", "unitPriceGross", request);

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("productId", Integer.valueOf(productId));
        invoicePositionCmd.putParam("addressId", customerId);
        invoicePositionCmd.putParam("companyId", companyId);
        invoicePositionCmd.setOp("getProductTextByAddress");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            String text = (String) resultDTO.get("getProductTextByAddress");
            setDto("text", text);
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePositionCmd.class.getName() + " FAIL");
        }
    }

    /**
     * This method validate the payMethod changes, after of the validation return a ActionError Object
     * if can not change the payMethod or return null if can change it.
     * <p/>
     * The validation is executed only when the payMethod change from any type to SingleWithoutContract type
     * or when payMethod field change from SingleWithoutContract to other type.
     * <p/>
     * To check if the salePosition can change the payMethod, the method execute the  SalePositionReadCmd command
     * with the operation 'canChangePayMethod'.
     *
     * @param request HttpServletRequest Object to execute the command.
     * @return ActionError object if cannot change the payMethod null in other case.
     */
    protected ActionError validatePayMethodChanges(HttpServletRequest request) {
        String op = (String) getDto("op");
        if ("create".equals(op)) {
            return null;
        }

        String copyPayMethod = (String) getDto("copyPayMethod");
        if (null == copyPayMethod) {
            copyPayMethod = "";
        }

        String payMethod = (String) getDto("payMethod");
        if (null == payMethod) {
            payMethod = "";
        }

        //The payMethod field isn't change in the form
        if (copyPayMethod.equals(payMethod)) {
            return null;
        }

        /*
        if try change the payMethod from any type to SingleWithoutContract or
        if try change from SingleWithoutContract to any type then execute the validation.
         */
        if (SalesConstants.PayMethod.SingleWithoutContract.equal(payMethod) ||
                SalesConstants.PayMethod.SingleWithoutContract.equal(copyPayMethod)) {

            String salePositionId = (String) getDto("salePositionId");

            SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
            salePositionReadCmd.setOp("canChangePayMethod");
            salePositionReadCmd.putParam("salePositionId", Integer.valueOf(salePositionId));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(salePositionReadCmd, request);
                Boolean canChangePayMethod = (Boolean) resultDTO.get("canChangePayMethod");
                if (!canChangePayMethod) {
                    setDto("payMethod", getDto("copyPayMethod"));
                    return new ActionError("SalePosition.error.payMethodChange");
                }
            } catch (AppLevelException e) {
                log.error("-> Execute " + SalePositionReadCmd.class.getName() + " FAIL", e);
            }
        }

        return null;
    }


    /**
     * This method return a ActionError object to show a warning message.
     * The warning message is showed only when the saleposition has assigned productContracts and
     * the payMethod selected in the jsp page is SingleWithoutContract.
     * <p/>
     * The method use a hidden input element with name 'showPayMethodWarn' and his value helps to detect if the message
     * was shown.
     * <p/>
     * To check if the salePosition has assigned productContracts the method use SalePositionReadCmd command with
     * operation 'getProductContractDTOs'.
     *
     * @param request HttpServletRequest Object to execute the command and recover se user from session.
     * @return ActionError to show the warning or null if the message was showing.
     */
    protected ActionError showPayMethodChangeWarn(HttpServletRequest request) {
        String op = (String) getDto("op");
        if ("create".equals(op)) {
            return null;
        }

        String showPayMethodWarn = (String) getDto("showPayMethodWarn");
        if (null != showPayMethodWarn && "true".equals(showPayMethodWarn)) {
            return null;
        }

        String copyPayMethod = (String) getDto("copyPayMethod");
        if (null == copyPayMethod) {
            copyPayMethod = "";
        }

        String payMethod = (String) getDto("payMethod");
        if (null == payMethod) {
            payMethod = "";
        }

        //The payMethod field isn't change in the form
        if (copyPayMethod.equals(payMethod)) {
            return null;
        }

        if (!SalesConstants.PayMethod.SingleWithoutContract.equal(payMethod)) {
            return null;
        }

        User user = RequestUtils.getUser(request);

        String salePositionId = (String) getDto("salePositionId");

        SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
        salePositionReadCmd.setOp("getProductContractDTOs");
        salePositionReadCmd.putParam("salePositionId", salePositionId);
        salePositionReadCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(salePositionReadCmd, request);
            List<ProductContractDTO> productContractDTOs =
                    (List<ProductContractDTO>) resultDTO.get("getProductContractDTOs");
            if (!productContractDTOs.isEmpty()) {
                setDto("showPayMethodWarn", true);
                return new ActionError("SalePosition.changePayMethodToSingleWithoutContract.msg");
            }
        } catch (AppLevelException e) {
            log.error(SalePositionForm.class.getName() + " can't execute " + SalePositionReadCmd.class.getName(), e);
        }

        return null;
    }

    protected ActionErrors customValidate(ActionMapping mapping, HttpServletRequest request) {
        String active = (String) getDto("active");
        if (GenericValidator.isBlankOrNull(active)) {
            setDto("active", false);
        }

        ActionErrors errors = super.validate(mapping, request);
        ActionError error = validatePayMethodChanges(request);
        if (null != error) {
            errors.add("validatePayMethodChanges", error);
        }

        ActionError unitPriceError = validateUnitPrice(request);
        if (null != unitPriceError) {
            errors.add("unitPriceError", unitPriceError);
        }


        ActionError saleNetGrossChangeError = validateSaleNetGross(request);
        if (null != saleNetGrossChangeError) {
            errors.add("saleNetGrossChangeError", saleNetGrossChangeError);
        }

        //validate categories
        validateSalePositionCategoryValues(errors, request);

        if (errors.isEmpty()) {
            ActionError showPayMethodChangeWarn = showPayMethodChangeWarn(request);
            if (null != showPayMethodChangeWarn) {
                errors.add("showPayMethodChangeWarn", showPayMethodChangeWarn);
            }
        }

        return errors;
    }

    private void validateSalePositionCategoryValues(ActionErrors errors, HttpServletRequest request) {
        //category validator
        setDto("pageCategoryIds", CategoryFieldValueForm.getPageCategories(request));
        CategoryFormUtil utilValidator = new CategoryFormUtil(this.getDtoMap(), request);
        List<ActionError> l = utilValidator.validateCategoryFields();
        int counter = 0;
        for (ActionError r : l) {
            errors.add("catValidation_" + counter, r);
            counter++;
        }
        if (errors.isEmpty()) {
            getDtoMap().putAll(utilValidator.getDateOptionsAsInteger());
            getDtoMap().putAll(utilValidator.getAttachmentsDTOs());
        } else {
            utilValidator.restoreAttachmentFields();
        }
    }

    protected ActionErrors updateForm(HttpServletRequest request) {
        updatePositionText(request);
        ActionErrors errors = new ActionErrors();
        errors.add("emptyError", new ActionError("Admin.Company.new"));
        request.setAttribute("skipErrors", "true");
        return errors;
    }

    protected ActionError validateUnitPrice(HttpServletRequest request) {
        String price = null;
        String msg = "";
        String actualNetGross = (String) getDto("saleNetGross");

        if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
            price = (String) getDto("unitPrice");
            msg = "SalePosition.unitPriceNet";
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
            price = (String) getDto("unitPriceGross");
            msg = "SalePosition.unitPriceGross";
        }

        if (GenericValidator.isBlankOrNull(price)) {
            return null;
        }

        ActionError decimalValidation =
                FieldChecks.validateDecimalNumber(price, msg, 10, 4, request);

        if (null == decimalValidation) {
            if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
                setDto("unitPrice", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
            }

            if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
                setDto("unitPriceGross", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
            }
        }

        return decimalValidation;
    }

    protected boolean existMainElement(HttpServletRequest request) {
        String op = (String) getDto("op");
        boolean existsSale = Functions.existsSale(getDto("saleId"));

        if (existsSale) {
            this.saleDTO = Functions.getSaleDTO((String) getDto("saleId"), request);
        }

        if ("update".equals(op) || "delete".equals(op)) {
            boolean existSalePosition = Functions.existsSalePosition(getDto("salePositionId"));
            return existSalePosition && existsSale;
        }

        return existsSale;
    }

    protected ActionError validateSaleNetGross(HttpServletRequest request) {
        String saleNetGross = (String) getDto("saleNetGross");

        Integer actualSaleNetGross = (Integer) saleDTO.get("netGross");
        if (actualSaleNetGross.toString().equals(saleNetGross)) {
            return null;
        }

        return new ActionError("Sale.saleNetGrossChange.error",
                JSPHelper.getMessage(request, "Sale.netGross"));
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("save");
    }
}
