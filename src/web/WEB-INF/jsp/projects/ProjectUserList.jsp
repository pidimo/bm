<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ProjectUser/List.do" focus="parameter(lastName@_firstName@_searchName)"
               styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="lastName@_firstName@_searchName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(lastName@_firstName@_searchName)"
                               styleId="lastName@_firstName@_searchName_id"
                               styleClass="largeText ${app2:getFormInputClasses()}"
                               maxlength="40"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
                </div>
            </div>
        </div>
    </html:form>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="ProjectUser/List.do"
                        mode="bootstrap"
                        parameterName="lastName"/>
    </div>
    <app2:checkAccessRight functionality="PROJECTUSER" permission="CREATE">
        <div class="row">
            <div class="col-xs-12">
                <html:form action="/ProjectUser/Forward/CreateUser.do" styleClass="form-horizontal">
                    <html:submit
                            styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight  marginButton">
                        <fmt:message key="ProjectAssignee.newUser"/>
                    </html:submit>
                </html:form>
                <html:form action="/ProjectUser/Forward/CreatePerson.do?createPerson=true">
                    <html:submit
                            styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton">
                        <fmt:message key="ProjectAssignee.newPerson"/>
                    </html:submit>
                </html:form>
            </div>
        </div>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="projectUserList"
                     styleClass="${app2:getFantabulousTableClases()}"
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
                                        glyphiconClass="${app2:getClassGlyphEdit()}"
                                        width="50%"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PROJECTUSER" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"
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
    </div>

    <app2:checkAccessRight functionality="PROJECTUSER" permission="CREATE">
        <div class="row">
            <div class="col-xs-12">
                <html:form action="/ProjectUser/Forward/CreateUser.do">
                    <html:submit
                            styleClass="button pull-left marginRight marginButton ${app2:getFormButtonClasses()}">
                        <fmt:message key="ProjectAssignee.newUser"/>
                    </html:submit>
                </html:form>
                <html:form action="/ProjectUser/Forward/CreatePerson.do?createPerson=true">
                    <html:submit
                            styleClass="button pull-left marginRight marginLeft marginButton ${app2:getFormButtonClasses()}">
                        <fmt:message key="ProjectAssignee.newPerson"/>
                    </html:submit>
                </html:form>
            </div>
        </div>
    </app2:checkAccessRight>
</div>