<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<html:form action="/Report/UserByGroupList/Execute.do" focus="parameter(userGroupId)" styleId="userByGroupForm"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Admin.Report.UserByGroupList"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="userGroupId_id">
                        <fmt:message key="Admin.Report.UserGroupList"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(userGroupId)" styleId="userGroupId_id" listName="userGroupList" tabIndex="1"
                                      valueProperty="userGroupId" labelProperty="groupName" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect" module="/admin">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
        </fieldset>
        <c:set var="reportFormats" value="${userByGroupReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userByGroupReportForm.pageSizes}" scope="request"/>

        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="groupName" labelKey="Appointment.groupName" isDefault="true"
                                            defaultOrder="true" isDefaultGrouping="true"/>
            <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName"/>
            <titus:reportGroupSortColumnTag name="login" labelKey="User.login"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>
    </div>
    <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
    <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
    <c:set var="internal"><fmt:message key="User.intenalUser"/></c:set>
    <c:set var="external"><fmt:message key="User.externalUser"/></c:set>

    <titus:reportInitializeConstantsTag/>

    <titus:reportTag id="userByGroupList" title="Admin.Report.UserByGroupList"
                     locale="${sessionScope.user.valueMap['locale']}"/>

    <titus:reportFieldTag name="groupName" resourceKey="Appointment.groupName" type="${FIELD_TYPE_STRING}"
                          width="0" fieldPosition="10" isGroupingColumn="true"/>
    <titus:reportFieldTag name="userName" resourceKey="User.userName" type="${FIELD_TYPE_STRING}" width="50"
                          fieldPosition="2"/>

    <titus:reportFieldTag name="login" resourceKey="User.login" type="${FIELD_TYPE_STRING}" width="25"
                          fieldPosition="3"/>

    <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                          fieldPosition="4"/>

    <titus:reportFieldTag name="type" resourceKey="User.typeUser" type="${FIELD_TYPE_STRING}" width="15"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource type [${internal}] [${external}] [1] [0]"
                          fieldPosition="5"/>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="formReset('userByGroupForm')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

</html:form>
<tags:jQueryValidation formName="userByGroupReportForm"/>