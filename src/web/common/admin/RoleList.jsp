<%@ include file="/Includes.jsp" %>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br>

<table width="70%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Admin.Role.Title"/>
        </td>
    </tr>
    <html:form action="/Role/List.do" focus="parameter(roleName)">
        <TR>
            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(roleName)" styleClass="largeText" maxlength="40"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </TR>
    </html:form>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="Role/List.do" parameterName="roleNameAlpha"/>
        </td>
    </tr>


    <tr>
        <html:form action="/Role/Forward/Create">
            <td class="button" colspan="2"><br>
                <app2:securitySubmit operation="CREATE" functionality="ROLE" styleClass="button">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </td>
        </html:form>
    </tr>

    <tr>
        <td colspan="2">
            <app2:checkAccessRight functionality="ROLE" permission="VIEW" var="hasRoleView"/>
            <app2:checkAccessRight functionality="ACCESSRIGHT" permission="UPDATE" var="hasAccessRightUpdate"/>
            <app2:checkAccessRight functionality="ROLE" permission="DELETE" var="hasRoleDelete"/>
            <fanta:table width="100%" id="roleValue" action="Role/List.do" imgPath="${baselayout}" align="center">
                <c:set var="editLink"
                       value="Role/Forward/Update.do?roleId=${roleValue.roleId}&dto(roleId)=${roleValue.roleId}&dto(roleName)=${app2:encode(roleValue.roleName)}&isDefault=${roleValue.isDefault}&index=0"/>
                <c:set var="accessRightsLink"
                       value="Role/Forward/AccessRight/Read.do?roleId=${roleValue.roleId}&isDefault=${roleValue.isDefault}&dto(roleName)=${app2:encode(roleValue.roleName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(roleId)=${roleValue.roleId}&dto(isDefault)=${roleValue.isDefault}&index=1"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="7%">

                    <c:if test="${hasRoleView}">
                        <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="33%" image="${baselayout}/img/edit.gif"/>
                    </c:if>
                    <c:if test="${hasAccessRightUpdate}">
                        <fanta:actionColumn name="" title="Common.accessRights" action="${accessRightsLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="33%"
                                            image="${baselayout}/img/key.gif"/>
                    </c:if>
                    <c:if test="${hasRoleDelete}">
                        <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                            <c:choose>
                                <c:when test="${roleValue.isDefault != '1'}">
                                    <html:link
                                            page="/Role/Forward/Delete.do?roleId=${roleValue.roleId}&dto(roleId)=${roleValue.roleId}&dto(roleName)=${app2:encode(roleValue.roleName)}&dto(withReferences)=true&index=0"
                                            titleKey="Common.delete">
                                        <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                                  border="0"/>
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
        </td>
    </tr>

    <tr>
        <html:form action="/Role/Forward/Create">
            <td class="button" colspan="2">
                <app2:securitySubmit operation="CREATE" functionality="ROLE" styleClass="button">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </td>
        </html:form>
    </tr>
</table>
