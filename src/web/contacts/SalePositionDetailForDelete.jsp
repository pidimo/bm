<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salePosition.delete" scope="request"/>

<fmt:message var="title" key="SalePosition.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/SalePosition/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="SalePosition.Title.delete" scope="request"/>
<c:set var="editSaleUrl" value="contacts/Sale/Forward/Update.do" scope="request"/>
<c:set var="customTabKey" value="Contacts.Tab.sale" scope="request"/>
<!--Enable Sale link-->
<c:set var="showSaleLink" value="${true}" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/SalePosition.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/SalePosition.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>