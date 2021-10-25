package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: ivan
 * Date: 28-10-2006: 03:22:33 PM
 */
public class CampaignTemplateFileListAction extends CampaignListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        //todo ivan
        //aqui hay un problema, ya que si ejecuta algun link del listado, y el template fue eliminado,
        //muestra el mensaje de error, sin embargo lo muestra dentro del i-frame
        String templateId = request.getParameter("parameter(templateId)");

        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN_TEMPLATE, "templateid",
                templateId, errors, new ActionError("template.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainList");
        }

        addFilter("templateId", templateId);
        return super.execute(mapping, form, request, response);
    }
}
