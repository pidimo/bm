<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <html:form focus="parameter(lastName@_firstName@_searchName)"
               action="/User/Forward/UserGroupImportList.do?userGroupId=${param.userGroupId}&parameter(userGroupId)=${param.userGroupId}"
               styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(lastName@_firstName@_searchName)"
                               styleClass="${app2:getFormInputClasses()} largeText"
                               maxlength="40"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet
                action="User/Forward/UserGroupImportList.do?userGroupId=${param.userGroupId}&parameter(userGroupId)=${param.userGroupId}"
                parameterName="lastNameAlpha" mode="bootstrap"/>
    </div>

    <app2:checkAccessRight functionality="USERGROUP" permission="UPDATE">
        <c:set var="groupName"><%=request.getAttribute("groupName")%>
        </c:set>
        <html:form action="User/UserGroupImportList/SearchUser.do?dto(groupName)=${app2:encode(groupName)}">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                     list="userByGroupList" width="100%" id="userImport"
                     action="User/Forward/UserGroupImportList.do?userGroupId=${param.userGroupId}"
                     imgPath="${baselayout}" align="center">
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                <c:set var="delete"
                       value="/UserOfGroup/Forward/Delete.do?dto(withReferences)=true&userName=${app2:encode(userImport.userName)}&dto(userGroupId)=${userImport.userGroupId}&dto(name)=${app2:encode(userImport.userName)}&userId=${userImport.userId}&dto(userId)=${userImport.userId}"/>
                <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                    <app2:checkAccessRight functionality="USERGROUP" permission="UPDATE">
                        <app:link page="${delete}" titleKey="Common.delete">
                            <span class="${app2:getClassGlyphTrash()}"></span>
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
    </div>

    <app2:checkAccessRight functionality="USERGROUP" permission="UPDATE">
        <html:form action="User/UserGroupImportList/SearchUser.do?dto(groupName)=${app2:encode(groupName)}">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
