package com.piramide.elwis.web.bmapp.action.product;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.action.ListRESTAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.productmanager.el.Functions;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class ProductEventListRESTAction extends ListRESTAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing ProductEventListRESTAction..." + request.getParameterMap());

        SearchForm listForm = (SearchForm) form;

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }

        //add filters
        Long todayMillis = getTodayMillis(dateTimeZone);

        if ("true".equals(request.getParameter("isArchiveEvents"))) {
            addStaticFilter("archiveEventMillis", todayMillis.toString());
            //define order desc
            listForm.setOrderParam("1", "initDateTime-false");
        } else {
            addStaticFilter("currentEventMillis", todayMillis.toString());
            //define order asc
            listForm.setOrderParam("1", "initDateTime-true");
        }

        Integer eventTypeId = Functions.findProductTypeIdOfEventType(request);
        addStaticFilter("productTypeId", (eventTypeId != null) ? eventTypeId.toString() : "-1");

        return super.execute(mapping, listForm, request, response);
    }

    private Long getTodayMillis(DateTimeZone dateTimeZone) {
        DateTime dateTime = new DateTime(dateTimeZone).withTime(0, 0, 0, 0);
        return dateTime.getMillis();
    }
}
