<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<%
    response.setContentType("text/xml");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "max-age=0");
%>
<%--<?xml version="1.0" encoding="UTF-8"?>--%>
<cal:DailyOverviewAJAXResponse/>