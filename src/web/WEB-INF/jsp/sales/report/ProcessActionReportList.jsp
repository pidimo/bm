<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>

<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveOrNoList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>


<script>
    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }

    function goSubmit() {
        document.forms[0].submit();
    }
    function clearValues() {
        if (document.getElementById('fieldProcessName_id').value != "") {
            document.getElementById('fieldProcessName_id').value = "";
            document.getElementById('fieldProcessId_id').value = "";
            document.getElementById('contactPerson').value = "";
            document.getElementById('fieldAddressId_id').value = "";
            goSubmit();
        }
    }
</script>

<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="/Report/ProcessActionList/Execute.do"
               focus="parameter(processName)"
               styleId="processStyle"
               styleClass="form-horizontal">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="SalesProcess.Report.ProcessActionList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldProcessName_id">
                            <fmt:message key="Document.salesAsociated"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">

                            <div class="input-group">
                                <app:text property="parameter(processName)"
                                          styleId="fieldProcessName_id"
                                          styleClass="${app2:getFormInputClasses()} middleText"
                                          maxlength="30"
                                          readonly="true"
                                          tabindex="1"/>
                                <html:hidden property="parameter(processId)" styleId="fieldProcessId_id"/>
                                <html:hidden property="parameter(percentId)" styleId="percent_id"/>
                                <html:hidden property="parameter(id)" styleId="fieldAddressId_id"/>
                                <html:hidden property="parameter(name)" styleId="fieldAddressName_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup url="/sales/SalesProcess/SearchSalesProcess.do"
                                               name="searchSalesProcess"
                                               titleKey="Common.search"
                                               modalTitleKey="SalesProcess.Title.simpleSearch"
                                               styleClass="${app2:getFormButtonClasses()}"
                                               styleId="contactSelectPopup_id"
                                               isLargeModal="true"
                                               submitOnSelect="true"
                                               tabindex="1"/>

                    <tags:clearBootstrapSelectPopup onclick="clearValues()"
                                                    keyFieldId="fieldProcessId_id"
                                                    nameFieldId="fieldProcessName_id"
                                                    styleClass="${app2:getFormButtonClasses()}"
                                                    titleKey="Common.clear"
                                                    tabindex="1"/>
                    <html:hidden property="parameter(addressProcessId)"/>

                </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startSendDate">
                            <fmt:message key="Document.date"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>

                                    <div class="input-group date">
                                        <app:dateText property="parameter(startSendDate)"
                                                      maxlength="10"
                                                      tabindex="2"
                                                      placeHolder="${from}"
                                                      styleId="startSendDate"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      mode="bootstrap"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <fmt:message key="Common.to" var="to"/>

                                    <div class="input-group date">
                                        <app:dateText property="parameter(endSendDate)"
                                                      maxlength="10"
                                                      tabindex="3"
                                                      placeHolder="${to}"
                                                      styleId="endSendDate"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPerson">
                            <fmt:message key="Appointment.contactPerson"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(contactPersonId)"
                                          listName="contactPersonSimpleList"
                                          firstEmpty="true"
                                          styleId="contactPerson"
                                          labelProperty="contactPersonName"
                                          valueProperty="contactPersonId"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          module="/contacts"
                                          tabIndex="4">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty processActionReportForm.params.id?processActionReportForm.params.id:0}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="actionTypeId_id">
                            <fmt:message key="SalesProcessAction.actionType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(actionTypeId)"
                                          listName="actionTypeBaseList"
                                          labelProperty="name"
                                          styleId="actionTypeId_id"
                                          valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          module="/sales"
                                          firstEmpty="true"
                                          tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="SalesProcess.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(employeeId)"
                                          listName="employeeBaseList"
                                          firstEmpty="true"
                                          styleId="employeeId_id"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          module="/contacts"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="isActive_id">
                            <fmt:message key="Common.active"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(isActive)"
                                         styleId="isActive_id"
                                         styleClass="${app2:getFormSelectClasses()} shortSelect"
                                         tabindex="7">
                                <html:options collection="activeList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>
            </fieldset>

            <div>
                <titus:reportGroupSortTag width="100%"
                                          mode="bootstrap"
                                          tableStyleClass="${app2:getTableClasesIntoForm()}">
                    <titus:reportGroupSortColumnTag name="processName" labelKey="SalesProcess.name" isDefault="true"
                                                    defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="note" labelKey="SalesProcessAction.title"/>
                    <titus:reportGroupSortColumnTag name="actionTypeName" labelKey="SalesProcessAction.actionType"/>
                    <titus:reportGroupSortColumnTag name="employeeName" labelKey="Document.employee"/>
                    <titus:reportGroupSortColumnTag name="date" labelKey="Document.date" isDate="true"/>
                    <titus:reportGroupSortColumnTag name="value" labelKey="SalesProcess.value"/>
                </titus:reportGroupSortTag>

                <c:set var="reportFormats" value="${processActionReportForm.reportFormats}" scope="request"/>
                <c:set var="pageSizes" value="${processActionReportForm.pageSizes}" scope="request"/>

                <tags:bootstrapReportOptionsTag/>

                <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
                <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>

                <titus:reportInitializeConstantsTag/>
                <titus:reportTag id="actionReportList" title="SalesProcess.Report.ProcessActionList"
                                 locale="${sessionScope.user.valueMap['locale']}"
                                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
                <titus:reportFieldTag name="processName" resourceKey="SalesProcess.processName"
                                      type="${FIELD_TYPE_STRING}"
                                      width="15"
                                      fieldPosition="1"/>
                <titus:reportFieldTag name="note" resourceKey="SalesProcessAction.title" type="${FIELD_TYPE_STRING}"
                                      width="15"
                                      fieldPosition="2"/>
                <titus:reportFieldTag name="actionTypeName" resourceKey="SalesProcessAction.actionType"
                                      type="${FIELD_TYPE_STRING}"
                                      width="15" fieldPosition="3"/>
                <titus:reportFieldTag name="employeeName" resourceKey="Document.employee" type="${FIELD_TYPE_STRING}"
                                      width="25"
                                      fieldPosition="4"/>
                <titus:reportFieldTag name="date" resourceKey="Document.date" type="${FIELD_TYPE_DATEINT}"
                                      patternKey="datePattern"
                                      width="10" fieldPosition="5"/>
                <titus:reportFieldTag name="value" resourceKey="SalesProcess.value" type="${FIELD_TYPE_DECIMALNUMBER}"
                                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="10"
                                      fieldPosition="6"/>
                <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                                      fieldPosition="7"
                                      conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"/>

            </div>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('processStyle')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

    </html:form>

</div>