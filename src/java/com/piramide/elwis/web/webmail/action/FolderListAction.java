package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: FolderListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FolderListAction extends WebmailListAction {

    protected static Log log = LogFactory.getLog(ListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        //setting FOLDER DEFAULT into fantabulous
        addStaticFilter("folder_type", WebMailConstants.FOLDER_DEFAULT);

        return super.execute(mapping, form, request, response);
    }


}
