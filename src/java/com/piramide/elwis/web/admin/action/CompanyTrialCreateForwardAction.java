package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.ReadTemplateCompanyCmd;
import com.piramide.elwis.web.admin.form.TrialCompanyForm;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * This class check if exists configurated one template for trial companies creation
 *
 * @author: ivan
 */
public class CompanyTrialCreateForwardAction extends AbstractDefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ReadTemplateCompanyCmd cmd = new ReadTemplateCompanyCmd();
        cmd.setOp("checkTrialTemplateByLanguage");

        ResultDTO resulDTO = BusinessDelegate.i.execute(cmd, request);
        if (resulDTO.isFailure()) {
            ActionErrors errors = new ActionErrors();
            errors.add("unavailableService", new ActionError("Common.serviceUnavailable"));
            saveErrors(request, errors);
            request.setAttribute("hasErrors", "true");
            return mapping.findForward("Fail");
        }
        DefaultForm defForm = (DefaultForm) form;

        Locale defaultLocale = request.getLocale();

        String language = defaultLocale.getLanguage();
        if (null != request.getParameter("locale")) {
            language = request.getParameter("locale");
        }

        TrialCompanyForm myForm = new TrialCompanyForm();
        myForm.setDto("favoriteLanguage", language);
        myForm.getDtoMap().putAll(defForm.getDtoMap());
        request.setAttribute("trialCompanyForm", myForm);
        return mapping.findForward("Success");
    }
}
