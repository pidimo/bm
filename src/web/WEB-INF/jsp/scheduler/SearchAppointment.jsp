<%@ include file="/Includes.jsp" %>

<html:form action="/SearchAppointment.do" focus="parameter(title)" styleClass="form-horizontal">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} text-left" for="title_id">
            <fmt:message key="Common.search"/>
        </label>
        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(title)" styleId="title_id" styleClass="largeText"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
            </div>
        </div>
    </div>
</html:form>
<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="SearchAppointment.do" mode="bootstrap" parameterName="titleAlphabet"/>
</div>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="appointmentSimpleList" width="100%" id="appointment" action="SearchAppointment.do"
                 imgPath="${baselayout}">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

            <fanta:actionColumn name="" title="Common.select"
                                useJScript="true"
                                action="javascript:opener.selectField('appointmentId_id', '${appointment.appointmentId}', 'appointmentName_id', '${app2:jscriptEncode(appointment.title)}');"
                                styleClass="listItem" headerStyle="listHeader" width="50%"
                                image="${baselayout}/img/import.gif"/>

        </fanta:columnGroup>
        <fanta:dataColumn name="title" styleClass="listItem" title="Appointment.name"
                          headerStyle="listHeader" width="30%" orderable="true" maxLength="50">
        </fanta:dataColumn>
        <fanta:dataColumn name="appTypeName" styleClass="listItem" title="Appointment.appType"
                          headerStyle="listHeader"
                          width="15%" orderable="true"/>
        <fanta:dataColumn name="ruleType" styleClass="listItem" title="Appointment.periodRecurrence"
                          headerStyle="listHeader"
                          width="20%" orderable="true" renderData="false">
            <c:choose>
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
</div>
