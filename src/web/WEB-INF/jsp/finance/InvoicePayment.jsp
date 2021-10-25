<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<!--verifies that you can still make payments.-->
<c:set var="payInvoice" value="${true}"/>

<!--verifies whether the invoice is a CreditNote and if is related with another invoice.-->
<c:set var="creditNoteIsRelatedWithInvoice"
       value="${app2:creditNoteIsRelatedWithInvoice(param.invoiceId, pageContext.request)}"/>

<!--verifies whether the payment is an associate CreditNote.-->
<c:set var="isAssociatedCreditNote" value="${false}"/>

<html:form action="${action}" focus="dto(payDate)" styleClass="form-horizontal">

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
    button when not associated with a credit note with relation to invoice
    and still be able to payments.
    -->
    <c:set var="showSubmitButton" value="${true}"/>
    <c:if test="${'create' != op}">
        <c:set var="showSubmitButton"
               value="${payInvoice && !creditNoteIsRelatedWithInvoice && !isAssociatedCreditNote}"/>
    </c:if>

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${showSubmitButton}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEPAYMENT"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="8">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEPAYMENT"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="SaveAndNew"
                                         tabindex="9">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>

            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="10">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                    <%--amounts info of invoice--%>
                <c:if test="${op == 'create'}">
                    <c:set var="invoiceInfo" value="${app2:getInvoice(param.invoiceId, pageContext.request)}"/>
                    <c:set var="invoiceIdVar" value="${param.invoiceId}"/>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="InvoicePayment.invoice"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <c:out value="${invoiceInfo.number}"/>
                            <div class="pull-right">
                                <fmt:message var="titleEdit" key="InvoicePayment.invoice.edit"/>
                                <app:link contextRelative="true" title="${titleEdit}"
                                          action="finance/Invoice/Forward/Update.do?invoiceId=${invoiceIdVar}&dto(invoiceId)=${invoiceIdVar}&dto(number)=${app2:encode(invoiceInfo.number)}&index=0">
                                    <span class="glyphicon glyphicon-edit"></span>
                                </app:link>

                                <c:if test="${not empty invoiceInfo.documentId}">
                                    <fmt:message var="titleDocument" key="InvoicePayment.invoice.document"/>
                                    <app:link contextRelative="true" title="${titleDocument}"
                                              action="finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceIdVar}">
                                        <span class="glyphicon glyphicon-open-file"></span>
                                    </app:link>
                                </c:if>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Invoice.totalAmountNet"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="totalAmountNet"
                                              value="${invoiceInfo.totalAmountNet}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                                ${totalAmountNet}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Invoice.totalAmountGross"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="totalAmountGross"
                                              value="${invoiceInfo.totalAmountGross}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                                ${totalAmountGross}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Invoice.openAmount"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="openAmount"
                                              value="${invoiceInfo.openAmount}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                                ${openAmount}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startDate">
                        <fmt:message key="InvoicePayment.payDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || !payInvoice || !showSubmitButton)}">
                        <div class="input-group date">
                            <app:dateText property="dto(payDate)"
                                          styleId="startDate"
                                          calendarPicker="${op != 'delete' || !payInvoice || !showSubmitButton}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          currentDate="true"
                                          view="${'delete' == op || !payInvoice || !showSubmitButton}"
                                          tabindex="1"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_price">
                        <fmt:message key="InvoicePayment.amount"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || !payInvoice || !showSubmitButton)}">
                        <app:numberText property="dto(amount)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="12"
                                        numberType="decimal"
                                        maxInt="10"
                                        maxFloat="2"
                                        styleId="field_price"
                                        view="${'delete' == op || !payInvoice || !showSubmitButton}"
                                        tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankAccountId_id">
                        <fmt:message key="InvoicePayment.bankAccount"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(bankAccountId)"
                                      styleId="bankAccountId_id"
                                      listName="paymentBankAccountList"
                                      labelProperty="description"
                                      valueProperty="bankAccountId"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      module="/contacts"
                                      firstEmpty="true"
                                      readOnly="${'delete' == op}"
                                      tabIndex="3">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="text_id">
                        <fmt:message key="InvoicePayment.text"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete'== op || !payInvoice || !showSubmitButton)}">
                        <html:textarea property="dto(text)"
                                       styleId="text_id"
                                       styleClass="form-control mediumDetailHigh"
                                       style="height:120px;width:99%;"
                                       readonly="${'delete'== op || !payInvoice || !showSubmitButton}"
                                       tabindex="4"/>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${showSubmitButton}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEPAYMENT"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="5">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEPAYMENT"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="SaveAndNew"
                                         tabindex="6">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>

            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="invoicePaymentForm"/>