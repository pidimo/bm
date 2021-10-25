package com.piramide.elwis.web.campaignmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;

/**
 * @author Yumi
 * @version $Id: CampaignForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignForm extends DefaultForm {


    private Log log = LogFactory.getLog(com.piramide.elwis.web.campaignmanager.form.CampaignForm.class);

    private String recordDateText;
    private String startDateText;

    public String getRecordDateText() {
        return recordDateText;
    }

    public void setRecordDateText(String recordDateText) {
        this.recordDateText = recordDateText;
    }

    public String getStartDateText() {
        return startDateText;
    }

    public void setStartDateText(String startDateText) {
        this.startDateText = startDateText;
    }

    /**
     * Validate emplyee form (date)
     *
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("Campaign Validate has been executed...");
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);
        if (errors.isEmpty()) {
            if ("".equals(getDto("budgetCost").toString().trim())) {
                setDto("budgetCost", "0");
            }

            String str = (String) getDto("typeId");
            if (null != str && !"".equals(str.trim())) {
                setDto("typeId", str);
            } else {
                setDto("typeId", null);
            }
        }
        return errors;
    }
}
