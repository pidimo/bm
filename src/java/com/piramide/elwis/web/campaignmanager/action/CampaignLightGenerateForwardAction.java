package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Jatun S.R.L.
 * set default values in form for campaign generation
 *
 * @author Miky
 * @version $Id: CampaignLightGenerateForwardAction.java 04-may-2009 16:03:39 $
 */
public class CampaignLightGenerateForwardAction extends CampaignManagerForwardAction {
    public Log log = LogFactory.getLog(CampaignManagerForwardAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignLightGenerateForwardAction........" + request.getParameterMap());

        DefaultForm generateForm = (DefaultForm) form;

        //set telecom type as selected
        List telecomTypeList = Functions.getTelecomTypesOfEmails(request);
        if (telecomTypeList.size() > 0) {
            LabelValueBean labelValueBean = (LabelValueBean) telecomTypeList.get(0);
            generateForm.setDto("telecomTypeId", labelValueBean.getValue());
        }

        //set user addressid as sender employee id
        User user = RequestUtils.getUser(request);
        Integer userAddressId = new Integer(user.getValue("userAddressId").toString());
        generateForm.setDto("senderEmployeeId", userAddressId);

        //set default notification email
        TelecomDTO telecomDTO = com.piramide.elwis.web.contactmanager.el.Functions.findDefaultEmailTelecom(userAddressId, null);
        if (telecomDTO != null) {
            generateForm.setDto("notificationMail", telecomDTO.getData());
        }

        return super.execute(mapping, form, request, response);
    }
}
