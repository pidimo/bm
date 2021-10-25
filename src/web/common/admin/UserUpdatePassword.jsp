<%@ include file="/Includes.jsp" %>
 <calendar:initialize/>

<table width="40%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="left">
    <html:form action="${action}" focus="dto(userPassword)" >

     <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
<app2:securitySubmit operation="${op}" functionality="USER" styleClass="button" property="dto(save)">
    ${button}
</app2:securitySubmit>

          </td>
        </tr>
      </table>
    <table id="Employee.jsp" border="0" cellpadding="3" cellspacing="0" width="100%" align="center" class="container">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}" />
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(employeeName)"/>
       <tr >
          <td colspan="4" class="title"><c:out value="${title}"/> </td>
        </tr>
         <td class="label"><fmt:message   key="User.login"/> </td>
          <td class="contain">
             <app:text property="dto(userLogin)" view="${op == 'update'}" styleClass="mediumText" maxlength="20" tabindex="4" />
           </td>
          </tr>
          <tr>
              <td class="label"><fmt:message   key="User.password"/>  </td>
              <td class="contain">
                <html:password property="dto(userPassword)"  styleClass="mediumText" maxlength="20" tabindex="2" redisplay="false"  />
             </td>
         </tr>
         <tr>
              <td class="label"><fmt:message   key="User.passConfir"/>  </td>
              <td class="contain">
                <html:password property="dto(passwordConfir)"  styleClass="mediumText" maxlength="20" tabindex="2" redisplay="false"/>
             </td>
         </tr>
      </table>
	  <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
             <app2:securitySubmit operation="${op}" functionality="USER" styleClass="button" property="dto(save)">
                 ${button}
             </app2:securitySubmit>

          </td>
        </tr>
      </table>
      </html:form>
    </td>
  </tr>
</table>