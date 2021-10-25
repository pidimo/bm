<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
    <!--
    function check() {
        field = document.getElementById('formList').selected;
        guia = document.getElementById('formList').guia;
        var i;

        if (guia.checked) {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = true;
                    }
                } else {
                    if (!field.disabled) field.checked = true;
                }
            }
        } else {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = false;
                    }
                } else {
                    if (!field.disabled) field.checked = false;
                }
            }
        }
    }

    function printAll() {
        document.getElementById('printButton').value = true;
        document.getElementById('generateValidDocument_id').value = false;
        goSubmit();
    }

    function printSelected() {
        document.getElementById('printButton').value = false;
        document.getElementById('generateValidDocument_id').value = false;
        goSubmit();
    }

    function generateValidDocuments() {
        document.getElementById('generateValidDocument_id').value = true;
        goSubmit();
    }

    //form management
    var isListSubmit = "false";
    function goSubmit() {
        if (isListSubmit == "false") {
            setSubmit(true);
            document.forms[1].submit();
        }
        isListSubmit = "false";
    }

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = ('true' == ss);
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        isListSubmit = "true";
    }

    //-->
</script>

<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="INVOICE_TYPE" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<tags:initBootstrapDatepicker/>


<div class="${app2:getListWrapperClasses()}">

    <html:form action="/IncomingInvoice/RangePrintList.do"
               focus="parameter(supplierName1@_supplierName2@_supplierSearchName)"
               styleClass="form-horizontal">
        <div class="searchContainer ${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="IncomingInvoice.print.range"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Finance.incomingInvoice.supplierName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(supplierName1@_supplierName2@_supplierSearchName)"
                                    styleClass="${app2:getFormInputClasses()} mediumText"
                                    maxlength="80" tabindex="1"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceDateFrom">
                            <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startInvoiceDate)" maxlength="10"
                                                      styleId="invoiceDateFrom"
                                                      placeHolder="${from}"
                                                      mode="bootstrap"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true" tabindex="4"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endInvoiceDate)" maxlength="10"
                                                      styleId="invoiceDateTo"
                                                      placeHolder="${to}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true" tabindex="5"
                                                      mode="bootstrap"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Finance.incomingInvoice.invoiceNumber"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="numberFrom_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <html:text property="parameter(invoiceNumberFrom)"
                                               styleId="numberFrom_id"
                                               styleClass="${app2:getFormInputClasses()} shortText numberInputWidth"
                                               tabindex="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="numberTo_id">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <html:text property="parameter(invoiceNumberTo)"
                                               styleId="numberTo_id"
                                               styleClass="${app2:getFormInputClasses()} shortText numberInputWidth"
                                               tabindex="3"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Finance.incomingInvoice.openAmount"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="openAmountFrom_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(openAmount1)"
                                                    styleId="openAmountFrom_id"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="6" maxlength="13"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="openAmountTo_id">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(openAmount2)" styleId="openAmountTo_id"
                                                    numberType="decimal" maxInt="10"
                                                    maxFloat="2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="7" maxlength="13"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="wrapperButton">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="7">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/IncomingInvoice/RangePrintList.do" parameterName="alphabetSupplierName1" mode="bootstrap"/>
        </div>
    </html:form>

    <%
        if (request.getAttribute("incomingInvoicePrintList") != null) {
            ResultList resultList = (ResultList) request.getAttribute("incomingInvoicePrintList");
            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
        }
    %>

    <html:form action="/IncomingInvoice/RangePrint.do" styleId="formList"
               onsubmit="return testSubmit();">
        <html:hidden property="dto(printAllIds)" value="${allPrintIds}" styleId="printAllIds_id"/>
        <html:hidden property="dto(isPrintAll)" value="${false}" styleId="printButton"/>
        <html:hidden property="dto(validDocumentIds)" value="${invoiceValidDocumentIds}" styleId="validDocumentIds_id"/>
        <html:hidden property="dto(isGenerateValidDocuments)" value="${false}" styleId="generateValidDocument_id"/>

        <input type="hidden" name="isSubmit" value="false" id="isSubmit">
        <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
            <div class="wrapperButton">
                <c:if test="${size > 0}">
                    <html:button property="selectedPrint" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="printSelected()"
                                 tabindex="8">
                        <fmt:message key="IncomingInvoice.print.printSelected"/>
                    </html:button>
                    <html:button property="allPrint" styleClass="${app2:getFormButtonClasses()}" onclick="printAll()"
                                 tabindex="9">
                        <fmt:message key="IncomingInvoice.print.printAll"/>
                    </html:button>
                </c:if>
            </div>
        </app2:checkAccessRight>

        <div class="table-responsive">
            <fanta:table mode="bootstrap" list="incomingInvoicePrintList" width="100%" styleClass="${app2:getFantabulousTableLargeClases()}"
                         id="incomingInvoice"
                         action="IncomingInvoice/RangePrintList.do"
                         imgPath="${baselayout}" align="center" withCheckBox="true">

                <html:hidden property="dto(${incomingInvoice.DOCUMENTID})" value="${incomingInvoice.INVOICENUMBER}"/>

                <%--address link--%>
                <tags:addressEditContextRelativeUrl varName="supplierEditLink" addressId="${incomingInvoice.supplierAddressId}" addressType="${incomingInvoice.supplierAddressType}" name1="${incomingInvoice.supplierName1}" name2="${incomingInvoice.supplierName2}" name3="${incomingInvoice.supplierName3}"/>


                <c:if test="${size > 0}">
                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                          property="DOCUMENTID" headerStyle="listHeader"
                                          styleClass="listItemCenter" width="5%"/>
                </c:if>

                <fanta:dataColumn name="INVOICENUMBER" styleClass="listItem"
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
                                  title="Finance.incomingInvoice.invoiceDate" headerStyle="listHeader" width="10%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatDate var="invoiceDateString" value="${app2:intToDate(incomingInvoice.INVOICEDATE)}"
                                    pattern="${datePattern}"/>
                    ${invoiceDateString}
                </fanta:dataColumn>

                <fanta:dataColumn name="RECEIPTDATE" styleClass="listItem"
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

        <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
            <div class="wrapperButton">
                <c:if test="${size > 0}">
                    <html:button property="selectedPrint" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="printSelected()"
                                 tabindex="10">
                        <fmt:message key="IncomingInvoice.print.printSelected"/>
                    </html:button>
                    <html:button property="allPrint" styleClass="${app2:getFormButtonClasses()}" onclick="printAll()"
                                 tabindex="11">
                        <fmt:message key="IncomingInvoice.print.printAll"/>
                    </html:button>
                </c:if>
            </div>
        </app2:checkAccessRight>
    </html:form>
</div>