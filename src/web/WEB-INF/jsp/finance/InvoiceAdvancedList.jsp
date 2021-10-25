<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<script>
    function resetFields() {
        var form = document.invoiceAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "hidden") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>
<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>
<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Invoice/AdvancedList.do" focus="parameter(number)" styleClass="form-horizontal">
        <div class="searchContainer">
            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                        <fmt:message key="Invoice.advancedSearch"/>
                    </legend>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="number_id">
                                <fmt:message key="Invoice.number"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:text property="parameter(number)" styleId="number_id"
                                           styleClass="${app2:getFormInputClasses()} mediumText" tabindex="1"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                                <fmt:message key="Invoice.type"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:select property="parameter(type)"
                                             styleId="type_id"
                                             styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                             tabindex="2">
                                    <html:option value=""/>
                                    <html:options collection="invoiceTypeList"
                                                  property="value"
                                                  labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                                <fmt:message key="Invoice.contact"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="input-group">
                                    <app:text property="parameter(address)"
                                              styleId="fieldAddressName_id"
                                              styleClass="${app2:getFormInputClasses()} middleText"
                                              maxlength="40"
                                              tabindex="3"
                                              readonly="true"/>

                                    <html:hidden property="parameter(invoiceAddressId)" styleId="fieldAddressId_id"/>
                                    <span class="input-group-btn">
                                       <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                                  name="searchAddress"
                                                                  titleKey="Common.search"
                                                                  hide="false"
                                                                  modalTitleKey="Contact.Title.search"
                                                                  styleId="searchAddressId"
                                                                  isLargeModal="true"
                                                                  tabindex="4"/>

                                       <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                       nameFieldId="fieldAddressName_id"
                                                                       titleKey="Common.clear"
                                                                       tabindex="5"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="payConditionId_id">
                                <fmt:message key="Invoice.payCondition"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="parameter(payConditionId)"
                                              styleId="payConditionId_id"
                                              listName="payConditionList"
                                              labelProperty="name"
                                              valueProperty="id"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              firstEmpty="true"
                                              module="/catalogs"
                                              tabIndex="6">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldCustomerName_id">
                                <fmt:message key="Customer"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="input-group">
                                    <app:text property="parameter(customerName)"
                                              styleId="fieldCustomerName_id"
                                              styleClass="${app2:getFormInputClasses()} middleText"
                                              maxlength="40"
                                              tabindex="7"
                                              readonly="true"/>

                                    <html:hidden property="parameter(customerId)" styleId="fieldCustomerId_id"/>
                                    <span class="input-group-btn">
                                       <tags:bootstrapSelectPopup url="/contacts/SearchCustomer.do?addressIdStyleId=fieldCustomerId_id&addressNameStyleId=fieldCustomerName_id"
                                                                  name="searchCustomer"
                                                                  titleKey="Common.search"
                                                                  hide="false"
                                                                  modalTitleKey="Customer.title.search"
                                                                  styleId="searchCustomerId"
                                                                  isLargeModal="true"
                                                                  tabindex="8"/>

                                       <tags:clearBootstrapSelectPopup keyFieldId="fieldCustomerId_id"
                                                                       nameFieldId="fieldCustomerName_id"
                                                                       titleKey="Common.clear"
                                                                       tabindex="9"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                                <fmt:message key="Invoice.currency"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="parameter(currencyId)"
                                              styleId="currencyId_id"
                                              listName="basicCurrencyList"
                                              labelProperty="name"
                                              valueProperty="id"
                                              firstEmpty="true"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              module="/catalogs"
                                              tabIndex="10">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Invoice.totalAmountNet"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="totalAmountNetFrom_id">
                                            <fmt:message key="Common.from"/>
                                        </label>
                                        <app:numberText property="parameter(totalAmountNetFrom)" numberType="decimal"
                                                        styleId="totalAmountNetFrom_id"
                                                        maxInt="10"
                                                        maxFloat="2"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="11"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="totalAmountNetTo_id">
                                            <fmt:message key="Common.to"/>
                                        </label>
                                        <app:numberText property="parameter(totalAmountNetTo)" numberType="decimal"
                                                        styleId="totalAmountNetTo_id"
                                                        maxInt="10"
                                                        maxFloat="2"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="12"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceDateFrom">
                                <fmt:message key="Invoice.invoiceDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="Common.from" var="from"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(invoiceDateFrom)" maxlength="10"
                                                          styleId="invoiceDateFrom"
                                                          placeHolder="${from}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" tabindex="13" mode="bootstrap"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="Common.to" var="to"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(invoiceDateTo)" maxlength="10"
                                                          styleId="invoiceDateTo"
                                                          placeHolder="${to}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" tabindex="14" mode="bootstrap"/>
                                        </div>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Invoice.totalAmountGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="totalAmountGrossFrom_id">
                                            <fmt:message key="Common.from"/>
                                        </label>
                                        <app:numberText property="parameter(totalAmountGrossFrom)"
                                                        styleId="totalAmountGrossFrom_id" numberType="decimal"
                                                        maxInt="10"
                                                        maxFloat="2"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="15"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="totalAmountGrossTo_id">
                                            <fmt:message key="Common.to"/>
                                        </label>
                                        <app:numberText property="parameter(totalAmountGrossTo)"
                                                        styleId="totalAmountGrossTo_id" numberType="decimal"
                                                        maxInt="10"
                                                        maxFloat="2"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="16"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="paymentDateFrom">
                                <fmt:message key="Invoice.paymentDay"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <div class="input-group date">
                                            <app:dateText property="parameter(paymentDateFrom)" maxlength="10"
                                                          placeHolder="${from}"
                                                          styleId="paymentDateFrom"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" tabindex="17"
                                                          mode="bootstrap"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <div class="input-group date">
                                            <app:dateText property="parameter(paymentDateTo)" maxlength="10"
                                                          placeHolder="${to}"
                                                          styleId="paymentDateTo"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" tabindex="18"
                                                          mode="bootstrap"/>
                                        </div>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Invoice.openAmount"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="openAmountFrom_id">
                                            <fmt:message key="Common.from"/>
                                        </label>
                                        <app:numberText property="parameter(openAmountFrom)" styleId="openAmountFrom_id"
                                                        numberType="decimal" maxInt="10"
                                                        maxFloat="2"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="19"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="openAmountTo_id">
                                            <fmt:message key="Common.to"/>
                                        </label>
                                        <app:numberText property="parameter(openAmountTo)" styleId="openAmountTo_id"
                                                        numberType="decimal"
                                                        maxInt="10"
                                                        maxFloat="2"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="20"/>

                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceNote_id">
                                <fmt:message key="Invoice.search.notes"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:text property="parameter(invoiceNote)"
                                           styleId="invoiceNote_id"
                                           styleClass="${app2:getFormInputClasses()} middleText" tabindex="21"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                                <fmt:message key="InvoicePosition.product"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="input-group">
                                    <html:hidden property="parameter(productId)" styleId="field_key"/>
                                    <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                                    <html:hidden property="parameter(2)" styleId="field_unitId"/>
                                    <html:hidden property="parameter(3)" styleId="field_price"/>

                                    <app:text property="parameter(productName)" styleId="field_name"
                                              styleClass="${app2:getFormInputClasses()} mediumText"
                                              maxlength="40"
                                              readonly="true" tabindex="22"/>
                            <span class="input-group-btn">
                                 <tags:bootstrapSelectPopup styleId="SearchProduct_id" url="/products/SearchProduct.do"
                                                            name="SearchProduct"
                                                            titleKey="Common.search"
                                                            modalTitleKey="Product.Title.SimpleSearch"
                                                            tabindex="23"
                                                            isLargeModal="true"/>
                                 <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                 styleClass="${app2:getFormButtonClasses()}"
                                                                 nameFieldId="field_name"
                                                                 titleKey="Common.clear"
                                                                 tabindex="24"
                                                                 glyphiconClass="glyphicon-erase"/>
                            </span>
                                </div>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoicePositionText_id">
                                <fmt:message key="Invoice.search.positionText"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:text property="parameter(invoicePositionText)" styleId="invoicePositionText_id"
                                           styleClass="${app2:getFormInputClasses()} middleText" tabindex="25"/>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Product.group"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="parameter(productGroupId)" listName="productGroupList" firstEmpty="true"
                                              labelProperty="name" valueProperty="id" module="/catalogs"
                                              styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                              tabIndex="26">
                                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                                                separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                                </fanta:select>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceTitle_id">
                                <fmt:message key="Invoice.title"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:text property="parameter(invoiceTitle)" styleId="invoiceTitle_id"
                                           styleClass="${app2:getFormInputClasses()} middleText" tabindex="27"/>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="serviceDateFrom">
                                <fmt:message key="Invoice.serviceDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="Common.from" var="from"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(serviceDateFrom)" maxlength="10"
                                                          styleId="serviceDateFrom"
                                                          placeHolder="${from}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" tabindex="28" mode="bootstrap"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="Common.to" var="to"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(serviceDateTo)" maxlength="10"
                                                          styleId="serviceDateTo"
                                                          placeHolder="${to}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" tabindex="28" mode="bootstrap"/>
                                        </div>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getRowClassesTwoColumns()}">
                        </div>
                    </div>

                    <div class="wrapperSearch">
                        <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="30">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                        <html:button property="reset1" tabindex="31" styleClass="${app2:getFormButtonClasses()}"
                                     onclick="resetFields();">
                            <fmt:message key="Common.clear"/>
                        </html:button>
                    </div>
                </fieldset>
            </div>

            <div class="${app2:getAlphabetWrapperClasses()}">
                <fanta:alphabet action="/Invoice/AdvancedList.do" parameterName="alphabetName1" mode="bootstrap"/>
            </div>
        </div>
    </html:form>


    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
        <html:form action="/Invoice/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="32">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoiceAdvancedList"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
                     width="100%"
                     id="invoice"
                     action="Invoice/AdvancedList.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>
            <c:set var="deleteLink"
                   value="Invoice/Forward/Delete.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&dto(withReferences)=true&index=0"/>
            <c:set var="downloadAction"
                   value="Download/Invoice/Document.do?dto(freeTextId)=${invoice.documentId}&dto(invoiceId)=${invoice.invoiceId}"/>

            <%--address link--%>
            <c:choose>
                <c:when test="${personType == invoice.addressType}">
                    <c:set var="addressEditLink"
                           value="/contacts/Person/Forward/Update.do?contactId=${invoice.addressId}&dto(addressId)=${invoice.addressId}&dto(addressType)=${invoice.addressType}&dto(name1)=${app2:encode(invoice.addressName1)}&dto(name2)=${app2:encode(invoice.addressName2)}&dto(name3)=${app2:encode(invoice.addressName3)}&index=0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="addressEditLink"
                           value="/contacts/Organization/Forward/Update.do?contactId=${invoice.addressId}&dto(addressId)=${invoice.addressId}&dto(addressType)=${invoice.addressType}&dto(name1)=${app2:encode(invoice.addressName1)}&dto(name2)=${app2:encode(invoice.addressName2)}&dto(name3)=${app2:encode(invoice.addressName3)}&index=0"/>
                </c:otherwise>
            </c:choose>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download" title="Common.download"
                                    action="${downloadAction}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="34%"
                                    render="${not empty invoice.documentId}"
                                    glyphiconClass="${app2:getClassGlyphDownload()}"/>

            </fanta:columnGroup>
            <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem" title="Invoice.number"
                              headerStyle="listHeader"
                              width="16%"
                              orderable="true"
                              maxLength="40"/>
            <fanta:dataColumn name="invoiceTitle" styleClass="listItem" title="Invoice.title"
                              headerStyle="listHeader"
                              width="17%"
                              orderable="true"/>
            <fanta:dataColumn name="type" styleClass="listItem" title="Invoice.type"
                              headerStyle="listHeader" orderable="true" renderData="false" width="3%">
                <c:if test="${invoice.type == invoiceType}">
                    <fmt:message key="Invoice.type.invoice.abbr"/>
                </c:if>
                <c:if test="${invoice.type == creditNoteType}">
                    <fmt:message key="Invoice.type.creditNote.abbr"/>
                </c:if>
            </fanta:dataColumn>
            <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              renderData="false">
                <fanta:textShorter title="${invoice.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${invoice.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                              headerStyle="listHeader"
                              width="8%"
                              orderable="true"/>
            <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountNet}
            </fanta:dataColumn>
            <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight" title="Invoice.totalAmountGross"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountGross}
            </fanta:dataColumn>
            <fanta:dataColumn name="openAmount" styleClass="listItem2Right" title="Invoice.openAmount"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                  pattern="${numberFormat}"/>
                ${openAmount}
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
        <html:form action="/Invoice/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="33">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
<tags:jQueryValidation formName="invoiceAdvancedListForm"/>