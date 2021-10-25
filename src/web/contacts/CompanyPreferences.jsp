<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<c:set var="helpResourceKey" value="help.companySettings.preferences" scope="request"/>

<fmt:message var="title" key="Company" scope="request"/>
<fmt:message var="save" key="Common.save" scope="request"/>

<c:set var="windowTitle" value="Company" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>

<c:set var="hasCompanyUpdateAccessRight"
       value="${app2:hasAccessRight(pageContext.request,'COMPANYPREFERENCES' ,'UPDATE')}" scope="request"/>


<c:choose>
       <c:when test="${sessionScope.isBootstrapUI}">
              <c:set var="body" value="/WEB-INF/jsp/contacts/Company.jsp" scope="request"/>
              <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CompanyPreferencesTabs.jsp" scope="request"/>
              <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
       </c:when>
       <c:otherwise>
              <c:set var="tabs" value="/CompanyPreferencesTabs.jsp" scope="request"/>
              <c:set var="body" value="/common/contacts/Company.jsp" scope="request"/>
              <c:import url="${sessionScope.layout}/main.jsp"/>
       </c:otherwise>
</c:choose>
