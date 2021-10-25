<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top" align="left" width="30%" class="moduleShortCut">

            <app2:checkAccessRight functionality="PROJECT" permission="VIEW">
                &nbsp;
                |&nbsp;
                <app:link page="/Project/List.do" addModuleParams="false" addModuleName="false">
                    <fmt:message key="Project.shortcut.list"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PROJECTTIME" permission="VIEW">
                |&nbsp;
                <app:link page="/ProjectTimeRegistration/List.do" addModuleParams="false" addModuleName="false">
                    <fmt:message key="ProjectTimeRegistration.shortcut.list"/>
                </app:link>
            </app2:checkAccessRight>
            &nbsp;|
        </td>
        <%--        for reports--%>
        <td align="right" class="moduleShortCut" width="10%" nowrap="nowrap">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:pullDownMenuItem action="/Project/Forward/ProjectReportList.do"
                                       titleKey="Project.Report.ProjectList"
                                       functionality="PROJECT" permission="VIEW"/>
                <tags:reportsMenu module="project"/>
                <tags:pullDownMenuItem action="/Project/Forward/ProjectTimesReportList.do"
                                       titleKey="Project.Report.ProjectTimesList"
                                       functionality="PROJECTTIME" permission="VIEW"/>
                <tags:reportsMenu module="project"/>
                <%--<tags:pullDownMenuItem action="/Project/Forward/ProjectTimesAdvancedSearch.do"
                                       titleKey="Project.Report.ProjectTimesAdvancedSearch"
                                       functionality="PROJECTTIME" permission="VIEW"/>
                <tags:reportsMenu module="project"/>--%>
            </tags:pullDownMenu>
        </td>
    </tr>
</table>
