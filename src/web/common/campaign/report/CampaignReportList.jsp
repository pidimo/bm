<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getStatusList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="/Report/CampaignList/Execute.do" focus="parameter(employeeId)" styleId="campaignReport">
        <tr>
            <td height="20" class="title" colspan="4">
                <fmt:message key="Campaign.Report.CampaignList"/>
            </td>
        </tr>
        <tr>
            <td width="15%" class="label"><fmt:message key="Campaign.responsibleEmployee"/></td>
            <td width="38%" class="contain">
                <fanta:select property="parameter(employeeId)" listName="employeeBaseList" tabIndex="1"
                              labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                              module="/contacts" firstEmpty="true">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <td width="15%" class="label"><fmt:message key="Campaign.budgetedCost"/></TD>
            <td width="32%" class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(budgetCost1)" styleClass="numberText" tabindex="8" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(budgetCost2)" styleId="price2" styleClass="numberText" tabindex="9"
                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Campaign.status"/></TD>
            <td class="contain">
                <html:select property="parameter(status)" styleClass="mediumSelect" tabindex="2">
                    <html:option value=""/>
                    <html:options collection="statusList" property="value" labelProperty="label"/>
                </html:select>
            </td>
            <td  class="label"><fmt:message key="Campaign.profits"/></TD>
            <td  class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(awaitedUtility1)" styleClass="numberText" tabindex="10" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(awaitedUtility2)" styleId="price2" styleClass="numberText" tabindex="11"
                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Campaign.type"/>
            </td>
            <td class="contain">
                <fanta:select property="parameter(typeId)" listName="campaignTypeList"
                              valueProperty="id" labelProperty="title" firstEmpty="true"
                              styleClass="mediumSelect" module="/catalogs" tabIndex="3" readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <td class="label"><fmt:message key="Campaign.realCost"/></TD>
            <td class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(realCost1)" styleClass="numberText" tabindex="12" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(realCost2)" styleId="price2" styleClass="numberText" tabindex="13"
                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Campaign.dateCreation"/></TD>
            <td class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(startDate1)" maxlength="10" tabindex="4" styleId="startDate1"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
                              &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(startDate2)" maxlength="10" tabindex="5" styleId="startDate2"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
            </td>
        <td  class="label"><fmt:message key="Campaign.totalContacts"/></td>
            <td class="contain">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:text property="parameter(numberContacts1)" styleClass="numberText" tabindex="14"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:text property="parameter(numberContacts2)"  styleClass="numberText" tabindex="15"/>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Campaign.closeDate"/></TD>
            <td class="contain" colspan="3">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(endDate1)" maxlength="10" tabindex="6" styleId="endDate1"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
                              &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endDate2)" maxlength="10" tabindex="7" styleId="endDate2"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
            </td>
        </tr>

        <c:set var="reportFormats" value="${campaignReportListForm.reportFormats}" scope="request"/>
                <c:set var="pageSizes" value="${campaignReportListForm.pageSizes}" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="campaignName" labelKey="Campaign.mailing" />
                    <titus:reportGroupSortColumnTag name="employeeName" labelKey="Campaign.responsibleEmployee" />
                    <titus:reportGroupSortColumnTag name="startDate" labelKey="Campaign.dateCreation" isDate="true"/>
                    <titus:reportGroupSortColumnTag name="endDate" labelKey="Campaign.closeDate" isDate="true"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>
        
        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">

                <html:submit styleClass="button" tabindex="57"><fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('campaignReport')">
                    <fmt:message key="Common.clear"/></html:button>
                &nbsp;
            </td>
        </tr>

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
</html:form>
</table>

