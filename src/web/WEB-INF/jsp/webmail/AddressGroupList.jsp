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

<div class="${app2:getListWrapperClasses()}">
    <label class="optionTitle">
        <fmt:message key="Webmail.addressGroup"/>: ${groupName}
    </label>

    <html:form action="/Mail/AddressGroupList.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
               focus="parameter(name1A1@_name2A1@_searchNameA1)" styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="Webmail.mailContact.search"/>
            </legend>
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.for"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(name1A1@_name2A1@_searchNameA1)"
                                   styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                    </div>
                </div>
            </div>
        </fieldset>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Mail/AddressGroupList.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
                        parameterName="name1A1" mode="bootstrap"/>
    </div>

    <div class="${app2:getFormGroupClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlAddAddress}'">
            <c:out value="${button}"/>
        </html:button>

        <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}"
                     onclick="location.href='${urlCancel}'">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="groupList" width="100%" id="address" styleClass="${app2:getFantabulousTableClases()}"
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
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="WEBMAILGROUP" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader"
                              width="3%"
                              renderData="false">

                <c:choose>
                    <c:when test="${not empty address.addressName2}">
                        <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>
                    </c:otherwise>
                </c:choose>
                <span class="${address.addressType == personType? personPrefixImageName : app2:getClassGlyphOrganization()}" title="Contact.favorites.add"></span>
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
    </div>

    <div class="${app2:getFormGroupClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlAddAddress}'">
            <c:out value="${button}"/>
        </html:button>


        <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}"
                     onclick="location.href='${urlCancel}'">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>
</div>