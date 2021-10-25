<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil" %>
<%@ include file="/Includes.jsp" %>

<%--
createSaleLink : Link for create Sale Button, to setting this link use :
            <c:set> tag with scope="request" property
--%>

<c:if test="${empty createSaleLink}">
    <c:set var="createSaleLink" value="/sales/SalesProcess/Sale/Forward/Create.do" scope="request"/>
</c:if>

<c:if test="${empty tabName}">
    <c:set var="tabName" value="SalesProcess.tab.sale" scope="request"/>
</c:if>

<c:if test="${empty createTaskLink}">
    <c:set var="createTaskLink" value="/sales/Task/Forward/Create.do" scope="request"/>
</c:if>

<c:if test="${empty taskTabName}">
    <c:set var="taskTabName" value="Scheduler.Tasks" scope="request"/>
</c:if>

<c:if test="${empty createActionLink}">
    <c:set var="createActionLink" value="/sales/SalesProcess/Action/Forward/Create.do" scope="request"/>
</c:if>
<c:if test="${empty actionTabName}">
    <c:set var="actionTabName" value="SalesProcess.Tab.detail" scope="request"/>
</c:if>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapFile/>

<app2:jScriptUrl
        url="${createSaleLink}?processId=${actionForm.dtoMap['processId']}&dto(contactId)=${actionForm.dtoMap['contactId']}&dto(processId)=${actionForm.dtoMap['processId']}&tabKey=${tabName}"
        var="createSaleUrl"/>

<app2:jScriptUrl
        url="${createTaskLink}?processId=${actionForm.dtoMap['processId']}&dto(processId)=${actionForm.dtoMap['processId']}&addressName=${app2:encode(addressName)}&processName=${app2:encode(processName)}&dto(processName)=${app2:encode(actionForm.dtoMap['processName'])}&tabKey=${taskTabName}"
        var="newTaskForwardUrl"/>

<app2:jScriptUrl
        url="${createActionLink}?processId=${actionForm.dtoMap['processId']}&dto(processId)=${actionForm.dtoMap['processId']}&processName=${app2:encode(processName)}&dto(processName)=${app2:encode(actionForm.dtoMap['processName'])}&tabKey=${actionTabName}"
        var="newActionForwardUrl"/>

<%--url to send Action document via email--%>
<app2:jScriptUrl
        url="/sales/SalesProcess/Action/SendViaEmail.do?paramProcessId=${actionForm.dtoMap['processId']}&paramContactId=${actionForm.dtoMap['contactId']}"
        var="jsSendViaEmailUrl">
    <app2:jScriptUrlParam param="telecomId" value="telecomId"/>
    <app2:jScriptUrlParam param="freeTextId" value="documentFreeTextId"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
    function goToForward(id) {
        selectBox = document.getElementById(id);

        optionObject = selectBox.options[selectBox.selectedIndex];

        if (null != optionObject) {
            if ('1' == optionObject.value) {
                location.href = ${newTaskForwardUrl};
            }

            if ('2' == optionObject.value) {
                location.href = ${newActionForwardUrl};
            }
        }
    }


    function createSale() {
        location.href = ${createSaleUrl};
    }
    function jumpForm(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
    function changeCommunicationType(obj) {
        document.getElementById("changeCommunicationTypeId").value = "true";
        document.forms[0].submit();
    }
    function changeActionType(obj) {
        document.getElementById("changeActionTypeId").value = "true";
        document.forms[0].submit();
    }

    function sendDocumentViaEmail(emailBoxId, documentFreeTextId) {
        var telecomId = document.getElementById(emailBoxId).value;
        location.href = ${jsSendViaEmailUrl};
    }

</script>

<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<c:set var="saveButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Save.getKey()%>" scope="page"/>
<c:set var="generateButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey()%>"
       scope="page"/>
<c:set var="sendButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Send.getKey()%>" scope="page"/>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="document" value="<%= com.piramide.elwis.utils.CommunicationTypes.DOCUMENT%>" scope="page"/>

<c:set var="BODY_TYPE_HTML" value="<%=com.piramide.elwis.utils.WebMailConstants.BODY_TYPE_HTML%>"/>
<c:set var="BODY_TYPE_TEXT" value="<%=com.piramide.elwis.utils.WebMailConstants.BODY_TYPE_TEXT%>"/>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<c:set var="forwardOptions" value="${app2:getActionForwardSelectOptions(pageContext.request)}"/>
<html:form action="${action}" focus="dto(type)" enctype="multipart/form-data" styleId="composeEmailId"
           styleClass="form-horizontal">

    <html:hidden property="dto(changeCommunicationType)" value="false" styleId="changeCommunicationTypeId"/>
    <html:hidden property="dto(changeActionType)" value="false" styleId="changeActionTypeId"/>

    <html:hidden property="dto(op)" value="${op}" styleId="op"/>
    <html:hidden property="dto(processId)"/>
    <html:hidden property="dto(isAction)" value="true"/>
    <html:hidden property="dto(addressId)" value="${isSalesProcess ? param.addressId : param.contactId}"
                 styleId="delta"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

    <c:if test="${op == 'create'}">
        <html:hidden property="dto(status)" value="0"/>
        <html:hidden property="dto(active)" value="true"/>
    </c:if>

    <c:if test="${'update' == op || 'delete' == op}">
        <html:hidden property="dto(showNetGrossWarningMessage)" styleId="showNetGrossWarningMessageId"/>
        <html:hidden property="dto(contactId)"/>
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(status)"/>

    </c:if>

    <div class="row">
        <div class="col-xs-12">
            <c:if test="${'update' == op && null != forwardOptions}">
                <div class="form-group col-xs-12 col-sm-4">
                    <div class="input-group">
                        <html:select property="dto(forwardOption)"
                                     style="border-radius: 4px"
                                     styleClass="select ${app2:getFormSelectClasses()} ignore"
                                     styleId="topForwardOptionSelect">
                            <html:options collection="forwardOptions"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="input-group-btn" style="padding-left: 5px !important; border-radius: 4px">
                            <html:button property=""
                                         style="border-radius: 4px"
                                         onclick="goToForward('topForwardOptionSelect')"
                                         styleClass="button ${app2:getFormButtonClasses()} marginRight marginLeft">
                                <fmt:message key="Common.go"/>
                            </html:button>
                        </span>
                    </div>
                </div>
            </c:if>
            <c:choose>
                <c:when test="${actionForm.dtoMap['type'] == letter || actionForm.dtoMap['type'] == fax}">

                    <app2:securitySubmit property="${saveButton}" operation="${op}"
                                         functionality="SALESPROCESSACTION"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                         styleId="saveButtonId">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>

                    <c:if test="${op != 'delete'}">
                        <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="EXECUTE">
                            <html:submit property="${generateButton}"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton">
                                <fmt:message key="Document.generate"/>
                            </html:submit>
                        </app2:checkAccessRight>

                        <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
                            <c:set var="docInfo"
                                   value="${app2:getCommunicationDocumentInfo(actionForm.dtoMap['contactId'])}"/>
                            <c:if test="${not empty docInfo.freeTextId}">
                                <app:url var="openDocUrl"
                                         value="contacts/Download.do?dto(type)=comm&dto(fid)=${docInfo.freeTextId}"
                                         contextRelative="true"/>
                                <html:button property=""
                                             styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                             onclick="location.href='${openDocUrl}'">
                                    <fmt:message key="Document.open"/>
                                </html:button>

                                <c:set var="isActionSendViaEmail" value="${not empty docInfo.freeTextId and app2:hasDefaultMailAccount(pageContext.request)}"/>
                                <c:if test="${isActionSendViaEmail}">
                                    <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                                        <html:button property=""
                                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                                     onclick="sendDocumentViaEmail('upToEmail_id', '${docInfo.freeTextId}')">
                                            <fmt:message key="SalesProcessAction.sendViaEmail"/>
                                        </html:button>

                                        <div class="form-group col-xs-12 col-sm-3">
                                            <app:telecomSelect property="toEmail"
                                                               styleId="upToEmail_id"
                                                               telecomIdColumn="telecomid"
                                                               numberColumn="telecomnumber"
                                                               telecomType="${TELECOMTYPE_EMAIL}"
                                                               addressId="${isSalesProcess ? param.addressId : param.contactId}"
                                                               contactPersonId="${actionForm.dtoMap['contactPersonId']}"
                                                               showOwner="true"
                                                               styleClass="${app2:getFormSelectClasses()}"
                                                               optionStyleClass="list" showDescription="false"
                                                               selectPredetermined="true"/>

                                        </div>

                                    </app2:checkAccessRight>
                                </c:if>

                            </c:if>
                        </app2:checkAccessRight>
                    </c:if>
                </c:when>
                <c:when test="${actionForm.dtoMap['type'] == email}">
                    <c:choose>
                        <c:when test="${'create' == op}">
                            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                                <app2:securitySubmit operation="${op}"
                                                     functionality="SALESPROCESSACTION"
                                                     property="${sendButton}"
                                                     styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton">
                                    <fmt:message key="Common.send"/>
                                </app2:securitySubmit>
                            </app2:checkAccessRight>
                        </c:when>
                        <c:when test="${null != op1}">
                        </c:when>
                        <c:otherwise>
                            <app2:securitySubmit property="${saveButton}" operation="${op}"
                                                 functionality="SALESPROCESSACTION"
                                                 styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                                 styleId="saveButtonId">
                                <c:out value="${button}"/>
                            </app2:securitySubmit>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit property="${saveButton}" operation="${op}"
                                         functionality="SALESPROCESSACTION"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                         styleId="saveButtonId">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>

            <app2:checkAccessRight functionality="SALE" permission="CREATE">
                <c:if test="${'update' == op && 100 == actionForm.dtoMap['probability'] && actionForm.dtoMap['type'] != email}">
                    <html:button property="createSaleButton"
                                 styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                 onclick="javascript:createSale();">
                        <fmt:message key="SalessProcess.createSale"/>
                    </html:button>
                </c:if>
            </app2:checkAccessRight>

            <html:cancel
                    styleClass="button ${app2:getFormButtonCancelClasses()} pull-left marginRight marginLeft marginButton"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                        <fmt:message key="Communication.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op != 'create')}">
                        <c:set var="communicationTypes"
                               value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                        <html:select property="dto(type)"
                                     styleId="type_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     onchange="changeCommunicationType(this)"
                                     readonly="${op != 'create'}"
                                     tabindex="1">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="communicationTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="actionTypeId_id">
                        <fmt:message key="SalesProcessAction.actionType"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <fanta:select property="dto(actionTypeId)"
                                      styleId="actionTypeId_id"
                                      listName="actionTypeBaseList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      onChange="javascript:changeActionType(this);"
                                      readOnly="${op == 'delete'}"
                                      module="/sales"
                                      firstEmpty="true"
                                      tabIndex="2">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <c:if test="${email != actionForm.dtoMap['type']}">
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="note_id">
                            <fmt:message key="Document.subject"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(note)"
                                      styleId="note_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      tabindex="3"
                                      view="${op == 'delete'}"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="top ${app2:getFormLabelClassesTwoColumns()}" for="number_id">
                            <fmt:message key="SalesProcessAction.number"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(number)"
                                      styleId="number_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="30"
                                      tabindex="4"
                                      view="${'delete' == op}"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="fieldContactPersonName_id">
                            <fmt:message key="Document.contactPerson"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="input-group">
                                <app:text property="dto(contactPersonName)"
                                          styleClass="mediumText ${app2:getFormInputClasses()}" view="${op == 'delete'}"
                                          readonly="true"
                                          styleId="fieldContactPersonName_id"
                                          tabindex="5"/>
                                <html:hidden property="dto(contactPersonId)" styleId="fieldContactPersonId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup styleId="searchContactPerson_id"
                                                               url="/contacts/ContactPerson/Search.do?contactId=${isSalesProcess ? param.addressId : param.contactId}"
                                                               name="searchContactPerson"
                                                               isLargeModal="true"
                                                               titleKey="Common.search"
                                                               modalTitleKey="ContactPerson.search"
                                                               hide="${op == 'delete'}"
                                                               submitOnSelect="${actionForm.dtoMap['type'] == email}"
                                                               tabindex="6"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldContactPersonId_id"
                                                                    nameFieldId="fieldContactPersonName_id"
                                                                    titleKey="Common.clear"
                                                                    hide="${op == 'delete'}"
                                                                    submitOnClear="${actionForm.dtoMap['type']==email}"
                                                                    tabindex="7"/>
                                </span>
                            </div>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="probability_id">
                            <fmt:message key="SalesProcess.probability"/> (<fmt:message key="Common.probabilitySymbol"/>)
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}"
                                <c:if test="${actionForm.dtoMap['type'] == null || actionForm.dtoMap['type'] == ''}"></c:if>>
                            <c:if test="${op == 'create'}">
                                <c:set var="probabilityValue"
                                       value="${isSalesProcess ? (probability) : (param.probability) }"/>
                            </c:if>
                            <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                            <html:select property="dto(probability)"
                                         styleId="probability_id"
                                         styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                         readonly="${op == 'delete'}"
                                         tabindex="8"
                                         value="${(op == 'create' && actionForm.dtoMap['probability'] == null) ? (probabilityValue) : (actionForm.dtoMap['probability'])}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="probabilities" property="value" labelProperty="label"/>
                            </html:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="dateStart">
                            <fmt:message key="Document.date"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="input-group date">
                                <fmt:message var="datePattern" key="datePattern"/>
                                <app:dateText property="dto(dateStart)" styleId="dateStart"
                                              calendarPicker="${op != 'delete' && actionForm.dtoMap['type']!=email}"
                                              datePatternKey="${datePattern}"
                                              styleClass="mediumText ${app2:getFormInputClasses()}"
                                              view="${op == 'delete'}"
                                              mode="bootstrap"
                                              maxlength="10"
                                              tabindex="9"
                                              currentDate="true"
                                              readonly="${actionForm.dtoMap['type'] == email}"/>
                            </div>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                            <fmt:message key="Common.active"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(active)"
                                                   styleId="active_id"
                                                   disabled="${op == 'delete'}"
                                                   styleClass="radio"
                                                   value="true"
                                                   tabindex="10"/>
                                    <label for="active_id"></label>
                                </div>
                            </div>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="netGross_id">
                            <fmt:message key="SalesProcessAction.netGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
                            <html:select property="dto(netGross)"
                                         styleId="netGross_id"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         readonly="${'delete' == op}"
                                         tabindex="11">
                                <html:option value=""/>
                                <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
                            </html:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="SalesProcess.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete'|| actionForm.dtoMap['type']==email)}">
                            <fanta:select property="dto(employeeId)"
                                          styleId="employeeId_id"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'|| actionForm.dtoMap['type']==email}"
                                          module="/contacts"
                                          tabIndex="12"
                                          value="${sessionScope.user.valueMap['userAddressId']}">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                            <fmt:message key="SalesProcessAction.currency"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="dto(currencyId)"
                                          styleId="currencyId_id"
                                          listName="basicCurrencyList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${'delete' == op}"
                                          module="/catalogs"
                                          tabIndex="13">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Action.createDateTime"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <c:if test="${'create' != op && null != actionForm.dtoMap['createDateTime']}">
                                <html:hidden property="dto(createDateTime)"/>
                                <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['createDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="value_id">
                            <fmt:message key="SalesProcess.value"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <c:choose>
                                <c:when test="${op == 'update'}">
                                    <div class="row col-xs-11">
                                        <app:numberText property="dto(value)"
                                                        styleId="value_id"
                                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                                        maxlength="18"
                                                        view="${'delete' == op}"
                                                        numberType="decimal"
                                                        maxInt="12"
                                                        maxFloat="2"
                                                        tabindex="14"/>
                                    </div>
                            <span class="pull-right">
                                <html:submit property="calculate" styleId="calculate_id" value=" &nbsp;&nbsp;"
                                             styleClass="calculatorButton cancel btn btn-link"
                                             titleKey="ActionPosition.calculate" tabindex="14"/>

                            </span>
                                </c:when>
                                <c:otherwise>
                                    <app:numberText property="dto(value)"
                                                    styleClass="numberText ${app2:getFormInputClasses()}"
                                                    maxlength="18"
                                                    view="${'delete' == op}"
                                                    numberType="decimal"
                                                    maxInt="12"
                                                    maxFloat="2"
                                                    tabindex="15"/>
                                </c:otherwise>
                            </c:choose>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="userName_id">
                            <fmt:message key="Action.creatorUser"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <c:if test="${'create' == op}">
                                <c:out value="${app2:getAddressName(sessionScope.user.valueMap['userAddressId'])}"/>
                            </c:if>
                            <c:if test="${'update' == op || 'delete' == op}">
                                <app:text property="dto(userName)"
                                          styleId="userName_id"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          maxlength="20"
                                          readonly="true"
                                          tabindex="16"
                                          view="true"/>
                            </c:if>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${actionForm.dtoMap['type'] == fax || actionForm.dtoMap['type'] == letter}">

                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="top ${app2:getFormLabelClassesTwoColumns()}" for="templateId">
                                    <fmt:message key="Document.template"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <html:hidden property="dto(rebuildDocument)"/>
                                    <!--this field is as flag, set in true when the document (fax,letter) should be regererated-->

                                    <c:set var="mediatype_WORD"
                                           value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
                                    <fanta:select property="dto(templateId)" styleId="templateId"
                                                  listName="templateList"
                                                  labelProperty="description" valueProperty="id" module="/catalogs"
                                                  firstEmpty="true"
                                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="${'delete' == op}" tabIndex="17">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                        <fanta:parameter field="mediaType" value="${mediatype_WORD}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
                                    <fmt:message key="Communication.additionalAddress"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <fanta:select property="dto(additionalAddressId)"
                                                  styleId="additionalAddressId_id"
                                                  listName="additionalAddressSelectList"
                                                  labelProperty="name" valueProperty="additionalAddressId"
                                                  module="/contacts"
                                                  preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                                  firstEmpty="true"
                                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="${'delete' == op}" tabIndex="18">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                        <fanta:parameter field="addressId"
                                                         value="${isSalesProcess ? param.addressId : param.contactId}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>

                        <c:if test="${'update' == op}">
                            <div class="row">
                                <div class="${app2:getFormGroupClassesTwoColumns()}">
                                    <label class=" ${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="Action.updateDateTime"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns('update' == op)}">
                                        <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                                            <html:hidden property="dto(updateDateTime)"/>
                                            <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                    </c:when>
                    <c:when test="${phone == actionForm.dtoMap['type'] || meeting == actionForm.dtoMap['type'] || other == actionForm.dtoMap['type']}">

                        <c:set var="colspanRow" value="3"/>
                        <c:if test="${'update' == op}">
                            <c:set var="colspanRow" value=""/>
                        </c:if>

                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
                                    <fmt:message key="Communication.additionalAddress"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <fanta:select property="dto(additionalAddressId)"
                                                  styleId="additionalAddressId_id"
                                                  listName="additionalAddressSelectList"
                                                  labelProperty="name" valueProperty="additionalAddressId"
                                                  module="/contacts"
                                                  preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                                  firstEmpty="true"
                                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="${'delete' == op}" tabIndex="19">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                        <fanta:parameter field="addressId"
                                                         value="${isSalesProcess ? param.addressId : param.contactId}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <c:if test="${'update' == op}">
                                <div class="${app2:getFormGroupClassesTwoColumns()}">
                                    <label class=" ${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="Action.updateDateTime"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns('update' == op)}">
                                        <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                                            <html:hidden property="dto(updateDateTime)"/>
                                            <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </div>

                        <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-12 label-left row">
                                    <fmt:message key="Document.content"/>
                                </label>

                                <div class="col-xs-12 col-sm-12 row">
                                    <html:textarea property="dto(freeText)"
                                                   rows="5"
                                                   styleClass="mediumDetail ${app2:getFormInputClasses()}"
                                                   readonly="${op == 'delete'}"
                                                   tabindex="20"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${document == actionForm.dtoMap['type']}">

                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="documentFile_id">
                                    <fmt:message key="Communication.document"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' ==  op)}">
                                    <fmt:message key="Common.download" var="downloadMsg"/>
                                    <c:if test="${'create' == op || 'update' ==  op}">
                                        <c:if test="${'update' == op}">
                                            <div class="row col-xs-11">
                                        </c:if>
                                        <tags:bootstrapFile property="dto(documentFile)" styleId="documentFile_id"
                                                            tabIndex="21"/>
                                        <c:if test="${'update' == op}">
                                            </div>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${'update' == op}">
                                        <span class="pull-right">
                                            <html:hidden property="dto(documentFileName)"/>
                                            <app:link
                                                    action="/SalesProcess/Action/Document/Download.do?communicationId=${actionForm.dtoMap['contactId']}&processId=${actionForm.dtoMap['processId']}&dto(processId)=${actionForm.dtoMap['processId']}"
                                                    title="${downloadMsg}"
                                                    tabindex="21"
                                                    addModuleParams="true" styleClass="${app2:getFormButtonClasses()}">
                                                <span title="${downloadMsg}"
                                                      class="glyphicon glyphicon-download-alt"></span>
                                            </app:link>
                                        </span>
                                    </c:if>
                                    <c:if test="${'delete' == op}">
                                        <c:out value="${actionForm.dtoMap['documentFileName']}"/>
                                    </c:if>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
                                    <fmt:message key="Communication.additionalAddress"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <fanta:select property="dto(additionalAddressId)"
                                                  styleId="additionalAddressId_id"
                                                  listName="additionalAddressSelectList"
                                                  labelProperty="name" valueProperty="additionalAddressId"
                                                  module="/contacts"
                                                  preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                                  firstEmpty="true"
                                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="${'delete' == op}" tabIndex="22">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                        <fanta:parameter field="addressId"
                                                         value="${isSalesProcess ? param.addressId : param.contactId}"/>
                                    </fanta:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>

                        <c:if test="${'update' == op}">
                            <div class="row">
                                <div class="${app2:getFormGroupClassesTwoColumns()}">
                                    <label class=" ${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="Action.updateDateTime"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns('update' == op)}">
                                        <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                                            <html:hidden property="dto(updateDateTime)"/>
                                            <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </c:if>
                <%-- if email--%>
            <c:if test="${email == actionForm.dtoMap['type']}">
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="dateStart">
                            <fmt:message key="Document.date"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fmt:message var="datePattern" key="datePattern"/>
                            <app:dateText property="dto(dateStart)" styleId="dateStart"
                                          calendarPicker="false"
                                          datePatternKey="${datePattern}"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          view="${op == 'delete'}"
                                          mode="bootstrap"
                                          maxlength="10"
                                          tabindex="3"
                                          currentDate="true"
                                          readonly="true"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="number_id">
                            <fmt:message key="SalesProcessAction.number"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:text property="dto(number)"
                                      styleId="number_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="30"
                                      tabindex="4"
                                      view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="netGross_id">
                            <fmt:message key="SalesProcessAction.netGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
                            <html:select property="dto(netGross)"
                                         styleId="netGross_id"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         readonly="${'delete' == op}"
                                         tabindex="5">
                                <html:option value=""/>
                                <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="probability_id">
                            <fmt:message key="SalesProcess.probability"/>
                            (<fmt:message key="Common.probabilitySymbol"/>)
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                            <c:if test="${op == 'create'}">
                                <c:set var="probabilityValue"
                                       value="${isSalesProcess ? (probability) : (param.probability)}"/>
                            </c:if>
                            <html:select property="dto(probability)"
                                         styleId="probability_id"
                                         styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                         readonly="${op == 'delete'}"
                                         tabindex="6"
                                         value="${(op == 'create' && actionForm.dtoMap['probability'] == null) ? (probabilityValue) : (actionForm.dtoMap['probability'])}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="probabilities" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>

                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                            <fmt:message key="SalesProcessAction.currency"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <fanta:select property="dto(currencyId)"
                                          styleId="currencyId_id"
                                          listName="basicCurrencyList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${'delete' == op}"
                                          module="/catalogs"
                                          tabIndex="7">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                            <fmt:message key="Common.active"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(active)"
                                                   styleId="active_id"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="8"
                                                   styleClass="radio"
                                                   value="true"/>
                                    <label for="active_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="value_id">
                            <fmt:message key="SalesProcess.value"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:numberText property="dto(value)"
                                            styleId="value_id"
                                            styleClass="numberText ${app2:getFormInputClasses()}"
                                            maxlength="18"
                                            view="${'delete' == op}"
                                            numberType="decimal"
                                            maxInt="12"
                                            maxFloat="2"
                                            tabindex="9"/>
                            <c:if test="${op == 'update'}">
                                <html:submit property="calculate" value=" " styleClass="calculatorButton"
                                             titleKey="ActionPosition.calculate" tabindex="9"/>
                            </c:if>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="SalesProcess.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <fanta:select property="dto(employeeId)"
                                          styleId="employeeId_id"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}"
                                          module="/contacts"
                                          tabIndex="10"
                                          value="${sessionScope.user.valueMap['userAddressId']}">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Action.createDateTime"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <c:if test="${'create' != op && null != actionForm.dtoMap['createDateTime']}">
                                <html:hidden property="dto(createDateTime)"/>
                                <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['createDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                            </c:if>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="userName_id">
                            <fmt:message key="Action.creatorUser"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('create' == op)}">
                            <c:if test="${'create' == op}">
                                <c:out value="${app2:getAddressName(sessionScope.user.valueMap['userAddressId'])}"/>
                            </c:if>
                            <c:if test="${'update' == op || 'delete' == op}">
                                <app:text property="dto(userName)"
                                          styleId="userName_id"
                                          styleClass="mediumText"
                                          maxlength="20"
                                          readonly="true"
                                          tabindex="11"
                                          view="true"/>
                            </c:if>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <c:if test="${'update' == op}">
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class=" ${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Action.updateDateTime"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <c:if test="${null != actionForm.dtoMap['updateDateTime']}">
                                    <html:hidden property="dto(updateDateTime)"/>
                                    <c:out value="${app2:getDateWithTimeZone(actionForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:if>
                    <c:set var="mainCommunicationForm" value="${actionForm}" scope="request"/>

                    <c:set var="labelWidthAttach" value="15%" scope="request"/>
                    <c:set var="containWidthAttach" value="85%" scope="request"/>

                    <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
            </c:if>
        </fieldset>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <c:if test="${'update' == op && null != forwardOptions}">
                <div class="form-group col-xs-12 col-sm-4">
                    <div class="input-group">
                        <html:select property="dto(forwardOption)"
                                     style="border-radius: 4px"
                                     styleClass="select ${app2:getFormSelectClasses()} ignore"
                                     styleId="bottomForwardOptionSelect">
                            <html:options collection="forwardOptions"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="input-group-btn" style="padding-left: 5px !important; border-radius: 4px">
                        <html:button property=""
                                     style="border-radius: 4px"
                                     onclick="goToForward('bottomForwardOptionSelect')"
                                     styleClass="button ${app2:getFormButtonClasses()} marginLeft">
                            <fmt:message key="Common.go"/>
                        </html:button>
                    </span>
                    </div>
                </div>
            </c:if>
            <c:choose>
                <c:when test="${actionForm.dtoMap['type'] == letter || actionForm.dtoMap['type'] == fax}">

                    <app2:securitySubmit property="${saveButton}" operation="${op}"
                                         functionality="SALESPROCESSACTION"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                         styleId="saveButtonId"
                                         tabindex="90">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>

                    <c:if test="${op != 'delete'}">
                        <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="EXECUTE">
                            <html:submit property="${generateButton}"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                         tabindex="90">
                                <fmt:message key="Document.generate"/>
                            </html:submit>
                        </app2:checkAccessRight>

                        <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
                            <c:if test="${not empty docInfo.freeTextId}">
                                <html:button property=""
                                             styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                             onclick="location.href='${openDocUrl}'"
                                             tabindex="90">
                                    <fmt:message key="Document.open"/>
                                </html:button>

                                <c:if test="${isActionSendViaEmail}">
                                    <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                                        <html:button property=""
                                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                                     onclick="sendDocumentViaEmail('belowToEmail_id', '${docInfo.freeTextId}')"
                                                     tabindex="90">
                                            <fmt:message key="SalesProcessAction.sendViaEmail"/>
                                        </html:button>
                                        <div class="form-group col-xs-12 col-sm-3">
                                            <app:telecomSelect property="toEmail"
                                                               styleId="belowToEmail_id"
                                                               tabindex="90"
                                                               telecomIdColumn="telecomid"
                                                               numberColumn="telecomnumber"
                                                               telecomType="${TELECOMTYPE_EMAIL}"
                                                               addressId="${isSalesProcess ? param.addressId : param.contactId}"
                                                               contactPersonId="${actionForm.dtoMap['contactPersonId']}"
                                                               showOwner="true"
                                                               styleClass="${app2:getFormSelectClasses()}"
                                                               optionStyleClass="list" showDescription="false"
                                                               selectPredetermined="true"/>
                                        </div>
                                    </app2:checkAccessRight>
                                </c:if>
                            </c:if>
                        </app2:checkAccessRight>
                    </c:if>
                </c:when>
                <c:when test="${actionForm.dtoMap['type'] == email}">
                    <c:choose>
                        <c:when test="${'create' == op}">
                            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                                <app2:securitySubmit operation="${op}"
                                                     functionality="SALESPROCESSACTION"
                                                     property="${sendButton}"
                                                     styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                                     tabindex="90">
                                    <fmt:message key="Common.send"/>
                                </app2:securitySubmit>
                            </app2:checkAccessRight>
                        </c:when>
                        <c:when test="${null != op1}">
                            &nbsp;
                        </c:when>
                        <c:otherwise>
                            <app2:securitySubmit property="${saveButton}" operation="${op}"
                                                 functionality="SALESPROCESSACTION"
                                                 styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                                 styleId="saveButtonId"
                                                 tabindex="90">
                                <c:out value="${button}"/>
                            </app2:securitySubmit>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit property="${saveButton}" operation="${op}"
                                         functionality="SALESPROCESSACTION"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                         styleId="saveButtonId"
                                         tabindex="90">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>

            <app2:checkAccessRight functionality="SALE" permission="CREATE">
                <c:if test="${'update' == op && 100 == actionForm.dtoMap['probability'] && actionForm.dtoMap['type'] != email}">
                    <html:button property="createSaleButton"
                                 styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                 onclick="javascript:createSale();"
                                 tabindex="90">
                        <fmt:message key="SalessProcess.createSale"/>
                    </html:button>
                </c:if>
            </app2:checkAccessRight>

            <html:cancel
                    styleClass="button ${app2:getFormButtonCancelClasses()} pull-left marginRight marginLeft marginButton"
                    tabindex="90"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
<c:if test="${'update' == op }">
    <c:if test="${isSalesProcess==true}">
        <c:set value="/SalesProcess/ActionPosition/Forward/Create.do?dto(contactId)=${actionForm.dtoMap['contactId']}&dto(note)=${app2:encode(actionForm.dtoMap['note'])}&dto(processId)=${actionForm.dtoMap['processId']}"
               var="urlCreate"/>
    </c:if>
    <c:if test="${isSalesProcess==false}">
        <c:set value="/SalesProcess/ActionPosition/Forward/Create.do?dto(contactId)=${actionForm.dtoMap['contactId']}&dto(processId)=${actionForm.dtoMap['processId']}&processId=${actionForm.dtoMap['processId']}&dto(note)=${app2:encode(actionForm.dtoMap['note'])}"
               var="urlCreate"/>
    </c:if>
    <br/><br/>

    <div class="row">
        <div class="col-xs-12">
            <html:form action="${urlCreate}" styleClass="form-horizontal">
                <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="CREATE">
                    <html:submit property="new"
                                 styleClass="button cancel ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </app2:checkAccessRight>
            </html:form>
            <html:form action="SalesProcess/Action/Forward/Update.do?cmd=true" styleClass="form-horizontal">
                <html:hidden property="dto(contactId)" value="${actionForm.dtoMap['contactId']}"/>
                <html:hidden property="dto(note)" value="${actionForm.dtoMap['note']}"/>
                <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="CREATE">
                    <div class="form-group col-xs-12 col-sm-4">
                        <div class="input-group">
                            <span class="input-group-btn">
                                <html:submit property="copyFromButton"
                                             style="padding-right: 5px !important; border-radius: 4px"
                                             styleClass="button cancel ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton">
                                    <fmt:message key="Common.copyFrom"/>
                                </html:submit>
                            </span>
                            <fanta:select property="dto(copyFromContactId)"
                                          listName="actionList"
                                          labelProperty="note"
                                          valueProperty="contactId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()} marginLeft"
                                          module="/sales"
                                          firstEmpty="true">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="processId"
                                                 value="${not empty actionForm.dtoMap['processId']?actionForm.dtoMap['processId']:0}"/>
                                <fanta:parameter field="contactId"
                                                 value="${not empty actionForm.dtoMap['contactId']?actionForm.dtoMap['contactId']:0}"/>
                            </fanta:select>
                        </div>
                    </div>
                </app2:checkAccessRight>
            </html:form>
        </div>
    </div>
    <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="VIEW">
        <div class="embed-responsive embed-responsive-16by9 col-xs-12">
            <iframe name="frame1"
                    src="<app:url value="SalesProcess/ActionPosition/List.do?dto(contactId)=${actionForm.dtoMap['contactId']}&dto(processId)=${actionForm.dtoMap['processId']}&dto(note)=${app2:encode(dto.note)}&processId=${actionForm.dtoMap['processId']}"/>"
                    class="embed-responsive-item" scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </div>
    </app2:checkAccessRight>
</c:if>

<tags:jQueryValidation formName="actionForm"/>