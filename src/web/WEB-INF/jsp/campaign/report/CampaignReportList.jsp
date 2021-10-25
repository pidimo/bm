<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getStatusList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:initBootstrapDatepicker/>


    <html:form action="/Report/CampaignList/Execute.do" focus="parameter(employeeId)" styleId="campaignReport" styleClass="form-horizontal">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Campaign.Report.CampaignList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.responsibleEmployee"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(employeeId)"
                                          listName="employeeBaseList"
                                          tabIndex="1"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          module="/contacts" firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.budgetedCost"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(budgetCost1)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="2"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(budgetCost2)"
                                                    styleId="price2"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="3"
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.status"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(status)" styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="4">
                                <html:option value=""/>
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.profits"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(awaitedUtility1)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="5"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(awaitedUtility2)"
                                                    styleId="price2"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="6"
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.type"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(typeId)"
                                          listName="campaignTypeList"
                                          valueProperty="id"
                                          labelProperty="title"
                                          firstEmpty="true"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          module="/catalogs"
                                          tabIndex="7"
                                          readOnly="${'delete' == op}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.realCost"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(realCost1)"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="8"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"/>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(realCost2)"
                                                    styleId="price2"
                                                    styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
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
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.dateCreation"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startDate1)"
                                                      maxlength="10"
                                                      tabindex="10"
                                                      styleId="startDate1"
                                                      calendarPicker="true"
                                                      placeHolder="${from}"
                                                      mode="bootstrap"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true"/>
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startDate2)"
                                                      maxlength="10"
                                                      tabindex="11"
                                                      styleId="startDate2"
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

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.totalContacts"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:text property="parameter(numberContacts1)"
                                              styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                              tabindex="12"/>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label" for="">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:text property="parameter(numberContacts2)"
                                              styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                              tabindex="13"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.closeDate"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endDate1)"
                                                      maxlength="10"
                                                      tabindex="14"
                                                      styleId="endDate1"
                                                      calendarPicker="true"
                                                      placeHolder="${from}"
                                                      mode="bootstrap"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      convert="true"/>
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endDate2)"
                                                      maxlength="10"
                                                      tabindex="15"
                                                      styleId="endDate2"
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
                </div>
            </fieldset>

            <c:set var="reportFormats" value="${campaignReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${campaignReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap" tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="campaignName" labelKey="Campaign.mailing"/>
                <titus:reportGroupSortColumnTag name="employeeName" labelKey="Campaign.responsibleEmployee"/>
                <titus:reportGroupSortColumnTag name="startDate" labelKey="Campaign.dateCreation" isDate="true"/>
                <titus:reportGroupSortColumnTag name="endDate" labelKey="Campaign.closeDate" isDate="true"/>
            </titus:reportGroupSortTag>


            <tags:bootstrapReportOptionsTag/>


            <c:set var="preparation"><fmt:message key="Campaign.preparation"/></c:set>
            <c:set var="sent"><fmt:message key="Campaign.sent"/></c:set>
            <c:set var="cancel"><fmt:message key="Campaign.cancel"/></c:set>

            <titus:reportInitializeConstantsTag/>

            <titus:reportTag id="campaignReportList" title="Campaign.Report.CampaignList"
                             locale="${sessionScope.user.valueMap['locale']}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <titus:reportFieldTag name="campaignName" resourceKey="Campaign.mailing" type="${FIELD_TYPE_STRING}" width="30"
                                  fieldPosition="1"/>
            <titus:reportFieldTag name="employeeName" resourceKey="Campaign.responsibleEmployee" type="${FIELD_TYPE_STRING}"
                                  width="25" fieldPosition="2"/>
            <titus:reportFieldTag name="status" resourceKey="Campaign.status" type="${FIELD_TYPE_STRING}" width="15"
                                  fieldPosition="3"
                                  conditionMethod="com.piramide.elwis.utils.ReportHelper.getPayMethodResource status [${preparation}] [${sent}] [${cancel}] [1] [2] [3]"/>

            <titus:reportFieldTag name="startDate" resourceKey="Campaign.dateCreation" type="${FIELD_TYPE_DATEINT}"
                                  patternKey="datePattern" width="10" fieldPosition="5"/>
            <titus:reportFieldTag name="endDate" resourceKey="Campaign.closeDate" type="${FIELD_TYPE_DATEINT}"
                                  patternKey="datePattern" width="10" fieldPosition="6"/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">

            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="formReset('campaignReport')">
                <fmt:message key="Common.clear"/></html:button>
        </div>

    </html:form>


