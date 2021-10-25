<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>
<tags:initBootstrapSelectPopupAdvanced
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
        <div class="${app2:getFormGroupClasses()}">
            <label class=" ${app2:getFormLabelClasses()}">
                <fmt:message key="Document.contactPerson"/>
            </label>

            <div class="${app2:getFormContainClasses(op == 'delete')}">
                <div class="input-group">
                    <app:text property="dto(contactPersonName)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              view="${op == 'delete'}"
                              readonly="true" styleId="fieldContactPersonName_id"/>
                    <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup
                                styleId="searchContactPerson_id"
                                tabindex="5"
                                isLargeModal="true"
                                url="/contacts/ContactPerson/Search.do?contactId=${param.contactId}"
                                name="searchContactPerson"
                                titleKey="Common.search"
                                modalTitleKey="ContactPerson.search" hide="${'delete' ==  op}"
                                submitOnSelect="true"/>
                        <tags:clearBootstrapSelectPopup keyFieldId="fieldContactPersonId_id"
                                                        nameFieldId="fieldContactPersonName_id"
                                                        titleKey="Common.clear" hide="${'delete' ==  op}" tabindex="6"
                                                        submitOnClear="true"/>
                    </span>
                </div>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClasses()}">
            <label class=" ${app2:getFormLabelClasses()}">
                <fmt:message key="Document.phoneNumber"/>
            </label>
            <c:choose>
                <c:when test="${op == 'create'}">
                    <div class="${app2:getFormContainClasses(null)}">
                        <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                        <div class="row col-xs-9 col-md-9 col-lg-10">
                            </c:if>
                            <app:telecomSelect numberColumn="telecomnumber"
                                               addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                               contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                               maxLength="30" optionStyleClass="list" telecomType="PHONE"
                                               property="dto(contactNumber)" styleId="phoneId" tabindex="8"/>
                            <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                        </div>
                        </c:if>
                        <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                            <span class="pull-right">
                                <a href="javascript:callto('phoneId', 'callto')" class="${app2:getFormButtonClasses()}"
                                   tabindex="9" alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                   title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">
                                    <span class="glyphicon glyphicon-phone-alt"></span>
                                </a>

                                <a href="javascript:callto('phoneId', 'tel')" class="${app2:getFormButtonClasses()}"
                                   tabindex="9" alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                   title="<fmt:message   key="Contact.telecom.mobileCall"/>">
                                    <span class="glyphicon glyphicon-phone"></span>
                                </a>
                            </span>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                        <div class="row col-xs-9 col-md-9 col-lg-10">
                            </c:if>
                            <app:text property="dto(contactNumber)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      view="${'delete' == op}" tabindex="10" styleId="phoneId"/>
                            <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                        </div>
                        </c:if>
                        <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                            <a href="javascript:callto('phoneId', 'callto')" tabindex="11"
                               alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                               title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">
                                <span class="glyphicon glyphicon-phone-alt"></span>
                            </a>

                            <a href="javascript:callto('phoneId', 'tel')" tabindex="11"
                               alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                               title="<fmt:message   key="Contact.telecom.mobileCall"/>">
                                <span class="glyphicon glyphicon-phone"></span>
                            </a>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${email != mainCommunicationForm.dtoMap['type']}">
            <div class="${app2:getFormGroupClasses()}">
                <label class=" ${app2:getFormLabelClasses()}">
                    <fmt:message key="Document.contactPerson"/>
                </label>

                <div class="${app2:getFormContainClasses(view)}">
                    <div class="input-group">
                        <app:text property="dto(contactPersonName)"
                                  styleClass="mediumText ${app2:getFormInputClasses()}"
                                  view="${view}"
                                  readonly="true" styleId="fieldContactPersonName_id"/>
                        <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup tabindex="5"
                                                       styleId="searchContactPerson_id"
                                                       isLargeModal="true"
                                                       url="/contacts/ContactPerson/Search.do?contactId=${param.contactId}"
                                                       name="searchContactPerson"
                                                       titleKey="Common.search"
                                                       modalTitleKey="ContactPerson.search"
                                                       hide="${view}"
                                                       submitOnSelect="${email == mainCommunicationForm.dtoMap['type']}"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldContactPersonId_id"
                                                            nameFieldId="fieldContactPersonName_id"
                                                            titleKey="Common.clear"
                                                            hide="${view}"
                                                            tabindex="6"
                                                            submitOnClear="${email == mainCommunicationForm.dtoMap['type']}"/>
                        </span>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </c:if>
    </c:otherwise>
</c:choose>
<c:if test="${!isCampGeneration}">
    <div class="${app2:getFormGroupClasses()}">
        <label class=" ${app2:getFormLabelClasses()}">
            <fmt:message key="SalesProcess"/>
        </label>

        <div class="${app2:getFormContainClasses(null)}">

            <div class="input-group">
                <c:if test="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}">
                <div class="form-control-static">
                    </c:if>
                    <app:text property="dto(processName)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              readonly="true" styleId="fieldProcessName_id"
                              view="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"/>
                    <c:if test="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}">
                </div>
                </c:if>
                <html:hidden property="dto(processId)" styleId="fieldProcessId_id"/>
                <html:hidden property="dto(aId)" styleId="addressId_id"/>
                <html:hidden property="dto(anId)" styleId="addressNameId_id"/>
                <span class="input-group-btn">
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                        <c:if test="${'update' == op && not empty mainCommunicationForm.dtoMap['processId']}">
                            <c:set var="processEditLink"
                                   value="/sales/SalesProcess/Forward/Update.do?processId=${mainCommunicationForm.dtoMap['processId']}&dto(processId)=${mainCommunicationForm.dtoMap['processId']}&dto(processName)=${app2:encode(mainCommunicationForm.dtoMap['processName'])}&addressId=${mainCommunicationForm.dtoMap['addressId']}&index=0"/>
                            <c:choose>
                                <c:when test="${true == mainCommunicationForm.dtoMap['isAction']}">
                                    <app:link action="${processEditLink}" contextRelative="true" titleKey="Common.edit"
                                              tabindex="13" styleClass="btn btn-link">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:when>
                                <c:otherwise>
                                    <app:link action="${processEditLink}" contextRelative="true" titleKey="Common.edit"
                                              tabindex="13" styleClass="${app2:getFormButtonClasses()}">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </app2:checkAccessRight>

                    <tags:bootstrapSelectPopup
                            styleId="searchSalesProcess_id"
                            isLargeModal="true"
                            url="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${param.contactId}&contact=true"
                            name="searchSalesProcess"
                            titleKey="Common.search"
                            modalTitleKey="SalesProcess.Title.simpleSearch"
                            tabindex="13"
                            hide="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"
                            submitOnSelect="true"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldProcessId_id" nameFieldId="fieldProcessName_id"
                                                    titleKey="Common.clear" tabindex="14"
                                                    hide="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"
                                                    submitOnClear="true"/>
                </span>
            </div>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:choose>
    <c:when test="${mainCommunicationForm.dtoMap['processId'] != null && mainCommunicationForm.dtoMap['processId'] != ''}">
        <div class="${app2:getFormGroupClasses()}">
            <label class=" ${app2:getFormLabelClasses()}">
                <fmt:message key="SalesProcess.probability"/>

                ( <fmt:message key="Common.probabilitySymbol"/>)
            </label>

            <div class="${app2:getFormContainClasses('delete' == op || true == mainCommunicationForm.dtoMap['isAction'])}">
                <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                <html:select property="dto(probability)" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                             readonly="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}"
                             tabindex="15"
                             styleId="probability_id">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="probabilities" property="value" labelProperty="label"/>
                </html:select>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <html:hidden property="dto(probability)" styleId="probability_id" value=""/>
    </c:otherwise>
</c:choose>

<c:if test="${isCampGeneration}">
    <div class="${app2:getFormGroupClasses()}">
        <label class=" ${app2:getFormLabelClasses()}">
            <fmt:message key="Campaign"/>
        </label>

        <div class="${app2:getFormContainClasses(true)}">
            <c:set var="campInfo" value="${app2:getGenerationCampaignInfo(mainCommunicationForm.dtoMap['contactId'])}"/>
            <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW" var="viewCampaign"/>
            <div class="row col-xs-10">
                <c:out value="${campInfo.campaignName}"/>
            </div>
            <c:if test="${viewCampaign}">
                <c:set var="editLink"
                       value="campaign/Campaign/Forward/Update.do?campaignId=${campInfo.campaignId}&index=0&dto(campaignId)=${campInfo.campaignId}&dto(campaignName)=${app2:encode(campInfo.campaignName)}&dto(operation)=update"/>
                <span class="pull-right">
                    <app:link action="${editLink}" contextRelative="true" titleKey="Common.edit" tabindex="15"
                              styleClass="">
                        <span class="glyphicon glyphicon-edit"></span>
                    </app:link>
                </span>
            </c:if>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class=" ${app2:getFormLabelClasses()}">
            <fmt:message key="Communication.additionalAddress"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
            <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                          readOnly="${'delete' == op}" tabIndex="16">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty mainCommunicationForm.dtoMap['addressId'] ? mainCommunicationForm.dtoMap['addressId'] : param.contactId}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<div class="${app2:getFormGroupClasses()}">
    <label class=" ${app2:getFormLabelClasses()}" for="inout1">
        <fmt:message key="Document.inout"/>
    </label>

    <div class="${app2:getFormContainClasses(null)}">
        <div class="radiocheck">
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)" styleId="inout1" value="1"
                            disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                            tabindex="16">
                </html:radio>
                <label for="inout1"> <fmt:message key="Document.in"/></label>
            </div>
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)" styleId="inout2" value="0"
                            disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                            tabindex="17">
                </html:radio>
                <label for="inout2"> <fmt:message key="Document.out"/></label>
                <c:if test="${'delete' == op || isCampGeneration}">
                    <html:hidden property="dto(inOut)"/>
                </c:if>
            </div>
        </div>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>

</div>

<c:if test="${meeting == mainCommunicationForm.dtoMap['type'] || other == mainCommunicationForm.dtoMap['type'] || phone == mainCommunicationForm.dtoMap['type']}">

    <div class="col-xs-12 col-sm-12">
        <div class="form-group">
            <label class="control-label col-xs-12 col-sm-12 label-left row" for="content_id">
                <fmt:message key="Document.content"/>
            </label>

            <div class="col-xs-12 col-sm-12 row">
                <html:textarea property="dto(freeText)"
                               styleClass="form-control mediumDetailHigh"
                               style="height:220px;width:99%;"
                               styleId="content_id"
                               tabindex="19"
                               readonly="${'delete' == op}"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>

</c:if>

<c:if test="${(letter == mainCommunicationForm.dtoMap['type'] || fax == mainCommunicationForm.dtoMap['type']) && !isCampGeneration}">
    <div class="${app2:getFormGroupClasses()}">
        <label class=" ${app2:getFormLabelClasses()}">
            <fmt:message key="Document.template"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">

            <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
            <fanta:select property="dto(templateId)" styleId="templateId" listName="templateList"
                          labelProperty="description" valueProperty="id" module="/catalogs"
                          firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                          readOnly="${'delete' == op}" tabIndex="18">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="mediaType" value="${mediatype_WORD}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${web_document == mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class=" ${app2:getFormLabelClasses()}">
            <fmt:message key="Communication.webDocument"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
            <fanta:select property="dto(webDocumentId)" listName="webDocumentList"
                          labelProperty="name" valueProperty="webDocumentId" module="/catalogs"
                          firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                          readOnly="${'delete' == op}" tabIndex="18">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${document == mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class=" ${app2:getFormLabelClasses()}">
            <fmt:message key="Communication.document"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
            <fmt:message key="Common.download" var="downloadMsg"/>
            <c:if test="${'create' == op || 'update' ==  op}">
                <c:if test="${'update' == op}">
                    <div class="row col-xs-11">
                </c:if>
                <tags:bootstrapFile property="dto(documentFile)"/>
                <c:if test="${'update' == op}">
                    </div>
                </c:if>
            </c:if>
            <c:if test="${'update' == op}">
                <span class="pull-right">
                    <html:hidden property="dto(documentFileName)"/>
                <app:link
                        action="${downloadDocumentURL}"
                        title="${downloadMsg}"
                        addModuleParams="true" styleClass="${app2:getFormButtonClasses()}">
                    <span class="glyphicon glyphicon-download-alt"></span>
                </app:link>
                </span>
            </c:if>
            <c:if test="${'delete' == op}">
                <c:out value="${mainCommunicationForm.dtoMap['documentFileName']}"/>
            </c:if>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${email == mainCommunicationForm.dtoMap['type']}">
    <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
</c:if>

