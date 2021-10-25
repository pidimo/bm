<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, probability_id, addressId_id, addressNameId_id"/>
<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>

<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="view"
       value="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'] && 'update' == op) || isCampGeneration}"/>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="document" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT%>" scope="page"/>
<c:set var="web_document" value="<%= com.piramide.elwis.utils.CommunicationTypes.WEB_DOCUMENT%>" scope="page"/>

<html:hidden property="dto(addressId)" value="${param.contactId}"/>

<c:if test="${mainCommunicationForm.dtoMap.caseId != null}">
    <html:hidden property="dto(caseId)"/>
</c:if>

<c:choose>
    <c:when test="${phone == mainCommunicationForm.dtoMap['type']}">
        <tr>
            <td class="label">
                <fmt:message key="Document.contactPerson"/>
            </td>
            <td class="contain">
                <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                <app:text property="dto(contactPersonName)" styleClass="mediumText"
                          view="${op == 'delete'}"
                          readonly="true" styleId="fieldContactPersonName_id"/>

                <tags:selectPopup tabindex="5"
                                  url="/contacts/ContactPerson/Search.do?contactId=${param.contactId}"
                                  name="searchContactPerson"
                                  titleKey="Common.search" hide="${'delete' ==  op}"
                                  submitOnSelect="true"/>
                <tags:clearSelectPopup keyFieldId="fieldContactPersonId_id" nameFieldId="fieldContactPersonName_id"
                                       titleKey="Common.clear" hide="${'delete' ==  op}" tabindex="6"
                                       submitOnClear="true"/>
            </td>
        </tr>
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
    </c:when>
    <c:otherwise>
        <c:if test="${email != mainCommunicationForm.dtoMap['type']}">
            <tr>
                <td class="label">
                    <fmt:message key="Document.contactPerson"/>
                </td>
                <td class="contain">
                    <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                    <app:text property="dto(contactPersonName)" styleClass="mediumText"
                              view="${view}"
                              readonly="true" styleId="fieldContactPersonName_id"/>

                    <tags:selectPopup tabindex="5"
                                      url="/contacts/ContactPerson/Search.do?contactId=${param.contactId}"
                                      name="searchContactPerson"
                                      titleKey="Common.search"
                                      hide="${view}"
                                      submitOnSelect="${email == mainCommunicationForm.dtoMap['type']}"/>
                    <tags:clearSelectPopup keyFieldId="fieldContactPersonId_id" nameFieldId="fieldContactPersonName_id"
                                           titleKey="Common.clear"
                                           hide="${view}"
                                           tabindex="6"
                                           submitOnClear="${email == mainCommunicationForm.dtoMap['type']}"/>
                </td>
            </tr>
        </c:if>
    </c:otherwise>
</c:choose>
<c:if test="${!isCampGeneration}">
    <TR>
        <TD class="label">
            <fmt:message key="SalesProcess"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(processId)" styleId="fieldProcessId_id"/>
            <html:hidden property="dto(aId)" styleId="addressId_id"/>
            <html:hidden property="dto(anId)" styleId="addressNameId_id"/>
            <app:text property="dto(processName)" styleClass="mediumText" readonly="true" styleId="fieldProcessName_id"
                      view="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"/>

            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                <c:if test="${'update' == op && not empty mainCommunicationForm.dtoMap['processId']}">
                    <c:set var="processEditLink"
                           value="/sales/SalesProcess/Forward/Update.do?processId=${mainCommunicationForm.dtoMap['processId']}&dto(processId)=${mainCommunicationForm.dtoMap['processId']}&dto(processName)=${app2:encode(mainCommunicationForm.dtoMap['processName'])}&addressId=${mainCommunicationForm.dtoMap['addressId']}&index=0"/>
                    <app:link action="${processEditLink}" contextRelative="true" tabindex="13">
                        <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                    </app:link>
                </c:if>
            </app2:checkAccessRight>

            <tags:selectPopup
                    url="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${param.contactId}&contact=true"
                    name="searchSalesProcess"
                    titleKey="Common.search"
                    width="800" tabindex="13"
                    hide="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"
                    submitOnSelect="true"/>
            <tags:clearSelectPopup keyFieldId="fieldProcessId_id" nameFieldId="fieldProcessName_id"
                                   titleKey="Common.clear" tabindex="14"
                                   hide="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"
                                   submitOnClear="true"/>
        </TD>
    </TR>
</c:if>

<c:choose>
    <c:when test="${mainCommunicationForm.dtoMap['processId'] != null && mainCommunicationForm.dtoMap['processId'] != ''}">
        <TR>
            <TD class="label">
                <fmt:message key="SalesProcess.probability"/>
            </TD>
            <TD class="contain">
                <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                <html:select property="dto(probability)" styleClass="mediumSelect"
                             readonly="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"
                             tabindex="15"
                             styleId="probability_id">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="probabilities" property="value" labelProperty="label"/>
                </html:select>
                <fmt:message key="Common.probabilitySymbol"/>
            </TD>
        </TR>
    </c:when>
    <c:otherwise>
        <html:hidden property="dto(probability)" styleId="probability_id" value=""/>
    </c:otherwise>
</c:choose>

<c:if test="${isCampGeneration}">
    <tr>
        <td class="label">
            <fmt:message key="Campaign"/>
        </td>
        <td class="contain">
            <c:set var="campInfo" value="${app2:getGenerationCampaignInfo(mainCommunicationForm.dtoMap['contactId'])}"/>
            <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW" var="viewCampaign"/>

            <c:out value="${campInfo.campaignName}"/>

            <c:if test="${viewCampaign}">
                <c:set var="editLink"
                       value="campaign/Campaign/Forward/Update.do?campaignId=${campInfo.campaignId}&index=0&dto(campaignId)=${campInfo.campaignId}&dto(campaignName)=${app2:encode(campInfo.campaignName)}&dto(operation)=update"/>
                <app:link action="${editLink}" contextRelative="true" tabindex="15">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                </app:link>
            </c:if>
        </td>
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
                <fanta:parameter field="addressId" value="${not empty mainCommunicationForm.dtoMap['addressId'] ? mainCommunicationForm.dtoMap['addressId'] : param.contactId}"/>
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
                    disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                    tabindex="16">&nbsp;
            <fmt:message key="Document.in"/>
        </html:radio>
        &nbsp;
        <html:radio property="dto(inOut)" styleId="inout2" value="0" styleClass="radio"
                    disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                    tabindex="17">&nbsp;
            <fmt:message key="Document.out"/>
        </html:radio>
        <c:if test="${'delete' == op || isCampGeneration}">
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

<c:if test="${(letter == mainCommunicationForm.dtoMap['type'] || fax == mainCommunicationForm.dtoMap['type']) && !isCampGeneration}">
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

