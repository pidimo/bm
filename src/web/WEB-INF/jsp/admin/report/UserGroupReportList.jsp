<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<html:form action="/Report/UserGroupReportList/Execute.do" styleId="userGroupForm" styleClass="form-horizontal">

    <div class="${app2:getFormPanelClasses()}">
        <c:set var="reportFormats" value="${userGroupReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userGroupReportForm.pageSizes}" scope="request"/>

        <titus:reportGroupSortTag tableStyleClass="${app2:getTableClasesIntoForm()}"
                                  mode="bootstrap"
                                  width="100%"
                                  titleKey="Admin.Report.UserGroupList" generateGrouping="false">
            <titus:reportGroupSortColumnTag name="groupName" labelKey="UserGroup.name"/>
        </titus:reportGroupSortTag>
        <tags:bootstrapReportOptionsTag/>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="userGroupReportForm" title="Admin.Report.UserGroupList"
                         locale="${sessionScope.user.valueMap['locale']}"/>
        <titus:reportFieldTag name="groupName" resourceKey="UserGroup.name" type="${FIELD_TYPE_STRING}"
                              width="100" fieldPosition="1"/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}" onclick="formReset('userGroupForm')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>
</html:form>