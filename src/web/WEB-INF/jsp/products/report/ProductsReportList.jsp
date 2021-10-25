<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/ProductList/Execute.do" focus="parameter(productTypeId)" styleId="productStyle"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Product.Report.ProductList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productTypeId_id">
                            <fmt:message key="Product.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(productTypeId)" styleId="productTypeId_id"
                                          listName="productTypeList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect" tabIndex="1"
                                          preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Product.priceNet"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(price1)"
                                                    styleClass="numberText form-control numberText numberInputWidth"
                                                    tabindex="2"
                                                    maxlength="12"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(price2)" styleId="price2"
                                                    styleClass="numberText form-control numberText numberInputWidth"
                                                    tabindex="3"
                                                    maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Product.unit"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(unitId)" listName="productUnitList" firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect" tabIndex="4">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Product.priceGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(priceGross1)"
                                                    styleClass="numberText form-control numberText numberInputWidth"
                                                    tabindex="5"
                                                    maxlength="15"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(priceGross2)"
                                                    styleClass="numberText form-control numberText numberInputWidth"
                                                    tabindex="6"
                                                    maxlength="15"
                                                    numberType="decimal" maxInt="10" styleId="priceGross2"
                                                    maxFloat="2"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Product.group"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(productGroupId)" listName="productGroupList"
                                          firstEmpty="true"
                                          labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="7">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                                            separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Product.version"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:text property="parameter(currentVersion)"
                                      styleClass="${app2:getFormInputClasses()} shortText" maxlength="40"
                                      tabindex="8"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="productName" labelKey="Contact.name"/>
                <titus:reportGroupSortColumnTag name="type" labelKey="Product.type"/>
                <titus:reportGroupSortColumnTag name="group" labelKey="Product.group"/>
                <titus:reportGroupSortColumnTag name="unit" labelKey="Product.unit"/>
                <titus:reportGroupSortColumnTag name="price" labelKey="Product.priceNet"/>
                <titus:reportGroupSortColumnTag name="priceGross" labelKey="Product.priceGross"/>
            </titus:reportGroupSortTag>

            <c:set var="reportFormats" value="${productReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${productReportListForm.pageSizes}" scope="request"/>

            <tags:bootstrapReportOptionsTag/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('productStyle')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

        <fmt:message var="eventTypeLabel" key="ProductType.item.event"/>
        <c:set var="eventTypeLabelFixed" value="${fn:replace(eventTypeLabel, '[', '(')}" />
        <c:set var="eventTypeLabelFixed" value="${fn:replace(eventTypeLabelFixed, ']', ')')}"/>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="productReportList" title="Product.Report.ProductList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="productName" resourceKey="Contact.name" type="${FIELD_TYPE_STRING}" width="30"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="type" resourceKey="Product.type" type="${FIELD_TYPE_STRING}" width="18"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getProductTypeNameByCondition type productTypeType [${eventTypeLabelFixed}]"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="group" resourceKey="Product.group" type="${FIELD_TYPE_STRING}" width="18"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="unit" resourceKey="Product.unit" type="${FIELD_TYPE_STRING}" width="14"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="price" resourceKey="Product.priceNet" type="${FIELD_TYPE_DECIMALNUMBER}"
                              patternKey="numberFormat.2DecimalPlaces" width="10" fieldPosition="5"
                              align="${FIELD_ALIGN_RIGHT}"/>
        <titus:reportFieldTag name="priceGross" resourceKey="Product.priceGross" type="${FIELD_TYPE_DECIMALNUMBER}"
                              patternKey="numberFormat.2DecimalPlaces" width="10" fieldPosition="6"
                              align="${FIELD_ALIGN_RIGHT}"/>
    </html:form>
</div>

<tags:jQueryValidation formName="productReportListForm"/>