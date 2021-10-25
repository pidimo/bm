<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<%
    String locale = request.getParameter("locale");
    Locale defaultLocale = request.getLocale();
    if (locale != null) {
        defaultLocale = new Locale(locale);
    }
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
    pageContext.setAttribute("language", defaultLocale.getLanguage());
%>
{
	Login: "<%= JSPHelper.getMessage(defaultLocale, "Logon.Title")%>",
	Username: "<%= JSPHelper.getMessage(defaultLocale, "Logon.username")%>",
	Password: "<%= JSPHelper.getMessage(defaultLocale, "Logon.password")%>",
	Company: "<%= JSPHelper.getMessage(defaultLocale, "Logon.passwordCia")%>"
}