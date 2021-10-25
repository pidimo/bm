<%@ include file="/Includes.jsp" %>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/webmail/WebmailListFragment.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/webmail/WebmailListFragment.jsp"/>
    </c:otherwise>
</c:choose>