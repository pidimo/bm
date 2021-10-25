<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<app2:jScriptUrl url="/contacts/Organization/Forward/Update.do?m=1&index=0&module=contacts" var="jsOrganizationUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(addressId)" value="obj.options[obj.selectedIndex].value"/>
    <app2:jScriptUrlParam param="contactId" value="obj.options[obj.selectedIndex].value"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/contacts/Person/Forward/Update.do?m=1&index=0&module=contacts" var="jsPersonUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(addressId)" value="obj.options[obj.selectedIndex].value"/>
    <app2:jScriptUrlParam param="contactId" value="obj.options[obj.selectedIndex].value"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
    <!--
    function goToUpdate(obj)
    {
        if (obj.options[obj.selectedIndex].value != "") {
            if (obj.options[obj.selectedIndex].id == "<%=ContactConstants.ADDRESSTYPE_ORGANIZATION%>")
                window.location = ${jsOrganizationUrl};
            else if (obj.options[obj.selectedIndex].id == "<%=ContactConstants.ADDRESSTYPE_PERSON%>")
                window.location = ${jsPersonUrl};
        }
    }
    //-->
</script>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="left" class="moduleShortCut" width="35%">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                &nbsp;| <app:link page="/Search.do" addModuleParams="false"><fmt:message key="Common.search"/>
            </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                | <app:link
                    page="/contacts/Organization/Forward/Update.do?contactId=${sessionScope.user.valueMap['companyId']}&index=0&dto(addressId)=${sessionScope.user.valueMap['companyId']}"
                    contextRelative="true" addModuleParams="false"><fmt:message key="Common.myCompany"/></app:link>
            </app2:checkAccessRight>
            <c:if test="${app2:hasAccessRight(pageContext.request,'CONTACT','CREATE')}">
                <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                    | <app:link page="/contacts/DataImport/List.do"
                                contextRelative="true" addModuleParams="false">
                            <fmt:message key="Contacts.import"/>
                       </app:link>

                    <%--| <app:link page="/contacts/Forward/DataImport.do"
                                contextRelative="true" addModuleParams="false">
                            <fmt:message key="Contacts.import"/>
                      </app:link>--%>
                </app2:checkAccessRight>
            </c:if>
            <app2:checkAccessRight functionality="COMMUNICATIONOVERVIEW" permission="VIEW">
                | <app:link page="/contacts/Communication/overviewSearch/List.do"
                            contextRelative="true" addModuleParams="false">
                    <fmt:message key="Contacts.communication.overview"/>
                  </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
                | <app:link page="/contacts/DedupliContact/List.do"
                            contextRelative="true" addModuleParams="false">
                        <fmt:message key="Contacts.checkDuplicates"/>
                   </app:link>
            </app2:checkAccessRight>

            |
        </td>

        <app2:checkAccessRight functionality="FAVORITE" permission="VIEW">
            <td align="right" class="moduleShortCut" nowrap="nowrap">
                <fmt:message key="Contact.favorites"/>
            </td>
            <td align="left" class="moduleShortCut" nowrap="nowrap">
                <fanta:select property="dto(addressId)" listName="favoriteList" firstEmpty="true"
                              labelProperty="addressName" valueProperty="addressId" styleClass="mediumSelect"
                              onChange="goToUpdate(this)" optionId="addressType" optionStyleClass="option">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                </fanta:select>
                <app:link
                        action="/Favorite/Forward/Update.do?dto(op)=update&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                        titleKey="Contact.favorites.delete">
                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/edit.gif"
                         alt="<fmt:message   key="Contact.favorites.delete"/>" border="0" align="middle">
                </app:link>
            </td>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
            <td align="right" class="moduleShortCut" nowrap="nowrap">
                <fmt:message key="Contact.recents"/>
            </td>
            <td align="left" class="moduleShortCut" nowrap="nowrap">
                <fanta:select property="dto(addressId)" listName="recentList" firstEmpty="true"
                              labelProperty="addressName" valueProperty="addressId" styleClass="mediumSelect"
                              onChange="goToUpdate(this)" optionId="addressType" optionStyleClass="option">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                </fanta:select>
            </td>
        </app2:checkAccessRight>
        <td align="right" class="moduleShortCut">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:pullDownMenuItem action="/Report/ContactList.do" titleKey="Contact.Report.ContactList"
                                       functionality="CONTACT" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/ContactPersonList.do" titleKey="Contact.Report.ContactPersonList"
                                       functionality="CONTACTPERSON" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/DepartmentList.do" titleKey="Contact.Report.DepartmentList"
                                       functionality="DEPARTMENT" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/CustomerList.do" titleKey="Contact.Report.CustomerList"
                                       functionality="CUSTOMER" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/SupplierList.do" titleKey="Contact.Report.SupplierList"
                                       functionality="SUPPLIER" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/EmployeeList.do" titleKey="Contact.Report.EmployeeList"
                                       functionality="EMPLOYEE" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/CommunicationList.do" titleKey="Contact.Report.CommunicationList"
                                       functionality="COMMUNICATION" permission="VIEW"/>

                <tags:pullDownMenuItem
                        action="/Report/ContactSingleList.do?parameter(addressId_Selected)=${param.contactId}&parameter(contactPersonId_selected)=${param['dto(contactPersonId)']}"
                        titleKey="Contact.Report.ContactSingleReport"
                        functionality="CONTACT" permission="VIEW"/>
                <tags:reportsMenu module="contacts"/>
            </tags:pullDownMenu>
        </td>
    </tr>
</table>