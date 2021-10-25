<%@ include file="/Includes.jsp" %>
<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/catalogs/RenderSubCategory.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/catalogs/RenderSubCategory.jsp"/>
    </c:otherwise>
</c:choose>