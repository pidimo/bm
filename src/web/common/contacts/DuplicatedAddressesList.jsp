<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %></c:set>


<c:if test="${duplicatedAddressesList != null}">


    <TABLE id="DuplicatedAddressesList.jsp" border="0" cellpadding="0" cellspacing="0" width="800" class="container"
           align="center">
        <TR align="center">
            <TD class="listHeader" width="50%"><fmt:message key="Contact.name"/></TD>
            <TD class="listHeader" width="10%" nowrap><fmt:message key="Contact.countryCode"/></TD>
            <TD class="listHeader" width="10%"><fmt:message key="Contact.zip"/></TD>
            <TD class="listHeader" width="15%"><fmt:message key="Contact.city"/></TD>
            <TD class="listHeader" width="15%"><fmt:message key="Contact.street"/></TD>
        </TR>
        <c:forEach var="contact" items="${duplicatedAddressesList}">
            <c:choose>
                <c:when test="${contact.addressType == personType}">
                    <c:set var="updateAction" value="/Person/Forward/Update.do"/>
                </c:when>
                <c:otherwise>
                    <c:set var="updateAction" value="/Organization/Forward/Update.do"/>
                </c:otherwise>
            </c:choose>
            <TR class="listRow">
                <TD class="listItem" height="20">
                    <c:choose>
                        <c:when test="${isCreatedByPopup}">
                           <a href="javascript:opener.selectField('fieldAddressId_id', '${contact.addressId}', 'fieldAddressName_id', '${app2:jscriptEncode(contact.addressName)}');">
                              ${contact.addressName}
                           </a>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${contact.addressType == personType }">
                                    <app:link
                                            page="/Person/Forward/Update.do?dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&contactId=${contact.addressId}"
                                            maxLength="45">
                                        ${app2:filterForHtml(contact.addressName)}
                                    </app:link>
                                </c:when>
                                <c:when test="${contact.addressType == organizationType}">
                                    <app:link
                                            page="/Organization/Forward/Update.do?dto(addressId)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}&dto(name3)=${app2:encode(contact.name3)}&contactId=${contact.addressId}"
                                            maxLength="45">
                                        ${app2:filterForHtml(contact.addressName)}
                                    </app:link>
                                </c:when>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                </TD>
                <TD class="listItem">
                    <c:out value="${contact.countryCode}"/>&nbsp;
                </TD>
                <TD class="listItem">
                    <c:out value="${contact.zip}"/>&nbsp;
                </TD>
                <TD class="listItem">
                    <c:out value="${contact.city}"/>&nbsp;
                </TD>
                <TD class="listItem2">
                    <c:out value="${contact.street}"/>&nbsp;
                </TD>
            </TR>
        </c:forEach>
    </TABLE>


</c:if>