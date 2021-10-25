<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Report/CustomerList/Execute.do" focus="parameter(countryId)" styleId="customerReport"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Contact.Report.CustomerList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="countryId">
                            <fmt:message key="Contact.country"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(countryId)" styleId="countryId"
                                          listName="countryBasicList"
                                          firstEmpty="true"
                                          labelProperty="name" valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="1">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sourceId_id">
                            <fmt:message key="Customer.source"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(sourceId)"
                                               styleId="sourceId_id"
                                               catalogTable="addresssource"
                                               idColumn="sourceid"
                                               labelColumn="sourcename"
                                               styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="zip_id">
                            <fmt:message key="Contact.cityZip"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4 wrapperButton">
                                    <app:text property="parameter(zip)" styleId="zip_id"
                                              styleClass="${app2:getFormInputClasses()} zipText"
                                              maxlength="10"
                                              titleKey="Contact.zip"
                                              tabindex="3"/>
                                </div>
                                <div class="col-xs-12 col-sm-8 wrapperButton">
                                    <app:text property="parameter(cityName)"
                                              styleClass="${app2:getFormInputClasses()} cityNameText" tabindex="4"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="Customer.priority"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(priorityId)" styleId="priorityId_id"
                                          listName="priorityList"
                                          labelProperty="name" valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs" firstEmpty="true" tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="type" value="CUSTOMER"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Customer.partner"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <html:hidden property="parameter(partnerId)" styleId="fieldAddressId_id"/>
                                <app:text property="parameter(address)" styleId="fieldAddressName_id"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40"
                                          tabindex="6" readonly="true"/>
                                <span class="input-group-btn">
                                   <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                              styleId="searchAddress_id" name="searchAddress"
                                                              titleKey="Contact.Title.search"
                                                              tabindex="7"
                                                              hide="false"
                                                              isLargeModal="true"/>
                                   <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                   styleClass="${app2:getFormButtonClasses()}"
                                                                   nameFieldId="fieldAddressName_id"
                                                                   titleKey="Common.clear"
                                                                   hide="false"
                                                                   tabindex="8"
                                                                   glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payConditionId_id">
                            <fmt:message key="Customer.payCondition"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(payConditionId)" styleId="payConditionId_id"
                                               catalogTable="paycondition"
                                               idColumn="payconditionid"
                                               labelColumn="conditionname"
                                               styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="9"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="Appointment.responsible"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(employeeId)" styleId="employeeId_id"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName" valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/contacts" tabIndex="10" firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payMoralityId_id">
                            <fmt:message key="Customer.payMorality"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(payMoralityId)" styleId="payMoralityId_id"
                                               catalogTable="paymorality"
                                               idColumn="paymoralityid"
                                               labelColumn="paymoralityname"
                                               styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="11"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="customerTypeId_id">
                            <fmt:message key="Customer.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(customerTypeId)" styleId="customerTypeId_id"
                                               catalogTable="customertype"
                                               idColumn="customertypeid"
                                               labelColumn="customertypename"
                                               styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="12"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="row col-xs-12 col-sm-12 col-md-6">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Customer.expectedTurnOver"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6">
                                    <label class="control-label" for="turnOver1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(turnOver1)"
                                                    styleId="turnOver1_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                                    tabindex="13"
                                                    maxlength="12"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <label class="control-label" for="turnOver2_id">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(turnOver2)"
                                                    styleId="turnOver2_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                                    tabindex="14"
                                                    maxlength="12"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="branchId_id">
                            <fmt:message key="Customer.branch"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:catalogSelect property="parameter(branchId)" styleId="branchId_id"
                                               catalogTable="branch" idColumn="branchid"
                                               labelColumn="branchname"
                                               styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="15"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="row col-xs-12 col-sm-12 col-md-6">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Customer.numberOfEmpoyees"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6">
                                    <label class="control-label" for="numberEmployee1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:text property="parameter(numberEmployee1)"
                                              styleId="numberEmployee1_id"
                                              styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                              tabindex="16"
                                              maxlength="15"/>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <label class="control-label" for="numberEmployee2">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:text property="parameter(numberEmployee2)" styleId="numberEmployee2"
                                              styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                              tabindex="17"
                                              maxlength="15"/>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
            <c:set var="reportFormats" value="${customerReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${customerReportListForm.pageSizes}" scope="request"/>
            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="name" labelKey="ProductCustomer.customer"/>
                <titus:reportGroupSortColumnTag name="countryCode" labelKey="Contact.countryCode"/>
                <titus:reportGroupSortColumnTag name="zip" labelKey="Contact.zip"/>
                <titus:reportGroupSortColumnTag name="cityName" labelKey="Contact.city"/>
                <titus:reportGroupSortColumnTag name="addressType" labelKey="Contact.type"/>
                <titus:reportGroupSortColumnTag name="active" labelKey="Common.active"/>
            </titus:reportGroupSortTag>
            <tags:bootstrapReportOptionsTag/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)"
                         tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('customerReport')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
        <c:set var="person"><fmt:message key="Contact.person"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="customerReportList" title="Contact.Report.CustomerList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="name" resourceKey="ProductCustomer.customer" type="${FIELD_TYPE_STRING}"
                              width="30"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="countryCode" resourceKey="Contact.countryCode" type="${FIELD_TYPE_STRING}"
                              width="10"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="zip" resourceKey="Contact.zip" type="${FIELD_TYPE_STRING}" width="10"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="cityName" resourceKey="Contact.city" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="addressType" resourceKey="Contact.type" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="5"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource addressType [${person}] [${organization}] [1] [0]"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="6"/>

    </html:form>
</div>
<tags:jQueryValidation formName="customerReportListForm"/>
