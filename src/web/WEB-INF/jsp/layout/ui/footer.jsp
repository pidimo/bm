<%@ include file="/Includes.jsp" %>

<div class="container text-center copyrightText">
  <hr>

  <jsp:useBean id="currentDate" class="java.util.Date" scope="page"/>
  <fmt:formatDate value="${currentDate}" pattern="yyyy" var="currentDateString"/>
  <p class="text-muted credit ${customCopyrightClass}">
    <strong><fmt:message key="Common.applicationName"/></strong> <br/>
    @@VERSION@@<br/>
    &#169; Copyright 2004 -
    <fmt:formatDate value="${currentDate}" pattern="yyyy"/><br/>
    ${app2:getConfigurationPropertyValue('elwis.copyright.html')}
  </p>
</div>

