<%@ include file="/Includes.jsp" %>

<script>
    function resetFields() {
        var form = document.saleAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
<fmt:message key="datePattern" var="datePattern"/>
<calendar:initialize/>

<html:form action="/Sale/AdvancedList.do" focus="parameter(title)">
<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
<tr>
    <td class="title" colspan="4">
        <fmt:message key="Sale.advancedSearch"/>
    </td>
</tr>
<TR>
    <td class="label" width="15%">
        <fmt:message key="Sale.title"/>
    </td>
    <td class="contain" width="35%">
        <html:text property="parameter(title)"
                   styleClass="mediumText" tabindex="1"/>
    </td>
    <td class="label" width="15%">
        <fmt:message key="Sale.seller"/>
    </td>
    <td class="contain" width="35%">
        <fanta:select property="parameter(sellerId)"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="middleSelect"
                      module="/contacts"
                      firstEmpty="true"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</TR>
<tr>
    <td class="label">
        <fmt:message key="Sale.customer"/>
    </td>
    <td class="contain">
        <html:text
                property="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)"
                styleClass="mediumText"
                maxlength="80" tabindex="2"/>
    </td>
    <td class="label">
        <fmt:message key="Sale.productGroup"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(productGroupId)"
                      listName="productGroupList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.contactPerson"/>
    </td>
    <td class="contain">
        <html:text
                property="parameter(contactPersonName1@_contactPersonName2@_contactPersonName3)"
                styleClass="mediumText"
                maxlength="80" tabindex="3"/>
    </td>
    <td class="label">
        <fmt:message key="Sale.productType"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(productTypeId)"
                      listName="productTypeList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                      tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.product"/>
    </td>
    <td class="contain">
        <html:text
                property="parameter(productName)"
                styleClass="mediumText"
                maxlength="80" tabindex="4"/>
    </td>
    <td class="label">
        <fmt:message key="Sale.saleDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>&nbsp;
        <app:dateText property="parameter(saleDateFrom)" maxlength="10" styleId="saleDateFrom"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true" tabindex="8"/>
        &nbsp;
        <fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(saleDateTo)" maxlength="10" styleId="saleDateTo"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true" tabindex="9"/>
    </td>
</tr>
<tr>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="/Sale/AdvancedList.do" parameterName="alphabetTitle"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" tabindex="10"><fmt:message key="Common.go"/></html:submit>
        <html:button property="reset1" tabindex="11" styleClass="button" onclick="resetFields();">
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
                    <app2:checkAccessRight functionality="SALE" permission="CREATE">
                        <html:form action="/Sale/Forward/Create.do">
                            <td class="button" width="100%">
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </app2:checkAccessRight>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="saleAdvancedList"
                         width="100%"
                         id="sale"
                         action="Sale/AdvancedList.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="Sale/Forward/Update.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&index=0"/>
                <c:set var="deleteLink"
                       value="Sale/Forward/Delete.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&dto(withReferences)=true&index=0"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="title" action="${editLink}" styleClass="listItem" title="Sale.title"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="customerName" styleClass="listItem" title="Sale.customer"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Sale.contactPerson"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="sellerName" styleClass="listItem" title="Sale.seller"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="saleDate" styleClass="listItem2" title="Sale.saleDate"
                                  headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                    <fmt:formatDate var="saleDateValue" value="${app2:intToDate(sale.saleDate)}"
                                    pattern="${datePattern}"/>
                    ${saleDateValue}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <app2:checkAccessRight functionality="SALE" permission="CREATE">
                        <html:form action="/Sale/Forward/Create.do">
                            <td class="button" width="100%">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </app2:checkAccessRight>
                </tr>
            </table>
        </td>
    </tr>
</table>