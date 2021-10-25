<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
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

<html:form action="${action}" focus="${showSaleLink == true ? 'dto(unitId)' : 'dto(customerId)'}"
           enctype="multipart/form-data" styleClass="form-horizontal">

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


    <div class="button ${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}" functionality="SALEPOSITION"
                             onclick="javascript:fillMultipleSelectValues();"
                             styleClass="button ${app2:getFormButtonClasses()}" property="save" tabindex="50">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="51">
            <fmt:message key="Common.cancel"/>
        </html:cancel>

            <%--top links--%>

        <c:if test="${op == 'update'}">
            <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                <app2:categoryTabLink id="salePositionCategoryLinkId"
                                      action="/products/SalePosition/CategoryTab/Forward/Update.do?index=${param.index}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
                                      categoryConstant="7"
                                      finderName="findValueBySalePositionId"
                                      styleClass="folderTabLink">
                    <app2:categoryFinderValue forId="salePositionCategoryLinkId"
                                              value="${salePositionForm.dtoMap['salePositionId']}"/>
                </app2:categoryTabLink>
            </app2:checkAccessRight>
        </c:if>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <c:out value="${title}"/>
        </legend>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.customer"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op || showSaleLink == true)}">
                    <c:if test="${not empty salePositionForm.dtoMap['customerId'] and 'update' == op}">
                        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                            <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
                            </c:set>
                            <c:set var="addressMap"
                                   value="${app2:getAddressMap(salePositionForm.dtoMap['customerId'])}"/>
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
                    <c:set var="showOnlyEditIcon" value="${'delete' == op || showSaleLink == true}"/>

                    <div class="${showOnlyEditIcon? 'row col-xs-11': 'input-group'}">
                        <app:text property="dto(customerName)"
                                  styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="40" readonly="true"
                                  styleId="fieldAddressName_id"
                                  view="${'delete' == op || showSaleLink == true}" tabindex="1"/>
                        <html:hidden property="dto(customerId)" styleId="fieldAddressId_id"/>
                        <c:if test="${showOnlyEditIcon}">
                    </div>
                    <div>
                        </c:if>
                        <span class="${showOnlyEditIcon?'pull-right':'input-group-btn'}">
                            <c:if test="${not empty addressEditLink}">
                                <app:link action="${addressEditLink}"
                                          styleClass="${showOnlyEditIcon?'':app2:getFormButtonClasses()}"
                                          contextRelative="true" titleKey="Common.edit" tabindex="2">
                                    <span class="glyphicon glyphicon-edit "></span>
                                </app:link>
                            </c:if>

                            <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                       styleId="searchAddress_id"
                                                       name="searchAddress"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Contact.Title.search"
                                                       submitOnSelect="true"
                                                       isLargeModal="true"
                                                       hide="${'delete' == op || showSaleLink == true}"
                                                       tabindex="2"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                            nameFieldId="fieldAddressName_id"
                                                            titleKey="Common.clear"
                                                            submitOnClear="true"
                                                            hide="${'delete' == op || showSaleLink == true}"
                                                            tabindex="3"/>
                        </span>
                    </div>
                </div>
            </div>

            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.quantity"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op || false == salePositionForm.dtoMap['canUpdateQuantityField'])}">
                    <app:numberText property="dto(quantity)"
                                    styleClass="numberText ${app2:getFormInputClasses()}"
                                    maxlength="10"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"
                                    view="${'delete' == op || false == salePositionForm.dtoMap['canUpdateQuantityField']}"
                                    tabindex="4"/>
                </div>
            </div>

        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.contactPerson"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op || showSaleLink == true)}">
                    <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                                  module="/contacts" firstEmpty="true"
                                  labelProperty="contactPersonName"
                                  valueProperty="contactPersonId"
                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${'delete' == op || showSaleLink == true}" tabIndex="5">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="addressId"
                                         value="${not empty salePositionForm.dtoMap['customerId']?salePositionForm.dtoMap['customerId']:0}"/>
                    </fanta:select>
                </div>
            </div>

            <c:choose>
                <c:when test="${showSaleLink}">
                    <c:if test="${useNetPrice}">
                        <html:hidden property="dto(unitPriceGross)"/>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="SalePosition.unitPriceNet"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                <app:numberText property="dto(unitPrice)"
                                                styleClass="numberText ${app2:getFormInputClasses()}"
                                                maxlength="18"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="4"
                                                view="${'delete' == op}"
                                                tabindex="6"/>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${useGrossPrice}">
                        <html:hidden property="dto(unitPrice)"/>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="SalePosition.unitPriceGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">

                                <app:numberText property="dto(unitPriceGross)"
                                                styleClass="numberText ${app2:getFormInputClasses()}"
                                                maxlength="18"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="4"
                                                view="${'delete' == op}"
                                                tabindex="6"/>
                            </div>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="SalePosition.unitPrice"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:numberText property="dto(unitPrice)"
                                            styleClass="numberText ${app2:getFormInputClasses()}"
                                            maxlength="18"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="4"
                                            view="${'delete' == op}"
                                            tabindex="6"/>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.unitName"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <fanta:select property="dto(unitId)"
                                  listName="productUnitList"
                                  firstEmpty="true"
                                  labelProperty="name"
                                  valueProperty="id"
                                  module="/catalogs"
                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${op == 'delete'}"
                                  styleId="field_unitId" tabIndex="7">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </div>
            </div>

            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.discount"/> (<c:out value="%"/>)
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <app:numberText property="dto(discount)"
                                    styleId="discount_name"
                                    styleClass="numberText ${app2:getFormInputClasses()}"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"
                                    view="${op == 'delete'}"
                                    tabindex="8"/>

                </div>
            </div>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.serial"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <app:text property="dto(serial)"
                              styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="100"
                              view="${'delete' == op}" tabindex="9"/>
                </div>
            </div>
            <c:choose>
                <c:when test="${showSaleLink}">
                    <c:if test="${useNetPrice}">
                        <html:hidden property="dto(totalPriceGross)"/>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="SalePosition.totalPriceNet"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <app:numberText property="dto(totalPrice)"
                                                styleId="totalPrice_name"
                                                styleClass="numberText ${app2:getFormInputClasses()}"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"
                                                readonly="true"
                                                view="true"
                                                tabindex="14"/>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${useGrossPrice}">
                        <html:hidden property="dto(totalPrice)"/>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="SalePosition.totalPriceGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <app:numberText property="dto(totalPriceGross)"
                                                styleId="totalPrice_name"
                                                styleClass="numberText ${app2:getFormInputClasses()}"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"
                                                readonly="true"
                                                view="true"
                                                tabindex="10"/>
                            </div>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="SalePosition.totalPrice"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <app:numberText property="dto(totalPrice)"
                                            styleId="totalPrice_name"
                                            styleClass="numberText ${app2:getFormInputClasses()}"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            readonly="true"
                                            view="true"
                                            tabindex="14"/>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.versionNumber"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <app:text property="dto(versionNumber)"
                              styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="20"
                              styleId="field_versionNumber"
                              view="${'delete' == op}"
                              tabindex="11"/>
                </div>
            </div>

            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.deliveryDate"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <div class="input-group date">
                        <app:dateText property="dto(deliveryDate)"
                                      styleId="startDate"
                                      mode="bootstrap"
                                      calendarPicker="${'delete' != op}"
                                      datePatternKey="${datePattern}"
                                      styleClass="text ${app2:getFormInputClasses()}"
                                      maxlength="10"
                                      currentDate="${'create' == op}"
                                      view="${'delete' == op}"
                                      tabindex="12"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.vat"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <fanta:select property="dto(vatId)"
                                  listName="vatList"
                                  labelProperty="name"
                                  valueProperty="id"
                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                  module="/catalogs"
                                  firstEmpty="true"
                                  readOnly="${'delete' == op}"
                                  tabIndex="13">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </div>
            </div>

            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="SalePosition.active"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(active)"
                                           value="true"
                                           disabled="${op == 'delete'}"
                                           styleClass="radios"
                                           styleId="active_id"
                                           tabindex="14"/>
                            <label for="active_id"></label>
                        </div>
                    </div>

                </div>
            </div>

        </div>


        <c:if test="${showSaleLink == true}">
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="SalePosition.sale"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(saleId)"/>
                        <html:hidden property="dto(saleName)"/>

                        <c:out value="${salePositionForm.dtoMap['saleName']}"/>

                        <c:if test="${app2:hasAccessRight(pageContext.request,'SALE' ,'CREATE' )}">
                            <span class="pull-right">
                                <app:link contextRelative="true" titleKey="Common.edit"
                                          action="sales/Sale/Forward/Update.do?saleId=${salePositionForm.dtoMap['saleId']}&dto(saleId)=${salePositionForm.dtoMap['saleId']}&dto(title)=${app2:encode(salePosition.dtoMap['saleName'])}&index=0">
                                    <span class="glyphicon glyphicon-edit "></span>
                                </app:link>
                            </span>
                        </c:if>
                    </div>

                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="SalePosition.payMethod"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || false == salePositionForm.dtoMap['canChangePayMethod'])}">
                        <c:set var="payMethodList" value="${app2:getSalePositionPayMethods(pageContext.request)}"/>
                        <html:select property="dto(payMethod)"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     styleId="payMethodId"
                                     readonly="${'delete' == op || false == salePositionForm.dtoMap['canChangePayMethod']}"
                                     tabindex="14">
                            <html:option value=""/>
                            <html:options collection="payMethodList"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
        </c:if>


        <div class="${app2:getFormGroupClasses()}">
            <label class="control-label col-xs-12 label-left" for="">
                <fmt:message key="SalePosition.text"/>
            </label>

            <div class="col-xs-12">

                <html:textarea property="dto(text)"
                               styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                               style="height:120px;width:99%;"
                               readonly="${'delete'== op}" tabindex="15"/>
            </div>
        </div>
        <c:if test="${empty salePositionSubCategoriesAjaxAction}">
            <c:set var="salePositionSubCategoriesAjaxAction" value="/products/SalePosition/MainPageReadSubCategories.do"
                   scope="request"/>
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
        <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
        <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}" functionality="SALEPOSITION"
                             onclick="javascript:fillMultipleSelectValues();"
                             styleClass="button ${app2:getFormButtonClasses()}" property="save"
                             tabindex="${lastTabIndex+1}">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="${lastTabIndex+2}">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>

</html:form>
<c:if test="${singleWithoutContract != salePositionForm.dtoMap['copyPayMethod']}">
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
        <c:if test="${'update' == op}">
            <div class="embed-responsive embed-responsive-16by9 col-xs-12">
                <iframe name="frame1"
                        src="<app:url value="ProductContractBySalePosition/List.do?salePositionId=${salePositionForm.dtoMap['salePositionId']}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&customerName=${app2:encode(salePositionForm.dtoMap['customerName'])}"/>"
                        class="embed-responsive-item" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </div>
        </c:if>
    </app2:checkAccessRight>
</c:if>

