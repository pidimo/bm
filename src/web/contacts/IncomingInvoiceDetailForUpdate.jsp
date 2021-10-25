<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.incomingInvoice.edit" scope="request"/>

<fmt:message  var="title" key="Finance.incomingInvoice.update" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/IncomingInvoice/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Finance.incomingInvoice.update" scope="request"/>
<c:set var="pagetitle" value="Finance.incomingInvoice.plural" scope="request"/>
<c:set var="fromContacts" value="true" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/finance/IncomingInvoice.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/IncomingInvoice.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>