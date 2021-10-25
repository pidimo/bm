<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:initBootstrapDatepicker/>


<app2:jScriptUrl url="/admin/Report/UserSessionList.do?module=admin" var="locationJsUrl" addModuleParams="false"/>


<script>
    function jump() {
        window.location = ${locationJsUrl};
    }
</script>

<c:set var="myTabNumber" value="0"/>
<html:form action="/Report/UserSessionList/Execute.do" focus="parameter(long@initLastLoginDate_1)"
           styleId="userSessionForm" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Admin.Report.UserSessionList"/>
        </legend>
        <div class="row">
            <div class="${app2:getRowClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="User.startConection"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <fmt:message var="datePattern" key="datePattern"/>
                            <fmt:message key="Common.from" var="from"/>
                            <div class="input-group date">
                                <app:dateText property="parameter(long@initLastLoginDate_1)"
                                              maxlength="10"
                                              tabindex="1"
                                              styleId="initLastLoginDate_1"
                                              calendarPicker="true"
                                              mode="bootstrap"
                                              placeHolder="${from}"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              convert="true"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <fmt:message key="Common.to" var="to"/>
                            <div class="input-group date">
                                <app:dateText property="parameter(long@endLastLoginDate_1)"
                                              maxlength="10"
                                              tabindex="2"
                                              styleId="endLastLoginDate_1"
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

            <div class="${app2:getRowClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="User.lastConection"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <fmt:message var="datePattern" key="datePattern"/>
                            <fmt:message key="Common.from" var="from"/>
                            <div class="input-group date">
                                <app:dateText property="parameter(long@initLastActionDate_2)"
                                              maxlength="10"
                                              tabindex="3"
                                              styleId="initLastActionDate_2"
                                              calendarPicker="true"
                                              mode="bootstrap"
                                              placeHolder="${from}"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              convert="true"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <fmt:message key="Common.to" var="to"/>
                            <div class="input-group date">
                                <app:dateText property="parameter(long@endLastActionDate_2)"
                                              maxlength="10"
                                              tabindex="4"
                                              styleId="endLastActionDate_2"
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
            <div class="${app2:getRowClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="User.endConection"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <fmt:message var="datePattern" key="datePattern"/>
                            <fmt:message key="Common.from" var="from"/>
                            <div class="input-group date">
                                <app:dateText property="parameter(long@initLogOffDate_3)"
                                              maxlength="10"
                                              tabindex="5"
                                              styleId="initLogOffDate_3"
                                              calendarPicker="true"
                                              placeHolder="${from}"
                                              mode="bootstrap"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              convert="true"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <fmt:message key="Common.to" var="to"/>
                            <div class="input-group date">
                                <app:dateText property="parameter(long@endLogOffDate_3)"
                                              maxlength="10"
                                              tabindex="6"
                                              styleId="endLogOffDate_3"
                                              calendarPicker="true"
                                              placeHolder="${to}"
                                              mode="bootstrap"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              convert="true"/>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <c:choose>
                <c:when test="${sessionScope.user.valueMap['isDefaultCompany'] == true}">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Company"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:hidden property="parameter(reportCompanyId)" styleId="fieldReportCompany_id"/>
                            <div class="input-group">
                                <app:text property="parameter(reportCompany)"
                                          styleId="fieldReportCompanyName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="7"
                                          readonly="true"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup url="/admin/Report/SearchCompany.do"
                                                       name="searchCompany"
                                                       styleId="searchCompany_id"
                                                       isLargeModal="true"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Admin.Company.Title.search"
                                                       hide="false"
                                                       submitOnSelect="false"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldReportCompany_id"
                                                            nameFieldId="fieldReportCompanyName_id"
                                                            titleKey="Common.clear"/>
                        </span>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="">

                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:hidden property="parameter(reportCompanyId)"
                                         value="${sessionScope.user.valueMap['companyId']}"/>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:set var="reportFormats" value="${userSessionReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userSessionReportForm.pageSizes}" scope="request"/>

        <titus:reportGroupSortTag tableStyleClass="${app2:getTableClasesIntoForm()}" mode="bootstrap" width="100%">
            <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName"/>
            <titus:reportGroupSortColumnTag name="companyName" labelKey="Company"
                                            render="${sessionScope.user.valueMap['isDefaultCompany'] == true}"/>
            <titus:reportGroupSortColumnTag name="startConection" labelKey="User.startConection" isDate="true"/>
            <titus:reportGroupSortColumnTag name="endConection" labelKey="User.endConection" isDate="true"/>
            <titus:reportGroupSortColumnTag name="lastConection" labelKey="User.lastConection" isDate="true"/>
            <titus:reportGroupSortColumnTag name="ip" labelKey="User.ip"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="userSessionReportList" title="Admin.Report.UserSessionList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="userName" resourceKey="User.userName" type="${FIELD_TYPE_STRING}" width="30"
                              fieldPosition="1"/>


        <c:set var="width" value="17"/>

        <c:if test="${sessionScope.user.valueMap['isDefaultCompany'] == true}">
            <titus:reportFieldTag name="companyName" resourceKey="Company" type="${FIELD_TYPE_STRING}" width="30"
                                  fieldPosition="2"/>
            <c:set var="width" value="10"/>

        </c:if>


        <titus:reportFieldTag name="startConection" resourceKey="User.startConection"
                              type="${FIELD_TYPE_DATELONG}" width="${width}" fieldPosition="3"
                              patternKey="dateTimePattern"/>

        <titus:reportFieldTag name="endConection" resourceKey="User.endConection" type="${FIELD_TYPE_DATELONG}"
                              width="${width}" fieldPosition="4" patternKey="dateTimePattern"/>

        <titus:reportFieldTag name="lastConection" resourceKey="User.lastConection"
                              type="${FIELD_TYPE_DATELONG}" width="${width}" fieldPosition="5"
                              patternKey="dateTimePattern"/>

        <titus:reportFieldTag name="ip" resourceKey="User.ip" type="${FIELD_TYPE_STRING}" width="${width}"
                              align="${FIELD_ALIGN_RIGHT}" fieldPosition="6"/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="cancel" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="jump()"
                     tabindex="58">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

</html:form>
