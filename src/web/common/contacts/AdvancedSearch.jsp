<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<script>
    function myReset() {
        var form = document.advancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(active)") {
                form.elements[i].value = "1";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>
<br>
<html:form action="/AdvancedSearch.do">
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
        <tr>
            <td height="20" class="title" colspan="4">
                <fmt:message key="Contact.Title.advancedSearch"/>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <tags:dynamicSearchFilter dynamicSearchName="advancedContactSearch"
                                          customSearchConfigUrl="/common/contacts/AdvancedDynamicSearch.jsp"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="12">
                    <fmt:message key="Common.go"/>
                </html:submit>
                <html:button property="reset1" tabindex="13" styleClass="button" onclick="clearDynamicSearch()">
                    <fmt:message key="Common.clear"/>
                </html:button>
                &nbsp;
            </td>
        </tr>
    </table>
</html:form>



<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td colspan="4">
            <br/>

            <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Person/Forward/Create.do?advancedListForward=ContactAdvancedSearch"
                                 addModuleParams="false" var="newPersonUrl"/>
                        <app:url value="/Organization/Forward/Create.do?advancedListForward=ContactAdvancedSearch"
                                 addModuleParams="false" var="newOrganizationUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Contact.Person.new"/>"
                               onclick="window.location ='${newPersonUrl}'">
                        <input type="button" class="button" value="<fmt:message key="Contact.Organization.new"/>"
                               onclick="window.location='${newOrganizationUrl}'">
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
            <fanta:table list="allAdvancedAddressList" width="100%" id="contact" action="AdvancedSearch.do"
                         imgPath="${baselayout}"
                         align="center">

                <app2:checkAccessRight functionality="CONTACT" permission="VIEW" var="hasViewPermission"/>
                <app2:checkAccessRight functionality="CONTACT" permission="DELETE" var="hasDeletePermission"/>
                <c:set var="personPrefixImageName" value="person-private"/>
                <c:choose>
                    <c:when test="${contact.addressType == personType}">
                        <c:set var="contactPersonLink"
                               value="Person/Forward/Update.do?contactId=${contact.addressId}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0&advancedListForward=ContactAdvancedSearch"/>

                        <c:set var="deleteLink"
                               value="Person/Forward/Delete.do?contactId=${contact.addressId}&dto(withReferences)=true&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0&advancedListForward=ContactAdvancedSearch"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="contactPersonLink"
                               value="Organization/Forward/Update.do?contactId=${contact.addressId}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0&advancedListForward=ContactAdvancedSearch"/>

                        <c:set var="deleteLink"
                               value="Organization/Forward/Delete.do?contactId=${contact.addressId}&dto(withReferences)=true&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0&advancedListForward=ContactAdvancedSearch"/>
                    </c:otherwise>
                </c:choose>

                <c:if test="${not empty contact.contactPersonAddressId}">
                    <c:set var="personPrefixImageName" value="person"/>
                    <c:set var="contactPersonLink"
                           value="ContactPerson/Forward/Update.do?dto(addressId)=${contact.contactPersonAddressId}&dto(contactPersonId)=${contact.addressId}&contactId=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressType2}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&tabKey=Contacts.Tab.contactPersons&advancedListForward=ContactAdvancedSearch"/>
                    <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW" var="hasViewPermission"/>

                    <c:choose>
                        <c:when test="${contact.addressType2 == personType}">
                            <c:set var="editLink"
                                   value="Person/Forward/Update.do?contactId=${contact.contactPersonAddressId}&dto(addressId)=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0&advancedListForward=ContactAdvancedSearch"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="editLink"
                                   value="Organization/Forward/Update.do?contactId=${contact.contactPersonAddressId}&dto(addressId)=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressTypeA1}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0&advancedListForward=ContactAdvancedSearch"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="8%">
                    <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem"
                                        headerStyle="listHeader" image="${baselayout}/img/edit.gif" render="false"
                                        width="33%">
                        <c:choose>
                            <c:when test="${hasViewPermission}">
                                <html:link action="${contactPersonLink}" titleKey="Common.update">
                                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.update"
                                              border="0"/>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>

                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                        <c:if test="${hasDeletePermission}">
                            <c:choose>
                                <c:when test="${contact.addressId != sessionScope.user.valueMap['companyId']}">
                                    <html:link action="${deleteLink}" titleKey="Common.delete">
                                        <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete"
                                                  border="0"/>
                                    </html:link>
                                </c:when>
                                <c:otherwise>
                                    &nbsp;
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </fanta:actionColumn>
                    <app2:checkAccessRight functionality="FAVORITE" permission="CREATE">
                        <fanta:actionColumn name="fav" label="" styleClass="listItem" headerStyle="listHeader"
                                            width="34%">
                            <html:link
                                    page="/AddFavorite.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&advancedListForward=ContactAdvancedSearch"
                                    titleKey="Contact.favorites.add">
                                <html:img
                                        src="${baselayout}/img/${contact.addressType == personType? personPrefixImageName : 'org'}.gif"
                                        titleKey="Contact.favorites.add" border="0"/>
                            </html:link>
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="addressName1" styleClass="listItem" title="Contact.name"
                                  headerStyle="listHeader" width="18%" orderable="true" maxLength="33"
                                  renderData="false">
                    <fanta:textShorter title="${contact.addressName1}">
                        <c:choose>
                            <c:when test="${hasViewPermission && not empty contact.addressName1}">
                                <html:link action="${contactPersonLink}">
                                    <c:out value="${contact.addressName1}"/>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${contact.addressName1}"/>
                            </c:otherwise>
                        </c:choose>
                    </fanta:textShorter>
                </fanta:dataColumn>
                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                    <fanta:dataColumn name="addressName2" styleClass="listItem"
                                      title="ContactPerson.contactName"
                                      headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                        <c:if test="${not empty contact.contactPersonAddressId}">
                            <fanta:textShorter title="${contact.addressName2}">
                                <html:link action="${editLink}">
                                    <c:out value="${contact.addressName2}"/>
                                </html:link>
                            </fanta:textShorter>
                        </c:if>
                    </fanta:dataColumn>
                </app2:checkAccessRight>

                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                  headerStyle="listHeader" width="7%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                                  width="7%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city" headerStyle="listHeader"
                                  width="15%" orderable="true">
                </fanta:dataColumn>
                <fanta:dataColumn name="" styleClass="listItem2Center" title="ContactPerson.telecomNumbers"
                                  headerStyle="listHeader" width="25%" renderData="false">
                    <c:choose>
                        <c:when test="${not empty contact.contactPersonAddressId}">
                            <app:telecomSelect numberColumn="telecomnumber"
                                               addressId="${contact.contactPersonAddressId}"
                                               contactPersonId="${contact.addressId}"
                                               styleClass="middleSelect" optionStyleClass="list" showDescription="true"
                                               groupedByTelecomType="true"/>
                        </c:when>
                        <c:otherwise>
                            <app:telecomSelect numberColumn="telecomnumber" addressId="${contact.addressId}"
                                               contactPersonId=""
                                               styleClass="middleSelect" optionStyleClass="list" showDescription="true"
                                               groupedByTelecomType="true"/>
                        </c:otherwise>
                    </c:choose>
                </fanta:dataColumn>
            </fanta:table>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </td>
    </tr>
</table>


