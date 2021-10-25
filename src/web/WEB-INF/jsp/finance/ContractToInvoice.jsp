<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<fmt:message key="datePattern" var="datePattern"/>

<tags:initBootstrapDatepicker/>

<c:set var="voucherType"><%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>
</c:set>
<c:set var="failMap" value="${dto.invalidToInvoiceMap}"/>
<c:set var="hasInvalidToInvoice" value="${not empty failMap}"/>

<fmt:message var="button" key="Contract.invoice.generate"/>

<c:if test="${hasInvalidToInvoice}">
    <fmt:message var="button" key="ContractToInvoice.button.generateAnyway"/>
</c:if>

<html:form action="${action}" focus="dto(sequenceRuleId)" styleClass="form-horizontal">

    <c:forEach var="id" items="${contractIdsList}">
        <html:hidden property="contractIds" value="${id}" styleId="contractId${id}"/>
    </c:forEach>

    <c:if test="${not empty salePositionIdsList}">
        <c:forEach var="id" items="${salePositionIdsList}">
            <html:hidden property="salePositionIds" value="${id}" styleId="positionId${id}"/>
        </c:forEach>
    </c:if>

    <html:hidden property="dto(contractDateFilter)"/>
    <html:hidden property="dto(isFromContracts)"/>

    <%--invoice type--%>
    <html:hidden property="dto(type)"/>

    <div class="${app2:getFormClasses()}">

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICE"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="6">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    ${title}
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sequenceRuleId_id">
                        <fmt:message key="Invoice.sequenceRule"/>
                    </label>

                    <div class="${app2:getFormContainClasses(hasInvalidToInvoice)}">
                        <fanta:select property="dto(sequenceRuleId)"
                                      listName="sequenceRuleList"
                                      labelProperty="label"
                                      valueProperty="numberId"
                                      styleId="sequenceRuleId_id"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      firstEmpty="true"
                                      module="/catalogs"
                                      readOnly="${hasInvalidToInvoice}"
                                      tabIndex="1">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="${voucherType}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="templateId_id">
                        <fmt:message key="Invoice.template"/>
                    </label>

                    <div class="${app2:getFormContainClasses(hasInvalidToInvoice)}">
                        <fanta:select property="dto(templateId)"
                                      listName="invoiceTemplateList"
                                      labelProperty="title"
                                      styleId="templateId_id"
                                      valueProperty="templateId"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      module="/catalogs"
                                      firstEmpty="true"
                                      tabIndex="2"
                                      readOnly="${hasInvalidToInvoice}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>


                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startDate">
                        <fmt:message key="Invoice.invoiceDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses(hasInvalidToInvoice)}">
                        <div class="input-group date">
                            <app:dateText property="dto(invoiceDate)"
                                          styleId="startDate"
                                          mode="bootstrap"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          view="${hasInvalidToInvoice}"
                                          tabindex="3"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>


                <%--invalid contracts or sale positions detail--%>
                <c:if test="${hasInvalidToInvoice}">
                    <html:hidden property="dto(invoiceAnyway)" value="true"/>

                    <c:if test="${not empty failMap.failPayCondition}">
                        <legend class="title">
                            <fmt:message key="ContractToInvoice.invalid.withoutPayCondition"/>
                        </legend>

                        <c:forEach var="infoMap" items="${failMap.failPayCondition}">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                        </c:forEach>
                    </c:if>

                    <c:if test="${not empty failMap.failProductAccount}">
                        <legend class="title">
                            <fmt:message key="ContractToInvoice.invalid.withoutProductAccount"/>
                        </legend>

                        <c:forEach var="infoMap" items="${failMap.failProductAccount}">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                        </c:forEach>
                    </c:if>

                    <c:if test="${not empty failMap.failVatRate}">
                        <legend class="title">
                            <fmt:message key="ContractToInvoice.invalid.vatRateNotValid"/>
                        </legend>

                        <c:forEach var="infoMap" items="${failMap.failVatRate}">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                        </c:forEach>
                    </c:if>

                    <c:if test="${not empty failMap.failCurrency}">
                        <legend class="title">
                            <fmt:message key="ContractToInvoice.invalid.withoutCurrency"/>
                        </legend>

                        <c:forEach var="infoMap" items="${failMap.failCurrency}">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                        </c:forEach>
                    </c:if>

                    <c:if test="${not empty failMap.failNetGross}">
                        <legend class="title">
                            <fmt:message key="ContractToInvoice.invalid.withoutNetGross"/>
                        </legend>

                        <c:forEach var="infoMap" items="${failMap.failNetGross}">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                        </c:forEach>
                    </c:if>

                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICE"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="4">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="5">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="contractInvoiceCreateForm"/>