<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/SalePosition/List.do"
               focus="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <fieldset>
                <label class="${app2:getFormLabelOneSearchInput()}" for="fieldInput_id">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text
                                property="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)"
                                styleId="fieldInput_id"
                                styleClass="largeText ${app2:getFormInputClasses()}"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
                    </div>
                </div>
            </fieldset>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="SalePosition/List.do" mode="bootstrap"
                        parameterName="customerAddressName1"/>
    </div>


    <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
        <html:form action="/SalePosition/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>


    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="salePositionForProductsList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="salePosition"
                     action="SalePosition/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="SalePosition/Forward/Update.do?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(customerName)=${app2:encode(salePosition.customerName)}"/>
            <c:set var="deleteLink"
                   value="SalePosition/Forward/Delete.do?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(customerName)=${app2:encode(salePosition.customerName)}&dto(withReferences)=true"/>

            <%--contact person links--%>
            <c:if test="${not empty salePosition.contactPersonId}">
                <c:set var="editContactPersonUrl"
                       value="/contacts/ContactPerson/Forward/Update.do?contactId=${salePosition.customerId}&dto(addressId)=${salePosition.customerId}&dto(contactPersonId)=${salePosition.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>
            </c:if>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALEPOSITION" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="customerName" action="${editLink}" styleClass="listItem"
                              title="SalePosition.customer"
                              headerStyle="listHeader" width="17%" orderable="true"/>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="SalePosition.contactPerson"
                              headerStyle="listHeader" width="17%" orderable="true" renderData="false">
                <fanta:textShorter title="${salePosition.contactPersonName}">
                    <app:link action="${editContactPersonUrl}" contextRelative="true" addModuleName="false">
                        <c:out value="${salePosition.contactPersonName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
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

            <fanta:dataColumn name="unitPrice"
                              styleClass="listItemRight"
                              title="SalePosition.unitPrice"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="false"
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
            <fanta:dataColumn name="totalPrice"
                              styleClass="listItemRight"
                              title="SalePosition.totalPrice"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="false"
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

            <fanta:dataColumn name="deliveryDate" styleClass="listItem" title="SalePosition.deliveryDate"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false">
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


    <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
        <html:form action="/SalePosition/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>



