<%@ include file="/Includes.jsp" %>


<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <html:form focus="parameter(lastName@_firstName@_searchName)"
                   action="/User/Forward/UserGroupImportList.do?userGroupId=${param.userGroupId}&parameter(userGroupId)=${param.userGroupId}">
            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="100%">
                <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText" maxlength="40"/>
                &nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </html:form>
    </tr>

    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet
                    action="User/Forward/UserGroupImportList.do?userGroupId=${param.userGroupId}&parameter(userGroupId)=${param.userGroupId}"
                    parameterName="lastNameAlpha"/>
        </td>
    </tr>
    <app2:checkAccessRight functionality="USERGROUP" permission="UPDATE">
        <tr>
            <c:set var="groupName"><%=request.getAttribute("groupName")%>
            </c:set>
            <html:form action="User/UserGroupImportList/SearchUser.do?dto(groupName)=${app2:encode(groupName)}">
                <td class="button" colspan="2">
                    <html:submit styleClass="button">
                        <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
    </app2:checkAccessRight>

    <tr>
        <td colspan="2">
            <fanta:table list="userByGroupList" width="100%" id="userImport"
                         action="User/Forward/UserGroupImportList.do?userGroupId=${param.userGroupId}"
                         imgPath="${baselayout}" align="center">
                <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                    <c:set var="delete"
                           value="/UserOfGroup/Forward/Delete.do?dto(withReferences)=true&userName=${app2:encode(userImport.userName)}&dto(userGroupId)=${userImport.userGroupId}&dto(name)=${app2:encode(userImport.userName)}&userId=${userImport.userId}&dto(userId)=${userImport.userId}"/>
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                        <app2:checkAccessRight functionality="USERGROUP" permission="UPDATE">
                            <app:link page="${delete}" titleKey="Common.delete">
                                <html:img src="${baselayout}/img/delete.gif" altKey="Common.delete" border="0"/>
                            </app:link>
                        </app2:checkAccessRight>
                    </fanta:actionColumn>
                </fanta:columnGroup>
                <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name" headerStyle="listHeader"
                                  width="30%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="login" styleClass="listItem" title="User.userName" headerStyle="listHeader"
                                  width="25%" orderable="true"/>
                <fanta:dataColumn name="type" styleClass="listItem2" title="User.typeUser" renderData="false"
                                  headerStyle="listHeader" width="15%" orderable="true">
                    <c:if test="${userImport.type == '1'}">
                        <fmt:message key="User.intenalUser"/>
                    </c:if>
                    <c:if test="${userImport.type == '0'}">
                        <fmt:message key="User.externalUser"/>
                    </c:if>
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
    <app2:checkAccessRight functionality="USERGROUP" permission="UPDATE">
        <tr>
            <html:form action="User/UserGroupImportList/SearchUser.do?dto(groupName)=${app2:encode(groupName)}">
                <td class="button" colspan="2">
                    <html:submit styleClass="button">
                        <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                    </html:submit>
                </td>
            </html:form>
        </tr>
    </app2:checkAccessRight>
</table>
