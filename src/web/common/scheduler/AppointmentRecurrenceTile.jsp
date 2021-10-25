<%@ include file="/Includes.jsp" %>
<%
pageContext.setAttribute("dayWeekList", JSPHelper.getDaysWeekList(request));
pageContext.setAttribute("monthsYearList", JSPHelper.getMonthYearList(request));
pageContext.setAttribute("numberDayList", JSPHelper.getNumberDayList(request));
pageContext.setAttribute("numberWeekList", JSPHelper.getNumberWeekList(request));
%>

<tags:jscript language="JavaScript" src="/js/scheduler/scheduler.jsp"/>
<fmt:message   var="datePattern" key="datePattern"/>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td colspan="2">
<fieldset  class="fieldset">
<legend class="legend" ><fmt:message key="Appointment.Recurrence.rrule"/></legend>
<table  border="0" cellpadding="0" cellspacing="0" height="100" width="100%">
    <tr>
        <td class="label" style="border:0px"   width="15%" >
            <html:radio property="dto(ruleType)" value="1" styleId="ruleType1" onclick="changeRuleType(ruleType1)" styleClass="radio" tabindex="21" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.daily"/>
            </html:radio>
        </td>

        <%--daily--%>
        <td id="ruleType1_1" ${!(appointmentForm.dtoMap.ruleType == '1') ? "style=\"display:none;padding-left:20px;\"":"style=\"padding-left:20px;\""}  class="containTop"  rowspan="4" >
            <fmt:message key="Appointment.Recurrence.rEvery"/>&nbsp;
                <app:text  property="dto(recurEveryDay)" styleClass="tinyText" maxlength="2" tabindex="25" view="${readOnly}"/>&nbsp;
            <fmt:message key="Appointment.Recurrence.days"/>
        </td>
        <%--weekly--%>
        <td id="ruleType2_1" ${!(appointmentForm.dtoMap.ruleType == '2') ? "style=\"display:none;padding-left:20px;\"": "style=\"padding-left:20px;\""}  class="containTop"  rowspan="4" >
            <fmt:message key="Appointment.Recurrence.rEvery"/>&nbsp;
                <app:text  property="dto(recurEveryWeek)"  styleClass="tinyText" maxlength="2" tabindex="25" view="${readOnly}"/>&nbsp;
            <fmt:message key="Appointment.Recurrence.weeksOn"/>
            <br><br>
            <html:checkbox property="dto(1)" tabindex="26" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.mon"/></html:checkbox>&nbsp;&nbsp;
            <html:checkbox property="dto(2)" tabindex="27" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.tue"/></html:checkbox>&nbsp;&nbsp;
            <html:checkbox property="dto(3)" tabindex="28" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.wed"/></html:checkbox>&nbsp;&nbsp;
            <html:checkbox property="dto(4)" tabindex="29" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.thu"/></html:checkbox>&nbsp;&nbsp;
            <html:checkbox property="dto(5)" tabindex="30" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.fri"/></html:checkbox>&nbsp;&nbsp;
            <html:checkbox property="dto(6)" tabindex="31" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.sat"/></html:checkbox>&nbsp;&nbsp;
            <html:checkbox property="dto(7)" tabindex="32" styleClass="radio" disabled="${readOnly}"><fmt:message key="Appointment.Recurrence.sun"/></html:checkbox>&nbsp;&nbsp;
        </td>
        <%--monthly--%>
        <TD id="ruleType3_1" ${!(appointmentForm.dtoMap.ruleType == '3') ? "style=\"display:none;padding-left:20px;\"":"style=\"padding-left:20px;\""} class="containTop" rowspan="4" >
            <fmt:message key="Appointment.Recurrence.every"/>&nbsp;
                <app:text  property="dto(recurEveryMonth)"   styleClass="tinyText" maxlength="2" tabindex="25" view="${readOnly}" />&nbsp;
            <fmt:message key="Appointment.Recurrence.months"/>
            <br><br>
            <html:radio property="dto(ruleValueTypeMonth)" value="1" styleClass="radio" tabindex="26" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.recurOnThe"/>
            </html:radio>&nbsp;
            <html:select property="dto(ruleValueDay)" styleClass="tinySelect" styleId="ruleValueDay_Id" tabindex="28" readonly="${readOnly}">
                <html:options collection="numberDayList"  property="value" labelProperty="label"/>
            </html:select>&nbsp;
            <fmt:message key="Appointment.Recurrence.day"/>
            <br><br>
            <html:radio property="dto(ruleValueTypeMonth)" value="2"  styleClass="radio" tabindex="27" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.recurOnThe"/>
            </html:radio>&nbsp;
            <html:select property="dto(ruleValueWeek)" styleId="ruleValueWeek_Id" styleClass="tinySelect" tabindex="29" readonly="${readOnly}">
              <html:options collection="numberWeekList"  property="value" labelProperty="label"/>
            </html:select>&nbsp;
            <html:select property="dto(daysWeek)" styleId="dayWeek_Id" styleClass="shortSelect" tabindex="30" readonly="${readOnly}">
                <html:options collection="dayWeekList"  property="value" labelProperty="label"/>
            </html:select>
        </td>

        <%--yearly--%>
        <td id="ruleType4_1" ${!(appointmentForm.dtoMap.ruleType == '4') ? "style=\"display: none;padding-left:20px;\"":"style=\"padding-left:20px;\""} class="containTop" rowspan="4" >
            <fmt:message key="Appointment.Recurrence.every"/>&nbsp;
            <app:text  property="dto(recurEveryYear)"  styleClass="tinyText" maxlength="2" tabindex="25" view="${readOnly}"/> &nbsp;
            <fmt:message key="Appointment.Recurrence.years"/>
            <br><br>
            <html:radio property="dto(ruleValueTypeYear)" value="1" styleClass="radio" tabindex="26" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.rInTheMonth"/>
            </html:radio>&nbsp;
            <html:select property="dto(ruleValue)"  styleClass="shortSelect" readonly="${readOnly}" tabindex="28" >
                <html:options collection="monthsYearList"  property="value" labelProperty="label"/>
            </html:select>
            <br><br>
            <html:radio property="dto(ruleValueTypeYear)" value="2" styleClass="radio" tabindex="27"  disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.rOnThisDay"/>
            </html:radio>
        </td>
    </tr>
    <tr>
        <TD class="label" style="border:0px" >
            <html:radio property="dto(ruleType)" styleId="ruleType2" value="2" onclick="changeRuleType(ruleType2)" styleClass="radio" tabindex="22" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.week"/>
            </html:radio>
        </TD>
    </tr>
    <tr>
        <TD class="label" style="border:0px" >
            <html:radio property="dto(ruleType)" styleId="ruleType3" value="3" onclick="changeRuleType(ruleType3)" styleClass="radio" tabindex="23" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.month"/>
            </html:radio>
        </TD>
    </tr>
    <tr>
        <TD class="label" style="border:0px" >
            <html:radio property="dto(ruleType)" styleId="ruleType4"  value="4" onclick="changeRuleType(ruleType4)" styleClass="radio" tabindex="24" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.year"/>
            </html:radio>
        </TD>
    </tr>
</table>
</fieldset>                                                                
</td>
</tr>
<tr>
<td width="50%" valign="top" >
<fieldset class="fieldset" style="height:105px; vertical-align:top;" >
<legend class="legend"><fmt:message key="Appointment.Recurrence.rrange"/></legend>
<table  border="0"  cellpadding="0" cellspacing="0">
    <html:hidden property="dto(rt)" value="${appointmentForm.dtoMap.rangeType}" styleId="rt"/>
    <tr>
        <td class="label"  style="border:0px">
            <html:radio property="dto(rangeType)" value="1" onclick="deshabilita_1()" styleClass="radio" tabindex="33" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.noEndDate"/>
            </html:radio>
        </td>
        <td class="contain">
            &nbsp;
        </td>
    </tr>
    <tr>
        <td class="label" style="border:0px">
            <html:radio property="dto(rangeType)" value="2" styleClass="radio"  onclick="habilita_1()"  tabindex="34" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.endAfter"/>
            </html:radio>
        </td>
        <td class="contain">
            <app:text  property="dto(rangeValueText)"   styleId="rangeValueText_Id"  styleClass="tinyText" maxlength="2" tabindex="36" view="${readOnly}"/>
            <fmt:message key="Appointment.Recurrence.occurrences"/>
        </td>
    </tr>
    <tr>
        <td class="label" style="border:0px">
            <html:radio property="dto(rangeType)" value="3" onclick="habilita1_1()" styleClass="radio" tabindex="35" disabled="${readOnly}">
                <fmt:message key="Appointment.Recurrence.endBy"/>
            </html:radio>

        </td>
        <td class="contain" >
            <label id="disablePicker_calendar" >
                <app:dateText property="dto(rangeValueDate)" styleId="rangeValueDate_Id"  calendarPicker="${!param.onlyView}" datePatternKey="${datePattern}" styleClass="dateText" view="${readOnly}" maxlength="10" tabindex="37" />
            </label>
        <c:if test="${op != 'delete' && !readOnly}">
            <label id="disablePicker" >
                <app:text property="dto(rangeValueDate)" value="" styleClass="dateText" maxlength="10" tabindex="37" readonly="true"/>
            </label>
        </td>
        </c:if>
    </tr>
</table>
</fieldset>
</td>
<td width="50%" valign="top">
<fieldset class="fieldset" style="height:105px; vertical-align:top;" >
<legend class="legend" ><fmt:message key="Appointment.Recurrence.exceptions"/></legend>
<table  border="0"  cellpadding="0" cellspacing="0">
    <tr>
        <td class="label" style="border:0px">
            <app:dateText property="dto(exceptionDate)" styleId="exceptionDate"  calendarPicker="${!param.onlyView}"  datePatternKey="${datePattern}" styleClass="dateText" view="${readOnly}"  maxlength="10" tabindex="38"/>
        </td>
        <td class="contain" rowspan="3">
            <c:if test="${op == 'create'}" >
               <html:select property="array2" size="20" multiple="true" tabindex="41"  styleClass="schedulerSelect" styleId="exception" readonly="${readOnly}" />
            </c:if>
            <c:if test="${op != 'create'}" >
                <c:set var="nameValue" value="${array2}"/>
                <html:select property="array2" size="20" multiple="true"  tabindex="41" styleClass="schedulerSelect" styleId="exception" disabled="${readOnly}">
                    <html:options collection="nameValue" property="value" labelProperty="label" />
                </html:select>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="label" style="border:0px">
            <input type="button" onclick="add()" class="button"  ${readOnly ? "disabled=\"true\"":""}  property="dto(save)" value='<fmt:message key="Common.add"/>' tabindex="39" style="width:70px;"></input>
        </td>
    </tr>
    <tr>
        <td class="label" style="border:0px">
            <input type="button" tabindex="40"  class="button"  ${readOnly ? "disabled=\"true\"":""} value='<fmt:message key="Common.delete"/>' onclick="remove()" style="width:70px;"></input>
        </td>
    </tr>
</table>
</fieldset>
</td>
</tr>
</table>