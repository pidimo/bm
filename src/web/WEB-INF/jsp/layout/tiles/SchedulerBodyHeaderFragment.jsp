<%@ include file="/Includes.jsp" %>

<div class="container">
  <h4>
    <fmt:message key="Scheduler.user.viewCalendar"/><c:if test="${not empty userViewName}">:</c:if>
    <c:out value="${userViewName}" escapeXml="false"/>
  </h4>
</div>
