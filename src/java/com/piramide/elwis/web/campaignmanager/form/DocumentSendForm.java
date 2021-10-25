package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.web.campaignmanager.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: DocumentSendForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class DocumentSendForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate DocumentSendForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        //validate column order selected
        String firstColumnOrder = (String) getDto("firstOrder");
        String secondColumnOrder = (String) getDto("secondOrder");
        if (!GenericValidator.isBlankOrNull((firstColumnOrder)) && !GenericValidator.isBlankOrNull(secondColumnOrder)) {
            if (firstColumnOrder.equals(secondColumnOrder)) {
                errors.add("column", new ActionError("Campaign.docGenerate.error.sameOrderField"));
            }
        }

        //validate generation create communications, always the end
        if (getDto("createComm") != null) {
            if (GenericValidator.isBlankOrNull((String) getDto("docCommTitle"))) {
                errors.add("title", new ActionError("errors.required", JSPHelper.getMessage(request, "Campaign.activity.docGenerate.communicationTitle")));
            }

            if (errors.isEmpty() && GenericValidator.isBlankOrNull(getDto("createAgain").toString())) {
                Integer templateId = new Integer(getDto("templateId").toString());
                if ("true".equals(getDto("isCampaignLight"))) {
                    errors = Functions.generationComunicationsAlreadyCreated(templateId, request);
                } else {
                    errors = Functions.generationComunicationsAlreadyCreated(new Integer(getDto("activityId").toString()), templateId, request);
                }

                if (!errors.isEmpty()) {
                    setDto("createAgain", "true");
                }

            } else {
                setDto("createAgain", "");
            }
        }
        return errors;
    }
}
