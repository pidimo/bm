<%@ include file="/Includes.jsp" %>


<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr>
        <td colspan="2" align="center">
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
                <html:form action="/ProjectUser/List.do" focus="parameter(lastName@_firstName@_searchName)">
                    <TR>
                        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
                        <td align="left" class="contain" width="85%">
                            <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText"
                                       maxlength="40"/>
                            &nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                        </td>
                    </TR>
                </html:form>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="ProjectUser/List.do" parameterName="lastName"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <app2:checkAccessRight functionality="PROJECTUSER" permission="CREATE">
            <td colspan="2" class="button" nowrap>
                <br/>
                <br/>
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <html:form action="/ProjectUser/Forward/CreateUser.do">
                            <td>
                                <html:submit styleClass="button">
                                    <fmt:message key="ProjectAssignee.newUser"/>
                                </html:submit>&nbsp;
                            </td>
                        </html:form>
                        <html:form action="/ProjectUser/Forward/CreatePerson.do?createPerson=true">
                            <td>
                                <html:submit styleClass="button">
                                    <fmt:message key="ProjectAssignee.newPerson"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </td>
        </app2:checkAccessRight>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <fanta:table list="projectUserList"
                         action="ProjectUser/List.do"
                         width="100%"
                         id="projectUser"
                         imgPath="${baselayout}">

                <c:set var="editLink"
                       value="ProjectUser/Forward/Update.do?dto(projectId)=${projectUser.projectId}&dto(addressId)=${projectUser.addressId}&dto(userName)=${app2:encode(projectUser.userName)}"/>

                <c:set var="deleteLink"
                       value="ProjectUser/Forward/Delete.do?dto(projectId)=${projectUser.projectId}&dto(addressId)=${projectUser.addressId}&dto(userName)=${app2:encode(projectUser.userName)}&dto(withReferences)=true"/>

                <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="PROJECTUSER" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"
                                            width="50%"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PROJECTUSER" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"
                                            width="50%"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="userName"
                                  action="${editLink}"
                                  styleClass="listItem"
                                  title="ProjectAssignee.userName"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="25"
                                  width="45%"/>

                <fanta:dataColumn name="permission"
                                  styleClass="listItem2"
                                  title="ProjectAssignee.permission"
                                  headerStyle="listHeader"
                                  orderable="false"
                                  maxLength="25"
                                  renderData="false"
                                  width="50%">
                    <c:set var="permissionsEnabled"
                           value="${app2:getProjectUserPermissions(pageContext.request, projectUser.permission)}"/>
                    <c:out value="${permissionsEnabled}"/>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    <tr>
        <app2:checkAccessRight functionality="PROJECTUSER" permission="CREATE">
            <td colspan="2" class="button" nowrap>
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <html:form action="/ProjectUser/Forward/CreateUser.do">
                            <td>
                                <html:submit styleClass="button">
                                    <fmt:message key="ProjectAssignee.newUser"/>
                                </html:submit>&nbsp;
                            </td>
                        </html:form>
                        <html:form action="/ProjectUser/Forward/CreatePerson.do?createPerson=true">
                            <td>
                                <html:submit styleClass="button">
                                    <fmt:message key="ProjectAssignee.newPerson"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </td>
        </app2:checkAccessRight>
    </tr>
</table>