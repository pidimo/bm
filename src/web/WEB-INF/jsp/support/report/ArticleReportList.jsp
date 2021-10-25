<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initBootstrapSelectPopupEven fields="user_key, user_name"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endDate").val($("#startDate").val());
        $("#endChangeRange").val($("#startChangeRange").val());
        $("#endVisitRange").val($("#startVisitRange").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="/Report/ArticleList/Execute.do" focus="parameter(createUserName)" styleId="supportStyle"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Support.Report.ArticleList"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="user_name">
                        <fmt:message key="Article.ownerName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(createUserName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText" maxlength="40"
                                      tabindex="1"
                                      styleId="user_name" readonly="true"/>
                            <html:hidden property="parameter(createUserId)" styleId="user_key"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup
                                        url="/scheduler/ImportUserList.do?other=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                                        styleId="searchUser_id_user"
                                        name="searchUser"
                                        tabindex="1"
                                        titleKey="Scheduler.grantAccess.searchUser"
                                        modalTitleKey="Scheduler.grantAccess.searchUser"
                                        isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="user_key" tabindex="1"
                                                                nameFieldId="user_name"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                        <fmt:message key="Common.creationDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startRange)" maxlength="10" tabindex="2"
                                                  styleId="startDate"
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
                                    <app:dateText property="parameter(endRange)" maxlength="10" tabindex="3"
                                                  styleId="endDate"
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
                        <fmt:message key="Article.changeName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(updateUserName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText" maxlength="40"
                                      tabindex="5"
                                      styleId="fieldViewUserName_id" readonly="true"/>
                            <html:hidden property="parameter(updateUserId)" styleId="fieldViewUserId_id"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="searchUser_id_update"
                                                           url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                                                           name="searchUser"
                                                           tabindex="5"
                                                           titleKey="Scheduler.grantAccess.searchUser"
                                                           modalTitleKey="Scheduler.grantAccess.searchUser"
                                                           isLargeModal="true"/>

                                <tags:clearBootstrapSelectPopup keyFieldId="fieldViewUserId_id"
                                                                nameFieldId="fieldViewUserName_id"
                                                                tabindex="5"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startChangeRange">
                        <fmt:message key="Article.changeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>

                                    <app:dateText property="parameter(startChangeRange)" maxlength="10" tabindex="6"
                                                  styleId="startChangeRange"
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
                                    <app:dateText property="parameter(endChangeRange)" maxlength="10" tabindex="7"
                                                  styleId="endChangeRange"
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
                        <fmt:message key="Article.productName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(productName)" styleId="field_name"
                                      styleClass="${app2:getFormSelectClasses()} mediumText"
                                      maxlength="40"
                                      readonly="true" tabindex="8"/>
                            <html:hidden property="parameter(productId)" styleId="field_key"/>
                            <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                            <html:hidden property="parameter(2)" styleId="field_unitId"/>
                            <html:hidden property="parameter(3)" styleId="field_price"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="SearchProduct_id" url="/products/SearchProduct.do"
                                                           name="SearchProduct" tabindex="8"
                                                           titleKey="Common.search"
                                                           modalTitleKey="Product.Title.SimpleSearch"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="field_key" tabindex="8"
                                                                nameFieldId="field_name"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startVisitRange">
                        <fmt:message key="Article.lastVisit"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startVisitRange)" maxlength="10" tabindex="9"
                                                  styleId="startVisitRange"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endVisitRange)" maxlength="10" tabindex="10"
                                                  styleId="endVisitRange"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="categoryId_id">
                        <fmt:message key="Article.categoryName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(categoryId)" styleId="categoryId_id"
                                      listName="articleCategoryList" firstEmpty="true"
                                      labelProperty="name" valueProperty="id" module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}" tabIndex="11">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                        separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.readyBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <html:text property="parameter(view1)"
                                           styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                           tabindex="13"/>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <html:text property="parameter(view2)" styleId="view2"
                                           styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                           tabindex="14"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <c:set var="reportFormats" value="${articleReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${articleReportListForm.pageSizes}" scope="request"/>
        </fieldset>
        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="number" labelKey="Article.number"/>
            <titus:reportGroupSortColumnTag name="articleTitle" labelKey="Article.title"/>
            <titus:reportGroupSortColumnTag name="categoryName" labelKey="Article.categoryName"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Article.productName"/>
            <titus:reportGroupSortColumnTag name="ownerName" labelKey="Article.ownerName"/>
            <titus:reportGroupSortColumnTag name="creationDate" labelKey="Article.createDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="changeDate" labelKey="Article.changeDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="changeName" labelKey="Article.changeName"/>
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


    <c:set var="progress"><fmt:message key="Task.InProgress"/></c:set>
    <c:set var="noInit"><fmt:message key="Task.notInit"/></c:set>
    <c:set var="concluded"><fmt:message key="Scheduler.Task.Concluded"/></c:set>
    <c:set var="deferred"><fmt:message key="Task.Deferred"/></c:set>
    <c:set var="toCheck"><fmt:message key="Task.ToCheck"/></c:set>
    <fmt:message var="dateTimePattern" key="dateTimePattern"/>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="articleReportList" title="Support.Report.ArticleList"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="number" resourceKey="Article.number" type="${FIELD_TYPE_STRING}" width="7"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="articleTitle" resourceKey="Article.title" type="${FIELD_TYPE_STRING}" width="12"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="categoryName" resourceKey="Article.categoryName" type="${FIELD_TYPE_STRING}"
                          width="9"
                          fieldPosition="3"/>
    <titus:reportFieldTag name="productName" resourceKey="Article.productName" type="${FIELD_TYPE_STRING}"
                          width="14"
                          fieldPosition="4"/>
    <titus:reportFieldTag name="ownerName" resourceKey="Article.ownerName" type="${FIELD_TYPE_STRING}" width="18"
                          fieldPosition="5"/>
    <titus:reportFieldTag name="creationDate" resourceKey="Article.createDate" type="${FIELD_TYPE_DATELONG}"
                          width="10"
                          patternKey="dateTimePattern" fieldPosition="6"/>
    <titus:reportFieldTag name="changeDate" resourceKey="Article.changeDate" type="${FIELD_TYPE_DATELONG}"
                          width="12"
                          patternKey="dateTimePattern" fieldPosition="7"/>
    <titus:reportFieldTag name="changeName" resourceKey="Article.changeName" type="${FIELD_TYPE_STRING}" width="18"
                          fieldPosition="8"/>
</html:form>

<tags:jQueryValidation formName="articleReportListForm"/>
