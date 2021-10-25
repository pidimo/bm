<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:if test="${empty(incomingInvoiceId)}">
    <c:set var="incomingInvoiceId" value="${incomingPaymentForm.dtoMap['incomingInvoiceId']}"/>
</c:if>
<c:if test="${empty(incomingInvoiceVersion)}">
    <c:set var="incomingInvoiceVersion" value="${incomingPaymentForm.dtoMap['incomingInvoiceVersion']}"/>
</c:if>

<html:form action="${action}" focus="dto(payDate)">

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(incomingInvoiceId)" value="${incomingInvoiceId}"/>
<html:hidden property="dto(incomingInvoiceVersion)" value="${incomingInvoiceVersion}"/>

<c:if test="${('update' == op) || ('delete' == op)}">
    <html:hidden property="dto(paymentId)"/>
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="2" class="button">
            <app2:securitySubmit operation="${op}"
                                 functionality="INCOMINGPAYMENT"
                                 styleClass="button"
                                 tabindex="8">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="9">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </td>
    </tr>
    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>

    <%--amounts info of incoming invoice--%>
    <c:if test="${op == 'create'}">
        <c:set var="incomingInvoiceInfo" value="${app2:getIncomingInvoiceInfoMap(incomingInvoiceId, pageContext.request)}"/>

        <tr>
            <td class="label">
                <fmt:message key="Finance.incomingInvoice.amountNet"/>
            </td>
            <td class="contain">
                <fmt:formatNumber var="totalAmountNet"
                                  value="${incomingInvoiceInfo.amountNet}"
                                  type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountNet}
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Finance.incomingInvoice.amountGross"/>
            </td>
            <td class="contain">
                <fmt:formatNumber var="totalAmountGross"
                                  value="${incomingInvoiceInfo.amountGross}"
                                  type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountGross}
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Finance.incomingInvoice.openAmount"/>
            </td>
            <td class="contain">
                <fmt:formatNumber var="openAmount"
                                  value="${incomingInvoiceInfo.openAmount}"
                                  type="number"
                                  pattern="${numberFormat}"/>
                ${openAmount}
            </td>
        </tr>
    </c:if>

    <tr>
        <TD class="label" width="25%">
            <fmt:message key="Finance.incomingPayment.payDate"/>
        </TD>
        <TD class="contain" width="75%">
            <app:dateText property="dto(payDate)"
                          styleId="startDate"
                          calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          currentDate="true"
                          view="${'delete' == op}"
                          tabindex="1"/>
        </TD>
    </tr>
    <tr>
        <TD class="label">
            <fmt:message key="Finance.incomingPayment.amount"/>
        </TD>
        <TD class="contain">
            <app:numberText property="dto(amount)"
                            styleClass="numberText"
                            maxlength="12"
                            numberType="decimal"
                            maxInt="10"
                            maxFloat="2"
                            styleId="field_price"
                            view="${'delete' == op}"
                            tabindex="2"/>
        </TD>
    </tr>
    <tr>
        <td class="topLabel" colspan="2">
            <fmt:message key="Finance.incomingPayment.text"/>
            <html:textarea property="dto(notes)"
                           styleClass="mediumDetailHigh"
                           style="height:120px;width:99%;"
                           readonly="${'delete'== op}"
                           tabindex="3"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="button">
            <app2:securitySubmit operation="${op}"
                                 functionality="INCOMINGPAYMENT"
                                 styleClass="button"
                                 tabindex="5">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="6">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </td>
    </tr>
</table>
</html:form>