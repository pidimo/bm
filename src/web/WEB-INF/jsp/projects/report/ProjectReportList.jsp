<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<fmt:message key="datePattern" var="datePattern"/>


<fmt:message key="Project.status.entered" var="enteredLabel"/>
<fmt:message key="Project.status.opened" var="openedLabel"/>
<fmt:message key="Project.status.closed" var="closedLabel"/>
<fmt:message key="Project.status.finished" var="finishedLabel"/>
<fmt:message key="Project.status.invoiced" var="invoicedLabel"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endStartDate").val($("#startStartDate").val());
        $("#endEndDate").val($("#startEndDate").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="/Project/ProjectReportList/Execute.do" focus="parameter(responsibleId)"
           styleId="projectReportForm" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="${pagetitle}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="responsibleId_id">
                        <fmt:message key="Project.responsible"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(responsibleId)" styleId="responsibleId_id"
                                      listName="userBaseList"
                                      labelProperty="name" valueProperty="id"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      module="/admin" firstEmpty="true" tabIndex="1">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="name1@_name2@_searchName_id">
                        <fmt:message key="Project.customer"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <app:text property="parameter(name1@_name2@_searchName)"
                                  styleId="name1@_name2@_searchName_id"
                                  styleClass="${app2:getFormSelectClasses()} mediumText"
                                  tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                        <fmt:message key="Project.status"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:set var="statusesOptions" value="${app2:getProjectStatuses(pageContext.request)}"/>
                        <html:select property="parameter(status)" styleId="status_id"
                                     styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="3">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="statusesOptions" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="toBeInvoiced_id">
                        <fmt:message key="Project.toBeInvoiced"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:set var="toBeInvoicedOptions" value="${app2:getToBeInvoicedTypes(pageContext.request)}"/>
                        <html:select property="parameter(toBeInvoiced)" styleId="toBeInvoiced_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="4">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="toBeInvoicedOptions" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startStartDate">
                        <fmt:message key="Project.startDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startStartDate)" maxlength="10" tabindex="5"
                                                  styleId="startStartDate"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endStartDate)" maxlength="10" tabindex="6"
                                                  styleId="endStartDate"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="hasTimeLimit_id">
                        <fmt:message key="Project.hasTimeLimit"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <html:select property="parameter(hasTimeLimit)" styleId="hasTimeLimit_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="7">
                            <html:option value=""></html:option>
                            <html:option value="1"><fmt:message key="Common.yes"/> </html:option>
                            <html:option value="0"><fmt:message key="Common.no"/> </html:option>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                            <%--<html:checkbox property="parameter(hasTimeLimit)" tabindex="15"
                                                               styleClass="radio" value="1"/>--%>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startEndDate">
                        <fmt:message key="Project.endDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startEndDate)" maxlength="10" tabindex="8"
                                                  styleId="startEndDate"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endEndDate)" maxlength="10" tabindex="9"
                                                  styleId="endEndDate"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="accountId_id">
                        <fmt:message key="Project.account"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <fanta:select property="parameter(accountId)" styleId="accountId_id"
                                      listName="accountList" labelProperty="name" valueProperty="accountId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      module="/catalogs" firstEmpty="true"
                                      tabIndex="10">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="plannedAmount1_id">
                        <fmt:message key="Project.plannedInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="plannedAmount1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(plannedAmount1)" styleId="plannedAmount1_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="11"
                                                maxlength="8"
                                                numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="plannedAmount2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(plannedAmount2)" styleId="plannedAmount2_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="12"
                                                maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalInvoiceAmount1_id">
                        <fmt:message key="Project.totalInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="totalInvoiceAmount1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(totalInvoiceAmount1)" styleId="totalInvoiceAmount1_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="13"
                                                maxlength="8"
                                                numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="totalInvoiceAmount2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(totalInvoiceAmount2)" styleId="totalInvoiceAmount2_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="14"
                                                maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="plannedNoAmount1_id">
                        <fmt:message key="Project.plannedNoInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="plannedNoAmount1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(plannedNoAmount1)" styleId="plannedNoAmount1_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="15"
                                                maxlength="8"
                                                numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="plannedNoAmount2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(plannedNoAmount2)" styleId="plannedNoAmount2_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="16"
                                                maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalNoInvoiceAmount1_id">
                        <fmt:message key="Project.totalNoInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="totalNoInvoiceAmount1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(totalNoInvoiceAmount1)" styleId="totalNoInvoiceAmount1_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="17"
                                                maxlength="8"
                                                numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="totalNoInvoiceAmount2_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(totalNoInvoiceAmount2)" styleId="totalNoInvoiceAmount2_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="18"
                                                maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <c:set var="reportFormats" value="${projectReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${projectReportForm.pageSizes}" scope="request"/>
        </fieldset>
        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="projectName" labelKey="Project.name"/>
            <titus:reportGroupSortColumnTag name="userName" labelKey="Project.responsible"/>
            <titus:reportGroupSortColumnTag name="customerName" labelKey="Project.customer"/>
            <titus:reportGroupSortColumnTag name="startDate" labelKey="Project.startDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="endDate" labelKey="Project.endDate" isDate="true"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>

    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="resetForm(document.projectReportForm)">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

    <titus:reportInitializeConstantsTag/>

    <titus:reportTag id="projectReportList" title="Project.Report.ProjectList"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"/>

    <titus:reportFieldTag name="projectName" resourceKey="Project.name" type="${FIELD_TYPE_STRING}" width="16"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="userName" resourceKey="Project.responsible" type="${FIELD_TYPE_STRING}" width="14"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="customerName" resourceKey="Project.customer" type="${FIELD_TYPE_STRING}" width="14"
                          fieldPosition="3"/>

    <titus:reportFieldTag name="startDate" resourceKey="Project.startDate" type="${FIELD_TYPE_DATEINT}"
                          patternKey="datePattern" width="8" fieldPosition="4"/>
    <titus:reportFieldTag name="endDate" resourceKey="Project.endDate" type="${FIELD_TYPE_DATEINT}"
                          patternKey="datePattern" width="8" fieldPosition="5"/>

    <titus:reportFieldTag name="plannedInvoice" resourceKey="Project.plannedInvoice"
                          type="${FIELD_TYPE_DECIMALNUMBER}" width="8" fieldPosition="6"
                          patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"
                          totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>

    <titus:reportFieldTag name="totalInvoice" resourceKey="Project.totalInvoice" type="${FIELD_TYPE_DECIMALNUMBER}"
                          width="8" fieldPosition="7"
                          patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"
                          totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>

    <titus:reportFieldTag name="plannedNoInvoice" resourceKey="Project.plannedNoInvoice"
                          type="${FIELD_TYPE_DECIMALNUMBER}" width="8" fieldPosition="8"
                          patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"
                          totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>

    <titus:reportFieldTag name="totalNoInvoice" resourceKey="Project.totalNoInvoice"
                          type="${FIELD_TYPE_DECIMALNUMBER}" width="8" fieldPosition="9"
                          patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"
                          totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>

    <titus:reportFieldTag name="status" resourceKey="Project.status" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="10"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getProjectStatusLabel status [${enteredLabel}] [${openedLabel}] [${closedLabel}] [${finishedLabel}] [${invoicedLabel}]"/>

</html:form>


