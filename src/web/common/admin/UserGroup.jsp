<%@ include file="/Includes.jsp" %>
</br>
<html:form action="${action}" focus="dto(groupName)" >

   <table id="userGroup.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
   <html:hidden property="dto(op)" value="${op}"/>
   <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
       <html:hidden property="dto(op)" value="${op}"/>
       <c:if test="${op=='update' || op=='delete'}">
           <html:hidden property="dto(userGroupId)"/>
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
                <TD class="label" width="35%">
                    <fmt:message   key="UserGroup.name"/>
                </TD>
                <TD class="contain" width="65%">
                    <app:text property="dto(groupName)" styleClass="middleText" maxlength="60" tabindex="1" view="${op == 'delete'}"/>
                </TD>
        </TR>
       <tr>
           <TD class="label">
               <fmt:message   key="UserGroup.groupType"/>
           </TD>
           <TD class="contain">
               <c:set var="groupTypes" value="${app2:getUserGroupTypes(pageContext.request)}"/>
               <html:select property="dto(groupType)" styleClass="middleSelect" tabindex="2"
                            readonly="${'delete' == op}">
                   <html:option value="">&nbsp;</html:option>
                   <html:options collection="groupTypes" property="value" labelProperty="label"/>
               </html:select>
           </TD>
       </tr>

     <TR>
             <TD class="button" align="rigth" colspan="2">
                <c:if test="${op == 'create' || op == 'update'}">
                    <app2:securitySubmit operation="${op}" functionality="USERGROUP" property="dto(save)" styleClass="button" styleId="saveButtonId" tabindex="5">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'delete'}">
                    <app2:securitySubmit operation="DELETE" functionality="USERGROUP" property="dto(delete)" styleClass="button" tabindex="6">
                        <fmt:message   key="Common.delete"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel  styleClass="button" tabindex="7"><fmt:message key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
    </table>
</html:form>