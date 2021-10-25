<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/User/UserGroupList.do" focus="parameter(groupName)" styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="UserGroup.userGroupList"/>
            </legend>
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(groupName)" styleClass="${app2:getFormInputClasses()} largeText"
                                   maxlength="40"/>
                        <div class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="User/UserGroupList.do" parameterName="groupNameAlpha" mode="bootstrap"/>
    </div>

    <html:form action="/User/Forward/CreateUserGroup.do">
        <div class="${app2:getFormGroupClasses()}">
            <app2:securitySubmit operation="create" functionality="USERGROUP"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}" list="userGroupList"
                     width="100%" id="userGroup" action="User/UserGroupList.do"
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
                                        glyphiconClass="${app2:getClassGlyphOrganization()}"/>

                    <fanta:actionColumn name="edit" title="Common.update" action="${edit}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="USERGROUP" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${delete}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
    </div>

    <html:form action="/User/Forward/CreateUserGroup.do">
        <div class="${app2:getFormGroupClasses()}">
            <app2:securitySubmit operation="create" functionality="USERGROUP"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

</div>
