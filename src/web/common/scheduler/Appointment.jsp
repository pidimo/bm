<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/scheduler/scheduler.jsp"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.blockUI-2.31.js"/>

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
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>

<tags:initSelectPopup/>
<calendar:initialize/>

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

        if(isCreateToOtherUser()) {
            $.blockUI({ message: $('#questionPanel') , css: {padding: '20px'}});
        } else {
          submitForm();
        }
    }

    function isCreateToOtherUser() {
        var op = '${op}';
        var sessionUserId = '${sessionScope.user.valueMap['userId']}';
        var userId = document.getElementById("userId_Id").value;

        var calendarOf = document.getElementById("calendarOfId");
        if(calendarOf != undefined) {
            userId = calendarOf.value;
        }

        if(op == 'create' && userId != '' && userId != sessionUserId) {
            return true;
        } else {
            return false;
        }
    }


    function addSubmitActionHidden(idHidden, propertyHidden) {
        var submitForm = document.getElementById("appointmentFormId");
        var hiddenField = document.getElementById(idHidden);

        if(hiddenField == undefined){
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
<c:set var="userPermissionIsNULL"
       value="${publicSchedulerUserPermission == null && privateSchedulerUserPermission == null}"/>

<c:set var="rightEditAppointment"
       value="${(userPermissionIsNULL ? true : app2:hasEditAppointmentPermission(publicSchedulerUserPermission,privateSchedulerUserPermission,isPublicApp)) and (appointmentForm.dtoMap.userId == pageSchedulerUserId)}"/>
<c:set var="rightDelAppointment"
       value="${(userPermissionIsNULL ? true : app2:hasDelAppointmentPermission(publicSchedulerUserPermission,privateSchedulerUserPermission,isPublicApp)) and (appointmentForm.dtoMap.userId == pageSchedulerUserId)}"/>

<c:set var="readOnly" value="${op == 'delete' || (op == 'update' && !rightEditAppointment)}" scope="request"/>
<c:set var="opUpdate" value="${op == 'create' || (op == 'update' && rightEditAppointment)}" scope="request"/>

<br>
<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="datePattern" key="datePattern"/>
<html:form action="${action}?oldType=${param.type}" focus="dto(title)" styleId="appointmentFormId">

<%--modal dialog to show confirmation to send notification--%>
<div id="questionPanel" style="display:none; cursor: default">
    <span style="font-size:10pt; color:#000000; font-family:Verdana,serif;">
        <fmt:message key="Appointment.question.ownerSendNotification"/>
    </span>
    <input type="submit" id="questionYes" value="<fmt:message key="Common.yes"/>" onclick="questionYesButton()" class="button"/>
    <input type="button" id="questionNo" value="<fmt:message key="Common.no"/>" onclick="questionNoButton()" class="button"/>
</div>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <tr>
        <td class="button">
            <c:if test="${opUpdate || (op=='delete' && rightDelAppointment)}">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="${ope}">
                    <html:button styleClass="button" onclick="evaluateSubmit('onlySaveId','dto(onlySave)')" property="dto(onlySave)">
                        ${button}
                    </html:button>
                    <c:choose>
                        <c:when test="${enableSaveAndGotoOptions}">
                            <html:button styleClass="button" onclick="evaluateSubmit('saveId','dto(save)')" property="dto(save)">
                                ${buttonGoTo}
                            </html:button>
                            <c:choose>
                                <c:when test="${op == 'delete'}">
                                    <html:select property="dto(returnType)" styleClass="select"
                                                 onchange="returnListLow()"
                                                 styleId="returnTypeLow">
                                        <html:options collection="returnDeleteList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </c:when>
                                <c:otherwise>
                                    <html:select property="dto(returnType)" styleClass="select"
                                                 onchange="returnListLow()"
                                                 styleId="returnTypeLow">
                                        <html:options collection="optionBackList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
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
                    <html:button onclick="location.href='${url}'" styleClass="button" property="dto(save)">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

            <%--top links--%>
            &nbsp;
            <c:if test="${enableParticipantLink}">
                <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="VIEW">
                    <app:link
                            action="/scheduler/Appointment/ParticipantList.do?appointmentId=${appointmentForm.dtoMap.appointmentId}&dto(appointmentId)=${appointmentForm.dtoMap.appointmentId}&onlyView=${param.onlyView}&dto(title)=${app2:encode(title)}&calendar=${param.calendar}&type=${param.type}&tabKey=Appointment.Tab.participants&index=1"
                            styleClass="folderTabLink" contextRelative="true">
                        <fmt:message key="Appointment.link.participants"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>

        </td>
    </tr>
</table>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:hidden property="dto(op)" value="${op}" styleId="1"/>
<html:hidden property="dto(ope)" value="${ope}" styleId="2"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}" styleId="3"/>
<html:hidden property="dto(calendar)" value="${param.calendar}" styleId="4"/>
<c:set var="value" value="4"/>
<html:hidden property="dto(userId)" value="${pageSchedulerUserId}" styleId="userId_Id"/>
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

<tr>
    <td colspan="4" class="title">
        <c:out value="${titlePage}"/>
    </td>
</tr>
<tr>
    <td width="14%" class="label"><fmt:message key="Appointment.name"/></td>
    <td width="30%" class="contain">
        <app:text property="dto(title)" styleClass="middleText" maxlength="60" tabindex="1" view="${readOnly}"/>
    </td>
    <td class="label" width="16%"><fmt:message key="Appointment.contact"/></TD>
    <td class="contain" width="34%">

        <c:if test="${not empty appointmentForm.dtoMap['addressId'] and 'update' == op}">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                <c:set var="addressMap" value="${app2:getAddressMap(appointmentForm.dtoMap['addressId'])}"/>
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
        </c:if>

        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
        <c:choose>
            <c:when test="${enableContactInputField}">
                <app:text property="dto(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                          tabindex="12" view="${readOnly}" readonly="${opUpdate}"/>

                <c:if test="${not empty addressEditLink}">
                    <app:link action="${addressEditLink}" contextRelative="true">
                        <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
                    </app:link>
                </c:if>

                <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                                  hide="${op == 'delete' || readOnly}" submitOnSelect="true"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear"
                                       hide="${op == 'delete' || readOnly}" submitOnClear="true"/>
            </c:when>
            <c:otherwise>
                <app:text property="dto(contact)"
                          styleClass="middleText"
                          maxlength="40"
                          tabindex="12"
                          view="true"
                          readonly="true"/>
            </c:otherwise>
        </c:choose>

    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Appointment.appType"/></TD>
    <td class="contain">
        <fanta:select property="dto(appointmentTypeId)" listName="appointmentTypeList"
                      labelProperty="name" valueProperty="appointmentTypeId" styleClass="middleSelect"
                      readOnly="${readOnly}" module="/scheduler" firstEmpty="true" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="Appointment.contactPerson"/></TD>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" tabIndex="13" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                      valueProperty="contactPersonId" styleClass="middleSelect" readOnly="${readOnly}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty appointmentForm.dtoMap['addressId']?appointmentForm.dtoMap['addressId']:0}"/>
        </fanta:select>
    </td>

</tr>
<tr>
    <TD class="label"><fmt:message key="Task.priority"/></TD>
    <TD class="contain">
        <fanta:select property="dto(priorityId)" listName="selectPriorityList"
                      labelProperty="name" valueProperty="id" styleClass="middleSelect"
                      readOnly="${readOnly}" module="/catalogs" firstEmpty="true" tabIndex="3">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SCHEDULER"/>
        </fanta:select>
    </TD>
    <TD class="label"><fmt:message key="Appointment.location"/></TD>
    <TD class="contain">
        <app:text property="dto(location)" styleClass="middleText" maxlength="30" tabindex="14" view="${readOnly}"/>
    </TD>
</tr>
<tr>
    <td class="topLabel"><fmt:message key="Appointment.startDate"/></td>
    <td class="contain">

        <html:checkbox property="dto(isAllDay)" styleId="isAllDay" disabled="${readOnly}" onclick="deshabilitaAllDay1()"
                       tabindex="4" styleClass="radio"/>
        <fmt:message key="Appointment.allDay"/>
        <br>

        <app:dateText property="dto(startDate)" tabindex="5" styleId="startDate" onkeyup="equalDate()"
                      calendarPicker="${false}" datePatternKey="${datePattern}"
                      styleClass="dateText" view="${readOnly}" maxlength="10" currentDate="true"
                      currentDateTimeZone="${currentDateTimeZone}"/>

        <c:if test="${opUpdate}">
            <a href="javascript:openCalendarPickerScheduler(document.getElementById('startDate'),document.getElementById('endDate'));"
               alt="<fmt:message   key="Calendar.open"/>">
                <img src="<c:out value="${sessionScope.baselayout}"/>/img/calendarPicker.gif"
                     alt="<fmt:message   key="Calendar.open"/>" border="0"/>
            </a>
        </c:if>
        &nbsp;
        <abbr id="startDate_Id"  ${(appointmentForm.dtoMap.isAllDay =='true') ? "style=\"display: none;\"":""}>
            <html:select property="dto(startHour)" onchange="incrHour()" onkeyup="incrHour()" tabindex="6"
                         styleClass="select" readonly="${readOnly}" styleId="startHour" style="width:43">
                <html:options collection="hourList" property="value" labelProperty="label"/>
            </html:select>
            :
            <html:select property="dto(startMin)" onchange="equalMin()" onkeyup="equalMin()" styleClass="select"
                         tabindex="7" readonly="${readOnly}" styleId="startMin" style="width:43">
                <html:options collection="intervalMinList" property="value" labelProperty="label"/>
            </html:select>
        </abbr>
    </td>
    <td class="topLabel">
        <fmt:message key="Appointment.reminder"/>
    </td>
    <td class="containTop">
        <html:checkbox property="dto(reminder)" styleId="reminderId" disabled="${readOnly}" tabindex="15"
                       styleClass="radio" value="true" onclick="habilitaReminder()"/>
        <div id="reminderAreaId"
             style="padding-top:5px;${appointmentForm.dtoMap['reminder'] != 'true' ? 'display:none' :''}">

            <label id="reminder_1"   ${!(appointmentForm.dtoMap.reminderType == '1') ? "style=\"display: none;\"":""} >
                <html:select property="dto(timeBefore_1)" styleId="timeBefore" styleClass="select"
                             readonly="${readOnly}"
                             tabindex="17" style="width:43">
                    <html:options collection="timeBeforeList" property="value" labelProperty="label"/>
                </html:select>
            </label>
            <label id="reminder_2"  ${!(appointmentForm.dtoMap.reminderType != '1') ? "style=\"display: none;\"":""}>
                <app:text property="dto(timeBefore_2)" styleId="timeBefore_2" styleClass="tinyText" maxlength="2"
                          tabindex="18" view="${readOnly}"/>
            </label>
            &nbsp;
            <html:select property="dto(reminderType)" styleId="reminderType_Id" onchange="changeReminderType()"
                         styleClass="shortSelect" tabindex="19" readonly="${readOnly}">
                <html:options collection="reminderTypeList" property="value" labelProperty="label"/>
            </html:select>
            &nbsp;
            <fmt:message key="Appointment.before"/>

        </div>

    </td>
</tr>

<tr>
    <td class="topLabel"><fmt:message key="Appointment.endDate"/></TD>
    <td class="contain">
        <app:dateText property="dto(endDate)" tabindex="8" styleId="endDate" calendarPicker="${opUpdate}"
                      datePatternKey="${datePattern}"
                      styleClass="dateText" view="${readOnly}" maxlength="10" currentDate="true"
                      currentDateTimeZone="${currentDateTimeZone}"/>
        &nbsp;
        <abbr id="endDate_Id"  ${(appointmentForm.dtoMap.isAllDay =='true') ? "style=\"display: none;\"":""}>

            <html:select property="dto(endHour)" tabindex="9" styleClass="select" readonly="${readOnly}"
                         styleId="endHour" style="width:43">
                <html:options collection="hourList" property="value" labelProperty="label"/>
            </html:select>
            :
            <html:select property="dto(endMin)" tabindex="10" styleClass="select" readonly="${readOnly}"
                         styleId="endMin" style="width:43">
                <html:options collection="intervalMinList" property="value" labelProperty="label"/>
            </html:select>
        </abbr>
    </td>
    <TD class="topLabel"><fmt:message key="Appointment.private"/></TD>
    <TD class="contain">
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
                <input type="checkbox" name="tempIsPrivate" class="radio" disabled="disabled"
                       tabindex="20" ${processPermited eq onlyPrivateApp ?'checked="checked"':''}>
            </c:when>
            <c:otherwise>
                <html:checkbox property="dto(isPrivate)" disabled="${readOnly || processDisable}" tabindex="20"
                               styleClass="radio"/>
                <c:if test="${!isPublicApp && op == 'update' && (readOnly || processDisable)}">
                    <html:hidden property="dto(isPrivate)" value="on"/>
                </c:if>
            </c:otherwise>
        </c:choose>
    </TD>
</tr>
<tr>
    <TD class="label"><fmt:message key="Appointment.repeat"/></TD>
    <c:choose>
        <c:when test="${'create' == op && enableCalendarSelectField && calendarUsersConunter > 1}">
            <TD class="contain">
                <html:checkbox property="dto(isRecurrence)"
                               onclick="isRecurrence(this)"
                               disabled="${readOnly}"
                               tabindex="11"
                               styleClass="radio"/>
            </TD>
            <td class="label">
                <fmt:message key="Appointment.calendarOf"/>
            </td>
            <td class="contain">
                <html:select property="dto(calendarOf)"
                             styleId="calendarOfId"
                             tabindex="9"
                             styleClass="select">
                    <html:options collection="calendarUsers" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </c:when>
        <c:otherwise>
            <TD class="contain" colspan="${(op !='create' || owner)?0:3}">
                <html:checkbox property="dto(isRecurrence)" onclick="isRecurrence(this)" disabled="${readOnly}"
                               tabindex="11"
                               styleClass="radio"/>
            </TD>
            <c:if test="${owner}">
                <td class="label"><fmt:message key="Appointment.createdBy"/></TD>
                <td class="contain">
                    <app:write id="userid"
                               value="${sessionScope.user.valueMap['userId']}"
                               fieldName="username"
                               tableName="elwisuser" name="true"/>
                </TD>
            </c:if>
        </c:otherwise>
    </c:choose>

    <c:if test="${op != 'create'}">
        <td class="label"><fmt:message key="Appointment.createdBy"/></TD>
        <td class="contain">
            <app:write id="userid"
                       value="${appointmentForm.dtoMap.createdById}"
                       fieldName="username"
                       tableName="elwisuser" name="true"/>
        </TD>
    </c:if>
</tr>
<c:if test="${'create' != op && enableCalendarSelectField  && sessionScope.user.valueMap['userId'] != appointmentForm.dtoMap.userId}">
    <tr>
        <td class="label">
            <fmt:message key="Appointment.calendarOf"/>
        </td>
        <td class="contain" colspan="3">
            <c:out value="${app2:getExternalCalendarUser(appointmentForm.dtoMap.userId, pageContext.request)}"/>
        </td>
    </tr>
</c:if>


<tr>

    <td class="topLabel" colspan="4"><fmt:message key="Appointment.description"/><br>
        <html:textarea property="dto(descriptionText)" style="height:120px;width:99%;" tabindex="21"
                       readonly="${readOnly}"/>
        <html:hidden property="dto(freeTextId)" styleId="8"/>
    </td>

</tr>

    <%-- **********          Recurrence tile begin here           *************--%>
<tr id="recurrenceAreaTitle" ${!appointmentForm.dtoMap.isRecurrence ? "style=\"display: none;\"":""}>
    <td colspan="4" class="title">
        <fmt:message key="Appointment.Tab.recurrence"/>
    </td>
</tr>
<tr id="recurrenceAreaContent" ${!appointmentForm.dtoMap.isRecurrence ? "style=\"display: none;\"":""}>
    <TD class="containTop" colspan="4">
        <c:import url="/common/scheduler/AppointmentRecurrenceTile.jsp"/>
    </TD>
</tr>
</table>

<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <TR>
        <TD class="button">
            <c:if test="${opUpdate || (op=='delete' && rightDelAppointment)}">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="${ope}">
                    <html:button styleClass="button" onclick="evaluateSubmit('onlySaveId','dto(onlySave)')" property="dto(onlySave)" tabindex="52">
                        ${button}
                    </html:button>
                    <c:choose>
                        <c:when test="${enableSaveAndGotoOptions}">
                            <html:button styleClass="button" onclick="evaluateSubmit('saveId','dto(save)')" property="dto(save)"
                                         tabindex="53">
                                ${buttonGoTo}
                            </html:button>
                            <c:choose>
                                <c:when test="${op == 'delete'}">
                                    <html:select property="dto(returnType)" styleClass="select" onchange="returnList()"
                                                 styleId="returnTypeUp" tabindex="54">
                                        <html:options collection="returnDeleteList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </c:when>
                                <c:otherwise>
                                    <html:select property="dto(returnType)" styleClass="select" onchange="returnList()"
                                                 styleId="returnTypeUp" tabindex="54">
                                        <html:options collection="optionBackList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
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
                    <html:button onclick="location.href='${url}'" styleClass="button" property="dto(save)"
                                 tabindex="55">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="button" tabindex="56">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
</td>
</tr>
</table>
</html:form>