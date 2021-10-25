package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: CampaignFileDeleteAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignFileDeleteAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing TemplateFileDeleteAction... ");
        ActionForward forward = new ActionForward();
        DefaultForm campaignForm = (DefaultForm) form;
        if (campaignForm.getDto("cancel") != null) {
            forward = mapping.findForward("Cancel");
        } else {
            forward = super.execute(mapping, form, request, response);
        }
        return forward;
    }
}

