package com.piramide.elwis.web.common.taglib;

/**
 * Calendar script initializer
 *
 * @author Fernando Montaño
 * @version $Id: CalendarInitialize.java 9124 2009-04-17 00:35:24Z fernando $
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class CalendarInitialize extends TagSupport {

    public int doEndTag() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        try {
            StringBuffer script = new StringBuffer();

            script.append("<script>\n")
                    .append("function openCalendarPicker(field) {\n")
                    .append("   var winx = (screen.width)/2;\n")
                    .append("   var winy = (screen.height)/2;\n")
                    .append("   var posx = winx - 244/2;\n")
                    .append("   var posy = winy - 190/2;\n")
                    .append("   calendarWindow= window.open('")
                    .append(response.encodeURL(request.getContextPath() + "/calendar.html"))
                    .append("?date=' + ")
                    .append(" field.value + ")
                    .append(" '&field='")
                    .append(" + field.id, 'calendarWindow', 'resizable=no,width=244,height=190,left='+ posx +',top='+ posy);\n")
                    .append("calendarWindow.focus();")
                    .append("\n}\n")
                    .append("</script>");

            pageContext.getOut().print(new String(script));

        } catch (IOException _ex) {
        }
        return 6;
    }

    public CalendarInitialize() {
    }


}