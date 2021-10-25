<%@ include file="/Includes.jsp" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/webmail/WebmailNotifierResponse.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/webmail/WebmailNotifierResponse.jsp"/>
    </c:otherwise>
</c:choose>