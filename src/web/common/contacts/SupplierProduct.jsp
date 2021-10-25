<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<table width="50%" border="0" align="center" cellspacing="0" cellpadding="10">
<tr>
<td align="left">
<html:form action="${action}" focus="dto(productName)">
<table width="100%" border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td class="button">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER" styleClass="button"
                                     property="dto(save)">${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</table>
<table id="SupplierProduct.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center"
       class="container">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(addressId)" value="${param.contactId}"/>
    <html:hidden property="dto(isProduct)" value="false"/>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(supplierId)"/>
    </c:if>
    <c:if test="${('update' == op)}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <tr>
        <td colspan="2" class="title"><c:out value="${title}"/></td>
    </tr>

    <tr>
        <td class="label"><fmt:message key="Product.name"/></td>
        <td class="contain" nowrap>
            <!-- search products-->

            <html:hidden property="dto(productId)" styleId="field_key"/>

            <app:text property="dto(productName)" styleId="field_name" styleClass="mediumText"
                      maxlength="40" readonly="true" tabindex="2" view="${op == 'delete' || op == 'update'}"/>

            <tags:selectPopup url="/products/SearchProduct.do?contactId=${param.contactId}"
                              name="SearchProduct" titleKey="Common.search" hide="${op != 'create'}"/>
            <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name"
                                   titleKey="Common.clear" hide="${op != 'create'}"/>

        </td>
    </tr>

    <tr>
        <td class="label"><fmt:message key="SupplierProduct.ContactPerson"/></td>
        <td class="contain" nowrap>
            <!-- search contact person -->
            <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                          firstEmpty="true"
                          labelProperty="contactPersonName" valueProperty="contactPersonId"
                          styleClass="mediumSelect"
                          readOnly="${op == 'delete'}" tabIndex="3">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${not empty param.contactId? param.contactId:0}"/>
            </fanta:select>

        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="CustomerProduct.Unitname"/></td>
        <td class="contain">
            <fanta:select property="dto(unitId)" styleId="field_unitId" listName="productUnitList"
                          firstEmpty="true"
                          labelProperty="name" valueProperty="id" module="/catalogs"
                          styleClass="mediumSelect"
                          readOnly="${op == 'delete'}" tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="label" width="35%"><fmt:message key="SupplierProduct.orderNumber"/></td>
        <td class="contain" width="75%">
            <app:text property="dto(partNumber)" view="${op == 'delete'}" styleClass="numberText"
                      maxlength="20" tabindex="5"/>
        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="SupplierProduct.price"/></td>
        <td class="contain">
            <app:numberText property="dto(price)" styleId="field_price" styleClass="numberText" tabindex="6"
                            maxlength="12"
                            view="${'delete' == op}" numberType="decimal" maxInt="7" maxFloat="2"/>
        </td>
    </tr>

    <tr>
        <td class="label"><fmt:message key="SupplierProduct.discount"/></td>
        <td class="contain">
            <app:numberText property="dto(discount)" styleClass="numberText" tabindex="7" maxlength="12"
                            view="${'delete' == op}"
                            numberType="decimal" maxInt="7" maxFloat="2"/>&nbsp;
        </td>

    </tr>
    <tr>
        <td class="label"><fmt:message key="SupplierProduct.active"/></td>
        <td class="contain">
            <html:checkbox styleClass="radio" property="dto(active)" tabindex="8"
                           disabled="${op == 'delete'}"/>
        </td>
    </tr>
    <html:hidden property="dto(numberVersion)" styleId="field_versionNumber"/>
</table>
<table width="100%" border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td class="button">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER" styleClass="button"
                                     property="dto(save)" tabindex="21">${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="22"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</table>
</html:form>
</td>
</tr>
</table>