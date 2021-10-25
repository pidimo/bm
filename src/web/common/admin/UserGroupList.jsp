<%@ include file="/Includes.jsp" %>

<table width="70%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="UserGroup.userGroupList"/>
        </td>
    </tr>
    <tr>
        <html:form action="/User/UserGroupList.do" focus="parameter(groupName)">

            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(groupName)" styleClass="largeText" maxlength="40"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>

        </html:form>
    </tr>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="User/UserGroupList.do" parameterName="groupNameAlpha"/>
        </td>
    </tr>

    <tr>
        <html:form action="/User/Forward/CreateUserGroup.do">
            <td class="button" colspan="2"><br>
                <app2:securitySubmit operation="create" functionality="USERGROUP" styleClass="button">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </td>
        </html:form>
    </tr>

    <tr>
        <td colspan="2">

            <fanta:table list="userGroupList" width="100%" id="userGroup" action="User/UserGroupList.do"
                         imgPath="${baselayout}"
                         align="center">

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="10%">

                    <c:set var="edit"
                           value="User/Forward/UpdateUserGroup.do?groupName=${app2:encode(userGroup.groupName)}&userGroupId=${userGroup.userGroupId}&dto(userGroupId)=${userGroup.userGroupId}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(groupName)=${app2:encode(userGroup.groupName)}"/>

                    <c:set var="delete"
                           value="User/Forward/DeleteUserGroup.do?userGroupId=${userGroup.userGroupId}&groupName=${app2:encode(userGroup.groupName)}&dto(userGroupId)=${userGroup.userGroupId}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(groupName)=${app2:encode(userGroup.groupName)}"/>

                    <c:set var="urlUserList"
                           value="User/Forward/UserGroupImportList.do?parameter(type)=1&index=1&groupName=${app2:encode(userGroup.groupName)}&parameter(userGroupId)=${userGroup.userGroupId}&userGroupId=${userGroup.userGroupId}&dto(groupName)=${app2:encode(userGroup.groupName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>

                    <app2:checkAccessRight functionality="USERGROUP" permission="VIEW">
                        <fanta:actionColumn name="viewContacts" title="Webmail.addressGroup.edit"
                                            action="${urlUserList}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/webmail/contactgroup_edit.gif"/>

                        <fanta:actionColumn name="edit" title="Common.update" action="${edit}" styleClass="listItem"
                                            headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="USERGROUP" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${delete}" styleClass="listItem"
                                            headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                </fanta:columnGroup>
                <fanta:dataColumn name="groupName" styleClass="listItem" action="${edit}"
                                  title="UserGroup.name"
                                  headerStyle="listHeader" width="70%" orderable="true" maxLength="50"/>
                <fanta:dataColumn name="groupType" styleClass="listItem2"
                                  title="UserGroup.groupType"
                                  headerStyle="listHeader" width="25%" orderable="true" maxLength="50" renderData="false">
                    <c:out value="${app2:getUserGroupMessage(userGroup.groupType, pageContext.request)}"/>
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
    <tr>
        <html:form action="/User/Forward/CreateUserGroup.do">
            <td class="button" colspan="2">
                <app2:securitySubmit operation="create" functionality="USERGROUP" styleClass="button">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </td>
        </html:form>
    </tr>

</table>
