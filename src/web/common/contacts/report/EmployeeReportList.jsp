<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>
<tags:initSelectPopup/>
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

<table border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Contact.Report.EmployeeList"/></td>
</tr>

<html:form action="/Report/EmployeeList/Execute.do" focus="parameter(healthFundName)" styleId="employeeReport">
<tr>
    <td class="label" width="15%"><fmt:message key="Contact.Organization.Employee.healthFund"/></td>
    <td class="contain" width="32%" nowrap>

        <html:hidden property="parameter(healthFundId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(healthFundName)" styleClass="mediumText" maxlength="40" tabindex="1"
                  styleId="fieldAddressName_id" disabled="true"/>
        <tags:selectPopup url="/contacts/OrganizationSearch.do" name="organizationSearchList" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"/>
    </td>
    <td class="label" width="15%"><fmt:message key="Contact.Organization.Employee.hireDate"/></td>
    <td class="contain" width="38%">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(hireDate1)" maxlength="10" tabindex="6" styleId="hireDate1"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(hireDate2)" maxlength="10" tabindex="7" styleId="hireDate2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>

<TR>
    <td class="label"><fmt:message key="Contact.Organization.Employee.department"/></td>
    <td class="contain">
        <!-- Collection departments-->
        <fanta:select property="parameter(departmentId)" listName="departmentBaseList" firstEmpty="true"
                      labelProperty="name" valueProperty="departmentId" styleClass="mediumSelect" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="Contact.Organization.Employee.endDate"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(endDate1)" maxlength="10" tabindex="8" styleId="startRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endDate2)" maxlength="10" tabindex="9" styleId="endRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<TR>
    <td class="label"><fmt:message key="Contact.Organization.Employee.office"/></td>
    <td class="contain">
        <!-- Collection offices-->
        <fanta:select property="parameter(officeId)" listName="officeBaseList" firstEmpty="true" labelProperty="name"
                      valueProperty="officeId" styleClass="mediumSelect" tabIndex="3">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <TD class="topLabel"><fmt:message key="Contact.Organization.Employee.costPosition"/></TD>
    <TD class="containTop">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(cost1)" styleClass="numberText" tabindex="10" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(cost2)" styleId="cost2" styleClass="numberText" tabindex="11"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </TD>
</tr>
<tr>
    <td class="label"><fmt:message key="Contact.Organization.Employee.function"/></td>
    <td class="contain">
        <app:text property="parameter(function)" styleClass="mediumText" maxlength="40" tabindex="4"/>
    </td>
    <td class="label"><fmt:message key="Contact.Organization.Employee.costHour"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(costHour1)" styleClass="numberText" tabindex="12" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(costHour2)" styleId="costHour2" styleClass="numberText" tabindex="13"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Contact.Organization.Employee.costCenter"/></td>
    <td class="contain">
        <!-- Collection costcenters-->
        <fanta:select property="parameter(costCenterId)" listName="costCenterBaseList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" styleClass="mediumSelect" module="/catalogs"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>

    </td>
    <td class="label"><fmt:message key="Contact.Organization.Employee.hourlyRate"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(hourlyRate1)" styleClass="numberText" tabindex="14" maxlength="19"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(hourlyRate2)" styleId="costHour2" styleClass="numberText" tabindex="15"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</tr>

<c:set var="reportFormats" value="${employeeReportListForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${employeeReportListForm.pageSizes}" scope="request"/>

<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="employeeName" labelKey="Employee.name"/>
            <titus:reportGroupSortColumnTag name="departmentName" labelKey="Employee.DepartmentName"/>
            <titus:reportGroupSortColumnTag name="officeName" labelKey="Employee.officeName"/>
            <titus:reportGroupSortColumnTag name="hireDate" labelKey="Contact.Organization.Employee.hireDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="endDate" labelKey="Contact.Organization.Employee.endDate" isDate="true"/>
        </titus:reportGroupSortTag>
    </td>
</tr>

<tags:reportOptionsTag/>

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('employeeReport')">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>
</tr>
<titus:reportInitializeConstantsTag/>
<titus:reportTag id="employeeReportList" title="Contact.Report.EmployeeList"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="employeeName" resourceKey="Employee.name" type="${FIELD_TYPE_STRING}" width="30"
                      fieldPosition="1"/>
<titus:reportFieldTag name="departmentName" resourceKey="Employee.DepartmentName" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="2"/>
<titus:reportFieldTag name="officeName" resourceKey="Employee.officeName" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="3"/>
<titus:reportFieldTag name="hireDate" resourceKey="Contact.Organization.Employee.hireDate" type="${FIELD_TYPE_DATEINT}"
                      patternKey="datePattern" width="15" fieldPosition="4"/>
<titus:reportFieldTag name="endDate" resourceKey="Contact.Organization.Employee.endDate" type="${FIELD_TYPE_DATEINT}"
                      patternKey="datePattern" width="15" fieldPosition="5"/>
</html:form>
</table>
