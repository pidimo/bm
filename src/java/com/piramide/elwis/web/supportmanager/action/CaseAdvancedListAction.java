package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
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
 * Time: 12:04:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class CaseAdvancedListAction extends ListAction {
    //private Log log = LogFactory.getLog(CaseAdvancedListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("...  CaseAdvancedListAction  EXECUTE  ...");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        log.debug("PARAm-T:" + request.getParameter("t"));
        if (request.getParameter("t") == null) {
            log.debug("normal list");
            addFilter("onlyOpenCase", "true");
        } else {
            addFilter("ofCompany", "true");
        }

        return super.execute(mapping, searchForm, request, response);
    }
}
