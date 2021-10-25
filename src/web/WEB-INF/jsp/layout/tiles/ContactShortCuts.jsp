<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<app2:jScriptUrl url="/contacts/Organization/Forward/Update.do?m=1&index=0&module=contacts" var="jsOrganizationUrl" addModuleParams="false">
  <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
  <app2:jScriptUrlParam param="contactId" value="addressId"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/contacts/Person/Forward/Update.do?m=1&index=0&module=contacts" var="jsPersonUrl" addModuleParams="false">
  <app2:jScriptUrlParam param="dto(addressId)" value="addressId"/>
  <app2:jScriptUrlParam param="contactId" value="addressId"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
  <!--
  function goToUpdate(addressId, addressType)
  {
    if (addressId != "") {
      if (addressType == "<%=ContactConstants.ADDRESSTYPE_ORGANIZATION%>")
        window.location = ${jsOrganizationUrl};
      else if (addressType == "<%=ContactConstants.ADDRESSTYPE_PERSON%>")
        window.location = ${jsPersonUrl};
    }
  }
  //-->
</script>


<ul class="dropdown-menu">

  <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
    <li>
      <app:link page="/contacts/Search.do" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Common.search"/>
      </app:link>
    </li>
  </app2:checkAccessRight>

  <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
    <li>
      <app:link page="/contacts/AdvancedSearch.do?search=true" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Common.advancedSearch"/>
      </app:link>
    </li>
  </app2:checkAccessRight>

  <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
    <li>
      <app:link page="/contacts/Organization/Forward/Update.do?contactId=${sessionScope.user.valueMap['companyId']}&index=0&dto(addressId)=${sessionScope.user.valueMap['companyId']}"
                contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Common.myCompany"/>
      </app:link>
    </li>
  </app2:checkAccessRight>

  <c:if test="${app2:hasAccessRight(pageContext.request,'CONTACT','CREATE')}">
    <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
      <li>
        <app:link page="/contacts/DataImport/List.do"
                  contextRelative="true" addModuleParams="false" addModuleName="false">
          <fmt:message key="Contacts.import"/>
        </app:link>
      </li>
    </app2:checkAccessRight>
  </c:if>

  <app2:checkAccessRight functionality="COMMUNICATIONOVERVIEW" permission="VIEW">
    <li>
      <app:link page="/contacts/Communication/overviewSearch/List.do"
                contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Contacts.communication.overview"/>
      </app:link>
    </li>
  </app2:checkAccessRight>

  <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
    <li>
      <app:link page="/contacts/DedupliContact/List.do"
                contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Contacts.checkDuplicates"/>
      </app:link>
    </li>
  </app2:checkAccessRight>

  <%--favorites--%>
  <app2:checkAccessRight functionality="FAVORITE" permission="VIEW">
    <li role="separator" class="divider"></li>
    <li>
      <a href="#">
        <fmt:message key="Contact.favorites"/>
        <span class="caret"></span>
      </a>
      <ul class="dropdown-menu">

        <li>
          <app:link action="/contacts/Favorite/Forward/Update.do?dto(op)=update&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
            <fmt:message key="Contact.favorites.delete"/>
          </app:link>
        </li>

        <li role="separator" class="divider"></li>

        <fanta:list listName="favoriteList" module="/contacts">
          <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
          <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
        </fanta:list>

        <c:if test="${not empty favoriteList.result}">
          <c:forEach var="item" items="${favoriteList.result}">
            <li>
              <a href="#" onclick="goToUpdate(${item.addressId}, ${item.addressType})">
                <c:out value="${item.addressName}"/>
              </a>
            </li>
          </c:forEach>
        </c:if>
      </ul>
    </li>
  </app2:checkAccessRight>

  <%--recents--%>
  <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
    <li role="separator" class="divider"></li>
    <li>
      <a href="#">
        <fmt:message key="Contact.recents"/>
        <span class="caret"></span>
      </a>
      <ul class="dropdown-menu">

        <fanta:list listName="recentList" module="/contacts">
          <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
          <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
        </fanta:list>

        <c:if test="${not empty recentList.result}">
          <c:forEach var="item" items="${recentList.result}">
            <li>
              <a href="#" onclick="goToUpdate(${item.addressId}, ${item.addressType})">
                <c:out value="${item.addressName}"/>
              </a>
            </li>
          </c:forEach>
        </c:if>
      </ul>
    </li>
  </app2:checkAccessRight>

  <%--reports--%>
  <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
    <tags:bootstrapMenuItem action="/contacts/Report/ContactList.do" contextRelative="true" titleKey="Contact.Report.ContactList"
                           functionality="CONTACT" permission="VIEW"/>
    <tags:bootstrapMenuItem action="/contacts/Report/ContactPersonList.do" contextRelative="true" titleKey="Contact.Report.ContactPersonList"
                           functionality="CONTACTPERSON" permission="VIEW"/>
    <tags:bootstrapMenuItem action="/contacts/Report/DepartmentList.do" contextRelative="true" titleKey="Contact.Report.DepartmentList"
                           functionality="DEPARTMENT" permission="VIEW"/>
    <tags:bootstrapMenuItem action="/contacts/Report/CustomerList.do" contextRelative="true" titleKey="Contact.Report.CustomerList"
                           functionality="CUSTOMER" permission="VIEW"/>
    <tags:bootstrapMenuItem action="/contacts/Report/SupplierList.do" contextRelative="true" titleKey="Contact.Report.SupplierList"
                           functionality="SUPPLIER" permission="VIEW"/>
    <tags:bootstrapMenuItem action="/contacts/Report/EmployeeList.do" contextRelative="true" titleKey="Contact.Report.EmployeeList"
                           functionality="EMPLOYEE" permission="VIEW"/>
    <tags:bootstrapMenuItem action="/contacts/Report/CommunicationList.do" contextRelative="true" titleKey="Contact.Report.CommunicationList"
                           functionality="COMMUNICATION" permission="VIEW"/>

    <tags:bootstrapMenuItem
            action="/contacts/Report/ContactSingleList.do?parameter(addressId_Selected)=${param.contactId}&parameter(contactPersonId_selected)=${param['dto(contactPersonId)']}"
            contextRelative="true"
            titleKey="Contact.Report.ContactSingleReport"
            functionality="CONTACT" permission="VIEW"/>

    <tags:bootstrapReportsMenu module="contacts"/>
  </tags:bootstrapMenu>

</ul>
