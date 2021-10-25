<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<script language="JavaScript" type="text/javascript">

    function setAdditionalInvoiceInfo(totalAmountNet, totalAmountGross, openAmount, documentId) {
        document.getElementById('amountNet_id').value = totalAmountNet;
        document.getElementById('amountGross_id').value = totalAmountGross;
        document.getElementById('openAmount_id').value = openAmount;
        document.getElementById('documentId_id').value = documentId;

        //default amount value
        document.getElementById('field_price').value = openAmount;
    }

</script>

<html:form action="${action}" focus="dto(number)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="INVOICEPAYMENT" styleClass="${app2:getFormButtonClasses()}" property="save"
                                 tabindex="9">
                ${button}
            </app2:securitySubmit>
            <app2:securitySubmit operation="${op}" functionality="INVOICEPAYMENT" styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew"
                                 tabindex="10">
                <fmt:message key="Common.saveAndNew"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="11">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="fieldInvoiceNumber_id">
                        <fmt:message key="InvoicePayment.invoice"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="input-group">
                            <app:text property="dto(number)"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="40"
                                      styleId="fieldInvoiceNumber_id"
                                      readonly="true"
                                      tabindex="1"/>
                            <!--put parameter(openAmount)=0 to list only open amounts-->

                            <html:hidden property="dto(documentId)" styleId="documentId_id"/>
                            <c:set var="invoiceIdVar" value="${invoicePaymentInOneStepForm.dtoMap['invoiceId']}"/>
                            <c:set var="hasInvoiceSelected" value="${not empty invoiceIdVar}"/>
                            <html:hidden property="dto(invoiceId)" styleId="fieldInvoiceId_id"/>

                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup url="/finance/Invoice/Search.do?chekIfButtonPressed=false&parameter(openAmount)=0&sendAdditionalInfo=true"
                                                           name="invoiceSearchList"
                                                           titleKey="Common.search"
                                                           submitOnSelect="true"
                                                           tabindex="1"
                                                           modalTitleKey="Invoice.singleSearch"
                                                           isLargeModal="true"
                                                           styleId="contactSelectPopup_id"/>

                                <tags:clearBootstrapSelectPopup keyFieldId="fieldInvoiceId_id"
                                                       nameFieldId="fieldInvoiceNumber_id"
                                                       titleKey="Common.clear"
                                                       submitOnClear="true"
                                                       tabindex="2"/>

                                <c:if test="${hasInvoiceSelected}">
                                    <fmt:message var="titleEdit" key="InvoicePayment.invoice.edit"/>
                                    <app:link contextRelative="true" styleClass="btn btn-default" title="${titleEdit}"
                                              action="finance/Invoice/Forward/Update.do?invoiceId=${invoiceIdVar}&dto(invoiceId)=${invoiceIdVar}&dto(number)=${app2:encode(invoicePaymentInOneStepForm.dtoMap['number'])}&index=0">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>

                                    <c:if test="${not empty invoicePaymentInOneStepForm.dtoMap['documentId']}">
                                        <fmt:message var="titleDocument" key="InvoicePayment.invoice.document"/>
                                        <app:link contextRelative="true" styleClass="btn btn-default" title="${titleDocument}"
                                                  action="finance/Download/Invoice/Document.do?dto(freeTextId)=${invoicePaymentInOneStepForm.dtoMap['documentId']}&dto(invoiceId)=${invoiceIdVar}">
                                            <span class="glyphicon glyphicon-open-file"></span>
                                        </app:link>
                                    </c:if>
                                </c:if>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <%--<tr ${hasInvoiceSelected ? "" : "style=\"display: none;\""}>--%>
                <div ${hasInvoiceSelected ? "" : "style=\"display: none;\""}>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Invoice.totalAmountNet"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="totalAmountNet"
                                              value="${invoicePaymentInOneStepForm.dtoMap['totalAmountNet']}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                            <html:hidden property="dto(totalAmountNet)" styleId="amountNet_id"/>
                            ${totalAmountNet}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Invoice.totalAmountGross"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <html:hidden property="dto(totalAmountGross)" styleId="amountGross_id"/>
                            <fmt:formatNumber var="totalAmountGross"

                                              value="${invoicePaymentInOneStepForm.dtoMap['totalAmountGross']}"
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
                                              value="${invoicePaymentInOneStepForm.dtoMap['openAmount']}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                            <html:hidden property="dto(openAmount)" styleId="openAmount_id"/>
                            ${openAmount}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startDate">
                        <fmt:message key="InvoicePayment.payDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <div class="input-group date">
                            <app:dateText property="dto(payDate)"
                                          styleId="startDate"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          currentDate="true"
                                          tabindex="3"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_price">
                        <fmt:message key="InvoicePayment.amount"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <app:numberText property="dto(amount)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="12"
                                        numberType="decimal"
                                        maxInt="10"
                                        maxFloat="2"
                                        styleId="field_price"
                                        tabindex="4"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankAccountId_id">
                        <fmt:message key="InvoicePayment.bankAccount"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <fanta:select property="dto(bankAccountId)"
                                      styleId="bankAccountId_id"
                                      listName="paymentBankAccountList"
                                      labelProperty="description"
                                      valueProperty="bankAccountId"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      module="/contacts"
                                      firstEmpty="true"
                                      tabIndex="5">
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

                    <div class="${app2:getFormContainClasses(false)}">
                        <html:textarea property="dto(text)"
                                       styleId="text_id"
                                       styleClass="form-control mediumDetailHigh"
                                       style="height:120px;width:100%;"
                                       tabindex="6"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="INVOICEPAYMENT" styleClass="${app2:getFormButtonClasses()}" property="save"
                                 tabindex="7">
                ${button}
            </app2:securitySubmit>
            <app2:securitySubmit operation="${op}" functionality="INVOICEPAYMENT" styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew"
                                 tabindex="8">
                <fmt:message key="Common.saveAndNew"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="9">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="invoicePaymentInOneStepForm"/>