<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<!-- Defines the edit url, should not have '/' at start and must end in '.do' -->
<c:if test="${empty editUrl}">
    <c:set var="editUrl" value="SalePosition/Forward/Update.do" scope="request"/>
</c:if>

<!-- Defines the delete url, should not have '/' at start and must end in '.do' -->
<c:if test="${empty deleteUrl}">
    <c:set var="deleteUrl" value="SalePosition/Forward/Delete.do" scope="request"/>
</c:if>

<!--Used when a list is called from an iframe by default is false-->
<c:if test="${empty useJavaScript || 'false' == useJavaScript}">
    <c:set var="useJavaScript" value="false" scope="request"/>
</c:if>

<!--Used to define list action by default is 'SalePosition/List' in contact struts-config.xml-->
<c:if test="${empty listUrl}">
    <c:set var="listUrl" value="SalePosition/List.do" scope="request"/>
</c:if>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<fanta:table list="salePositionList"
             width="100%"
             id="salePosition"
             action="${listUrl}"
             imgPath="${baselayout}"
             align="center">

    <c:if test="${'false' == useJavaScript}">
        <c:set var="editLink"
               value="${editUrl}?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(productName)=${app2:encode(salePosition.productName)}"/>
        <c:set var="deleteLink"
               value="${deleteUrl}?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(productName)=${app2:encode(salePosition.productName)}&dto(withReferences)=true"/>
        <c:set var="editAction" value="${editLink}"/>
        <c:set var="deleteAction" value="${deleteLink}"/>
    </c:if>

    <c:if test="${'true' == useJavaScript}">
        <app:url var="editLink"
                 value="${editUrl}?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(productName)=${app2:encode(salePosition.productName)}"/>
        <app:url var="deleteLink"
                 value="${deleteUrl}?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(productName)=${app2:encode(salePosition.productName)}&dto(withReferences)=true"/>
        <c:set var="editAction" value="javascript:goParentURL('${editLink}')"/>
        <c:set var="deleteAction" value="javascript:goParentURL('${deleteLink}')"/>
    </c:if>

    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
        <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
            <fanta:actionColumn name="edit"
                                title="Common.update"
                                action="${editAction}"
                                useJScript="${'true' == useJavaScript}"
                                styleClass="listItem"
                                headerStyle="listHeader"
                                width="50%"
                                image="${baselayout}/img/edit.gif"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="SALEPOSITION" permission="DELETE">
            <fanta:actionColumn name="delete"
                                title="Common.delete"
                                action="${deleteAction}"
                                useJScript="${'true' == useJavaScript}"
                                styleClass="listItem"
                                headerStyle="listHeader"
                                width="50%"
                                image="${baselayout}/img/delete.gif"/>
        </app2:checkAccessRight>
    </fanta:columnGroup>
    <fanta:dataColumn name="productName" action="${editAction}"
                      useJScript="${'true' == useJavaScript}"
                      styleClass="listItem" title="SalePosition.productName"
                      headerStyle="listHeader" width="17%" orderable="true"
                      maxLength="40"/>
    <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="SalePosition.contactPerson"
                      headerStyle="listHeader" width="17%" orderable="true"/>
    <fanta:dataColumn name="unitName" styleClass="listItem" title="SalePosition.unitName"
                      headerStyle="listHeader" width="9%" orderable="true"
                      maxLength="40"/>

    <fanta:dataColumn name="quantity"
                      styleClass="listItemRight"
                      title="SalePosition.quantity"
                      headerStyle="listHeader"
                      width="9%"
                      orderable="true"
                      renderData="false">
        <fmt:formatNumber var="quantityFormatted" value="${salePosition.quantity}" type="number"
                          pattern="${numberFormat}"/>
        ${quantityFormatted}
    </fanta:dataColumn>


    <fanta:dataColumn name="unitPrice" styleClass="listItemRight" title="SalePosition.unitPrice"
                      headerStyle="listHeader" width="11%" orderable="false"
                      renderData="false">
        <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
        <c:choose>
            <c:when test="${null != salePosition.saleId}">
                <c:if test="${null == salePosition.netGross}">
                    <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
                </c:if>
                <c:if test="${null != salePosition.netGross && netPrice == salePosition.netGross}">
                    <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
                </c:if>
                <c:if test="${null != salePosition.netGross && grossPrice == salePosition.netGross}">
                    <c:set var="unitPriceValue" value="${salePosition.unitPriceGross}"/>
                </c:if>
            </c:when>
            <c:otherwise>
                <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
            </c:otherwise>
        </c:choose>

        <fmt:formatNumber var="unitPriceFormatted" value="${unitPriceValue}" type="number"
                          pattern="${numberFormat4Decimal}"/>
        ${unitPriceFormatted}
    </fanta:dataColumn>
    <fanta:dataColumn name="totalPrice" styleClass="listItemRight" title="SalePosition.totalPrice"
                      headerStyle="listHeader" width="11%" orderable="false"
                      renderData="false">
        <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
        <c:choose>
            <c:when test="${null != salePosition.saleId}">
                <c:if test="${null == salePosition.netGross}">
                    <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
                </c:if>
                <c:if test="${null != salePosition.netGross && netPrice == salePosition.netGross}">
                    <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
                </c:if>
                <c:if test="${null != salePosition.netGross && grossPrice == salePosition.netGross}">
                    <c:set var="totalPriceValue" value="${salePosition.totalPriceGross}"/>
                </c:if>
            </c:when>
            <c:otherwise>
                <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
            </c:otherwise>
        </c:choose>

        <fmt:formatNumber var="totalPriceFormatted" value="${totalPriceValue}"
                          type="number"
                          pattern="${numberFormat}"/>
        ${totalPriceFormatted}
    </fanta:dataColumn>

    <fanta:dataColumn name="deliveryDate"
                      styleClass="listItem"
                      title="SalePosition.deliveryDate"
                      headerStyle="listHeader"
                      width="11%"
                      orderable="true"
                      renderData="false">
        <fmt:formatDate var="deliveryDateValue" value="${app2:intToDate(salePosition.deliveryDate)}"
                        pattern="${datePattern}"/>
        ${deliveryDateValue}
    </fanta:dataColumn>
    <fanta:dataColumn name="active" styleClass="listItem2" title="SalePosition.active"
                      headerStyle="listHeader" width="10%" renderData="false">
        <c:if test="${salePosition.active == 1}">
            <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>
        </c:if>&nbsp;
    </fanta:dataColumn>
</fanta:table>

