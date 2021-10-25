<%@ page import="com.piramide.elwis.web.common.util.JSPHelper" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="java.util.Locale" %>
<%
    String acceptHeader = request.getHeader("accept");

    if (acceptHeader.indexOf("application/vnd.wap.xhtml+xml") != -1) {
        response.setContentType("application/vnd.wap.xhtml+xml");
    } else {
        if (acceptHeader.indexOf("text/html") != -1) {
            response.setContentType("text/html");
        } else {
            response.setContentType("application/xhtml+xml");
        }
    }
%>