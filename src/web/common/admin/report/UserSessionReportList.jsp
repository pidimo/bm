<%@ include file="/Includes.jsp" %>

<tags:initSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<calendar:initialize/>


<app2:jScriptUrl url="/admin/Report/UserSessionList.do?module=admin" var="locationJsUrl" addModuleParams="false"/>


<script>
    function jump() {
        window.location = ${locationJsUrl};
    }
</script>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="95%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Admin.Report.UserSessionList"/></td>
    </tr>


<c:set var="myTabNumber" value="0"/>
<html:form action="/Report/UserSessionList/Execute.do" focus="parameter(long@initLastLoginDate_1)"
           styleId="userSessionForm">
<tr>
    <td class="topLabel" width="15%"><fmt:message key="User.startConection"/></td>
    <td class="contain" width="35%">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:message key="Common.from"/>&nbsp;
        <app:dateText property="parameter(long@initLastLoginDate_1)" maxlength="10" tabindex="1"
                      styleId="initLastLoginDate_1" calendarPicker="true" datePatternKey="${datePattern}"
                      styleClass="dateText" convert="true"/>
        &nbsp;<fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(long@endLastLoginDate_1)" maxlength="10" tabindex="2"
                      styleId="endLastLoginDate_1" calendarPicker="true" datePatternKey="${datePattern}"
                      styleClass="dateText" convert="true"/>
    </td>


    <td class="label" width="15%"><fmt:message key="User.lastConection"/></td>
    <td class="contain" width="35%">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:message key="Common.from"/>&nbsp;
        <app:dateText property="parameter(long@initLastActionDate_2)" maxlength="10" tabindex="5"
                      styleId="initLastActionDate_2" calendarPicker="true" datePatternKey="${datePattern}"
                      styleClass="dateText" convert="true"/>

        &nbsp;<fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(long@endLastActionDate_2)" maxlength="10" tabindex="6"
                      styleId="endLastActionDate_2" calendarPicker="true" datePatternKey="${datePattern}"
                      styleClass="dateText" convert="true"/>
    </td>
</tr>

<tr>

    <td class="topLabel"><fmt:message key="User.endConection"/></td>
    <td class="contain">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:message key="Common.from"/>&nbsp
        <app:dateText property="parameter(long@initLogOffDate_3)" maxlength="10" tabindex="3"
                      styleId="initLogOffDate_3" calendarPicker="true" datePatternKey="${datePattern}"
                      styleClass="dateText" convert="true"/>

        &nbsp;<fmt:message key="Common.to"/>&nbsp;
        <app:dateText property="parameter(long@endLogOffDate_3)" maxlength="10" tabindex="4"
                      styleId="endLogOffDate_3" calendarPicker="true" datePatternKey="${datePattern}"
                      styleClass="dateText" convert="true"/>
    </td>

    <c:choose>
        <c:when test="${sessionScope.user.valueMap['isDefaultCompany'] == true}">
            <td class="label"><fmt:message key="Company"/></td>
            <td class="contain">

                <html:hidden property="parameter(reportCompanyId)" styleId="fieldReportCompany_id"/>
                <app:text property="parameter(reportCompany)" styleId="fieldReportCompanyName_id"
                          styleClass="middleText" maxlength="40"
                          tabindex="1" readonly="true"/>
                <tags:selectPopup url="/admin/Report/SearchCompany.do" name="searchCompany" titleKey="Common.search"
                                  hide="false" submitOnSelect="false"/>
                <tags:clearSelectPopup  keyFieldId="fieldReportCompany_id"
                                       nameFieldId="fieldReportCompanyName_id" titleKey="Common.clear"/>

            </td>
        </c:when>
        <c:otherwise>
            <td class="contain" colspan="2">
                <html:hidden property="parameter(reportCompanyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                &nbsp;
            </td>
        </c:otherwise>
    </c:choose>

</tr>

<c:set var="reportFormats" value="${userSessionReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${userSessionReportForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName" />
            <titus:reportGroupSortColumnTag name="companyName" labelKey="Company" render="${sessionScope.user.valueMap['isDefaultCompany'] == true}"/>
            <titus:reportGroupSortColumnTag name="startConection" labelKey="User.startConection" isDate="true"/>
            <titus:reportGroupSortColumnTag name="endConection" labelKey="User.endConection" isDate="true"/>
            <titus:reportGroupSortColumnTag name="lastConection" labelKey="User.lastConection" isDate="true"/>
            <titus:reportGroupSortColumnTag name="ip" labelKey="User.ip"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>

<titus:reportInitializeConstantsTag/>

<titus:reportTag id="userSessionReportList" title="Admin.Report.UserSessionList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="userName" resourceKey="User.userName" type="${FIELD_TYPE_STRING}" width="30"
                      fieldPosition="1"/>


<c:set var="width" value="17"/>

<c:if test="${sessionScope.user.valueMap['isDefaultCompany'] == true}">
    <titus:reportFieldTag name="companyName" resourceKey="Company" type="${FIELD_TYPE_STRING}" width="30"
                          fieldPosition="2"/>
    <c:set var="width" value="10"/>

</c:if>


<titus:reportFieldTag name="startConection" resourceKey="User.startConection"
                      type="${FIELD_TYPE_DATELONG}" width="${width}" fieldPosition="3"
                      patternKey="dateTimePattern"/>

<titus:reportFieldTag name="endConection" resourceKey="User.endConection" type="${FIELD_TYPE_DATELONG}"
                      width="${width}" fieldPosition="4" patternKey="dateTimePattern"/>

<titus:reportFieldTag name="lastConection" resourceKey="User.lastConection"
                      type="${FIELD_TYPE_DATELONG}" width="${width}" fieldPosition="5"
                      patternKey="dateTimePattern"/>

<titus:reportFieldTag name="ip" resourceKey="User.ip" type="${FIELD_TYPE_STRING}" width="${width}"
                      align="${FIELD_ALIGN_RIGHT}" fieldPosition="6"/>


<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="cancel" styleClass="button" onclick="jump()" tabindex="58">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

</html:form>
</table>