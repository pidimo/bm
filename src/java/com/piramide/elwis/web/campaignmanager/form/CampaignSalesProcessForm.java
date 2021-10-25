package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.campaignmanager.CampaignContactSalesProcessReadCmd;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignSalesProcessForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignSalesProcessForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(CampaignSalesProcessForm.class);

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);// validating with super class

        if (errors.isEmpty()) {
            try {
                Integer startDate = (Integer) getDto("startDate");
                Integer endDate = (Integer) getDto("endDate");
                if (startDate.intValue() > endDate.intValue()) {
                    errors.add("startDate", new ActionError("Common.greaterThan", JSPHelper.getMessage(request,
                            "SalesProcess.endDate"), JSPHelper.getMessage(request, "SalesProcess.startDate")));
                }
            } catch (ClassCastException e) {
                //some of both are empty
            }
        }

        if (errors.isEmpty()) {
            CampaignContactSalesProcessReadCmd cmd = new CampaignContactSalesProcessReadCmd();
            cmd.putParam("activityId", getDto("activityId"));
            cmd.putParam("op", "readAll");

            ResultDTO resultDto = new ResultDTO();
            try {
                resultDto = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
            }
            if (resultDto.containsKey("hasProcess")) {
                errors.add("empty", new ActionError("Campaign.activity.salesProcess.notPosibleCreate"));
            }
        }

        return errors;
    }
}
