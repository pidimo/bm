package com.piramide.elwis.web.dashboard.action;

import com.piramide.elwis.service.exception.webmail.CheckOutEmailException;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 2:55:05 PM
 */
public class DrawComponentAction extends com.piramide.elwis.web.dashboard.component.web.struts.action.DrawComponentAction {

    private Log log = LogFactory.getLog(DrawComponentAction.class);


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        int componentId = new Integer(request.getParameter("componentId"));

        Component xmlComponent = Builder.i.findComponentById(componentId);

        if ("inbox".equals(xmlComponent.getName())) {
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            int companyId = new Integer(user.getValue("companyId").toString());
            int userId = new Integer(user.getValue("userId").toString());
            String errorMSG = getInboxMails(userId, request, xmlComponent);
            if (!"".equals(errorMSG)) {
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                try {
                    PrintWriter writer = response.getWriter();

                    writer.write(errorMSG);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return null;
            }
        }
        return super.execute(mapping, form, request, response);
    }

    public Map dataBaseReadParameters(HttpServletRequest request) {
        Map dataBaseReadParameters = new HashMap();
        dataBaseReadParameters.put("dbComponentId", request.getParameter("dbComponentId"));
        dataBaseReadParameters.put("dashboardContainerId", request.getParameter("dashboardContainerId"));
        dataBaseReadParameters.put("componentId", request.getParameter("componentId"));

        return dataBaseReadParameters;
    }

    public ActionServlet getActionServlet() {
        return getServlet();
    }

    public String dbComponentIdParameterName() {
        return "dbComponentId";
    }

    public String dbComponentIdParameterValue(HttpServletRequest request) {
        return request.getParameter("dbComponentId");
    }

    public String getApplicationResourcesPath() {
        return "com.piramide.elwis.web.resources.ApplicationResources";
    }

    public String getContainerParameterName() {
        return "dashboardContainerId";
    }

    public String getContainerParameterValue(HttpServletRequest request) {
        return request.getParameter("dashboardContainerId");
    }

    public Locale getLocale(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Locale l;
        if (null != user.getValue("locale")) {
            l = new Locale(user.getValue("locale").toString());
        } else {
            l = new Locale("en");
        }
        return l;
    }

    public boolean hideIcons(HttpServletRequest request) {
        boolean showIcons = false;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        String functionality = "DASHBOARD";
        String permission = "UPDATE";

        if (user.getSecurityAccessRights().containsKey(functionality)) {
            Byte accessRight = (Byte) user.getSecurityAccessRights().get(functionality);
            if (PermissionUtil.hasAccessRight(accessRight, permission)) {
                showIcons = true;
            }
        }
        return showIcons;
    }

    public Map getSearchParameters(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int companyId = new Integer(user.getValue("companyId").toString());
        int userId = new Integer(user.getValue("userId").toString());
        int userAddressId = new Integer(user.getValue("userAddressId").toString());
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Map params = new HashMap();
        params.put("companyId", companyId);
        params.put("userId", userId);
        params.put("userMailId", userId);
        params.put("recordUserId", userId);
        params.put("processEmployeeId", userAddressId);
        params.put("dateTimeZone", dateTimeZone);

        return params;
    }

    public Map getConverterParameters(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Map m = new HashMap();
        m.put("timeZone", user.getValue("dateTimeZone"));
        m.put("userType", user.getValue("userType"));
        return m;
    }

    private String getInboxMails(int userId, HttpServletRequest request, Component xmlComponent)
            throws CheckOutEmailException {
        String str = "";
        MailFormHelper mailFormHelper = new MailFormHelper();

        Integer userMailId = mailFormHelper.getUserMailId(userId, request);
        if (null == userMailId) {
            return buildErrorMessage("webmail.error.account", request, xmlComponent);
        }

        return str;
    }

    private String buildErrorMessage(String resourceKey,
                                     HttpServletRequest request,
                                     Component xmlComponent) {
        String table = "<TABLE width=\"95%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"table-layout:fixed\">";
        String tr = "<TR>";
        String td = "<TD class=\"contain\">";

        String tableEnd = "</TABLE>";
        String trEnd = "</TR>";
        String tdEnd = "</TD>";

        return table +
                tr +
                td +
                JSPHelper.getMessage(request, "dashboard.component.error",
                        JSPHelper.getMessage(request, xmlComponent.getResourceKey())) +
                tdEnd +
                trEnd +
                tr +
                td +
                JSPHelper.getMessage(request, resourceKey) +
                tdEnd +
                trEnd +
                tableEnd;
    }
}
