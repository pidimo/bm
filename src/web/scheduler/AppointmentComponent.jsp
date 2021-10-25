<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/JSPErrorPage.jsp" %>
<%@ include file="/Includes.jsp" %>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/scheduler/AppointmentComponent.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/scheduler/AppointmentComponent.jsp"/>
    </c:otherwise>
</c:choose>