<%@ include file="/Includes.jsp" %>


<%--
Documentation:
         showSalesProcessOption        : if true, then show SalesProcessAction select box.
         readOnlySalesProcessOption    : if true, the SalesProcessAction select box showed as read only view.

         Exists function changeAction() this is javaSript function and make submit of de form when the page is imported;
         this function is used in the salesProcessOption in the onchange property.
--%>

<script language="JavaScript" type="text/javascript">
    function changeAction() {
        document.forms[0].submit();
    }
</script>

<calendar:initialize/>
<tags:initSelectPopup/>
<fmt:message var="datePattern" key="datePattern"/>
<tr>
    <td class="label" width="30%">
        <fmt:message key="Sale.title"/>
    </td>
    <td class="contain" width="70%">
        <app:text property="dto(title)"
                  styleClass="middleText" maxlength="40" tabindex="1" view="${'delete' == op}"/>
    </td>
</tr>
<c:if test="${true == showSalesProcessOption}">
    <tr>
        <td class="label">
            <fmt:message key="Sale.salesProcessAction"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(contactId)"
                          listName="actionSelectList"
                          module="/sales"
                          firstEmpty="true"
                          labelProperty="note"
                          valueProperty="contactId"
                          styleClass="middleSelect"
                          readOnly="${true == readOnlySalesProcessOption}"
                          onChange="javascript:changeAction();"
                          tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="processId" value="${saleObject.dtoMap['processId']}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>
<tr>
    <td class="label">
        <fmt:message key="Sale.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      tabIndex="5"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty saleObject.dtoMap['addressId']?saleObject.dtoMap['addressId']:param.contactId}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.seller"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sellerId)"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="middleSelect"
                      value="${sessionScope.user.valueMap['userAddressId']}"
                      module="/contacts"
                      tabIndex="6"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>

<tr>
    <TD class="label">
        <fmt:message key="Sale.sentAddress"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(sentAddressId)" listName="invoiceAddressRelationList"
                      labelProperty="relatedAddressName" valueProperty="relatedAddressId" styleClass="middleSelect"
                      readOnly="${op == 'delete'}"
                      onChange="javascript:changeAction();"
                      module="/contacts" tabIndex="6" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty saleObject.dtoMap['addressId'] ? saleObject.dtoMap['addressId'] : param.contactId}"/>
        </fanta:select>
    </TD>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.sentContactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      tabIndex="6"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                         value="${not empty saleObject.dtoMap['sentAddressId'] ? saleObject.dtoMap['sentAddressId'] : 0}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.additionalAddress"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                      firstEmpty="true" styleClass="middleSelect" readOnly="${'delete' == op}" tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty saleObject.dtoMap['addressId'] ? saleObject.dtoMap['addressId'] : param.contactId}"/>
        </fanta:select>
    </td>
</tr>

<c:if test="${not empty saleObject.dtoMap['processId']}">
    <html:hidden property="dto(processId)"/>
    <html:hidden property="dto(contactId)"/>
    <tr>
        <td class="label">
            <fmt:message key="Sale.salesProcess"/>
        </td>
        <td class="contain">
            <app:text property="dto(processName)"
                      styleClass="middleText"
                      maxlength="40"
                      tabindex="1"
                      view="true"/>

            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                <c:if test="${'update' == op && not empty saleObject.dtoMap['processId']}">
                    <c:set var="processEditLink"
                           value="/sales/SalesProcess/Forward/Update.do?processId=${saleObject.dtoMap['processId']}&dto(processId)=${saleObject.dtoMap['processId']}&dto(processName)=${app2:encode(saleObject.dtoMap['processName'])}&addressId=${saleObject.dtoMap['addressId']}&index=0"/>
                    <app:link action="${processEditLink}" contextRelative="true">
                        <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                    </app:link>
                </c:if>
            </app2:checkAccessRight>
        </td>
    </tr>
</c:if>
<tr>
    <td class="label">
        <fmt:message key="Sale.saleDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(saleDate)"
                      styleId="startDate"
                      calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      tabindex="7"
                      currentDate="true" view="${'delete' == op}"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="middleSelect"
                      module="/catalogs"
                      readOnly="${op == 'delete'}"
                      tabIndex="8">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.netGross"/>
    </td>
    <td class="contain">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleClass="middleSelect"
                     readonly="${op == 'delete'}"
                     tabindex="9">
            <html:option value=""/>
            <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="topLabel" colspan="2">
        <fmt:message key="Sale.text"/>
        <html:textarea property="dto(text)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       readonly="${'delete'== op}"
                       tabindex="10"/>
    </td>
</tr>