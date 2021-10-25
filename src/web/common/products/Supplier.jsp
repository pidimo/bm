<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>


<script type="text/javascript">
    function submitForm(obj)
    {
        document.forms[0].submit();

    }
</script>
<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<br>
<c:choose>
    <c:when test="${op == 'delete'}">
        <c:set var="setFocus" value="dto(supplierName)"/>
    </c:when>
    <c:otherwise>
        <c:set var="setFocus" value="dto(contactPersonId)"/>
    </c:otherwise>
</c:choose>
<html:form action="${action}" focus="${setFocus}">
<table cellSpacing="0" cellPadding="0" width="70%" border=0 align="center">
    <TR>
        <TD class="button">

            <c:if test="${op == 'add'}">
                <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="CREATE">
                    <html:submit styleClass="button"><fmt:message key="Common.save"/></html:submit>&nbsp;
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op != 'add'}">
                <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER" styleClass="button"
                                     property="dto(save)" tabindex="7">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" tabindex="8"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>

<table id="Supplier.jsp" border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
    <html:hidden property="dto(productId)" value="${param.productId}"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(isProduct)" value="true"/>

        <%--for the version control if update action--%>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(supplierId)"/>
    </c:if>
        <%--for the control referencial integrity if delete action--%>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
        <html:hidden property="dto(supplierId)"/>
    </c:if>
    <TR>
        <TD colspan="2" class="title"><c:out value="${title}"/></TD>
    </TR>
    <tr>
        <TD class="label" width="25%"><fmt:message key="Supplier"/></TD>
        <TD class="contain" width="75%">

                <%--<fanta:select property="dto(supplierId)" listName="supplierSearchList"
                              labelProperty="supplierName" valueProperty="supplierId" styleClass="mediumSelect"
                              readOnly="${op == 'delete' || op == 'update'}"
                              module="/products" tabIndex="1" firstEmpty="true" styleId="fieldAddressId_id" onChange="submitForm(this);">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>--%>


            <html:hidden property="dto(supplierId)" styleId="fieldAddressId_id"/>
            <app:text property="dto(supplierName)" styleId="fieldAddressName_id" styleClass="mediumText"
                      readonly="true" tabindex="1" view="${op == 'delete' || op == 'update'}"/>


            <tags:selectPopup url="/contacts/SearchAddress.do?allowCreation=2" name="SearchSupplier"
                              titleKey="Common.search" hide="${op != 'create'}" submitOnSelect="true"/>

            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                   titleKey="Common.clear" hide="${op != 'create'}" submitOnClear="true"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="25%"><fmt:message key="ContactPerson"/></TD>
        <td class="contain" width="75%">
            <fanta:select property="dto(contactPersonId)" tabIndex="1" listName="searchContactPersonList"
                          module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                          valueProperty="contactPersonId" styleClass="mediumSelect" readOnly="${op == 'delete'}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${not empty supplierForm.dtoMap['supplierId']?supplierForm.dtoMap['supplierId']:0}"/>
            </fanta:select>
        </TD>
    </tr>
    <tr>
        <TD class="label"><fmt:message key="Product.unit"/></TD>
        <TD class="contain">
            <fanta:select property="dto(unitId)" listName="productUnitList" firstEmpty="true" labelProperty="name"
                          valueProperty="id" module="/catalogs" styleClass="mediumSelect"
                          readOnly="${op == 'delete'}" tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </tr>
    <TR>
        <TD class="label" width="25%"><fmt:message key="SupplierProduct.orderNumber"/></TD>
        <TD class="contain" width="75%">
            <app:text property="dto(partNumber)" styleClass="numberText" maxlength="40" view="${'delete' == op }"
                      tabindex="2"/>
        </TD>
    </tr>

    <tr>
        <TD class="label" width="25%"><fmt:message key="SupplierProduct.price"/></TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(price)" styleClass="numberText" maxlength="20" view="${'delete' == op}"
                            numberType="decimal" maxInt="10" maxFloat="2" tabindex="3"/>
        </TD>
    </tr>
    <tr>
        <TD class="label" width="25%"><fmt:message key="SupplierProduct.discount"/></TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(discount)" styleClass="numberText" maxlength="20" view="${'delete' == op}"
                            numberType="decimal" maxInt="10" maxFloat="2" tabindex="4"/>
        </TD>
    </tr>
    <c:if test="${op != 'add'}">
        <TR>
            <TD class="label" width="25%"><fmt:message key="Common.active"/></TD>
            <TD class="contain" width="75%">
                <html:checkbox styleClass="radio" property="dto(active)" disabled="${op == 'delete'  }"
                               tabindex="5"/>
            </TD>
        </tr>
    </c:if>
</table>

<table cellSpacing="0" cellPadding="0" width="70%" border=0 align="center">
    <TR>
        <TD class="button">

            <c:if test="${op == 'add'}">
                <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="CREATE">
                    <html:submit styleClass="button"><fmt:message key="Common.save"/></html:submit>&nbsp;
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op != 'add'}">
                <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER" styleClass="button"
                                     property="dto(save)" tabindex="7">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" tabindex="8"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>
</html:form>
</td>
</tr>
</table>