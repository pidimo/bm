<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<br/>
<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Admin.Report.UserByGroupList"/></td>
    </tr>

    <html:form action="/Report/UserByGroupList/Execute.do" focus="parameter(userGroupId)" styleId="userByGroupForm">
        <tr>
            <td class="label" width="15%"><fmt:message key="Admin.Report.UserGroupList"/></td>
            <td class="contain" width="35%" colspan="3">

                <fanta:select property="parameter(userGroupId)" listName="userGroupList" tabIndex="1"
                              valueProperty="userGroupId" labelProperty="groupName" firstEmpty="true"
                              styleClass="mediumSelect" module="/admin">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>

            </td>
        </tr>
        <c:set var="reportFormats" value="${userByGroupReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userByGroupReportForm.pageSizes}" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="groupName" labelKey="Appointment.groupName" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName"/>
                    <titus:reportGroupSortColumnTag name="login" labelKey="User.login"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <tags:reportOptionsTag />

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


        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('userByGroupForm')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>

    </html:form>
</table>