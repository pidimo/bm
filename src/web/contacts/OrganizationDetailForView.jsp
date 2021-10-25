<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


<fmt:message var="title" key="Organization.Title.view" scope="request"/>

<%--<fmt:message   var="button" key="Common.delete" scope="request"/>--%>
<c:set var="action" value="/Organization/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="onlyView" value="true" scope="request"/>


<c:set var="windowTitle" value="Organization.Title.view" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>
<%--
<c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
--%>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/Organization.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/Organization.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>