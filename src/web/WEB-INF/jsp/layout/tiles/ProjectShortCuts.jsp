<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">

    <app2:checkAccessRight functionality="PROJECT" permission="VIEW">
        <li>
            <app:link page="/projects/Project/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Project.shortcut.list"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PROJECTTIME" permission="VIEW">
        <li>
            <app:link page="/projects/ProjectTimeRegistration/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="ProjectTimeRegistration.shortcut.list"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapMenuItem action="/projects/Project/Forward/ProjectReportList.do"
                                contextRelative="true"
                                titleKey="Project.Report.ProjectList"
                                functionality="PROJECT" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/projects/Project/Forward/ProjectTimesReportList.do"
                                contextRelative="true"
                                titleKey="Project.Report.ProjectTimesList"
                                functionality="PROJECTTIME" permission="VIEW"/>

        <%--<tags:bootstrapMenuItem action="/projects/Project/Forward/ProjectTimesAdvancedSearch.do"
                                contextRelative="true"
                               titleKey="Project.Report.ProjectTimesAdvancedSearch"
                               functionality="PROJECTTIME" permission="VIEW"/>--%>

        <tags:bootstrapReportsMenu module="project" moduleContext="/projects"/>
    </tags:bootstrapMenu>
</ul>
