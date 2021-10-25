<%@ include file="/Includes.jsp" %>

<c:remove var="index" scope="session"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/webmail/MailBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/webmail/MailBody.jsp"/>
    </c:otherwise>
</c:choose>