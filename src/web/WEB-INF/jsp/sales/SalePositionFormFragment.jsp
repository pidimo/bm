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

<tags:initBootstrapFile/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="dto(productName)" enctype="multipart/form-data" styleClass="form-horizontal">
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

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}" functionality="SALEPOSITION"
                             onclick="javascript:fillMultipleSelectValues();"
                             styleClass="${app2:getFormButtonClasses()}" property="save" tabindex="50">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="51">
            <fmt:message key="Common.cancel"/>
        </html:cancel>

            <%--top links--%>
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
                    <app2:categoryFinderValue forId="salePositionCategoryLinkId"
                                              value="${salePositionForm.dtoMap['salePositionId']}"/>
                </app2:categoryTabLink>
            </app2:checkAccessRight>
        </c:if>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldProductName_id">
                        <fmt:message key="SalePosition.productName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced'])}">
                        <c:set value="${true == salePositionForm.dtoMap['salePositionHasInvoiced']}"
                               var="showModeButtonEdit"/>
                        <div class="${showModeButtonEdit ? 'row col-xs-11' : 'input-group'}">

                            <app:text property="dto(productName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      readonly="true"
                                      styleId="fieldProductName_id"
                                      tabindex="1"
                                      view="${'delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced']}"/>

                            <html:hidden property="dto(productId)" styleId="fieldProductId_id"/>

                            <c:if test="${showModeButtonEdit}">
                        </div>
                        <div>
                            </c:if>
                                <span class="${showModeButtonEdit ? 'pull-right' : 'input-group-btn'}">
                                    <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                                        <c:if test="${'update' == op && not empty salePositionForm.dtoMap['productId']}">
                                            <c:set var="productEditLink"
                                                   value="/products/Product/Forward/Update.do?productId=${salePositionForm.dtoMap['productId']}&dto(productId)=${salePositionForm.dtoMap['productId']}&dto(productName)=${app2:encode(salePositionForm.dtoMap['productName'])}&index=0"/>
                                            <app:link action="${productEditLink}"
                                                      styleClass="${showModeButtonEdit ? '' : app2:getFormButtonClasses()}"
                                                      contextRelative="true" tabindex="2"
                                                      titleKey="Common.edit">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </app:link>
                                        </c:if>
                                    </app2:checkAccessRight>

                                    <tags:bootstrapSelectPopup styleId="SearchProduct_id"
                                                               url="/products/SearchProduct.do"
                                                               name="SearchProduct"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Product.Title.SimpleSearch"
                                                               hide="${'delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced']}"
                                                               submitOnSelect="true"
                                                               tabindex="3"
                                                               isLargeModal="true"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldProductId_id"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    nameFieldId="fieldProductName_id"
                                                                    titleKey="Common.clear"
                                                                    submitOnClear="true"
                                                                    hide="${'delete' == op || true == salePositionForm.dtoMap['salePositionHasInvoiced']}"
                                                                    tabindex="4"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="quantity_id">
                        <fmt:message key="SalePosition.quantity"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || false == salePositionForm.dtoMap['canUpdateQuantityField'])}">
                        <app:numberText property="dto(quantity)"
                                        styleId="quantity_id"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="10"
                                        numberType="decimal"
                                        maxInt="10"
                                        maxFloat="2"
                                        view="${'delete' == op || false == salePositionForm.dtoMap['canUpdateQuantityField']}"
                                        tabindex="5"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_unitId">
                        <fmt:message key="SalePosition.unitName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <fanta:select property="dto(unitId)"
                                      listName="productUnitList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}"
                                      styleId="field_unitId"
                                      tabIndex="6">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <c:if test="${useNetPrice}">
                        <html:hidden property="dto(unitPriceGross)"/>
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="unitPrice_id">
                            <fmt:message key="SalePosition.unitPriceNet"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:numberText property="dto(unitPrice)"
                                            styleId="unitPrice_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="18"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="4"
                                            view="${'delete' == op}"
                                            tabindex="7"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </c:if>
                    <c:if test="${useGrossPrice}">
                        <html:hidden property="dto(unitPrice)"/>
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="unitPriceGross_id">
                            <fmt:message key="SalePosition.unitPriceGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:numberText property="dto(unitPriceGross)"
                                            styleId="unitPriceGross_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="18"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="4"
                                            view="${'delete' == op}"
                                            tabindex="7"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="serial_id">
                        <fmt:message key="SalePosition.serial"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:text property="dto(serial)"
                                  styleId="serial_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="100"
                                  view="${'delete' == op}" tabindex="8"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="discount_name">
                        <fmt:message key="SalePosition.discount"/>
                        (<c:out value="%"/>)
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:numberText property="dto(discount)"
                                        styleId="discount_name"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        numberType="decimal"
                                        maxInt="10"
                                        maxFloat="2"
                                        view="${op == 'delete'}"
                                        tabindex="9"/>

                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_versionNumber">
                        <fmt:message key="SalePosition.versionNumber"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:text property="dto(versionNumber)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20"
                                  styleId="field_versionNumber"
                                  view="${'delete' == op}"
                                  tabindex="10"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${useNetPrice}">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <html:hidden property="dto(totalPriceGross)"/>
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalPrice_name">
                            <fmt:message key="SalePosition.totalPriceNet"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <app:numberText property="dto(totalPrice)"
                                            styleId="totalPrice_name"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            readonly="true"
                                            view="true"
                                            tabindex="11"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${useGrossPrice}">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <html:hidden property="dto(totalPrice)"/>
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalPrice_name">
                            <fmt:message key="SalePosition.totalPriceGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <app:numberText property="dto(totalPriceGross)"
                                            styleId="totalPrice_name"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            readonly="true"
                                            view="true"
                                            tabindex="11"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="vatId_id">
                        <fmt:message key="SalePosition.vat"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(vatId)"
                                      styleId="vatId_id"
                                      listName="vatList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      module="/catalogs"
                                      firstEmpty="true"
                                      readOnly="${'delete' == op}"
                                      tabIndex="12">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                        <fmt:message key="SalePosition.deliveryDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(deliveryDate)"
                                          styleId="startDate"
                                          calendarPicker="${'delete' != op}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          view="${'delete' == op}"
                                          tabindex="13"
                                          mode="bootstrap"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="SalePosition.active"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(active)"
                                           styleId="active_id"
                                           value="true"
                                           disabled="${op == 'delete'}"
                                           tabindex="14"/>
                            <label for="active_id"></label>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="payMethodId">
                        <fmt:message key="SalePosition.payMethod"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || false == canChangePayMethod)}">
                        <c:set var="payMethodList" value="${app2:getSalePositionPayMethods(pageContext.request)}"/>
                        <html:select property="dto(payMethod)"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     styleId="payMethodId"
                                     readonly="${'delete' == op || false == canChangePayMethod}"
                                     tabindex="15">
                            <html:option value=""/>
                            <html:options collection="payMethodList"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-12">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="control-label col-xs-12 col-sm-12 label-left row">
                        <fmt:message key="SalePosition.text"/>
                    </label>

                    <div class="col-xs-12 col-sm-12 row">
                        <html:textarea property="dto(text)"
                                       styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                       style="height:120px;"
                                       readonly="${'delete'== op}" tabindex="16"/>
                    </div>
                </div>
            </div>
            <c:if test="${empty salePositionSubCategoriesAjaxAction}">
                <c:set var="salePositionSubCategoriesAjaxAction"
                       value="/sales/SalePosition/MainPageReadSubCategories.do" scope="request"/>
            </c:if>

            <c:if test="${empty salePositionDownloadAttachCategoryAction}">
                <c:set var="salePositionDownloadAttachCategoryAction"
                       value="/sales/SalePosition/MainPage/DownloadCategoryFieldValue.do?dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
                       scope="request"/>
            </c:if>

            <c:set var="elementCounter" value="${17}" scope="request"/>
            <c:set var="ajaxAction" value="${salePositionSubCategoriesAjaxAction}" scope="request"/>
            <c:set var="downloadAction" value="${salePositionDownloadAttachCategoryAction}"
                   scope="request"/>
            <c:set var="formName" value="salePositionForm" scope="request"/>
            <c:set var="table" value="<%=ContactConstants.SALE_POSITION_CATEGORY%>" scope="request"/>
            <c:set var="operation" value="${op}" scope="request"/>
            <c:set var="labelWidth" value="18" scope="request"/>
            <c:set var="containWidth" value="82" scope="request"/>
            <c:set var="generalWidth" value="${250}" scope="request"/>
            <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
            <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
        </fieldset>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}" functionality="SALEPOSITION"
                             onclick="javascript:fillMultipleSelectValues();"
                             styleClass="${app2:getFormButtonClasses()}" property="save"
                             tabindex="${lastTabIndex+1}">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="${lastTabIndex+2}">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>

</html:form>

<tags:jQueryValidation formName="salePositionForm"/>
