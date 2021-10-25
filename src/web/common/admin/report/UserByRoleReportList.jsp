<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Admin.Report.UserByRolesList"/></td>
    </tr>

    <html:form action="/Report/UserByRoleList/Execute.do" focus="parameter(roleId)" styleId="userByRoleForm">
        <tr>
            <td class="label" width="20%"><fmt:message key="Admin.Role"/></td>
            <td class="contain" width="30%">

                <fanta:select property="parameter(roleId)" listName="lightRoleList" tabIndex="1"
                              valueProperty="roleId" labelProperty="roleName" firstEmpty="true"
                              styleClass="mediumSelect" module="/admin">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>

            </td>
            <td class="topLabel" width="15%"><fmt:message key="User.typeUser"/></td>
            <td class="contain" width="35%">
                <html:select property="parameter(type)" styleClass="mediumSelect" styleId="type" tabindex="3">
                    <html:option value=""/>
                    <html:options collection="typeUserList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>

        <tr>
            <td class="label"><fmt:message key="Common.active"/></td>
            <td class="contain" colspan="3">
                <html:select property="parameter(active)" value="1" styleClass="mediumSelect" tabindex="2">
                    <html:options collection="activeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>

        <c:set var="reportFormats" value="${userByRoleReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userByRoleReportForm.pageSizes}" scope="request"/>
        <c:set var="tabNumber" value="3" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="roleName" labelKey="Admin.Role" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <tags:reportOptionsTag/>

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


        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" styleClass="button" onclick="formReset('userByRoleForm')" tabindex="58">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>

    </html:form>
</table>