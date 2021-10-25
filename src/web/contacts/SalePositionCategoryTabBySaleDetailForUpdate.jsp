<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salePosition.categoryTab" scope="request"/>

<fmt:message var="title" key="SalePosition.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action"
       value="/Sale/SalePosition/CategoryTab/Update.do?dto(salePositionId)=${param.salePositionId}&dto(categoryTabId)=${param.categoryTabId}&categoryTabId=${param.categoryTabId}"
       scope="request"/>

<c:set var="ajaxAction" value="/contacts/SalePosition/ReadSubCategories.do" scope="request"/>

<c:set var="downloadAction"
       value="/contacts/Sale/SalePosition/DownloadCategoryFieldValue.do?dto(salePositionId)=${param.salePositionId}&salePositionId=${param.salePositionId}&dto(categoryTabId)=${param.categoryTabId}&categoryTabId=${param.categoryTabId}&index=${param.index}"
       scope="request"/>

<c:set var="showCancelButton" value="true" scope="request"/>

<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="SalePosition.Title.update" scope="request"/>
<c:choose>

    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/CategoryTabUtil.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/CategoryTabUtil.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>