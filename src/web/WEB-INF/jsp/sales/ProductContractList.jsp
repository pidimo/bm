<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(onlyActiveContract)" value="document.getElementById('idActive').value"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
    function jumpAlphabet(obj) {
        window.location = ${jsAlphabetUrl};
    }
</script>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/ProductContract/List.do"
               focus="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)"
               styleClass="form-horizontal">
        <legend class="title">
            <fmt:message key="ProductContract.singleSearch"/>
        </legend>

        <div class="wrapperSearch">
            <div class="form-group col-sm-4">
                <label class="${app2:getFormLabelClasses()} label-left" for="search_id">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:text
                            property="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)"
                            styleClass="${app2:getFormInputClasses()} largeText"
                            styleId="search_id"
                            maxlength="80"/>
                </div>
            </div>
            <div class="form-group col-sm-4">
                <label class="${app2:getFormLabelClasses()}"></label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:select property="parameter(onlyActiveContract)"
                                 styleClass="${app2:getFormSelectClasses()} shortSelect"
                                 styleId="idActive">
                        <html:option value=""><fmt:message key="ProductContract.filter.all"/></html:option>
                        <html:option value="true"><fmt:message key="ProductContract.filter.active"/></html:option>
                    </html:select>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchButton()}">
                <div class="col-sm-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
            <div class="pull-left">
                <app:link action="/ProductContract/AdvancedList.do?advancedListForward=ContractAdvancedSearch"
                          styleClass="btn btn-link">
                    <fmt:message key="Common.advancedSearch"/>
                </app:link>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/ProductContract/List.do"
                            parameterName="alphabetName1"
                            mode="bootstrap"
                            onClick="jumpAlphabet(this);return false;"/>
        </div>

    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="productContractMainSingleList"
                     width="100%"
                     id="productContract"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
                     action="ProductContract/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="ProductContract/Forward/Update.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&index=0"/>
            <c:set var="deleteLink"
                   value="ProductContract/Forward/Delete.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&dto(withReferences)=true&index=0"/>

            <%--address link--%>
            <tags:addressEditContextRelativeUrl varName="addressEditLink" addressId="${productContract.addressId}" addressType="${productContract.addressType}" name1="${productContract.addressName1}" name2="${productContract.addressName2}" name3="${productContract.addressName3}"/>
            <%--customer links--%>
            <tags:addressEditContextRelativeUrl varName="customerEditLink" addressId="${productContract.customerAddressId}" addressType="${productContract.customerAddressType}" name1="${productContract.customerName1}" name2="${productContract.customerName2}" name3="${productContract.customerName3}"/>

            <%--product links--%>
            <c:set var="productEditLink"
                   value="/products/Product/Forward/Update.do?productId=${productContract.productId}&dto(productId)=${productContract.productId}&dto(productName)=${app2:encode(productContract.productName)}&index=0"/>
            <app2:checkAccessRight functionality="PRODUCT" permission="VIEW" var="hasProductViewPermission"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="contractNumber"
                              action="${editLink}"
                              title="ProductContract.contractNumber"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              width="10%"/>

            <fanta:dataColumn name="addressName"
                              title="Contract.contact"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="15%"
                              renderData="false">
                <fanta:textShorter title="${productContract.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${productContract.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="customerName"
                              title="ProductContract.customer"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="15%"
                              renderData="false">
                <fanta:textShorter title="${productContract.customerName}">
                    <app:link action="${customerEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${productContract.customerName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="productName"
                              title="Contract.product"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="10%" renderData="false">
                <fanta:textShorter title="${productContract.productName}">
                    <c:choose>
                        <c:when test="${hasProductViewPermission}">
                            <app:link action="${productEditLink}" contextRelative="true">
                                <c:out value="${productContract.productName}"/>
                            </app:link>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${productContract.productName}"/>
                        </c:otherwise>
                    </c:choose>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="price"
                              styleClass="listItemRight"
                              title="Contract.price"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="8%">
                <fmt:formatNumber var="priceFormattedValue" value="${productContract.price}" type="number"
                                  pattern="${numberFormat}"/>
                ${priceFormattedValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="discount"
                              styleClass="listItemRight"
                              title="Contract.discount"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="5%">
                <fmt:formatNumber var="discountFormattedValue" value="${productContract.discount}" type="number"
                                  pattern="${numberFormat}"/>
                ${discountFormattedValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="openAmount"
                              styleClass="listItemRight"
                              title="Contract.openAmount"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="8%">
                <fmt:formatNumber var="openAmountFormattedValue" value="${productContract.openAmount}" type="number"
                                  pattern="${numberFormat}"/>
                <c:if test="${productContract.payMethod != PERIODIC_PAYMETHOD}">
                    ${openAmountFormattedValue}
                </c:if>
            </fanta:dataColumn>
            <fanta:dataColumn name="orderDate"
                              styleClass="listItem"
                              title="Contract.orderDate"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="8%">
                <fmt:formatDate var="orderDateValue" value="${app2:intToDate(productContract.orderDate)}"
                                pattern="${datePattern}"/>
                ${orderDateValue}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="payMethod"
                              styleClass="listItem"
                              title="Contract.payMethod"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="8%">
                <c:set var="payMethodLabel" value="${app2:searchLabel(payMethodList, productContract.payMethod)}"/>
                <fanta:textShorter title="${payMethodLabel}">
                    ${payMethodLabel}
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="contractTypeName"
                              styleClass="listItem2"
                              title="Contract.contractType"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="8%"/>
        </fanta:table>
    </div>
</div>