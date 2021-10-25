<%@ include file="/Includes.jsp" %>

<c:set var="path" value="${pageContext.request.contextPath}"/>

<br>

    <table cellSpacing=0 cellPadding=2 width="50%" border=0 align="center" >

    <TR>
        <TD>
        <html:form action="${action}" focus="dto(roleName)">
            <table id="Role.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <c:if test="${op == 'update'}">

                    <html:hidden property="dto(version)"/>
                </c:if>
                <c:if test="${op != 'create'}">
                    <html:hidden property="dto(roleId)"/>                     
                </c:if>
                <TR>
                    <TD colspan="2" class="title">
                        <c:out value="${title}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="label" width="15%"><fmt:message   key="Role.name"/></TD>
                    <TD class="contain" width="35%">
                      <app:text property="dto(roleName)" styleClass="mediumText" maxlength="80" tabindex="1" view="${op == 'delete'}" />
                    </TD>
                </tr>
                <tr>
                    <TD class="topLabel"><fmt:message   key="Role.description"/></TD>
                    <TD class="contain">
                        <html:textarea  property="dto(roleDescription)" styleClass="mediumDetailHigh" tabindex="2" readonly="${op == 'delete'}" />
                    </TD>
                </tr>

      </table>
     </TD>
  </tr>
  <TR>
    <TD class="button">
<app2:securitySubmit operation="${op}" functionality="ROLE" styleClass="button" property="dto(save)" tabindex="3">
    ${button}
</app2:securitySubmit>

         <c:if test="${op == 'create'}">
<app2:securitySubmit operation="${op}" functionality="ROLE" styleClass="button" property="SaveAndNew" tabindex="4" >
    <fmt:message   key="Common.saveAndNew"/>
</app2:securitySubmit>

         </c:if>
         <html:cancel styleClass="button" tabindex="5" ><fmt:message key="Common.cancel"/></html:cancel>
    </TD>
   </TR>

    </table>
 </html:form>


