package com.piramide.elwis.web.bmapp.action;

import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.web.productmanager.action.DownloadProductImageAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class DownloadCompanyLogoRESTAction extends DownloadProductImageAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  DownloadCompanyLogoRESTAction..." + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;

        String companyLogin = (String) defaultForm.getDto("companyLogin");
        Integer logoId = getCompanyLogoId(companyLogin, request);

        if (logoId != null) {
            defaultForm.setDto("freeTextId", logoId);
        } else {
            return mapping.findForward("Fail");
        }

        return super.execute(mapping, defaultForm, request, response);
    }

    private Integer getCompanyLogoId(String companyLogin, HttpServletRequest request) {
        Integer logoId = null;

        if (companyLogin != null) {
            CompanyReadCmd companyReadCmd = new CompanyReadCmd();
            companyReadCmd.setOp("getCompanyByLogin");
            companyReadCmd.putParam("login", companyLogin);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadCmd, request);
                if (!resultDTO.isFailure() && resultDTO.get("logoId") != null) {
                    logoId = new Integer(resultDTO.get("logoId").toString());
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd CompanyReadCmd", e);
            }
        }

        return logoId;
    }

}
