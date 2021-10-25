<%@ include file="/Includes.jsp" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/webmail/WebmailNotifierFragment.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/webmail/WebmailNotifierFragment.jsp"/>
    </c:otherwise>
</c:choose>