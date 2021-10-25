<%@ include file="/Includes.jsp" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/contacts/AddColumn.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/contacts/AddColumn.jsp"/>
    </c:otherwise>
</c:choose>
