<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoice.reminder.edit" scope="request"/>

<fmt:message var="title" key="InvoiceReminder.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/InvoiceReminder/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="InvoiceReminder.Title.update" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/finance/InvoiceReminder.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/InvoiceReminder.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>