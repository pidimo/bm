<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<script>
    function resetFields() {
        var form = document.productContractAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/ProductContract/AdvancedList.do"
               focus="parameter(contractNumber)"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="ProductContract.advancedSearch"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractNumber_id">
                            <fmt:message key="ProductContract.contractNumber"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(contractNumber)"
                                       styleId="contractNumber_id"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       tabindex="1"/>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payMethodId">
                            <fmt:message key="Contract.payMethod"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(payMethod)"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         styleId="payMethodId"
                                         tabindex="2">
                                <html:option value=""/>
                                <html:options collection="payMethodList"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}"
                               for="contractAddressName1@_contractAddressName2@_contractAddressName3_id">
                            <fmt:message key="Contract.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3)"
                                    styleClass="${app2:getFormInputClasses()} mediumText"
                                    styleId="contractAddressName1@_contractAddressName2@_contractAddressName3_id"
                                    tabindex="3"/>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractTypeId_id">
                            <fmt:message key="Contract.contractType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(contractTypeId)"
                                          listName="contractTypeList"
                                          firstEmpty="true"
                                          styleId="contractTypeId_id"
                                          labelProperty="name"
                                          valueProperty="contractTypeId"
                                          module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          tabIndex="4">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}"
                               for="customerAddressName1@_customerAddressName2@_customerAddressName3_id">
                            <fmt:message key="Sale.customer"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)"
                                    styleClass="${app2:getFormInputClasses()} mediumText"
                                    styleId="customerAddressName1@_customerAddressName2@_customerAddressName3_id"
                                    tabindex="5"/>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="vatId_id">
                            <fmt:message key="ProductContract.vat"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(vatId)"
                                          listName="vatList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleId="vatId_id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                            <fmt:message key="Contract.product"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(productName)"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       maxlength="80"
                                       styleId="productName_id"
                                       tabindex="7"/>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                            <fmt:message key="ProductContract.currency"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(currencyId)"
                                          listName="basicCurrencyList"
                                          labelProperty="name"
                                          styleId="currencyId_id"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          tabIndex="8">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_price">
                            <fmt:message key="Contract.price"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(priceFrom)"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_price"
                                                    tabindex="9"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(priceTo)"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_price"
                                                    tabindex="10"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sellerId_id">
                            <fmt:message key="ProductContract.seller"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(sellerId)"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleId="sellerId_id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/contacts"
                                          firstEmpty="true"
                                          tabIndex="11">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_discount">
                            <fmt:message key="Contract.discount"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(discountFrom)"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_discount"
                                                    tabindex="12"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(discountTo)"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_price"
                                                    tabindex="13"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="saleDateFrom">
                            <fmt:message key="Contract.orderDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.from" var="from"/>

                                    <div class="input-group date">
                                        <app:dateText property="parameter(orderDateFrom)"
                                                      maxlength="10"
                                                      styleId="saleDateFrom"
                                                      placeHolder="from"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"
                                                      tabindex="14"
                                                      mode="bootstrap"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message var="to" key="Common.to"/>

                                    <div class="input-group date">
                                        <app:dateText property="parameter(orderDateTo)"
                                                      maxlength="10"
                                                      styleId="saleDateTo"
                                                      placeHolder="to"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"
                                                      tabindex="15"
                                                      mode="bootstrap"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_open_amount">
                            <fmt:message key="Contract.openAmount"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(openAmountFrom)"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_open_amount"
                                                    tabindex="16"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(openAmountTo)"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_price"
                                                    tabindex="17"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="salePositionFreetext_id">
                            <fmt:message key="Contract.salePosition.freeText"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(salePositionFreetext)"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       styleId="salePositionFreetext_id"
                                       tabindex="18"/>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productContractNote_id">
                            <fmt:message key="Contract.note"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(productContractNote)"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       styleId="productContractNote_id"
                                       tabindex="19"/>
                        </div>
                    </div>

                </div>
            </fieldset>
            <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="20">
                <fmt:message key="Common.go"/>
            </html:submit>
            <html:button property="reset1" tabindex="21" styleClass="${app2:getFormButtonClasses()}"
                         onclick="resetFields();">
                <fmt:message key="Common.clear"/>
            </html:button>

        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/ProductContract/AdvancedList.do"
                            parameterName="alphabetName1"
                            mode="bootstrap"/>
        </div>

    </html:form>


    &nbsp;

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="productContractAdvancedList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
                     id="productContract"
                     action="ProductContract/AdvancedList.do"
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
                              width="15%" renderData="false">
                <fanta:textShorter title="${productContract.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${productContract.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="customerName"
                              title="Sale.customer"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="15%" renderData="false">
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