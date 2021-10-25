<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    <!--

    function select(addressId, contactPersonId, nameAddress) {
        var namePage = window.name;
        opener.putAddress(addressId, contactPersonId, namePage, nameAddress);
    }
    //-->
</script>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td height="20" class="title">
            <fmt:message key="Webmail.Contact.searchContactsOrContactPerson"/>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">

                <html:form action="/Mail/Forward/AddressImportListForUpdate.do"
                           focus="parameter(contactSearchName@_contactPersonOfSearchName)" styleId="listMailForm">

                    <TR>
                        <TD class="label" width="10%"><fmt:message key="Common.for"/></TD>
                        <td class="contain" width="90%">
                            <html:text property="parameter(contactSearchName@_contactPersonOfSearchName)" styleClass="largeText"/>
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                        </td>
                    </TR>
                </html:form>

                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="Mail/Forward/AddressImportListForUpdate.do" parameterName="name1A1"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br/>
                        <fanta:table list="allAddressList" width="100%" id="contact"
                                     action="Mail/Forward/AddressImportListForUpdate.do" imgPath="${baselayout}"
                                     align="center">
                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                                <c:choose>
                                    <c:when test="${contact.contactPersonAddressId!='' && contact.contactPersonAddressId!=null}">
                                        <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                                            action="javascript:select('${contact.contactPersonAddressId}', '${contact.addressId}', '${app2:jscriptEncode(contact.addressName1)}');"
                                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                                            image="${baselayout}/img/import.gif"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                                            action="javascript:select('${contact.addressId}','','${app2:jscriptEncode(contact.addressName1)}');"
                                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                                            image="${baselayout}/img/import.gif"/>
                                    </c:otherwise>
                                </c:choose>
                            </fanta:columnGroup>

                            <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader" width="3%"
                                              renderData="false">
                                <c:choose>
                                    <c:when test="${not empty contact.contactPersonAddressId}">
                                        <c:set var="personPrefixImageName" value="person"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="personPrefixImageName" value="person-private"/>
                                    </c:otherwise>
                                </c:choose>

                                <html:img
                                        src="${baselayout}/img/${contact.addressType == personType? personPrefixImageName : 'org'}.gif"
                                        border="0"/>

                            </fanta:dataColumn>
                            <fanta:dataColumn name="addressName1" styleClass="listItem" title="Contact.name"
                                              headerStyle="listHeader" width="25%" orderable="true"/>
                            <fanta:dataColumn name="addressName2" styleClass="listItem"
                                              title="ContactPerson.contactName" headerStyle="listHeader" width="27%"
                                              orderable="true"/>
                            <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                              headerStyle="listHeader" width="8%" orderable="true"/>
                            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"
                                              headerStyle="listHeader" width="7%" orderable="true"/>
                            <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city"
                                              headerStyle="listHeader" width="10%" orderable="true"/>
                            <fanta:dataColumn name="addressId" styleClass="listItem2" title="Webmail.mailContact.email"
                                              headerStyle="listHeader" width="15%" renderData="false">
                                <c:choose>
                                    <c:when test="${contact.contactPersonAddressId!='' && contact.contactPersonAddressId!=null}">
                                        <app:telecomSelect property="dirAddressEmails" numberColumn="telecomnumber"
                                                           telecomType="${TELECOMTYPE_EMAIL}"
                                                           resultIsEmptyKey="Webmail.Address.withoutEmails"
                                                           addressId="${contact.contactPersonAddressId}"
                                                           contactPersonId="${contact.addressId}" styleClass="select"
                                                           optionStyleClass="list" showDescription="false"/>
                                    </c:when>
                                    <c:otherwise>
                                        <app:telecomSelect property="dirAddressEmails" numberColumn="telecomnumber"
                                                           telecomType="${TELECOMTYPE_EMAIL}"
                                                           resultIsEmptyKey="Webmail.Address.withoutEmails"
                                                           addressId="${contact.addressId}" styleClass="select"
                                                           optionStyleClass="list" showDescription="false"/>
                                    </c:otherwise>
                                </c:choose>
                            </fanta:dataColumn>
                        </fanta:table>

                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>

