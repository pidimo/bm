<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("dayWeekList", JSPHelper.getDaysWeekList(request));
    pageContext.setAttribute("monthsYearList", JSPHelper.getMonthYearList(request));
    pageContext.setAttribute("numberDayList", JSPHelper.getNumberDayList(request));
    pageContext.setAttribute("numberWeekList", JSPHelper.getNumberWeekList(request));
%>

<tags:jscript language="JavaScript" src="/js/scheduler/scheduler.jsp"/>
<fmt:message var="datePattern" key="datePattern"/>

<fieldset class="fieldset">
    <div class="">
        <legend class="panel panel-title title">
            <fmt:message key="Appointment.Recurrence.rrule"/>
        </legend>
        <div class="row">
            <div class="col-sm-2">
                <div class="radio radio-default">
                    <html:radio property="dto(ruleType)"
                                value="1" styleId="ruleType1"
                                onclick="changeRuleType(ruleType1)"
                                styleClass="radio" tabindex="21" disabled="${readOnly}"/>
                    <label>
                        <fmt:message key="Appointment.Recurrence.daily"/>
                    </label>
                </div>

                <div class="radio radio-default">
                    <html:radio property="dto(ruleType)"
                                styleId="ruleType2" value="2"
                                onclick="changeRuleType(ruleType2)" styleClass="radio"
                                tabindex="22" disabled="${readOnly}"/>
                    <label>
                        <fmt:message key="Appointment.Recurrence.week"/>
                    </label>
                </div>

                <div class="radio radio-default">
                    <html:radio property="dto(ruleType)"
                                styleId="ruleType3" value="3"
                                onclick="changeRuleType(ruleType3)"
                                styleClass="radio" tabindex="23" disabled="${readOnly}"/>
                    <label>
                        <fmt:message key="Appointment.Recurrence.month"/>
                    </label>
                </div>
                <div class="radio radio-default">
                    <html:radio property="dto(ruleType)" styleId="ruleType4" value="4"
                                onclick="changeRuleType(ruleType4)"
                                styleClass="radio" tabindex="24" disabled="${readOnly}"/>
                    <label>
                        <fmt:message key="Appointment.Recurrence.year"/>
                    </label>
                </div>
            </div>
            <div class="col-sm-10">
                <%--daily--%>
                <div id="ruleType1_1" ${!(appointmentForm.dtoMap.ruleType == '1') ? "style=\"display:none;\"":"style=\"\""}>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="col-sm-3 col-md-2 control-label" for="">
                            <fmt:message key="Appointment.Recurrence.rEvery"/>
                        </label>

                        <div class="col-xs-11 col-sm-7">
                            <div class="pull-left" style="width: 60%;">
                                <app:text property="dto(recurEveryDay)"
                                          styleClass="tinyText ${app2:getFormInputClasses()}"
                                          maxlength="2" tabindex="25"
                                          view="${readOnly}"/>
                            </div>
                            <span class="pull-left form-control-static">&nbsp;
                                <fmt:message key="Appointment.Recurrence.days"/>
                            </span>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <%--weekly--%>
                <div id="ruleType2_1" ${!(appointmentForm.dtoMap.ruleType == '2') ? "style=\"display:none;\"": "style=\"\""}>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="col-sm-3 col-md-2 control-label" for="">
                            <fmt:message key="Appointment.Recurrence.rEvery"/>
                        </label>

                        <div class="col-xs-11 col-sm-7">
                            <div class="pull-left" style="width: 60%;">
                                <app:text property="dto(recurEveryWeek)"
                                          styleClass="tinyText ${app2:getFormInputClasses()}"
                                          maxlength="2" tabindex="25"
                                          view="${readOnly}"/>
                            </div>
                            <span class="pull-left form-control-static">&nbsp;
                                <fmt:message key="Appointment.Recurrence.weeksOn"/>
                            </span>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="form-group col-xs-12">
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(1)" styleId="1_id" tabindex="26" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="1_id"><fmt:message key="Appointment.Recurrence.mon"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(2)" styleId="2_id" tabindex="27" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="2_id"><fmt:message key="Appointment.Recurrence.tue"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(3)" styleId="3_id" tabindex="28" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="3_id"><fmt:message key="Appointment.Recurrence.wed"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(4)" styleId="4_id" tabindex="29" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="4_id"><fmt:message key="Appointment.Recurrence.thu"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(5)" styleId="5_id" tabindex="30" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="5_id"><fmt:message key="Appointment.Recurrence.fri"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(6)" styleId="6_id" tabindex="31" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="6_id"><fmt:message key="Appointment.Recurrence.sat"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-3">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(7)" styleId="7_id" tabindex="32" styleClass="radio"
                                                   disabled="${readOnly}"/>
                                    <label for="7_id"><fmt:message key="Appointment.Recurrence.sun"/></label>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>

                </div>
                <%--monthly--%>
                <div id="ruleType3_1" ${!(appointmentForm.dtoMap.ruleType == '3') ? "style=\"display:none;\"":"style=\"\""}>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="col-sm-3 col-md-2 control-label" for="">
                            <fmt:message key="Appointment.Recurrence.every"/>
                        </label>

                        <div class="col-xs-11 col-sm-7">
                            <div class="pull-left" style="width: 60%;">
                                <app:text property="dto(recurEveryMonth)"
                                          styleClass="tinyText ${app2:getFormInputClasses()}"
                                          maxlength="2" tabindex="25"
                                          view="${readOnly}"/>
                            </div>
                                <span class="pull-left form-control-static">&nbsp;
                                    <fmt:message key="Appointment.Recurrence.months"/>
                                </span>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <div class="col-sm-3 col-md-2">
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(ruleValueTypeMonth)" value="1" styleClass="radio"
                                            tabindex="26"
                                            disabled="${readOnly}"/>
                                <label> <fmt:message key="Appointment.Recurrence.recurOnThe"/></label>
                            </div>
                        </div>
                        <div class="col-xs-11 col-sm-7">
                            <div class="pull-left" style="width: 60%;">
                                <html:select property="dto(ruleValueDay)"
                                             styleClass="tinySelect ${app2:getFormSelectClasses()}"
                                             styleId="ruleValueDay_Id"
                                             tabindex="28"
                                             readonly="${readOnly}">
                                    <html:options collection="numberDayList" property="value"
                                                  labelProperty="label"/>

                                </html:select>
                            </div>
                            <span class="pull-left form-control-static">&nbsp;
                                <fmt:message key="Appointment.Recurrence.day"/>
                            </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3 col-md-2">
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(ruleValueTypeMonth)" value="2" styleClass="radio"
                                            tabindex="27"
                                            disabled="${readOnly}"/>
                                <label>
                                    <fmt:message key="Appointment.Recurrence.recurOnThe"/>
                                </label>
                            </div>
                        </div>
                        <div class="col-xs-11 col-sm-7">
                            <div class="row">
                                <div class="col-xs-11 col-sm-6 marginButton">
                                    <html:select property="dto(ruleValueWeek)" styleId="ruleValueWeek_Id"
                                                 styleClass="tinySelect ${app2:getFormSelectClasses()}"
                                                 tabindex="29"
                                                 readonly="${readOnly}">
                                        <html:options collection="numberWeekList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </div>
                                <div class="col-xs-11 col-sm-6 marginButton">
                                    <html:select property="dto(daysWeek)" styleId="dayWeek_Id"
                                                 styleClass="shortSelect ${app2:getFormSelectClasses()}" tabindex="30"
                                                 readonly="${readOnly}">
                                        <html:options collection="dayWeekList" property="value" labelProperty="label"/>
                                    </html:select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--yearly--%>
                <div id="ruleType4_1" ${!(appointmentForm.dtoMap.ruleType == '4') ? "style=\"display: none;padding-left:20px;\"":"style=\"padding-left:20px;\""}>
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="col-sm-3 control-label">
                            <fmt:message key="Appointment.Recurrence.every"/>
                        </div>
                        <div class="col-xs-11 col-sm-7">
                            <div class="pull-left" style="width: 60%;">
                                <app:text property="dto(recurEveryYear)"
                                          styleClass="tinyText ${app2:getFormInputClasses()}"
                                          maxlength="2" tabindex="25"
                                          view="${readOnly}"/>
                            </div>
                            &nbsp;
                            <span class="pull-left form-control-static">&nbsp;
                                <fmt:message key="Appointment.Recurrence.years"/>
                            </span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="col-sm-3">
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(ruleValueTypeYear)" value="1" styleClass="radio" tabindex="26"
                                            disabled="${readOnly}"/>
                                <label>
                                    <fmt:message key="Appointment.Recurrence.rInTheMonth"/>
                                </label>
                            </div>
                        </div>
                        <div class="col-xs-11 col-sm-7">
                            <html:select property="dto(ruleValue)"
                                         styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                         readonly="${readOnly}" tabindex="28">
                                <html:options collection="monthsYearList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}">
                        <div class="col-sm-3">
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(ruleValueTypeYear)" value="2" styleClass="radio" tabindex="27"
                                            disabled="${readOnly}"/>
                                <label>
                                    <fmt:message key="Appointment.Recurrence.rOnThisDay"/>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</fieldset>
<div class="row">
    <div class="col-xs-12 col-sm-6 marginButton">
        <fieldset class="fieldset">
            <legend class="panel panel-title title">
                <fmt:message key="Appointment.Recurrence.rrange"/>
            </legend>
            <html:hidden property="dto(rt)" value="${appointmentForm.dtoMap.rangeType}" styleId="rt"/>

            <div class="row">
                <div class="col-xs-12">
                    <div class="radiocheck">
                        <div class="radio radio-default">
                            <html:radio property="dto(rangeType)" value="1" onclick="deshabilita_1()" styleClass="radio"
                                        tabindex="33" disabled="${readOnly}"/>
                            <label>
                                <fmt:message key="Appointment.Recurrence.noEndDate"/>
                            </label>
                        </div>
                    </div>
                </div>
                <div>
                    <div class="col-xs-12 col-sm-4">
                        <div class="radiocheck">
                            <div class="radio radio-default">
                                <html:radio property="dto(rangeType)" value="2" styleClass="radio"
                                            onclick="habilita_1()"
                                            tabindex="34" disabled="${readOnly}"/>
                                <label>
                                    <fmt:message key="Appointment.Recurrence.endAfter"/>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-11 col-sm-7">
                        <div class="pull-left" style="width: 50%;">
                            <app:text property="dto(rangeValueText)" styleId="rangeValueText_Id"
                                      styleClass="tinyText ${app2:getFormInputClasses()}"
                                      maxlength="2" tabindex="36" view="${readOnly}"/>
                        </div>
                        <span class="pull-left form-control-static">&nbsp;
                            <fmt:message key="Appointment.Recurrence.occurrences"/>
                        </span>
                    </div>
                </div>
                <div>
                    <div class="col-xs-12 col-sm-4">
                        <div class="radiocheck">
                            <div class="radio radio-default">
                                <html:radio property="dto(rangeType)" value="3" onclick="habilita1_1()"
                                            styleClass="radio"
                                            tabindex="35" disabled="${readOnly}"/>
                                <label>
                                    <fmt:message key="Appointment.Recurrence.endBy"/>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-11 col-sm-7">

                        <div id="disablePicker_calendar">
                            <div class="input-group date">
                                <app:dateText property="dto(rangeValueDate)" styleId="rangeValueDate_Id"
                                              calendarPicker="${!param.onlyView}"
                                              mode="bootstrap"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              view="${readOnly}" maxlength="10" tabindex="37"/>
                            </div>
                        </div>
                        <c:if test="${op != 'delete' && !readOnly}">
                            <div id="disablePicker">
                                <app:text property="dto(rangeValueDate)" value=""
                                          styleClass="dateText ${app2:getFormInputClasses()}" maxlength="10"
                                          tabindex="37" readonly="true"/>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
    <div class="col-xs-12 col-sm-6 marginButton">
        <fieldset class="fieldset">
            <legend class="panel panel-title title">
                <fmt:message key="Appointment.Recurrence.exceptions"/>
            </legend>
            <div class="row">
                <div class="col-sm-4">
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="col-xs-11 col-sm-12">
                            <div class="input-group date">
                                <app:dateText property="dto(exceptionDate)" styleId="exceptionDate"
                                              mode="bootstrap"
                                              calendarPicker="${!param.onlyView}" datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}" view="${readOnly}"
                                              maxlength="10"
                                              tabindex="38"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <input type="button" onclick="add()"
                                   class="button ${app2:getFormButtonClasses()} marginButton" ${readOnly ? "disabled='true'":''}
                                   property="dto(save)" value='<fmt:message key="Common.add"/>' tabindex="39"></input>
                            <input type="button" tabindex="40"
                                   class="button ${app2:getFormButtonClasses()} marginButton"
                            ${readOnly ? "disabled=\"true\"":""}
                                   value='<fmt:message key="Common.delete"/>' onclick="remove()"></input>
                        </div>
                    </div>
                </div>
                <div class="col-xs-11 col-sm-8">
                    <c:if test="${op == 'create'}">
                        <html:select property="array2" size="5" multiple="true" tabindex="41"
                                     styleClass="schedulerSelect ${app2:getFormSelectClasses()}" styleId="exception"
                                     readonly="${readOnly}"/>
                    </c:if>
                    <c:if test="${op != 'create'}">
                        <c:set var="nameValue" value="${array2}"/>
                        <html:select property="array2" size="5" multiple="true" tabindex="41"
                                     styleClass="schedulerSelect ${app2:getFormSelectClasses()}" styleId="exception"
                                     disabled="${readOnly}">
                            <html:options collection="nameValue" property="value" labelProperty="label"/>
                        </html:select>
                    </c:if>
                </div>
            </div>
        </fieldset>
    </div>
</div>
