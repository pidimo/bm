<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapFile/>
<fmt:message var="datePattern" key="datePattern" scope="request"/>

<%--<c:set var="onlyView" value="${op == 'delete' || dto.status == '1'}"/>--%>

<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<%--<html:hidden property="dto(addressId)" value="${isFromSalesProcess ? param.addressId : param.contactId}"/>--%>
<html:hidden property="dto(isHtml)"/>
<html:hidden property="dto(redirect)"/>
<html:hidden property="dto(mailId)"/>
<html:hidden property="dto(complementOperation)" value="${complementOperations}"/>

<c:choose>
    <c:when test="${op == 'create'}">
        <html:hidden property="dto(status)" value="0"/>
    </c:when>
    <c:otherwise>
        <html:hidden property="dto(status)"/>

        <html:hidden property="dto(commVersion)"/>

    </c:otherwise>
</c:choose>
<c:if test="${caseActivityForm.dtoMap.type != email}">
    <div class="row">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="note_id">
                <fmt:message key="Document.subject"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(onlyView || !isNewMail)}">
                <app:text property="dto(note)"
                          styleId="note_id"
                          onchange="checkComm()"
                          styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="40"
                          view="${onlyView || !isNewMail}" tabindex="13"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                <fmt:message key="Contact.title"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(onlyView || !isNewMail)}">
                <div class="input-group">
                    <app:text property="dto(contact)" styleId="fieldAddressName_id"
                              styleClass="middleText ${app2:getFormInputClasses()}"
                              tabindex="14" readonly="true" view="${onlyView || !isNewMail}"/>
                    <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                   styleId="searchAddress_id"
                                                   name="searchAddress"
                                                   isLargeModal="true"
                                                   modalTitleKey="Contact.Title.search"
                                                   titleKey="Common.search"
                                                   hide="${onlyView || !isNewMail}" submitOnSelect="true"
                                                   tabindex="15"/>
                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                        tabindex="16"
                                                        titleKey="Common.clear" submitOnClear="true"
                                                        hide="${onlyView || !isNewMail}"/>
                    </span>
                </div>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateStart">
                <fmt:message key="Common.date"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(onlyView || !isNewMail)}">
                <app:dateText property="dto(dateStart)" styleId="dateStart"
                              onchange="checkComm()"
                              mode="bootstrap"
                              calendarPicker="${onlyView && caseActivityForm.dtoMap.type!=email}"
                              datePatternKey="${datePattern}" styleClass="mediumText ${app2:getFormInputClasses()}"
                              view="${onlyView || !isNewMail}"
                              maxlength="10" tabindex="17" currentDate="true"
                              readonly="${caseActivityForm.dtoMap.type==email}"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                <fmt:message key="ContactPerson.title"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(onlyView || !isNewMail)}">
                <fanta:select property="dto(contactPersonId)"
                              styleId="contactPersonId_id"
                              tabIndex="18" listName="searchContactPersonList"
                              onChange="checkCommSubmit(${isNewMail && caseActivityForm.dtoMap.type == email})"
                              module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                              valueProperty="contactPersonId" styleClass="middleSelect ${app2:getFormSelectClasses()}"
                              readOnly="${onlyView || !isNewMail}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                     value="${not empty caseActivityForm.dtoMap.addressId?caseActivityForm.dtoMap.addressId:0}"/>
                </fanta:select>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>
</c:if>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
            <fmt:message key="Document.employee"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(onlyView || caseActivityForm.dtoMap.type == email)}">
            <fanta:select property="dto(employeeId)"
                          styleId="employeeId_id"
                          listName="employeeBaseList"
                          labelProperty="employeeName" valueProperty="employeeId"
                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                          readOnly="${onlyView || caseActivityForm.dtoMap.type == email}"
                          value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts" tabIndex="19">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="inout1">
            <fmt:message key="Document.inout"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(null)}">
            <div class="radiocheck">
                <div class="radio radio-default radio-inline">
                    <html:radio property="dto(inOut)"
                                onchange="checkComm()" styleId="inout1"
                                value="1" styleClass="radio"
                                disabled="${onlyView || !isNewMail || (caseActivityForm.dtoMap.type == email)}"
                                tabindex="20"/>
                    <label><fmt:message key="Document.in"/></label>
                </div>
                <div class="radio radio-default radio-inline">
                    <html:radio property="dto(inOut)" onchange="checkComm()" styleId="inout2" value="0"
                                styleClass="radio"
                                disabled="${onlyView || !isNewMail || (caseActivityForm.dtoMap.type == email)}"
                                tabindex="21"/>
                    <label><fmt:message key="Document.out"/></label>
                </div>
            </div>
            <span class="glyphicon form-control-feedback iconValidation"></span>
            <c:choose>
                <c:when test="${onlyView || (!isNewMail && caseActivityForm.dtoMap.type == email)}">
                </c:when>
                <c:otherwise>
                    <html:hidden property="dto(inOutValidate)" value="true"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<c:choose>
    <c:when test="${caseActivityForm.dtoMap.type == fax || caseActivityForm.dtoMap.type == letter}">
        <c:import url="/WEB-INF/jsp/support/CommunicationFaxLetterFragment.jsp"/>
    </c:when>
    <c:when test="${caseActivityForm.dtoMap.type == phone || caseActivityForm.dtoMap.type== meeting || caseActivityForm.dtoMap.type== other}">
        <div class="row">
            <div class="col-xs-12">
                <label class="control-label" for="freeText_id">
                    <fmt:message key="Document.content"/>
                </label>
                <html:textarea property="dto(freeText)"
                               styleId="freeText_id"
                               onchange="checkComm()"
                               styleClass="mediumDetail ${app2:getFormInputClasses()}"
                               readonly="${onlyView}" tabindex="24"
                               style="height:120px;width:100%;"/>
            </div>
        </div>
        <br/>
    </c:when>
    <c:when test="${document == caseActivityForm.dtoMap.type}">
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Communication.document"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(onlyView)}">
                    <fmt:message key="Common.download" var="downloadMsg"/>
                    <c:if test="${!onlyView}">
                        <c:if test="${null != caseActivityForm.dtoMap.contactId}">
                            <div class="row col-xs-10 col-sm-11">
                        </c:if>
                            <tags:bootstrapFile property="dto(documentFile)" tabIndex="24"/>
                        <c:if test="${null != caseActivityForm.dtoMap.contactId}">
                            </div>
                        </c:if>
                    </c:if>
                    <c:if test="${null != caseActivityForm.dtoMap.contactId}">
                        <span class="pull-right">
                            <html:hidden property="dto(documentFileName)"/>
                            <app:link
                                    styleClass="${app2:getFormButtonClasses()}"
                                    action="/CaseActivity/Document/Download.do?communicationId=${caseActivityForm.dtoMap.contactId}&dto(activityId)=${caseActivityForm.dtoMap.activityId}&caseId=${caseActivityForm.dtoMap.caseId}"
                                    title="${downloadMsg}"
                                    tabindex="24"
                                    addModuleParams="true">
                                <span class="glyphicon glyphicon-download-alt"></span>
                            </app:link>
                        </span>
                    </c:if>
                    <c:if test="${onlyView}">
                        <c:out value="${caseActivityForm.dtoMap['documentFileName']}"/>
                    </c:if>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
    </c:when>
    <c:when test="${caseActivityForm.dtoMap.type == email}">
        <c:set var="mainCommunicationForm" value="${caseActivityForm}" scope="request"/>
        <c:if test="${'update' == op && 'true' == caseActivityForm.dtoMap['redirectValidation'] && (null == caseActivityForm.dtoMap.mailId || empty caseActivityForm.dtoMap.mailId)}">
            <c:set var="showEditor" value="true" scope="request"/>
            <c:set var="isSupportActivity" value="true" scope="request"/>
        </c:if>
        <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
    </c:when>
</c:choose>