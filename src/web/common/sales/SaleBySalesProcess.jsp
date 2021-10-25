<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<fmt:message var="datePattern" key="datePattern"/>

<script language="JavaScript" type="text/javascript">
    function changeAction() {
        document.forms[0].submit();
    }
</script>

<html:form action="${action}" focus="dto(title)">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(processId)"/>

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

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                                     tabindex="13">
                    ${button}
                </app2:securitySubmit>

                    <c:if test="${'update' == op}">
                        <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                            <html:submit property="invoiceFromSale" styleClass="button" tabindex="14">
                                <fmt:message key="Sale.invoice"/>
                            </html:submit>
                        </app2:checkAccessRight>

                        <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                            <c:set var="invoiceInfo" value="${app2:getOnlyOneSaleInvoiceInfoMap(saleBySalesProcessForm.dtoMap['saleId'], pageContext.request)}"/>

                            <c:choose>
                                <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                    <app:url var="urlSaleInvoiceList" value="/Sale/Invoiced/List.do?tabKey=Sale.Tab.saleInvoices"/>
                                    <html:button property="" styleClass="button" tabindex="14" onclick="location.href='${urlSaleInvoiceList}'">
                                        <fmt:message key="Sale.viewInvoices"/>
                                    </html:button>
                                </c:when>
                                <c:when test="${not empty invoiceInfo.documentId}">
                                    <app:url var="urlDownloadDocument"
                                             value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceInfo.invoiceId}" contextRelative="true"/>
                                    <html:button property="" styleClass="button" tabindex="14" onclick="location.href='${urlDownloadDocument}'">
                                        <fmt:message key="Sale.openInvoice"/>
                                    </html:button>
                                </c:when>
                            </c:choose>
                        </app2:checkAccessRight>
                    </c:if>

                <html:cancel styleClass="button" tabindex="15">
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
                          styleClass="middleText"
                          maxlength="40"
                          view="${'delete' == op}"/>
            </td>
        </tr>
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
                              readOnly="${'delete' == op || 'update' == op}"
                              onChange="javascript:changeAction();">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="processId" value="${saleBySalesProcessForm.dtoMap['processId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Sale.customer"/>
            </td>
            <td class="contain" nowrap>
                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                <app:text property="dto(addressName)"
                          styleClass="middleText"
                          maxlength="40"
                          readonly="true"
                          styleId="fieldAddressName_id"
                          view="true"/>

                    <%--<tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
             titleKey="Common.search"
             submitOnSelect="true"
             hide="${'delete' == op}"/>
<tags:clearSelectPopup keyFieldId="fieldAddressId_id"
                  nameFieldId="fieldAddressName_id"
                  titleKey="Common.clear"
                  submitOnClear="true"
                  hide="${'delete' == op}"/>--%>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Sale.contactPerson"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(contactPersonId)"
                              listName="searchContactPersonList"
                              module="/contacts" firstEmpty="true"
                              labelProperty="contactPersonName"
                              valueProperty="contactPersonId"
                              styleClass="middleSelect"
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                     value="${not empty saleBySalesProcessForm.dtoMap['addressId']?saleBySalesProcessForm.dtoMap['addressId']:0}"/>
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
                              module="/contacts" firstEmpty="true">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${not empty saleBySalesProcessForm.dtoMap['addressId'] ? saleBySalesProcessForm.dtoMap['addressId'] : 0}"/>
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
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                 value="${not empty saleBySalesProcessForm.dtoMap['sentAddressId'] ? saleBySalesProcessForm.dtoMap['sentAddressId'] : 0}"/>
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
                              firstEmpty="true" styleClass="middleSelect" readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${not empty saleBySalesProcessForm.dtoMap['addressId'] ? saleBySalesProcessForm.dtoMap['addressId'] : 0}"/>
                </fanta:select>
            </td>
        </tr>

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
                          readOnly="${op == 'delete'}">
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
                             readonly="${'delete' == op}">
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
                               readonly="${'delete'== op}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button">
                    ${button}
                </app2:securitySubmit>
                    <c:if test="${'update' == op}">
                        <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                            <html:submit property="invoiceFromSale" styleClass="button">
                                <fmt:message key="Sale.invoice"/>
                            </html:submit>
                        </app2:checkAccessRight>

                        <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                            <c:choose>
                                <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                    <html:button property="" styleClass="button" onclick="location.href='${urlSaleInvoiceList}'">
                                        <fmt:message key="Sale.viewInvoices"/>
                                    </html:button>
                                </c:when>
                                <c:when test="${not empty invoiceInfo.documentId}">
                                    <html:button property="" styleClass="button" onclick="location.href='${urlDownloadDocument}'">
                                        <fmt:message key="Sale.openInvoice"/>
                                    </html:button>
                                </c:when>
                            </c:choose>
                        </app2:checkAccessRight>

                    </c:if>

                <html:cancel styleClass="button">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>

<c:if test="${'update' == op}">
    <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
        <iframe name="frame1"
                src="<app:url value="SalesProcess/SalePosition/List.do?processId=${saleBySalesProcessForm.dtoMap['processId']}&saleId=${saleBySalesProcessForm.dtoMap['saleId']}"/>"
                class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </app2:checkAccessRight>
</c:if>