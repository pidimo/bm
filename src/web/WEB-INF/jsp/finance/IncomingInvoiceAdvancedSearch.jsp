<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>

<c:set var="INVOICE_TYPE" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<script>
    function myReset() {
        var form = document.incomingInvoiceAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(currencyId)") {
                form.elements[i].options.selectedIndex = 0;
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>


<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/IncomingInvoice/AdvancedSearch.do" focus="parameter(invoiceNumber)"
               styleClass="form-horizontal">
        <div class="searchContainer">
            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                        <fmt:message key="${pagetitle}"/>
                    </legend>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceNumber_id">
                                <fmt:message key="Finance.incomingInvoice.invoiceNumber"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <app:text property="parameter(invoiceNumber)" styleId="invoiceNumber_id"
                                          styleClass="${app2:getFormInputClasses()} mediumText" tabindex="1"
                                          maxlength="30"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <!--*******************COLUMN DIVISION************************-->
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                                <fmt:message key="Finance.incomingInvoice.type"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:select property="parameter(type)"
                                             styleId="type_id"
                                             styleClass="${app2:getFormSelectClasses()} middleSelect"
                                             tabindex="2">
                                    <html:option value=""/>
                                    <html:options collection="invoiceTypeList" property="value" labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                                <fmt:message key="Finance.incomingInvoice.supplierName"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <app:text property="parameter(supplierName1@_supplierName2@_supplierSearchName)"
                                          styleId="fieldAddressName_id" tabindex="3"
                                          styleClass="${app2:getFormInputClasses()} mediumText" maxlength="50"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <!--*******************COLUMN DIVISION************************-->
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                                <fmt:message key="Finance.incomingInvoice.currency"/>
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
                                              tabIndex="4">
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
                                <fmt:message key="Finance.incomingInvoice.amountNet"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="amountNet1_id">
                                            <fmt:message key="Common.from"/>
                                        </label>
                                        <app:numberText property="parameter(amountNet1)" styleId="amountNet1_id"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="5"
                                                        maxlength="12"
                                                        numberType="decimal" maxInt="10" maxFloat="2"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="amountNet2_id">
                                            <fmt:message key="Common.to"/>
                                        </label>
                                        <app:numberText property="parameter(amountNet2)" styleId="amountNet2_id"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="6"
                                                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <!--*******************COLUMN DIVISION************************-->
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="startInvoiceDate">
                                <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="datePattern" var="datePattern"/>
                                        <fmt:message key="Common.from" var="from"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(startInvoiceDate)" maxlength="10"
                                                          tabindex="7"
                                                          styleId="startInvoiceDate"
                                                          placeHolder="${from}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" mode="bootstrap"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <fmt:message key="Common.to" var="to"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(endInvoiceDate)" maxlength="10"
                                                          tabindex="8"
                                                          placeHolder="${to}"
                                                          styleId="endInvoiceDate"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true" mode="bootstrap"/>
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
                                <fmt:message key="Finance.incomingInvoice.amountGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="parameter(amountGross1)_id">
                                            <fmt:message key="Common.from"/>
                                        </label>
                                        <app:numberText property="parameter(amountGross1)"
                                                        styleId="parameter(amountGross1)_id"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="9"
                                                        maxlength="12"
                                                        numberType="decimal" maxInt="10" maxFloat="2"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="parameter(amountGross2)_id">
                                            <fmt:message key="Common.to"/>
                                        </label>
                                        <app:numberText property="parameter(amountGross2)"
                                                        styleId="parameter(amountGross2)_id"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="10"
                                                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <!--*******************COLUMN DIVISION************************-->
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="startPayDate">
                                <fmt:message key="Finance.incomingInvoice.receiptDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="datePattern" var="datePattern"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(startReceiptDate)" maxlength="10"
                                                          tabindex="11"
                                                          placeHolder="${from}"
                                                          styleId="startPayDate"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true"
                                                          mode="bootstrap"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <div class="input-group date">
                                            <app:dateText property="parameter(endReceiptDate)" maxlength="10"
                                                          tabindex="12"
                                                          styleId="endPayDate"
                                                          placeHolder="${to}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true"
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
                                <fmt:message key="Finance.incomingInvoice.openAmount"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="parameter(openAmount1)_id">
                                            <fmt:message key="Common.from"/>
                                        </label>
                                        <app:numberText property="parameter(openAmount1)"
                                                        styleId="parameter(openAmount1)_id"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="13"
                                                        numberType="decimal" maxInt="10" maxFloat="2"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <label class="control-label" for="parameter(openAmount2)_id">
                                            <fmt:message key="Common.to"/>
                                        </label>
                                        <app:numberText property="parameter(openAmount2)"
                                                        styleId="parameter(openAmount2)_id"
                                                        styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                        tabindex="14"
                                                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <!--*******************COLUMN DIVISION************************-->
                        <div class="${app2:getRowClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="startPaidUntilDate">
                                <fmt:message key="Finance.incomingInvoice.paidUntil"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <fmt:message key="datePattern" var="datePattern"/>
                                        <div class="input-group date">
                                            <app:dateText property="parameter(startPaidUntilDate)" maxlength="10"
                                                          tabindex="15"
                                                          styleId="startPaidUntilDate"
                                                          placeHolder="${from}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true"
                                                          mode="bootstrap"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 marginButton">
                                        <div class="input-group date">
                                            <app:dateText property="parameter(endPaidUntilDate)" maxlength="10"
                                                          tabindex="16"
                                                          styleId="endPaidUntilDate"
                                                          placeHolder="${to}"
                                                          calendarPicker="true" datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()} dateText"
                                                          convert="true"
                                                          mode="bootstrap"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="wrapperSearch">
                        <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="17">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                        <html:button property="reset1" tabindex="18" styleClass="${app2:getFormButtonClasses()}"
                                     onclick="myReset()">
                            <fmt:message key="Common.clear"/>
                        </html:button>
                    </div>
                </fieldset>
            </div>

            <div class="${app2:getAlphabetWrapperClasses()}">
                <fanta:alphabet action="IncomingInvoice/AdvancedSearch.do" parameterName="invoiceNumber"
                                mode="bootstrap"/>
            </div>
        </div>
    </html:form>


    <%--****************************FANTABULOS LIST TABLE***********************--%>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="CREATE">
            <html:form action="/IncomingInvoice/Forward/Create.do">
                <div class="${app2:getFormGroupClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </div>
            </html:form></app2:checkAccessRight>
    </div>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="incomingInvoiceAdvancedList" width="100%" styleClass="${app2:getFantabulousTableLargeClases()}"
                     id="incomingInvoice"
                     action="IncomingInvoice/AdvancedSearch.do"
                     imgPath="${baselayout}" align="center">
            <app:url
                    value="/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}&tabKey=Finance.IncomingInvoice.detail"
                    var="urlUpdate" enableEncodeURL="false"/>
            <app:url
                    value="/IncomingInvoice/Forward/Delete.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}&tabKey=Finance.IncomingInvoice.detail"
                    var="urlDelete" enableEncodeURL="false"/>

            <c:set var="downloadAction"
                   value="/finance/Download/IncomingInvoice/Document.do?dto(freeTextId)=${incomingInvoice.DOCUMENTID}&dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}"/>

            <%--address link--%>
            <tags:addressEditContextRelativeUrl varName="supplierEditLink" addressId="${incomingInvoice.supplierAddressId}" addressType="${incomingInvoice.supplierAddressType}" name1="${incomingInvoice.supplierName1}" name2="${incomingInvoice.supplierName2}" name3="${incomingInvoice.supplierName3}"/>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                        styleClass="listItem" headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                        styleClass="listItem" headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download" title="Common.download"
                                    styleClass="listItem" headerStyle="listHeader" width="34%"
                                    render="false">
                    <c:choose>
                        <c:when test="${not empty incomingInvoice.DOCUMENTID}">
                            <app:link action="${downloadAction}" contextRelative="true" titleKey="Common.download">
                                <span class="${app2:getClassGlyphDownload()}"></span>
                            </app:link>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </fanta:actionColumn>

            </fanta:columnGroup>
            <fanta:dataColumn name="INVOICENUMBER" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingInvoice.invoiceNumber" headerStyle="listHeader" width="14%"
                              orderable="true" maxLength="40"/>

            <fanta:dataColumn name="TYPE" styleClass="listItem"
                              title="Finance.incomingInvoice.type" headerStyle="listHeader" width="5%"
                              orderable="true" renderData="false">
                <c:choose>

                    <c:when test="${incomingInvoice.TYPE==INVOICE_TYPE}">
                        <fmt:message key="Invoice.type.invoice.abbr"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="Invoice.type.creditNote.abbr"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>

            <fanta:dataColumn name="INVOICEDATE" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingInvoice.invoiceDate" headerStyle="listHeader" width="10%"
                              orderable="true"
                              renderData="false">
                <fmt:formatDate var="invoiceDateString" value="${app2:intToDate(incomingInvoice.INVOICEDATE)}"
                                pattern="${datePattern}"/>
                ${invoiceDateString}
            </fanta:dataColumn>

            <fanta:dataColumn name="RECEIPTDATE" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingInvoice.receiptDate" headerStyle="listHeader" width="11%"
                              orderable="true"
                              renderData="false">
                <fmt:formatDate var="receiptDateString" value="${app2:intToDate(incomingInvoice.RECEIPTDATE)}"
                                pattern="${datePattern}"/>
                ${receiptDateString}
            </fanta:dataColumn>

            <fanta:dataColumn name="SUPPLIERNAME" styleClass="listItem"
                              title="Finance.incomingInvoice.supplierName" headerStyle="listHeader" width="15%"
                              orderable="true" renderData="false">
                <fanta:textShorter title="${incomingInvoice.SUPPLIERNAME}">
                    <app:link action="${supplierEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${incomingInvoice.SUPPLIERNAME}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>

            <fanta:dataColumn name="CURRENCYLABEL" styleClass="listItem"
                              title="Finance.incomingInvoice.currency"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="true"/>

            <fanta:dataColumn name="AMOUNTNET" styleClass="listItemRight"
                              title="Finance.incomingInvoice.amountNet" headerStyle="listHeader" width="10%"
                              orderable="true" renderData="false">
                <fmt:formatNumber var="amountNetNumber" value="${incomingInvoice.AMOUNTNET}" type="number"
                                  pattern="${numberFormat}"/>
                ${amountNetNumber}
            </fanta:dataColumn>

            <fanta:dataColumn name="AMOUNTGROSS" styleClass="listItemRight"
                              title="Finance.incomingInvoice.amountGross" headerStyle="listHeader" width="10%"
                              orderable="true" renderData="false">
                <fmt:formatNumber var="amountGrossNumber" value="${incomingInvoice.AMOUNTGROSS}" type="number"
                                  pattern="${numberFormat}"/>
                ${amountGrossNumber}
            </fanta:dataColumn>

            <fanta:dataColumn name="OPENAMOUNT" styleClass="listItem2Right"
                              title="Finance.incomingInvoice.openAmount" headerStyle="listHeader" width="10%"
                              orderable="true" renderData="false">
                <fmt:formatNumber var="openAmountNumber" value="${incomingInvoice.OPENAMOUNT}" type="number"
                                  pattern="${numberFormat}"/>
                ${openAmountNumber}
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="CREATE">
            <html:form action="/IncomingInvoice/Forward/Create.do">
                <div class="${app2:getFormGroupClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </div>
            </html:form>
        </app2:checkAccessRight>
    </div>
</div>
<tags:jQueryValidation formName="incomingInvoiceAdvancedListForm"/>