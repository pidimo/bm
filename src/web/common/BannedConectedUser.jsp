<%@include file="/Includes.jsp" %>

<html:form action="/Banned/PreviousUserConnected.do">

  <%--when process from external logon AJAX--%>
  <div id="isOtherUserConnectedDivId">
    <input type="hidden" id="urlOtherUserConnectedId" value="${param['urlOtherUserConnected']}">
  </div>


  <table cellSpacing=0 cellPadding=0 width="45%" border=0 align="center" class="container">
    <tr>
      <td class="title">
        <fmt:message key="User.alreadyOtherUserLogged.title"/>
      </td>
    </tr>
    <tr>
      <td class="contain">
        <fmt:message key="User.alreadyOtherUserLogged"/>
      </td>
    </tr>
    <tr>
      <td class="button">
        <html:submit property="dto(save)" styleClass="button" tabindex="1">
          <fmt:message key="Common.continue"/>
        </html:submit>

        <app:url var="urlLogoff" value="/Logoff.do?locale=${sessionScope.user.valueMap['locale']}&keepUserSessionAsActive=true"/>
        <html:button property="" styleClass="button" onclick="location.href='${urlLogoff}'" tabindex="2">
          <fmt:message key="Common.cancel"/>
        </html:button>
      </td>
    </tr>
  </table>
</html:form>


