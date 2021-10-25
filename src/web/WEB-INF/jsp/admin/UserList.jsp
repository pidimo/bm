<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<div class="${app2:getListWrapperClasses()}">
    <legend class="title">
        <fmt:message key="Admin.User.Title"/>
    </legend>

    <html:form action="/User/List.do" focus="parameter(lastName@_firstName@_login)" styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="inputSearch_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(lastName@_firstName@_login)"
                               styleClass="largeText ${app2:getFormInputClasses()}" styleId="inputSearch_id"
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
        <fanta:alphabet action="User/List.do" mode="bootstrap" parameterName="lastNameAlpha"/>
    </div>

    <html:form styleId="CREATE_NEW_USER"
               action="/User/Forward/Create?dto(companyId)=${sessionScope.user.valueMap['companyId']}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="CREATE" functionality="USER"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="userList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="userValue"
                     action="User/List.do"
                     imgPath="${baselayout}"
                     align="center">
            <c:set var="editLink"
                   value="User/Forward/Update.do?userId=${userValue.userId}&dto(userId)=${userValue.userId}&dto(userName)=${app2:encode(userValue.userName)}&dto(employeeName)=${app2:encode(userValue.userName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <app2:checkAccessRight functionality="USER" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="USER" permission="UPDATE">
                    <fanta:actionColumn name="" title="Common.update"
                                        action="User/Forward/UpdatePassword.do?userId=${userValue.userId}&dto(userId)=${userValue.userId}&dto(userName)=${app2:encode(userValue.userName)}&dto(employeeName)=${app2:encode(userValue.userName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=1"
                                        styleClass="listItem" headerStyle="listHeader" width="34%"
                                        glyphiconClass="${app2:getClassGlyphKey()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="USER" permission="DELETE">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                        <c:if test="${userValue.userId != sessionScope.user.valueMap['userId'] && userValue.default == 0}">
                            <html:link
                                    page="/User/Forward/Delete.do?userId=${userValue.userId}&dto(userId)=${userValue.userId}&dto(userName)=${app2:encode(userValue.userName)}&dto(employeeName)=${app2:encode(userValue.userName)}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"
                                    titleKey="Common.delete">
                                <span class="${app2:getClassGlyphTrash()}"></span>
                            </html:link>
                        </c:if>
                    </fanta:actionColumn>
                </app2:checkAccessRight>

            </fanta:columnGroup>
            <fanta:dataColumn name="userName" action="${editLink}" styleClass="listItem" title="User.login"
                              headerStyle="listHeader" width="40%" orderable="true" maxLength="45"/>
            <fanta:dataColumn name="login" styleClass="listItem" title="User.userName" headerStyle="listHeader"
                              width="30%" orderable="true"/>
            <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser" renderData="false"
                              headerStyle="listHeader" width="15%" orderable="true">
                <c:if test="${userValue.type == '1'}">
                    <fmt:message key="User.intenalUser"/>
                </c:if>
                <c:if test="${userValue.type == '0'}">
                    <fmt:message key="User.externalUser"/>
                </c:if>
            </fanta:dataColumn>

            <fanta:dataColumn name="active" styleClass="listItem2Center" title="Common.active" headerStyle="listHeader"
                              width="10%" renderData="false" orderable="true">
                <c:if test="${userValue.active == '1'}">
                    <span title="" class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <html:form styleId="CREATE_NEW_USER"
               action="/User/Forward/Create?dto(companyId)=${sessionScope.user.valueMap['companyId']}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="CREATE" functionality="USER"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>
</div>

