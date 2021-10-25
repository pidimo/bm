<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<html:form action="/Report/UserByRoleList/Execute.do" focus="parameter(roleId)" styleId="userByRoleForm" styleClass="form-horizontal">

    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Admin.Report.UserByRolesList"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="roleId_id">
                    <fmt:message key="Admin.Role"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <fanta:select property="parameter(roleId)"
                                  styleId="roleId_id"
                                  listName="lightRoleList" tabIndex="1"
                                  valueProperty="roleId" labelProperty="roleName" firstEmpty="true"
                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}" module="/admin">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="type">
                    <fmt:message key="User.typeUser"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <html:select property="parameter(type)"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                 styleId="type" tabindex="3">
                        <html:option value=""/>
                        <html:options collection="typeUserList" property="value" labelProperty="label"/>
                    </html:select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                    <fmt:message key="Common.active"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <html:select property="parameter(active)" value="1"
                                 styleId="active_id"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="2">
                        <html:options collection="activeList" property="value" labelProperty="label"/>
                    </html:select>
                </div>
            </div>
        </div>

        <c:set var="reportFormats" value="${userByRoleReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userByRoleReportForm.pageSizes}" scope="request"/>
        <c:set var="tabNumber" value="3" scope="request"/>
        <titus:reportGroupSortTag width="100%" tableStyleClass="${app2:getTableClasesIntoForm()}" mode="bootstrap">
            <titus:reportGroupSortColumnTag name="roleName" labelKey="Admin.Role" isDefault="true"
                                            defaultOrder="true" isDefaultGrouping="true"/>
            <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName"/>
        </titus:reportGroupSortTag>
        <tags:bootstrapReportOptionsTag/>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="internal"><fmt:message key="User.intenalUser"/></c:set>
        <c:set var="external"><fmt:message key="User.externalUser"/></c:set>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="roleUserReportList" title="Admin.Report.UserByRolesList"
                         locale="${sessionScope.user.valueMap['locale']}"/>
        <titus:reportFieldTag name="roleName" resourceKey="Admin.Role" type="${FIELD_TYPE_STRING}" width="0"
                              fieldPosition="10" isGroupingColumn="true"/>
        <titus:reportFieldTag name="userName" resourceKey="User.userName" type="${FIELD_TYPE_STRING}" width="60"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="20"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="3"/>

        <titus:reportFieldTag name="type" resourceKey="User.typeUser" type="${FIELD_TYPE_STRING}" width="20"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource type [${internal}] [${external}] [1] [0]"
                              fieldPosition="4"/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" styleClass="button ${app2:getFormButtonClasses()}"
                     onclick="formReset('userByRoleForm')" tabindex="58">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

</html:form>