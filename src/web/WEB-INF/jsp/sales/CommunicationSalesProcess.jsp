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

<html:hidden property="dto(addressId)" value="${param.addressId}"/>
<html:hidden property="dto(processId)" value="${param.processId}"/>

<c:choose>
    <c:when test="${phone == mainCommunicationForm.dtoMap['type']}">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}" for="fieldContactPersonName_id">
                <fmt:message key="Document.contactPerson"/>
            </label>

            <div class="${app2:getFormContainClasses(op == 'delete')}">
                <div class="input-group">
                    <app:text property="dto(contactPersonName)" styleClass="${app2:getFormInputClasses()} mediumText"
                              view="${op == 'delete'}"
                              readonly="true" styleId="fieldContactPersonName_id"/>
                    <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup tabindex="5"
                                                   styleId="searchContactPerson_id"
                                                   url="/contacts/ContactPerson/Search.do?contactId=${param.addressId}"
                                                   name="searchContactPerson"
                                                   titleKey="Common.search"
                                                   modalTitleKey="ContactPerson.search" hide="${'delete' ==  op}"
                                                   submitOnSelect="true"
                                                   isLargeModal="true"/>
                        <tags:clearBootstrapSelectPopup keyFieldId="fieldContactPersonId_id"
                                                        styleClass="${app2:getFormButtonClasses()}"
                                                        nameFieldId="fieldContactPersonName_id"
                                                        titleKey="Common.clear" hide="${'delete' ==  op}" tabindex="6"
                                                        submitOnClear="true"
                                                        glyphiconClass="glyphicon-erase"/>
                    </span>
                </div>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Document.phoneNumber"/>
            </label>
            <c:choose>
                <c:when test="${op == 'create'}">
                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="row col-xs-9 col-md-9 col-lg-10">
                            <app:telecomSelect numberColumn="telecomnumber"
                                               addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                               contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                               styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                               maxLength="30" optionStyleClass="list" telecomType="PHONE"
                                               property="dto(contactNumber)" styleId="phoneId" tabindex="8"/>
                        </div>
                        <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                            <span class="pull-right">
                                <a href="javascript:callto('phoneId', 'callto')" class="${app2:getFormButtonClasses()}"
                                   tabindex="9">
                               <span class="glyphicon glyphicon-phone-alt"
                                     alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                     title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">
                               </span>
                                </a>

                                <a href="javascript:callto('phoneId', 'tel')" class="${app2:getFormButtonClasses()}"
                                   tabindex="9">
                                <span class="glyphicon glyphicon-phone"
                                      alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                      title="<fmt:message   key="Contact.telecom.mobileCall"/>">
                                </span>
                                </a>
                            </span>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="${app2:getFormContainClasses(view)}">
                        <app:text property="dto(contactNumber)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="40"
                                  view="${'delete' == op}" tabindex="10" styleId="phoneId"/>
                        <c:if test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                            <span class="pull-right">
                                <a href="javascript:callto('phoneId', 'callto')" tabindex="11"
                                   class="${app2:getFormButtonClasses()}">
                                <span class="glyphicon glyphicon-phone-alt"
                                      alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                      title="<fmt:message   key="Contact.newVOIP.phoneCall"/>">
                                </span>
                                </a>

                                <a href="javascript:callto('phoneId', 'tel')" tabindex="11"
                                   class="${app2:getFormButtonClasses()}">
                                <span class="glyphicon glyphicon-phone"
                                      alt="<fmt:message   key="Contact.telecom.mobileCall"/>"
                                      title="<fmt:message   key="Contact.telecom.mobileCall"/>">
                                </span>
                                </a>
                            </span>
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
                <label class="${app2:getFormLabelClasses()}" for="fieldContactPersonName_id">
                    <fmt:message key="Document.contactPerson"/>
                </label>

                <div class="${app2:getFormContainClasses(view)}">
                    <div class="input-group">
                        <app:text property="dto(contactPersonName)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  view="${view}"
                                  readonly="true" styleId="fieldContactPersonName_id"/>
                        <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup tabindex="5"
                                                       styleId="searchContactPerson_id"
                                                       url="/contacts/ContactPerson/Search.do?contactId=${param.addressId}"
                                                       name="searchContactPerson"
                                                       titleKey="Common.search"
                                                       modalTitleKey="ContactPerson.search" hide="${view}"
                                                       submitOnSelect="${email == mainCommunicationForm.dtoMap['type']}"
                                                       isLargeModal="true"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldContactPersonId_id"
                                                            styleClass="${app2:getFormButtonClasses()}"
                                                            nameFieldId="fieldContactPersonName_id"
                                                            titleKey="Common.clear" hide="${view}" tabindex="6"
                                                            submitOnClear="${email == mainCommunicationForm.dtoMap['type']}"
                                                            glyphiconClass="glyphicon-erase"/>
                        </span>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </c:if>
    </c:otherwise>
</c:choose>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="probability_id">
        <fmt:message key="SalesProcess.probability"/>
        (<fmt:message key="Common.probabilitySymbol"/>)
    </label>

    <div class="${app2:getFormContainClasses('delete' == op || true == mainCommunicationForm.dtoMap['isAction'])}">
        <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
        <html:select property="dto(probability)" styleId="probability_id"
                     styleClass="${app2:getFormSelectClasses()} shortSelect"
                     readonly="${'delete' == op || true == mainCommunicationForm.dtoMap['isAction']}" tabindex="12"
                     value="${(op == 'create' && mainCommunicationForm.dtoMap['probability'] == null) ? (probability) : (mainCommunicationForm.dtoMap.probability)}">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="probabilities" property="value" labelProperty="label"/>
        </html:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>

    </div>
</div>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="additionalAddressId_id">
            <fmt:message key="Communication.additionalAddress"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
            <fanta:select property="dto(additionalAddressId)" styleId="additionalAddressId_id"
                          listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="${app2:getFormSelectClasses()} mediumSelect"
                          readOnly="${'delete' == op}" tabIndex="16">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty mainCommunicationForm.dtoMap['addressId'] ? mainCommunicationForm.dtoMap['addressId'] : param.addressId}"/>
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
                            disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                            tabindex="16">
                </html:radio>
                <label><fmt:message key="Document.in"/></label>
            </div>
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)" styleId="inout2" value="0"
                            disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                            tabindex="17">
                </html:radio>
                <label><fmt:message key="Document.out"/></label>
            </div>
        </div>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(inOut)"/>
        </c:if>
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
        <label class="${app2:getFormLabelClasses()}" for="templateId">
            <fmt:message key="Document.template"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
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
        <label class="${app2:getFormLabelClasses()}" for="webDocumentId_id">
            <fmt:message key="Communication.webDocument"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
            <fanta:select property="dto(webDocumentId)" styleId="webDocumentId_id" listName="webDocumentList"
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
        <label class="${app2:getFormLabelClasses()}" for="documentFile_id">
            <fmt:message key="Communication.document"/>
        </label>

        <div class="${app2:getFormContainClasses('delete' == op)}">
            <fmt:message key="Common.download" var="downloadMsg"/>
            <c:if test="${'create' == op || 'update' ==  op}">
                <c:if test="${'update' == op}">
                    <div class="row col-xs-11">
                </c:if>
                <tags:bootstrapFile property="dto(documentFile)"
                                    tabIndex="5"
                                    styleId="documentFile_id"/>
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
                            addModuleParams="true" tabindex="6" styleClass="${app2:getFormButtonClasses()}">
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

