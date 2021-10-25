<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initSelectPopupEven fields="user_key, user_name"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
<tr>
    <td class="title" colspan="4"><fmt:message key="Support.Report.CaseList"/></td>
</tr>
<fmt:message key="datePattern" var="datePattern"/>

<html:form action="/Report/CaseList/Execute.do" focus="parameter(openUserName)" styleId="supportStyle">
<TR>
    <TD width="13%" class="label"><fmt:message key="Common.openBy"/></TD>
    <TD class="contain" width="32%">
        <html:hidden property="parameter(openByUserId)" styleId="user_key"/>
        <app:text property="parameter(openUserName)"  tabindex="1" styleClass="mediumText" maxlength="40"
                  styleId="user_name"  readonly="true"/>
        <tags:selectPopup
                url="/scheduler/ImportUserList.do?other=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                name="searchUser"
                titleKey="Scheduler.grantAccess.searchUser"
                width="630"
                heigth="480"
                imgWidth="17"
                imgHeight="19" />
        <tags:clearSelectPopup keyFieldId="user_key"
                               nameFieldId="user_name"
                               titleKey="Common.clear"/>
    </td>
    <td width="15%" class="label"><fmt:message key="Common.openDate"/></TD>
    <td width="40%" class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(openDate1)" maxlength="10" tabindex="10" styleId="openDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(openDate2)" maxlength="10" tabindex="11" styleId="openDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Common.assignedTo"/></TD>
    <td class="contain">
        <html:hidden property="parameter(toUserId)" styleId="fieldViewUserId_id" />
        <app:text property="parameter(fromUserName)" tabindex="2" styleClass="mediumText" maxlength="40"
                  styleId="fieldViewUserName_id" readonly="true"/>
        <tags:selectPopup url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                          name="searchUser"
                          titleKey="Scheduler.grantAccess.searchUser"
                          width="630" heigth="480" imgWidth="17" imgHeight="19" />

        <tags:clearSelectPopup keyFieldId="fieldViewUserId_id"
                               nameFieldId="fieldViewUserName_id"
                               titleKey="Common.clear"/>
    </td>
    <td class="label">
        <fmt:message key="Common.expireDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(expireDate1)" maxlength="10" tabindex="12" styleId="expireDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(expireDate2)" maxlength="10" tabindex="13" styleId="expireDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Contact.title"/></TD>
    <TD class="contain">
        <html:hidden property="parameter(contactAddressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(contactName)" styleId="fieldAddressName_id" styleClass="mediumText" maxlength="40"
                  tabindex="3" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"/>
    </TD>
    <TD class="label">
        <fmt:message key="Common.closeDate"/>
    </td>
    <TD class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(closeDate1)" maxlength="10" tabindex="14" styleId="closeDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(closeDate2)" maxlength="10" tabindex="15" styleId="closeDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Product.title"/></TD>
    <TD class="contain">
        <html:hidden property="parameter(productId)" styleId="field_key"/>
        <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
        <html:hidden property="parameter(2)" styleId="field_unitId"/>
        <html:hidden property="parameter(3)" styleId="field_price"/>

        <app:text property="parameter(productName)" styleId="field_name" styleClass="mediumText" maxlength="40"
                  readonly="true" tabindex="4"/>
        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"/>
    </TD>

    <TD class="label"><fmt:message key="State.title"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(stateId)" listName="stateBaseList" labelProperty="name" tabIndex="16"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="1"/>
        </fanta:select>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Priority.title"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(priorityId)" listName="selectPriorityList" labelProperty="name" tabIndex="5"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SUPPORT"/>
        </fanta:select>
    </TD>
    <td class="label"><fmt:message key="WorkLevel.title"/></td>
    <td class="contain">
        <fanta:select property="parameter(workLevelId)" listName="workLevelList" labelProperty="name" tabIndex="17"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</TR>
<TR>
    <td class="label"><fmt:message key="CaseType.title"/></td>
    <td class="contain">
        <fanta:select property="parameter(caseTypeId)" listName="caseTypeList" labelProperty="name" tabIndex="6"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap.companyId}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="CaseSeverity.title"/></td>
    <td class="contain">
        <fanta:select property="parameter(severityId)" listName="caseSeverityList" labelProperty="name" tabIndex="18"
                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${onlyView}"
                      module="/catalogs">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</TR>
<TR>
    <td class="label"><fmt:message key="Common.totalHours"/></td>
    <td class="contain" colspan="3">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:text property="parameter(totalHours1)" styleClass="numberText" maxlength="40" tabindex="7"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:text property="parameter(totalHours2)" styleId="totalHours2" styleClass="numberText" maxlength="40"
                  tabindex="8"/>
    </td>
</TR>


<c:set var="reportFormats" value="${caseReportListForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${caseReportListForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="number" labelKey="Article.number"/>
            <titus:reportGroupSortColumnTag name="caseTitle" labelKey="Common.title"/>
            <titus:reportGroupSortColumnTag name="typeName" labelKey="CaseType.title"/>
            <titus:reportGroupSortColumnTag name="priorityName" labelKey="Priority.title"/>
            <titus:reportGroupSortColumnTag name="severityName" labelKey="CaseSeverity.title"/>
            <titus:reportGroupSortColumnTag name="stateName" labelKey="State.title"/>
            <titus:reportGroupSortColumnTag name="openByUser" labelKey="Common.openBy"/>
            <titus:reportGroupSortColumnTag name="toUser" labelKey="Common.assignedTo"/>
            <titus:reportGroupSortColumnTag name="contact" labelKey="Contact.title"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Product.title"/>
            <titus:reportGroupSortColumnTag name="openDate" labelKey="Common.openDate" isDate="true"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>


<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" tabindex="57"><fmt:message key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('supportStyle')">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>

</tr>
<titus:reportInitializeConstantsTag/>
<titus:reportTag id="caseReportList" title="Support.Report.CaseList" pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="number" resourceKey="Article.number" type="${FIELD_TYPE_STRING}" width="5"
                      fieldPosition="1"/>
<titus:reportFieldTag name="caseTitle" resourceKey="Common.title" type="${FIELD_TYPE_STRING}" width="11"
                      fieldPosition="2"/>
<titus:reportFieldTag name="typeName" resourceKey="CaseType.title" type="${FIELD_TYPE_STRING}" width="8"
                      fieldPosition="3"/>
<titus:reportFieldTag name="priorityName" resourceKey="Priority.title" type="${FIELD_TYPE_STRING}" width="8"
                      fieldPosition="4"/>
<titus:reportFieldTag name="severityName" resourceKey="CaseSeverity.title" type="${FIELD_TYPE_STRING}" width="8"
                      fieldPosition="5"/>
<titus:reportFieldTag name="stateName" resourceKey="State.title" type="${FIELD_TYPE_STRING}" width="8"
                      fieldPosition="6"/>
<titus:reportFieldTag name="openByUser" resourceKey="Common.openBy" type="${FIELD_TYPE_STRING}" width="12"
                      fieldPosition="7"/>
<titus:reportFieldTag name="toUser" resourceKey="Common.assignedTo" type="${FIELD_TYPE_STRING}" width="12"
                      fieldPosition="8"/>
<titus:reportFieldTag name="contact" resourceKey="Contact.title" type="${FIELD_TYPE_STRING}" width="12"
                      fieldPosition="9"/>
<titus:reportFieldTag name="productName" resourceKey="Product.title" type="${FIELD_TYPE_STRING}" width="9"
                      fieldPosition="10"/>
<titus:reportFieldTag name="openDate" resourceKey="Common.openDate" type="${FIELD_TYPE_DATEINT}" width="7"
                      patternKey="datePattern"
                      fieldPosition="11"/>
</html:form>
</table>
