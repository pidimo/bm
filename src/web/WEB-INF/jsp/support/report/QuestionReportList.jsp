<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<%
    pageContext.setAttribute("publishedList", JSPHelper.getPublishedQuestionList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endRange").val($("#startRange").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="/Report/QuestionList/Execute.do" focus="parameter(createUserName)" styleId="supportStyle"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Support.Report.QuestionList"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldViewUserName_id">
                        <fmt:message key="Question.askedBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(createUserName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText" maxlength="40"
                                      tabindex="1"
                                      styleId="fieldViewUserName_id" readonly="true"/>
                            <html:hidden property="parameter(createUserId)" styleId="fieldViewUserId_id"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="searchUser_id"
                                                           url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                                                           name="searchUser"
                                                           tabindex="1"
                                                           titleKey="Scheduler.grantAccess.searchUser"
                                                           isLargeModal="true"/>

                                <tags:clearBootstrapSelectPopup keyFieldId="fieldViewUserId_id" tabindex="1"
                                                                nameFieldId="fieldViewUserName_id"
                                                                titleKey="Common.clear"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange">
                        <fmt:message key="Question.AskedOn"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startRange)" maxlength="10" tabindex="2"
                                                  styleId="startRange"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endRange)" maxlength="10" tabindex="3"
                                                  styleId="endRange"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                        <fmt:message key="Article.productName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(productName)" styleId="field_name"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      readonly="true" tabindex="4"/>
                            <html:hidden property="parameter(productId)" styleId="field_key"/>
                            <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                            <html:hidden property="parameter(2)" styleId="field_unitId"/>
                            <html:hidden property="parameter(3)" styleId="field_price"/>
                            <div class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="SearchProduct_id" url="/products/SearchProduct.do"
                                                           name="SearchProduct" tabindex="4"
                                                           titleKey="Common.search" modalTitleKey="Product.Title.SimpleSearch" isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="field_key" tabindex="4" nameFieldId="field_name"
                                                                titleKey="Common.clear"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="published_id">
                        <fmt:message key="Article.published"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <html:select property="parameter(published)" styleId="published_id" styleClass="${app2:getFormSelectClasses()} select"
                                     tabindex="5">
                            <html:option value=""/>
                            <html:options collection="publishedList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="categoryId_id">
                        <fmt:message key="Article.category"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(categoryId)" styleId="categoryId_id" listName="articleCategoryList" firstEmpty="true"
                                      labelProperty="name" valueProperty="id" module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      tabIndex="6">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                        separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <c:set var="reportFormats" value="${questionReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${questionReportListForm.pageSizes}" scope="request"/>
        </fieldset>
        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="summary" labelKey="Question.summary"/>
            <titus:reportGroupSortColumnTag name="categoryName" labelKey="Article.categoryName"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Article.productName"/>
            <titus:reportGroupSortColumnTag name="ownerName" labelKey="Question.askedBy"/>
            <titus:reportGroupSortColumnTag name="creationDate" labelKey="Question.AskedOn" isDate="true"/>
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
    <titus:reportTag id="questionReportList" title="Support.Report.QuestionList"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="summary" resourceKey="Question.summary" type="${FIELD_TYPE_STRING}" width="25"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="categoryName" resourceKey="Article.categoryName" type="${FIELD_TYPE_STRING}" width="15"
                          fieldPosition="3"/>
    <titus:reportFieldTag name="productName" resourceKey="Article.productName" type="${FIELD_TYPE_STRING}" width="15"
                          fieldPosition="4"/>
    <titus:reportFieldTag name="ownerName" resourceKey="Question.askedBy" type="${FIELD_TYPE_STRING}" width="25"
                          fieldPosition="5"/>
    <titus:reportFieldTag name="creationDate" resourceKey="Question.AskedOn" type="${FIELD_TYPE_DATELONG}" width="20"
                          patternKey="dateTimePattern" fieldPosition="6"/>

</html:form>

<tags:jQueryValidation formName="questionReportListForm"/>
