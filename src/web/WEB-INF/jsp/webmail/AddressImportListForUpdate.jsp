<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    <!--

    function select(addressId, contactPersonId, nameAddress) {
        var namePage = parent.readEmailIdTemp();
        parent.putAddress(addressId, contactPersonId, namePage, nameAddress);
    }
    //-->
</script>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<h1>lito 2222</h1>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Mail/Forward/AddressImportListForUpdate.do"
               focus="parameter(contactSearchName@_contactPersonOfSearchName)" styleId="listMailForm"
               styleClass="form-horizontal">
        <fieldset>
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.for"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(contactSearchName@_contactPersonOfSearchName)"
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
        <fanta:alphabet action="Mail/Forward/AddressImportListForUpdate.do" parameterName="name1A1" mode="bootstrap"/>
    </div>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="allAddressList" width="100%" id="contact" styleClass="${app2:getFantabulousTableClases()}"
                     action="Mail/Forward/AddressImportListForUpdate.do" imgPath="${baselayout}"
                     align="center">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <c:choose>
                    <c:when test="${contact.contactPersonAddressId!='' && contact.contactPersonAddressId!=null}">
                        <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                            action="javascript:select('${contact.contactPersonAddressId}', '${contact.addressId}', '${app2:jscriptEncode(contact.addressName1)}');"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            glyphiconClass="${app2:getClassGlyphImport()}"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                            action="javascript:select('${contact.addressId}','','${app2:jscriptEncode(contact.addressName1)}');"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            glyphiconClass="${app2:getClassGlyphImport()}"/>
                    </c:otherwise>
                </c:choose>
            </fanta:columnGroup>

            <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader" width="3%"
                              renderData="false">
                <c:choose>
                    <c:when test="${not empty contact.contactPersonAddressId}">
                        <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>
                    </c:otherwise>
                </c:choose>

                <span class="${contact.addressType == personType? personPrefixImageName : app2:getClassGlyphOrganization()}"></span>

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
                                           contactPersonId="${contact.addressId}"
                                           styleClass="${app2:getFormSelectClasses()}"
                                           optionStyleClass="list" showDescription="false"/>
                    </c:when>
                    <c:otherwise>
                        <app:telecomSelect property="dirAddressEmails" numberColumn="telecomnumber"
                                           telecomType="${TELECOMTYPE_EMAIL}"
                                           resultIsEmptyKey="Webmail.Address.withoutEmails"
                                           addressId="${contact.addressId}" styleClass="${app2:getFormSelectClasses()}"
                                           optionStyleClass="list" showDescription="false"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

