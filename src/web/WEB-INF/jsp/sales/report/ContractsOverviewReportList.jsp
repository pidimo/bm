<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/ContractsOverviewList/Execute.do" focus="parameter(contact)" styleId="formId"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="SalesProcess.Report.ContractsOverviewList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Contract.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <app:text property="parameter(contact)" styleId="fieldAddressName_id"
                                          styleClass="${app2:getFormInputClasses()} middleText"
                                          maxlength="40"
                                          tabindex="1" readonly="true"/>
                                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                               url="/contacts/SearchAddress.do" name="searchAddress"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Contact.Title.search"
                                                               tabindex="2" isLargeModal="true"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    nameFieldId="fieldAddressName_id"
                                                                    titleKey="Common.clear" tabindex="2"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sellerId_id">
                            <fmt:message key="ProductContract.seller"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(sellerId)"
                                          styleId="sellerId_id"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          firstEmpty="true"
                                          module="/contacts"
                                          tabIndex="3">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                            <fmt:message key="Contract.product"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <app:text property="parameter(productName)" styleId="field_name"
                                          styleClass="${app2:getFormInputClasses()} middleText"
                                          maxlength="40"
                                          readonly="true" tabindex="4"/>
                                <html:hidden property="parameter(productId)" styleId="field_key"/>
                                <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                                <html:hidden property="parameter(2)" styleId="field_unitId"/>
                                <html:hidden property="parameter(3)" styleId="field_price"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup styleId="SearchProduct_id"
                                                               url="/products/SearchProduct.do" name="SearchProduct"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Product.Title.SimpleSearch"
                                                               isLargeModal="true"
                                                               tabindex="5"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key" nameFieldId="field_name"
                                                                    titleKey="Common.clear"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    tabindex="5"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractTypeParamField">
                            <fmt:message key="Contract.contractType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">

                            <fmt:message var="allToBeInvoicedLabel" key="ContractToInvoice.report.allToBeInvoiced"/>
                            <c:set var="allToBeInvoiced" value="<%=SalesConstants.CONTRACTS_TO_BE_INVOICED%>"/>
                            <fmt:message var="allNotToBeInvoicedLabel"
                                         key="ContractToInvoice.report.allNotToBeInvoiced"/>
                            <c:set var="allNotToBeInvoiced" value="<%=SalesConstants.CONTRACTS_NOT_TO_BE_INVOICED%>"/>

                            <fmt:message var="groupToInvoiced" key="ContractToInvoice.report.contractToBeInvoiced"/>
                            <fmt:message var="groupNotInvoiced" key="ContractToInvoice.report.contractNotToBeInvoiced"/>

                            <fanta:select property="parameter(contractTypeParam)"
                                          listName="contractTypeList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="contractTypeId"
                                          module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="6"
                                          withGroups="true" styleId="contractTypeParamField">
                                <fanta:addItem key="${allToBeInvoiced}" value="${allToBeInvoicedLabel}"/>
                                <fanta:addItem key="${allNotToBeInvoiced}" value="${allNotToBeInvoicedLabel}"/>

                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:group groupName="${groupToInvoiced}" columnDiscriminator="tobeInvoiced"
                                             valueDiscriminator="1"/>
                                <fanta:group groupName="${groupNotInvoiced}" columnDiscriminator="tobeInvoiced"
                                             valueDiscriminator="0"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productGroupId_id">
                            <fmt:message key="Product.group"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(productGroupId)" styleId="productGroupId_id"
                                          listName="productGroupList"
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateId">
                            <fmt:message key="ContractsOverview.report.date"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group date">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <app:dateText property="parameter(inputDate)" maxlength="10" tabindex="8"
                                              styleId="dateId"
                                              currentDate="true"
                                              mode="bootstrap"
                                              calendarPicker="true" datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} datepicker dateText"
                                              convert="true"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productTypeId_id">
                            <fmt:message key="Product.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(productTypeId)" styleId="productTypeId_id"
                                          listName="productTypeList"
                                          firstEmpty="true"
                                          labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                                          tabIndex="9">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

            </fieldset>
            <c:set var="reportFormats" value="${contractsOverviewReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${contractsOverviewReportForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="contractNumber"
                                                labelKey="ProductContract.contractNumber"/>
                <titus:reportGroupSortColumnTag name="addressName" labelKey="Contract.contact"/>
                <titus:reportGroupSortColumnTag name="customerName" labelKey="Sale.customer"/>
                <titus:reportGroupSortColumnTag name="productName" labelKey="Contract.product"/>
                <titus:reportGroupSortColumnTag name="orderDate" labelKey="Contract.orderDate" isDate="true"/>
                <titus:reportGroupSortColumnTag name="payStartDate" labelKey="ProductContract.payStartDate"
                                                isDate="true"/>
                <titus:reportGroupSortColumnTag name="contractEndDate"
                                                labelKey="ContractToInvoice.report.contractEndDate"
                                                isDate="true"/>
                <titus:reportGroupSortColumnTag name="invoicedUntil" labelKey="ProductContract.invoiceUntil"
                                                isDate="true"/>
                <titus:reportGroupSortColumnTag name="currencyLabel" labelKey="ProductContract.currency"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('formId')">
                <fmt:message key="Common.clear"/>
            </html:button>

        </div>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="contractsOverviewReportList" title="SalesProcess.Report.ContractsOverviewList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="contractNumber" resourceKey="ProductContract.contractNumber"
                              type="${FIELD_TYPE_STRING}" width="8"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="addressName" resourceKey="Contract.contact" type="${FIELD_TYPE_STRING}"
                              width="15"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="customerName" resourceKey="Sale.customer" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="productName" resourceKey="Contract.product" type="${FIELD_TYPE_STRING}"
                              width="13"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="orderDate" resourceKey="Contract.orderDate" type="${FIELD_TYPE_DATEINT}"
                              width="8"
                              fieldPosition="5" patternKey="datePattern"/>
        <titus:reportFieldTag name="payStartDate" resourceKey="ProductContract.payStartDate"
                              type="${FIELD_TYPE_DATEINT}" width="8"
                              fieldPosition="6" patternKey="datePattern"/>
        <titus:reportFieldTag name="contractEndDate" resourceKey="ContractToInvoice.report.contractEndDate"
                              type="${FIELD_TYPE_DATEINT}" width="8"
                              fieldPosition="7" patternKey="datePattern"/>
        <titus:reportFieldTag name="invoicedUntil" resourceKey="ProductContract.invoiceUntil"
                              type="${FIELD_TYPE_DATEINT}" width="8"
                              fieldPosition="8" patternKey="datePattern"/>
        <titus:reportFieldTag name="price" resourceKey="ContractsOverview.report.incomeCost"
                              type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" width="9"
                              fieldPosition="9" align="${FIELD_ALIGN_RIGHT}"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getAsDouble (overviewIncomeVar)"
                              patternKey="numberFormat.2DecimalPlaces"/>
        <titus:reportFieldTag name="currencyLabel" resourceKey="ProductContract.currency"
                              type="${FIELD_TYPE_STRING}" width="8"
                              fieldPosition="10"/>
    </html:form>
</div>

<tags:jQueryValidation formName="contractsOverviewReportForm"/>
