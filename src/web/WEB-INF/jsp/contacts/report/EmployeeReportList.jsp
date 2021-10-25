<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<%--<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>--%>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>

    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }

</script>


<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="/Report/EmployeeList/Execute.do"
               focus="parameter(healthFundName)"
               styleId="employeeReport"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">

            <legend class="title">
                <fmt:message key="Contact.Report.EmployeeList"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Contact.Organization.Employee.healthFund"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">

                        <div class="input-group">
                            <app:text property="parameter(healthFundName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      tabindex="1"
                                      styleId="fieldAddressName_id"
                                      disabled="true"/>
                            <html:hidden property="parameter(healthFundId)" styleId="fieldAddressId_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup url="/contacts/OrganizationSearch.do"
                                               name="organizationSearchList"
                                               titleKey="Common.search"
                                               modalTitleKey="Contact.Title.search"
                                               styleClass="${app2:getFormButtonClasses()}"
                                               styleId="contactSelectPopup_id"
                                               isLargeModal="true"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                    nameFieldId="fieldAddressName_id"
                                                    styleClass="${app2:getFormButtonClasses()}"
                                                    titleKey="Common.clear"/>
                </span>
                        </div>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="hireDate1">
                        <fmt:message key="Contact.Organization.Employee.hireDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(hireDate1)"
                                                  maxlength="10"
                                                  tabindex="2"
                                                  styleId="hireDate1"
                                                  placeHolder="${from}"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message var="to" key="Common.to"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(hireDate2)"
                                                  maxlength="10"
                                                  tabindex="3"
                                                  placeHolder="${to}"
                                                  styleId="hireDate2"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="departmentId_id">
                        <fmt:message key="Contact.Organization.Employee.department"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(departmentId)"
                                      listName="departmentBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="departmentId"
                                      styleClass="${app2:getFormInputClasses()} mediumSelect"
                                      tabIndex="4"
                                      styleId="departmentId_id">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange">
                        <fmt:message key="Contact.Organization.Employee.endDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">

                                <div class="input-group date">
                                    <app:dateText property="parameter(endDate1)"
                                                  maxlength="10"
                                                  tabindex="5"
                                                  styleId="startRange"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">

                                <div class="input-group date">
                                    <app:dateText property="parameter(endDate2)"
                                                  maxlength="10"
                                                  tabindex="6"
                                                  styleId="endRange"
                                                  placeHolder="${to}"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}  dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="officeId_id">
                        <fmt:message key="Contact.Organization.Employee.office"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <!-- Collection offices-->
                        <fanta:select property="parameter(officeId)"
                                      listName="officeBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="officeId"
                                      styleClass="${app2:getFormInputClasses()} mediumSelect"
                                      tabIndex="7"
                                      styleId="officeId_id">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="cost1_id">
                        <fmt:message key="Contact.Organization.Employee.costPosition"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>

                                <app:numberText property="parameter(cost1)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="8"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"
                                                styleId="cost1_id"/>

                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>

                                <app:numberText property="parameter(cost2)"
                                                styleId="cost2"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="9"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="function_id">
                        <fmt:message key="Contact.Organization.Employee.function"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(function)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="40"
                                  tabindex="10"
                                  styleId="function_id"/>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="costHour1_id">
                        <fmt:message key="Contact.Organization.Employee.costHour"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>

                                <app:numberText property="parameter(costHour1)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="11"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"
                                                styleId="costHour1_id"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>

                                <app:numberText property="parameter(costHour2)"
                                                styleId="costHour2"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="12"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>

                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="costCenterId_id">
                        <fmt:message key="Contact.Organization.Employee.costCenter"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <!-- Collection costcenters-->
                        <fanta:select property="parameter(costCenterId)"
                                      listName="costCenterBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleClass="${app2:getFormInputClasses()} mediumSelect"
                                      module="/catalogs"
                                      tabIndex="13"
                                      styleId="costCenterId_id">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="hourlyRate1_id">
                        <fmt:message key="Contact.Organization.Employee.hourlyRate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(hourlyRate1)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="14"
                                                maxlength="19"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"
                                                styleId="hourlyRate1_id"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(hourlyRate2)"
                                                styleId="costHour2"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="15"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>


            </div>

            <c:set var="reportFormats" value="${employeeReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${employeeReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag mode="bootstrap" width="100%"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="employeeName" labelKey="Employee.name"/>
                <titus:reportGroupSortColumnTag name="departmentName" labelKey="Employee.DepartmentName"/>
                <titus:reportGroupSortColumnTag name="officeName" labelKey="Employee.officeName"/>
                <titus:reportGroupSortColumnTag name="hireDate"
                                                labelKey="Contact.Organization.Employee.hireDate"
                                                isDate="true"/>
                <titus:reportGroupSortColumnTag name="endDate"
                                                labelKey="Contact.Organization.Employee.endDate"
                                                isDate="true"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>


            <titus:reportInitializeConstantsTag/>
            <titus:reportTag id="employeeReportList" title="Contact.Report.EmployeeList"
                             locale="${sessionScope.user.valueMap['locale']}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

            <titus:reportFieldTag name="employeeName" resourceKey="Employee.name" type="${FIELD_TYPE_STRING}" width="30"
                                  fieldPosition="1"/>
            <titus:reportFieldTag name="departmentName" resourceKey="Employee.DepartmentName"
                                  type="${FIELD_TYPE_STRING}"
                                  width="20"
                                  fieldPosition="2"/>
            <titus:reportFieldTag name="officeName" resourceKey="Employee.officeName" type="${FIELD_TYPE_STRING}"
                                  width="20"
                                  fieldPosition="3"/>
            <titus:reportFieldTag name="hireDate" resourceKey="Contact.Organization.Employee.hireDate"
                                  type="${FIELD_TYPE_DATEINT}"
                                  patternKey="datePattern" width="15" fieldPosition="4"/>
            <titus:reportFieldTag name="endDate" resourceKey="Contact.Organization.Employee.endDate"
                                  type="${FIELD_TYPE_DATEINT}"
                                  patternKey="datePattern" width="15" fieldPosition="5"/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}"
                         property="parameter(generate)"
                         tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1"
                         tabindex="58"
                         styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('employeeReport')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

    </html:form>

</div>
