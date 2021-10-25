<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/ContractToInvoiceList/Execute.do" focus="parameter(contact)" styleId="formId"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="SalesProcess.Report.ContractToInvoiceList"/>
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payMethodId">
                            <fmt:message key="Contract.payMethod"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(payMethod)"
                                         styleClass="${app2:getFormSelectClasses()} middleSelect" styleId="payMethodId"
                                         tabindex="3">
                                <html:option value=""/>
                                <html:options collection="payMethodList" property="value" labelProperty="label"/>
                            </html:select>
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
                                    <tags:bootstrapSelectPopup url="/products/SearchProduct.do" name="SearchProduct"
                                                               styleId="SearchProduct_id"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Product.Title.SimpleSearch"
                                                               tabindex="5" isLargeModal="true"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    nameFieldId="field_name"
                                                                    titleKey="Common.clear"
                                                                    tabindex="5" glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractTypeParam_id">
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
                                          styleId="contractTypeParam_id"
                                          listName="contractTypeList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="contractTypeId"
                                          module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="6"
                                          withGroups="true">
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractIncome_id">
                            <fmt:message key="ContractToInvoice.report.calculateTotalAs"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <c:set var="contractIncomeList"
                                   value="${app2:getContractIncomeTypes(pageContext.request)}"/>
                            <html:select property="parameter(contractIncome)" styleId="contractIncome_id"
                                         styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="8">
                                <html:options collection="contractIncomeList" property="value" labelProperty="label"/>
                            </html:select>
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
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ContractToInvoice.report.withoutAlreadyInvoiced"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="parameter(withoutInvoiced)" styleId="allContacts"
                                                   tabindex="10">
                                    </html:checkbox>
                                    <label for="allContacts"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
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
                                          tabIndex="11">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fromDate">
                            <fmt:message key="ContractToInvoice.report.date"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(fromDate)" maxlength="10" tabindex="12"
                                                      styleId="fromDate"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      placeHolder="${from}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"
                                                      mode="bootstrap"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(toDate)" maxlength="10" tabindex="13"
                                                      styleId="toDate"
                                                      placeHolder="${to}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"
                                                      mode="bootstrap"/>
                                    </div>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>

            <c:set var="reportFormats" value="${contractToInvoiceReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${contractToInvoiceReportForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name=" contractNumber" labelKey="ProductContract.contractNumber"/>
                <titus:reportGroupSortColumnTag name="addressName" labelKey="Contract.contact"/>
                <titus:reportGroupSortColumnTag name="customerName" labelKey="Sale.customer"/>
                <titus:reportGroupSortColumnTag name="productName" labelKey="Contract.product"/>
                <titus:reportGroupSortColumnTag name="payStartDate" labelKey="ProductContract.payStartDate"
                                                isDate="true"/>
                <titus:reportGroupSortColumnTag name="contractEndDate"
                                                labelKey="ContractToInvoice.report.contractEndDate" isDate="true"/>
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
        <titus:reportTag id="contractInvoiceReportList" title="SalesProcess.Report.ContractToInvoiceList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="contractNumber" resourceKey="ProductContract.contractNumber"
                              type="${FIELD_TYPE_STRING}" width="10" fieldPosition="1"/>
        <titus:reportFieldTag name="addressName" resourceKey="Contract.contact" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="customerName" resourceKey="Sale.customer" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="productName" resourceKey="Contract.product" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="payStartDate" resourceKey="ProductContract.payStartDate"
                              type="${FIELD_TYPE_DATEINT}" width="10" fieldPosition="5" patternKey="datePattern"/>
        <titus:reportFieldTag name="contractEndDate" resourceKey="ContractToInvoice.report.contractEndDate"
                              type="${FIELD_TYPE_DATEINT}" width="10" fieldPosition="6" patternKey="datePattern"/>
        <titus:reportFieldTag name="contractIncomeVar" resourceKey="ContractToInvoice.report.incomeCost"
                              type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" width="15" fieldPosition="7"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getAsDouble (contractIncomeVar)"
                              align="${FIELD_ALIGN_RIGHT}" patternKey="numberFormat.2DecimalPlaces"/>
        <titus:reportFieldTag name="currencyLabel" resourceKey="ProductContract.currency" type="${FIELD_TYPE_STRING}"
                              width="10" fieldPosition="8"/>
    </html:form>
</div>

<tags:jQueryValidation formName="contractToInvoiceReportForm"/>
