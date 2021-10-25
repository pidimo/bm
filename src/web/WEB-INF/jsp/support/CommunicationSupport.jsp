<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
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
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="SupportCase.title"/>
        </label>

        <div class="${app2:getFormContainClasses(true)}">
            <app:text property="dto(supportCaseName)" styleClass="${app2:getFormInputClasses()}" view="true"/>
            <html:hidden property="dto(caseId)"/>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="Contact.title"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <div class="input-group">
                <app:text property="dto(contact)" styleId="fieldAddressName_id"
                          styleClass="${app2:getFormInputClasses()} middleText"
                          readonly="true"
                          view="${view}"/>
                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup styleId="searchAddress_id" url="/contacts/SearchAddress.do"
                                               name="searchAddress" modalTitleKey="Contact.Title.search"
                                               titleKey="Common.search"
                                               hide="${view}" submitOnSelect="true" tabindex="5" isLargeModal="true"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                    titleKey="Common.clear" submitOnClear="true" tabindex="6"
                                                    hide="${view}"/>
                </span>
            </div>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="ContactPerson.title"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <fanta:select property="dto(contactPersonId)" tabIndex="7" listName="searchContactPersonList"
                          onChange="submit()"
                          module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                          valueProperty="contactPersonId" styleClass="${app2:getFormSelectClasses()} middleSelect"
                          readOnly="${view}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty mainCommunicationForm.dtoMap.addressId ? mainCommunicationForm.dtoMap.addressId : 0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${phone == mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="Document.phoneNumber"/>
        </label>
        <c:choose>
            <c:when test="${op == 'create'}">
                <div class="${app2:getFormContainClasses(view)}">
                    <div class="row col-xs-9 col-md-9 col-lg-10">
                        <app:telecomSelect numberColumn="telecomnumber"
                                           addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                           contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                           styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                           maxLength="30" optionStyleClass="list" telecomType="PHONE"
                                           property="dto(contactNumber)" styleId="phoneId" tabindex="8"/>
                    </div>

                    <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                        <div class="pull-right">
                            <a class="${app2:getFormButtonClasses()}" href="javascript:callto('phoneId', 'callto')" tabindex="9"
                               title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">
                                <span class="glyphicon glyphicon-phone-alt"></span>
                            </a>

                            <a class="${app2:getFormButtonClasses()}" href="javascript:callto('phoneId', 'tel')" tabindex="9"
                               title="<fmt:message   key="Contact.telecom.mobileCall"/>">
                                <span class="glyphicon glyphicon-phone"></span>
                            </a>
                        </div>
                    </c:if>

                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="${app2:getFormContainClasses(view)}">
                    <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                    <div class="input-group">
                        </c:if>
                        <app:text property="dto(contactNumber)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="40"
                                  view="${'delete' == op}" tabindex="10" styleId="phoneId"/>

                        <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                            <span class="input-group-btn">
                                <a class="btn btn-default" title="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                   href="javascript:callto('phoneId', 'callto')" tabindex="11">
                                    <span class="glyphicon glyphicon-phone-alt"></span>
                                </a>

                                <a class="btn btn-default" title="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                   href="javascript:callto('phoneId', 'tel')" tabindex="11">
                                    <span class="glyphicon glyphicon-phone"></span>
                                </a>
                            </span>
                        </c:if>
                        <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                    </div>
                    </c:if>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="Communication.additionalAddress"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="${app2:getFormSelectClasses()} mediumSelect"
                          readOnly="${'delete' == op}" tabIndex="16">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty mainCommunicationForm.dtoMap['addressId'] ? mainCommunicationForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}">
        <fmt:message key="Document.inout"/>
    </label>

    <div class="${app2:getFormContainClasses(null)}">
        <div class="radiocheck">
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)" styleId="inout1" value="1"
                            disabled="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'])}"
                            tabindex="16"/>
                <label><fmt:message key="Document.in"/></label>
            </div>
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)" styleId="inout2" value="0"
                            disabled="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'])}"
                            tabindex="17"/>
                <label><fmt:message key="Document.out"/></label>
            </div>
            <c:if test="${'delete' == op}">
                <html:hidden property="dto(inOut)"/>
            </c:if>
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

<c:if test="${letter == mainCommunicationForm.dtoMap['type'] || fax == mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="Document.template"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
            <fanta:select property="dto(templateId)" styleId="templateId" listName="templateList"
                          labelProperty="description" valueProperty="id" module="/catalogs"
                          firstEmpty="true" styleClass="${app2:getFormSelectClasses()} mediumSelect"
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
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="Communication.webDocument"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <fanta:select property="dto(webDocumentId)" listName="webDocumentList"
                          labelProperty="name" valueProperty="webDocumentId" module="/catalogs"
                          firstEmpty="true" styleClass="${app2:getFormSelectClasses()} mediumSelect"
                          readOnly="${'delete' == op}" tabIndex="18">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<c:if test="${document == mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}">
            <fmt:message key="Communication.document"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <fmt:message key="Common.download" var="downloadMsg"/>
            <c:if test="${'create' == op || 'update' ==  op}">
                <c:if test="${'update' == op}">
                    <div class="row col-xs-11 col-sm-11">
                </c:if>
                <tags:bootstrapFile property="dto(documentFile)"/>
                <c:if test="${'update' == op}">
                    </div>
                </c:if>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
            </c:if>
            <c:if test="${'update' == op}">
                <html:hidden property="dto(documentFileName)"/>
                <span class="pull-right">
                    <app:link
                            action="${downloadDocumentURL}"
                            title="${downloadMsg}"
                            styleClass="${app2:getFormButtonClasses()}"
                            addModuleParams="true">
                        <span class="glyphicon glyphicon-download-alt"></span>
                    </app:link>
                </span>
            </c:if>
            <c:if test="${'delete' == op}">
                <c:out value="${mainCommunicationForm.dtoMap['documentFileName']}"/>
            </c:if>
        </div>
    </div>
</c:if>

<c:if test="${email == mainCommunicationForm.dtoMap['type']}">
    <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
</c:if>