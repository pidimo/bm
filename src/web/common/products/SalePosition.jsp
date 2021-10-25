<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="singleWithoutContract"
       value="<%=SalesConstants.PayMethod.SingleWithoutContract.getConstantAsString()%>"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<!--enable or disable sale link if salePosition has related by sale-->
<c:choose>
    <c:when test="${null != salePositionForm.dtoMap['saleId'] && not empty salePositionForm.dtoMap['saleId'] && showSaleLink}">
        <c:set var="showSaleLink" value="${true}" scope="request"/>
        <c:set var="saleNetGross"
               value="${app2:getNetGrossFieldFromSale(salePositionForm.dtoMap['saleId'] ,pageContext.request)}"/>

        <c:set var="useNetPrice" value="${netPrice == saleNetGross}"/>
        <c:set var="useGrossPrice" value="${grossPrice == saleNetGross}"/>

    </c:when>
    <c:otherwise>
        <c:set var="showSaleLink" value="${false}" scope="request"/>
    </c:otherwise>
</c:choose>

<html:form action="${action}" focus="${showSaleLink == true ? 'dto(unitId)' : 'dto(customerId)'}" enctype="multipart/form-data">

<html:hidden property="dto(oldPriceElement)" styleId="field_price"/>

<c:choose>
    <c:when test="${showSaleLink}">
        <html:hidden property="dto(saleNetGross)" value="${saleNetGross}" styleId="actionNetGrossId"/>
    </c:when>
    <c:otherwise>
        <html:hidden property="dto(saleNetGross)" value="${1}" styleId="actionNetGrossId"/>
    </c:otherwise>
</c:choose>

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(productId)" value="${param.productId}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<c:if test="${('update' == op) || ('delete' == op)}">
    <html:hidden property="dto(salePositionHasInvoiced)"/>
    <html:hidden property="dto(showPayMethodWarn)" styleId="showPayMethodWarnId"/>
    <html:hidden property="dto(salePositionId)"/>
    <html:hidden property="dto(canChangePayMethod)"/>
    <html:hidden property="dto(canUpdateQuantityField)"/>
</c:if>
<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(copyPayMethod)"/>
</c:if>
<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>


<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}" functionality="SALEPOSITION"
                             onclick="javascript:fillMultipleSelectValues();"
                             styleClass="button" property="save" tabindex="50">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="51">
            <fmt:message key="Common.cancel"/>
        </html:cancel>

        <%--top links--%>
        &nbsp;
        <c:if test="${op == 'update'}">
            <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                <app2:categoryTabLink id="salePositionCategoryLinkId"
                                      action="/products/SalePosition/CategoryTab/Forward/Update.do?index=${param.index}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
                                      categoryConstant="7"
                                      finderName="findValueBySalePositionId"
                                      styleClass="folderTabLink">
                    <app2:categoryFinderValue forId="salePositionCategoryLinkId" value="${salePositionForm.dtoMap['salePositionId']}"/>
                </app2:categoryTabLink>
            </app2:checkAccessRight>
        </c:if>
    </td>
</tr>
<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<tr>
    <TD class="label" width="18%">
        <fmt:message key="SalePosition.customer"/>
    </TD>
    <TD class="contain" width="32%" nowrap>

        <c:if test="${not empty salePositionForm.dtoMap['customerId'] and 'update' == op}">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
                <c:set var="addressMap" value="${app2:getAddressMap(salePositionForm.dtoMap['customerId'])}"/>
                <c:choose>
                    <c:when test="${personType == addressMap['addressType']}">
                        <c:set var="addressEditLink"
                               value="/contacts/Person/Forward/Update.do?contactId=${salePositionForm.dtoMap['customerId']}&dto(addressId)=${salePositionForm.dtoMap['customerId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="addressEditLink"
                               value="/contacts/Organization/Forward/Update.do?contactId=${salePositionForm.dtoMap['customerId']}&dto(addressId)=${salePositionForm.dtoMap['customerId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
        </c:if>


        <html:hidden property="dto(customerId)" styleId="fieldAddressId_id"/>
        <app:text property="dto(customerName)"
                  styleClass="mediumText" maxlength="40" readonly="true"
                  styleId="fieldAddressName_id"
                  view="${'delete' == op || showSaleLink == true}" tabindex="1"/>

        <c:if test="${not empty addressEditLink}">
            <app:link action="${addressEditLink}" contextRelative="true" tabindex="2">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
            </app:link>
        </c:if>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          hide="${'delete' == op || showSaleLink == true}"
                          tabindex="2"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="${'delete' == op || showSaleLink == true}"
                               tabindex="3"/>
    </TD>
    <TD class="label" width="18%">
        <fmt:message key="SalePosition.quantity"/>
    </TD>
    <TD class="contain" width="32%" nowrap>
        <app:numberText property="dto(quantity)"
                        styleClass="numberText"
                        maxlength="10"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        view="${'delete' == op || false == salePositionForm.dtoMap['canUpdateQuantityField']}"
                        tabindex="11"/>
    </TD>
</tr>
<tr>
    <td class="label">
        <fmt:message key="SalePosition.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="mediumSelect"
                      readOnly="${'delete' == op || showSaleLink == true}" tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty salePositionForm.dtoMap['customerId']?salePositionForm.dtoMap['customerId']:0}"/>
        </fanta:select>
    </td>
    <c:choose>
        <c:when test="${showSaleLink}">
            <c:if test="${useNetPrice}">
                <html:hidden property="dto(unitPriceGross)"/>
                <TD class="label">
                    <fmt:message key="SalePosition.unitPriceNet"/>
                </TD>
                <TD class="contain">
                    <app:numberText property="dto(unitPrice)"
                                    styleClass="numberText"
                                    maxlength="18"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="4"
                                    view="${'delete' == op}"
                                    tabindex="12"/>
                </TD>
            </c:if>
            <c:if test="${useGrossPrice}">
                <html:hidden property="dto(unitPrice)"/>
                <TD class="label">
                    <fmt:message key="SalePosition.unitPriceGross"/>
                </TD>
                <TD class="contain">

                    <app:numberText property="dto(unitPriceGross)"
                                    styleClass="numberText"
                                    maxlength="18"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="4"
                                    view="${'delete' == op}"
                                    tabindex="12"/>
                </TD>
            </c:if>
        </c:when>
        <c:otherwise>
            <TD class="label">
                <fmt:message key="SalePosition.unitPrice"/>
            </TD>
            <TD class="contain">
                <app:numberText property="dto(unitPrice)"
                                styleClass="numberText"
                                maxlength="18"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="4"
                                view="${'delete' == op}"
                                tabindex="12"/>
            </TD>
        </c:otherwise>
    </c:choose>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="SalePosition.unitName"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(unitId)"
                      listName="productUnitList"
                      firstEmpty="true"
                      labelProperty="name"
                      valueProperty="id"
                      module="/catalogs"
                      styleClass="mediumSelect"
                      readOnly="${op == 'delete'}"
                      styleId="field_unitId" tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="SalePosition.discount"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(discount)"
                        styleId="discount_name"
                        styleClass="numberText"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        view="${op == 'delete'}"
                        tabindex="13"/>
        <c:out value="%"/>
    </TD>
</tr>
<tr>
    <td class="label">
        <fmt:message key="SalePosition.serial"/>
    </td>
    <td class="contain">
        <app:text property="dto(serial)"
                  styleClass="mediumText"
                  maxlength="100"
                  view="${'delete' == op}" tabindex="6"/>
    </td>
    <c:choose>
        <c:when test="${showSaleLink}">
            <c:if test="${useNetPrice}">
                <html:hidden property="dto(totalPriceGross)"/>
                <TD class="label">
                    <fmt:message key="SalePosition.totalPriceNet"/>
                </TD>
                <TD class="contain">
                    <app:numberText property="dto(totalPrice)"
                                    styleId="totalPrice_name"
                                    styleClass="numberText"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"
                                    readonly="true"
                                    view="true"
                                    tabindex="14"/>
                </TD>
            </c:if>
            <c:if test="${useGrossPrice}">
                <html:hidden property="dto(totalPrice)"/>
                <TD class="label">
                    <fmt:message key="SalePosition.totalPriceGross"/>
                </TD>
                <TD class="contain">
                    <app:numberText property="dto(totalPriceGross)"
                                    styleId="totalPrice_name"
                                    styleClass="numberText"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"
                                    readonly="true"
                                    view="true"
                                    tabindex="14"/>
                </TD>
            </c:if>
        </c:when>
        <c:otherwise>
            <TD class="label">
                <fmt:message key="SalePosition.totalPrice"/>
            </TD>
            <TD class="contain">
                <app:numberText property="dto(totalPrice)"
                                styleId="totalPrice_name"
                                styleClass="numberText"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="2"
                                readonly="true"
                                view="true"
                                tabindex="14"/>
            </TD>
        </c:otherwise>
    </c:choose>
</tr>
<tr>
    <td class="label">
        <fmt:message key="SalePosition.versionNumber"/>
    </td>
    <td class="contain">
        <app:text property="dto(versionNumber)"
                  styleClass="mediumText"
                  maxlength="20"
                  styleId="field_versionNumber"
                  view="${'delete' == op}"
                  tabindex="7"/>
    </td>
    <td class="label">
        <fmt:message key="SalePosition.deliveryDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(deliveryDate)"
                      styleId="startDate"
                      calendarPicker="${'delete' != op}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      currentDate="${'create' == op}"
                      view="${'delete' == op}"
                      tabindex="15"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="SalePosition.vat"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(vatId)"
                      listName="vatList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="mediumSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${'delete' == op}"
                      tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="SalePosition.active"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(active)"
                       value="true"
                       disabled="${op == 'delete'}"
                       styleClass="radio"
                       tabindex="16"/>
    </td>
</tr>

<tr>
    <c:if test="${showSaleLink == true}">
        <td class="label">
            <fmt:message key="SalePosition.sale"/>
        </td>
        <td class="contain">
            <html:hidden property="dto(saleId)"/>
            <html:hidden property="dto(saleName)"/>

            <c:out value="${salePositionForm.dtoMap['saleName']}"/>

            <c:if test="${app2:hasAccessRight(pageContext.request,'SALE' ,'CREATE' )}">
                <app:link contextRelative="true"
                          action="sales/Sale/Forward/Update.do?saleId=${salePositionForm.dtoMap['saleId']}&dto(saleId)=${salePositionForm.dtoMap['saleId']}&dto(title)=${app2:encode(salePosition.dtoMap['saleName'])}&index=0">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                </app:link>
            </c:if>
        </td>
        <td class="label">
            <fmt:message key="SalePosition.payMethod"/>
        </td>
        <td class="contain">
            <c:set var="payMethodList" value="${app2:getSalePositionPayMethods(pageContext.request)}"/>
            <html:select property="dto(payMethod)"
                         styleClass="mediumSelect"
                         styleId="payMethodId"
                         readonly="${'delete' == op || false == salePositionForm.dtoMap['canChangePayMethod']}"
                         tabindex="16">
                <html:option value=""/>
                <html:options collection="payMethodList"
                              property="value"
                              labelProperty="label"/>
            </html:select>
        </td>
    </c:if>
</tr>


<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="SalePosition.text"/>
        <html:textarea property="dto(text)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       readonly="${'delete'== op}" tabindex="18"/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <c:if test="${empty salePositionSubCategoriesAjaxAction}">
            <c:set var="salePositionSubCategoriesAjaxAction" value="/products/SalePosition/MainPageReadSubCategories.do" scope="request"/>
        </c:if>

        <c:if test="${empty salePositionDownloadAttachCategoryAction}">
            <c:set var="salePositionDownloadAttachCategoryAction"
                   value="/products/SalePosition/MainPage/DownloadCategoryFieldValue.do?dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
                   scope="request"/>
        </c:if>

        <c:set var="elementCounter" value="${19}" scope="request"/>
        <c:set var="ajaxAction" value="${salePositionSubCategoriesAjaxAction}" scope="request"/>
        <c:set var="downloadAction" value="${salePositionDownloadAttachCategoryAction}" scope="request"/>
        <c:set var="formName" value="salePositionForm" scope="request"/>
        <c:set var="table" value="<%=ContactConstants.SALE_POSITION_CATEGORY%>" scope="request"/>
        <c:set var="operation" value="${op}" scope="request"/>
        <c:set var="labelWidth" value="18" scope="request"/>
        <c:set var="containWidth" value="82" scope="request"/>
        <c:set var="generalWidth" value="${250}" scope="request"/>
        <c:import url="/common/catalogs/CategoryUtil.jsp"/>
        <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}" functionality="SALEPOSITION"
                             onclick="javascript:fillMultipleSelectValues();"
                             styleClass="button" property="save" tabindex="${lastTabIndex+1}">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="${lastTabIndex+2}">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>
<c:if test="${singleWithoutContract != salePositionForm.dtoMap['copyPayMethod']}">
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
        <c:if test="${'update' == op}">
            <iframe name="frame1"
                    src="<app:url value="ProductContractBySalePosition/List.do?salePositionId=${salePositionForm.dtoMap['salePositionId']}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&customerName=${app2:encode(salePositionForm.dtoMap['customerName'])}"/>"
                    class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </c:if>
    </app2:checkAccessRight>
</c:if>