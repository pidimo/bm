<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<%
    response.setContentType("text/xml");
    response.setCharacterEncoding("UTF-8");
    //Content-Type: application/x-javascript; charset=?
    //response.setContentType("text/xml; charset=UTF-8");
    response.setHeader("Cache-Control", "max-age=0");
    /*try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }*/
%>
<%--<?xml version="1.0" encoding="UTF-8"?>--%>
<cal:DailyAJAXResponse/>