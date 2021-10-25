<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top" align="left" width="30%" class="moduleShortCut">

            <c:if test="${sessionScope.user.valueMap['isDefaultCompany']}">
                <app2:checkAccessRight functionality="COMPANY" permission="VIEW">
                    &nbsp;|&nbsp;
                    <app:link page="/Company/List.do" addModuleParams="false">
                        <fmt:message key="Admin.Company.Title.plural"/>
                    </app:link>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="APPLICATIONSIGNATURE" permission="VIEW">
                    &nbsp;|&nbsp;
                    <app:link page="/ApplicationSignature/Forward/Update.do" addModuleParams="false">
                        <fmt:message key="ApplicationSignature.title.shortcut"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>
            <app2:checkAccessRight functionality="USER" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/User/List.do" addModuleParams="false"><fmt:message key="Admin.User.Title"/></app:link>

            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="USERGROUP" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/User/UserGroupList.do" addModuleParams="false"><fmt:message
                        key="Admin.User.UserGroup"/></app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="ROLE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Role/List.do" addModuleParams="false"><fmt:message key="Admin.Role.Title"/></app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="USERSESSION" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/User/UserSessionList.do" addModuleParams="false"><fmt:message key="User.userSession"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/PasswordChange/List.do" addModuleParams="false">
                    <fmt:message key="PasswordChange.shortCut.title"/>
                </app:link>
            </app2:checkAccessRight>

            <c:if test="${sessionScope.user.valueMap['isDefaultCompany']}">
                <app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
                    &nbsp;|&nbsp;
                    <app:link page="/Report/Jrxml/List.do" addModuleParams="false">
                        <fmt:message key="Report.jrxml.external"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>

            &nbsp;|
        </td>
        <td valign="middle" align="right" class="moduleShortCut" width="5%" nowrap="nowrap">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <c:if test="${sessionScope.user.valueMap['isDefaultCompany']}">
                    <tags:pullDownMenuItem action="/Report/CompanyList.do" titleKey="Admin.Report.CompanyList"
                                           functionality="COMPANY" permission="VIEW"/>
                </c:if>
                <tags:pullDownMenuItem action="/Report/UserList.do" titleKey="Admin.Report.UserList"
                                       functionality="USER" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/UserByGroupList.do" titleKey="Admin.Report.UserByGroupList"
                                       functionality="USER" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/UserByRoleList.do" titleKey="Admin.Report.UserByRolesList"
                                       functionality="USER" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/RoleReportList.do" titleKey="Admin.Report.rolesList"
                                       functionality="ROLE" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/UserGroupReportList.do" titleKey="Admin.Report.UserGroupList"
                                       functionality="USERGROUP" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/UserSessionList.do" titleKey="Admin.Report.UserSessionList"
                                       functionality="USERSESSION" permission="VIEW"/>
                <tags:reportsMenu module="admin"/>
            </tags:pullDownMenu>
        </td>
    </tr>
</table>