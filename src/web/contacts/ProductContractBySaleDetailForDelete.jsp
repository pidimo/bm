<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.productContract.delete" scope="request"/>

<fmt:message var="title" key="ProductContract.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/ProductContractBySale/Delete.do" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="ProductContract.Title.delete" scope="request"/>

<!--Define module to build ajax configuration-->
<c:set var="fromModule" value="contacts" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/ProductContract.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/ProductContract.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>