package com.piramide.elwis.web.common.dynamicsearch.action;

import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchExecute;
import com.piramide.elwis.web.common.dynamicsearch.form.DynamicSearchForm;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.structure.ListStructureCloneUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchListAction extends ListAction {
    protected Log log = LogFactory.getLog(this.getClass());

    private DynamicSearchForm dynamicSearchForm;
    private HttpServletRequest request;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DynamicSearchListAction....." + request.getParameterMap());

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        this.dynamicSearchForm = (DynamicSearchForm) form;
        this.request = request;

        //reset list filters
        super.initializeFilter();

        ActionForward forward = super.execute(mapping, form, request, response);
        return forward;
    }

    @Override
    public ListStructure getListStructure() throws Exception {
        return completeDynamicSearchFilters(super.getListStructure());
    }

    private ListStructure completeDynamicSearchFilters(ListStructure sourceListStructure) {
        ListStructure dynamicListStructure = cloneListStructure(sourceListStructure);
        String module = getModule(request);

        DynamicSearchExecute dynamicSearchExecute = new DynamicSearchExecute(dynamicListStructure, dynamicSearchForm, module);
        dynamicListStructure = dynamicSearchExecute.execute(request);

        addPropertyParametersAsFilter(dynamicSearchForm);
        processBeforeExecuteList(dynamicSearchForm, request);

        return dynamicListStructure;
    }

    protected void processBeforeExecuteList(DynamicSearchForm dynamicSearchForm, HttpServletRequest request) {
    }

    private ListStructure cloneListStructure(ListStructure sourceListStructure) {
        return ListStructureCloneUtil.clone(sourceListStructure);
    }

    private void addPropertyParametersAsFilter(DynamicSearchForm dynamicSearchForm) {
        Map<String, String> propertyFilterMap = dynamicSearchForm.getPropertyFilterMap();
        for (Iterator iterator = propertyFilterMap.keySet().iterator(); iterator.hasNext();) {
            String parameterName = (String) iterator.next();

            addFilter(parameterName, propertyFilterMap.get(parameterName));
        }
    }
}
