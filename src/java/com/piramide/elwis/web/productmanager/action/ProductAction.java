package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Action Class for interact with ProductForm.
 *
 * @author Fernando Montaño
 * @version $Id: ProductAction.java 12479 2016-02-15 20:29:29Z miguel $
 */

public class ProductAction extends DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Product Action execution...");
        if (isCancelled(request)) {
            log.debug("Operation cancelled");
            return (mapping.findForward("Cancel"));
        }
        DefaultForm productForm = (DefaultForm) form;

        ActionForward forward = super.execute(mapping, productForm, request, response);

        processAfterExecuteCmd(productForm, request);

        if (productForm.getDto("productId") != null && "create".equals(productForm.getDto("op"))) {
            return new ActionForwardParameters().
                    add("productId", productForm.getDto("productId").toString()).
                    add("dto(productId)", productForm.getDto("productId").toString()).
                    forward(forward);
        }

        return forward;

    }

    private void processAfterExecuteCmd(DefaultForm productForm, HttpServletRequest request) {
        Map resultDtoMap = (Map) request.getAttribute("dto");

        if (resultDtoMap != null) {
            DateTimeZone dateTimeZone = getUserDateTimeZone(request);

            DateTime dateTimeInit = null;
            DateTime dateTimeEnd = null;
            DateTime dateTimeClosing = null;

            if (resultDtoMap.get("initDateTime") != null) {
                dateTimeInit = new DateTime((Long) resultDtoMap.get("initDateTime"), dateTimeZone);
            }
            if (resultDtoMap.get("endDateTime") != null) {
                dateTimeEnd = new DateTime((Long) resultDtoMap.get("endDateTime"), dateTimeZone);
            }
            if (resultDtoMap.get("closingDateTime") != null) {
                dateTimeClosing = new DateTime((Long) resultDtoMap.get("closingDateTime"), dateTimeZone);
            }

            if (dateTimeInit != null) {
                productForm.setDto("initDate", (DateUtils.dateToInteger(dateTimeInit)));
                productForm.setDto("initHour", dateTimeInit.getHourOfDay());
                productForm.setDto("initMin", (dateTimeInit.getMinuteOfHour() < 10 ? "0" :"") + dateTimeInit.getMinuteOfHour());
            }

            if (dateTimeEnd != null) {
                productForm.setDto("endDate", (DateUtils.dateToInteger(dateTimeEnd)));
                productForm.setDto("endHour", dateTimeEnd.getHourOfDay());
                productForm.setDto("endMin", (dateTimeEnd.getMinuteOfHour() < 10 ? "0" :"") + dateTimeEnd.getMinuteOfHour());
            }

            if (dateTimeClosing != null) {
                productForm.setDto("closeDate", (DateUtils.dateToInteger(dateTimeClosing)));
                productForm.setDto("closeHour", dateTimeClosing.getHourOfDay());
                productForm.setDto("closeMin", (dateTimeClosing.getMinuteOfHour() < 10 ? "0" :"") + dateTimeClosing.getMinuteOfHour());
            }
        }

    }

    private DateTimeZone getUserDateTimeZone(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        return dateTimeZone;
    }
}

