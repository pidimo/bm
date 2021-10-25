<%@ include file="/Includes.jsp" %>


<table width="75%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <html:form action="/GrantAccess/List.do" focus="parameter(lastName@_firstName@_searchName)">
        <tr>
            <td height="20" class="title" colspan="2"><fmt:message key="Scheduler.grantAccess.plural"/></td>
        </tr>
        <TR>
            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText" maxlength="40"/>
                &nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </TR>
    </html:form>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="GrantAccess/List.do" parameterName="lastNameAlpha"/>
        </td>
    </tr>

    <tr>
        <html:form action="/GrantAccess/Forward/Create.do">
            <td colspan="2" class="button">
                <br>
                <app2:checkAccessRight functionality="GRANTACCESS" permission="CREATE">
                    <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                </app2:checkAccessRight>
            </td>
        </html:form>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <fanta:table list="grantAccessList" width="100%" id="viewUser" action="GrantAccess/List.do"
                         imgPath="${baselayout}">
                <c:set var="editAction"
                       value="GrantAccess/Forward/Update.do?dto(viewUserId)=${viewUser.myViewerUserId}&dto(viewUserName)=${app2:encode(viewUser.userName)}"/>
                <c:set var="deleteAction"
                       value="GrantAccess/Forward/Delete.do?dto(viewUserId)=${viewUser.myViewerUserId}&dto(viewUserName)=${app2:encode(viewUser.userName)}"/>
                <c:set var="actualPermission" value="${viewUser.permission}" scope="request"/>
                <c:set var="test" value="${app2:getSchedulerPermissions(pageContext.request, actualPermission)}"/>
                <c:set var="privatePermission" value="${app2:getSchedulerPermissions(pageContext.request, viewUser.privatePermission)}"/>
                <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="GRANTACCESS" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem"
                                            headerStyle="listHeader" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="GRANTACCESS" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                            headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="userName" action="${editAction}" styleClass="listItem"
                                  title="Scheduler.grantAccess.userName" headerStyle="listHeader" width="29%"
                                  orderable="true" maxLength="25"/>
                <fanta:dataColumn name="permission" styleClass="listItem" title="Scheduler.grantAccess.public.permissions"
                                  headerStyle="listHeader" width="33%" orderable="false" maxLength="25"
                                  renderData="false">
                    ${test}
                </fanta:dataColumn>
                <fanta:dataColumn name="privatePermission" styleClass="listItem2" title="Scheduler.grantAccess.private.permissions"
                                  headerStyle="listHeader" width="33%" orderable="false" maxLength="25"
                                  renderData="false">
                    ${privatePermission}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>

</table>