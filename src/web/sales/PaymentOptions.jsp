<%@ include file="/Includes.jsp" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/sales/PaymentOptions.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/sales/PaymentOptions.jsp"/>
    </c:otherwise>
</c:choose>