<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initBootstrapSelectPopupEven fields="user_key, user_name"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#openDate2").val($("#openDate1").val());
        $("#expireDate2").val($("#expireDate1").val());
        $("#closeDate2").val($("#closeDate1").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message key="datePattern" var="datePattern"/>
<html:form action="/Report/CaseList/Execute.do" focus="parameter(openUserName)" styleId="supportStyle"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Support.Report.CaseList"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="user_name">
                        <fmt:message key="Common.openBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(openUserName)" tabindex="1"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      styleId="user_name" readonly="true"/>
                            <html:hidden property="parameter(openByUserId)" styleId="user_key"/>
                            <div class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="searchUser_id_open"
                                                           url="/scheduler/ImportUserList.do?other=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                                                           name="searchUser"
                                                           tabindex="1"
                                                           titleKey="Scheduler.grantAccess.searchUser"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="user_key"
                                                                tabindex="1"
                                                                nameFieldId="user_name"
                                                                titleKey="Common.clear"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="openDate1">
                        <fmt:message key="Common.openDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(openDate1)" maxlength="10" tabindex="2"
                                                  styleId="openDate1"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(openDate2)" maxlength="10" tabindex="3"
                                                  styleId="openDate2"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldViewUserName_id">
                        <fmt:message key="Common.assignedTo"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(fromUserName)" tabindex="4"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      styleId="fieldViewUserName_id" readonly="true"/>
                            <html:hidden property="parameter(toUserId)" styleId="fieldViewUserId_id"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="searchUser_id" tabindex="4"
                                                           url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                                                           name="searchUser"
                                                           titleKey="Scheduler.grantAccess.searchUser"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="fieldViewUserId_id" tabindex="4"
                                                                nameFieldId="fieldViewUserName_id"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="expireDate1">
                        <fmt:message key="Common.expireDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>

                                    <app:dateText property="parameter(expireDate1)" maxlength="10" tabindex="5"
                                                  styleId="expireDate1"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>

                                    <app:dateText property="parameter(expireDate2)" maxlength="10" tabindex="6"
                                                  styleId="expireDate2"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  convert="true" mode="bootstrap"
                                                  placeHolder="${to}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Contact.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(contactName)" styleId="fieldAddressName_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      tabindex="7" readonly="true"/>
                            <html:hidden property="parameter(contactAddressId)" styleId="fieldAddressId_id"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="searchAddress_id" tabindex="7"
                                                           url="/contacts/SearchAddress.do" name="searchAddress"
                                                           titleKey="Common.search" modalTitleKey="Contact.Title.search"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" tabindex="7"
                                                                nameFieldId="fieldAddressName_id"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="closeDate1">
                        <fmt:message key="Common.closeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(closeDate1)" maxlength="10" tabindex="8"
                                                  styleId="closeDate1"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(closeDate2)" maxlength="10" tabindex="9"
                                                  styleId="closeDate2"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                        <fmt:message key="Product.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(productName)" styleId="field_name"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      readonly="true" tabindex="10"/>
                            <html:hidden property="parameter(productId)" styleId="field_key"/>
                            <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                            <html:hidden property="parameter(2)" styleId="field_unitId"/>
                            <html:hidden property="parameter(3)" styleId="field_price"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="SearchProduct_id" url="/products/SearchProduct.do"
                                                           name="SearchProduct" tabindex="10"
                                                           titleKey="Common.search"
                                                           modalTitleKey="Product.Title.SimpleSearch"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="field_key" tabindex="10"
                                                                nameFieldId="field_name"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="stateId_id">
                        <fmt:message key="State.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(stateId)" styleId="stateId_id" listName="stateBaseList"
                                      labelProperty="name"
                                      tabIndex="11"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="1"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                        <fmt:message key="Priority.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(priorityId)" styleId="priorityId_id"
                                      listName="selectPriorityList"
                                      labelProperty="name"
                                      tabIndex="12"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="SUPPORT"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="workLevelId_id">
                        <fmt:message key="WorkLevel.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(workLevelId)" styleId="workLevelId_id"
                                      listName="workLevelList" labelProperty="name"
                                      tabIndex="13"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="caseTypeId_id">
                        <fmt:message key="CaseType.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(caseTypeId)" styleId="caseTypeId_id" listName="caseTypeList"
                                      labelProperty="name"
                                      tabIndex="14"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap.companyId}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="severityId_id">
                        <fmt:message key="CaseSeverity.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(severityId)" styleId="severityId_id"
                                      listName="caseSeverityList" labelProperty="name"
                                      tabIndex="15"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalHours1_id">
                        <fmt:message key="Common.totalHours"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label" for="totalHours1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:text property="parameter(totalHours1)" styleId="totalHours1_id"
                                          styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                          maxlength="40"
                                          tabindex="16"/>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:text property="parameter(totalHours2)" styleId="totalHours2"
                                          styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                          maxlength="40"
                                          tabindex="17"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <c:set var="reportFormats" value="${caseReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${caseReportListForm.pageSizes}" scope="request"/>
        </fieldset>
        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="number" labelKey="Article.number"/>
            <titus:reportGroupSortColumnTag name="caseTitle" labelKey="Common.title"/>
            <titus:reportGroupSortColumnTag name="typeName" labelKey="CaseType.title"/>
            <titus:reportGroupSortColumnTag name="priorityName" labelKey="Priority.title"/>
            <titus:reportGroupSortColumnTag name="severityName" labelKey="CaseSeverity.title"/>
            <titus:reportGroupSortColumnTag name="stateName" labelKey="State.title"/>
            <titus:reportGroupSortColumnTag name="openByUser" labelKey="Common.openBy"/>
            <titus:reportGroupSortColumnTag name="toUser" labelKey="Common.assignedTo"/>
            <titus:reportGroupSortColumnTag name="contact" labelKey="Contact.title"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Product.title"/>
            <titus:reportGroupSortColumnTag name="openDate" labelKey="Common.openDate" isDate="true"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="formReset('supportStyle')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="caseReportList" title="Support.Report.CaseList"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="number" resourceKey="Article.number" type="${FIELD_TYPE_STRING}" width="5"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="caseTitle" resourceKey="Common.title" type="${FIELD_TYPE_STRING}" width="11"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="typeName" resourceKey="CaseType.title" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="3"/>
    <titus:reportFieldTag name="priorityName" resourceKey="Priority.title" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="4"/>
    <titus:reportFieldTag name="severityName" resourceKey="CaseSeverity.title" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="5"/>
    <titus:reportFieldTag name="stateName" resourceKey="State.title" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="6"/>
    <titus:reportFieldTag name="openByUser" resourceKey="Common.openBy" type="${FIELD_TYPE_STRING}" width="12"
                          fieldPosition="7"/>
    <titus:reportFieldTag name="toUser" resourceKey="Common.assignedTo" type="${FIELD_TYPE_STRING}" width="12"
                          fieldPosition="8"/>
    <titus:reportFieldTag name="contact" resourceKey="Contact.title" type="${FIELD_TYPE_STRING}" width="12"
                          fieldPosition="9"/>
    <titus:reportFieldTag name="productName" resourceKey="Product.title" type="${FIELD_TYPE_STRING}" width="9"
                          fieldPosition="10"/>
    <titus:reportFieldTag name="openDate" resourceKey="Common.openDate" type="${FIELD_TYPE_DATEINT}" width="7"
                          patternKey="datePattern"
                          fieldPosition="11"/>
</html:form>

<tags:jQueryValidation formName="caseReportListForm"/>