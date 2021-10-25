package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.CompanyDeleteCmd;
import com.piramide.elwis.web.admin.delegate.DeleteCompanyDelegate;
import com.piramide.elwis.web.admin.form.CompanyDeleteForm;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CompanyDeleteAction extends DefaultAction {
    private Log log = LogFactory.getLog(CompanyDeleteAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        CompanyDeleteForm companyForm = (CompanyDeleteForm) form;
        Integer companyIdToDelete = Integer.valueOf(
                companyForm.getDto("companyId").toString());
        String companyName = companyForm.getDto("name1") + " " + companyForm.getDto("name2");

        CompanyDeleteCmd cmd = new CompanyDeleteCmd();
        cmd.setOp("checkCompanyForDelete");
        cmd.putParam("companyId", companyIdToDelete);
        cmd.putParam("companyName", companyName);
        try {
            ResultDTO result = BusinessDelegate.i.execute(cmd, request);
            if ("Fail".equals(result.getForward())) {
                Iterator it = result.getResultMessages();
                while (it.hasNext()) {
                    ResultMessage message = (ResultMessage) it.next();
                    errors.add("Messages", new ActionError(message.getKey(), message.getParams()));
                }
            }
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("Fail");
            }
        } catch (AppLevelException e) {
            e.printStackTrace();
        }

        boolean successFul = DeleteCompanyDelegate.i.deleteCompany(companyIdToDelete);
        if (!successFul) {
            log.error("Cannot delete Company companyId=" + companyIdToDelete +
                    ", Because has it unknown references");
            errors.add("companyError", new ActionError("Company.error.delete", companyName));
            saveErrors(request, errors);

            return mapping.findForward("Fail");
        }


        return mapping.findForward("Success");
    }
}
