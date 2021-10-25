<%@ include file="/Includes.jsp" %>

<table width="60%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="left">
    <html:form action="${action}" focus="dto(name)" >

    
    <table id="Office.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(officeId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <tr>
          <td colspan="2" class="title"><c:out value="${title}"/> </td>
        </tr>
        <tr>
          <td width="25%" class="label" ><fmt:message    key="Contact.Organization.Office.name" /></td>
          <td width="75%"class="contain">
<%--      <html:text property="dto(name)" readonly="${op == 'delete'}" styleClass="mediumText" maxlength="15" tabindex="1" />--%>
               <app:text property="dto(name)" view="${op == 'delete'}" styleClass="mediumText" maxlength="15" tabindex="1" />
         </td>
        </tr>
         <tr>
          <td width="25%" class="label"><fmt:message   key="Contact.Organization.Office.supervisor"/>  </td>
          <td width="75%" class="contain">   <!-- Collection employees-->
            <fanta:select property="dto(supervisorId)" listName="employeeBaseList" firstEmpty="true" labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>
          </td>
        </tr>
        <tr>
	            <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
             </tr>
      </table>
	  <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
              <app2:securitySubmit operation="${op}" functionality="OFFICE" styleClass="button" tabindex="3" ><c:out value="${button}"/></app2:securitySubmit>
              <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="OFFICE" styleClass="button" property="SaveAndNew" tabindex="4" ><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
              </c:if>
              <html:cancel styleClass="button"  tabindex="6" ><fmt:message   key="Common.cancel"/></html:cancel>
          </td>
        </tr>
      </table>
      </html:form>
    </td>
  </tr>
</table>

