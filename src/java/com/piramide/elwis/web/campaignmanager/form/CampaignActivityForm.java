package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: ivan
 * Date: 14-11-2006: 11:52:49 AM
 */
public class CampaignActivityForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        if (errors.isEmpty()) {
            int startDate = new Integer(getDto("startDate").toString());
            int closeDate = new Integer(getDto("closeDate").toString());
            if (startDate > closeDate) {
                errors.add("dateError", new ActionError("Common.greaterThan",
                        JSPHelper.getMessage(request, "CampaignActivity.closeDate"),
                        JSPHelper.getMessage(request, "CampaignActivity.startDate")));
            }
        }
        return errors;
    }
}
