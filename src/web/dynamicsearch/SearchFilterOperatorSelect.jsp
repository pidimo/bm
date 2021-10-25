<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:choose>
  <c:when test="${sessionScope.isBootstrapUI}">
    <c:import url="/WEB-INF/jsp/dynamicsearch/SearchFilterOperatorSelect.jsp"/>
  </c:when>
  <c:otherwise>
    <c:import url="/common/dynamicsearch/SearchFilterOperatorSelect.jsp"/>
  </c:otherwise>
</c:choose>