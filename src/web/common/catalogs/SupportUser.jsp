<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<script type="text/javascript">
function clear(){

 document.getElementById('fieldViewUserName_id').value = "";
 document.getElementById('fieldViewUserId_id').value = null;
 document.getElementById('reload').value = "true";
 document.getElementById('notification').style.display = "none";
 document.forms[0].submit();
}

</script>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
      <td>
         <html:form action="${action}" >
         <table  border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
          <html:hidden property="dto(op)" value="${op}"/>
          <html:hidden property="dto(save_)"/>
          <html:hidden property="dto(lastUser)"/>
          <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
          <c:if test="${('update' == op) || ('delete' == op)}">
              <html:hidden property="dto(userId)" styleId="userId"/>
              <%--<html:hidden property="dto(productId)"/>--%>
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
              <TD class="label" width="35%" nowrap><fmt:message  key="SupportUser.supportProduct"/></TD>
              <TD class="contain" width="65%">

<html:hidden property="dto(productId)" styleId="field_key"/>
<html:hidden property="dto(1)" styleId="field_versionNumber"/>
<html:hidden property="dto(2)" styleId="field_unitId"/>
<html:hidden property="dto(3)" styleId="field_price"/>

<app:text property="dto(productName)" styleId="field_name" styleClass="mediumText" maxlength="40" readonly="true" tabindex="1" view="${op != 'create'}"/>

<tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search" hide="${op != 'create'}" tabindex="2"/>
<tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear" hide="${op != 'create'}" tabindex="3"/>

              </TD>
          </TR>
          <TR>
              <TD class="label"><fmt:message   key="SupportUser.supportUser"/></TD>
              <TD class="contain">
<c:if test="${op == 'update'}" >
    <html:hidden property="dto(userId_selected)" styleId="fieldViewUserId_id"/>
</c:if>

<c:if test="${op != 'update'}" >
    <html:hidden property="dto(userId)" styleId="fieldViewUserId_id"/>
</c:if>

<app:text property="dto(userName)" styleClass="mediumText" maxlength="40" view="${op == 'delete'}" styleId="fieldViewUserName_id" readonly="true" tabindex="4" />

<tags:selectPopup url="/catalogs/Support/SupportUserImportList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
 name="searchUser"
 hide="${op == 'delete'}"
 titleKey="Scheduler.grantAccess.searchUser"
 width="630"
 heigth="480"
 imgWidth="17"
 imgHeight="19" submitOnSelect="true"
 tabindex="5"
 />

<c:if test="${op != 'delete'}" >
<html:hidden property="dto(reload)" styleId="reload"/>
<a href="javascript:clear();" tabindex="6">
        <img src="<c:out value="${sessionScope.baselayout}"/>/img/clear.png" alt="<fmt:message key="Common.clear"/>"
     border="0" align="middle" />
</a>
</c:if>

    </td>
</tr>
<%--<c:if test="${op != 'create'}" >--%>
<tr>
    <td class="label">
      <fmt:message key="Admin.CaseEmail"/>
    </td>
    <td class="contain">
        <div id="notification">
            <html:hidden property="dto(emailNotification)" write="true"/>
        </div>
    </td>
</tr>
<%--</c:if>--%>
     </table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
           <table cellSpacing=0 cellPadding=4 width="70%" border=0 align="center">
              <tr>
                  <td class="button">
                  <%--<c:if test="${op != 'update'}" >--%>
                    <app2:securitySubmit property="dto(save)" operation="${op}" functionality="SUPPORTUSER" styleClass="button" tabindex="7" >
                        ${button}
                    </app2:securitySubmit>
                    <%--</c:if>--%>
                  <c:if test="${op == 'create'}" >
                    <%--<app2:securitySubmit operation="${op}" functionality="SUPPORTUSER" styleClass="button" tabindex="11"  property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>--%>
                  </c:if>
                  <html:cancel styleClass="button" tabindex="8" ><fmt:message   key="Common.cancel"/></html:cancel>
                  </td>
              </tr>
           </table>
      </html:form>
      </td>
  </tr>
</table>