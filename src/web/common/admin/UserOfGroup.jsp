<%@ include file="/Includes.jsp" %>
</br>
<html:form action="${action}?userGroupId=${param.userGroupId}" focus="dto(name)" >

   <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
   <html:hidden property="dto(op)" value="${op}"/>
   <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
   <html:hidden property="dto(userId)" value="${param.userId}"/>
   <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
        <TR> 
                <TD colspan="2" class="title">
                    <c:out value="${title}"/>
                </TD>
        </TR>
        <TR>
                <TD class="label" width="15%">
                    <fmt:message   key="UserGroup.member.userName"/>
                </TD>
                <TD class="contain" width="35%">
                    <app:text property="dto(name)" value="${param.userName}"  styleClass="middleText" maxlength="80" tabindex="1" view="${op == 'delete'}"/>
                </TD>
        </TR>
        
      </table>

      <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
            <TR>
                 <TD class="button">
                    <c:if test="${op == 'delete'}">
                        <html:submit property="dto(delete)" styleClass="button" ><fmt:message   key="Common.delete"/></html:submit>
                    </c:if>
                    <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                </TD>
            </TR>
       </table>
</html:form>