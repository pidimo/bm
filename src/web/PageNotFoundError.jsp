<%@ include file="/Includes.jsp" %>
<c:choose>
    <c:when test="${not empty sessionScope.user.valueMap['mobile']}">
        <c:import url="/mobile/PageNotFound.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/PageNotFound.jsp"/>
    </c:otherwise>
</c:choose>