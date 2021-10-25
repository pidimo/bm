<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<br>
<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title"  ${(default == 'true')? "colspan=\"4\"":"colspan=\"2\""}>
        <fmt:message key="User.userSession"/>
    </td>
</tr>
<html:form action="/User/UserSessionList.do" focus="parameter(lastName@_firstName@_login)">
    <tr>
        <td class="label" width="10%"><fmt:message key="User.title"/></td>
        <td align="left" class="contain" width="25%">
            <html:text property="parameter(lastName@_firstName@_login)" styleClass="mediumText" maxlength="40"/>
            &nbsp;
    <c:if test="${default=='false'}">
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
    </c:if>
        </td>
    <c:if test="${default}">
        <td class="label" width="15%"><fmt:message key="Common.company"/></td>
        <td align="left" class="contain" width="35%">
                <html:text property="parameter(lastNameCia@_firstNameCia@_loginCia)" styleClass="mediumText" maxlength="40"/>
                    &nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
        </td>
     </c:if>
    </tr>
</html:form>

<tr>
    <td  ${(default == 'true')? "colspan=\"4\"":"colspan=\"2\""} align="center" class="alpha" >
        <fanta:alphabet action="User/UserSessionList.do" parameterName="lastNameAlpha"/>
    </td>
</tr>
<tr>
    <td  ${(default == 'true')? "colspan=\"4\"":"colspan=\"2\""} align="center">
    <br>
        <fanta:table list="userSessionList" width="100%" id="userValue" action="User/UserSessionList.do" imgPath="${baselayout}"
                     align="center">
            <fmt:message   var="dateTimePattern" key="dateTimePattern"/>
            <fanta:dataColumn name="userName"  styleClass="listItem" title="User.user"
                              headerStyle="listHeader" width="18%" orderable="true" maxLength="20"/>
           <c:if test="${view != 'true'}">
            <fanta:dataColumn name="companyName"  styleClass="listItem" title="Company"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="18"/>
           </c:if>
<fanta:dataColumn name="startConection" styleClass="listItem"  title="User.startConection" headerStyle="listHeader" width="13%" orderable="true" renderData="false">
<c:choose>
  <c:when test="${userValue.startConection != ''}">
${app2:getDateWithTimeZone(userValue.startConection, timeZone, dateTimePattern)}
  </c:when>
</c:choose>
</fanta:dataColumn>
<fanta:dataColumn name="lastConection" styleClass="listItem"  title="User.lastConection" headerStyle="listHeader" width="13%" orderable="true" renderData="false">
    <c:choose>
      <c:when test="${userValue.lastConection != ''}">
       ${app2:getDateWithTimeZone(userValue.lastConection, timeZone, dateTimePattern)}
        </c:when>
    </c:choose>
</fanta:dataColumn>
<fanta:dataColumn name="endConection" styleClass="listItem"  title="User.endConection" headerStyle="listHeader" width="13%" orderable="true" renderData="false">
<c:choose>
  <c:when test="${userValue.endConection != ''}">
        ${app2:getDateWithTimeZone(userValue.endConection, timeZone, dateTimePattern)}
    </c:when>
</c:choose>
</fanta:dataColumn>
<fanta:dataColumn name="ip" styleClass="listItem" title="User.ip" headerStyle="listHeader" width="10%" orderable="true"/>

<fanta:dataColumn name="lastActionApp" styleClass="listItem"  title="User.lastActionApp" headerStyle="listHeader" width="13%" orderable="true" renderData="false">
    <c:choose>
        <c:when test="${userValue.lastActionApp != ''}">
            ${app2:getDateWithTimeZone(userValue.lastActionApp, timeZone, dateTimePattern)}
        </c:when>
    </c:choose>
</fanta:dataColumn>

<fanta:dataColumn name="statusId" styleClass="listItem2Center" title="User.connected" renderData="false"
                              headerStyle="listHeader" width="5%" orderable="true">
        <c:choose>
          <c:when test="${userValue.statusId == '1'}">
                <html:img titleKey="User.connected" src="${baselayout}/img/check.gif" border="0" />
            </c:when>
            <c:otherwise>
                <html:img  titleKey="User.disconnected" src="${baselayout}/img/logout.gif" border="0" />
            </c:otherwise>
        </c:choose>
</fanta:dataColumn>
        </fanta:table>
    </td>
</tr>
</table>
