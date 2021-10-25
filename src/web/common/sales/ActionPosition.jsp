<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="actionNetGross"
       value="${app2:getNetGrossFieldFromAction(param['dto(contactId)'],param['dto(processId)'] ,pageContext.request)}"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="useNetPrice" value="${netPrice == actionNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == actionNetGross}"/>

<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<br/>

<html:form action="${action}" focus="dto(productName)">

<html:hidden property="dto(op)" value="${op}"/>

<html:hidden property="dto(actionNetGross)" value="${actionNetGross}" styleId="actionNetGrossId"/>

<c:if test="${'update' == op || op == 'delete'}">
    <html:hidden property="dto(positionId)"/>
    <html:hidden property="dto(version)"/>
</c:if>

<html:hidden property="dto(processId)" value="${dto.processId}"/>
<html:hidden property="dto(contactId)" value="${dto.contactId}"/>
<html:hidden property="dto(note)" value="${dto.note}"/>
<html:hidden property="dto(processName)" value="${dto.processName}"/>
<html:hidden property="dto(addressId)"
             value="${isSalesProcess ? param.addressId : param.contactId}"/> <%--verificar--%>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<html:hidden property="dto(price_old)" styleId="field_price"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>

<table cellSpacing="0" cellPadding="4" width="800" border="0" align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}"
                                 functionality="SALESPROCESSPOSITION"
                                 property="dto(save)"
                                 styleClass="button"
                                 styleId="saveButtonId">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="SALESPROCESSPOSITION"
                                     styleClass="button"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>

<table id="ActionPosition.jsp" border="0" cellpadding="0" cellspacing="0" width="800" align="center"
       class="container">
    <TR>
        <TD colspan="4" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="15%">
            <fmt:message key="ActionPosition.product"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(productId)" styleId="field_key"/>
            <html:hidden property="dto(versionNumber)" styleId="field_versionNumber"/>
            <app:text property="dto(productName)"
                      styleClass="mediumText"
                      maxlength="40"
                      readonly="true"
                      tabindex="1"
                      styleId="field_name"
                      view="${op == 'delete'}"/>

            <tags:selectPopup
                    url="/products/SearchProduct.do?contactId=${isSalesProcess ? param.addressId : param.contactId}"
                    name="SearchProduct"
                    titleKey="Common.search"
                    submitOnSelect="true"
                    hide="${op == 'delete'}"
                    tabindex="1"/>
            <tags:clearSelectPopup keyFieldId="field_key"
                                   nameFieldId="field_name"
                                   titleKey="Common.clear"
                                   submitOnClear="true"
                                   hide="${op == 'delete'}"
                                   tabindex="1"/>

        </TD>
        <td class="label">
            <fmt:message key="ActionPosition.number"/>
        </td>
        <td class="contain">
            <app:numberText property="dto(number)"
                            styleClass="numberText"
                            maxlength="10"
                            numberType="integer"
                            view="${'delete' == op}"
                            tabindex="6"/>
        </td>
    </TR>
    <tr>
        <TD class="label">
            <fmt:message key="ActionPosition.quantity"/>
        </TD>
        <TD class="contain">
            <app:numberText property="dto(amount)"
                            styleClass="numberText"
                            maxlength="18"
                            view="${'delete' == op}"
                            numberType="decimal"
                            maxInt="10"
                            maxFloat="2"
                            tabindex="2"/>

        </TD>
        <TD class="label">
            <fmt:message key="ActionPosition.unit"/>
        </TD>
        <TD class="contain">
            <fanta:select property="dto(unit)"
                          styleId="field_unitId"
                          listName="productUnitList"
                          firstEmpty="true"
                          labelProperty="name"
                          valueProperty="id"
                          module="/catalogs"
                          styleClass="mediumSelect"
                          readOnly="${op == 'delete'}" tabIndex="7">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </tr>
    <TR>
        <c:if test="${useNetPrice}">
            <TD class="label">
                <fmt:message key="ActionPosition.unitPriceNet"/>
            </TD>
            <TD class="contain">
                <html:hidden property="dto(unitPriceGross)"/>
                <app:numberText property="dto(price)"
                                styleClass="numberText"
                                maxlength="18"
                                view="${'delete' == op}"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="4"
                                tabindex="3"/>
            </TD>
        </c:if>
        <c:if test="${useGrossPrice}">
            <TD class="label">
                <fmt:message key="ActionPosition.unitPriceGross"/>
            </TD>
            <TD class="contain">
                <html:hidden property="dto(price)"/>
                <app:numberText property="dto(unitPriceGross)"
                                styleClass="numberText"
                                maxlength="18"
                                view="${'delete' == op}"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="4"
                                tabindex="3"/>
            </TD>
        </c:if>
        <TD class="topLabel" rowspan="3">
            <fmt:message key="ActionPosition.description"/>
        </TD>
        <TD class="contain" rowspan="3">
            <html:textarea property="dto(description)"
                           styleClass="tinyDetail"
                           style="height: 70px;"
                           readonly="${op == 'delete'}"
                           tabindex="8"/>
        </TD>
    </TR>

    <TR>
        <td class="label">
            <fmt:message key="ActionPosition.discount"/>
        </td>
        <td class="contain">
            <app:numberText property="dto(discount)"
                            styleClass="numberText"
                            maxlength="18"
                            view="${'delete' == op}"
                            numberType="decimal"
                            maxInt="10"
                            maxFloat="2"
                            tabindex="4"/>
            <c:out value="%"/>
        </td>
    </TR>
    <TR>
        <c:if test="${useNetPrice}">
            <TD class="label">
                <fmt:message key="ActionPosition.totalPriceNet"/>
            </TD>
            <TD class="contain">
                <html:hidden property="dto(totalPriceGross)"/>
                <app:numberText property="dto(totalPrice)"
                                styleId="totalPrice_name"
                                styleClass="numberText"
                                tabindex="5"
                                numberType="decimal"
                                maxInt="18"
                                maxFloat="2"
                                readonly="true"
                                view="true"/>
            </TD>
        </c:if>
        <c:if test="${useGrossPrice}">
            <TD class="label">
                <fmt:message key="ActionPosition.totalPriceGross"/>
            </TD>
            <TD class="contain">
                <html:hidden property="dto(totalPrice)"/>
                <app:numberText property="dto(totalPriceGross)"
                                styleId="totalPrice_name"
                                styleClass="numberText"
                                tabindex="5"
                                numberType="decimal"
                                maxInt="18"
                                maxFloat="2"
                                readonly="true"
                                view="true"/>
            </TD>
        </c:if>
    </TR>
</table>

<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}"
                                 tabindex="11"
                                 functionality="SALESPROCESSPOSITION"
                                 property="dto(save)"
                                 styleClass="button"
                                 styleId="saveButtonId">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="SALESPROCESSPOSITION"
                                     styleClass="button"
                                     property="SaveAndNew"
                                     tabindex="12">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" tabindex="13">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
</html:form>

</td>
</tr>
</table>
