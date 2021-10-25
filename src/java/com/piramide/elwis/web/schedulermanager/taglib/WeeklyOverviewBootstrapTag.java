package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.el.Functions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: lito
 * Date: Apr 07, 2015
 * Time: 11:12:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class WeeklyOverviewBootstrapTag extends WeeklyViewBootstrapTag {
    private Map<Integer, Byte[]> userPermisionsMap;

    public int doStartTag() throws JspException {
        userPermisionsMap = new HashMap<Integer, Byte[]>();
        initializeOverviewUserColorMap();
        return super.doStartTag();
    }

    protected void renderCellWeekAppointment(StringBuffer html, String calendarDate, AppointmentCompountView appointment, boolean chop,
                                             boolean showToolTip, String styleClass, WeeklyAppointmentUI weeklyAppointmentUI, boolean isTimeInterval) {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        boolean hasReadPermission = hasReadPermission(appointment);
        boolean asOnlyAnonymous = (!hasReadPermission);

        styleClass = composeStyleClassForAppointmentDiv(styleClass, asOnlyAnonymous, isTimeInterval);

        String otherProperties = renderBorderStyleToOverviewApp(appointment.getUserId());
        String styleAttribute = composeStyleAttributeForAppointmentDiv(appointment, asOnlyAnonymous, weeklyAppointmentUI, otherProperties);

        renderOpenDIV(html, styleClass, styleAttribute);

        renderOpenSPAN(html, SPAN_DOTDOTDOT_KEY_CSS);
        renderTimeLabelAndIcons(html, calendarDate, appointment, true, showToolTip);
        renderCloseSPAN(html);

        html.append(BR);

        StringBuffer titleApp = new StringBuffer();
        renderOpenSPAN(titleApp, "month_time");
        //add user name
        titleApp.append("<strong>[" + appointment.getOwnerUserName() + "]</strong>").append(BR);
        renderCloseSPAN(titleApp);

        if (hasReadPermission) {

            // Show the private label only if is private
            if (!appointment.isPublic()) {
                renderOpenSPAN(titleApp, "type_appoiment");
                titleApp.append("[")
                        .append(RES_PRIVATE)
                        .append("]");
                renderCloseSPAN(titleApp);
                titleApp.append(BR);
            }

            titleApp.append(chop ? renderTitle(appointment.getTitle()) : appointment.getTitle());

            if (showToolTip) {
                StringBuffer toolTipSpan = new StringBuffer();
                renderOpenSPAN(toolTipSpan, "appointment_font", renderToolTip(TOOLTIP_WITDH, appointment));
                toolTipSpan.append(titleApp);
                renderCloseSPAN(toolTipSpan);

                html.append(toolTipSpan);
            } else {
                html.append(titleApp);
            }

            html.append(SPACE);

        } else {
            html.append(titleApp);
        }

        renderCloseDIV(html);
    }

    protected void renderCellAppointment(StringBuffer html, String calendarDate, AppointmentCompountView appointment, boolean chop, boolean showToolTip, boolean withDiv, String styleClass) {

        boolean hasReadPermission = hasReadPermission(appointment);

        if (withDiv) {
            styleClass = styleClass + " appointment_font" + (hasReadPermission ? "" : " " + APP_ANONYMOUS_CSS);
            StringBuffer otherAttribs = new StringBuffer("style=\"");
            if (hasReadPermission) {
                otherAttribs.append("background: " + appointment.getColor() + ";");
            }
            otherAttribs.append(renderBorderStyleToOverviewApp(appointment.getUserId())).append("\"");

            renderOpenDIV(html, styleClass, otherAttribs.toString());
        }


        // If has reminder show the icon
        renderIcons(html, calendarDate, appointment);
        html.append(BR);
        StringBuffer timeLabel = new StringBuffer();
        renderOpenSPAN(timeLabel, "month_time");

        //add user name
        timeLabel.append("<strong>[" + appointment.getOwnerUserName() + "]</strong>").append(BR);

        timeLabel.append(!appointment.isAllDay() ? appointment.getTimeLabel(true) : RES_ALLDAY);
        renderCloseSPAN(timeLabel);
        timeLabel.append(SPACE);

        if (hasReadPermission) {

            // Show the private label only if is private
            if (!appointment.isPublic()) {
                renderOpenSPAN(timeLabel, "type_appoiment");
                timeLabel.append("[")
                        .append(RES_PRIVATE)
                        .append("]");
                renderCloseSPAN(timeLabel);
            }
            timeLabel.append(BR);
            timeLabel.append(chop ? renderTitle(appointment.getTitle()) : appointment.getTitle());

            if (showToolTip) {
                StringBuffer toolTipSpan = new StringBuffer();
                renderOpenSPAN(toolTipSpan, "appointment_font", renderToolTip(TOOLTIP_WITDH, appointment));
                toolTipSpan.append(timeLabel);
                renderCloseSPAN(toolTipSpan);

                html.append(toolTipSpan);
            } else {
                html.append(timeLabel);
            }

            html.append(SPACE);

        } else {
            html.append(timeLabel);
        }

        if (withDiv) {
            renderCloseDIV(html);
        }
    }

    protected void renderDelURL(HttpServletRequest req, StringBuffer html, String calendarDate, AppointmentCompountView appointment) {
        //we not need del url
    }

    protected void renderAddURL(HttpServletRequest req, StringBuffer html, String text, int hour, String min, String date, String calendarDate) {
        //we not need add url
    }

    private boolean hasReadPermission(AppointmentCompountView appointment) {
        //read app owner user permission
        Byte publicPermission;
        Byte privatePermission;
        if (userPermisionsMap.containsKey(appointment.getUserId())) {
            publicPermission = userPermisionsMap.get(appointment.getUserId())[0];
            privatePermission = userPermisionsMap.get(appointment.getUserId())[1];

        } else {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            User user = RequestUtils.getUser(request);
            Integer userId = (Integer) user.getValue("userId");
            publicPermission = Functions.getPublicPermission(appointment.getUserId(), userId);
            privatePermission = Functions.getPrivatePermission(appointment.getUserId(), userId);

            userPermisionsMap.put(appointment.getUserId(), new Byte[]{publicPermission, privatePermission});
        }

        return (Functions.hasPermissions(publicPermission, privatePermission, appointment.isPublic())
                && Functions.hasReadAppointmentPermission(publicPermission, privatePermission, appointment.isPublic()));
    }

}
