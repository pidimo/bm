<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<script type="text/javascript">
    function reSubmit() {
        document.forms[0].submit();
    }
</script>


<fmt:message var="datePattern" key="datePattern"/>
<html:form action="${action}" focus="dto(title)">
<html:hidden property="dto(op)" value="${op}"/>

<c:if test="${'update'== op || 'delete'== op}">
    <html:hidden property="dto(showNetGrossWarningMessage)" styleId="showNetGrossWarningMessageId"/>
    <html:hidden property="dto(saleId)"/>
</c:if>

<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<c:if test="${null != saleForm.dtoMap['processId']}">
    <html:hidden property="dto(processId)"/>
</c:if>

<c:if test="${null != saleForm.dtoMap['contactId']}">
    <html:hidden property="dto(contactId)"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
<tr>
    <td colspan="2" class="button" width="100%">
        <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                             tabindex="14">
            ${button}
        </app2:securitySubmit>

        <c:if test="${'update' == op}">
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <html:submit property="invoiceFromSale" styleClass="button" tabindex="15">
                    <fmt:message key="Sale.invoice"/>
                </html:submit>
            </app2:checkAccessRight>

            <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                <c:set var="invoiceInfo" value="${app2:getOnlyOneSaleInvoiceInfoMap(param.saleId, pageContext.request)}"/>

                <c:choose>
                    <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                        <app:url var="urlSaleInvoiceList" value="/Sale/Invoiced/List.do?tabKey=Sale.Tab.saleInvoices"/>
                        <html:button property="" styleClass="button" tabindex="16" onclick="location.href='${urlSaleInvoiceList}'">
                            <fmt:message key="Sale.viewInvoices"/>
                        </html:button>
                    </c:when>
                    <c:when test="${not empty invoiceInfo.documentId}">
                        <app:url var="urlDownloadDocument"
                                 value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceInfo.invoiceId}" contextRelative="true"/>
                        <html:button property="" styleClass="button" tabindex="16" onclick="location.href='${urlDownloadDocument}'">
                            <fmt:message key="Sale.openInvoice"/>
                        </html:button>
                    </c:when>
                </c:choose>
            </app2:checkAccessRight>
        </c:if>

        <html:cancel styleClass="button" tabindex="17">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<tr>
    <td colspan="2" class="title" width="100%">
        <c:out value="${title}"/>
    </td>
</tr>
<tr>
    <td class="label" width="30%">
        <fmt:message key="Sale.title"/>
    </td>
    <td class="contain" width="70%">
        <app:text property="dto(title)"
                  styleClass="middleText" maxlength="40" tabindex="1" view="${'delete' == op}"/>
    </td>
</tr>
<c:if test="${null != saleForm.dtoMap['contactId'] && null != saleForm.dtoMap['processId']}">
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
                          readOnly="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="processId" value="${saleForm.dtoMap['processId']}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>
<tr>
    <td class="label">
        <fmt:message key="Sale.customer"/>
    </td>
    <td class="contain" nowrap>
        <c:if test="${not empty saleForm.dtoMap['addressId'] and 'update' == op}">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
                <c:set var="addressMap" value="${app2:getAddressMap(saleForm.dtoMap['addressId'])}"/>
                <c:choose>
                    <c:when test="${personType == addressMap['addressType']}">
                        <c:set var="addressEditLink"
                               value="/contacts/Person/Forward/Update.do?contactId=${saleForm.dtoMap['addressId']}&dto(addressId)=${saleForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="addressEditLink"
                               value="/contacts/Organization/Forward/Update.do?contactId=${saleForm.dtoMap['addressId']}&dto(addressId)=${saleForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
        </c:if>

        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="dto(addressName)"
                  styleClass="middleText" maxlength="40" readonly="true"
                  styleId="fieldAddressName_id" view="${'delete' == op}" tabindex="2"/>

        <c:if test="${not empty addressEditLink}">
            <app:link action="${addressEditLink}" contextRelative="true" tabindex="2">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
            </app:link>
        </c:if>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          tabindex="3" hide="${'delete' == op}"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               name="searchAddress"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               tabindex="4" hide="${'delete' == op}"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Sale.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect" tabIndex="5"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty saleForm.dtoMap['addressId']?saleForm.dtoMap['addressId']:0}"/>
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
                      onChange="reSubmit()"
                      module="/contacts" tabIndex="6" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty saleForm.dtoMap['addressId'] ? saleForm.dtoMap['addressId'] : 0}"/>
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
                         value="${not empty saleForm.dtoMap['sentAddressId'] ? saleForm.dtoMap['sentAddressId'] : 0}"/>
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
            <fanta:parameter field="addressId" value="${not empty saleForm.dtoMap['addressId'] ? saleForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </td>
</tr>


<c:if test="${not empty saleForm.dtoMap['processId']}">
    <tr>
        <td class="label">
            <fmt:message key="Sale.salesProcess"/>
        </td>
        <td class="contain">
            <app:text property="dto(processName)"
                      styleClass="middleText" maxlength="40" view="true"/>

            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                <c:if test="${'update' == op && not empty saleForm.dtoMap['processId']}">
                    <c:set var="processEditLink"
                           value="/sales/SalesProcess/Forward/Update.do?processId=${saleForm.dtoMap['processId']}&dto(processId)=${saleForm.dtoMap['processId']}&dto(processName)=${app2:encode(saleForm.dtoMap['processName'])}&addressId=${saleForm.dtoMap['addressId']}&index=0"/>
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
                      currentDate="true"
                      view="${'delete' == op}"/>
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
                      tabIndex="7">
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
                     readonly="${'delete' == op}"
                     tabindex="8">
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
                       tabindex="9"/>
    </td>
</tr>
<tr>
    <td colspan="2" class="button" width="100%">
        <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                             tabindex="10">
            ${button}
        </app2:securitySubmit>
        <c:if test="${'update' == op}">
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <html:submit property="invoiceFromSale" styleClass="button" tabindex="11">
                    <fmt:message key="Sale.invoice"/>
                </html:submit>
            </app2:checkAccessRight>

            <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                <c:choose>
                    <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                        <html:button property="" styleClass="button" tabindex="12" onclick="location.href='${urlSaleInvoiceList}'">
                            <fmt:message key="Sale.viewInvoices"/>
                        </html:button>
                    </c:when>
                    <c:when test="${not empty invoiceInfo.documentId}">
                        <html:button property="" styleClass="button" tabindex="12" onclick="location.href='${urlDownloadDocument}'">
                            <fmt:message key="Sale.openInvoice"/>
                        </html:button>
                    </c:when>
                </c:choose>
            </app2:checkAccessRight>

        </c:if>

        <html:cancel styleClass="button" tabindex="13">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>

</html:form>