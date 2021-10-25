package com.piramide.elwis.web.productmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;
import org.apache.struts.validator.Resources;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CompetitorForm.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class CompetitorForm extends DefaultForm {

    private Log log = LogFactory.getLog(CompetitorForm.class);

    String entryDateU;

    public String getEntryDate() {
        return entryDateU;
    }

    public void setEntryDate(String entryDate) {
        this.entryDateU = entryDate;
    }

    public CompetitorForm() {
        super();
        log.debug("CompetitorForm constructor called...");
    }

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("Validate has been executed...");

        MessageResources messages = (PropertyMessageResources)
                request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        ActionErrors errors = new ActionErrors();

        if (getDto("save") != null || "create".equals(getDto("op")) && request.getParameter("search") == null && request.getParameter("dto(cancel)") == null) {
            log.debug("save form");
            errors = super.validate(mapping, request);


            if (getDto("productName") == null || "".equals((String) getDto("productName"))) {
                errors.add("productname", new ActionError("errors.required",
                        Resources.getMessage(messages, locale, "Product.name")));
            }

            if (getDto("competitorId") == null || "".equals((String) getDto("competitorId"))) {
                errors.add("competitorname", new ActionError("errors.required",
                        Resources.getMessage(messages, locale, "Competitor.competitorName")));
            }
            //para entryDate format ....
        }
        return errors;
    }
}
