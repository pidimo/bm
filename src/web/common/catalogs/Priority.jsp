<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>

  </tr>
  <tr>
      <td>
         <html:form action="${action}" focus="dto(priorityName)" >
         <table id="Priority.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

          <html:hidden property="dto(op)" value="${op}"/>
          <html:hidden property="dto(type)" value="${type}"/>
          <c:if test="${('update' == op) || ('delete' == op)}">
              <html:hidden property="dto(priorityId)"/>
          </c:if>
          <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
          </c:if>
          <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
          </c:if>
          <TR>
              <TD colspan="2" class="title">
                  <c:out value="${title}"/>
              </TD>
          </TR>

          <TR>
              <TD class="label" width="25%" nowrap><fmt:message  key="Priority.name"/></TD>
              <TD class="contain" width="75%">
              <app:text property="dto(priorityName)" styleClass="largetext" maxlength="20" view="${'delete' == op}"/>
              </TD>
          </TR>
          <TR>
              <TD class="label"><fmt:message   key="Priority.sequence"/></TD>
              <TD class="contain">
              <app:text property="dto(sequence)" styleClass="shortText" style="text-align:right" maxlength="4" view="${'delete' == op}"/>
              </TD>
          </TR>
          <tr>
              <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
          </tr>
          </table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
           <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
              <TR>
                  <TD class="button">
                    <app2:securitySubmit operation="${op}" functionality="${function}" styleClass="button">
                        ${button}
                    </app2:securitySubmit>
                  <c:if test="${op == 'create'}" >
                    <app2:securitySubmit operation="${op}" functionality="${function}" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
                  </c:if>
                  <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
                  </TD>
              </TR>
           </table>
      </html:form>

      </td>
  </tr>
</table>



