<%@ include file="/Includes.jsp" %>

<c:choose>
  <c:when test="${sessionScope.isBootstrapUI}">
    <c:import url="/WEB-INF/jsp/campaign/SendResponsibleTelecom.jsp"/>
  </c:when>

  <c:otherwise>
    <c:import url="/common/campaign/SendResponsibleTelecom.jsp"/>
  </c:otherwise>
</c:choose>