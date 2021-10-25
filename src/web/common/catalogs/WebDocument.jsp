<%@ page import="com.piramide.elwis.cmd.utils.VariableConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="CONTACT_TYPE" value="<%=VariableConstants.VariableType.CONTACT.getConstant()%>"/>
<c:set var="COMPANY_TYPE" value="<%=VariableConstants.VariableType.COMPANY.getConstant()%>"/>
<c:set var="EMPLOYEE_TYPE" value="<%=VariableConstants.VariableType.EMPLOYEE.getConstant()%>"/>


<html:form action="${action}" focus="dto(name)">
  <html:hidden property="dto(op)" value="${op}"/>

  <c:if test="${'update'== op || 'delete'== op}">
    <html:hidden property="dto(webDocumentId)"/>
  </c:if>

  <c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
  </c:if>

  <c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
  </c:if>

  <table border="0" cellpadding="0" cellspacing="0" width="75%" align="center" class="container">
    <tr>
      <td colspan="2" class="button">
        <app2:securitySubmit operation="${op}" functionality="WEBDOCUMENT" property="save"
                             styleClass="button" tabindex="13">
          ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button" tabindex="15">
          <fmt:message key="Common.cancel"/>
        </html:cancel>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="title">
        <c:out value="${title}"/>
      </td>
    </tr>
    <tr>
      <td class="label" width="16%">
        <fmt:message key="WebDocument.name"/>
      </td>
      <td class="contain" width="84%">
        <app:text property="dto(name)" styleClass="largetext" maxlength="100" view="${'delete' == op}" tabindex="1"/>
      </td>
    </tr>
    <tr>
      <td class="label">
        <fmt:message key="WebDocument.url"/>
      </td>
      <td class="contain">
        <app:text property="dto(url)" styleClass="largetext" maxlength="250" view="${'delete' == op}" tabindex="2"/>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="title">
        <fmt:message key="WebDocument.contactParameters"/>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <tags:webParameters formObject="${webDocumentForm}" variableTypeConstant="${CONTACT_TYPE}" tabIndex="3"/>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="title">
        <fmt:message key="WebDocument.companyParameters"/>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <tags:webParameters formObject="${webDocumentForm}" variableTypeConstant="${COMPANY_TYPE}" tabIndex="4"/>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="title">
        <fmt:message key="WebDocument.employeeParameters"/>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <tags:webParameters formObject="${webDocumentForm}" variableTypeConstant="${EMPLOYEE_TYPE}" tabIndex="5"/>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="button">
        <app2:securitySubmit operation="${op}" functionality="WEBDOCUMENT" property="save"
                             styleClass="button" tabindex="10">
          ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button" tabindex="12">
          <fmt:message key="Common.cancel"/>
        </html:cancel>
      </td>
    </tr>
  </table>

</html:form>