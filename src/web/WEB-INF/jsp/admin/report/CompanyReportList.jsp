<%@ page import="com.piramide.elwis.utils.AdminConstants" %>
<%@ include file="/Includes.jsp" %>
<%
    if (null != request.getParameter("parameter(rangeAmount)"))
        request.setAttribute("range_amount", Boolean.valueOf(true));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<tags:initBootstrapDatepicker/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endCreationDate").val($("#startCreationDate").val());
        $("#endDeliveryDate").val($("#startDeliveryDate").val());
        $("#endFinishDeliveryDate").val($("#startFinishDeliveryDate").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="/Report/CompanyList/Execute.do" focus="parameter(active)" styleId="companyReportForm"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Admin.Report.CompanyList"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="active_id">
                        <fmt:message key="Common.active"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:choose>
                            <c:when test="${companyReportForm.params.active==null}">
                                <html:select property="parameter(active)" styleId="active_id" value="1"
                                             styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                             tabindex="1">
                                    <html:options collection="activeList" property="value" labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <html:select property="parameter(active)"
                                             styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="1">
                                    <html:options collection="activeList" property="value" labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startCreationDate">
                        <fmt:message key="Common.creationDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message var="datePattern" key="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startCreationDate)" maxlength="10" tabindex="2"
                                                  styleId="startCreationDate" calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endCreationDate)" maxlength="10" tabindex="3"
                                                  styleId="endCreationDate" calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  convert="true" mode="bootstrap"
                                                  placeHolder="${to}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="companyType_id">
                        <fmt:message key="Company.companyType"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:set var="companyTypes" value="${app2:getCompanyTypes(pageContext.request)}"/>
                        <html:select property="parameter(companyType)" styleId="companyType_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="4">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="companyTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDeliveryDate">
                        <fmt:message key="Company.startLicenseDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message var="datePattern" key="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(initStartLicenseDate_1)" maxlength="10"
                                                  tabindex="5"
                                                  styleId="startDeliveryDate" calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  onchange="changeStartDateValue()"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endStartLicenseDate_1)" maxlength="10"
                                                  tabindex="6"
                                                  styleId="endDeliveryDate" calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  convert="true" mode="bootstrap"
                                                  placeHolder="${to}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="initNumberOfUsers_3_id">
                        <fmt:message key="Company.usersAllowed"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label" for="initNumberOfUsers_3_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <html:text property="parameter(initNumberOfUsers_3)" styleId="initNumberOfUsers_3_id"
                                           styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                           tabindex="7"
                                           maxlength="15"/>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label" for="amount2">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <html:text property="parameter(endNumberOfUsers_3)"
                                           styleClass="${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                           tabindex="8"
                                           maxlength="15"
                                           styleId="amount2"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startFinishDeliveryDate">
                        <fmt:message key="Company.finishLicenseDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message var="datePattern" key="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(initFinishLicenseDate_2)" maxlength="10"
                                                  tabindex="9"
                                                  styleId="startFinishDeliveryDate" calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>

                                    <app:dateText property="parameter(endFinishLicenseDate_2)" maxlength="10"
                                                  tabindex="10"
                                                  styleId="endFinishDeliveryDate" calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  convert="true" mode="bootstrap"
                                                  placeHolder="${to}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
        </fieldset>
        <c:set var="reportFormats" value="${companyReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${companyReportForm.pageSizes}" scope="request"/>

        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="companyName" labelKey="Company"/>
            <titus:reportGroupSortColumnTag name="login" labelKey="Company.login"/>
            <titus:reportGroupSortColumnTag name="usersAllowed" labelKey="Company.usersAllowed"/>
            <titus:reportGroupSortColumnTag name="startLicense" labelKey="Company.startLicenseDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="finishLicense" labelKey="Company.finishLicenseDate"
                                            isDate="true"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="formReset('companyReportForm')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>


    <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
    <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
    <c:set var="default"><fmt:message key="Common.default"/></c:set>

    <fmt:message var="regularLabel" key="<%=AdminConstants.CompanyType.REGULAR.getResource()%>"/>
    <fmt:message var="trialLabel" key="<%=AdminConstants.CompanyType.TRIAL.getResource()%>"/>
    <fmt:message var="demoLabel" key="<%=AdminConstants.CompanyType.DEMO.getResource()%>"/>


    <titus:reportInitializeConstantsTag/>

    <titus:reportTag id="companyReportList" title="Admin.Report.CompanyList"
                     locale="${sessionScope.user.valueMap['locale']}"/>

    <titus:reportFieldTag name="companyName" resourceKey="Company" type="${FIELD_TYPE_STRING}" width="20"
                          fieldPosition="1"/>

    <titus:reportFieldTag name="login" resourceKey="Company.login" type="${FIELD_TYPE_STRING}" width="20"
                          fieldPosition="2"/>

    <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                          fieldPosition="3"/>

    <titus:reportFieldTag name="companyType" resourceKey="Company.companyType" type="${FIELD_TYPE_STRING}" width="10"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getCompanyTypeLabel companyType [${regularLabel}] [${trialLabel}] [${demoLabel}]"
                          fieldPosition="4"/>

    <titus:reportFieldTag name="usersAllowed" resourceKey="Company.usersAllowed" type="${FIELD_TYPE_INTEGER}"
                          width="10"
                          align="${FIELD_ALIGN_RIGHT}" fieldPosition="5"/>

    <titus:reportFieldTag name="startLicense" resourceKey="Company.startLicenseDate" type="${FIELD_TYPE_DATEINT}"
                          width="15" fieldPosition="6" patternKey="datePattern"/>

    <titus:reportFieldTag name="finishLicense" resourceKey="Company.finishLicenseDate" type="${FIELD_TYPE_DATEINT}"
                          width="15" fieldPosition="7" patternKey="datePattern"/>

</html:form>

<tags:jQueryValidation formName="companyReportForm"/>
