<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.contact.additionalAddress.delete" scope="request"/>

<fmt:message var="title" key="AdditionalAddress.title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/AdditionalAddress/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="windowTitle" value="AdditionalAddress.title.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/contacts/AdditionalAddress.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/AdditionalAddress.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>