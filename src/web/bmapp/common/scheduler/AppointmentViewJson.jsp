<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>

{
"appointmentsList":
[
<c:forEach var="item" items="${appointmentsData}" varStatus="statusVar">
    <c:set var="startMillisVar" value="${app2:getDateTimeAsMillis(item.value.startDate.dateAsInteger, item.value.startTime.hour, item.value.startTime.minute, pageContext.request)}"/>
    <c:set var="endMillisVar" value="${app2:getDateTimeAsMillis(item.value.endDate.dateAsInteger, item.value.endTime.hour, item.value.endTime.minute, pageContext.request)}"/>

    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "virtualAppointmentId" : "${item.value.id}",
    "title" : "${app2:escapeJSON(item.value.title)}",
    "location" : "${app2:escapeJSON(item.value.location)}",
    "color" : "${item.value.color}",
    "isPublic" : "${item.value.public}",
    "isAllDay" : "${item.value.allDay}",
    "isOwner" : "${item.value.owner}",
    "dateInteger" : "${item.value.startDate.dateAsInteger}",
    "startMillis" : "${startMillisVar}",
    "endMillis" : "${endMillisVar}",
    "startDateFormated" : "${app2:getLocaleFormattedDateTime(startMillisVar, timeZone, dateTimePattern, locale)}",
    "endDateFormated" : "${app2:getLocaleFormattedDateTime(endMillisVar, timeZone, dateTimePattern, locale)}"
    }
</c:forEach>
]
}

