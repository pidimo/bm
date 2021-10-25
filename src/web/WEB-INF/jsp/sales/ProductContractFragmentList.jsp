<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<!-- Defines the edit url, should not have '/' at start and must end in '.do' -->
<c:if test="${empty editUrl}">
    <c:set var="editUrl" value="ProductContractBySalePosition/Forward/Update.do" scope="request"/>
</c:if>

<!-- Defines the delete url, should not have '/' at start and must end in '.do' -->
<c:if test="${empty deleteUrl}">
    <c:set var="deleteUrl" value="ProductContractBySalePosition/Forward/Delete.do" scope="request"/>
</c:if>

<!--Used to define list action by default is 'SalePosition/List' in contact struts-config.xml-->
<c:if test="${empty listUrl}">
    <c:set var="listUrl" value="ProductContractBySalePosition/List.do" scope="request"/>
</c:if>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>

<app:url value="ProductContractBySalePosition/List.do" var="urlList" enableEncodeURL="false"/>

<div class="table-responsive">
    <fanta:table mode="bootstrap" action="${listUrl}"
                 imgPath="${baselayout}"
                 width="100%"
                 id="productContract"
                 list="productContractSingleList"
                 withContext="false"
                 styleClass="${app2:getFantabulousTableClases()}">

        <app:url var="urlUpdate"
                 value="${editUrl}?salePositionId=${param.salePositionId}&dto(salePositionId)=${param.salePositionId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&productName=${app2:encode(param.productName)}&customerName=${app2:encode(param.customerName)}"/>
        <app:url var="urlDelete"
                 value="${deleteUrl}?salePositionId=${param.salePositionId}&dto(salePositionId)=${param.salePositionId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&productName=${app2:encode(param.productName)}&customerName=${app2:encode(param.customerName)}&dto(withReferences)=true"/>

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                <fanta:actionColumn name="update" title="Common.update" useJScript="true"
                                    action="javascript:goParentURL('${urlUpdate}')"
                                    styleClass="listItem" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="DELETE">
                <fanta:actionColumn name="delete" title="Common.delete" useJScript="true"
                                    action="javascript:goParentURL('${urlDelete}')"
                                    styleClass="listItem" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="addressName" useJScript="true"
                          action="javascript:goParentURL('${urlUpdate}')"
                          title="Contract.contact"
                          styleClass="listItem"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="40"
                          width="21%"/>
        <fanta:dataColumn name="price"
                          styleClass="listItemRight"
                          title="Contract.price"
                          headerStyle="listHeader"
                          orderable="true"
                          renderData="false"
                          width="12%">
            <fmt:formatNumber var="priceFormattedValue" value="${productContract.price}" type="number"
                              pattern="${numberFormat}"/>
            ${priceFormattedValue}
        </fanta:dataColumn>
        <fanta:dataColumn name="payMethod"
                          styleClass="listItem"
                          title="Contract.payMethod"
                          headerStyle="listHeader"
                          orderable="true"
                          renderData="false"
                          width="14%">
            ${app2:searchLabel(payMethodList, productContract.payMethod)}
        </fanta:dataColumn>
        <fanta:dataColumn name="openAmount"
                          styleClass="listItemRight"
                          title="Contract.openAmount"
                          headerStyle="listHeader"
                          orderable="true"
                          renderData="false"
                          width="17%">
            <fmt:formatNumber var="openAmountFormattedValue" value="${productContract.openAmount}"
                              type="number"
                              pattern="${numberFormat}"/>
            <c:if test="${productContract.payMethod != PERIODIC_PAYMETHOD}">
                ${openAmountFormattedValue}
            </c:if>
        </fanta:dataColumn>
        <fanta:dataColumn name="contractTypeName"
                          styleClass="listItem"
                          title="Contract.contractType"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="40"
                          width="16%"/>
        <fanta:dataColumn name="orderDate"
                          styleClass="listItem2"
                          title="Contract.orderDate"
                          headerStyle="listHeader"
                          orderable="true"
                          renderData="false"
                          width="14%">
            <fmt:formatDate var="orderDateValue" value="${app2:intToDate(productContract.orderDate)}"
                            pattern="${datePattern}"/>
            ${orderDateValue}&nbsp;
        </fanta:dataColumn>
    </fanta:table>
</div>