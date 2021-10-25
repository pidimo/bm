<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.contact.incomingInvoice.list" scope="request"/>

<c:set var="windowTitle" value="Finance.IncomingInvoice.Title.singleSearch" scope="request"/>
<c:set var="pagetitle" value="Finance.incomingInvoice.plural" scope="request"/>
<c:set var="listTableWidth" value="95%" scope="request"/>
<c:set var="fromFinance" value="false" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/finance/IncomingInvoiceList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/IncomingInvoiceList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>