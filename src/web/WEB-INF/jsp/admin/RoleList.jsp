<%@ include file="/Includes.jsp" %>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<div class="${app2:getListWrapperClasses()}">
    <legend class="title">
        <fmt:message key="Admin.Role.Title"/>
    </legend>
    <html:form action="/Role/List.do" focus="parameter(roleName)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="roleName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(roleName)"
                               styleId="roleName_id"
                               styleClass="largeText ${app2:getFormInputClasses()}"
                               maxlength="40"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                                key="Common.go"/></html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Role/List.do" mode="bootstrap" parameterName="roleNameAlpha"/>
    </div>

    <html:form action="/Role/Forward/Create">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="CREATE"
                                 functionality="ROLE"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <app2:checkAccessRight functionality="ROLE" permission="VIEW" var="hasRoleView"/>
        <app2:checkAccessRight functionality="ACCESSRIGHT" permission="UPDATE" var="hasAccessRightUpdate"/>
        <app2:checkAccessRight functionality="ROLE" permission="DELETE" var="hasRoleDelete"/>
        <fanta:table mode="bootstrap" width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="roleValue"
                     action="Role/List.do"
                     imgPath="${baselayout}"
                     align="center">
            <c:set var="editLink"
                   value="Role/Forward/Update.do?roleId=${roleValue.roleId}&dto(roleId)=${roleValue.roleId}&dto(roleName)=${app2:encode(roleValue.roleName)}&isDefault=${roleValue.isDefault}&index=0"/>
            <c:set var="accessRightsLink"
                   value="Role/Forward/AccessRight/Read.do?roleId=${roleValue.roleId}&isDefault=${roleValue.isDefault}&dto(roleName)=${app2:encode(roleValue.roleName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(roleId)=${roleValue.roleId}&dto(isDefault)=${roleValue.isDefault}&index=1"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="7%">

                <c:if test="${hasRoleView}">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </c:if>
                <c:if test="${hasAccessRightUpdate}">
                    <fanta:actionColumn name="" title="Common.accessRights" action="${accessRightsLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphKeyLock()}"/>
                </c:if>
                <c:if test="${hasRoleDelete}">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                        <c:choose>
                            <c:when test="${roleValue.isDefault != '1'}">
                                <html:link
                                        page="/Role/Forward/Delete.do?roleId=${roleValue.roleId}&dto(roleId)=${roleValue.roleId}&dto(roleName)=${app2:encode(roleValue.roleName)}&dto(withReferences)=true&index=0"
                                        titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </c:if>
            </fanta:columnGroup>
            <fanta:dataColumn name="roleName" action="${editLink}" styleClass="listItem2" title="Role.name"
                              headerStyle="listHeader" width="95%" orderable="true" maxLength="40"/>
        </fanta:table>
    </div>

    <html:form action="/Role/Forward/Create">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="CREATE" functionality="ROLE" styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>
</div>


