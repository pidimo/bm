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
               focus="parameter(productName@_contractNumber)"
               styleClass="form-horizontal">

        <div class="${app2:getSearchWrapperClasses()}">
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()} label-left" for="">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:text property="parameter(productName@_contractNumber)"
                               styleClass="${app2:getFormInputClasses()} largeText"
                               maxlength="80"/>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()}" for="">

                </label>

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
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/ProductContract/List.do"
                            parameterName="productNameAlpha"
                            onClick="jumpAlphabet(this);return false;"
                            mode="bootstrap"/>
        </div>

    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="productContractMainSingleList"
                     width="100%"
                     id="productContract"
                     action="ProductContract/List.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editLink"
                   value="ProductContract/Forward/Update.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}"/>
            <c:set var="deleteLink"
                   value="ProductContract/Forward/Delete.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&dto(withReferences)=true"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="edit" title="Common.update"
                                    action="${editLink}"
                                    styleClass="listItem"
                                    headerStyle="listHeader" width="50%" glyphiconClass="${app2:getClassGlyphEdit()}"/>
                <fanta:actionColumn name="delete" title="Common.delete"
                                    action="${deleteLink}"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}" />
            </fanta:columnGroup>
            <fanta:dataColumn name="contractNumber"
                              action="${editLink}"
                              title="ProductContract.contractNumber"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              width="10%"/>
            <fanta:dataColumn name="productName"
                              action="${editLink}"
                              title="Contract.product"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="15%"/>
            <fanta:dataColumn name="customerName"
                              title="ProductContract.customer"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="15%"/>
            <fanta:dataColumn name="price"
                              styleClass="listItemRight"
                              title="Contract.price"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="10%">
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
                              width="10%">
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
                              width="10%">
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
                              width="7%">
                <fmt:formatDate var="orderDateValue" value="${app2:intToDate(productContract.orderDate)}"
                                pattern="${datePattern}"/>
                ${orderDateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="payMethod"
                              styleClass="listItem"
                              title="Contract.payMethod"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="9%">
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
                              width="9%"/>
        </fanta:table>
    </div>
</div>