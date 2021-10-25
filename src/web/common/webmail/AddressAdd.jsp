<%@ page import="com.piramide.elwis.utils.ContactConstants"%>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

<tags:jscript language="JavaScript" src="/js/webmail/addressAdd.jsp"/>

<c:set var="ADDRESSTYPE_PERSON" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="ADDRESSTYPE_ORGANIZATION" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>"/>

<%--set focus--%>
<c:set var="addr" value="${addressData}"/>
<c:choose>
<c:when test="${addr.isUpdate == 'true'}">
<c:set var="focusAddress" value="dto(email)"/>
</c:when>
<c:otherwise>
<c:choose>
<c:when test="${ addr.typeAddress == ADDRESSTYPE_PERSON}">  <!-- address type is person -->
    <c:set var="focusAddress" value="dto(PerName1)"/>
</c:when>
<c:when test="${ addr.typeAddress == ADDRESSTYPE_ORGANIZATION}">  <!-- address type is Organization -->
    <c:set var="focusAddress" value="dto(OrgName1)"/>
</c:when>
<c:otherwise>
    <c:set var="focusAddress" value="dto(PerName1)"/>
</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>

<html:form action="${action}?dto(mailGroupAddrId)=${param['dto(mailGroupAddrId)']}" focus="${focusAddress}" >

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<html:hidden property="dto(userMailId)" value="${sessionScope.user.valueMap['userId']}"/>
<html:hidden property="dto(addressId)" />
<html:hidden property="dto(contactPersonId)" />
<html:checkbox property="dto(isUpdate)" style="visibility:hidden;position:absolute"/>
<%-- data of contact groups  --%>
<html:hidden property="dto(mailGroupAddrId)"/>

<c:choose>
<c:when test="${addr.isUpdate == 'true'}" >
    <c:set var="displayEdit" value=""/>
    <c:set var="displayPer" value="display:none"/>
    <c:set var="displayOrg" value="display:none"/>
    <c:set var="displaySelect" value="display:none"/>
</c:when>
<c:otherwise>
    <c:choose>
        <c:when test="${ addr.typeAddress == ADDRESSTYPE_PERSON}">  <!-- address type is person -->
            <c:set var="displayEdit" value="display:none"/>
            <c:set var="displayPer" value=""/>
            <c:set var="displayOrg" value="display:none"/>
            <c:set var="displaySelect" value=""/>
        </c:when>
        <c:when test="${ addr.typeAddress == ADDRESSTYPE_ORGANIZATION}">  <!-- address type is Organization -->
            <c:set var="displayEdit" value="display:none"/>
            <c:set var="displayPer" value="display:none"/>
            <c:set var="displayOrg" value=""/>
            <c:set var="displaySelect" value=""/>
        </c:when>
        <c:otherwise>
            <c:set var="displayEdit" value="display:none"/>
            <c:set var="displayPer" value=""/>
            <c:set var="displayOrg" value="display:none"/>
            <c:set var="displaySelect" value=""/>
        </c:otherwise>
    </c:choose>
</c:otherwise>
</c:choose>

<table id="AddressSent.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
  <tr  class="listRow">
         <td>
             <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                   <tr>
                       <td colspan="2" class="title">
                           <fmt:message key="WebMail.contact.new"/>
                       </td>
                   </tr>
                   <tr id="trSelect" style="${displaySelect}">
                      <td class="label" ><fmt:message key="Common.select"/></td>
                      <td class="label">
                          <html:select property="dto(addressType)"  styleClass="select" onkeyup="javascript:hiddenTable()" onchange="javascript:hiddenTable()" tabindex="1" >
                               <html:option  value="${ADDRESSTYPE_PERSON}" > <fmt:message key="Contact.Person.new"/> </html:option>
                               <html:option  value="${ADDRESSTYPE_ORGANIZATION}" > <fmt:message key="Contact.Organization.new"/> </html:option>
                          </html:select>
                      </td>
                   </tr>
                   <tr>
                         <td colspan="2" >
                               <table id="tablePerson" style="${displayPer}"  border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                                  <tr class="listRow">
                                     <td colspan="2" class="listItem">
                                         <fmt:message key="Contact.Person.new"/>
                                     </td>
                                  </tr>
                                  <tr>
                                      <td class="label" width="28%" ><fmt:message   key="Contact.Person.lastname"/></td>
                                      <TD class="contain">
                                         <app:text property="dto(PerName1)" styleClass="middleText" maxlength="40" tabindex="2" />
                                         <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                            <%--<tags:selectPopup url="/webmail/Mail/Forward/AddressImportListForUpdate.do"  name="selectPopup" titleKey="Webmail.Contact.searchContactsOrContactPerson" width="755" heigth="480"/>--%>
                                         </app2:checkAccessRight>
                                      </TD>
                                  </tr>
                                  <tr>
                                      <TD class="label" ><fmt:message   key="Contact.Person.firstname"/></TD>
                                      <TD class="contain">
                                         <app:text property="dto(PerName2)" styleClass="middleText" maxlength="40" tabindex="3" />
                                         <html:hidden property="dto(PerName3)" styleId="PerName3_Id"/>
                                      </TD>
                                  </tr>
                                  <tr>
                                      <TD class="label"><fmt:message   key="Contact.Person.searchName"/></TD>
                                      <TD class="contain">
                                          <app:text property="dto(PerSearchName)" styleClass="middleText" maxlength="60" tabindex="4" />
                                      </TD>
                                  </tr>
                               </table>

                               <table id="tableOrg" style="${displayOrg}" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                                   <tr class="listRow">
                                       <td colspan="2" class="listItem">
                                            <fmt:message key="Contact.Organization.new"/>
                                       </td>
                                   </tr>
                                   <tr>
                                       <TD class="topLabel" width="28%" rowspan="3"><fmt:message   key="Contact.Organization.name"/></TD>
                                       <TD class="contain" >
                                         <app:text property="dto(OrgName1)" styleClass="middleText" maxlength="40" tabindex="2" />
                                         <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                            <%--<tags:selectPopup url="/webmail/Mail/Forward/AddressImportListForUpdate.do"  name="selectPopup" titleKey="Webmail.Contact.searchContactsOrContactPerson" width="755" heigth="480"/>--%>
                                         </app2:checkAccessRight>
                                       </TD>
                                   </tr>
                                   <tr>
                                       <TD class="contain">
                                           <app:text property="dto(OrgName2)" styleClass="middleText" maxlength="40" tabindex="3" />
                                       </TD>
                                   </tr>
                                   <tr>
                                       <TD class="contain">
                                           <app:text property="dto(OrgName3)" styleClass="middleText" maxlength="40" tabindex="4" />
                                       </TD>
                                   </tr>
                                   <tr>
                                       <TD class="label" ><fmt:message   key="Contact.Organization.searchName"/></TD>
                                       <TD class="contain">
                                          <app:text property="dto(OrgSearchName)" styleClass="middleText" maxlength="60" tabindex="5" />
                                       </TD>
                                   </tr>
                               </table>

                               <table id="tableEdit" style="${displayEdit}"  border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                                  <tr class="listRow">
                                     <td colspan="2" class="listItem">
                                         <fmt:message key="Webmail.Address.addInContact"/>
                                     </td>
                                  </tr>
                                  <tr>
                                      <TD class="label" width="28%"><fmt:message   key="Common.name"/></TD>
                                      <TD class="contain">
                                         <app:text property="dto(EditName)" styleClass="middleText" maxlength="60" tabindex="2"  readonly="true"/>
                                         <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                            <%--<tags:selectPopup url="/webmail/Mail/Forward/AddressImportListForUpdate.do"  name="selectPopup" titleKey="Webmail.Contact.searchContactsOrContactPerson" width="755" heigth="480"/>
                                            <a href="javascript:clearContactSelectPopup()" title="<fmt:message key="Common.clear"/>" ><img src="${pageContext.request.contextPath}/layout/ui/img/clear.gif" border="0"></a>--%>
                                         </app2:checkAccessRight>
                                      </TD>
                                  </tr>
                               </table>

                         </td>
                  </tr>
                  <tr>
                      <td class="label" width="28%"  ><fmt:message key="Common.email"/></td>
                      <td class="contain" >
                           <app:text property="dto(email)" styleClass="middleText" maxlength="80" tabindex="7" />
                      </td>
                  </tr>
                  <tr>
                      <td class="label"><fmt:message key="Telecom.telecomType"/></td>
                      <td class="contain" >
                          <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                              <html:select property="dto(telecomTypeId)" styleClass="shortSelect" tabindex="8"  >
                                  <html:option value="">&nbsp;</html:option>
                                  <html:options collection="telecomTypes"  property="value" labelProperty="label"/>
                              </html:select>
                      </td>
                  </tr>

             </table>
         </td>
   </tr>
</table>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
<TR>
   <TD class="button">
      <app2:securitySubmit operation="create" functionality="CONTACT" property="dto(save)" styleClass="button" tabindex="20" ><c:out value="${button}"/></app2:securitySubmit>
      <html:submit property="dto(cancel)" styleClass="button" tabindex="21" ><fmt:message   key="Common.cancel"/></html:submit>
  </TD>
</TR>
</table>

</html:form>

