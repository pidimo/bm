<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br>
<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="2">
        <fmt:message key="Admin.User.Title"/>
    </td>
</tr>
<html:form action="/User/List.do" focus="parameter(lastName@_firstName@_login)">
    <TR>
        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain" width="85%">
            <html:text property="parameter(lastName@_firstName@_login)" styleClass="largeText" maxlength="40"/>
            &nbsp;
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
    </TR>
</html:form>
<tr>
    <td colspan="2" align="center" class="alpha">
        <fanta:alphabet action="User/List.do" parameterName="lastNameAlpha"/>
    </td>
</tr>
<tr>

    <html:form styleId="CREATE_NEW_USER"
               action="/User/Forward/Create?dto(companyId)=${sessionScope.user.valueMap['companyId']}">
        <td class="button" colspan="2"><br>
            <app2:securitySubmit operation="CREATE" functionality="USER" styleClass="button">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </td>
    </html:form>
</tr>

<tr>
    <td colspan="2" align="center">
        <fanta:table list="userList" width="100%" id="userValue" action="User/List.do" imgPath="${baselayout}"
                     align="center">
            <c:set var="editLink"
                   value="User/Forward/Update.do?userId=${userValue.userId}&dto(userId)=${userValue.userId}&dto(userName)=${app2:encode(userValue.userName)}&dto(employeeName)=${app2:encode(userValue.userName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <app2:checkAccessRight functionality="USER" permission="VIEW" >
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="33%" image="${baselayout}/img/edit.gif"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="USER" permission="UPDATE">
                    <fanta:actionColumn name="" title="Common.update"
                                        action="User/Forward/UpdatePassword.do?userId=${userValue.userId}&dto(userId)=${userValue.userId}&dto(userName)=${app2:encode(userValue.userName)}&dto(employeeName)=${app2:encode(userValue.userName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=1"
                                        styleClass="listItem" headerStyle="listHeader" width="34%"
                                        image="${baselayout}/img/key.gif"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="USER" permission="DELETE">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                        <c:if test="${userValue.userId != sessionScope.user.valueMap['userId'] && userValue.default == 0}">
                            <html:link
                                    page="/User/Forward/Delete.do?userId=${userValue.userId}&dto(userId)=${userValue.userId}&dto(userName)=${app2:encode(userValue.userName)}&dto(employeeName)=${app2:encode(userValue.userName)}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"
                                    titleKey="Common.delete">
                                <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                          border="0"/>
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
                    <img alt="" src="<c:out value="${baselayout}"/>/img/check.gif"/>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </td>
</tr>
<tr>
    <br>
    <html:form styleId="CREATE_NEW_USER"
               action="/User/Forward/Create?dto(companyId)=${sessionScope.user.valueMap['companyId']}">
        <td class="button" colspan="2">
            <app2:securitySubmit operation="CREATE" functionality="USER" styleClass="button">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </td>
    </html:form>
</tr>


</table>
