package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.productmanager.el.Functions;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.1
 */
public class ProductAdvancedListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing ProductAdvancedListAction..." + request.getParameterMap());

        SearchForm listForm = (SearchForm) form;

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }

        //add filters
        Integer eventTypeId = Functions.findProductTypeIdOfEventType(request);
        Object typeId = listForm.getParameter("typeId");

        if (typeId != null && !GenericValidator.isBlankOrNull(typeId.toString()) && Integer.valueOf(typeId.toString()).equals(eventTypeId)) {

            Object eventDateFrom = listForm.getParameter("eventDateFrom");
            Object eventDateTo = listForm.getParameter("eventDateTo");

            if (eventDateFrom != null && !GenericValidator.isBlankOrNull(eventDateFrom.toString())) {
                Long eventDateTimeFrom = getMillis(new Integer(eventDateFrom.toString()) , dateTimeZone);
                listForm.setParameter("eventDateTimeFrom", eventDateTimeFrom.toString());
            }

            if (eventDateTo != null && !GenericValidator.isBlankOrNull(eventDateTo.toString())) {
                DateTime dateTimeTo = DateUtils.integerToDateTime(new Integer(eventDateTo.toString()), dateTimeZone).withTime(23, 59, 59, 0);

                Long eventDateTimeTo = dateTimeTo.getMillis();
                listForm.setParameter("eventDateTimeTo", eventDateTimeTo.toString());
            }
        }

        return super.execute(mapping, listForm, request, response);
    }

    private Long getMillis(Integer date, DateTimeZone dateTimeZone) {
        DateTime dateTime = DateUtils.integerToDateTime(date, dateTimeZone);
        return dateTime.getMillis();
    }
}
