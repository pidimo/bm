<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<!--verifies that you can still make payments.-->
<c:set var="payInvoice" value="${true}"/>

<!--verifies whether the invoice is a CreditNote and if is related with another invoice.-->
<c:set var="creditNoteIsRelatedWithInvoice" value="${app2:creditNoteIsRelatedWithInvoice(param.invoiceId, pageContext.request)}"/>

<!--verifies whether the payment is an associate CreditNote.-->
<c:set var="isAssociatedCreditNote" value="${false}"/>

<html:form action="${action}" focus="dto(payDate)">

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(invoiceId)" value="${param.invoiceId}"/>

<c:if test="${('update' == op) || ('delete' == op)}">
    <html:hidden property="dto(paymentId)"/>
    <c:set var="isAssociatedCreditNote" value="${null != invoicePaymentForm.dtoMap['creditNoteId']}"/>
</c:if>
<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
    <c:choose>
        <c:when test="${null == invoicePaymentForm.dtoMap['oldAmount']}">
            <html:hidden property="dto(oldAmount)" value="${invoicePaymentForm.dtoMap['amount']}"/>
        </c:when>
        <c:otherwise>
            <html:hidden property="dto(oldAmount)"/>
        </c:otherwise>
    </c:choose>
    <c:set var="payInvoice" value="${app2:canPayInvoice(param.invoiceId, pageContext.request)}"/>
</c:if>
<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<!--
    Enable or disable submit button.
    When op = create always show button, for delete and update operations show submit
    button when  not  associated  with  a  credit note with relation to invoice
    and still be able to payments.
-->
<c:set var="showSubmitButton" value="${true}"/>
<c:if test="${'create' != op}">
    <c:set var="showSubmitButton" value="${payInvoice && !creditNoteIsRelatedWithInvoice && !isAssociatedCreditNote}"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="2" class="button">
            <c:if test="${showSubmitButton}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEPAYMENT"
                                     styleClass="button"
                                     tabindex="8">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEPAYMENT"
                                         styleClass="button"
                                         property="SaveAndNew"
                                         tabindex="9">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>

            </c:if>
            <html:cancel styleClass="button" tabindex="10">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </td>
    </tr>
    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>

    <%--amounts info of invoice--%>
    <c:if test="${op == 'create'}">
        <c:set var="invoiceInfo" value="${app2:getInvoice(param.invoiceId, pageContext.request)}"/>
        <c:set var="invoiceIdVar" value="${param.invoiceId}"/>

        <tr>
            <td class="label">
                <fmt:message key="InvoicePayment.invoice"/>
            </td>
            <td class="contain">

                <c:out value="${invoiceInfo.number}"/>

                <app:link contextRelative="true"
                          action="finance/Invoice/Forward/Update.do?invoiceId=${invoiceIdVar}&dto(invoiceId)=${invoiceIdVar}&dto(number)=${app2:encode(invoiceInfo.number)}&index=0">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="InvoicePayment.invoice.edit" border="0"/>
                </app:link>

                <c:if test="${not empty invoiceInfo.documentId}">
                    <app:link contextRelative="true"
                              action="finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceIdVar}">
                        <html:img src="${baselayout}/img/pdf.png" titleKey="InvoicePayment.invoice.document" border="0"/>
                    </app:link>
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.totalAmountNet"/>
            </td>
            <td class="contain">
                <fmt:formatNumber var="totalAmountNet"
                                  value="${invoiceInfo.totalAmountNet}"
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
                <fmt:formatNumber var="totalAmountGross"
                                  value="${invoiceInfo.totalAmountGross}"
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
                <fmt:formatNumber var="openAmount"
                                  value="${invoiceInfo.openAmount}"
                                  type="number"
                                  pattern="${numberFormat}"/>
                ${openAmount}
            </td>
        </tr>
    </c:if>

    <tr>
        <TD class="label" width="25%">
            <fmt:message key="InvoicePayment.payDate"/>
        </TD>
        <TD class="contain" width="75%">
            <app:dateText property="dto(payDate)"
                          styleId="startDate"
                          calendarPicker="${op != 'delete' || !payInvoice || !showSubmitButton}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          currentDate="true"
                          view="${'delete' == op || !payInvoice || !showSubmitButton}"
                          tabindex="1"/>
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
                            view="${'delete' == op || !payInvoice || !showSubmitButton}"
                            tabindex="2"/>
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
                          readOnly="${'delete' == op}"
                          tabIndex="3">
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
                           readonly="${'delete'== op || !payInvoice || !showSubmitButton}"
                           tabindex="4"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="button">
            <c:if test="${showSubmitButton}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEPAYMENT"
                                     styleClass="button"
                                     tabindex="5">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEPAYMENT"
                                         styleClass="button"
                                         property="SaveAndNew"
                                         tabindex="6">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>

            </c:if>
            <html:cancel styleClass="button" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </td>
    </tr>
</table>
</html:form>