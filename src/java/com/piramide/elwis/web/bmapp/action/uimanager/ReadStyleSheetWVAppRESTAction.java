package com.piramide.elwis.web.bmapp.action.uimanager;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action to read wvapp styles configuration
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.2
 */
public class ReadStyleSheetWVAppRESTAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing  ReadStyleSheetWVAppRESTAction..." + request.getParameterMap());

        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());
        Integer userId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        //read wvapp styles
        StyleSheetWVAppUtil styleSheetWVAppUtil = new StyleSheetWVAppUtil();

        request.setAttribute("wvappCompanyStylesList", styleSheetWVAppUtil.readWVAppCompanyStyles(companyId));
        request.setAttribute("wvappUserStylesList", styleSheetWVAppUtil.readWVAppUserStyles(companyId, userId));

        return mapping.findForward("Success");
    }
}
