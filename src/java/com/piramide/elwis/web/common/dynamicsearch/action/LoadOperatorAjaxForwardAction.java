package com.piramide.elwis.web.common.dynamicsearch.action;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.form.DynamicSearchForm;
import com.piramide.elwis.web.common.dynamicsearch.util.OperatorUtil;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class LoadOperatorAjaxForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing LoadOperatorAjaxForwardAction.............." + request.getParameterMap());

        //define the default operator for this field
        SearchForm searchForm = (SearchForm) form;
        String dynamicSearchName = request.getParameter("dynamicSearchName");
        String fieldAlias = request.getParameter("fieldProperty");

        log.debug("Dynamic search name:"+dynamicSearchName);
        log.debug("field alias:" + fieldAlias);

        DynamicSearchConstants.Operator defaultOperator = OperatorUtil.getFieldDefaultOperator(dynamicSearchName, fieldAlias, request);
        if (defaultOperator != null) {
            searchForm.setParameter(DynamicSearchForm.composeOperatorParameterName(fieldAlias), defaultOperator.getConstant());
        }

        return super.execute(mapping, searchForm, request, response);
    }

}
