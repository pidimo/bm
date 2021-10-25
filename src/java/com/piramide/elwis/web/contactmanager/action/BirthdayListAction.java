package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.persistence.PersistenceLoadStatusCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.web.FantabulousUtil;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: BirthdayListAction.java 10500 2014-10-16 00:07:59Z miguel $
 */
public class BirthdayListAction extends com.piramide.elwis.web.common.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Map myParamenters = PersistenceManager.persistence().loadStatus(user.getValue("userId").toString(),
                "birthdayList",
                PersistenceLoadStatusCmd.DEFAULT_MODULE);


        SearchForm searchForm = (SearchForm) form;

        addFilter("recordUserId", user.getValue("userId").toString());

        Integer decrement = new Integer(user.getValue("rangeBirthdayStart").toString());
        Integer increment = new Integer(user.getValue("rangeBirthdayFinish").toString());
        try {
            String n = (String) searchForm.getParameter("increment");
            String n2 = (String) searchForm.getParameter("decrement");

            if (null != myParamenters.get("increment") && null == n) {
                n = myParamenters.get("increment").toString();
            }

            if (null != myParamenters.get("decrement") && null == n2) {
                n2 = myParamenters.get("decrement").toString();
            }

            if (n != null && !"".equals(n)) {
                increment = new Integer(n);
                searchForm.setParameter("increment", n);
            } else {
                searchForm.setParameter("increment", increment.toString());
            }

            if (n2 != null && !"".equals(n2)) {
                decrement = new Integer(n2);
                searchForm.setParameter("decrement", n2);
            } else {
                searchForm.setParameter("decrement", decrement.toString());
            }

        } catch (NumberFormatException e) {
            log.debug("Number Format Exception on Birth Day Action");
            increment = new Integer(((User) request.getSession().getAttribute(Constants.USER_KEY)).getValue("rangeBirthdayStart").toString());
            decrement = new Integer(((User) request.getSession().getAttribute(Constants.USER_KEY)).getValue("rangeBirthdayFinish").toString());
        }

        String beforeDate = DateUtils.dateToInteger(getCurrentDate(dateTimeZone), increment.intValue()).toString();
        if (decrement.intValue() > 0) {
            decrement = new Integer(-1 * decrement.intValue());
        }

        String afterDate = DateUtils.dateToInteger(getCurrentDate(dateTimeZone), decrement.intValue()).toString();
        String actualDate = DateUtils.dateToInteger(getCurrentDate(dateTimeZone)).toString();

        String actualYear = actualDate.substring(0, 4);
        String firstYear = afterDate.substring(0, 4);
        String advanceYear = beforeDate.substring(0, 4);

        String cad2 = beforeDate.substring(4);
        String cad1 = afterDate.substring(4);

        if ((Integer.valueOf(firstYear).intValue() != Integer.valueOf(actualYear).intValue()) ||
                (Integer.valueOf(advanceYear).intValue() != Integer.valueOf(actualYear).intValue())) {
            addFilter("special", "true");
            addFilter("normal", null);
        } else {
            addFilter("normal", "true");
            addFilter("special", null);
        }

        if (searchForm.getOrderParams().isEmpty() && !myParamenters.isEmpty()) {
            Map myNewOrder = processOrder(myParamenters);
            if (null != myNewOrder) {
                searchForm.setOrderParams(processOrder(myParamenters));
            }
        }

        searchForm.setParameter("date2", (new Integer(cad2)).toString());
        searchForm.setParameter("date1", (new Integer(cad1)).toString());

        return super.execute(mapping, form, request, response);
    }

    private Map processOrder(Map map) {
        if (map.containsKey("PARAM_ORDER")) {
            return (FantabulousUtil.getInstance().getOrderParams(map.get("PARAM_ORDER").toString()));
        }
        return null;
    }

    private Date getCurrentDate(DateTimeZone dateTimeZone) {
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        DateTime currentDateTime = new DateTime(dateTimeZone);

        return DateUtils.integerToDate(DateUtils.dateToInteger(currentDateTime));
    }

}