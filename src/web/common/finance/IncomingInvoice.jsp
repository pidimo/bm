<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>

<html:form action="${action}" focus="dto(type)">

<html:hidden property="dto(op)" value="${op}" styleId="dto_op"/>

    
<c:if test="${'update'== op || 'delete'== op}">
    <html:hidden property="dto(incomingInvoiceId)"/>
    <html:hidden property="dto(notesId)"/>
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(companyId)"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>
<c:if test="${op=='create'}">
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="95%" align="center" class="container">
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INCOMINGINVOICE"
                             styleClass="button"
                             property="save"
                             tabindex="15">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="16">
            <fmt:message key="Common.cancel"/>
        </html:cancel>

        <%--top links--%>
        &nbsp;
        <c:if test="${op=='update' && fromContacts}">
            <app:link action="/finance/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoiceForm.dtoMap.incomingInvoiceId}&dto(op)=read&incomingInvoiceId=${incomingInvoiceForm.dtoMap.incomingInvoiceId}&tabKey=Finance.IncomingInvoice.detail" contextRelative="true">
                <fmt:message key="Invoice.editDetails"/>
            </app:link>
        </c:if>
    </td>
</tr>
<tr>
    <td colspan="4" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<tr>
    <td width="15%" class="label">
        <fmt:message key="Finance.incomingInvoice.type"/>
    </td>
    <td width="35%" class="contain">
        <html:select property="dto(type)"
                             styleClass="mediumSelect"
                             readonly="${op=='delete'}"
                             tabindex="1">
                    <html:option value=""/>
                    <html:options collection="invoiceTypeList"
                                  property="value"
                                  labelProperty="label"/>
                </html:select>
    </td>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="mediumSelect"
                      module="/catalogs"
                      tabIndex="8" readOnly="${op=='delete'}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td width="15%" class="label">
        <fmt:message key="Finance.incomingInvoice.invoiceNumber"/>
    </td>
    <td width="35%" class="contain">
        <app:text property="dto(invoiceNumber)" maxlength="30" view="${op=='delete'}" tabindex="2" styleClass="mediumText"/>
    </td>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
    </td>
    <td class="contain">

        <app:dateText property="dto(invoiceDate)" maxlength="10" tabindex="9" styleId="invoiceDate_id"
                      calendarPicker="${op!='delete'}" datePatternKey="${datePattern}" styleClass="dateText" convert="true"
                      currentDate="${op=='create'}" view="${op=='delete'}"/>
    </td>

</tr>
<tr>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.supplierName"/>
    </td>
    <td class="contain">
        <c:choose>
            <c:when test="${op=='create' && fromContacts}">
                <html:hidden property="dto(supplierId)" styleId="fieldAddressId_id" value="${param.contactId}"/>
            </c:when>
            <c:otherwise>
                <html:hidden property="dto(supplierId)" styleId="fieldAddressId_id"/>
                <app:text property="dto(supplierName)" styleId="fieldAddressName_id" styleClass="mediumText"
                          readonly="true" view="${op == 'delete' || fromContacts}"/>

                <tags:selectPopup url="/contacts/SearchAddress.do?allowCreation=2" name="SearchSupplier"
                                  titleKey="Common.search" hide="${op == 'delete' || fromContacts}" submitOnSelect="false" tabindex="3"/>

                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear" hide="${op == 'delete' || fromContacts}" submitOnClear="false" tabindex="4"/>
            </c:otherwise>
        </c:choose>

    </td>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.receiptDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(receiptDate)" maxlength="10" tabindex="10" styleId="receiptDate_id"
                      calendarPicker="${op!='delete'}" datePatternKey="${datePattern}" styleClass="dateText" convert="true"
                      currentDate="${op=='create'}" view="${op=='delete'}"/>
    </td>

</tr>
<tr>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.amountNet"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(amountNet)" styleClass="numberText" tabindex="5" maxlength="13"
                        view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.toBePaidUntil"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(toBePaidUntil)" maxlength="10" tabindex="11" styleId="toBePaidUntil_id"
                      calendarPicker="${op!='delete'}" datePatternKey="${datePattern}" styleClass="dateText" convert="true"
                      view="${op=='delete'}"/>
    </td>

</tr>
<tr>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.amountGross"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(amountGross)" styleClass="numberText" tabindex="6" maxlength="13"
                        view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.paidUntil"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(paidUntil)" maxlength="10" tabindex="12" styleId="paidUntil_id"
                      calendarPicker="false" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true" view="true"/>
    </td>

</tr>
<tr>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.openAmount"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(openAmount)" styleClass="numberText" tabindex="7" maxlength="13"
                        view="true" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="contain" colspan="2">&nbsp;</td>
    
</tr>
<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="Invoice.notes"/>
        <html:textarea property="dto(notes)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       tabindex="14"
                       readonly="${'delete' == op}"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INCOMINGINVOICE"
                             styleClass="button"
                             property="save"
                             tabindex="15">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="16">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>