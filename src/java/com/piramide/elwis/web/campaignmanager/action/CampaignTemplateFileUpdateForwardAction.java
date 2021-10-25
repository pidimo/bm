package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Update forward action, Additionally update img tags urls to download images
 * @author Miky
 * @version $Id: CampaignTemplateFileUpdateForwardAction.java 2009-09-24 04:11:01 PM $
 */
public class CampaignTemplateFileUpdateForwardAction extends CampaignTemplateAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignTemplateFileUpdateForwardAction........" + request.getParameterMap());

        ActionForward forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        if ("Success".equals(forward.getName())) {
            Map dtoValues = (Map) request.getAttribute("dto");

            String templateBody = (String) dtoValues.get("editor");
            if (templateBody != null) {
                //is here update url to download images
                String newTemplateBody = Functions.updateImageStoreDownloadUrlForImgTags(templateBody, response, request);
                DefaultForm defaultForm = (DefaultForm) form;
                defaultForm.setDto("editor", newTemplateBody);
            }
        }

        return forward;
    }
}
