<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initSelectPopup/>

<tags:initSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>

<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="invoiceNetGross" value="${app2:getInvoiceNetGross(param.invoiceId, pageContext.request)}"/>

<c:set var="useNetPrice" value="${netPrice == invoiceNetGross}"/>

<c:set var="useGrossPrice" value="${grossPrice == invoiceNetGross}"/>

<c:set var="invoiceIsCreditNote" value="${app2:invoiceIsCreditNote(param.invoiceId, pageContext.request)}"/>

<c:set var="productSearchUrl" value="/products/SearchProduct.do"/>
<c:if test="${invoiceIsCreditNote && null != invoiceDTO}">
    <c:set var="productSearchUrl"
           value="/finance/SearchInvoicePosition.do?parameter(invoiceId)=${invoiceDTO.invoiceId}"/>
</c:if>


<script type="text/javascript">
    function selectInvoicePosition(invoicePositionId) {
        $("#relatedInvoicePositionId").attr("value", invoicePositionId);
        $("#selectInvoicePositionId").attr("value", "true");
    }

    function unSelectInvoicePosition() {
        var hiddenElement = $("#relatedInvoicePositionId");
        if (null != hiddenElement) {
            $("#relatedInvoicePositionId").attr("value", "");
        }
    }

    function selectProductContract() {
        $("#selectProductContractId").attr("value", "true");
    }

    function unSelectProductContract() {
        $("#unSelectProductContractId").attr("value", "true");
        document.forms[0].submit();
    }

    function selectPaymentStep() {
        $("#selectPaymentStepId").attr("value", "true");
        document.forms[0].submit();
    }
</script>

<html:form action="${action}" focus="dto(number)">
<html:hidden property="dto(op)" value="${op}"/>

<html:hidden property="dto(invoiceId)" value="${param.invoiceId}" styleId="invoiceId"/>
<html:hidden property="dto(invoiceNetGross)" value="${invoiceNetGross}" styleId="invoiceNetGrossId"/>

<html:hidden property="dto(discountValue)" styleId="discountValueId"/>
<html:hidden property="dto(discount)" styleId="discountId"/>


<c:if test="${'update' == op || 'delete' == op}">
    <html:hidden property="dto(positionId)"/>
    <html:hidden property="dto(modifiedTotalPrice)" styleId="modifiedTotalPriceId"/>
    <html:hidden property="dto(oldTotalPrice)" styleId="oldTotalPriceId"/>
    <html:hidden property="dto(oldTotalPriceGross)" styleId="oldTotalPriceGrossId"/>
    <html:hidden property="dto(oldDiscountValue)" styleId="oldDiscountValueId"/>


</c:if>

<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<c:if test="${invoiceIsCreditNote &&  null != invoiceDTO}">
    <html:hidden property="dto(relatedInvoicePositionId)" styleId="relatedInvoicePositionId"/>
    <html:hidden property="dto(selectInvoicePosition)" styleId="selectInvoicePositionId" value="false"/>

    <html:hidden property="dto(contractId)"/>
    <html:hidden property="dto(payStepId)"/>
    <html:hidden property="dto(oldContractId)"/>
</c:if>

<%-- this is hidden, fix this when it implemented to user view--%>
<%--<c:if test="${not empty invoicePositionForm.dtoMap['discount']}">
    <html:hidden property="dto(discount)"/>
</c:if>--%>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
<tr>
    <td colspan="2" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INVOICEPOSITION"
                             styleClass="button"
                             property="save"
                             tabindex="13">
            ${button}
        </app2:securitySubmit>
        <c:if test="${op == 'create'}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICEPOSITION"
                                 styleClass="button"
                                 property="SaveAndNew"
                                 tabindex="14">
                <fmt:message key="Common.saveAndNew"/>
            </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button" tabindex="15">
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
    <TD class="label" width="25%">
        <fmt:message key="InvoicePosition.number"/>
    </TD>
    <TD class="contain" width="75%">
        <app:numberText property="dto(number)"
                        styleClass="numberText"
                        maxlength="10"
                        numberType="integer"
                        view="${'delete' == op}"
                        tabindex="1"/>
    </TD>
</tr>

<app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
    <c:if test="${not empty invoicePositionForm.dtoMap['contractId']}">
        <c:set var="contractEditLink"
               value="/sales/ProductContract/Forward/Update.do?contractId=${invoicePositionForm.dtoMap['contractId']}&dto(contractId)=${invoicePositionForm.dtoMap['contractId']}&index=0"/>
    </c:if>
</app2:checkAccessRight>

<c:if test="${invoiceIsCreditNote && 'create' != op && not empty invoicePositionForm.dtoMap['contractId']}">
    <tr>
        <td class="label">
            <fmt:message key="InvoicePosition.productContract"/>
        </td>
        <td class="contain">
            <html:hidden property="dto(contractId)"/>
            <html:hidden property="dto(contractNumber)"/>

            <c:out value="${invoicePositionForm.dtoMap['contractNumber']}"/>

            <c:if test="${not empty contractEditLink}">
                <app:link action="${contractEditLink}" contextRelative="true">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                </app:link>
            </c:if>
        </td>
    </tr>
</c:if>
<c:if test="${!invoiceIsCreditNote}">
    <c:set var="isInvoiceRelatedToCreditNote"
           value="${app2:isInvoiceRelatedToCreditNote(param.invoiceId, pageContext.request )}"/>

    <c:set var="productContractSearchUrl"
           value="/sales/SearchProductContract.do?addressId=${invoiceDTO.addressId}&netGross=${invoiceDTO.netGross}&parameter(addressId)=${invoiceDTO.addressId}&parameter(netGross)=${invoiceDTO.netGross}"/>


    <%--used to detect when user press product contract search link--%>
    <html:hidden property="dto(selectProductContract)" styleId="selectProductContractId" value="false"/>

    <%--used to detect when user press clear link on selected product contract--%>
    <html:hidden property="dto(unSelectProductContract)" styleId="unSelectProductContractId" value="false"/>

    <%--used to detect when user change payment step for some produc contract--%>
    <html:hidden property="dto(selectPaymentStep)" styleId="selectPaymentStepId" value="false"/>

    <%--to detect if the selected contract is partial fixed--%>
    <html:hidden property="dto(isRelatedWithPartialFixedContract)"/>

    <%--to detect if quantity field is enable or disable--%>
    <html:hidden property="dto(disableQuantityField)" styleId="disableQuantityFieldId"/>
    <tr>
        <td class="label">
            <fmt:message key="InvoicePosition.productContract"/>
        </td>
        <td class="contain">
            <html:hidden property="dto(oldContractId)"/>

            <html:hidden property="dto(contractId)" styleId="fieldContractId_id"/>

            <c:choose>
                <c:when test="${'delete' != op && true == isInvoiceRelatedToCreditNote}">

                    <html:hidden property="dto(contractNumber)"/>
                    <c:out value="${invoicePositionForm.dtoMap['contractNumber']}"/>

                    <c:if test="${not empty contractEditLink}">
                        <app:link action="${contractEditLink}" contextRelative="true">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                        </app:link>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <app:text property="dto(contractNumber)"
                              styleClass="middleText"
                              maxlength="40"
                              readonly="true"
                              styleId="fieldContractNumber_id"
                              view="${'delete' == op}"
                              tabindex="2"/>

                    <c:if test="${'update' == op and not empty contractEditLink}">
                        <app:link action="${contractEditLink}" contextRelative="true" tabindex="2">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
                        </app:link>
                    </c:if>

                    <tags:selectPopup url="${productContractSearchUrl}"
                                      name="SearchContractProduct"
                                      titleKey="Common.search"
                                      hide="${'delete' == op}"
                                      submitOnSelect="true"
                                      tabindex="2"/>

                    <tags:clearSelectPopup keyFieldId="fieldContractId_id"
                                           nameFieldId="fieldContractNumber_id"
                                           titleKey="Common.clear"
                                           hide="${'delete' == op}"
                                           tabindex="2"
                                           onclick="javascript:unSelectProductContract();"/>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <c:choose>
        <c:when test="${'true' == invoicePositionForm.dtoMap['isRelatedWithPartialFixedContract']}">
            <tr>
                <td class="label">
                    <fmt:message key="InvoicePosition.payment"/>
                </td>
                <td class="contain">
                    <c:choose>
                        <c:when test="${'create' == op}">
                            <c:set var="unInvoicedPaymentSteps"
                                   value="${app2:getUnInvoicedPayments(invoicePositionForm.dtoMap['contractId'], pageContext.request)}"/>

                            <html:select property="dto(payStepId)"
                                         styleClass="middleSelect"
                                         tabindex="3"
                                         onchange="javascript:selectPaymentStep();">
                                <html:option value=""/>
                                <html:options collection="unInvoicedPaymentSteps" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </c:when>

                        <c:when test="${'update' == op && invoicePositionForm.dtoMap['oldContractId'] != invoicePositionForm.dtoMap['contractId']}">
                            <c:set var="unInvoicedPaymentSteps"
                                   value="${app2:getUnInvoicedPayments(invoicePositionForm.dtoMap['contractId'],pageContext.request)}"/>

                            <html:select property="dto(payStepId)"
                                         styleClass="middleSelect"
                                         tabindex="3"
                                         onchange="javascript:selectPaymentStep();">
                                <html:option value=""/>
                                <html:options collection="unInvoicedPaymentSteps" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </c:when>

                        <c:otherwise>
                            <html:hidden property="dto(payStepId)"/>
                            <c:set var="paymentLabel"
                                   value="${app2:getFormattedPaymentStepLabel(invoicePositionForm.dtoMap['payStepId'],pageContext.request)}"/>
                            <c:out value="${paymentLabel}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <html:hidden property="dto(payStepId)" value=""/>
        </c:otherwise>
    </c:choose>
</c:if>
<tr>
    <TD class="label">
        <fmt:message key="InvoicePosition.product"/>
    </TD>
    <TD class="contain">
        <html:hidden property="dto(productId)" styleId="fieldProductId_id"/>
        <html:hidden property="dto(1)" styleId="field_versionNumber"/>
        <html:hidden property="dto(2)" styleId="field_unitId"/>
        <html:hidden property="dto(3)" styleId="field_price"/>

        <app:text property="dto(productName)"
                  styleClass="middleText"
                  maxlength="40"
                  readonly="true"
                  styleId="fieldProductName_id"
                  view="${'delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"
                  tabindex="2"/>

        <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
            <c:if test="${'update' == op && not empty invoicePositionForm.dtoMap['productId']}">
                <c:set var="productEditLink"
                       value="/products/Product/Forward/Update.do?productId=${invoicePositionForm.dtoMap['productId']}&dto(productId)=${invoicePositionForm.dtoMap['productId']}&dto(productName)=${app2:encode(invoicePositionForm.dtoMap['productName'])}&index=0"/>
                <app:link action="${productEditLink}" contextRelative="true" tabindex="2">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
                </app:link>
            </c:if>
        </app2:checkAccessRight>

        <tags:selectPopup url="${productSearchUrl}"
                          name="SearchProduct"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          hide="${'delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"
                          tabindex="2"/>

        <tags:clearSelectPopup keyFieldId="fieldProductId_id"
                               nameFieldId="fieldProductName_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="${'delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"
                               onclick="javascript:unSelectInvoicePosition();"
                               tabindex="3"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="InvoicePosition.unit"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(unit)"
                  styleClass="middleText"
                  maxlength="40"
                  view="${'delete' == op}" tabindex="3"/>
    </TD>
</tr>
<tr>
    <c:if test="${useNetPrice}">
        <TD class="label">
            <fmt:message key="InvoicePosition.unitPrice"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(unitPriceGross)"/>
            <app:numberText property="dto(unitPrice)"
                            styleClass="numberText"
                            maxlength="18"
                            numberType="decimal"
                            maxInt="10"
                            maxFloat="4"
                            view="${'delete' == op}"
                            tabindex="4"/>
        </TD>
    </c:if>
    <c:if test="${useGrossPrice}">
        <TD class="label">
            <fmt:message key="InvoicePosition.unitPriceGross"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(unitPrice)"/>
            <app:numberText property="dto(unitPriceGross)"
                            styleClass="numberText"
                            maxlength="18"
                            numberType="decimal"
                            maxInt="10"
                            maxFloat="4"
                            view="${'delete' == op}"
                            tabindex="4"/>
        </TD>
    </c:if>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="InvoicePosition.quantity"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(quantity)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        view="${'delete' == op || true == invoicePositionForm.dtoMap['disableQuantityField']}"
                        tabindex="5"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="InvoicePosition.vat"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(vatId)"
                      listName="vatList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${'delete' == op}"
                      tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="InvoicePosition.account"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(accountId)"
                      listName="accountList"
                      labelProperty="name"
                      valueProperty="accountId"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${'delete' == op}"
                      tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</tr>
<tr>
    <c:if test="${useNetPrice}">
        <TD class="label">
            <fmt:message key="InvoicePosition.totalPrice"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(totalPriceGross)"/>
            <html:hidden property="dto(totalPrice)"/>
            <fmt:formatNumber var="totalPriceFormatted"
                              value="${invoicePositionForm.dtoMap['totalPrice']}"
                              type="number"
                              pattern="${numberFormat}"/>
                ${totalPriceFormatted}
        </TD>
    </c:if>
    <c:if test="${useGrossPrice}">
        <TD class="label">
            <fmt:message key="InvoicePosition.totalPriceGross"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(totalPrice)"/>
            <html:hidden property="dto(totalPriceGross)"/>
            <fmt:formatNumber var="totalPriceGrossFormatted"
                              value="${invoicePositionForm.dtoMap['totalPriceGross']}"
                              type="number"
                              pattern="${numberFormat}"/>
                ${totalPriceGrossFormatted}
        </TD>
    </c:if>
</tr>
<tr>
    <td class="topLabel" colspan="2">
        <fmt:message key="InvoicePosition.text"/>
        <html:textarea property="dto(text)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       readonly="${'delete'== op}"
                       tabindex="8"/>
    </td>
</tr>
<tr>
    <td colspan="2" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INVOICEPOSITION"
                             styleClass="button"
                             property="save"
                             tabindex="10">
            ${button}
        </app2:securitySubmit>
        <c:if test="${op == 'create'}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICEPOSITION"
                                 styleClass="button"
                                 property="SaveAndNew"
                                 tabindex="11">
                <fmt:message key="Common.saveAndNew"/>
            </app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button" tabindex="12">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>