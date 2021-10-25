<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, probability_id, addressId_id, addressNameId_id"/>
<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>

<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="view"
       value="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'] && 'update' == op)}"/>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="document" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT%>" scope="page"/>
<c:set var="web_document" value="<%= com.piramide.elwis.utils.CommunicationTypes.WEB_DOCUMENT%>" scope="page"/>

<c:if test="${'create' == op}">
    <html:hidden property="dto(activityId)" value="${mainCommunicationForm.dtoMap.activityId}"/>
</c:if>

<c:if test="${mainCommunicationForm.dtoMap.supportCaseName != null}">
    <tr>
        <td class="label" width="15%">
            <fmt:message key="SupportCase.title"/>
        </td>
        <td class="contain" width="35%">
            <app:text property="dto(supportCaseName)" view="true"/>
            <html:hidden property="dto(caseId)"/>
        </td>
    </tr>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <tr>
        <td class="label" width="15%">
            <fmt:message key="Contact.title"/>
        </td>
        <td class="contain" width="35%">
            <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
            <app:text property="dto(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                      view="${view}"/>

            <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                              hide="${view}" submitOnSelect="true" tabindex="5"/>
            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                   titleKey="Common.clear" submitOnClear="true" tabindex="6"
                                   hide="${view}"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="15%">
            <fmt:message key="ContactPerson.title"/>
        </td>
        <td class="contain" width="35%">
            <fanta:select property="dto(contactPersonId)" tabIndex="7" listName="searchContactPersonList"
                          onChange="submit()"
                          module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                          valueProperty="contactPersonId" styleClass="middleSelect" readOnly="${view}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty mainCommunicationForm.dtoMap.addressId ? mainCommunicationForm.dtoMap.addressId : 0}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>

<c:if test="${phone == mainCommunicationForm.dtoMap['type']}">
    <tr>
        <td class="label">
            <fmt:message key="Document.phoneNumber"/>
        </td>
        <c:choose>
            <c:when test="${op == 'create'}">
                <td class="contain">
                    <app:telecomSelect numberColumn="telecomnumber"
                                       addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                       contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                       styleClass="mediumSelect"
                                       maxLength="30" optionStyleClass="list" telecomType="PHONE"
                                       property="dto(contactNumber)" styleId="phoneId" tabindex="8"/>
                    <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                        <a href="javascript:callto('phoneId', 'callto')" tabindex="9">
                            <img src="${sessionScope.baselayout}/img/phone2.gif"
                                 alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                 title="<fmt:message   key="Contact.newVOIP.phoneCall"/>" border="0"
                                 align="middle"/>
                        </a>

                        <a href="javascript:callto('phoneId', 'tel')" tabindex="9">
                            <img src="<c:url value="/layout/ui/img/mobile.gif"/>"
                                 alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                 title="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                 border="0" align="middle"/>
                        </a>
                    </c:if>
                </td>
            </c:when>
            <c:otherwise>
                <td class="contain">
                    <app:text property="dto(contactNumber)" styleClass="mediumText" maxlength="40"
                              view="${'delete' == op}" tabindex="10" styleId="phoneId"/>
                    <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                        <a href="javascript:callto('phoneId', 'callto')" tabindex="11">
                            <img src="${sessionScope.baselayout}/img/phone2.gif"
                                 alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                 title="<fmt:message   key="Contact.newVOIP.phoneCall"/>" border="0"
                                 align="middle"/>
                        </a>

                        <a href="javascript:callto('phoneId', 'tel')" tabindex="11">
                            <img src="<c:url value="/layout/ui/img/mobile.gif"/>"
                                 alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                 title="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                 border="0" align="middle"/>
                        </a>
                    </c:if>
                </td>
            </c:otherwise>
        </c:choose>
    </tr>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <tr>
        <td class="label">
            <fmt:message key="Communication.additionalAddress"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="16">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${not empty mainCommunicationForm.dtoMap['addressId'] ? mainCommunicationForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>

<TR>
    <TD class="label">
        <fmt:message key="Document.inout"/>
    </TD>
    <TD class="contain">
        <html:radio property="dto(inOut)" styleId="inout1" value="1" styleClass="radio"
                    disabled="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'])}" tabindex="16">&nbsp;
            <fmt:message key="Document.in"/>
        </html:radio>
        &nbsp;
        <html:radio property="dto(inOut)" styleId="inout2" value="0" styleClass="radio"
                    disabled="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'])}" tabindex="17">&nbsp;
            <fmt:message key="Document.out"/>
        </html:radio>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(inOut)"/>
        </c:if>
    </TD>
</TR>

<c:if test="${meeting == mainCommunicationForm.dtoMap['type'] || other == mainCommunicationForm.dtoMap['type'] || phone == mainCommunicationForm.dtoMap['type']}">
    <TR>
        <TD class="topLabel" colspan="2">
            <fmt:message key="Document.content"/>
            <br>
            <html:textarea property="dto(freeText)" styleClass="mediumDetail"
                           readonly="${'delete' == op}" tabindex="19"
                           style="height:120px;width:99%;"/>
            <br>&nbsp;
        </TD>
    </TR>
</c:if>

<c:if test="${letter == mainCommunicationForm.dtoMap['type'] || fax == mainCommunicationForm.dtoMap['type']}">
    <TR>
        <TD class="label">
            <fmt:message key="Document.template"/>
        </TD>
        <TD class="contain">

            <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
            <fanta:select property="dto(templateId)" styleId="templateId" listName="templateList"
                          labelProperty="description" valueProperty="id" module="/catalogs"
                          firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="18">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="mediaType" value="${mediatype_WORD}"/>
            </fanta:select>

        </TD>
    </TR>
</c:if>

<c:if test="${web_document == mainCommunicationForm.dtoMap['type']}">
    <tr>
        <td class="label">
            <fmt:message key="Communication.webDocument"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(webDocumentId)" listName="webDocumentList"
                          labelProperty="name" valueProperty="webDocumentId" module="/catalogs"
                          firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="18">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>

<c:if test="${document == mainCommunicationForm.dtoMap['type']}">
    <TR>
        <TD class="label">
            <fmt:message key="Communication.document"/>
        </TD>
        <TD class="contain">
            <fmt:message key="Common.download" var="downloadMsg"/>
            <c:if test="${'create' == op || 'update' ==  op}">
                <html:file property="dto(documentFile)"/>&nbsp;
            </c:if>
            <c:if test="${'update' == op}">
                <html:hidden property="dto(documentFileName)"/>
                <app:link
                        action="${downloadDocumentURL}"
                        title="${downloadMsg}"
                        addModuleParams="true">
                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/openfile.png"
                         alt="${downloadMsg}" border="0" align="middle"/>
                </app:link>
            </c:if>
            <c:if test="${'delete' == op}">
                <c:out value="${mainCommunicationForm.dtoMap['documentFileName']}"/>
            </c:if>
        </TD>
    </TR>
</c:if>

<c:if test="${email == mainCommunicationForm.dtoMap['type']}">
    <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
</c:if>