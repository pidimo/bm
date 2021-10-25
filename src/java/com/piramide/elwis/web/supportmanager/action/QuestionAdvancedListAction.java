package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.utils.SupportConstants;
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
 * Date: Nov 4, 2005
 * Time: 11:28:53 AM
 * To change this template use File | Settings | File Templates.
 */

public class QuestionAdvancedListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- QuestionAdvancedListAction  execute  ....");

        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        if ("0".equals(request.getParameter("parameter(answer)"))) {
            addFilter("createQuestion", SupportConstants.TRUE_VALUE);
        } else if ("1".equals(request.getParameter("parameter(answer)"))) {
            addFilter("noCreateByQuestion", SupportConstants.TRUE_VALUE);
        }

        return super.execute(mapping, searchForm, request, response);
    }


}