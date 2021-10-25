<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<script>
    function resetFields() {
        var form = document.invoiceAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>
<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>


<html:form action="/Invoice/AdvancedList.do" focus="parameter(number)">
    <table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
        <tr>
            <td class="title" colspan="4">
                <fmt:message key="Invoice.advancedSearch"/>
            </td>
        </tr>
        <TR>
            <td class="label" width="15%">
                <fmt:message key="Invoice.number"/>
            </td>
            <td class="contain" width="35%">
                <html:text property="parameter(number)"
                           styleClass="mediumText" tabindex="1"/>
            </td>
            <td class="label" width="15%">
                <fmt:message key="Invoice.payCondition"/>
            </td>
            <td class="contain" width="35%">
                <fanta:select property="parameter(payConditionId)"
                              listName="payConditionList"
                              labelProperty="name"
                              valueProperty="id"
                              styleClass="middleSelect"
                              firstEmpty="true"
                              module="/catalogs"
                              tabIndex="13">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </TR>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.contact"/>
            </td>
            <td class="contain">
                <html:text
                        property="parameter(invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3)"
                        styleClass="mediumText"
                        maxlength="80" tabindex="2"/>
            </td>
            <td class="label">
                <fmt:message key="Invoice.currency"/>
            </td>
            <td class="contain">
                <fanta:select property="parameter(currencyId)"
                              listName="basicCurrencyList"
                              labelProperty="name"
                              valueProperty="id"
                              firstEmpty="true"
                              styleClass="middleSelect"
                              module="/catalogs"
                              tabIndex="15">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.type"/>
            </td>
            <td class="contain">
                <html:select property="parameter(type)"
                             styleClass="mediumSelect"
                             tabindex="3">
                    <html:option value=""/>
                    <html:options collection="invoiceTypeList"
                                  property="value"
                                  labelProperty="label"/>
                </html:select>
            </td>
            <td class="label">
                <fmt:message key="Invoice.invoiceDate"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:dateText property="parameter(invoiceDateFrom)" maxlength="10" styleId="invoiceDateFrom"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="16"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:dateText property="parameter(invoiceDateTo)" maxlength="10" styleId="invoiceDateTo"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="17"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.totalAmountNet"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:numberText property="parameter(totalAmountNetFrom)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="4"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:numberText property="parameter(totalAmountNetTo)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="5"/>
            </td>
            <td class="label">
                <fmt:message key="Invoice.paymentDay"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:dateText property="parameter(paymentDateFrom)" maxlength="10" styleId="paymentDateFrom"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="18"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:dateText property="parameter(paymentDateTo)" maxlength="10" styleId="paymentDateTo"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="19"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.totalAmountGross"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:numberText property="parameter(totalAmountGrossFrom)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="6"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:numberText property="parameter(totalAmountGrossTo)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="7"/>
            </td>
            <td class="label">
                <fmt:message key="Invoice.search.notes"/>
            </td>
            <td class="contain">
                <html:text property="parameter(invoiceNote)" styleClass="middleText" tabindex="20"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.openAmount"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:numberText property="parameter(openAmountFrom)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="8"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:numberText property="parameter(openAmountTo)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="9"/>
            </td>
            <td class="label">
                <fmt:message key="Invoice.search.positionText"/>
            </td>
            <td class="contain">
                <html:text property="parameter(invoicePositionText)" styleClass="middleText" tabindex="21"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="InvoicePosition.product"/>
            </td>
            <td class="contain" colspan="3">
                <html:hidden property="parameter(productId)" styleId="field_key"/>
                <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                <html:hidden property="parameter(2)" styleId="field_unitId"/>
                <html:hidden property="parameter(3)" styleId="field_price"/>

                <app:text property="parameter(productName)" styleId="field_name" styleClass="mediumText" maxlength="40"
                          readonly="true" tabindex="10"/>
                <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"
                                  tabindex="11"/>
                <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"
                                       tabindex="12"/>
            </td>

        </tr>
        <tr>
            <td colspan="4" align="center" class="alpha">
                <fanta:alphabet action="/Invoice/AdvancedList.do" parameterName="alphabetName1"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="25"><fmt:message key="Common.go"/></html:submit>
                <html:button property="reset1" tabindex="26" styleClass="button" onclick="resetFields();"><fmt:message
                        key="Common.clear"/></html:button>
                &nbsp;
            </td>
        </tr>
    </table>
</html:form>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/Invoice/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="invoiceAdvancedList"
                         width="100%"
                         id="invoice"
                         action="Invoice/AdvancedList.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>
                <c:set var="deleteLink"
                       value="Invoice/Forward/Delete.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&dto(withReferences)=true&index=0"/>
                <c:set var="downloadAction"
                       value="Download/Invoice/Document.do?dto(freeTextId)=${invoice.documentId}&dto(invoiceId)=${invoice.invoiceId}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="33%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="INVOICE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="33%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                    <fanta:actionColumn name="download" title="Common.download"
                                        action="${downloadAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="34%"
                                        render="${not empty invoice.documentId}"
                                        image="${baselayout}/img/openfile.png"/>

                </fanta:columnGroup>
                <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem" title="Invoice.number"
                                  headerStyle="listHeader"
                                  width="14%"
                                  orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="type" styleClass="listItem" title="Invoice.type"
                                  headerStyle="listHeader" orderable="true" renderData="false" width="5%">
                    <c:if test="${invoice.type == invoiceType}">
                        <fmt:message key="Invoice.type.invoice.abbr"/>
                    </c:if>
                    <c:if test="${invoice.type == creditNoteType}">
                        <fmt:message key="Invoice.type.creditNote.abbr"/>
                    </c:if>
                </fanta:dataColumn>
                <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="paymentDate" styleClass="listItem" title="Invoice.paymentDate"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatDate var="paymentDateValue" value="${app2:intToDate(invoice.paymentDate)}"
                                    pattern="${datePattern}"/>
                    ${paymentDateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true"/>
                <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>
                <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountNet}
                </fanta:dataColumn>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight" title="Invoice.totalAmountGross"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItem2Right" title="Invoice.openAmount"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${openAmount}
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
    <tr>
        <td>
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/Invoice/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
</table>