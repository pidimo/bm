<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<calendar:initialize/>
<%--<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>--%>

<c:set var="timeZone" value="${timeZone}"/>
<script language="JavaScript">
    function onSubmit() {
        document.forms[1].submit();
    }

    function myReset() {
        var form = document.appointmentListForm;
        for (i = 0; i < form.elements.length; i++) {
            
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(isRecurrence)") {
                form.elements[i].checked = false;
            } else if (form.elements[i].name == "beginsWithParam(title)") {
                form.elements[i].checked = false;
            } else if (form.elements[i].name == "parameter(contactPersonId)") {
                form.elements[i].options.length = 1;
            } else if (form.elements[i].name == "parameter(addressId)") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].selectedIndex=0;
            }
        }
    }

    function clearContactValues() {
        document.getElementById('fieldAddressId_id').value = "";
        document.getElementById('fieldAddressName_id').value = "";
        document.getElementById('contactPersonId_id').options.length = 1;
    }
</script>

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_ADVANCED_SEARCHLIST%>"/>

<br>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="Scheduler.appointment.advancedSearch"/>
    </td>
</tr>

<html:form action="/AppointmentAdvancedList.do" focus="parameter(title)">
    <TR>
        <TD width="15%" class="label">
            <fmt:message key="Appointment.name"/>
        </TD>
        <TD class="contain" width="35%">
            <html:text property="parameter(title)" styleClass="largeText" tabindex="1"/>
        </TD>
        <TD width="15%" class="label">
            <fmt:message key="Appointment.appType"/>
        </TD>
        <TD width="35%" class="contain">
            <fanta:select property="parameter(appTypeId)" listName="appointmentTypeList"
                          labelProperty="name" valueProperty="appointmentTypeId" styleClass="largeSelect"
                          readOnly="${op == 'delete' || onlyView}" module="/scheduler" firstEmpty="true" tabIndex="5">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Appointment.contact"/>
        </TD>
        <TD class="contain">
            <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
            <app:text  property="parameter(contact_Name_Field)"  styleId="fieldAddressName_id" styleClass="largeText" maxlength="40" tabindex="2" view="${readOnly}"  readonly="true"/>
              <tags:selectPopup url="/contacts/SearchAddress.do"  name="searchAddress" titleKey="Common.search" submitOnSelect="true"/>
              <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id" titleKey="Common.clear" onclick="clearContactValues()"/>
        </TD>

        <TD class="label" valign="middle">
            <fmt:message key="Appointment.repeat"/>
        </TD>
        <td class="contain" valign="middle">
            <html:checkbox property="parameter(isRecurrence)" tabindex="6" styleClass="radio"></html:checkbox>
        </TD>

    </TR>
     <TR>
        <TD class="label">
            <fmt:message key="Appointment.contactPerson"/>
        </TD>
        <TD class="contain">
            <fanta:select property="parameter(contactPersonId)" styleId="contactPersonId_id" tabIndex="3" listName="searchContactPersonList" module="/contacts" firstEmpty="true" labelProperty="contactPersonName" valueProperty="contactPersonId" styleClass="largeSelect" readOnly="${readOnly}" >
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            <fanta:parameter field="addressId" value="${not empty appointmentListForm.params.addressId?appointmentListForm.params.addressId:0}"/>
            <%--<fanta:parameter field="addressId" value="${not empty addressId?addressId:0}"/>--%>
        </fanta:select>
        </TD>

         <TD class="label">
             <fmt:message key="Appointment.startDate"/>

         </TD>
         <TD class="contain">
             <fmt:message key="datePattern" var="datePattern"/>
             <fmt:message key="Common.from"/>
             &nbsp;
             <app:dateText property="parameter(startDateMillis)" maxlength="10" tabindex="7" styleId="startDate"
                           calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                           parseLongAsDate="true"/>
             &nbsp;
             <fmt:message key="Common.to"/>
             &nbsp;
             <app:dateText property="parameter(endDateMillis)" maxlength="10" tabindex="8" styleId="endDate"
                           calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                           parseLongAsDate="true"/>
         </TD>
    </TR>
    <tr>
        <td class="label">
            <fmt:message key="Appointment.createdBy"/>
        </td>
        <td class="contain" colspan="3">

            <fanta:select property="parameter(createdById)" listName="userBaseList"
                          labelProperty="name" valueProperty="id"
                          styleClass="largeSelect"
                          tabIndex="4" firstEmpty="true"
                          module="/admin">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </td>

    </tr>

    <tr>
        <td colspan="4" align="center" class="alpha">
            <fanta:alphabet action="AppointmentAdvancedList.do" parameterName="titleAlphabet"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" class="button">
            <html:submit styleClass="button" tabindex="10" property="submitButton">
                <fmt:message key="Common.go"/>
            </html:submit>
            <html:button property="reset1" tabindex="11" styleClass="button" onclick="myReset()">
                <fmt:message key="Common.clear"/>
            </html:button>
        </td>
    </tr>
</html:form>
<tr>
<td align="center" colspan="4">
<br/>

<app2:checkAccessRight functionality="APPOINTMENT" permission="CREATE">
    <%-- rightAddAppointment and back variables set on SchedulerShortCuts.jsp--%>
    <c:if test="${rightAddAppointment}">
        <c:set var="newButtonsTable" scope="page">
            <tags:buttonsTable>
                <app:url value="/Appointment/Forward/Create.do?create=true&dto(returnType)=${returnType}&type=5&advancedListForward=AppointmentAdvancedSearch${back}"
                         addModuleParams="false" var="newAppointmentUrl"/>
                <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newAppointmentUrl}'">
            </tags:buttonsTable>
        </c:set>
    </c:if>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>

<c:set  var="userPermissionIsNULL" value="${publicSchedulerUserPermission == null && privateSchedulerUserPermission == null}"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<fanta:table width="100%" id="appointment" action="AppointmentAdvancedList.do" imgPath="${baselayout}">
    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

        <c:set var="isPublicApp" value="${appointment.private eq '0'}"/>
        <c:set var="rightEditAppointment" value="${userPermissionIsNULL ? true : app2:hasEditAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}" />
        <c:set var="rightDelAppointment"  value="${userPermissionIsNULL ? true : app2:hasDelAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
        <c:set var="onlyAnonymousPermission"  value="${userPermissionIsNULL ? false : app2:hasOnlyAnonymousAppPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
        <c:set var="renderRow" value="${onlyAnonymousPermission ? 'false' :'true'}"/>

        <c:set var="isOwner" value="${sessionScope.user.valueMap.schedulerUserId == appointment.appUserId}"/>
        <c:set var="editAction"
               value="Appointment/Forward/Update.do?type=6&dto(appointmentId)=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&update=true${not (isOwner && rightEditAppointment) ? '&onlyView=true' : ''}&advancedListForward=AppointmentAdvancedSearch"/>
        <c:set var="deleteAction"
               value="Appointment/Forward/Delete.do?type=6&dto(withReferences)=true&dto(appointmentId)=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&operation=delete&fromView=true&advancedListForward=AppointmentAdvancedSearch"/>
        <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
            <c:choose>
                <c:when test="${onlyAnonymousPermission}">
                    <fanta:actionColumn name="update" styleClass="listItem" headerStyle="listHeader" width="50%">
                        &nbsp;
                    </fanta:actionColumn>
                </c:when>
                <c:otherwise>
                    <fanta:actionColumn name="update" label="Common.update" action="${editAction}" title="Common.update"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/edit.gif"/>
                </c:otherwise>
            </c:choose>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
            <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader" width="50%">
                <c:choose>
                    <c:when test="${rightDelAppointment and (appointment.appUserId == sessionScope.user.valueMap['schedulerUserId'])}">
                        <html:link action="${deleteAction}" titleKey="Common.delete">
                            <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete" border="0"/>
                        </html:link>
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:actionColumn>
        </app2:checkAccessRight>
    </fanta:columnGroup>
    <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="Appointment.name"
                      headerStyle="listHeader" width="30%" orderable="true" maxLength="50"
                      renderData="${renderRow}" renderUrl="${renderRow}">
        <fmt:message key="Scheduler.appointment.anonymous"/>
    </fanta:dataColumn>
    <fanta:dataColumn name="appTypeName" styleClass="listItem" title="Appointment.appType"
                      headerStyle="listHeader" width="15%" orderable="true"
                      renderData="${renderRow}">
        <fmt:message key="Scheduler.appointment.anonymous"/>
    </fanta:dataColumn>
    <fanta:dataColumn name="ruleType" styleClass="listItem" title="Appointment.periodRecurrence"
                      headerStyle="listHeader"
                      width="20%" orderable="false" renderData="false">
        <c:choose>
            <c:when test="${onlyAnonymousPermission}">
                <fmt:message key="Scheduler.appointment.anonymous"/>
            </c:when>
            <c:when test="${appointment.ruleType == '1'}">
                <fmt:message key="Scheduler.calendarView.daily"/>
            </c:when>
            <c:when test="${appointment.ruleType == '2'}">
                <fmt:message key="Scheduler.calendarView.weekly"/>
            </c:when>
            <c:when test="${appointment.ruleType == '3'}">
                <fmt:message key="Scheduler.calendarView.monthly"/>
            </c:when>
            <c:when test="${appointment.ruleType == '4'}">
                <fmt:message key="Scheduler.calendarView.yearly"/>
            </c:when>
            <c:otherwise>
                &nbsp;
            </c:otherwise>
        </c:choose>
    </fanta:dataColumn>
    <fanta:dataColumn name="startDate" styleClass="listItem" title="Appointment.startDate"
                      headerStyle="listHeader"
                      width="15%" orderable="true" renderData="false">
        <fmt:message var="datePattern" key="datePattern"/>
        <c:choose>
            <c:when test="${appointment.isAllDay == '1'}">
                ${app2:getDateWithTimeZone(appointment.startDate, timeZone, datePattern)}
            </c:when>
            <c:otherwise>
                ${app2:getDateWithTimeZone(appointment.startDate, timeZone, dateTimePattern)}
            </c:otherwise>
        </c:choose>
    </fanta:dataColumn>
    <fanta:dataColumn name="endDate" styleClass="listItem2" title="Appointment.endDate"
                      headerStyle="listHeader" width="15%" orderable="true" renderData="false">

        <c:choose>
            <c:when test="${appointment.isAllDay == '1'}">
                ${app2:getDateWithTimeZone(appointment.endDate, timeZone, datePattern)}
            </c:when>
            <c:otherwise>
                ${app2:getDateWithTimeZone(appointment.endDate, timeZone, dateTimePattern)}
            </c:otherwise>
        </c:choose>
    </fanta:dataColumn>
</fanta:table>
<c:out value="${newButtonsTable}" escapeXml="false"/>
</td>
</tr>


</table>
