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

<br>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="/Report/CampaignContactList/Execute.do" focus="parameter(campaignId)"
               styleId="campaignContactReport">
        <tr>
            <td height="20" class="title" colspan="4">
                <fmt:message key="Campaign.Report.CampaignContactList"/>
            </td>
        </tr>
        <tr>
            <td width="15%" class="label"><fmt:message key="Campaign.mailing"/></TD>
            <td class="contain" width="35%">
                <fanta:select property="parameter(campaignId)" listName="campaignList" tabIndex="1"
                              labelProperty="campaignName" valueProperty="campaignId" styleClass="mediumSelect"
                              module="/campaign" firstEmpty="true" onChange="goSubmit()">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <td width="15%" class="label"><fmt:message key="Common.for"/></TD>
            <td class="contain" width="35%">
                <html:select property="parameter(addressType)" styleClass="mediumSelect" tabindex="4">
                    <html:option value=""/>
                    <html:options collection="addressTypeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td width="15%" class="label"><fmt:message key="Campaign.activity.activity"/></td>
            <td class="contain" width="35%">
                <fanta:select property="parameter(activityId)" listName="campaignActivityList" tabIndex="2"
                              labelProperty="title" valueProperty="activityId" styleClass="mediumSelect"
                              module="/campaign" firstEmpty="true">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="campaignId"
                                     value="${not empty reportGeneratorForm.params.campaignId?reportGeneratorForm.params.campaignId:0}"/>
                </fanta:select>
            </td>
            <td class="label"><fmt:message key="Common.active"/></td>
            <td class="contain">
                <html:select property="parameter(active)" styleClass="mediumSelect" tabindex="5">
                    <html:options collection="activeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="CampaignActivity.responsible"/>
            </td>
            <td class="contain" colspan="3">
                <fanta:select property="parameter(userId)" listName="internalUserList"
                              labelProperty="name" valueProperty="id"
                              styleClass="mediumSelect"
                              tabIndex="3" firstEmpty="true"
                              module="/admin">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>

        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="campaignName" labelKey="Campaign" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="activityName" labelKey="CampaignActivity.title" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="contactName" labelKey="Campaign.company" />
                    <titus:reportGroupSortColumnTag name="contactPersonName" labelKey="Campaign.contactPerson" />
                    <titus:reportGroupSortColumnTag name="responsibleName" labelKey="CampaignActivity.responsible" />
                </titus:reportGroupSortTag>
            </td>
        </tr>

        <c:set var="reportFormats" value="${reportGeneratorForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${reportGeneratorForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                        key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="formReset('campaignContactReport')">
                    <fmt:message key="Common.clear"/></html:button>
                &nbsp;
            </td>
        </tr>
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
    </html:form>
</table>