<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<script>
    function resetFields() {
        var form = document.productContractAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<calendar:initialize/>

<html:form action="/ProductContract/AdvancedList.do"
           focus="parameter(contractNumber)">
<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
<tr>
    <td class="title" colspan="4">
        <fmt:message key="ProductContract.advancedSearch"/>
    </td>
</tr>
<tr>
    <td class="label" width="15%">
        <fmt:message key="ProductContract.contractNumber"/>
    </td>
    <td class="contain" width="35%">
        <html:text property="parameter(contractNumber)"
                   styleClass="mediumText"
                   tabindex="1"/>
    </td>
    <td class="label" width="15%">
        <fmt:message key="Contract.payMethod"/>
    </td>
    <td class="contain" width="35%">
        <html:select property="parameter(payMethod)"
                     styleClass="mediumSelect"
                     styleId="payMethodId"
                     tabindex="12">
            <html:option value=""/>
            <html:options collection="payMethodList"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>
<TR>
    <td class="label">
        <fmt:message key="Contract.contact"/>
    </td>
    <td class="contain">
        <html:text property="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3)"
                   styleClass="mediumText"
                   tabindex="2"/>
    </td>
    <TD class="label">
        <fmt:message key="Contract.contractType"/>
    </TD>
    <TD class="contain">
        <fanta:select property="parameter(contractTypeId)"
                      listName="contractTypeList"
                      firstEmpty="true"
                      labelProperty="name"
                      valueProperty="contractTypeId"
                      module="/catalogs"
                      styleClass="mediumSelect"
                      tabIndex="13">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</TR>
<tr>
    <td class="label">
        <fmt:message key="Sale.customer"/>
    </td>
    <td class="contain">
        <html:text property="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)"
                   styleClass="mediumText"
                   tabindex="3"/>
    </td>
    <td class="label">
        <fmt:message key="ProductContract.vat"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(vatId)"
                      listName="vatList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="mediumSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      tabIndex="14">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.product"/>
    </td>
    <td class="contain">
        <html:text property="parameter(productName)"
                   styleClass="mediumText"
                   maxlength="80"
                   tabindex="4"/>
    </td>
    <td class="label">
        <fmt:message key="ProductContract.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="mediumSelect"
                      module="/catalogs"
                      tabIndex="15">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.price"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>&nbsp;
        <app:numberText property="parameter(priceFrom)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="5"/>
        &nbsp;
        <fmt:message key="Common.to"/>&nbsp;
        <app:numberText property="parameter(priceTo)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="6"/>
    </td>
    <td class="label" width="18%">
        <fmt:message key="ProductContract.seller"/>
    </td>
    <td class="contain" width="32%">
        <fanta:select property="parameter(sellerId)"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="mediumSelect"
                      module="/contacts"
                      firstEmpty="true"
                      tabIndex="16">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.discount"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>&nbsp;
        <app:numberText property="parameter(discountFrom)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="7"/>
        &nbsp;
        <fmt:message key="Common.to"/>&nbsp;
        <app:numberText property="parameter(discountTo)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="8"/>
    </td>
    <td class="label">
        <fmt:message key="Contract.orderDate"/>
    </td>
    <td class="contain" nowrap>
        <fmt:message key="Common.from"/>&nbsp;
        <app:dateText property="parameter(orderDateFrom)" maxlength="10" styleId="saleDateFrom"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true" tabindex="17"/>
        &nbsp;
        <fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(orderDateTo)" maxlength="10" styleId="saleDateTo"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true" tabindex="18"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.openAmount"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>&nbsp;
        <app:numberText property="parameter(openAmountFrom)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="9"/>
        &nbsp;
        <fmt:message key="Common.to"/>&nbsp;
        <app:numberText property="parameter(openAmountTo)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        styleId="field_price"
                        tabindex="10"/>
    </td>
    <td class="label">
        <fmt:message key="Contract.salePosition.freeText"/>
    </td>
    <td class="contain">
        <html:text property="parameter(salePositionFreetext)"
                   styleClass="mediumText"
                   tabindex="19"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.note"/>
    </td>
    <td class="contain" colspan="3">
        <html:text property="parameter(productContractNote)"
                   styleClass="mediumText"
                   tabindex="11"/>
    </td>
</tr>
<tr>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="/ProductContract/AdvancedList.do" parameterName="alphabetName1"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" tabindex="20"><fmt:message key="Common.go"/></html:submit>
        <html:button property="reset1" tabindex="21" styleClass="button" onclick="resetFields();">
            <fmt:message key="Common.clear"/>
        </html:button>
        &nbsp;
    </td>
</tr>
</table>
</html:form>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
                        <html:form action="/ProductContract/Forward/Create.do">
                            <td class="button" width="100%">
                                    <%--<html:submit styleClass="button">
                                        <fmt:message key="Common.new"/>
                                    </html:submit>--%>
                            </td>
                        </html:form>
                    </app2:checkAccessRight>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="productContractAdvancedList"
                         width="100%"
                         id="productContract"
                         action="ProductContract/AdvancedList.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="ProductContract/Forward/Update.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&index=0"/>
                <c:set var="deleteLink"
                       value="ProductContract/Forward/Delete.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&dto(withReferences)=true&index=0"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="contractNumber"
                                  action="${editLink}"
                                  title="ProductContract.contractNumber"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  width="10%"/>
                <fanta:dataColumn name="addressName"
                                  action="${editLink}"
                                  title="Contract.contact"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="40"
                                  width="15%"/>
                <fanta:dataColumn name="customerName"
                                  title="Sale.customer"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="40"
                                  width="15%"/>
                <fanta:dataColumn name="productName"
                                  title="Contract.product"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="40"
                                  width="10%"/>
                <fanta:dataColumn name="price"
                                  styleClass="listItemRight"
                                  title="Contract.price"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <fmt:formatNumber var="priceFormattedValue" value="${productContract.price}" type="number"
                                      pattern="${numberFormat}"/>
                    ${priceFormattedValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="discount"
                                  styleClass="listItemRight"
                                  title="Contract.discount"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  width="5%">
                    <fmt:formatNumber var="discountFormattedValue" value="${productContract.discount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${discountFormattedValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount"
                                  styleClass="listItemRight"
                                  title="Contract.openAmount"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <fmt:formatNumber var="openAmountFormattedValue" value="${productContract.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    <c:if test="${productContract.payMethod != PERIODIC_PAYMETHOD}">
                        ${openAmountFormattedValue}
                    </c:if>
                </fanta:dataColumn>
                <fanta:dataColumn name="orderDate"
                                  styleClass="listItem"
                                  title="Contract.orderDate"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <fmt:formatDate var="orderDateValue" value="${app2:intToDate(productContract.orderDate)}"
                                    pattern="${datePattern}"/>
                    ${orderDateValue}&nbsp;
                </fanta:dataColumn>
                <fanta:dataColumn name="payMethod"
                                  styleClass="listItem"
                                  title="Contract.payMethod"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <c:set var="payMethodLabel" value="${app2:searchLabel(payMethodList, productContract.payMethod)}"/>
                    <fanta:textShorter title="${payMethodLabel}">
                        ${payMethodLabel}
                    </fanta:textShorter>
                </fanta:dataColumn>
                <fanta:dataColumn name="contractTypeName"
                                  styleClass="listItem2"
                                  title="Contract.contractType"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="40"
                                  width="8%"/>
            </fanta:table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
                        <html:form action="/ProductContract/Forward/Create.do">
                            <td class="button" width="100%">
                                    <%--<html:submit styleClass="button">
                                        <fmt:message key="Common.new"/>
                                    </html:submit>--%>
                            </td>
                        </html:form>
                    </app2:checkAccessRight>
                </tr>
            </table>
        </td>
    </tr>
</table>