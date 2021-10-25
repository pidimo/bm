<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="saleNetGross"
       value="${app2:getNetGrossFieldFromSale(param.saleId ,pageContext.request)}"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="useNetPrice" value="${netPrice == saleNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == saleNetGross}"/>


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


<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="salePositionList"
                 width="100%"
                 styleClass="${app2:getFantabulousTableClases()}"
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

        <%--contact person links--%>
        <c:if test="${not empty salePosition.contactPersonId}">
            <app:url var="editContactPersonUrl"
                     contextRelative="true"
                     value="/contacts/ContactPerson/Forward/Update.do?contactId=${salePosition.customerId}&dto(addressId)=${salePosition.customerId}&dto(contactPersonId)=${salePosition.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>

            <c:if test="${'true' == useJavaScript}">
                <c:set var="editContactPersonUrl" value="javascript:goParentURL('${editContactPersonUrl}')"/>
            </c:if>
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
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALEPOSITION" permission="DELETE">
                <fanta:actionColumn name="delete"
                                    title="Common.delete"
                                    action="${deleteAction}"
                                    useJScript="${'true' == useJavaScript}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="productName" action="${editAction}"
                          useJScript="${'true' == useJavaScript}"
                          styleClass="listItem" title="SalePosition.productName"
                          headerStyle="listHeader" width="17%" orderable="true"
                          maxLength="40"/>

        <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="SalePosition.contactPerson"
                          headerStyle="listHeader" width="16%" orderable="true" renderData="false">
            <fanta:textShorter title="${salePosition.contactPersonName}">
                <a href="${editContactPersonUrl}">
                    <c:out value="${salePosition.contactPersonName}"/>
                </a>
            </fanta:textShorter>
        </fanta:dataColumn>
        <fanta:dataColumn name="unitName" styleClass="listItem" title="SalePosition.unitName"
                          headerStyle="listHeader" width="9%" orderable="true"
                          maxLength="40"/>
        <fanta:dataColumn name="quantity" styleClass="listItemRight" title="SalePosition.quantity"
                          headerStyle="listHeader" width="9%" orderable="true"/>

        <c:if test="${useNetPrice}">
            <fanta:dataColumn name="unitPrice"
                              styleClass="listItemRight"
                              title="SalePosition.unitPriceNet"
                              headerStyle="listHeader"
                              width="12%"
                              orderable="true"
                              renderData="false">
                <fmt:formatNumber var="unitPriceFormatted" value="${salePosition.unitPrice}" type="number"
                                  pattern="${numberFormat4Decimal}"/>
                ${unitPriceFormatted}
            </fanta:dataColumn>
            <fanta:dataColumn name="totalPrice"
                              styleClass="listItemRight"
                              title="SalePosition.totalPrice"
                              headerStyle="listHeader"
                              width="12%"
                              orderable="true"
                              renderData="false">
                <fmt:formatNumber var="totalPriceFormatted" value="${salePosition.totalPrice}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalPriceFormatted}
            </fanta:dataColumn>
        </c:if>
        <c:if test="${useGrossPrice}">
            <fanta:dataColumn name="unitPriceGross"
                              styleClass="listItemRight"
                              title="SalePosition.unitPriceGross"
                              headerStyle="listHeader"
                              width="12%"
                              orderable="true"
                              renderData="false">
                <fmt:formatNumber var="unitPriceFormatted" value="${salePosition.unitPriceGross}" type="number"
                                  pattern="${numberFormat4Decimal}"/>
                ${unitPriceFormatted}
            </fanta:dataColumn>
            <fanta:dataColumn name="totalPriceGross"
                              styleClass="listItemRight"
                              title="SalePosition.totalPriceGross"
                              headerStyle="listHeader"
                              width="12%"
                              orderable="true"
                              renderData="false">
                <fmt:formatNumber var="totalPriceFormatted" value="${salePosition.totalPriceGross}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalPriceFormatted}
            </fanta:dataColumn>
        </c:if>

        <fanta:dataColumn name="deliveryDate" styleClass="listItem" title="SalePosition.deliveryDate"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            <fmt:formatDate var="deliveryDateValue" value="${app2:intToDate(salePosition.deliveryDate)}"
                            pattern="${datePattern}"/>
            ${deliveryDateValue}
        </fanta:dataColumn>
        <fanta:dataColumn name="active" styleClass="listItem2" title="SalePosition.active"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            <c:if test="${salePosition.active == 1}">
                <span class="${app2:getClassGlyphOk()}"></span>
            </c:if>&nbsp;
        </fanta:dataColumn>
    </fanta:table>
</div>