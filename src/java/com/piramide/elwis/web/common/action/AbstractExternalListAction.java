package com.piramide.elwis.web.common.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.action.FantabulousExternalListAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: AbstractExternalListAction.java 10522 2015-03-16 23:06:32Z miguel $
 */

public abstract class AbstractExternalListAction extends FantabulousExternalListAction {
    protected static Log log = LogFactory.getLog(AbstractExternalListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing AbstractExternalListAction....");

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        log.debug("User Values:" + user.getValueMap());
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        addConfig(ROWS_PER_PAGE, user.getValue("rowsPerPage").toString());

        if (!hasAssignedCompanyIdFilter(request)) {
            addStaticFilter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        }

        addStaticFilter(Constants.USERID, user.getValue(Constants.USERID).toString());
        setUser(user.getValue(Constants.USERID).toString());

        //set external list
        ListStructure extListStructure = getExternalListStructure();
        //add access right data level security
        extListStructure = AccessRightDataLevelSecurity.i.processAddressAccessRightByTable(extListStructure, userId);
        setListStructure(extListStructure);

        return super.execute(mapping, form, request, response);
    }

    /**
     * get list structure
     *
     * @return List structure
     */
    public abstract ListStructure getExternalListStructure();

    protected boolean hasAssignedCompanyIdFilter(HttpServletRequest request) {
        return false;
    }

}
