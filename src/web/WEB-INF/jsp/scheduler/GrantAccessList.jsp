<%@ include file="/Includes.jsp" %>

<html:form action="/GrantAccess/List.do" focus="parameter(lastName@_firstName@_searchName)"
           styleClass="form-horizontal">
    <fieldset>
        <legend class="title">
            <fmt:message key="Scheduler.grantAccess.plural"/>
        </legend>
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="fieldViewSearchNane_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(lastName@_firstName@_searchName)"
                               styleClass="${app2:getFormInputClasses()}"
                               styleId="fieldViewSearchNane_id"
                               maxlength="40"/>
                <span class="input-group-btn">
                    <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message
                            key="Common.go"/></html:submit>
                </span>
                </div>
            </div>
        </div>
    </fieldset>
</html:form>
<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="GrantAccess/List.do" mode="bootstrap" parameterName="lastNameAlpha"/>
</div>

<html:form action="/GrantAccess/Forward/Create.do">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:checkAccessRight functionality="GRANTACCESS" permission="CREATE">
            <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
        </app2:checkAccessRight>
    </div>
</html:form>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="grantAccessList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%" id="viewUser" action="GrantAccess/List.do"
                 imgPath="${baselayout}">
        <c:set var="editAction"
               value="GrantAccess/Forward/Update.do?dto(viewUserId)=${viewUser.myViewerUserId}&dto(viewUserName)=${app2:encode(viewUser.userName)}"/>
        <c:set var="deleteAction"
               value="GrantAccess/Forward/Delete.do?dto(viewUserId)=${viewUser.myViewerUserId}&dto(viewUserName)=${app2:encode(viewUser.userName)}"/>
        <c:set var="actualPermission" value="${viewUser.permission}" scope="request"/>
        <c:set var="test" value="${app2:getSchedulerPermissions(pageContext.request, actualPermission)}"/>
        <c:set var="privatePermission"
               value="${app2:getSchedulerPermissions(pageContext.request, viewUser.privatePermission)}"/>
        <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
            <app2:checkAccessRight functionality="GRANTACCESS" permission="VIEW">
                <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="GRANTACCESS" permission="DELETE">
                <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
        <fanta:dataColumn name="privatePermission" styleClass="listItem2"
                          title="Scheduler.grantAccess.private.permissions"
                          headerStyle="listHeader" width="33%" orderable="false" maxLength="25"
                          renderData="false">
            ${privatePermission}
        </fanta:dataColumn>
    </fanta:table>
</div>
