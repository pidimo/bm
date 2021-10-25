<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>


{
"entity" : {
    "appointmentId" : "${appointmentForm.dtoMap['appointmentId']}",
    "companyId" : "${appointmentForm.dtoMap['companyId']}",
    "userId" : "${appointmentForm.dtoMap['userId']}",
    "createdById" : "${appointmentForm.dtoMap['createdById']}",
    "title" : "${app2:escapeJSON(appointmentForm.dtoMap['title'])}",

    "addressId" : "${appointmentForm.dtoMap['addressId']}",
    "contact" : "${app2:escapeJSON(appointmentForm.dtoMap['contact'])}",
    "contactPersonId" : "${appointmentForm.dtoMap['contactPersonId']}",

    "appointmentTypeId" : "${appointmentForm.dtoMap['appointmentTypeId']}",
    "priorityId" : "${appointmentForm.dtoMap['priorityId']}",
    "location" : "${app2:escapeJSON(appointmentForm.dtoMap['location'])}",
    "isPrivate" : "${appointmentForm.dtoMap['isPrivate']}",
    "reminder" : "${appointmentForm.dtoMap['reminder']}",
    "reminderType" : "${appointmentForm.dtoMap['reminderType']}",
    "timeBefore_1" : "${appointmentForm.dtoMap['timeBefore_1']}",
    "timeBefore_2" : "${appointmentForm.dtoMap['timeBefore_2']}",

    "isAllDay" : "${appointmentForm.dtoMap['isAllDay']}",
    "startDateTime" : "${appointmentForm.dtoMap['startDateTime']}",
    "startDateFormated" : "${app2:getLocaleFormattedDateTime(appointmentForm.dtoMap['startDateTime'], timeZone, dateTimePattern, locale)}",
    "startDate" : "${appointmentForm.dtoMap['startDate']}",
    "startHour" : "${appointmentForm.dtoMap['startHour']}",
    "startMin" : "${appointmentForm.dtoMap['startMin']}",
    "endDateTime" : "${appointmentForm.dtoMap['endDateTime']}",
    "endDateFormated" : "${app2:getLocaleFormattedDateTime(appointmentForm.dtoMap['endDateTime'], timeZone, dateTimePattern, locale)}",
    "endDate" : "${appointmentForm.dtoMap['endDate']}",
    "endHour" : "${appointmentForm.dtoMap['endHour']}",
    "endMin" : "${appointmentForm.dtoMap['endMin']}",

    "isRecurrence" : "${appointmentForm.dtoMap['isRecurrence']}",
    "freeTextId" : "${appointmentForm.dtoMap['freeTextId']}",
    "descriptionText" : "${app2:escapeJSON(appointmentForm.dtoMap['descriptionText'])}",
    "version" : "${appointmentForm.dtoMap['version']}"
    },
<c:import url="/bmapp/common/scheduler/AppointmentCatalogsJsonFragment.jsp"/>
}
