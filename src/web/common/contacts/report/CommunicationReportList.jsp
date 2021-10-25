<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<%
    pageContext.setAttribute("inOutCommunicationList", JSPHelper.getInOutCommunicationList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>

    function goSubmit() {
        document.forms[0].submit();
    }

    function clearValues(t) {

        if (document.getElementById('fieldAddressId_id').value != "" && t == '1') {
            document.getElementById('fieldAddressId_id').value = "";
            document.getElementById('fieldAddressName_id').value = "";
            document.getElementById('fieldProcessId_id').value = "";
            document.getElementById('fieldProcessName_id').value = "";
            goSubmit();
        }
        if (document.getElementById('fieldProcessId_id').value != "" && t == '2') {
            document.getElementById('fieldAddressId_id').value = "";
            document.getElementById('fieldAddressName_id').value = "";
            document.getElementById('fieldProcessId_id').value = "";
            document.getElementById('fieldProcessName_id').value = "";
            goSubmit();
        }
    }

</script>

<table border="0" cellpadding="0" cellspacing="0" width="97%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Contact.Report.CommunicationList"/></td>
</tr>

<html:form action="/Report/CommunicationList/Execute.do" focus="parameter(address)" styleId="communicationReportList">
<tr>
    <TD class="label" width="15%"><fmt:message key="Contact"/></TD>
    <TD class="contain" width="35%">

        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                  tabindex="1" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                          hide="false" submitOnSelect="true"/>
        <tags:clearSelectPopup onclick="clearValues(1)" keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"/>
            <%--<html:img src="${baselayout}/img/clear.gif" align="center" titleKey="Common.clear" border="0" onclick="clearValues(1)"/>--%>
    </TD>

    <TD class="label" width="15%"><fmt:message key="Communication.type"/></TD>
    <TD class="contain" width="35%">
        <c:set var="communicationTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
        <html:select property="parameter(type)" styleClass="select"
                     tabindex="5">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="communicationTypes" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</TR>
<TR>
    <td class="label"><fmt:message key="ContactPerson"/></TD>
    <td class="contain">
        <fanta:select property="parameter(contactPersonId)" styleId="contactPersonId" listName="contactPersonList"
                      firstEmpty="true" labelProperty="contactPersonName"
                      valueProperty="contactPersonId" module="/contacts" styleClass="middleSelect" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty communicationReportListForm.params.addressId?communicationReportListForm.params.addressId:0}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="Document.inout"/></td>
    <td class="contain">
        <html:select property="parameter(in_Out)" tabindex="6" styleClass="select">
            <html:options collection="inOutCommunicationList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Document.salesAsociated"/></TD>
    <td class="contain">
        <html:hidden property="parameter(processId)" styleId="fieldProcessId_id"/>
        <html:hidden property="parameter(percentId)" styleId="percent_id"/>
        <app:text property="parameter(processName)" styleId="fieldProcessName_id" styleClass="middleText" maxlength="30"
                  readonly="true" tabindex="3"/>
        <tags:selectPopup
                url="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${communicationReportListForm.params.addressId}"
                name="salesProcessList"
                titleKey="Common.search" width="900" heigth="480" scrollbars="false" submitOnSelect="true"/>
        <tags:clearSelectPopup onclick="clearValues(2)" keyFieldId="fieldProcessId_id" nameFieldId="fieldProcessName_id"
                               titleKey="Common.clear"/>
    </td>
    <td class="label"><fmt:message key="Document.date"/></td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startRange)" maxlength="10" tabindex="7" styleId="startRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endRange)" maxlength="10" tabindex="8" styleId="endRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</TR>
<TR>
    <td class="label"><fmt:message key="Contact.employee"/></td>
    <td class="contain" colspan="3">
        <fanta:select property="parameter(employeeId)" listName="employeeBaseList" tabIndex="4"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="middleSelect"
                      module="/contacts" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</tr>

<c:set var="reportFormats" value="${communicationReportListForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${communicationReportListForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="contactName" labelKey="Contact" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
            <titus:reportGroupSortColumnTag name="note" labelKey="Document.subject"/>
            <titus:reportGroupSortColumnTag name="contactPersonName" labelKey="ContactPerson"/>
            <titus:reportGroupSortColumnTag name="employeeName" labelKey="SalesProcess.employee"/>
            <titus:reportGroupSortColumnTag name="processName" labelKey="Document.salesAsociated"/>
            <titus:reportGroupSortColumnTag name="date" labelKey="Document.date" isDate="true"/>
            <titus:reportGroupSortColumnTag name="inOut" labelKey="Document.inout"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag />

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('communicationReportList')">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>
</tr>
<c:set var="phone"><fmt:message key="Communication.type.phone"/></c:set>
<c:set var="meeting"><fmt:message key="Communication.type.meeting"/></c:set>
<c:set var="fax"><fmt:message key="Communication.type.fax"/></c:set>
<c:set var="letter"><fmt:message key="Communication.type.letter"/></c:set>
<c:set var="mail"><fmt:message key="Communication.type.email"/></c:set>
<c:set var="other"><fmt:message key="Communication.type.other"/></c:set>
<c:set var="document"><fmt:message key="Communication.type.document"/></c:set>
<c:set var="webDocument"><fmt:message key="Communication.type.webDocument"/></c:set>

<c:set var="in"><fmt:message key="Document.in"/></c:set>
<c:set var="out"><fmt:message key="Document.out"/></c:set>

<titus:reportInitializeConstantsTag/>

<titus:reportTag id="communicationReportList" title="Contact.Report.CommunicationList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
<titus:reportFieldTag name="contactName" resourceKey="Contact" type="${FIELD_TYPE_STRING}" width="15"
                      fieldPosition="1"/>
<titus:reportFieldTag name="note" resourceKey="Document.subject" type="${FIELD_TYPE_STRING}" width="16"
                      fieldPosition="2"/>
<titus:reportFieldTag name="contactPersonName" resourceKey="ContactPerson" type="${FIELD_TYPE_STRING}" width="15"
                      fieldPosition="3"/>
<titus:reportFieldTag name="employeeName" resourceKey="SalesProcess.employee" type="${FIELD_TYPE_STRING}" width="15"
                      fieldPosition="4"/>
<titus:reportFieldTag name="processName" resourceKey="Document.salesAsociated" type="${FIELD_TYPE_STRING}" width="10"
                      fieldPosition="5"/>
<titus:reportFieldTag name="date" resourceKey="Document.date" type="${FIELD_TYPE_DATEINT}" patternKey="datePattern"
                      width="10"
                      fieldPosition="6"/>
<titus:reportFieldTag name="type" resourceKey="Document.mediaType" type="${FIELD_TYPE_STRING}" width="7"
                      conditionMethod="com.piramide.elwis.utils.ReportHelper.getMediaTypeResource type [${phone}] [${meeting}] [${fax}] [${letter}] [${other}] [${mail}] [${document}] [${webDocument}] [0] [1] [2] [3] [4] [5] [6] [7]"
                      fieldPosition="7"/>
<titus:reportFieldTag name="inOut" resourceKey="Document.inout" type="${FIELD_TYPE_STRING}" width="7"
                      conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource inOut [${in}] [${out}] [1] [0]"
                      fieldPosition="8"/>
</html:form>
</table>
