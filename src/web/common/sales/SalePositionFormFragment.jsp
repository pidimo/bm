<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="saleNetGross"
       value="${app2:getNetGrossFieldFromSale(param.saleId ,pageContext.request)}"/>
<c:if test="${empty canChangePayMethod}">
    <c:set var="canChangePayMethod" value="${true}" scope="request"/>
</c:if>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="useNetPrice" value="${netPrice == saleNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == saleNetGross}"/>

<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>


<html:form action="${action}" focus="dto(productName)" enctype="multipart/form-data">

<html:hidden property="dto(oldPriceElement)" styleId="field_price"/>

<html:hidden property="dto(saleNetGross)" value="${saleNetGross}" styleId="saleNetGrossId"/>
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(saleId)" value="${param.saleId}"/>
<html:hidden property="dto(processId)" styleId="processId"/>

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

            <c:if test="${empty categoryTabLinkUrl}">
                <c:set var="categoryTabLinkUrl"
                       value="/sales/SalePosition/CategoryTab/Forward/Update.do?index=${param.index}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
                       scope="request"/>
            </c:if>
            <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                <app2:categoryTabLink id="salePositionCategoryLinkId"
                                      action="${categoryTabLinkUrl}"
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
        <fmt:message key="SalePosition.productName"/>
    </TD>
    <TD class="contain" width="32%" nowrap>
        <html:hidden property="dto(productId)" styleId="fieldProductId_id"/>
        <app:text property="dto(productName)"
                  styleClass="mediumText"
                  maxlength="40"
                  readonly="true"
                  styleId="fieldProductName_id"
                  tabindex="1"
                  view="${'delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced']}"/>

        <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
            <c:if test="${'update' == op && not empty salePositionForm.dtoMap['productId']}">
                <c:set var="productEditLink"
                       value="/products/Product/Forward/Update.do?productId=${salePositionForm.dtoMap['productId']}&dto(productId)=${salePositionForm.dtoMap['productId']}&dto(productName)=${app2:encode(salePositionForm.dtoMap['productName'])}&index=0"/>
                <app:link action="${productEditLink}" contextRelative="true" tabindex="2">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
                </app:link>
            </c:if>
        </app2:checkAccessRight>

        <tags:selectPopup url="/products/SearchProduct.do"
                          name="SearchProduct"
                          titleKey="Common.search"
                          hide="${'delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced']}"
                          submitOnSelect="true"
                          tabindex="2"/>
        <tags:clearSelectPopup keyFieldId="fieldProductId_id"
                               nameFieldId="fieldProductName_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="${'delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced']}"
                               tabindex="3"/>
    </TD>
    <TD class="label" width="18%">
        <fmt:message key="SalePosition.quantity"/>
    </TD>
    <TD class="contain" width="32%">
        <app:numberText property="dto(quantity)"
                        styleClass="numberText"
                        maxlength="10"
                        numberType="decimal"
                        maxInt="10"
                        maxFloat="2"
                        view="${'delete' == op || false == salePositionForm.dtoMap['canUpdateQuantityField']}"
                        tabindex="10"/>
    </TD>
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
                      styleId="field_unitId"
                      tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
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
                            tabindex="11"/>
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
                            tabindex="11"/>
        </TD>
    </c:if>
</tr>
<tr>
    <td class="label">
        <fmt:message key="SalePosition.serial"/>
    </td>
    <td class="contain">
        <app:text property="dto(serial)"
                  styleClass="mediumText"
                  maxlength="100"
                  view="${'delete' == op}" tabindex="5"/>
    </td>
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
                        tabindex="12"/>
        <c:out value="%"/>
    </TD>
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
                  tabindex="6"/>
    </td>
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
                            tabindex="13"/>
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
                            tabindex="13"/>
        </TD>
    </c:if>
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
        <fmt:message key="SalePosition.deliveryDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(deliveryDate)"
                      styleId="startDate"
                      calendarPicker="${'delete' != op}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      view="${'delete' == op}"
                      tabindex="14"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="SalePosition.active"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(active)"
                       value="true"
                       disabled="${op == 'delete'}"
                       styleClass="radio"
                       tabindex="8"/>
    </td>
    <td class="label">
        <fmt:message key="SalePosition.payMethod"/>
    </td>
    <td class="contain">
        <c:set var="payMethodList" value="${app2:getSalePositionPayMethods(pageContext.request)}"/>
        <html:select property="dto(payMethod)"
                     styleClass="mediumSelect"
                     styleId="payMethodId"
                     readonly="${'delete' == op || false == canChangePayMethod}"
                     tabindex="15">
            <html:option value=""/>
            <html:options collection="payMethodList"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="SalePosition.text"/>
        <html:textarea property="dto(text)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       readonly="${'delete'== op}" tabindex="16"/>
    </td>
</tr>

<tr>
    <td colspan="4">
        <c:if test="${empty salePositionSubCategoriesAjaxAction}">
            <c:set var="salePositionSubCategoriesAjaxAction" value="/sales/SalePosition/MainPageReadSubCategories.do" scope="request"/>
        </c:if>

        <c:if test="${empty salePositionDownloadAttachCategoryAction}">
            <c:set var="salePositionDownloadAttachCategoryAction"
                   value="/sales/SalePosition/MainPage/DownloadCategoryFieldValue.do?dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
                   scope="request"/>
        </c:if>

        <c:set var="elementCounter" value="${17}" scope="request"/>
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