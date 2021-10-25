<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveOrNoList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>


<script>
    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }

    function goSubmit() {
        document.forms[0].submit();
    }
    function clearValues() {
        if (document.getElementById('fieldProcessName_id').value != "") {
            document.getElementById('fieldProcessName_id').value = "";
            document.getElementById('fieldProcessId_id').value = "";
            document.getElementById('contactPerson').value = "";
            document.getElementById('fieldAddressId_id').value = "";
            goSubmit();
        }
    }
</script>
<br>
<table width="97%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="SalesProcess.Report.ProcessActionList"/>
    </td>
</tr>
<html:form action="/Report/ProcessActionList/Execute.do" focus="parameter(processName)" styleId="processStyle">
    <tr>
        <td class="label" width="15%">
            <fmt:message key="Document.salesAsociated"/>
        </TD>
        <td class="contain" width="35%">
            <html:hidden property="parameter(processId)" styleId="fieldProcessId_id"/>
            <html:hidden property="parameter(percentId)" styleId="percent_id"/>
            <html:hidden property="parameter(id)" styleId="fieldAddressId_id"/>
            <html:hidden property="parameter(name)" styleId="fieldAddressName_id"/>

            <app:text property="parameter(processName)"
                      styleId="fieldProcessName_id"
                      styleClass="middleText"
                      maxlength="30"
                      readonly="true"
                      tabindex="1"/>
            <tags:selectPopup url="/sales/SalesProcess/SearchSalesProcess.do"
                              name="searchSalesProcess"
                              titleKey="Common.search"
                              width="900"
                              heigth="480"
                              scrollbars="false"
                              submitOnSelect="true"
                              tabindex="1"/>
            <tags:clearSelectPopup onclick="clearValues()"
                                   keyFieldId="fieldProcessId_id"
                                   nameFieldId="fieldProcessName_id"
                                   titleKey="Common.clear"
                                   tabindex="1"/>

            <html:hidden property="parameter(addressProcessId)"/>
        </td>
        <TD class="label" width="15%">
            <fmt:message key="Document.date"/>
        </td>
        <TD class="contain" width="35%">
            <fmt:message key="datePattern" var="datePattern"/>
            <fmt:message key="Common.from"/>
            &nbsp;
            <app:dateText property="parameter(startSendDate)" maxlength="10" tabindex="4" styleId="startSendDate"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
            &nbsp;
            <fmt:message key="Common.to"/>
            &nbsp;
            <app:dateText property="parameter(endSendDate)" maxlength="10" tabindex="5" styleId="endSendDate"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        </td>
    </tr>
    <tr>
        <TD class="label">
            <fmt:message key="Appointment.contactPerson"/>
        </TD>
        <TD class="contain">
            <fanta:select property="parameter(contactPersonId)"
                          listName="contactPersonSimpleList"
                          firstEmpty="true"
                          styleId="contactPerson"
                          labelProperty="contactPersonName"
                          valueProperty="contactPersonId"
                          styleClass="middleSelect"
                          module="/contacts"
                          tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty processActionReportForm.params.id?processActionReportForm.params.id:0}"/>
            </fanta:select>
        </TD>
        <TD class="label">
            <fmt:message key="SalesProcessAction.actionType"/>
        </TD>
        <TD class="contain">
            <fanta:select property="parameter(actionTypeId)"
                          listName="actionTypeBaseList"
                          labelProperty="name"
                          valueProperty="id"
                          styleClass="middleSelect"
                          module="/sales"
                          firstEmpty="true"
                          tabIndex="6">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </TR>
    <tr>
        <TD class="topLabel"><fmt:message key="SalesProcess.employee"/></TD>
        <TD class="containTop">
            <fanta:select property="parameter(employeeId)"
                          listName="employeeBaseList"
                          firstEmpty="true"
                          labelProperty="employeeName"
                          valueProperty="employeeId"
                          styleClass="middleSelect"
                          module="/contacts"
                          tabIndex="3">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
        <td class="topLabel"><fmt:message key="Common.active"/></TD>
        <td class="containTop">
            <html:select property="parameter(isActive)" styleClass="shortSelect" tabindex="7">
                <html:options collection="activeList" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td colspan="4">
            <titus:reportGroupSortTag width="100%">
                <titus:reportGroupSortColumnTag name="processName" labelKey="SalesProcess.name" isDefault="true"
                                                defaultOrder="true" isDefaultGrouping="true"/>
                <titus:reportGroupSortColumnTag name="note" labelKey="SalesProcessAction.title"/>
                <titus:reportGroupSortColumnTag name="actionTypeName" labelKey="SalesProcessAction.actionType"/>
                <titus:reportGroupSortColumnTag name="employeeName" labelKey="Document.employee"/>
                <titus:reportGroupSortColumnTag name="date" labelKey="Document.date" isDate="true"/>
                <titus:reportGroupSortColumnTag name="value" labelKey="SalesProcess.value"/>
            </titus:reportGroupSortTag>
        </td>
    </tr>
    <c:set var="reportFormats" value="${processActionReportForm.reportFormats}" scope="request"/>
    <c:set var="pageSizes" value="${processActionReportForm.pageSizes}" scope="request"/>
    <tags:reportOptionsTag/>

    <tr>
        <td colspan="4" class="button">
            <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/></html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('processStyle')">
                <fmt:message
                        key="Common.clear"/></html:button>
        </td>
    </tr>

    <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
    <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="actionReportList" title="SalesProcess.Report.ProcessActionList"
                     locale="${sessionScope.user.valueMap['locale']}" pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <titus:reportFieldTag name="processName" resourceKey="SalesProcess.processName" type="${FIELD_TYPE_STRING}"
                          width="15"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="note" resourceKey="SalesProcessAction.title" type="${FIELD_TYPE_STRING}" width="15"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="actionTypeName" resourceKey="SalesProcessAction.actionType" type="${FIELD_TYPE_STRING}"
                          width="15" fieldPosition="3"/>
    <titus:reportFieldTag name="employeeName" resourceKey="Document.employee" type="${FIELD_TYPE_STRING}" width="25"
                          fieldPosition="4"/>
    <titus:reportFieldTag name="date" resourceKey="Document.date" type="${FIELD_TYPE_DATEINT}" patternKey="datePattern"
                          width="10" fieldPosition="5"/>
    <titus:reportFieldTag name="value" resourceKey="SalesProcess.value" type="${FIELD_TYPE_DECIMALNUMBER}"
                          patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="10"
                          fieldPosition="6"/>
    <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                          fieldPosition="7"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"/>
    </table>
</html:form>