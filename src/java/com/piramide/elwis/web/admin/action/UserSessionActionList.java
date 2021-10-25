package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 28, 2005
 * Time: 11:53:01 AM
 * To change this template use File | Settings | File Templates.
 */

public class UserSessionActionList extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- UserSessionActionList      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        User user = RequestUtils.getUser(request);
        DateTimeZone timeZone;
        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }

        Company company = null;
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        company = companyHome.findByPrimaryKey(new Integer(user.getValue("companyId").toString()));

        if (company != null) {
            if (!company.getIsDefault().booleanValue()) {
                request.setAttribute("view", SupportConstants.TRUE_VALUE);
                request.setAttribute("default", new Boolean(false));
                addFilter("noDefault", SupportConstants.TRUE_VALUE);
            } else {
                request.setAttribute("default", new Boolean(true));
            }
        }

        request.setAttribute("timeZone", timeZone);
        return super.execute(mapping, searchForm, request, response);
    }
}