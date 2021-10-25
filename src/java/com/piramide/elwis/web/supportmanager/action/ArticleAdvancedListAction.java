package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 26, 2005
 * Time: 11:33:22 AM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleAdvancedListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- ArticleAdvancedListAction      execute  ....");

        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        User user = RequestUtils.getUser(request);

        return super.execute(mapping, searchForm, request, response);
    }

    @Override
    public ListStructure getListStructure() throws Exception {
        return AccessRightDataLevelSecurity.i.processAccessRightByList(super.getListStructure(), userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration.ARTICLE_ACCESS);
    }
}