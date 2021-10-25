<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<%
    pageContext.setAttribute("inOutCommunicationList", JSPHelper.getInOutCommunicationList(request));
%>

<script>

    function goSubmit() {
        document.forms[0].submit();
    }

    function clearValues(t) {

        if (document.getElementById('fieldAddressId_id').value != "" && t == '1') {
            document.getElementById('fieldAddressId_id').value = "";
            document.getElementById('fieldAddressName_id').value = "";
            document.getElementById('fieldProcessId_id').value = "";
            document.getElementById('fieldProcessName_id').value = "";
            goSubmit();
        }
        if (document.getElementById('fieldProcessId_id').value != "" && t == '2') {
            document.getElementById('fieldAddressId_id').value = "";
            document.getElementById('fieldAddressName_id').value = "";
            document.getElementById('fieldProcessId_id').value = "";
            document.getElementById('fieldProcessName_id').value = "";
            goSubmit();
        }
    }

</script>

<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="/Report/CommunicationList/Execute.do"
               focus="parameter(address)"
               styleId="communicationReportList"
               styleClass="form-horizontal">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Contact.Report.CommunicationList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <app:text property="parameter(address)"
                                          styleId="fieldAddressName_id"
                                          styleClass="${app2:getFormInputClasses()} middleText"
                                          maxlength="40"
                                          tabindex="1"
                                          readonly="true"/>
                                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                   name="searchAddress"
                                                   titleKey="Common.search"
                                                   modalTitleKey="Contact.Title.search"
                                                   hide="false"
                                                   tabindex="1"
                                                   isLargeModal="true"
                                                   styleId="contactSelectPopup_id"
                                                   submitOnSelect="true"/>
                        <tags:clearBootstrapSelectPopup onclick="clearValues(1)"
                                                        keyFieldId="fieldAddressId_id"
                                                        tabindex="1"
                                                        nameFieldId="fieldAddressName_id"
                                                        titleKey="Common.clear"/>
                    </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                            <fmt:message key="Communication.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <c:set var="communicationTypes"
                                   value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                            <html:select property="parameter(type)"
                                         styleId="type_id"
                                         styleClass="${app2:getFormSelectClasses()} select"
                                         tabindex="2">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="communicationTypes" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId">
                            <fmt:message key="ContactPerson"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(contactPersonId)"
                                          styleId="contactPersonId"
                                          listName="contactPersonList"
                                          firstEmpty="true"
                                          labelProperty="contactPersonName"
                                          valueProperty="contactPersonId"
                                          module="/contacts"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          tabIndex="3">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty communicationReportListForm.params.addressId?communicationReportListForm.params.addressId:0}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="in_Out_id">
                            <fmt:message key="Document.inout"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(in_Out)"
                                         tabindex="4"
                                         styleId="in_Out_id"
                                         styleClass="${app2:getFormSelectClasses()} select">
                                <html:options collection="inOutCommunicationList" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

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
                                          tabindex="5"/>
                                <html:hidden property="parameter(processId)" styleId="fieldProcessId_id"/>
                                <html:hidden property="parameter(percentId)" styleId="percent_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup
                                url="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${communicationReportListForm.params.addressId}"
                                name="salesProcessList"
                                styleId="salesProcessPopup_id"
                                styleClass="${app2:getFormButtonClasses()}"
                                titleKey="Common.search"
                                modalTitleKey="SalesProcess.Title.simpleSearch"
                                tabindex="5"
                                isLargeModal="true"
                                submitOnSelect="true"/>
                        <tags:clearBootstrapSelectPopup onclick="clearValues(2)"
                                                        tabindex="5"
                                                        keyFieldId="fieldProcessId_id"
                                                        nameFieldId="fieldProcessName_id"
                                                        titleKey="Common.clear"/>
                    </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange">
                            <fmt:message key="Document.date"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startRange)"
                                                      maxlength="10"
                                                      tabindex="6"
                                                      styleId="startRange"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>

                                    <div class="input-group date">
                                        <app:dateText property="parameter(endRange)"
                                                      maxlength="10"
                                                      tabindex="7"
                                                      styleId="endRange"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="Contact.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(employeeId)"
                                          listName="employeeBaseList"
                                          tabIndex="8"
                                          styleId="employeeId_id"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                                          module="/contacts"
                                          firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>
            </fieldset>
            <c:set var="reportFormats" value="${communicationReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${communicationReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="contactName" labelKey="Contact" isDefault="true"
                                                defaultOrder="true" isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="note" labelKey="Document.subject"/>
                <titus:reportGroupSortColumnTag name="contactPersonName" labelKey="ContactPerson"/>
                <titus:reportGroupSortColumnTag name="employeeName" labelKey="SalesProcess.employee"/>
                <titus:reportGroupSortColumnTag name="processName" labelKey="Document.salesAsociated"/>
                <titus:reportGroupSortColumnTag name="date" labelKey="Document.date" isDate="true"/>
                <titus:reportGroupSortColumnTag name="inOut" labelKey="Document.inout"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">

            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('communicationReportList')">
                <fmt:message key="Common.clear"/>
            </html:button>

        </div>
        <c:set var="phone"><fmt:message key="Communication.type.phone"/></c:set>
        <c:set var="meeting"><fmt:message key="Communication.type.meeting"/></c:set>
        <c:set var="fax"><fmt:message key="Communication.type.fax"/></c:set>
        <c:set var="letter"><fmt:message key="Communication.type.letter"/></c:set>
        <c:set var="mail"><fmt:message key="Communication.type.email"/></c:set>
        <c:set var="other"><fmt:message key="Communication.type.other"/></c:set>
        <c:set var="document"><fmt:message key="Communication.type.document"/></c:set>
        <c:set var="webDocument"><fmt:message key="Communication.type.webDocument"/></c:set>

        <c:set var="in"><fmt:message key="Document.in"/></c:set>
        <c:set var="out"><fmt:message key="Document.out"/></c:set>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="communicationReportList" title="Contact.Report.CommunicationList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="contactName" resourceKey="Contact" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="note" resourceKey="Document.subject" type="${FIELD_TYPE_STRING}" width="16"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="contactPersonName" resourceKey="ContactPerson" type="${FIELD_TYPE_STRING}"
                              width="15"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="employeeName" resourceKey="SalesProcess.employee" type="${FIELD_TYPE_STRING}"
                              width="15"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="processName" resourceKey="Document.salesAsociated" type="${FIELD_TYPE_STRING}"
                              width="10"
                              fieldPosition="5"/>
        <titus:reportFieldTag name="date" resourceKey="Document.date" type="${FIELD_TYPE_DATEINT}"
                              patternKey="datePattern"
                              width="10"
                              fieldPosition="6"/>
        <titus:reportFieldTag name="type" resourceKey="Document.mediaType" type="${FIELD_TYPE_STRING}" width="7"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getMediaTypeResource type [${phone}] [${meeting}] [${fax}] [${letter}] [${other}] [${mail}] [${document}] [${webDocument}] [0] [1] [2] [3] [4] [5] [6] [7]"
                              fieldPosition="7"/>
        <titus:reportFieldTag name="inOut" resourceKey="Document.inout" type="${FIELD_TYPE_STRING}" width="7"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource inOut [${in}] [${out}] [1] [0]"
                              fieldPosition="8"/>
    </html:form>

</div>