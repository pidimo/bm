<%@ include file="/Includes.jsp" %>



<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2"><fmt:message key="Scheduler.grantAccess.searchUser"/></td>
    </tr>

    <html:form action="/${action}?other=${param.other}" focus="parameter(lastName@_firstName@_searchName)">
        <html:hidden property="parameter(ownUserId)" value="${sessionScope.user.valueMap['userId']}"/>
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
            <fanta:alphabet action="${action}?other=${param.other}" parameterName="lastNameAlpha"/>
        </td>
    </tr>


    <tr>
        <td colspan="2" align="center">
            <br/>
            <fanta:table width="100%" id="viewUser" action="${action}?other=${param.other}"
                         imgPath="${baselayout}" align="center">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
    <c:choose>
    <c:when test="${param.other =='true'}" >

<fanta:actionColumn name="" useJScript="true"
                    action="javascript:opener.selectMultipleEvenField('${viewUser.userId}', '${app2:jscriptEncode(viewUser.userName)}');"
                    title="Common.select" styleClass="listItem" headerStyle="listHeader"
                    image="${baselayout}/img/import.gif"/>
    </c:when>
    <c:otherwise>
<fanta:actionColumn name="" useJScript="true"
                    action="javascript:opener.selectField('fieldViewUserId_id', '${viewUser.userId}', 'fieldViewUserName_id', '${app2:jscriptEncode(viewUser.userName)}');"
                    title="Common.select" styleClass="listItem" headerStyle="listHeader"
                    image="${baselayout}/img/import.gif"/>
    </c:otherwise>
    </c:choose>
                </fanta:columnGroup>
                <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name" headerStyle="listHeader"
                                  width="65%" orderable="true"/>
                <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser" renderData="false"
                                  headerStyle="listHeader" width="15%" orderable="true">
                    <c:if test="${viewUser.type == '1'}">
                        <fmt:message key="User.intenalUser"/>
                    </c:if>
                    <c:if test="${viewUser.type == '0'}">
                        <fmt:message key="User.externalUser"/>
                    </c:if>
                </fanta:dataColumn>                
            </fanta:table>
        </td>
    </tr>
</table>