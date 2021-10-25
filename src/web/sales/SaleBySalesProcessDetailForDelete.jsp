<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.delete" scope="request"/>

<fmt:message var="title" key="Sale.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/SalesProcess/Sale/Delete.do" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="windowTitle" value="Sale.Title.delete" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/SaleBySalesProcess.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/SaleBySalesProcess.jsp"  scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>