<%@ page import="com.jatun.titus.reportgenerator.util.ReportGeneratorConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<html:form action="/Report/SalesProcessList/Execute.do" focus="parameter(contact)" styleId="processStyle"
           styleClass="form-horizontal">
    <div class="${app2:getFormWrapperTwoColumns()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="SalesProcess.Report.SalesProcessList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Appointment.contact"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <app:text property="parameter(contact)" styleId="fieldAddressName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}" maxlength="40"
                                          tabindex="1" readonly="true"/>
                                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                               isLargeModal="true"
                                                               styleId="searchAddress_id"
                                                               name="searchAddress"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Contact.Title.search"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                                    titleKey="Common.clear"/>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="probability1_id">
                            <fmt:message key="SalesProcess.probability"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="probability1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(probability1)"
                                                    styleId="probability1_id"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    numberType="integer" tabindex="2" maxlength="3"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="probability2">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(probability2)"
                                                    styleId="probability2"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="3"
                                                    maxlength="3" numberType="integer"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="SalesProcess.employee"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(employeeId)"
                                          styleId="employeeId_id"
                                          listName="employeeBaseList" firstEmpty="true"
                                          labelProperty="employeeName" valueProperty="employeeId"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          module="/contacts" tabIndex="4">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="value1_id">
                            <fmt:message key="SalesProcess.value"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="value1_id">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(value1)"
                                                    styleId="value1_id"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="5" maxlength="12"
                                                    numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="value2">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(value2)"
                                                    styleId="value2"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="6"
                                                    maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="SalesProcess.priority"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(priorityId)"
                                          styleId="priorityId_id"
                                          listName="sProcessPriorityList"
                                          labelProperty="name" valueProperty="id"
                                          styleClass="select ${app2:getFormSelectClasses()}"
                                          module="/sales" firstEmpty="true" tabIndex="7">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                            <fmt:message key="SalesProcess.startDate"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-sm-6 marginButton">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startCreateDate)"
                                                      maxlength="10" tabindex="8" styleId="startDate"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <div class="col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endCreateDate)"
                                                      maxlength="10" tabindex="9" styleId="endDate"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="statusId_id">
                            <fmt:message key="SalesProcess.status"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(statusId)"
                                          styleId="statusId_id"
                                          listName="statusList"
                                          labelProperty="statusName" valueProperty="statusId"
                                          styleClass="select ${app2:getFormSelectClasses()}"
                                          module="/sales" firstEmpty="true" tabIndex="10">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class=" ${app2:getFormLabelClassesTwoColumns()}" for="startDueDate">
                            <fmt:message key="SalesProcess.endDate"/>
                        </label>

                        <div class=" ${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-sm-6 marginButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startDueDate)"
                                                      maxlength="10" tabindex="11"
                                                      styleId="startDueDate"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <div class="col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endDueDate)"
                                                      maxlength="10" tabindex="12"
                                                      styleId="endDueDate"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="processName" labelKey="SalesProcess.name"/>
                <titus:reportGroupSortColumnTag name="contactName" labelKey="SalesProcess.contact"/>
                <titus:reportGroupSortColumnTag name="employeeName" labelKey="SalesProcess.employee"/>
                <titus:reportGroupSortColumnTag name="priorityName" labelKey="SalesProcess.priority"/>
                <titus:reportGroupSortColumnTag name="statusName" labelKey="SalesProcess.status"/>
                <titus:reportGroupSortColumnTag name="endDate" labelKey="SalesProcess.endDate" isDefault="true"
                                                defaultOrder="true" isDate="true" isDefaultGrouping="true"
                                                defaultDateGrouping="<%=ReportGeneratorConstants.DATE_FILTER_MONTH%>"/>
                <titus:reportGroupSortColumnTag name="probability" labelKey="SalesProcess.probability"/>
            </titus:reportGroupSortTag>
            <c:set var="reportFormats" value="${salesProcessReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${salesProcessReportForm.pageSizes}" scope="request"/>
            <c:set var="dateFilters" value="${salesProcessReportForm.dateFilters}" scope="request"/>
            <tags:bootstrapReportOptionsTag/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)"
                         tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/></html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="formReset('processStyle')">
                <fmt:message key="Common.clear"/></html:button>
        </div>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="salesProcessReportList" title="SalesProcess.Report.SalesProcessList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="processName" resourceKey="SalesProcess.name" type="${FIELD_TYPE_STRING}" width="13"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="contactName" resourceKey="SalesProcess.contact" type="${FIELD_TYPE_STRING}"
                              width="13"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="employeeName" resourceKey="SalesProcess.employee" type="${FIELD_TYPE_STRING}"
                              width="13"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="priorityName" resourceKey="SalesProcess.priority" type="${FIELD_TYPE_STRING}"
                              width="10"
                              fieldPosition="4"/>
        <!--removed temporally because there's no space-->
            <%--<titus:reportFieldTag name="startDate" resourceKey="SalesProcess.startDate" type="${FIELD_TYPE_DATEINT}"--%>
            <%--fieldPosition="5" patternKey="datePattern" width="8" />--%>
        <titus:reportFieldTag name="statusName" resourceKey="SalesProcess.status" type="${FIELD_TYPE_STRING}" width="9"
                              fieldPosition="5"/>

        <titus:reportFieldTag name="endDate" resourceKey="SalesProcess.endDate" type="${FIELD_TYPE_DATEINT}"
                              fieldPosition="6" patternKey="datePattern" width="8"/>
        <titus:reportFieldTag name="endDateCopy" resourceKey="SalesProcess.endDate" type="${FIELD_TYPE_DATEINT}"
                              fieldPosition="7" patternKey="datePattern" width="8"/>

        <titus:reportFieldTag name="value" resourceKey="SalesProcess.value" align="${FIELD_ALIGN_RIGHT}"
                              type="${FIELD_TYPE_DECIMALNUMBER}" patternKey="numberFormat.2DecimalPlaces" width="8"
                              fieldPosition="8" totalizatorOp="${CALCULATION_SUM}"
                              totalizatorKey="ReportGenerator.totalSum"/>

        <titus:reportFieldTag name="probability" resourceKey="SalesProcess.probability" type="${FIELD_TYPE_STRING}"
                              width="8"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getConcatenatedString probability [%]"
                              fieldPosition="9" align="${FIELD_ALIGN_RIGHT}"/>
            <%--The ponderated value--%>
        <titus:reportFieldTag name="processId" resourceKey="SalesProcess.ponderatedValue" align="${FIELD_ALIGN_RIGHT}"
                              type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}"
                              patternKey="numberFormat.2DecimalPlaces" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getPonderatedValue value probability"
                              fieldPosition="10" totalizatorOp="${CALCULATION_SUM}"
                              totalizatorKey="ReportGenerator.totalSum"/>

            <%--<titus:reportFieldTag name="endDateCopy" resourceKey="SalesProcess.endDate" type="${FIELD_TYPE_DATEINT}" width="8"
                                  patternKey="datePattern" conditionMethod="com.piramide.elwis.utils.ReportHelper.getGroupingDate endDate {REPORT_DATE_PATTERN} [${datePattern}] [${sessionScope.user.valueMap['locale']}]"
                                  fieldPosition="10"/>--%>
    </div>
</html:form>
</table>
