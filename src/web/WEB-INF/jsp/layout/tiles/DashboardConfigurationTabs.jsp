<%@ include file="/Includes.jsp" %>


<c:if test="${param.category != null}">
  <c:set var="category" value="${param.category}" scope="request"/>
</c:if>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<c:choose>
  <c:when test="${category == '1' || param.category == null}">
    <c:set target="${tabItems}" property="dashboard.configuration"  value="/Dashboard/Container/Read.do"/>
  </c:when>
</c:choose>
