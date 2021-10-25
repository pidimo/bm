<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:initSelectPopup/>
<tags:initBootstrapDatepicker/>

<c:set var="percents" value="${app2:defaultProbabilities()}"/>
<c:set var="statusList" value="${app2:getCampaignActivityStatusList(pageContext.request)}"/>

<html:form action="/Report/CampaignActivityList/Execute.do" focus="parameter(campaignId)" styleClass="form-horizontal"
           styleId="campaignActivityReport">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Campaign.Report.CampaignActivityList"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.mailing"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:choose>
                            <c:when test="${not empty param.campaignId}">
                                <fanta:select property="parameter(campaignId)"
                                              listName="campaignList"
                                              tabIndex="1"
                                              labelProperty="campaignName"
                                              valueProperty="campaignId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              module="/campaign"
                                              value="${param.campaignId}"
                                              firstEmpty="true">
                                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <fanta:select property="parameter(campaignId)"
                                              listName="campaignList"
                                              tabIndex="2"
                                              labelProperty="campaignName"
                                              valueProperty="campaignId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              module="/campaign"
                                              firstEmpty="true">
                                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CampaignActivity.cost"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(cost1)"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="3" maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(cost2)"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="4" maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.responsibleEmployee"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(responsibleId)"
                                      listName="internalUserList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="5"
                                      firstEmpty="true"
                                      module="/admin">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CampaignActivity.percent"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <html:text property="parameter(percent1)"
                                           styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                           tabindex="6" styleId="percent1"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <html:text property="parameter(percent2)" styleId="percent2"
                                           styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                           tabindex="7"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CampaignActivity.state"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(state)" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     tabindex="8">
                            <html:option value=""/>
                            <html:options collection="statusList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.startDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <fmt:message key="datePattern" var="datePattern"/>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">

                                    <app:dateText property="parameter(startDate1)"
                                                  maxlength="10"
                                                  tabindex="9"
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
                                                  tabindex="10"
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
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CampaignActivity.numberContact"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:text property="parameter(numberContacts1)"
                                          styleClass="numberText ${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                          tabindex="11"/>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:text property="parameter(numberContacts2)"
                                          styleClass="numberText ${app2:getFormInputClasses()} numberText wrapperButton numberInputWidth"
                                          tabindex="12"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.closeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(closeDate1)"
                                                  maxlength="10"
                                                  tabindex="13"
                                                  styleId="sendDate1"
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
                                    <app:dateText property="parameter(closeDate2)"
                                                  maxlength="10"
                                                  tabindex="14"
                                                  styleId="sendDate2"
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


    <titus:reportGroupSortTag width="100%" mode="bootstrap" tableStyleClass="${app2:getTableClasesIntoForm()}">
        <titus:reportGroupSortColumnTag name="campaignName" labelKey="Campaign" isDefault="true" defaultOrder="true"
                                        isDefaultGrouping="true"/>
        <titus:reportGroupSortColumnTag name="activityName" labelKey="CampaignActivity.title"/>
        <titus:reportGroupSortColumnTag name="responsibleName" labelKey="CampaignActivity.responsible"/>
        <titus:reportGroupSortColumnTag name="startDate" labelKey="Campaign.dateCreation" isDate="true"/>
        <titus:reportGroupSortColumnTag name="closeDate" labelKey="Campaign.closeDate" isDate="true"/>
    </titus:reportGroupSortTag>
    <c:set var="reportFormats" value="${campaignActivityReportForm.reportFormats}" scope="request"/>
    <c:set var="pageSizes" value="${campaignActivityReportForm.pageSizes}" scope="request"/>

    <tags:bootstrapReportOptionsTag/>



    <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
    <c:set var="person"><fmt:message key="Contact.person"/></c:set>
    <titus:reportInitializeConstantsTag/>

    <titus:reportTag id="CampaignActivityReport" title="Campaign.Report.CampaignActivityList"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <titus:reportFieldTag name="campaignName" resourceKey="Campaign" type="${FIELD_TYPE_STRING}" width="20"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="activityName" resourceKey="CampaignActivity.title" type="${FIELD_TYPE_STRING}"
                          width="25"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="responsibleName" resourceKey="CampaignActivity.responsible" type="${FIELD_TYPE_STRING}"
                          width="25"
                          fieldPosition="3"/>
    <titus:reportFieldTag name="startDate" resourceKey="Campaign.dateCreation" type="${FIELD_TYPE_DATEINT}"
                          patternKey="datePattern" width="15" fieldPosition="4"/>
    <titus:reportFieldTag name="closeDate" resourceKey="Campaign.closeDate" type="${FIELD_TYPE_DATEINT}"
                          patternKey="datePattern" width="15" fieldPosition="5"/>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">

        <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>

        <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                     onclick="formReset('campaignActivityReport')">
            <fmt:message key="Common.clear"/></html:button>
    </div>
</html:form>
