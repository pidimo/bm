<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<html:form action="/Report/RoleReportList/Execute.do" styleId="roleForm" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <c:set var="reportFormats" value="${roleReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${roleReportForm.pageSizes}" scope="request"/>

        <titus:reportGroupSortTag width="100%" titleKey="Admin.Report.rolesList" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="roleName" labelKey="Role.name"/>
            <titus:reportGroupSortColumnTag name="roleDescription" labelKey="Role.description"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="roleReportList" title="Admin.Report.rolesList"
                         locale="${sessionScope.user.valueMap['locale']}"/>

        <titus:reportFieldTag name="roleName" resourceKey="Role.name" type="${FIELD_TYPE_STRING}" width="40"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="roleDescription" resourceKey="Role.description" type="${FIELD_TYPE_STRING}"
                              width="60" fieldPosition="2"/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="formReset('roleForm')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>
</html:form>
