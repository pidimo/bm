<%@ include file="/Includes.jsp" %>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
      <td>
         <html:form action="${action}" focus="dto(detail)" >
         <table  border="0" cellpadding="0" cellspacing="0" width="750" align="center" class="container">

          <html:hidden property="dto(op)" value="${op}"/>
          <html:hidden property="dto(actionHistory)" value="${actionHistory}"/>
          <fmt:message   var="dateTimePattern"   key="dateTimePattern"/>
          <html:hidden property="dto(commentId)"/>
          <html:hidden property="dto(withReferences)" value="true"/>
         <html:hidden property="dto(detail)"/>                
          <TR>
              <TD colspan="2" class="title">
                  <c:out value="${title}"/>
              </TD>
          </TR>

          <TR>
              <TD class="label" width="25%" nowrap><fmt:message  key="Link.publishBy"/></TD>
              <TD class="contain" width="75%">
              <app:text property="dto(ownerName)" styleClass="largetext" maxlength="20" view="${true}"/>
              </TD>
          </TR>
          <TR>
              <TD class="label" width="25%" nowrap><fmt:message  key="Link.publishDate"/></TD>
              <TD class="contain" width="75%">
              ${app2:getDateWithTimeZone(commentForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
              </TD>
          </TR>
          <TR>
              <TD class="contain" colspan="2">
              <html:textarea property="dto(detail)"  disabled="true"  style="height:80px;width:100%;"/>
              </TD>
          </TR>
          </table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
           <table cellSpacing=0 cellPadding=4 width="750" border=0 align="center">
              <TR>
                  <TD class="button">
                  <app2:securitySubmit operation="delete" functionality="ARTICLECOMMENT" styleClass="button" tabindex="10" >
                        ${button}
                  </app2:securitySubmit>
                  <html:cancel styleClass="button" tabindex="12" ><fmt:message   key="Common.cancel"/></html:cancel>
                  </TD>
              </TR>
           </table>
      </html:form>
      </td>
  </tr>
</table>
