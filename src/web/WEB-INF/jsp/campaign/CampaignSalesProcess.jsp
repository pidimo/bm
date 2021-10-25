<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endDate").val($("#startDate").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/SalesProcess/CreateMany.do" focus="dto(processName)" styleClass="form-horizontal">
        <fmt:message var="datePattern" key="datePattern"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="SALESPROCESS" property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 styleId="saveButtonId">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
                <%--<html:hidden property="dto(op)" value="${op}"/>--%>
            <html:hidden property="dto(campaignId)"/>
            <html:hidden property="dto(activityId)"/>
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="processName_id">
                            <fmt:message key="SalesProcess.name"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(processName)" styleId="processName_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="80" tabindex="1"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="SalesProcess.priority"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="dto(priorityId)" styleId="priorityId_id"
                                          listName="sProcessPriorityList"
                                          labelProperty="name" valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}" module="/sales" firstEmpty="true" tabIndex="2">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </fanta:select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="probability_id">
                            <fmt:message key="SalesProcess.probability"/>
                            (<fmt:message key="Common.probabilitySymbol"/>)
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                            <html:select property="dto(probability)" styleId="probability_id"
                                         styleClass="${app2:getFormSelectClasses()}"
                                         readonly="${op == 'delete'}"
                                         tabindex="3">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="probabilities" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                            <fmt:message key="SalesProcess.startDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="input-group date">
                                <app:dateText property="dto(startDate)" styleId="startDate"
                                              onchange="changeStartDateValue()"
                                              mode="bootstrap"
                                              styleClass="${app2:getFormInputClasses()} text"
                                              calendarPicker="${op != 'delete'}"
                                              datePatternKey="${datePattern}" view="${op == 'delete'}"
                                              maxlength="10"
                                              tabindex="4" currentDate="true"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="statusId_id">
                            <fmt:message key="SalesProcess.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="dto(statusId)" styleId="statusId_id" listName="statusList"
                                          labelProperty="statusName" valueProperty="statusId"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}" module="/sales" firstEmpty="true" tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="endDate">
                            <fmt:message key="SalesProcess.endDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="input-group date">
                                <app:dateText property="dto(endDate)" mode="bootstrap" styleId="endDate"
                                              calendarPicker="${op != 'delete'}"
                                              datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} text" view="${op == 'delete'}"
                                              maxlength="10"
                                              tabindex="6" currentDate="true"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
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

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:numberText property="dto(value)" styleId="value_id"
                                            styleClass="${app2:getFormInputClasses()} text" maxlength="12"
                                            view="${'delete' == op}"
                                            numberType="decimal" maxInt="10" maxFloat="2" tabindex="7"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="SALESPROCESS" property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 styleId="saveButtonId" tabindex="12">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="salesProcessManyForm"/>