<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("searchTypeList", JSPHelper.getSearchTypeList(request));
%>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<app:url var="urlAddAddress" value="/Mail/Forward/AddAddressGroup.do?dto(mailGroupAddrId)=${mailGroupAddrId}"/>
<app:url var="urlCancel" value="/Mail/ContactGroupList.do"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <TR>
        <TD>
            <div class="optionTitle">
                <fmt:message key="Webmail.addressGroup"/>: ${groupName}
            </div>
            <br>
        </TD>
    </TR>
    <TR>
        <TD height="20" class="title">
            <fmt:message key="Webmail.mailContact.search"/>
        </TD>
    </TR>
    <TR>
        <TD>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
                <html:form action="/Mail/AddressGroupList.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
                           focus="parameter(name1A1@_name2A1@_searchNameA1)">
                    <TR>
                        <TD class="label" width="10%"><fmt:message key="Common.for"/></TD>

                        <TD class="contain" width="90%" colspan="3">
                            <html:text property="parameter(name1A1@_name2A1@_searchNameA1)" styleClass="largeText"/>
                            &nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
                        </TD>
                    </TR>
                </html:form>

                <TR>
                    <TD colspan="4" align="center" class="alpha">
                        <fanta:alphabet action="Mail/AddressGroupList.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
                                        parameterName="name1A1"/>
                    </TD>
                </TR>
                <TR>
                    <TD colspan="4" class="button" border="0" cellpadding="0" cellspacing="0">
                        <table border="0">
                            <TR>
                                <TD class="button">
                                    <html:button property="" styleClass="button"
                                                 onclick="location.href='${urlAddAddress}'"><c:out value="${button}"/>
                                    </html:button>
                                </TD>
                                <TD class="button">
                                    <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'">
                                        <fmt:message key="Common.cancel"/></html:button>
                                </TD>
                            </TR>
                        </table>

                    </TD>
                </TR>
                <TR>
                    <TD colspan="4" valing="top" align="center">
                        <fanta:table list="groupList" width="100%" id="address"
                                     action="Mail/AddressGroupList.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
                                     imgPath="${baselayout}" align="center">

                            <c:set var="updateAction"
                                   value="Mail/Forward/UpdateAddressGroup.do?dto(addressGroupId)=${address.addressGroupId}&dto(mailGroupAddrId)=${mailGroupAddrId}"/>
                            <c:set var="deleteAction"
                                   value="Mail/Forward/DeleteAddressGroup.do?dto(addressGroupId)=${address.addressGroupId}&dto(mailGroupAddrId)=${mailGroupAddrId}"/>

                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                                <app2:checkAccessRight functionality="WEBMAILGROUP" permission="VIEW">
                                    <fanta:actionColumn name="edit" title="Common.update"
                                                        action="${updateAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="WEBMAILGROUP" permission="DELETE">
                                    <fanta:actionColumn name="delete" title="Common.delete"
                                                        action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader" width="3%"
                                              renderData="false">

                                <c:choose>
                                    <c:when test="${not empty address.addressName2}">
                                        <c:set var="personPrefixImageName" value="person"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="personPrefixImageName" value="person-private"/>
                                    </c:otherwise>
                                </c:choose>
                                <html:img
                                        src="${baselayout}/img/${address.addressType == personType? personPrefixImageName : 'org'}.gif"
                                        titleKey="Contact.favorites.add" border="0"/>
                            </fanta:dataColumn>
                            <fanta:dataColumn name="addressName1" styleClass="listItem" title="Contact.name"
                                              action="${updateAction}"
                                              headerStyle="listHeader" width="25%" orderable="true"/>
                            <fanta:dataColumn name="addressName2" styleClass="listItem"
                                              title="ContactPerson.contactName" headerStyle="listHeader" width="25%"
                                              orderable="true"/>
                            <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                              headerStyle="listHeader" width="8%" orderable="true"/>
                            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"
                                              headerStyle="listHeader" width="7%" orderable="true"/>
                            <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city"
                                              headerStyle="listHeader" width="10%" orderable="true"/>
                            <fanta:dataColumn name="" styleClass="listItem2" title="Webmail.mailContact.email"
                                              headerStyle="listHeader" width="17%" renderData="false">
                                <c:choose>
                                    <c:when test="${not empty address.telecomNumber}">
                                        <c:set var="emailsText" value="${address.telecomNumber}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="Webmail.addressGroup.allMails" var="emailsText"/>
                                    </c:otherwise>
                                </c:choose>
                                <fanta:textShorter title="${emailsText}">
                                    <c:out value="${emailsText}"/>
                                </fanta:textShorter>
                            </fanta:dataColumn>
                        </fanta:table>
                    </TD>
                </TR>
            </table>
        </TD>
    </TR>
</table>
<table cellSpacing=0 cellPadding=2 width="95%" border=0 align="center">
    <TR>
        <TD class="button">
            <table border="0">
                <TR>
                    <TD class="button">
                        <html:button property="" styleClass="button" onclick="location.href='${urlAddAddress}'"><c:out
                                value="${button}"/></html:button>
                    </TD>
                    <TD class="button">
                        <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'"><fmt:message
                                key="Common.cancel"/></html:button>
                    </TD>
                </TR>
            </table>
        </TD>
    </TR>
</table>