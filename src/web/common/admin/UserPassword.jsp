<%@ include file="/Includes.jsp" %>


   <html:form action="/User/PasswordUpdate.do" focus="dto(userPassword)"  >

   <html:hidden property="dto(op)" value="update"/>
   <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
   <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
   <html:hidden property="dto(opPreference)" value="upPassword"/>

    <table cellSpacing=0 cellPadding=0 width="500" border=0 align="center" >
    <TR>
            <TD class="button">
                <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button">${save}</app2:securitySubmit>
                <%--<html:submit property="dto(save)" styleClass="button" ><c:out value="${save}"/></html:submit>--%>
            </td>
    </TR>
    <TR>
        <TD>
           <table id="User.jsp" border="0" cellpadding="0" cellspacing="0" width="500" align="center" class="container">
              <TR>
                <TD colspan="2" class="title"><fmt:message   key="User.passChange"/>
                </TD>
              </TR>
              <tr>
                <TD class="label"><fmt:message   key="User.pass"/></TD>
                <TD class="contain">
                    <html:password property="dto(userPassword)"  styleClass="mediumText" maxlength="20" tabindex="1" value=""/>
                </TD>
              </tr>
              <tr>
                <TD class="label"><fmt:message   key="User.passNew"/></TD>
                <TD class="contain">
                    <html:password property="dto(password1)"  styleClass="mediumText" tabindex="2" value="" maxlength="20"/>
                </TD>
              </tr>
              <tr>
                     <TD class="label"><fmt:message   key="User.passConfir"/></TD>
                     <TD class="contain">
                     <html:password property="dto(password2)"  styleClass="mediumText" tabindex="3" value="" maxlength="20"/>
                     </TD>
              </tr>
         </table>
     </TD>
  </tr>
  <TR>
    <TD class="button">
        <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button" tabindex="4">${save}</app2:securitySubmit>
         <%--<html:submit property="dto(save)" styleClass="button" tabindex="4" ><c:out value="${save}"/></html:submit>--%>
    </td>
   </TR>

    </table>
 </html:form>


