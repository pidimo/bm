<%@ include file="/Includes.jsp"%>
<calendar:initialize/>
<tags:initSelectPopup/>

<c:set var="newEmployee" value="${employeeForm.dtoMap['newEmployee']}"/>
<c:set var="importEmployee" value="${employeeForm.dtoMap['importEmployee']}"/>
<table width="90%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="left">
    <html:form action="${action}" focus="dto(initials)" >
    <fmt:message var="datePattern" key="datePattern"/>

     <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
              <app2:securitySubmit operation="${op}" functionality="EMPLOYEE" styleClass="button" property="dto(save)" tabindex="1" ><c:out value="${button}"/></app2:securitySubmit>
              <html:cancel styleClass="button" tabindex="2" ><fmt:message   key="Common.cancel"/></html:cancel>

              <%--top links--%>
              &nbsp;
              <c:if test="${('update' == op)}">
                   <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                       <html:link styleClass="folderTabLink" page="/Person/Forward/Update.do?contactId=${employeeForm.dtoMap['employeeId']}&index=0&dto(addressId)=${employeeForm.dtoMap['employeeId']}&dto(sourceEmployeeId)=${employeeForm.dtoMap['employeeId']}&dto(onlyViewDetail)=true">
                       <img src="<c:url value="${layout}/img/edit.gif"/>" alt="<fmt:message    key="ContactPerson.personalInfo"/>" border="0" tabindex="3"/>
                       <fmt:message   key="ContactPerson.personalInfo"/>
                       </html:link>
                   </app2:checkAccessRight>
              </c:if>
          </td>
        </tr>
      </table>
    <table id="Employee.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}" />
        <html:hidden property="dto(userTypeValue)" value="1"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(employeeId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${('create' == op)}">
            <html:hidden property="dto(employeeAddresId)" />

         </c:if>

        <tr >
          <td colspan="4" class="title"><c:out value="${title}"/> </td>
        </tr>
        <tr>
           <td width="15%" class="label"  ><fmt:message   key="Contact.Person.lastname"/>  </td>
           <td width="35%" class="contain">
               <app:text property="dto(name1)" styleClass="mediumText" maxlength="60" tabindex="4" view="true"/>
           </td>
           <td width="15%" class="label"><fmt:message   key="Contact.Organization.Employee.function"/>  </td>
           <td width="35%" class="contain">
               <app:text property="dto(function)" view="${op == 'delete'}" styleClass="mediumText" maxlength="40" tabindex="13" />
           </td>
       </tr>
       <tr>
          <td class="label" ><fmt:message   key="Contact.Person.firstname" /></td>
          <td  class="contain">
               <app:text property="dto(name2)" styleClass="mediumText" maxlength="60" tabindex="5" view="true"/>
          </td>
          <td class="label" rowspan="1"><fmt:message   key="Contact.Organization.Employee.socialSecure"/>  </td>
          <td class="contain" rowspan="1">
             <app:text property="dto(socialSecurityNumber)" view="${op == 'delete'}" styleClass="mediumText" maxlength="20" tabindex="14" />
          </td>

        </tr>
        <tr>
          <td class="label" ><fmt:message   key="Contact.Organization.Employee.initials" /></td>
          <td  class="contain">
             <app:text property="dto(initials)"  view="${op == 'delete'}" styleClass="mediumText" maxlength="10" tabindex="6"  />
          </td>
           <td class="label"><fmt:message   key="Contact.Organization.Employee.healthFund"/>  </td>
           <td class="contain" nowrap>
              <!-- search Health Fundations-->
               <html:hidden property="dto(healthFundId)" styleId="fieldAddressId_id" />

               <app:text property="dto(healthFundName)" styleClass="mediumText" maxlength="40" view="${op == 'delete'}"
                         tabindex="15"  styleId="fieldAddressName_id" readonly="true"/>

                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">

                    <tags:selectPopup url="/contacts/OrganizationSearch.do?allowCreation=0" name="organizationSearchList"
                                 titleKey="Common.search" hide="${op == 'delete'}" />

                    <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                 titleKey="Common.clear" hide="${op == 'delete'}"/>
                </app2:checkAccessRight>

           </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.department"/>  </td>
          <td class="contain">
             <!-- Collection departments-->
             <fanta:select property="dto(departmentId)" listName="departmentBaseList" firstEmpty="true" labelProperty="name" valueProperty="departmentId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="7">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                <fanta:parameter field="addressId" value="${not empty param.contactId?param.contactId:0}"/>
            </fanta:select>
          </td>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.costCenter"/>  </td>
          <td class="contain">
              <!-- Collection costcenters-->
            <fanta:select property="dto(costCenterId)" listName="costCenterBaseList" firstEmpty="true" labelProperty="name" valueProperty="id" styleClass="mediumSelect" module="/catalogs" readOnly="${op == 'delete'}" tabIndex="17">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>

           </td>
          </tr>
          <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.office"/>  </td>
          <td class="contain">
             <!-- Collection offices-->
             <fanta:select property="dto(officeId)" listName="officeBaseList" firstEmpty="true" labelProperty="name" valueProperty="officeId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="8">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                <fanta:parameter field="addressId" value="${not empty param.contactId?param.contactId:0}"/>
            </fanta:select>
           </td>
           <td class="label"><fmt:message key="Contact.Organization.Employee.costPosition"/>  </td>
          <td class="contain">
             <app:numberText property="dto(costPosition)" styleClass="mediumText" maxlength="14" view="${'delete' == op}" numberType="decimal" tabindex="18"  maxInt="11" maxFloat="2"  />
          </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.hireDate"/>  </td>
           <td class="contain">
               <app:dateText property="dto(hireDate)" styleId="hireDate" calendarPicker="${op != 'delete'}"
                       datePatternKey="${datePattern}" styleClass="mediumText" view="${op == 'delete'}" maxlength="10"
                       tabindex="9"/>
          </td>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.costHour"/>  </td>
          <td class="contain">
             <app:numberText property="dto(costHour)" styleClass="mediumText" maxlength="14" view="${'delete' == op}" numberType="decimal" tabindex="19" maxInt="11" maxFloat="2" />
           </td>
         </tr>
         <tr>
           <td class="label"><fmt:message   key="Contact.Organization.Employee.endDate"/>  </td>
           <td  class="contain">
               <app:dateText property="dto(dateEnd)" styleId="dateEnd" calendarPicker="${op != 'delete'}"
                       datePatternKey="${datePattern}" styleClass="mediumText" view="${op == 'delete'}" maxlength="10"
                       tabindex="11"/>
           </td>
           <td width="20%" class="label"><fmt:message   key="Contact.Organization.Employee.hourlyRate"/>  </td>
           <td width="30%" class="contain">
             <app:numberText property="dto(hourlyRate)" styleClass="mediumText" maxlength="14" view="${'delete' == op}" numberType="decimal" tabindex="20"  maxInt="11" maxFloat="2" />
           </td>
         </tr>
         <tr>
            <td colspan="4" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
         </tr>
      </table>
	  <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
              <app2:securitySubmit operation="${op}" functionality="EMPLOYEE" styleClass="button" property="dto(save)"  tabindex="21" ><c:out value="${button}"/></app2:securitySubmit>
              <html:cancel styleClass="button" tabindex="22" ><fmt:message   key="Common.cancel"/></html:cancel>
          </td>
        </tr>
      </table>
      </html:form>
    </td>
  </tr>
</table>