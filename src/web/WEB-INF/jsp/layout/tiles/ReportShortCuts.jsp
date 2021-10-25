<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">
    <app2:checkAccessRight functionality="REPORT" permission="VIEW">
        <li>
            <app:link page="/reports/Report/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Common.search"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
</ul>
