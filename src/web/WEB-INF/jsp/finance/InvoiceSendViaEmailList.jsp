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

  function sendAllInvoices() {
    document.getElementById('isSendAll_id').value = true;
    goSubmit();
  }

  function sendSelectedInvoices() {
    document.getElementById('isSendAll_id').value = false;
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
<tags:initBootstrapDatepicker/>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/Invoice/Send/ViaEmail/List.do"
               focus="parameter(invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)"
               styleClass="form-horizontal">

        <div class="searchContainer ${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Invoice.sendViaEmail.title"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Invoice.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)"
                                    styleClass="${app2:getFormInputClasses()} mediumText"
                                    maxlength="80" tabindex="1"/>
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
                                                      mode="bootstrap"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true" tabindex="4"/>
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
                            <fmt:message key="Invoice.number"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="numberFrom_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <html:text property="parameter(numberFrom)"
                                               styleId="numberFrom_id"
                                               styleClass="${app2:getFormInputClasses()} shortText numberInputWidth"
                                               tabindex="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="numberTo_id">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <html:text property="parameter(numberTo)"
                                               styleId="numberTo_id"
                                               styleClass="${app2:getFormInputClasses()} shortText numberInputWidth"
                                               tabindex="3"/>
                                </div>
                            </div>
                        </div>
                    </div>
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
                                    <app:numberText property="parameter(openAmountFrom)"
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
                                    <app:numberText property="parameter(openAmountTo)" styleId="openAmountTo_id"
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
            <fanta:alphabet action="/Invoice/Send/ViaEmail/List.do" parameterName="alphabetName1" mode="bootstrap"/>
        </div>
    </html:form>

    <%
        if (request.getAttribute("invoiceSendViaEmailList") != null) {
            ResultList resultList = (ResultList) request.getAttribute("invoiceSendViaEmailList");
            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
        }
    %>

    <html:form action="/Invoice/Forward/Send/ViaEmail.do" styleId="formList" onsubmit="return testSubmit();">

        <html:hidden property="dto(allSendInvoiceIds)" value="${allSendInvoiceIds}" styleId="allSendInvoiceIds_id"/>
        <html:hidden property="dto(isAllSendInvoice)" value="${false}" styleId="isSendAll_id"/>
        <input type="hidden" name="isSubmit" value="false" id="isSubmit">

        <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
            <div class="wrapperButton">
                <c:if test="${size > 0}">
                    <html:button property="selectedSend" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="sendSelectedInvoices()"
                                 tabindex="8">
                        <fmt:message key="Invoice.sendViaEmail.button.sendSelected"/>
                    </html:button>
                    <html:button property="allSend" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="sendAllInvoices()"
                                 tabindex="9">
                        <fmt:message key="Invoice.sendViaEmail.button.sendAll"/>
                    </html:button>
                </c:if>
            </div>
        </app2:checkAccessRight>

        <div class="table-responsive">
            <fanta:table mode="bootstrap" list="invoiceSendViaEmailList"
                         styleClass="${app2:getFantabulousTableLargeClases()}"
                         width="100%"
                         id="invoice"
                         action="Invoice/Send/ViaEmail/List.do"
                         imgPath="${baselayout}"
                         align="center" withCheckBox="true">

                <%--address link--%>
                <tags:addressEditContextRelativeUrl varName="addressEditLink" addressId="${invoice.addressId}" addressType="${invoice.addressType}" name1="${invoice.addressName1}" name2="${invoice.addressName2}" name3="${invoice.addressName3}"/>

                <c:if test="${size > 0}">
                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                          property="invoiceId" headerStyle="listHeader"
                                          styleClass="listItemCenter" width="5%"/>

                </c:if>

                <fanta:dataColumn name="number" styleClass="listItem" title="Invoice.number"
                                  headerStyle="listHeader"
                                  width="16%"
                                  orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="invoiceTitle" styleClass="listItem" title="Invoice.title"
                                  headerStyle="listHeader"
                                  width="20%"
                                  orderable="true"/>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true" renderData="false">
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
                <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                                  headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                                  headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountNet}
                </fanta:dataColumn>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight"
                                  title="Invoice.totalAmountGross"
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

        <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
            <div class="wrapperButton">
                <c:if test="${size > 0}">
                    <html:button property="selectedSend" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="sendSelectedInvoices()"
                                 tabindex="10">
                        <fmt:message key="Invoice.sendViaEmail.button.sendSelected"/>
                    </html:button>
                    <html:button property="allSend" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="sendAllInvoices()"
                                 tabindex="11">
                        <fmt:message key="Invoice.sendViaEmail.button.sendAll"/>
                    </html:button>
                </c:if>
            </div>
        </app2:checkAccessRight>
    </html:form>
</div>