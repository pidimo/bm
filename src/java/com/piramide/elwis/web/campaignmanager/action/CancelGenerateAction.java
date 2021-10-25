package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignDocumentGenerateCmd;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CancelGenerateAction.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class CancelGenerateAction extends Action {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("CANCEL ACTION: EXECUTE....................................");
        request.setAttribute("jsLoad", "onLoad=\"window.close()\"");
        CampaignDocumentGenerateCmd.stop.put(request.getSession().getId(), new Boolean(true));
        return mapping.findForward("Cancel");
    }

}
