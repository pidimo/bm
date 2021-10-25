<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <html:form action="/Report/UserGroupReportList/Execute.do" styleId="userGroupForm">


        <c:set var="reportFormats" value="${userGroupReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userGroupReportForm.pageSizes}" scope="request"/>

        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%" titleKey="Admin.Report.UserGroupList" generateGrouping="false">
                    <titus:reportGroupSortColumnTag name="groupName" labelKey="UserGroup.name" />
                </titus:reportGroupSortTag>
            </td>
        </tr>

        <tags:reportOptionsTag/>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="userGroupReportForm" title="Admin.Report.UserGroupList"
                         locale="${sessionScope.user.valueMap['locale']}"/>
        <titus:reportFieldTag name="groupName" resourceKey="UserGroup.name" type="${FIELD_TYPE_STRING}"
                              width="100" fieldPosition="1"/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('userGroupForm')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>

    </html:form>
</table>