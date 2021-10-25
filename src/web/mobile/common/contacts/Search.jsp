<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>
<html:form action="/contacts/Search" method="post">

    <div class="label">
        <fmt:message key="Common.search"/>:
    </div>
    <div class="content">
        <html:text property="parameter(contactSearchName)"/>
    </div>
    <div class="buttons">
        <input type="submit" value="<fmt:message key="Common.go"/>"/>
    </div>

    <div class="content">
        <fanta:table action="/contacts/Search.do" imgPath="${baselayout}" enableRowLighting="false"
                     styleClass="list" id="contact"
                     textShortening="false" showPagination="true" addDefaultWidth="false"
                     addDefaultAlign="false" renderSimplePaginator="true" rowEvenStyleClass="rowEven"
                     rowOddStyleClass="rowOdd">
            <c:choose>
                <c:when test="${contact.addressType2 == personType}">
                    <c:set var="viewLink"
                           value="contacts/Person/Forward/View.do?contactId=${contact.contactPersonAddressId}&dto(addressId)=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="viewLink"
                           value="contacts/Organization/Forward/View.do?contactId=${contact.contactPersonAddressId}&dto(addressId)=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressTypeA1}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&index=0"/>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${contact.addressName1 == contact.addressName2 ||  empty contact.contactPersonAddressId }">
                    <c:if test="${contact.addressType == personType}">
                        <c:set var="contactPersonLink"
                               value="contacts/Person/Forward/View.do?contactId=${contact.addressId}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}"/>
                    </c:if>
                    <c:if test="${contact.addressType == organizationType}">
                        <c:set var="contactPersonLink"
                               value="contacts/Organization/Forward/View.do?contactId=${contact.addressId}&dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}"/>
                    </c:if>
                    <app2:checkAccessRight functionality="CONTACT" permission="VIEW" var="hasViewPermission"/>
                </c:when>
                <c:otherwise>
                    <c:set var="contactPersonLink"
                           value="contacts/ContactPerson/Forward/View.do?dto(addressId)=${contact.contactPersonAddressId}&dto(contactPersonId)=${contact.addressId}&contactId=${contact.contactPersonAddressId}&dto(addressType)=${contact.addressType2}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}"/>
                    <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW" var="hasViewPermission"/>
                </c:otherwise>
            </c:choose>

            <fanta:dataColumn name="" styleClass="listItem" title="Contact.name"
                              headerStyle="listHeader" width="100%" orderable="false" maxLength="40" renderData="false">
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

                <c:if test="${(contact.addressName1 != contact.addressName2) && not empty contact.addressName2}">
                    <c:out value="("/>
                    <html:link action="${viewLink}">
                        <c:out value="${contact.addressName2}"/>
                    </html:link>
                    <c:out value=")"/>
                </c:if>

            </fanta:dataColumn>

        </fanta:table>

    </div>

</html:form>


