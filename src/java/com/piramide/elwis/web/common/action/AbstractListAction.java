package com.piramide.elwis.web.common.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import org.alfacentauro.fantabulous.web.action.FantabulousAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Set defaults values for lists, such rowsPerPage , companyId, and then execute list.
 *
 * @author Tayes
 * @version $Id: AbstractListAction.java 7936 2007-10-27 16:08:39Z fernando $
 */
public abstract class AbstractListAction extends FantabulousAction {
    protected static Log log = LogFactory.getLog(AbstractListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Master ListAction");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        log.debug("User Values:" + user.getValueMap());
        addFilter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        setUser(user.getValue(Constants.USERID).toString());
        return super.execute(mapping, form, request, response);
    }
}
