package com.piramide.elwis.web.bmapp.action.product;

import com.piramide.elwis.web.bmapp.action.ListRESTAction;
import com.piramide.elwis.web.productmanager.el.Functions;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class ProductEventMineListRESTAction extends ListRESTAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing ProductEventMineListRESTAction..." + request.getParameterMap());

        SearchForm listForm = (SearchForm) form;

        Integer eventTypeId = Functions.findProductTypeIdOfEventType(request);
        addStaticFilter("productTypeId", (eventTypeId != null) ? eventTypeId.toString() : "-1");
        //define order desc
        listForm.setOrderParam("1", "initDateTime-false");

        return super.execute(mapping, listForm, request, response);
    }
}
