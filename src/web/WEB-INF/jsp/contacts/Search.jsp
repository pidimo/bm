<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<c:import url="/WEB-INF/jsp/contacts/AvatarImageResizeJSFragment.jsp"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Search.do"
               focus="parameter(contactSearchName)"
               styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="Contact.Title.search"/>
            </legend>

            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(contactSearchName)"
                                   styleClass="${app2:getFormInputClasses()} largeText"/>
                <span class="input-group-btn">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                 </span>
                    </div>

                </div>
                    <%--${app2:getFormLabelOneSearchInput()}--%>
                <div class="pull-left">
                    <html:link action="/AdvancedSearch.do?search=true" styleClass="btn btn-link">
                        <fmt:message key="Common.advancedSearch"/>
                    </html:link>
                </div>
            </div>
        </fieldset>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Search.do" parameterName="name1A1" mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
        <c:set var="newButtonsTable" scope="page">
            <tags:buttonsTable>
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <app:url value="/Person/Forward/Create.do" addModuleParams="false" var="newPersonUrl"/>
                    <app:url value="/Organization/Forward/Create.do" addModuleParams="false"
                             var="newOrganizationUrl"/>
                    <input type="button"
                           class="${app2:getFormButtonClasses()}"
                           value="<fmt:message
                       key="Contact.Organization.new"/>"
                           onclick="window.location='${newOrganizationUrl}'">
                    <input type="button"
                           class="${app2:getFormButtonClasses()}"
                           value="<fmt:message
                       key="Contact.Person.new"/>"
                           onclick="window.location ='${newPersonUrl}'">
                </div>
            </tags:buttonsTable>
        </c:set>
    </app2:checkAccessRight>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="allAddressList"
                     width="100%"
                     id="contact"
                     action="Search.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>

            <app2:checkAccessRight functionality="CONTACT" permission="VIEW" var="hasViewPermission"/>
            <app2:checkAccessRight functionality="CONTACT" permission="DELETE" var="hasDeletePermission"/>
            <c:choose>
                <c:when test="${contact.addressType == personType}">
                    <c:set var="contactPersonLink"
                           value="Person/Forward/Update.do?contactId=${contact.addressId}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>

                    <c:set var="deleteLink"
                           value="Person/Forward/Delete.do?contactId=${contact.addressId}&dto(withReferences)=true&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="contactPersonLink"
                           value="Organization/Forward/Update.do?contactId=${contact.addressId}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>

                    <c:set var="deleteLink"
                           value="Organization/Forward/Delete.do?contactId=${contact.addressId}&dto(withReferences)=true&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>
                </c:otherwise>
            </c:choose>

            <c:if test="${not empty contact.contactPersonAddressId}">
                <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                <c:set var="contactPersonLink"
                       value="ContactPerson/Forward/Update.do?dto(addressId)=${contact.contactPersonAddressId}&dto(contactPersonId)=${contact.addressId}&contactId=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressType2}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&tabKey=Contacts.Tab.contactPersons"/>
                <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW" var="hasViewPermission"/>

                <c:choose>
                    <c:when test="${contact.addressType2 == personType}">
                        <c:set var="editLink"
                               value="Person/Forward/Update.do?contactId=${contact.contactPersonAddressId}&dto(addressId)=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="editLink"
                               value="Organization/Forward/Update.do?contactId=${contact.contactPersonAddressId}&dto(addressId)=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressTypeA1}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>
                    </c:otherwise>
                </c:choose>
            </c:if>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="edit" title="Common.update" styleClass="listItem"
                                    headerStyle="listHeader" glyphiconClass="${app2:getClassGlyphEdit()}" render="false"
                                    width="33%">
                    <c:choose>
                        <c:when test="${hasViewPermission}">
                            <html:link action="${contactPersonLink}" titleKey="Common.update">
                                <span class="${app2:getClassGlyphEdit()}"
                            </html:link>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>
                </fanta:actionColumn>

                <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="33%">
                    <c:if test="${hasDeletePermission}">
                        <c:choose>
                            <c:when test="${contact.addressId != sessionScope.user.valueMap['companyId']}">
                                <html:link action="${deleteLink}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"
                                </html:link>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </fanta:actionColumn>
                <app2:checkAccessRight functionality="FAVORITE" permission="CREATE">
                    <fanta:actionColumn name="fav" label="" styleClass="listItem" headerStyle="listHeader"
                                        width="34%">
                        <html:link
                                page="/AddFavorite.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(userId)=${sessionScope.user.valueMap['userId']}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}"
                                titleKey="Contact.favorites.add">
                            <span class="${contact.addressType == personType ? personPrefixImageName : app2:getClassGlyphOrganization()}"></span>
                        </html:link>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="addressImage" styleClass="listItemCenter" label=""
                              headerStyle="listHeader" width="4%" orderable="false" renderData="false">
                <c:if test="${not empty contact.imageId && contact.addressType == personType}">
                    <html:img page="/DownloadAddressImage.do?dto(freeTextId)=${contact.imageId}"
                              styleClass="listAddressImage" width="30px"/>
                </c:if>
            </fanta:dataColumn>

            <fanta:dataColumn name="addressName1" styleClass="listItem" title="Contact.name"
                              headerStyle="listHeader" width="18%" orderable="true" renderData="false">

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
                                  headerStyle="listHeader" width="18%" orderable="true" renderData="false">
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
                              headerStyle="listHeader" width="10%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"
                              headerStyle="listHeader" width="7%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city" headerStyle="listHeader"
                              width="13%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItem2Center" title="ContactPerson.telecomNumbers"
                              headerStyle="listHeader" width="25%" renderData="false">
                <c:choose>
                    <c:when test="${not empty contact.contactPersonAddressId}">
                        <app:telecomSelect numberColumn="telecomnumber"
                                           addressId="${contact.contactPersonAddressId}"
                                           contactPersonId="${contact.addressId}"
                                           styleClass="${app2:getFormSelectClasses()} middleSelect"
                                           optionStyleClass="list"
                                           showDescription="true"
                                           groupedByTelecomType="true"/>
                    </c:when>
                    <c:otherwise>
                        <app:telecomSelect numberColumn="telecomnumber" addressId="${contact.addressId}"
                                           contactPersonId=""
                                           styleClass="${app2:getFormSelectClasses()} middleSelect"
                                           optionStyleClass="list"
                                           showDescription="true"
                                           groupedByTelecomType="true"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

</div>