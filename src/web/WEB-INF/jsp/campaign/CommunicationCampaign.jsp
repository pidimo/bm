<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapFile/>
<tags:initBootstrapSelectPopup/>
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

<c:if test="${'create' == op}">
    <html:hidden property="dto(activityId)" value="${mainCommunicationForm.dtoMap.activityId}"/>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="fieldAddressName_id">
            <fmt:message key="Contact.title"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <div class="input-group">
                <app:text property="dto(contact)"
                          styleId="fieldAddressName_id"
                          styleClass="${app2:getFormInputClasses()} middleText"
                          tabindex="5"
                          readonly="true"
                          view="${view}"/>
                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                               name="searchAddress"
                                               titleKey="Common.search"
                                               hide="${view}"
                                               modalTitleKey="Contact.Title.search"
                                               styleId="contactSelectPopup_id"
                                               submitOnSelect="true"
                                               isLargeModal="true"
                                               tabindex="5"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                    nameFieldId="fieldAddressName_id"
                                                    titleKey="Common.clear"
                                                    submitOnClear="true"
                                                    tabindex="6"
                                                    hide="${view}"/>
                </span>
            </div>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>

    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="contactPersonId_id">
            <fmt:message key="ContactPerson.title"/>
        </label>

        <div class="${app2:getFormContainClasses(view)}">
            <fanta:select property="dto(contactPersonId)"
                          tabIndex="7"
                          styleId="contactPersonId_id"
                          listName="searchContactPersonList"
                          onChange="submit()"
                          module="/contacts"
                          firstEmpty="true"
                          labelProperty="contactPersonName"
                          valueProperty="contactPersonId"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
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
        <label class="${app2:getFormLabelClasses()}" for="phoneId">
            <fmt:message key="Document.phoneNumber"/>
        </label>

        <c:choose>
            <c:when test="${op == 'create'}">
                <div class="${app2:getFormContainClasses(false)}">
                    <c:choose>
                        <c:when test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                            <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                                <div class="row col-xs-9 col-md-9 col-lg-10">
                            </c:if>
                            <%--<div class="input-group">--%>
                                <app:telecomSelect numberColumn="telecomnumber"
                                                   addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                                   contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                                   styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                   maxLength="30"
                                                   optionStyleClass="list"
                                                   telecomType="PHONE"
                                                   property="dto(contactNumber)"
                                                   styleId="phoneId"
                                                   tabindex="8"/>
                            <c:if test="${!empty mainCommunicationForm.dtoMap['addressId']}">
                                </div>
                            </c:if>

                            <span class="pull-right">

                                        <a href="javascript:callto('phoneId', 'callto')" class="${app2:getFormButtonClasses()}" tabindex="9">
                                            <span title="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                  alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                  class="glyphicon glyphicon-phone-alt">
                                            </span>
                                        </a>

                                        <a href="javascript:callto('phoneId', 'tel')" class="${app2:getFormButtonClasses()}" tabindex="9">
                                            <span title="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                  alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                                  class="glyphicon glyphicon-phone">
                                            </span>
                                        </a>

                                </span>
                            <%--</div>--%>
                        </c:when>

                        <c:otherwise>
                            <app:telecomSelect numberColumn="telecomnumber"
                                               addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                               contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                               styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                               maxLength="30"
                                               optionStyleClass="list"
                                               telecomType="PHONE"
                                               property="dto(contactNumber)"
                                               styleId="phoneId"
                                               tabindex="8"/>
                        </c:otherwise>
                    </c:choose>

                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </c:when>

            <c:otherwise>
                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <c:choose>
                        <c:when test="${!'delete' == op && !empty mainCommunicationForm.dtoMap['addressId']}">
                            <div class="input-group">
                                <app:text property="dto(contactNumber)"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          view="${'delete' == op}"
                                          tabindex="10"
                                          styleId="phoneId"/>

                                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>

                                <span class="input-group-addon">
                                    <a href="javascript:callto('phoneId', 'callto')" tabindex="9">
                                        <span title="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                              alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                              class="glyphicon glyphicon-phone-alt">
                                        </span>
                                    </a>
                                </span>

                                <span class="input-group-addon">
                                    <a href="javascript:callto('phoneId', 'tel')" tabindex="9">
                                        <span title="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                              alt="<fmt:message   key="Contact.newVOIP.phoneCall"/>"
                                              class="glyphicon glyphicon-phone">
                                        </span>
                                    </a>
                                </span>

                            </div>
                        </c:when>

                        <c:otherwise>
                            <app:text property="dto(contactNumber)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      view="${'delete' == op}"
                                      tabindex="10"
                                      styleId="phoneId"/>
                        </c:otherwise>
                    </c:choose>

                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<c:if test="${email != mainCommunicationForm.dtoMap['type']}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="additionalAddressId_id">
            <fmt:message key="Communication.additionalAddress"/>
        </label>

        <div class="${app2:getFormContainClasses(op == 'delete')}">
            <fanta:select property="dto(additionalAddressId)"
                          listName="additionalAddressSelectList"
                          labelProperty="name"
                          styleId="additionalAddressId_id"
                          valueProperty="additionalAddressId"
                          module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true"
                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                          readOnly="${'delete' == op}"
                          tabIndex="16">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty mainCommunicationForm.dtoMap['addressId'] ? mainCommunicationForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>

</c:if>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="inout1">
        <fmt:message key="Document.inout"/>
    </label>

    <div class="${app2:getFormContainClasses(null)}">
        <div class="radiocheck">
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)"
                            styleId="inout1"
                            value="1"
                            styleClass="radio"
                            disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                            tabindex="16"/>
                <label><fmt:message key="Document.in"/></label>
            </div>
            <div class="radio radio-default radio-inline">
                <html:radio property="dto(inOut)"
                            styleId="inout2"
                            value="0"
                            styleClass="radio"
                            disabled="${view || (email == mainCommunicationForm.dtoMap['type'] && 'create' == op)}"
                            tabindex="17"/>
                <label><fmt:message key="Document.out"/></label>
            </div>
            <c:if test="${view}">
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

<c:if test="${(letter == mainCommunicationForm.dtoMap['type'] || fax == mainCommunicationForm.dtoMap['type']) && !isCampGeneration}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="templateId">
            <fmt:message key="Document.template"/>
        </label>

        <div class="${app2:getFormContainClasses(op == 'delete' )}">
            <c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
            <fanta:select property="dto(templateId)"
                          styleId="templateId"
                          listName="templateList"
                          labelProperty="description"
                          valueProperty="id"
                          module="/catalogs"
                          firstEmpty="true"
                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                          readOnly="${'delete' == op}"
                          tabIndex="18">
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

        <div class="${app2:getFormContainClasses(op == 'delete')}">
            <fanta:select property="dto(webDocumentId)"
                          listName="webDocumentList"
                          labelProperty="name"
                          styleId="webDocumentId_id"
                          valueProperty="webDocumentId"
                          module="/catalogs"
                          firstEmpty="true"
                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                          readOnly="${'delete' == op}"
                          tabIndex="18">
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
                                    styleId="documentFile_id"
                                    tabIndex="3"/>
                <c:if test="${'update' == op}">
                    </div>
                </c:if>
            </c:if>

            <c:if test="${'update' == op}">
                <span class="pull-right">
                    <html:hidden property="dto(documentFileName)"/>

                    <app:link action="${downloadDocumentURL}"
                              title="${downloadMsg}"
                              styleClass="${app2:getFormButtonClasses()}"
                              addModuleParams="true">
                    <span title="${downloadMsg}" alt="${downloadMsg}"
                          class="glyphicon glyphicon-download-alt"></span>
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

<tags:jQueryValidation formName="mainCommunicationForm"/>