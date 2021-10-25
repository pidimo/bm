package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.CustomerCmd;
import com.piramide.elwis.web.catalogmanager.form.CategoryFieldValueForm;
import com.piramide.elwis.web.catalogmanager.form.CategoryFormUtil;
import com.piramide.elwis.web.common.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Fernando Montaño
 * @version CustomerForm.java, v 2.0 May 18, 2004 2:32:24 PM
 */
public class CustomerForm extends DefaultForm {

    private Log log = LogFactory.getLog(CustomerForm.class);

    public CustomerForm() {
        super();
    }

    /**
     * Validate Customer Form
     *
     * @param mapping
     * @param request
     * @return Errors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Validate has been executed...");
        ActionErrors errors = new ActionErrors();

        //validate only if save button has been pressed.
        if (getDto("save") != null) {
            errors = super.validate(mapping, request);
            Functions.emptyOrOnlyOneSelectedValidation(this, "invoiceAddressId", "additionalAddressId", "Customer.invoiceAddress", "Customer.invoiceAdditionalAddress", errors, request);

            //categoryValidator
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


            if (!errors.isEmpty()) {
                CustomerCmd customerCmd = new CustomerCmd();
                customerCmd.setOp(""); //force to read
                customerCmd.putParam("addressId", getDto("addressId"));
                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(customerCmd, request);
                    if ("Fail".equals(resultDTO.getForward())) {
                        log.debug("do nothing");
                    } else { //read the customer category values
                        log.debug("CustomerCategoryValues readed");
                        //setDto("customerCategoryValues", resultDTO.get("customerCategoryValues"));
                    }
                } catch (AppLevelException e) {
                    log.debug("Customer data was not found");
                }
            }
        }
        return errors;
    }
}
