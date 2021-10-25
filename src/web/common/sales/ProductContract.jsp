<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/cacheable/jquery.bgiframe.min.js"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery-jatun-sexy-combo-1.0.4.js"/>

<script type="text/javascript">
    function reSubmit() {
        document.forms[0].submit();
    }
</script>

<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<!--define module name to build AJAX url-->
<c:if test="${empty fromModule}">
    <c:set var="fromModule" value="sales" scope="request"/>
</c:if>

<!--set view only address field-->
<c:if test="${empty readOnlyForAddress}">
    <c:set var="readOnlyForAddress" value="false" scope="request"/>
</c:if>

<!--if contract is related with saleposition and sale, show this information in page-->
<c:if test="${empty showSaleInformation}">
    <c:set var="showSaleInformation" value="false" scope="request"/>
</c:if>
<!--url to show saleposition related with product-->
<c:if test="${empty productSalePositionAction}">
    <c:set var="productSalePositionAction" value="products/FromSaleModule/SalePosition/Forward/Update.do"
           scope="request"/>
</c:if>

<c:if test="${empty saleTabKey}">
    <c:set var="saleTabKey" value="Sale.Tab.Detail" scope="request"/>
</c:if>

<c:if test="${empty salePositionTabKey}">
    <c:set var="salePositionTabKey" value="Sale.Tab.SalePosition" scope="request"/>
</c:if>

<c:set var="single" value="<%=SalesConstants.PayMethod.Single.getConstantAsString()%>"/>
<c:set var="periodic" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>
<c:set var="partialPeriodic" value="<%=SalesConstants.PayMethod.PartialPeriodic.getConstantAsString()%>"/>
<c:set var="partialFixed" value="<%=SalesConstants.PayMethod.PartialFixed.getConstantAsString()%>"/>

<c:set var="amount" value="<%=SalesConstants.AmounType.AMOUNT.getConstantAsString()%>"/>
<c:set var="percentage" value="<%=SalesConstants.AmounType.PERCENTAGE.getConstantAsString()%>"/>

<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>

<c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>

<html:form action="${action}" focus="dto(contractNumber)" styleId="mainFormId">

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(salePositionId)" value="${productContractForm.dtoMap['salePositionId']}"/>

<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<c:if test="${('update' == op) || ('delete' == op)}">
    <html:hidden property="dto(contractId)"/>
    <html:hidden property="dto(hasInvoicePositions)" styleId="hasInvoicePositionsId"/>
    <html:hidden property="dto(totalPaid)" styleId="totalPaidId"/>
</c:if>
<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
</c:if>
<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="PRODUCTCONTRACT"
                             styleClass="button" property="save" tabindex="103">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="104">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>

<c:set var="relatedWithSale"
       value="${null != productContractForm.dtoMap['saleId'] && not empty productContractForm.dtoMap['saleId']}"/>
<c:set var="relatedWithSalePosition"
       value="${null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId']}"/>

<c:if test="${'true' == showSaleInformation && relatedWithSalePosition}">
    <html:hidden property="dto(salePositionId)"/>
    <html:hidden property="dto(salePositionCustomerId)"/>
    <html:hidden property="dto(productName)"/>
    <html:hidden property="dto(customerName)" styleId="customerName"/>
    <tr>
        <td class="title" colspan="4">
            <fmt:message key="ProductContract.title.saleInformation"/>
        </td>
    </tr>
    <c:choose>
        <c:when test="${relatedWithSale}">
            <tr>
                <html:hidden property="dto(saleId)"/>
                <html:hidden property="dto(saleName)"/>
                <td class="label" width="18%">
                    <fmt:message key="ProductContract.sale"/>
                </td>
                <td class="contain" width="32%">
                    <c:out value="${productContractForm.dtoMap['saleName']}"/>

                    <c:if test="${app2:hasAccessRight(pageContext.request,'SALE' ,'VIEW' )}">
                        <app:link contextRelative="true"
                                  action="${fromModule}/Sale/Forward/Update.do?contactId=${productContractForm.dtoMap['salePositionCustomerId']}&saleId=${productContractForm.dtoMap['saleId']}&dto(saleId)=${productContractForm.dtoMap['saleId']}&dto(title)=${app2:encode(productContractForm.dtoMap['saleName'])}&tabKey=${saleTabKey}">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                        </app:link>
                    </c:if>
                </td>
                <td class="label" width="18%">
                    <fmt:message key="ProductContract.salePosition"/>
                </td>
                <td class="contain" width="32%">
                    <c:out value="${productContractForm.dtoMap['productName']}"/>

                    <c:if test="${app2:hasAccessRight(pageContext.request,'SALEPOSITION' ,'VIEW' )}">
                        <app:link contextRelative="true"
                                  action="${fromModule}/SalePosition/Forward/Update.do?contactId=${productContractForm.dtoMap['salePositionCustomerId']}&saleId=${productContractForm.dtoMap['saleId']}&dto(saleId)=${productContractForm.dtoMap['saleId']}&dto(title)=${app2:encode(productContractForm.dtoMap['saleName'])}&salePositionId=${productContractForm.dtoMap['salePositionId']}&dto(salePositionId)=${productContractForm.dtoMap['salePositionId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&tabKey=${salePositionTabKey}">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                        </app:link>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="ProductContract.customer"/>
                </td>
                <td class="contain" colspan="3">
                    <c:out value="${productContractForm.dtoMap['customerName']}"/>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td class="label" width="18%">
                    <fmt:message key="ProductContract.salePosition"/>
                </td>
                <td class="contain" width="32%">
                    <c:out value="${productContractForm.dtoMap['productName']}"/>

                    <c:choose>
                        <c:when test="${'sales' == fromModule && !relatedWithSale && app2:hasAccessRight(pageContext.request,'SALEPOSITION' ,'VIEW' ) && app2:hasAccessRight(pageContext.request,'PRODUCT' ,'VIEW' )}">
                            <app:link contextRelative="true"
                                      action="${productSalePositionAction}?contractId=${productContractForm.dtoMap['contractId']}&dto(contractId)=${productContractForm.dtoMap['contractId']}&productId=${productContractForm.dtoMap['productId']}&dto(productId)=${productContractForm.dtoMap['productId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&salePositionId=${productContractForm.dtoMap['salePositionId']}&dto(salePositionId)=${productContractForm.dtoMap['salePositionId']}&dto(customerName)=${app2:encode(productContractForm.dtoMap['customerName'])}&tabKey=Product.Tab.SalePositions">
                                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                            </app:link>
                        </c:when>
                        <c:when test="${'sales' != fromModule && !relatedWithSale && app2:hasAccessRight(pageContext.request,'SALEPOSITION' ,'VIEW' )}">
                            <app:link contextRelative="true"
                                      action="${fromModule}/SalePosition/Forward/Update.do?saleId=${productContractForm.dtoMap['saleId']}&dto(saleId)=${productContractForm.dtoMap['saleId']}&dto(title)=${app2:encode(productContractForm.dtoMap['saleName'])}&salePositionId=${productContractForm.dtoMap['salePositionId']}&dto(salePositionId)=${productContractForm.dtoMap['salePositionId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&tabKey=${salePositionTabKey}">
                                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                            </app:link>
                        </c:when>
                    </c:choose>
                </td>
                <td class="label" width="18%">
                    <fmt:message key="ProductContract.customer"/>
                </td>
                <td class="contain" width="32%">
                    <c:out value="${productContractForm.dtoMap['customerName']}"/>
                </td>
            </tr>
        </c:otherwise>
    </c:choose>
</c:if>

<TR>
    <TD colspan="4" class="title">
        <div id="ajaxMessageId" class="messageToolTip" style="display:none; position:absolute; left:7%">
            <fmt:message key="Common.message.loading"/>
        </div>
        <c:out value="${title}"/>
    </TD>
</TR>

<tr>
    <td class="label" width="18%" nowrap>
        <fmt:message key="ProductContract.contractNumber"/>
    </td>
    <td class="contain" width="32%" nowrap>
        <app:text property="dto(contractNumber)"
                  styleClass="mediumText"
                  maxlength="40"
                  view="${'delete' == op}"
                  tabindex="1"/>
    </td>
    <td class="label" width="18%" nowrap>
        <fmt:message key="Contract.payMethod"/>
    </td>
    <td class="contain" width="32%" nowrap>
        <!--is used in form to detect if paymethod has changed-->
        <html:hidden property="dto(changePayMethod)" value="false" styleId="changePayMethodId"/>

        <c:if test="${'true' == productContractForm.dtoMap['hasInvoicePositions']}">
            <html:hidden property="dto(payMethodBK)" value="${productContractForm.dtoMap['payMethod']}"
                         styleId="payMethodId"/>
        </c:if>

        <html:select property="dto(payMethod)"
                     styleClass="mediumSelect"
                     styleId="payMethodId"
                     readonly="${'delete' == op || 'true' == productContractForm.dtoMap['hasInvoicePositions']}"
                     onchange="javascript:selectPayMethodUI(this);"
                     tabindex="9">
            <html:option value=""/>
            <html:options collection="payMethodList"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>

<tr>
    <TD class="label">
        <fmt:message key="Contract.contact"/>
    </TD>
    <TD class="contain">
        <c:if test="${not empty productContractForm.dtoMap['addressId'] and 'update' == op}">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
                <c:set var="addressMap" value="${app2:getAddressMap(productContractForm.dtoMap['addressId'])}"/>
                <c:choose>
                    <c:when test="${personType == addressMap['addressType']}">
                        <c:set var="addressEditLink"
                               value="/contacts/Person/Forward/Update.do?contactId=${productContractForm.dtoMap['addressId']}&dto(addressId)=${productContractForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="addressEditLink"
                               value="/contacts/Organization/Forward/Update.do?contactId=${productContractForm.dtoMap['addressId']}&dto(addressId)=${productContractForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
        </c:if>

        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="dto(addressName)"
                  styleClass="mediumText"
                  maxlength="40"
                  readonly="true"
                  styleId="fieldAddressName_id"
                  view="${'delete' == op || 'true' == readOnlyForAddress}"
                  tabindex="2"/>

        <c:if test="${not empty addressEditLink}">
            <app:link action="${addressEditLink}" contextRelative="true" tabindex="2">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
            </app:link>
        </c:if>

        <tags:selectPopup url="/contacts/SearchAddress.do"
                          name="searchAddress"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          hide="${'delete' == op || 'true' == readOnlyForAddress}"
                          tabindex="2"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id"
                               nameFieldId="fieldAddressName_id"
                               name="searchAddress"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="${'delete' == op || 'true' == readOnlyForAddress}"
                               tabindex="2"/>
    </TD>
    <td class="label">
        <fmt:message key="ProductContract.netGross"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(changeNetGross)" value="false" styleId="changeNetGrossId"/>

        <html:select property="dto(netGross)"
                     styleClass="mediumSelect"
                     readonly="${'delete' == op || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'])}"
                     tabindex="10"
                     onchange="javascript:selectNetGross();">
            <html:option value=""/>
            <html:options collection="netGrossOptions"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>

<tr>
    <td class="label">
        <fmt:message key="ProductContract.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)"
                      listName="searchContactPersonList"
                      module="/contacts"
                      firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="mediumSelect"
                      readOnly="${'delete' == op}"
                      tabIndex="3">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty productContractForm.dtoMap['addressId']?productContractForm.dtoMap['addressId']:0}"/>
        </fanta:select>
    </td>

    <c:set var="isPeriodic" value="${periodic == productContractForm.dtoMap['payMethod']}" scope="request"/>
    <c:choose>
        <c:when test="${isPeriodic}">
            <td colspan="2">
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr>
                        <td class="label" width="36%">
                            <fmt:message key="Contract.periodicPrice"/>
                        </td>
                        <td class="contain" width="64%">
                            <app:numberText property="dto(price)"
                                            styleClass="numberText"
                                            maxlength="12"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            styleId="field_price"
                                            view="${'delete' == op  || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod'])}"
                                            tabindex="11"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="ProductContract.pricePeriod"/>
                        </td>
                        <td class="contain">
                            <app:numberText property="dto(pricePeriod)"
                                            styleClass="numberText"
                                            maxlength="2"
                                            numberType="integer"
                                            view="${'delete' == op}"
                                            tabindex="11"/>
                            &nbsp;
                            <fmt:message key="ProductContract.months"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="ProductContract.pricePerMonth"/>
                        </td>
                        <td class="contain">
                            <app:numberText property="dto(pricePerMonth)"
                                            styleClass="numberText"
                                            maxlength="12"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            view="true"/>
                        </td>
                    </tr>
                </table>
            </td>
        </c:when>
        <c:otherwise>
            <td class="label">
                <fmt:message key="Contract.price"/>
            </td>
            <td class="contain">
                <app:numberText property="dto(price)"
                                styleClass="numberText"
                                maxlength="12"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="2"
                                styleId="field_price"
                                view="${'delete' == op  || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod'])}"
                                tabindex="11"/>
            </td>
        </c:otherwise>
    </c:choose>
</tr>

<tr>
    <TD class="label">
        <fmt:message key="ProductContract.sentAddress"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(sentAddressId)" listName="invoiceAddressRelationList"
                      labelProperty="relatedAddressName" valueProperty="relatedAddressId" styleClass="mediumSelect"
                      readOnly="${op == 'delete'}"
                      onChange="reSubmit()"
                      module="/contacts" tabIndex="4" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty productContractForm.dtoMap['addressId'] ? productContractForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </TD>

    <TD class="label">
        <fmt:message key="Contract.discount"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(discount)"
                        styleClass="numberText"
                        maxlength="12"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        view="${'delete' == op || ('true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod'])}"
                        tabindex="12"/>
        <c:choose>
            <c:when test="${'create' != op && null != productContractForm.dtoMap['discount'] && not empty productContractForm.dtoMap['discount']}">
                <c:out value="%"/>
            </c:when>
            <c:when test="${'create' == op}">
                <c:out value="%"/>
            </c:when>
            <c:otherwise>
                &nbsp;
            </c:otherwise>
        </c:choose>
    </TD>
</tr>

<tr>
    <td class="label">
        <fmt:message key="ProductContract.sentContactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="mediumSelect"
                      tabIndex="5"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                         value="${not empty productContractForm.dtoMap['sentAddressId'] ? productContractForm.dtoMap['sentAddressId'] : 0}"/>
        </fanta:select>
    </td>

    <td class="label">
        <fmt:message key="ProductContract.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="mediumSelect"
                      module="/catalogs"
                      readOnly="${op == 'delete'}"
                      tabIndex="13">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>

<tr>
    <td class="label">
        <fmt:message key="ProductContract.additionalAddress"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                      firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty productContractForm.dtoMap['addressId'] ? productContractForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </td>

    <td class="label">
        <fmt:message key="ProductContract.vat"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(vatId)"
                      listName="vatList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="mediumSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${op == 'delete'}"
                      tabIndex="14">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contract.contractType"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(contractTypeId)"
                      listName="contractTypeList"
                      firstEmpty="true"
                      labelProperty="name"
                      valueProperty="contractTypeId"
                      module="/catalogs"
                      styleClass="mediumSelect"
                      readOnly="${op == 'delete'}"
                      tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>

    <td class="label">
        <fmt:message key="ProductContract.payCondition"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(payConditionId)"
                      listName="payConditionList"
                      styleId="payConditionId"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="mediumSelect"
                      firstEmpty="true"
                      module="/catalogs"
                      readOnly="${op == 'delete'}"
                      tabIndex="15">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contract.product"/>
    </TD>
    <TD class="contain" nowrap>
        <html:hidden property="dto(productId)" styleId="fieldProductId_id"/>
        <html:hidden property="dto(productVersionNumber)" styleId="field_versionNumber"/>
        <html:hidden property="dto(productUnitId)" styleId="field_unitId"/>

        <app:text property="dto(productName)"
                  styleClass="mediumText"
                  maxlength="40"
                  readonly="true"
                  styleId="fieldProductName_id"
                  view="${'delete' == op || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId'])}"
                  tabindex="7"/>

        <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
            <c:if test="${'update' == op && not empty productContractForm.dtoMap['productId']}">
                <c:set var="productEditLink"
                       value="/products/Product/Forward/Update.do?productId=${productContractForm.dtoMap['productId']}&dto(productId)=${productContractForm.dtoMap['productId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&index=0"/>
                <app:link action="${productEditLink}" contextRelative="true">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                </app:link>
            </c:if>
        </app2:checkAccessRight>

        <tags:selectPopup url="/products/SearchProduct.do"
                          name="SearchProduct"
                          titleKey="Common.search"
                          hide="${'delete' == op || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId'])}"
                          tabindex="7"/>
        <tags:clearSelectPopup keyFieldId="fieldProductId_id"
                               nameFieldId="fieldProductName_id"
                               titleKey="Common.clear"
                               hide="${'delete' == op || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId'])}"
                               tabindex="7"/>
    </TD>

    <td class="label">
        <fmt:message key="Contract.orderDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(orderDate)"
                      styleId="startDate"
                      calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      currentDate="${'create' == op}"
                      view="${'delete' == op}"
                      tabindex="16"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="ProductContract.seller"/>
    </td>
    <td class="contain" colspan="3">
        <fanta:select property="dto(sellerId)"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="mediumSelect"
                      firstEmpty="true"
                      module="/contacts"
                      readOnly="${'delete' == op}"
                      tabIndex="8">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>

<tr>
    <td colspan="4">
        <div id="paymentOptionsId" width="100%">
        </div>
    </td>
</tr>

<tr>
    <td colspan="4" class="topLabel">
        <fmt:message key="Contract.note"/>
        <br>
        <html:textarea property="dto(noteText)" styleClass="middleDetail" tabindex="100"
                       style="height:120px;width:99%;"
                       readonly="${op == 'delete'}"/>
    </td>
</tr>

<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="PRODUCTCONTRACT"
                             styleClass="button" property="save" tabindex="101">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="102">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>


<c:set var="parameters" value="${app2:buildParametersForAjaxRequest(productContractForm)}"/>
<app2:jScriptUrl url="/${fromModule}/ProductContract/PaymentOptions.do?op=${op}" var="jsReadPayMethodUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="payMethod" value="payMethodId"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/${fromModule}/ProductContract/PaymentOptions.do?op=${op}" var="jsAddRowUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="payMethod" value="payMethodId"/>
    <app2:jScriptUrlParam param="installment" value="installmentId"/>
    <app2:jScriptUrlParam param="amounType" value="amounTypeId"/>
    <app2:jScriptUrlParam param="payConditionId" value="payConditionId"/>
</app2:jScriptUrl>

<script type="text/javascript">

    function makePOSTAjaxRequest(urlAddress, parameters) {
        $.ajax({
            async:true,
            type: "POST",
            dataType: "html",
            data:parameters,
            url:urlAddress,
            beforeSend:setLoadMessage,
            success: function(data) {
                document.getElementById('paymentOptionsId').innerHTML = data;
                var messagesDiv = document.getElementById('ajaxMessageId');
                messagesDiv.style.display = 'none';
                setSexyGroupingCombo();
            },
            error: function(ajaxRequest) {
                ajaxErrorProcess(ajaxRequest.status);
            }
        });
    }

    function firstExecution() {
        var payMethodId = '${productContractForm.dtoMap['payMethod']}';
        makePOSTAjaxRequest(${jsReadPayMethodUrl}, ${parameters});
    }

    function selectPayMethodUI(obj) {
        document.getElementById('changePayMethodId').value = 'true';
        document.getElementById('mainFormId').submit();
    }

    function selectNetGross() {
        document.getElementById('changeNetGrossId').value = 'true';
        document.getElementById('mainFormId').submit();
    }

    function setLoadMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.message.loading',pageContext.request)}';
        messagesDiv.style.display = 'inline';
    }

    function ajaxErrorProcess(status) {
        var messagesDiv = document.getElementById('ajaxMessageId');
        if (status == 404 || status == 302) {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.sessionExpired',pageContext.request)}';
        } else {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('error.tooltip.unexpected',pageContext.request)}';
        }
        messagesDiv.style.display = 'inline';
    }

    function addRow(obj) {
        var installmentId = new Number(obj.value);
        var payMethodId = document.getElementById('payMethodId').value;
        var amounTypeId = document.getElementById('amounTypeId').value;
        var payConditionId = document.getElementById('payConditionId').value;
        var steptsInvoicedCounterId = document.getElementById('steptsInvoicedCounterId').value;

        if (installmentId < steptsInvoicedCounterId)
            installmentId = steptsInvoicedCounterId;

        if (installmentId.toString().indexOf("NaN") == -1) {
            var jsParams = buildJsParameters(installmentId);

            if ('' != jsParams) {
                makePOSTAjaxRequest(${jsAddRowUrl}, jsParams + ${parameters});
            } else
                makePOSTAjaxRequest(${jsAddRowUrl}, ${parameters});
        }
    }

    function showPercentageSymbol(obj) {
        var amounTypeId = obj.value;
        var payMethodId = document.getElementById('payMethodId').value;
        var installmentId = new Number(document.getElementById('installmentId').value);
        var payConditionId = document.getElementById('payConditionId').value;

        if (installmentId > 0) {
            var jsParams = buildJsParameters(installmentId);

            if ('' != jsParams) {
                makePOSTAjaxRequest(${jsAddRowUrl}, jsParams + ${parameters});
            } else
                makePOSTAjaxRequest(${jsAddRowUrl}, ${parameters});
        }

    }
    function buildJsParameters(counter) {
        var params = '';
        for (var i = 1; i <= counter; i++) {
            var amountIdentifier = 'payAmountId_' + i;
            var dateIdentifier = 'payDateId_' + i;

            if (null != document.getElementById(amountIdentifier)) {
                var payAmount_i = document.getElementById(amountIdentifier);
                if ('' != payAmount_i.value) {
                    params = params + 'payAmount_' + i + '=' + encodeURI(payAmount_i.value) + '&';
                }

                var payDate_i = document.getElementById(dateIdentifier);
                if ('' != payDate_i.value) {
                    params = params + 'payDate_' + i + '=' + encodeURI(payDate_i.value) + '&';
                }
            }
        }

        return params;
    }

    function setSexyGroupingCombo() {
        $("#groupingId").sexyCombo({
            <c:if test="${not empty productContractForm.dtoMap['grouping']}">
            defaultValue:"${productContractForm.dtoMap['grouping']}",
            </c:if>
            suffix :"dto(grouping)",
            maxlength :"50",
            hiddenSuffix :"dto(groupingHidden)"
        });
    }


    firstExecution();
</script>
</html:form>