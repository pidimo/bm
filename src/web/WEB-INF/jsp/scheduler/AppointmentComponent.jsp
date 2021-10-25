<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/JSPErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>
<%@ include file="/Includes.jsp" %>
<%--wz_tooltip.js always the end--%>


<c:set var="recentList" value='<%=request.getParameter("rowsPerPage")%>'/>
<c:set var="context" value="<%=request.getContextPath()%>"/>
<div class="table-responsive">
    <cal:AppointmentComponentBootstrap
            url=""
            todayColor="#DECACA"
            tableWidth="100%" imgPathScheduler="${baselayout}"
            addURL="Appointment/Forward/Create.do?create=true&module=${param.module}"
            modURL="/scheduler/Appointment/Forward/Update.do?update=true&module=${param.module}"
            delURL="Appointment/Forward/Delete.do?dto(withReferences)=true&module=${param.module}"
            />
</div>
<tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>



