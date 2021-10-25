package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Employee form handler
 *
 * @author Titus
 * @version EmployeeForm.java, v 2.0 May 18, 2004 2:32:24 PM
 */
public class EmployeeForm extends DefaultForm {

    private Log log = LogFactory.getLog(EmployeeForm.class);

    public EmployeeForm() {
        super();
        log.debug("EmployeeForm constructor called...");
    }

    public void reset(ActionMapping action, HttpServletRequest request) {
        super.reset(action, request);
        if (request.getSession().getAttribute("employeeFormTemp") != null
                && request.getParameter("dto(importhealthFund)") != null) {

            log.debug("enter here employee form import search health");
            this.getDtoMap().putAll(((EmployeeForm) request.getSession().getAttribute("employeeFormTemp")).getDtoMap());
        }
    }

    /**
     * Validate emplyee form (date)
     *
     * @param mapping
     * @param request
     * @return Errors
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("Validate has been executed...");

        ActionErrors errors = new ActionErrors();

        if (getDto("save") != null || "create".equals(getDto("op")) && request.getParameter("search") == null) {

            errors = super.validate(mapping, request);

            if ("create".equals(getDto("op"))) {
                if (GenericValidator.isBlankOrNull((String) getDto("name1"))) {
                    errors.add("name1", new ActionError("errors.required", JSPHelper.getMessage(request, "Contact.Person.lastname")));
                }

            }

            log.debug("save form..." + getDtoMap());
            if (errors.isEmpty()) {
                if (!GenericValidator.isBlankOrNull(getDto("hireDate").toString()) && !GenericValidator.isBlankOrNull(getDto("dateEnd").toString())) {
                    Integer hireDate = new Integer(getDto("hireDate").toString());
                    Integer dateEnd = new Integer(getDto("dateEnd").toString());
                    if (hireDate > dateEnd) {
                        errors.add("DateText", new ActionError("errors.EmployeeDateContrat"));
                    }
                }
            }
        }
        return errors;
    }

}
