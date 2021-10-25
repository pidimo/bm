<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:if test="${empty(incomingInvoiceId)}">
    <c:set var="incomingInvoiceId" value="${incomingPaymentForm.dtoMap['incomingInvoiceId']}"/>
</c:if>
<c:if test="${empty(incomingInvoiceVersion)}">
    <c:set var="incomingInvoiceVersion" value="${incomingPaymentForm.dtoMap['incomingInvoiceVersion']}"/>
</c:if>

<html:form action="${action}" focus="dto(payDate)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(incomingInvoiceId)" value="${incomingInvoiceId}"/>
        <html:hidden property="dto(incomingInvoiceVersion)" value="${incomingInvoiceVersion}"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(paymentId)"/>
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INCOMINGPAYMENT"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="8">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="9">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                    <%--amounts info of incoming invoice--%>
                <c:if test="${op == 'create'}">
                    <c:set var="incomingInvoiceInfo"
                           value="${app2:getIncomingInvoiceInfoMap(incomingInvoiceId, pageContext.request)}"/>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Finance.incomingInvoice.amountNet"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="totalAmountNet"
                                              value="${incomingInvoiceInfo.amountNet}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                                ${totalAmountNet}
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Finance.incomingInvoice.amountGross"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="totalAmountGross"
                                              value="${incomingInvoiceInfo.amountGross}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                                ${totalAmountGross}
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Finance.incomingInvoice.openAmount"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <fmt:formatNumber var="openAmount"
                                              value="${incomingInvoiceInfo.openAmount}"
                                              type="number"
                                              pattern="${numberFormat}"/>
                                ${openAmount}
                        </div>
                    </div>

                </c:if>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startDate">
                        <fmt:message key="Finance.incomingPayment.payDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(payDate)"
                                          styleId="startDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          currentDate="true"
                                          view="${'delete' == op}"
                                          tabindex="1"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_price">
                        <fmt:message key="Finance.incomingPayment.amount"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(amount)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="12"
                                        numberType="decimal"
                                        maxInt="10"
                                        maxFloat="2"
                                        styleId="field_price"
                                        view="${'delete' == op}"
                                        tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="notes_id">
                        <fmt:message key="Finance.incomingPayment.text"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete'== op)}">
                        <html:textarea property="dto(notes)"
                                       styleId="notes_id"
                                       styleClass="form-control mediumDetailHigh"
                                       style="height:120px;width:99%;"
                                       readonly="${'delete'== op}"
                                       tabindex="3"/>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INCOMINGPAYMENT"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="5">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="6">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="incomingPaymentForm"/>