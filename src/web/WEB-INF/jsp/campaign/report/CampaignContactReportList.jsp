<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script>
    function goSubmit() {
        document.forms[0].submit();
    }
</script>

    <html:form action="/Report/CampaignContactList/Execute.do" focus="parameter(campaignId)" styleClass="form-horizontal"
               styleId="campaignContactReport">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Campaign.Report.CampaignContactList"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="">
                            <fmt:message key="Campaign.mailing"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(campaignId)"
                                          listName="campaignList"
                                          tabIndex="1"
                                          labelProperty="campaignName"
                                          valueProperty="campaignId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          module="/campaign"
                                          firstEmpty="true"
                                          onChange="goSubmit()">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="">
                            <fmt:message key="Common.for"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(addressType)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="2">
                                <html:option value=""/>
                                <html:options collection="addressTypeList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign.activity.activity"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(activityId)"
                                          listName="campaignActivityList"
                                          tabIndex="3"
                                          labelProperty="title"
                                          valueProperty="activityId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          module="/campaign"
                                          firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="campaignId"
                                                 value="${not empty reportGeneratorForm.params.campaignId?reportGeneratorForm.params.campaignId:0}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.active"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(active)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="4">
                                <html:options collection="activeList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="CampaignActivity.responsible"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(userId)"
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
                </div>
            </fieldset>

            <titus:reportGroupSortTag width="100%" mode="bootstrap" tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="campaignName" labelKey="Campaign" isDefault="true" defaultOrder="true"
                                                isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="activityName" labelKey="CampaignActivity.title" isDefault="true"
                                                defaultOrder="true" isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="contactName" labelKey="Campaign.company"/>
                <titus:reportGroupSortColumnTag name="contactPersonName" labelKey="Campaign.contactPerson"/>
                <titus:reportGroupSortColumnTag name="responsibleName" labelKey="CampaignActivity.responsible"/>
            </titus:reportGroupSortTag>


            <c:set var="reportFormats" value="${reportGeneratorForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${reportGeneratorForm.pageSizes}" scope="request"/>

            <tags:bootstrapReportOptionsTag/>

            <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
            <c:set var="person"><fmt:message key="Contact.person"/></c:set>

            <titus:reportInitializeConstantsTag/>
            <titus:reportTag id="CampaignContactReport" title="Campaign.Report.RecipientList"
                             locale="${sessionScope.user.valueMap['locale']}"
                             pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <titus:reportFieldTag name="campaignName" resourceKey="Campaign" type="${FIELD_TYPE_STRING}" width="15"
                                  fieldPosition="1" />
            <titus:reportFieldTag name="activityName" resourceKey="CampaignActivity.title" type="${FIELD_TYPE_STRING}"
                                  width="15"
                                  fieldPosition="2" />
            <titus:reportFieldTag name="contactName" resourceKey="Campaign.company" type="${FIELD_TYPE_STRING}" width="15"
                                  fieldPosition="3"/>
            <titus:reportFieldTag name="contactPersonName" resourceKey="Campaign.contactPerson" type="${FIELD_TYPE_STRING}"
                                  width="15"
                                  fieldPosition="4"/>
            <titus:reportFieldTag name="responsibleName" resourceKey="CampaignActivity.responsible"
                                  type="${FIELD_TYPE_STRING}" width="10"
                                  fieldPosition="5"/>
            <titus:reportFieldTag name="contactId" resourceKey="Common.phone" type="${FIELD_TYPE_STRING}" width="15"
                                  conditionMethod="com.piramide.elwis.utils.ReportHelper.getTelecomPhoneContacts contactId contactPersonId"
                                  fieldPosition="6"/>
            <titus:reportFieldTag name="campaignId" resourceKey="Common.email" type="${FIELD_TYPE_STRING}" width="15"
                                  conditionMethod="com.piramide.elwis.utils.ReportHelper.getTelecomEmailContacts contactId contactPersonId"
                                  fieldPosition="7"/>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)"
                         tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="formReset('campaignContactReport')">
                <fmt:message key="Common.clear"/></html:button>
        </div>

    </html:form>