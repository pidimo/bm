<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">

    <c:if test="${sessionScope.user.valueMap['isDefaultCompany']}">
        <app2:checkAccessRight functionality="COMPANY" permission="VIEW">
            <li>
                <app:link page="/admin/Company/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                    <fmt:message key="Admin.Company.Title.plural"/>
                </app:link>
            </li>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="APPLICATIONSIGNATURE" permission="VIEW">
            <li>
                <app:link page="/admin/ApplicationSignature/Forward/Update.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                    <fmt:message key="ApplicationSignature.title.shortcut"/>
                </app:link>
            </li>
        </app2:checkAccessRight>
    </c:if>

    <app2:checkAccessRight functionality="USER" permission="VIEW">
        <li>
            <app:link page="/admin/User/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Admin.User.Title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="USERGROUP" permission="VIEW">
        <li>
            <app:link page="/admin/User/UserGroupList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Admin.User.UserGroup"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="ROLE" permission="VIEW">
        <li>
            <app:link page="/admin/Role/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Admin.Role.Title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="USERSESSION" permission="VIEW">
        <li>
            <app:link page="/admin/User/UserSessionList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="User.userSession"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="VIEW">
        <li>
            <app:link page="/admin/PasswordChange/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="PasswordChange.shortCut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <c:if test="${sessionScope.user.valueMap['isDefaultCompany']}">
        <app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
            <li>
                <app:link page="/admin/Report/Jrxml/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                    <fmt:message key="Report.jrxml.external"/>
                </app:link>
            </li>
        </app2:checkAccessRight>
    </c:if>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <c:if test="${sessionScope.user.valueMap['isDefaultCompany']}">
            <tags:bootstrapMenuItem action="/admin/Report/CompanyList.do" contextRelative="true" titleKey="Admin.Report.CompanyList"
                                   functionality="COMPANY" permission="VIEW"/>
        </c:if>
        <tags:bootstrapMenuItem action="/admin/Report/UserList.do" contextRelative="true" titleKey="Admin.Report.UserList"
                               functionality="USER" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/admin/Report/UserByGroupList.do" contextRelative="true" titleKey="Admin.Report.UserByGroupList"
                               functionality="USER" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/admin/Report/UserByRoleList.do" contextRelative="true" titleKey="Admin.Report.UserByRolesList"
                               functionality="USER" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/admin/Report/RoleReportList.do" contextRelative="true" titleKey="Admin.Report.rolesList"
                               functionality="ROLE" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/admin/Report/UserGroupReportList.do" contextRelative="true" titleKey="Admin.Report.UserGroupList"
                               functionality="USERGROUP" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/admin/Report/UserSessionList.do" contextRelative="true" titleKey="Admin.Report.UserSessionList"
                               functionality="USERSESSION" permission="VIEW"/>
        <tags:bootstrapReportsMenu module="admin"/>
    </tags:bootstrapMenu>
</ul>
