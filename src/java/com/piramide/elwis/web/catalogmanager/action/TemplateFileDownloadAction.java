package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         Date: Jun 19, 2006
 *         Time: 12:12:53 PM
 */
public class TemplateFileDownloadAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        Integer templateId = null;
        try {

            templateId = new Integer(request.getParameter("dto(templateId)"));

            errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_TEMPLATE, "templateid",
                    templateId, errors, new ActionError("template.NotFound"));

            if (!errors.isEmpty()) {
                return getForward("MainSearch", errors, request, mapping);
            }
        } catch (NumberFormatException nfe) {
            errors.add("", new ActionError("template.NotFound"));
            return getForward("MainSearch", errors, request, mapping);
        }

        Integer fileId = null;
        try {

            fileId = new Integer(request.getParameter("dto(fid)"));
            errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_FREETEXT, "freetextid",
                    fileId, errors, new ActionError("Document.error.notfound"));
            if (!errors.isEmpty()) {
                return getForward("Fail", errors, request, mapping);
            }
        } catch (NumberFormatException nfe) {
        }


        String url = request.getContextPath()
                + "/catalogs/Download.do?dto(type)=temp&dto(templateId)="
                + templateId
                + "&dto(fid)="
                + fileId;

        String jscriptURL = "onLoad=\"location.href ='" + response.encodeRedirectURL(url) + "'\"";
        request.setAttribute("jsLoad", jscriptURL);

        return mapping.findForward("ToList");
    }

    private ActionForward getForward(String forward,
                                     ActionErrors errors,
                                     HttpServletRequest request,
                                     ActionMapping mapping) {
        saveErrors(request, errors);
        return mapping.findForward(forward);
    }
}
