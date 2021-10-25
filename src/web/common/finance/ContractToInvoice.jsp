<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<fmt:message key="datePattern" var="datePattern"/>
<calendar:initialize/>


<c:set var="voucherType"><%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%></c:set>

<c:set var="failMap" value="${dto.invalidToInvoiceMap}"/>
<c:set var="hasInvalidToInvoice" value="${not empty failMap}"/>

<fmt:message var="button" key="Contract.invoice.generate"/>
<c:if test="${hasInvalidToInvoice}">
    <fmt:message var="button" key="ContractToInvoice.button.generateAnyway"/>
</c:if>


<html:form action="${action}" focus="dto(sequenceRuleId)">

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

    <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
        <tr>
            <td class="button">
                <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICE"
                                     styleClass="button" tabindex="1">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="2">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

    <table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td class="title" colspan="2">
                ${title}
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.sequenceRule"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(sequenceRuleId)"
                              listName="sequenceRuleList"
                              labelProperty="label"
                              valueProperty="numberId"
                              styleClass="middleSelect"
                              firstEmpty="true"
                              module="/catalogs"
                              readOnly="${hasInvalidToInvoice}"
                              tabIndex="5">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="type" value="${voucherType}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.template"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(templateId)"
                              listName="invoiceTemplateList"
                              labelProperty="title"
                              valueProperty="templateId"
                              styleClass="middleSelect"
                              module="/catalogs"
                              firstEmpty="true"
                              tabIndex="13"
                              readOnly="${hasInvalidToInvoice}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>

        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.invoiceDate"/>
            </td>
            <td class="contain">
                <app:dateText property="dto(invoiceDate)"
                              styleId="startDate"
                              calendarPicker="true"
                              datePatternKey="${datePattern}"
                              styleClass="text"
                              maxlength="10"
                              view="${hasInvalidToInvoice}"
                              tabindex="11"/>
            </td>
        </tr>
    </table>

    <%--invalid contracts or sale positions detail--%>
    <c:if test="${hasInvalidToInvoice}">
        <html:hidden property="dto(invoiceAnyway)" value="true"/>

        <table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">

            <c:if test="${not empty failMap.failPayCondition}">
                <tr>
                    <td colspan="2" class="title">
                        <fmt:message key="ContractToInvoice.invalid.withoutPayCondition"/>
                    </td>
                </tr>
                <c:forEach var="infoMap" items="${failMap.failPayCondition}">
                    <tr>
                        <td class="label" colspan="2">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>

            <c:if test="${not empty failMap.failProductAccount}">
                <tr>
                    <td colspan="2" class="title">
                        <fmt:message key="ContractToInvoice.invalid.withoutProductAccount"/>
                    </td>
                </tr>
                <c:forEach var="infoMap" items="${failMap.failProductAccount}">
                    <tr>
                        <td class="label" colspan="2">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>

            <c:if test="${not empty failMap.failVatRate}">
                <tr>
                    <td colspan="2" class="title">
                        <fmt:message key="ContractToInvoice.invalid.vatRateNotValid"/>
                    </td>
                </tr>
                <c:forEach var="infoMap" items="${failMap.failVatRate}">
                    <tr>
                        <td class="label" colspan="2">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            
            <c:if test="${not empty failMap.failCurrency}">
                <tr>
                    <td colspan="2" class="title">
                        <fmt:message key="ContractToInvoice.invalid.withoutCurrency"/>
                    </td>
                </tr>
                <c:forEach var="infoMap" items="${failMap.failCurrency}">
                    <tr>
                        <td class="label" colspan="2">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            
            <c:if test="${not empty failMap.failNetGross}">
                <tr>
                    <td colspan="2" class="title">
                        <fmt:message key="ContractToInvoice.invalid.withoutNetGross"/>
                    </td>
                </tr>
                <c:forEach var="infoMap" items="${failMap.failNetGross}">
                    <tr>
                        <td class="label" colspan="2">
                            <c:set var="item" value="${infoMap}" scope="request"/>
                            <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </c:if>

    <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
        <tr>
            <td class="button">

                <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICE"
                                     styleClass="button" tabindex="6">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="7">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>
