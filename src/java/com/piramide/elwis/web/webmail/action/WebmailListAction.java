package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.WebmailReadCmd;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: WebmailListAction.java 10028 2010-12-19 05:49:15Z ivan ${CLASS_NAME}.java,v 1.2 14-02-2005 10:36:59 AM ivan Exp $
 */
public class WebmailListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        WebmailReadCmd webmailReadCmd = new WebmailReadCmd();
        webmailReadCmd.putParam("userId", userId);
        webmailReadCmd.putParam("companyId", companyId);

        ResultDTO resultDTO = BusinessDelegate.i.execute(webmailReadCmd, request);

        ActionForward forward;
        if (resultDTO.get(UserMailDTO.KEY_USERMAILID) != null) {
            addStaticFilter(UserMailDTO.KEY_USERMAILID, resultDTO.get(UserMailDTO.KEY_USERMAILID).toString());

            disableSearchExecution((SearchForm) form);

            forward = super.execute(mapping, form, request, response);
        } else {
            request.setAttribute("hideMailConfigurationTabs", Boolean.valueOf(true));
            forward = mapping.findForward("NoUserMail");
        }

        return forward;
    }

    private void disableSearchExecution(SearchForm searchForm) {
        if (isSelectAndNewButtonPressed(searchForm)) {
            searchForm.getPageParams().clear();
            searchForm.getParams().clear();
        }
    }

    private boolean isSelectAndNewButtonPressed(SearchForm searchForm) {
        String selectAndNewButton = (String) searchForm.getParameter("selectAndNewButton");
        return "true".equals(selectAndNewButton);
    }
}
