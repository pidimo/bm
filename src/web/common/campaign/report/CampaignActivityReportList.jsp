<%@ include file="/Includes.jsp" %>
<%
 pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:initSelectPopup/>
<calendar:initialize/>
<c:set var="percents" value="${app2:defaultProbabilities()}"/>
<c:set var="statusList" value="${app2:getCampaignActivityStatusList(pageContext.request)}"/>
<br>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="/Report/CampaignActivityList/Execute.do" focus="parameter(campaignId)" styleId="campaignActivityReport">
        <tr>
            <td class="title" colspan="4">
                <fmt:message key="Campaign.Report.CampaignActivityList"/>
            </td>
        </tr>
        <tr>
            <td width="15%" class="label"><fmt:message key="Campaign.mailing"/></td>
            <td class="contain" width="32%">
                <c:choose>
                    <c:when test="${not empty param.campaignId}">
                        <fanta:select property="parameter(campaignId)" listName="campaignList" tabIndex="1"
                                      labelProperty="campaignName" valueProperty="campaignId" styleClass="mediumSelect"
                                      module="/campaign" value="${param.campaignId}" firstEmpty="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="parameter(campaignId)" listName="campaignList" tabIndex="1"
                                      labelProperty="campaignName" valueProperty="campaignId" styleClass="mediumSelect"
                                      module="/campaign" firstEmpty="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </c:otherwise>
                </c:choose>
               </td>
            <td width="15%" class="label"><fmt:message key="CampaignActivity.cost"/></td>
            <td width="38%" class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(cost1)" styleClass="numberText" tabindex="6" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(cost2)" styleClass="numberText" tabindex="7" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
            </td>
        </tr>
        <tr>
        <td  class="label"><fmt:message key="Campaign.responsibleEmployee"/></TD>
            <td class="contain" >
                <fanta:select property="parameter(responsibleId)" listName="internalUserList"
                              labelProperty="name" valueProperty="id"
                              styleClass="mediumSelect" tabIndex="2" firstEmpty="true" module="/admin">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <td class="label"><fmt:message key="CampaignActivity.percent"/></TD>
            <td class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <html:text property="parameter(percent1)" styleClass="numberText" tabindex="8" styleId="percent1"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <html:text property="parameter(percent2)" styleId="percent2" styleClass="numberText" tabindex="9"/>
            </td>
        </tr>
        <tr>
        <td class="label">
        <fmt:message key="CampaignActivity.state"/>
        </td>
        <td class="contain" >
            <html:select property="parameter(state)" styleClass="mediumSelect" tabindex="3">
                <html:option value=""/>
                <html:options collection="statusList" property="value" labelProperty="label"/>
            </html:select>
        </td>
            <TD class="label" width="17%">
        <fmt:message key="Task.startDate"/>
    </td>
    <TD class="contain" >
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startDate1)" maxlength="10" tabindex="10" styleId="startDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(startDate2)" maxlength="10" tabindex="11" styleId="startDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
        </tr>
         <tr>
             <td  class="label"><fmt:message key="CampaignActivity.numberContact"/></TD>
            <td  class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:text property="parameter(numberContacts1)" styleClass="numberText" tabindex="4"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:text property="parameter(numberContacts2)"  styleClass="numberText" tabindex="5"/>
            </td>
            <td class="label"><fmt:message key="Campaign.closeDate"/></TD>
            <td class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(closeDate1)" maxlength="10" tabindex="12" styleId="sendDate1"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
                              &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(closeDate2)" maxlength="10" tabindex="13" styleId="sendDate2"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
            </td>
         </tr>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="campaignName" labelKey="Campaign" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="activityName" labelKey="CampaignActivity.title" />
                    <titus:reportGroupSortColumnTag name="responsibleName" labelKey="CampaignActivity.responsible" />
                    <titus:reportGroupSortColumnTag name="startDate" labelKey="Campaign.dateCreation" isDate="true"/>
                    <titus:reportGroupSortColumnTag name="closeDate" labelKey="Campaign.closeDate" isDate="true"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <c:set var="reportFormats" value="${campaignActivityReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${campaignActivityReportForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">

                <html:submit styleClass="button" tabindex="57"><fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="formReset('campaignActivityReport')">
                    <fmt:message key="Common.clear"/></html:button>
                &nbsp;
            </td>
        </tr>

        <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
        <c:set var="person"><fmt:message key="Contact.person"/></c:set>
        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="CampaignActivityReport" title="Campaign.Report.CampaignActivityList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="campaignName" resourceKey="Campaign" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="activityName" resourceKey="CampaignActivity.title" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="2" />
        <titus:reportFieldTag name="responsibleName" resourceKey="CampaignActivity.responsible" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="startDate" resourceKey="Campaign.dateCreation" type="${FIELD_TYPE_DATEINT}"
                              patternKey="datePattern" width="15" fieldPosition="4"/>
        <titus:reportFieldTag name="closeDate" resourceKey="Campaign.closeDate" type="${FIELD_TYPE_DATEINT}"
                              patternKey="datePattern" width="15" fieldPosition="5"/>
    </html:form>
</table>