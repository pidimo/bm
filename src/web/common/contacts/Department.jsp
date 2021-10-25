<%@ include file="/Includes.jsp" %>

<%--
	the following attributes should be set before including this page:
		- op (create|read|update|delete) : operation of this page
--%>

<table width="60%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="left">
    <html:form action="${action}" focus="dto(name)" >


    <table id="Department.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(departmentId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <tr >
          <td colspan="2" class="title"><c:out value="${title}"/> </td>
        </tr>
        <tr>
          <td width="25%" class="label" ><fmt:message    key="Contact.Organization.Department.name" /></td>
          <td width="75%"class="contain">
               <app:text property="dto(name)" view="${op == 'delete'}" styleClass="mediumText" maxlength="30" tabindex="1"/>
          </td>
        </tr>
        <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Department.nameParent"/>  </td>
          <td  class="contain">
            <fanta:select property="dto(parentId)" listName="${op == 'delete' ? 'departmentBaseList' : 'parentDepartmentList'}" firstEmpty="true" labelProperty="name" valueProperty="departmentId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                <fanta:parameter field="addressId" value="${ not empty param.contactId?param.contactId:0}"/>
                <c:if test="${op != 'create'}">
                    <fanta:parameter field="departmentId" value="${not empty dto.departmentId?dto.departmentId:0}"/>
                </c:if>
            </fanta:select>
          </td>
        </tr>
        <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Department.manager"/>  </td>
          <td  class="contain">   <!-- tag with contact persons-->
            <fanta:select property="dto(managerId)" listName="searchContactPersonList" firstEmpty="true" labelProperty="contactPersonName" valueProperty="contactPersonId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="3">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                <fanta:parameter field="addressId" value="${ not empty param.contactId?param.contactId:0}"/>
            </fanta:select>
          </td>
        </tr>        
      </table>
	  <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
              <app2:securitySubmit operation="${op}" functionality="DEPARTMENT" styleClass="button" tabindex="4"  ><c:out value="${button}"/></app2:securitySubmit>
              <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="DEPARTMENT" styleClass="button" property="SaveAndNew" tabindex="4"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
              </c:if>
              <html:cancel styleClass="button" tabindex="6"><fmt:message   key="Common.cancel"/></html:cancel>
         </tr>
      </table>
      </html:form>
    </td>
  </tr>
</table>

