<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(name)" >

   <table id="ContactGroup.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <TR>
                <TD colspan="2" class="title">
                    <c:out value="${title}"/>
                </TD>
        </TR>
        <TR>
                <TD class="label" width="15%">
                    <fmt:message   key="Webmail.contactGroup.name"/>
                </TD>
                <TD class="contain" width="35%">
                    <app:text property="dto(name)" styleClass="middleText" maxlength="50" tabindex="1" view="${op == 'delete'}"/>
                </TD>
        </TR>

        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${op=='update' || op=='delete'}">
            <html:hidden property="dto(mailGroupAddrId)"/>
        </c:if>
      </table>

      <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
            <TR>
                 <TD class="button">
                    <c:if test="${op == 'create' || op == 'update'}">
                        <app2:securitySubmit operation="${op}" functionality="WEBMAILGROUP" property="dto(save)" styleClass="button" styleId="saveButtonId"><c:out value="${button}"/></app2:securitySubmit>
                    </c:if>
                    <c:if test="${op == 'delete'}">
                        <html:submit property="dto(delete)" styleClass="button" ><fmt:message   key="Common.delete"/></html:submit>
                    </c:if>
                    <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                </TD>
            </TR>
       </table>
</html:form>