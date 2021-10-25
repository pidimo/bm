<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">


    <html:form action="/Report/RoleReportList/Execute.do" styleId="roleForm">

        <c:set var="reportFormats" value="${roleReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${roleReportForm.pageSizes}" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%" titleKey="Admin.Report.rolesList">
                    <titus:reportGroupSortColumnTag name="roleName" labelKey="Role.name" />
                    <titus:reportGroupSortColumnTag name="roleDescription" labelKey="Role.description"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <tags:reportOptionsTag/>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="roleReportList" title="Admin.Report.rolesList"
                         locale="${sessionScope.user.valueMap['locale']}"/>

        <titus:reportFieldTag name="roleName" resourceKey="Role.name" type="${FIELD_TYPE_STRING}" width="40"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="roleDescription" resourceKey="Role.description" type="${FIELD_TYPE_STRING}"
                              width="60" fieldPosition="2"/>


        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('roleForm')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>

    </html:form>
</table>