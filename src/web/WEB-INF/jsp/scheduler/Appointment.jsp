<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/scheduler/scheduler.jsp"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.blockUI-2.70.0.js"/>
<c:if test="${empty enableParticipantLink}">
    <c:set var="enableParticipantLink" value="${false}"/>
</c:if>

<c:if test="${empty enableSaveAndGotoOptions}">
    <c:set var="enableSaveAndGotoOptions" value="true" scope="request"/>
</c:if>
<c:if test="${empty enableContactInputField}">
    <c:set var="enableContactInputField" value="true" scope="request"/>
</c:if>
<c:if test="${empty enableCalendarSelectField}">
    <c:set var="enableCalendarSelectField" value="false" scope="request"/>
</c:if>
<c:if test="${empty pageSchedulerUserId}">
    <c:set var="pageSchedulerUserId" value="${sessionScope.user.valueMap['schedulerUserId']}" scope="request"/>
</c:if>
<c:if test="${empty deleteUrl}">
    <c:set var="deleteUrl"
           value="/scheduler/Appointment/Forward/Delete.do?dto(title)=${app2:encode(param['dto(title)'])}&dto(appointmentId)=${param.appointmentId}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&operation=delete"
           scope="request"/>
</c:if>

<c:set var="calendarUsers" value="${app2:getCalendarUsers(pageContext.request)}"/>
<c:set var="calendarUsersConunter" value="${fn:length(calendarUsers)}"/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<%
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
    pageContext.setAttribute("reminderTypeList", JSPHelper.getReminderTypeList(request));
    pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
    pageContext.setAttribute("timeBeforeList", JSPHelper.getTimeBefore());
    pageContext.setAttribute("optionBackList", JSPHelper.getReturnList(request));
    pageContext.setAttribute("returnDeleteList", JSPHelper.getReturnDeleteList(request));
    pageContext.setAttribute("returnSearchList", SchedulerConstants.RETURN_SEARCHLIST);
%>
<app2:jScriptUrl url="/calendar.html" var="jsUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="date" value="field.value"/>
    <app2:jScriptUrlParam param="field" value="field.id"/>
    <app2:jScriptUrlParam param="field_" value="field_.id"/>
</app2:jScriptUrl>


<script language="JavaScript">
    function changeStartDateValue() {
        $("#endDate").val($("#startDate").val());
    }
    function openCalendarPickerScheduler(field, field_) {
        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - 244 / 2;
        var posy = winy - 190 / 2;
        calendarWindow = window.open(${jsUrl}, 'calendarWindowScheduler', 'resizable=no,scrollbars=no,width=244,height=190,left=' + posx + ',top=' + posy);
        calendarWindow.focus();
    }

    function evaluateSubmit(idHidden, propertyHidden) {
        addSubmitActionHidden(idHidden, propertyHidden);

        if (isCreateToOtherUser()) {
            $.blockUI({message: $('#questionPanel'), css: {padding: '20px'}});
        } else {
            submitForm();
        }
    }

    function isCreateToOtherUser() {
        var op = '${op}';
        var sessionUserId = '${sessionScope.user.valueMap['userId']}';
        var userId = document.getElementById("userId_Id").value;

        var calendarOf = document.getElementById("calendarOfId");
        if (calendarOf != undefined) {
            userId = calendarOf.value;
        }

        if (op == 'create' && userId != '' && userId != sessionUserId) {
            return true;
        } else {
            return false;
        }
    }


    function addSubmitActionHidden(idHidden, propertyHidden) {
        var submitForm = document.getElementById("appointmentFormId");
        var hiddenField = document.getElementById(idHidden);

        if (hiddenField == undefined) {
            hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("id", idHidden);
            hiddenField.setAttribute("name", propertyHidden);
            hiddenField.setAttribute("value", "Save");

            submitForm.appendChild(hiddenField);
        }
    }

    function submitForm() {
        var exceptionList = document.getElementById('exception');
        send(exceptionList);
        document.getElementById("appointmentFormId").submit();
    }

    function questionYesButton() {
        $.unblockUI();
        document.getElementById("sendAppNotificationId").value = "true";
        submitForm();
    }

    function questionNoButton() {
        $.unblockUI();
        document.getElementById("sendAppNotificationId").value = "";
        submitForm();
    }

</script>

<c:set var="isPublicApp"
       value="${empty appointmentForm.dtoMap.isPrivate or appointmentForm.dtoMap.isPrivate eq 'false'}"/>

<c:set var="hasUserEditRightAsParticipant" value="${app2:hasUserEditAppointmentPermission(appointmentForm.dtoMap.createdById, sessionScope.user.valueMap['userId'], isPublicApp)}"/>

<c:set var="userPermissionIsNULL"
       value="${publicSchedulerUserPermission == null && privateSchedulerUserPermission == null}"/>

<c:set var="rightEditAppointment"
       value="${(userPermissionIsNULL ? true : app2:hasEditAppointmentPermission(publicSchedulerUserPermission,privateSchedulerUserPermission,isPublicApp)) and (appointmentForm.dtoMap.userId == pageSchedulerUserId or hasUserEditRightAsParticipant)}"/>
<c:set var="rightDelAppointment"
       value="${(userPermissionIsNULL ? true : app2:hasDelAppointmentPermission(publicSchedulerUserPermission,privateSchedulerUserPermission,isPublicApp)) and (appointmentForm.dtoMap.userId == pageSchedulerUserId)}"/>

<c:set var="readOnly" value="${op == 'delete' || (op == 'update' && !rightEditAppointment)}" scope="request"/>
<c:set var="opUpdate" value="${op == 'create' || (op == 'update' && rightEditAppointment)}" scope="request"/>

<br>
<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="datePattern" key="datePattern"/>
<html:form action="${action}?oldType=${param.type}" focus="dto(title)" styleId="appointmentFormId"
           styleClass="form-horizontal">

    <%--modal dialog to show confirmation to send notification--%>
    <div id="questionPanel" style="display:none; cursor: default">
    <span style="font-size:10pt; color:#000000; font-family:Verdana,serif;">
        <fmt:message key="Appointment.question.ownerSendNotification"/>
    </span>
        <input type="submit" id="questionYes" value="<fmt:message key="Common.yes"/>" onclick="questionYesButton()"
               class="button"/>
        <input type="button" id="questionNo" value="<fmt:message key="Common.no"/>" onclick="questionNoButton()"
               class="button"/>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <c:if test="${opUpdate || (op=='delete' && rightDelAppointment)}">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="${ope}">
                    <html:button
                            styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                            onclick="evaluateSubmit('onlySaveId','dto(onlySave)')" property="dto(onlySave)">
                        ${button}
                    </html:button>
                    <c:choose>
                        <c:when test="${enableSaveAndGotoOptions}">
                            <html:button
                                    styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                    onclick="evaluateSubmit('saveId','dto(save)')" property="dto(save)">
                                ${buttonGoTo}
                            </html:button>
                            <c:choose>
                                <c:when test="${op == 'delete'}">
                                    <div class="form-group col-xs-12 col-sm-3">
                                        <html:select property="dto(returnType)"
                                                     styleClass="select ${app2:getFormSelectClasses()}"
                                                     onchange="returnListLow()"
                                                     styleId="returnTypeLow">
                                            <html:options collection="returnDeleteList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group col-xs-12 col-sm-3">
                                        <html:select property="dto(returnType)"
                                                     styleClass="select ${app2:getFormSelectClasses()}"
                                                     onchange="returnListLow()"
                                                     styleId="returnTypeLow">
                                            <html:options collection="optionBackList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="dto(returnType)" value="${returnSearchList}"/>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${'update' == op && rightDelAppointment}">
                <app:url var="url" value="${deleteUrl}" contextRelative="true"/>

                <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                    <html:button onclick="location.href='${url}'"
                                 styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                 property="dto(save)">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel
                    styleClass="button ${app2:getFormButtonCancelClasses()} pull-left marginRight marginLeft marginButton">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
            <c:if test="${enableParticipantLink}">
                <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="VIEW">
                    <app:link
                            action="/scheduler/Appointment/ParticipantList.do?appointmentId=${appointmentForm.dtoMap.appointmentId}&dto(appointmentId)=${appointmentForm.dtoMap.appointmentId}&onlyView=${param.onlyView}&dto(title)=${app2:encode(title)}&calendar=${param.calendar}&type=${param.type}&tabKey=Appointment.Tab.participants&index=1"
                            styleClass="folderTabLink btn btn-link pull-left removeMarginButton" contextRelative="true">
                        <fmt:message key="Appointment.link.participants"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>
        </div>
    </div>

    <html:hidden property="dto(op)" value="${op}" styleId="1"/>
    <html:hidden property="dto(ope)" value="${ope}" styleId="2"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}" styleId="3"/>
    <html:hidden property="dto(calendar)" value="${param.calendar}" styleId="4"/>
    <c:set var="value" value="4"/>

    <c:choose>
        <c:when test="${'create' != op and hasUserEditRightAsParticipant}">
            <html:hidden property="dto(userId)" styleId="userId_Id"/>
        </c:when>
        <c:otherwise>
            <html:hidden property="dto(userId)" value="${pageSchedulerUserId}" styleId="userId_Id"/>
        </c:otherwise>
    </c:choose>

    <html:hidden property="dto(sendAppNotification)" styleId="sendAppNotificationId"/>

    <c:if test="${pageSchedulerUserId != sessionScope.user.valueMap['userId'] && op == 'create'}">
        <c:set var="owner" value="true"/>
    </c:if>
    <c:if test="${pageSchedulerUserId == sessionScope.user.valueMap['userId'] && op == 'create'}">
        <c:set var="value" value="5"/>
    </c:if>

    <c:if test="${'create' == op}">
        <html:hidden property="dto(createdById)" value="${sessionScope.user.valueMap['userId']}"/>
    </c:if>

    <c:if test="${'create' != op}">
        <html:hidden property="dto(version)" styleId="6"/>
        <html:hidden property="dto(appointmentId)" styleId="7"/>
        <html:hidden property="dto(createdById)"/>
    </c:if>

    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <c:out value="${titlePage}"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                    <fmt:message key="Appointment.name"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                    <app:text property="dto(title)"
                              styleId="title_id"
                              styleClass="middleText ${app2:getFormInputClasses()}" maxlength="60"
                              tabindex="1" view="${readOnly}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                    <fmt:message key="Appointment.contact"/>
                </label>


                <c:if test="${not empty appointmentForm.dtoMap['addressId'] and 'update' == op}">
                    <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                            <c:set var="addressMap"
                                   value="${app2:getAddressMap(appointmentForm.dtoMap['addressId'])}"/>
                            <c:choose>
                                <c:when test="${personType == addressMap['addressType']}">
                                    <c:set var="addressEditLink"
                                           value="/contacts/Person/Forward/Update.do?contactId=${appointmentForm.dtoMap['addressId']}&dto(addressId)=${appointmentForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="addressEditLink"
                                           value="/contacts/Organization/Forward/Update.do?contactId=${appointmentForm.dtoMap['addressId']}&dto(addressId)=${appointmentForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                </c:otherwise>
                            </c:choose>
                        </app2:checkAccessRight>
                    </div>
                </c:if>

                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>

                <c:set value="${op == 'delete' || readOnly}" var="showIconEditOnly"/>

                <c:choose>
                    <c:when test="${enableContactInputField}">
                        <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                            <div class="${showIconEditOnly ? 'col-xs-11 row' : 'input-group'}">
                                <app:text property="dto(contact)" styleId="fieldAddressName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="2" view="${readOnly}" readonly="${opUpdate}"/>
                                <c:if test="${showIconEditOnly}">
                            </div>
                            <div>
                                </c:if>
                            <span class="${showIconEditOnly ? 'pull-right':'input-group-btn'}">
                                <c:if test="${not empty addressEditLink}">
                                    <app:link action="${addressEditLink}"
                                              styleClass="${showIconEditOnly ? '': app2:getFormButtonClasses()}"
                                              contextRelative="true" titleKey="Common.edit">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:if>

                                <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                           isLargeModal="true"
                                                           url="/contacts/SearchAddress.do"
                                                           name="searchAddress"
                                                           titleKey="Common.search"
                                                           tabindex="2"
                                                           modalTitleKey="Contact.Title.search"
                                                           hide="${op == 'delete' || readOnly}" submitOnSelect="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                nameFieldId="fieldAddressName_id"
                                                                titleKey="Common.clear"
                                                                tabindex="2"
                                                                hide="${op == 'delete' || readOnly}"
                                                                submitOnClear="true"/>
                            </span>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <app:text property="dto(contact)"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      tabindex="2"
                                      view="true"
                                      readonly="true"/>
                        </div>
                    </c:otherwise>
                </c:choose>
                <span class="glyphicon form-control-feedback iconValidation"></span>

            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="appointmentTypeId_id">
                    <fmt:message key="Appointment.appType"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                    <fanta:select property="dto(appointmentTypeId)"
                                  styleId="appointmentTypeId_id"
                                  listName="appointmentTypeList"
                                  labelProperty="name" valueProperty="appointmentTypeId"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${readOnly}" module="/scheduler" firstEmpty="true" tabIndex="3">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Appointment.contactPerson"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                    <fanta:select property="dto(contactPersonId)" tabIndex="4" listName="searchContactPersonList"
                                  module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                                  valueProperty="contactPersonId"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${readOnly}">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="addressId"
                                         value="${not empty appointmentForm.dtoMap['addressId']?appointmentForm.dtoMap['addressId']:0}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                    <fmt:message key="Task.priority"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                    <fanta:select property="dto(priorityId)"
                                  styleId="priorityId_id"
                                  listName="selectPriorityList"
                                  labelProperty="name" valueProperty="id"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${readOnly}" module="/catalogs" firstEmpty="true" tabIndex="5">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="type" value="SCHEDULER"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="location_id">
                    <fmt:message key="Appointment.location"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(readOnly)}">
                    <app:text property="dto(location)"
                              styleId="location_id"
                              styleClass="middleText ${app2:getFormInputClasses()}"
                              maxlength="30"
                              tabindex="6"
                              view="${readOnly}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="isAllDay">
                    <fmt:message key="Appointment.startDate"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumnsLarge(readOnly && !op=='delete')}">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(isAllDay)" styleId="isAllDay" disabled="${readOnly}"
                                           onclick="deshabilitaAllDay1()"
                                           tabindex="7" styleClass=""/>
                            <label for="isAllDay"><fmt:message key="Appointment.allDay"/></label>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>

                    <div class="row" style="padding-top:5px;">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <div class="${readOnly ? '' : 'input-group date'}">
                                <app:dateText property="dto(startDate)"
                                              tabindex="7"
                                              onchange="changeStartDateValue()"
                                              mode="bootstrap"
                                              styleId="startDate" onkeyup="equalDate()"
                                              calendarPicker="${opUpdate}" datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}" view="${readOnly}"
                                              maxlength="10" currentDate="true"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
                            </div>
                        </div>
                        <abbr id="startDate_Id"  ${(appointmentForm.dtoMap.isAllDay =='true') ? "style=\"display: none;\"":""}>
                            <div class="col-xs-12 col-sm-6">
                                <div class="input-group">
                                    <html:select property="dto(startHour)" onchange="incrHour()" onkeyup="incrHour()"
                                                 tabindex="7"
                                                 style="border-radius:4px"
                                                 styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${readOnly}"
                                                 styleId="startHour">
                                        <html:options collection="hourList" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="${readOnly ? '' : 'input-group-addon'}"
                                          style="background-color: transparent;border:0px">:</span>
                                    <html:select property="dto(startMin)" onchange="equalMin()" onkeyup="equalMin()"
                                                 styleClass="select ${app2:getFormSelectClasses()}"
                                                 style="border-radius:4px"
                                                 tabindex="7" readonly="${readOnly}" styleId="startMin">
                                        <html:options collection="intervalMinList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </div>
                            </div>
                        </abbr>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="reminderId">
                    <fmt:message key="Appointment.reminder"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumnsLarge(readOnly && !op=='delete')}">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(reminder)" styleId="reminderId" disabled="${readOnly}"
                                           tabindex="8"
                                           styleClass="radio" value="true" onclick="habilitaReminder()"/>
                            <label for="reminderId"></label>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>

                    <div class="row">
                        <div id="reminderAreaId"
                             style="padding-top:5px;${appointmentForm.dtoMap['reminder'] != 'true' ? 'display:none' :''}">
                            <div class="col-xs-12 col-sm-4 wrapperButton">
                                <div id="reminder_1"   ${!(appointmentForm.dtoMap.reminderType == '1') ? "style=\"display: none;\"":""} >
                                    <html:select property="dto(timeBefore_1)" styleId="timeBefore"
                                                 styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${readOnly}"
                                                 tabindex="8" style="width:43">
                                        <html:options collection="timeBeforeList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>

                                </div>
                                <div id="reminder_2"  ${!(appointmentForm.dtoMap.reminderType != '1') ? "style=\"display: none;\"":""}>
                                    <app:text property="dto(timeBefore_2)" styleId="timeBefore_2"
                                              styleClass="tinyText ${app2:getFormInputClasses()}"
                                              maxlength="2"
                                              tabindex="8" view="${readOnly}"/>
                                </div>
                            </div>

                            <div class="col-xs-12 col-sm-5">
                                <html:select property="dto(reminderType)" styleId="reminderType_Id"
                                             onchange="changeReminderType()"
                                             styleClass="shortSelect ${app2:getFormSelectClasses()}" tabindex="8"
                                             readonly="${readOnly}">
                                    <html:options collection="reminderTypeList" property="value" labelProperty="label"/>
                                </html:select>
                            </div>
                            <div class="col-xs-12 col-sm-3" style="padding: 0">
                                <fmt:message key="Appointment.before"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="endDate">
                    <fmt:message key="Appointment.endDate"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumnsLarge(readOnly)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <div class="input-group date">
                                <app:dateText property="dto(endDate)" tabindex="9"
                                              styleId="endDate" calendarPicker="${opUpdate}"
                                              mode="bootstrap"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              view="${readOnly}" maxlength="10" currentDate="true"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <abbr id="endDate_Id"  ${(appointmentForm.dtoMap.isAllDay =='true') ? "style=\"display: none;\"":""}>
                                <div class="input-group">
                                    <html:select property="dto(endHour)" tabindex="9"
                                                 styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${readOnly}"
                                                 style="border-radius:4px"
                                                 styleId="endHour">
                                        <html:options collection="hourList" property="value" labelProperty="label"/>
                                    </html:select>
                                <span class="${readOnly ? '' : 'input-group-addon'}"
                                      style="background-color: transparent;border:0px">
                                    :
                                </span>
                                    <html:select property="dto(endMin)" tabindex="9"
                                                 styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${readOnly}"
                                                 style="border-radius:4px"
                                                 styleId="endMin">
                                        <html:options collection="intervalMinList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </div>
                            </abbr>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>

            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="isPrivate_id">
                    <fmt:message key="Appointment.private"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <c:set var="processDisable" value="${false}"/>
                    <c:set var="processPermited"
                           value="${app2:getProcessPermitedInAppoimtment(publicSchedulerUserPermission, privateSchedulerUserPermission, op, pageContext.request)}"/>
                    <c:set var="allApp" value="<%=SchedulerConstants.PROCESS_ALL_APP%>"/>
                    <c:set var="onlyPrivateApp" value="<%=SchedulerConstants.PROCESS_ONLYPRIVATE_APP%>"/>
                    <c:set var="onlyPublicApp" value="<%=SchedulerConstants.PROCESS_ONLYPUBLIC_APP%>"/>

                    <c:if test="${processPermited eq onlyPrivateApp || processPermited eq onlyPublicApp}">
                        <c:set var="processDisable" value="${true}"/>
                    </c:if>

                    <c:choose>
                        <c:when test="${op == 'create' && processDisable}">
                            <c:if test="${processPermited eq onlyPrivateApp}">
                                <html:hidden property="dto(isPrivate)" value="on"/>
                            </c:if>
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <input type="checkbox" name="tempIsPrivate" id="tempIsPrivate_id" class="radio"
                                           disabled="disabled"
                                           tabindex="10" ${processPermited eq onlyPrivateApp ?'checked="checked"':''}>
                                    <label for="tempIsPrivate_id"></label>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(isPrivate)" styleId="isPrivate_id"
                                                   disabled="${readOnly || processDisable}"
                                                   tabindex="10"
                                                   styleClass="radio"/>
                                    <label for="isPrivate_id"></label>
                                </div>
                            </div>
                            <c:if test="${!isPublicApp && op == 'update' && (readOnly || processDisable)}">
                                <html:hidden property="dto(isPrivate)" value="on"/>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="isRecurrence_id">
                    <fmt:message key="Appointment.repeat"/>
                </label>
                <c:choose>
                    <c:when test="${'create' == op && enableCalendarSelectField && calendarUsersConunter > 1}">
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-2">
                                    <div class="radiocheck">
                                        <div class="checkbox checkbox-default">
                                            <html:checkbox property="dto(isRecurrence)"
                                                           styleId="isRecurrence_id"
                                                           onclick="isRecurrence(this)"
                                                           disabled="${readOnly}"
                                                           tabindex="11"
                                                           styleClass="radio"/>
                                            <label for="isRecurrence_id"></label>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-2">
                                    <div class="radiocheck">
                                        <div class="checkbox checkbox-default">
                                            <html:checkbox property="dto(isRecurrence)"
                                                           styleId="isRecurrence_id"
                                                           onclick="isRecurrence(this)" disabled="${readOnly}"
                                                           tabindex="11"
                                                           styleClass="radio"/>
                                            <label for="isRecurrence_id"></label>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:choose>
                <c:when test="${'create' == op && enableCalendarSelectField && calendarUsersConunter > 1}">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="">
                            <fmt:message key="Appointment.calendarOf"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">

                            <html:select property="dto(calendarOf)"
                                         styleId="calendarOfId"
                                         tabindex="11"
                                         styleClass="select ${app2:getFormSelectClasses()}">
                                <html:options collection="calendarUsers" property="value"
                                              labelProperty="label"/>
                            </html:select>

                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${owner}">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Appointment.createdBy"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <app:write id="userid"
                                           value="${sessionScope.user.valueMap['userId']}"
                                           fieldName="username"
                                           tableName="elwisuser" name="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>

            <c:if test="${op != 'create'}">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="">
                        <fmt:message key="Appointment.createdBy"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:write id="userid"
                                   value="${appointmentForm.dtoMap.createdById}"
                                   fieldName="username"
                                   tableName="elwisuser" name="true"/>
                    </div>
                </div>
            </c:if>
        </div>

        <c:if test="${'create' != op && enableCalendarSelectField  && sessionScope.user.valueMap['userId'] != appointmentForm.dtoMap.userId}">
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Appointment.calendarOf"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <c:out value="${app2:getExternalCalendarUser(appointmentForm.dtoMap.userId, pageContext.request)}"/>
                    </div>
                </div>
            </div>
        </c:if>


        <div class="form-group">
            <div class="col-xs-12 ">
                <label class="control-label" for="descriptionText_id">
                    <fmt:message key="Appointment.description"/>
                </label>
                <html:textarea property="dto(descriptionText)" styleId="descriptionText_id"
                               rows="7" styleClass="${app2:getFormInputClasses()}" tabindex="12"
                               readonly="${readOnly}"/>
                <html:hidden property="dto(freeTextId)" styleId="8"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>

            <%-- **********          Recurrence tile begin here           *************--%>
        <div id="recurrenceAreaTitle" ${!appointmentForm.dtoMap.isRecurrence ? "style=\"display: none;\"":""}>
            <legend class="title">
                <fmt:message key="Appointment.Tab.recurrence"/>
            </legend>
        </div>
        <div id="recurrenceAreaContent" ${!appointmentForm.dtoMap.isRecurrence ? "style=\"display: none;\"":""}>
            <c:import url="/WEB-INF/jsp/scheduler/AppointmentRecurrenceTile.jsp"/>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <c:if test="${opUpdate || (op=='delete' && rightDelAppointment)}">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="${ope}">
                    <html:button
                            styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                            onclick="evaluateSubmit('onlySaveId','dto(onlySave)')"
                            property="dto(onlySave)" tabindex="52">
                        ${button}
                    </html:button>
                    <c:choose>
                        <c:when test="${enableSaveAndGotoOptions}">
                            <html:button
                                    styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                    onclick="evaluateSubmit('saveId','dto(save)')"
                                    property="dto(save)"
                                    tabindex="53">
                                ${buttonGoTo}
                            </html:button>
                            <c:choose>
                                <c:when test="${op == 'delete'}">
                                    <div class="form-group col-xs-12 col-sm-3">
                                        <html:select property="dto(returnType)"
                                                     styleClass="select ${app2:getFormSelectClasses()}"
                                                     onchange="returnList()"
                                                     styleId="returnTypeUp" tabindex="54">
                                            <html:options collection="returnDeleteList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group col-xs-12 col-sm-3">
                                        <html:select property="dto(returnType)"
                                                     styleClass="select ${app2:getFormSelectClasses()}"
                                                     onchange="returnList()"
                                                     styleId="returnTypeUp" tabindex="54">
                                            <html:options collection="optionBackList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="dto(returnType)" value="${returnSearchList}"/>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>
            </c:if>
            <c:if test="${'update' == op && rightDelAppointment}">
                <app:url var="url" value="${deleteUrl}" contextRelative="true"/>

                <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                    <html:button onclick="location.href='${url}'"
                                 styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                 property="dto(save)"
                                 tabindex="55">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel
                    styleClass="button ${app2:getFormButtonCancelClasses()} pull-left marginRight marginLeft marginButton"
                    tabindex="56">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="appointmentForm"/>