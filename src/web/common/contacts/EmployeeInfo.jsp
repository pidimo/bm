<%@ include file="/Includes.jsp"%>
<calendar:initialize/>
<tags:initSelectPopup/>

<c:set var="id" value="${employeeForm.dtoMap['employeeId']}"/>
<table width="90%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="left">
      <html:form action="${action}" focus="dto(initials)" >
      <fmt:message var="datePattern" key="datePattern"/>
     <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr >
          <td class="button">
            <!-- Buttons -->
            <c:choose>
              <c:when test="${id != sessionScope.user.valueMap['userAddressId']}">
                <app2:securitySubmit operation="update" functionality="EMPLOYEE" styleClass="button" property="dto(save)" tabindex="1" >
                    <c:out value="${button}"/></app2:securitySubmit>
                <app2:securitySubmit operation="delete" functionality="EMPLOYEE" styleClass="button" property="dto(delete)" tabindex="2" >
                    <fmt:message  key="Common.delete"/></app2:securitySubmit>
              </c:when>
               <c:otherwise>
                <app2:securitySubmit operation="update" functionality="EMPLOYEE" styleClass="button" property="dto(save)" tabindex="1" ><c:out value="${button}"/></app2:securitySubmit>
               </c:otherwise>
              </c:choose>

           </td>
        </tr>
      </table>

    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <c:choose>
          <c:when test="${op == null || op == '' }" >
            <html:hidden property="dto(op)" value="update"/>
        </c:when >
        <c:otherwise>
            <html:hidden property="dto(op)" value="${op}"/>
          </c:otherwise>
        </c:choose>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
        <html:hidden property="dto(name1)" value="${name1}" />
        <html:hidden property="dto(name2)" value="${name2}"/>
        <html:hidden property="dto(employeeId)"/>
        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}" />
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(fromInfo)" value="ok" />
        <html:hidden property="dto(userTypeValue)" value="1" />

        <tr >
          <td colspan="4" class="title"><c:out value="${title}"/> </td>
        </tr>
        <tr>
           <td class="label" ><fmt:message   key="Contact.Organization.Employee.initials" /></td>
          <td  class="contain">
             <app:text property="dto(initials)"  styleClass="mediumText" maxlength="20" tabindex="3" view="${op == 'delete'}"  />
          </td>
          <td width="15%" class="label"><fmt:message   key="Contact.Organization.Employee.socialSecure"/>  </td>
          <td width="35%" class="contain">
            <app:text property="dto(socialSecurityNumber)" view="${op == 'delete'}" styleClass="mediumText" maxlength="20" tabindex="11" />
           </td>
         </tr>
        <tr>
          <td width="18%" class="label"><fmt:message   key="Contact.Organization.Employee.function"/>  </td>
          <td width="32%" class="contain">
               <app:text property="dto(function)" styleClass="mediumText" view="${op == 'delete'}" maxlength="20" tabindex="4" />
          </td>
          <td class="label"><fmt:message key="Contact.Organization.Employee.healthFund"/></td>
           <td class="contain" nowrap>
               <!-- search Health Fundations-->
               <html:hidden property="dto(healthFundId)" styleId="fieldAddressId_id" />
               <app:text property="dto(healthFundName)" styleClass="mediumText" maxlength="40" view="${op == 'delete'}"
                         tabindex="15"  styleId="fieldAddressName_id"  readonly="true" />
              <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                    <tags:selectPopup url="/contacts/OrganizationSearch.do?allowCreation=0" name="organizationSearchList"
                                 titleKey="Common.search" hide="${op == 'delete' || dto.status == '1'}"/>
                    <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                 titleKey="Common.clear" hide="${op == 'delete' || dto.status == '1'}"/>
              </app2:checkAccessRight>

          </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.department"/>  </td>
          <td class="contain">
             <!-- Collection departments-->
             <fanta:select property="dto(departmentId)" listName="departmentBaseList" firstEmpty="true" labelProperty="name" valueProperty="departmentId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="5">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>

           </td>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.costCenter"/>  </td>
          <td class="contain">
              <!-- Collection costcenters-->
              <fanta:select property="dto(costCenterId)" listName="costCenterBaseList" firstEmpty="true" labelProperty="name" valueProperty="id" styleClass="mediumSelect" module="/catalogs" readOnly="${op == 'delete'}" tabIndex="14">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>

           </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.office"/>  </td>
          <td class="contain">
             <!-- Collection offices-->
            <fanta:select property="dto(officeId)" listName="officeBaseList" firstEmpty="true" labelProperty="name" valueProperty="officeId" styleClass="mediumSelect" readOnly="${op == 'delete'}" tabIndex="6">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>

           </td>
            <td class="label"><fmt:message   key="Contact.Organization.Employee.costPosition"/>  </td>
             <td class="contain">
             <app:numberText property="dto(costPosition)" styleClass="mediumText" maxlength="14" view="${'delete' == op}" numberType="decimal" tabindex="15" maxInt="11" maxFloat="2"/>
           </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.hireDate"/>  </td>
          <td class="contain">
              <app:dateText property="dto(hireDate)" styleId="hireDate" calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}" styleClass="mediumText" view="${op == 'delete'}" maxlength="10"
                      tabindex="7"/>
         </td>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.costHour"/>  </td>
          <td class="contain">
             <app:numberText property="dto(costHour)" styleClass="mediumText" maxlength="14" view="${'delete' == op}" numberType="decimal" tabindex="16" maxInt="11" maxFloat="2"/>
           </td>
         </tr>
         <tr>
          <td class="label"><fmt:message   key="Contact.Organization.Employee.endDate"/>  </td>
          <td  class="contain">
              <app:dateText property="dto(dateEnd)" styleId="dateEnd" calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}" styleClass="mediumText" view="${op == 'delete'}" maxlength="10"
                      tabindex="9"/>
          </td>
           <td width="20%" class="label"><fmt:message   key="Contact.Organization.Employee.hourlyRate"/>  </td>
           <td width="30%" class="contain">
             <app:numberText property="dto(hourlyRate)" styleClass="mediumText" maxlength="14" view="${'delete' == op}" numberType="decimal" tabindex="17"  maxInt="11" maxFloat="2"/>
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

                <c:choose>
                   <c:when test="${id != sessionScope.user.valueMap['userAddressId']}">
                      <app2:securitySubmit operation="update" functionality="EMPLOYEE" styleClass="button" property="dto(save)" tabindex="18" ><c:out value="${button}"/></app2:securitySubmit>
                      <app2:securitySubmit operation="delete" functionality="EMPLOYEE" styleClass="button" property="dto(delete)" tabindex="19" ><fmt:message   key="Common.delete"/></app2:securitySubmit>
                   </c:when>
                   <c:otherwise>
                     <app2:securitySubmit operation="update" functionality="EMPLOYEE" styleClass="button" property="dto(save)" tabindex="20" ><c:out value="${button}"/></app2:securitySubmit>
                  </c:otherwise>
               </c:choose>

          </td>
        </tr>
      </table>
      </html:form>
    </td>
  </tr>
</table>