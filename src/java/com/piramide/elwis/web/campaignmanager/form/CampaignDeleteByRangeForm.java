package com.piramide.elwis.web.campaignmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Form to validate delete campaign by record date range
 * @author Miguel A. Rojas Cardenas
 * @version 6.2
 */
public class CampaignDeleteByRangeForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate CampaignDeleteByRangeForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (errors.isEmpty()) {
            Integer startRecordDate = new Integer(getDto("startRecordDate").toString());
            Integer endRecordDate = new Integer(getDto("endRecordDate").toString());

            if (startRecordDate > endRecordDate) {
                errors.add("rangeError", new ActionError("Common.error.startDateGreaterEndDate"));
            }
        }
        return errors;
    }
}
