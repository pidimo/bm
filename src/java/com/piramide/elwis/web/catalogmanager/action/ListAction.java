package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan
 * @version $Id: ListAction.java 1066 2004-06-05 14:20:27Z mauren $
 */
public class ListAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Master ListAction");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        final DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("rowsPerPage", new Integer(user.getValue("rowsPerPage").toString()));
        defaultForm.setDto(Constants.LIST_COMPANYID, user.getValue("companyId"));
        return super.execute(mapping, form, request, response);
    }
}
