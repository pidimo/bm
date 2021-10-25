<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<script language="JavaScript" type="text/javascript">

    function setAdditionalInvoiceInfo(totalAmountNet, totalAmountGross, openAmount, documentId) {
        document.getElementById('amountNet_id').value = totalAmountNet;
        document.getElementById('amountGross_id').value = totalAmountGross;
        document.getElementById('openAmount_id').value = openAmount;
        document.getElementById('documentId_id').value = documentId;
    }

</script>

<html:form action="${action}" focus="dto(number)">

<html:hidden property="dto(op)" value="${op}"/>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
<tr>
    <td colspan="2" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INVOICEPAYMENT"
                             styleClass="button"
                             property="save"
                             tabindex="9">
            ${button}
        </app2:securitySubmit>
        <app2:securitySubmit operation="${op}" functionality="INVOICEPAYMENT" styleClass="button"
                             property="SaveAndNew" tabindex="10">
            <fmt:message key="Common.saveAndNew"/>
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="11">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<TR>
    <TD colspan="2" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<tr>
    <td class="label">
        <fmt:message key="InvoicePayment.invoice"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(documentId)" styleId="documentId_id"/>
        <c:set var="invoiceIdVar" value="${invoicePaymentInOneStepForm.dtoMap['invoiceId']}"/>
        <c:set var="hasInvoiceSelected" value="${not empty invoiceIdVar}"/>

        <html:hidden property="dto(invoiceId)" styleId="fieldInvoiceId_id"/>
        <app:text property="dto(number)"
                  styleClass="middleText" maxlength="40"
                  styleId="fieldInvoiceNumber_id"
                  readonly="true"
                tabindex="1"/>
        <!--put parameter(openAmount)=0 to list only open amounts-->
        <tags:selectPopup url="/finance/Invoice/Search.do?chekIfButtonPressed=false&parameter(openAmount)=0&sendAdditionalInfo=true"
                          name="invoiceSearchList"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          width="720"
                          tabindex="1"/>
        <tags:clearSelectPopup keyFieldId="fieldInvoiceId_id"
                               nameFieldId="fieldInvoiceNumber_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               tabindex="2"/>

        <c:if test="${hasInvoiceSelected}">
            <app:link contextRelative="true"
                      action="finance/Invoice/Forward/Update.do?invoiceId=${invoiceIdVar}&dto(invoiceId)=${invoiceIdVar}&dto(number)=${app2:encode(invoicePaymentInOneStepForm.dtoMap['number'])}&index=0">
                <img src="<c:out value="${sessionScope.baselayout}"/>/img/edit.gif" title="<fmt:message key="InvoicePayment.invoice.edit"/>" border="0" align="middle" alt=""/>
            </app:link>

            <c:if test="${not empty invoicePaymentInOneStepForm.dtoMap['documentId']}">
                <app:link contextRelative="true"
                          action="finance/Download/Invoice/Document.do?dto(freeTextId)=${invoicePaymentInOneStepForm.dtoMap['documentId']}&dto(invoiceId)=${invoiceIdVar}">
                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/pdf.png" title="<fmt:message key="InvoicePayment.invoice.document"/>" border="0" align="middle" alt=""/>
                </app:link>
            </c:if>
        </c:if>
    </td>
</tr>
<tr ${hasInvoiceSelected ? "" : "style=\"display: none;\""}>
    <td colspan="2">
        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
            <tr>
                <td class="label" width="25%">
                    <fmt:message key="Invoice.totalAmountNet"/>
                </td>
                <td class="contain" width="75%">
                    <html:hidden property="dto(totalAmountNet)" styleId="amountNet_id"/>
                    <fmt:formatNumber var="totalAmountNet"
                                      value="${invoicePaymentInOneStepForm.dtoMap['totalAmountNet']}"
                                      type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountNet}
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Invoice.totalAmountGross"/>
                </td>
                <td class="contain">
                    <html:hidden property="dto(totalAmountGross)" styleId="amountGross_id"/>
                    <fmt:formatNumber var="totalAmountGross"
                                      value="${invoicePaymentInOneStepForm.dtoMap['totalAmountGross']}"
                                      type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Invoice.openAmount"/>
                </td>
                <td class="contain">
                    <html:hidden property="dto(openAmount)" styleId="openAmount_id"/>
                    <fmt:formatNumber var="openAmount"
                                      value="${invoicePaymentInOneStepForm.dtoMap['openAmount']}"
                                      type="number"
                                      pattern="${numberFormat}"/>
                        ${openAmount}
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <TD class="label" width="25%">
        <fmt:message key="InvoicePayment.payDate"/>
    </TD>
    <TD class="contain" width="75%">
        <app:dateText property="dto(payDate)"
                      styleId="startDate"
                      calendarPicker="true"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      currentDate="true"
                      tabindex="3"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="InvoicePayment.amount"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(amount)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="4"/>
    </TD>
</tr>
<tr>
    <td class="label">
        <fmt:message key="InvoicePayment.bankAccount"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(bankAccountId)"
                      listName="paymentBankAccountList"
                      labelProperty="description"
                      valueProperty="bankAccountId"
                      styleClass="middleSelect"
                      module="/contacts"
                      firstEmpty="true"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="topLabel" colspan="2">
        <fmt:message key="InvoicePayment.text"/>
        <html:textarea property="dto(text)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       tabindex="6"/>
    </td>
</tr>
<tr>
    <td colspan="2" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INVOICEPAYMENT"
                             styleClass="button"
                             property="save"
                             tabindex="7">
            ${button}
        </app2:securitySubmit>
        <app2:securitySubmit operation="${op}" functionality="INVOICEPAYMENT" styleClass="button"
                             property="SaveAndNew" tabindex="8">
            <fmt:message key="Common.saveAndNew"/>
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="9">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>