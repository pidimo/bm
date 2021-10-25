<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.salePosition.edit" scope="request"/>

<fmt:message var="title" key="SalePosition.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalePositionBySale/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="SalePosition.Title.update" scope="request"/>
<c:set var="editSaleUrl" value="contacts/Sale/Forward/Update.do" scope="request"/>
<c:set var="customTabKey" value="Contacts.Tab.sale" scope="request"/>
<c:set var="productContractListUrl" value="/ProductContractBySale/List.do" scope="request"/>
<c:set var="categoryTabLinkUrl"
       value="/contacts/Sale/SalePosition/CategoryTab/Forward/Update.do?saleId=${salePositionForm.dtoMap['saleId']}&index=${param.index}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
       scope="request"/>

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