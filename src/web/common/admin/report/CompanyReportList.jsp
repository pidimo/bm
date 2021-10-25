<%@ include file="/Includes.jsp" %>
<%
    if (null != request.getParameter("parameter(rangeAmount)"))
        request.setAttribute("range_amount", Boolean.valueOf(true));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
    pageContext.setAttribute("trialList", JSPHelper.getTrialList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<calendar:initialize/>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="container" align="center">
    <tr>
        <td class="title" colspan="4"><fmt:message key="Admin.Report.CompanyList"/></td>
    </tr>

    <html:form action="/Report/CompanyList/Execute.do" focus="parameter(active)" styleId="companyReportForm">
        <tr>
            <td class="label" width="15%"><fmt:message key="Common.active"/></td>
            <td class="contain" width="35%">
                <c:choose>
                    <c:when test="${companyReportForm.params.active==null}">
                        <html:select property="parameter(active)" value="1" styleClass="mediumSelect" tabindex="1">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:select property="parameter(active)" styleClass="mediumSelect" tabindex="1">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="topLabel" width="15%"><fmt:message key="Common.creationDate"/></td>
            <td class="contain" width="35%">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(startCreationDate)" maxlength="10" tabindex="5"
                              styleId="startCreationDate" calendarPicker="true" datePatternKey="${datePattern}"
                              styleClass="dateText" convert="true"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endCreationDate)" maxlength="10" tabindex="6"
                              styleId="endCreationDate" calendarPicker="true" datePatternKey="${datePattern}"
                              styleClass="dateText" convert="true"/>
            </td>

        </tr>

        <tr>
            <td class="label"><fmt:message key="Company.trial"/></td>
            <td class="contain">
                <html:select property="parameter(trial)" styleClass="mediumSelect" tabindex="2">
                    <html:options collection="trialList" property="value" labelProperty="label"/>
                </html:select>
            </td>
            <td class="topLabel"><fmt:message key="Company.startLicenseDate"/></td>
            <td class="contain">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(initStartLicenseDate_1)" maxlength="10" tabindex="7"
                              styleId="startDeliveryDate" calendarPicker="true" datePatternKey="${datePattern}"
                              styleClass="dateText" convert="true"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endStartLicenseDate_1)" maxlength="10" tabindex="8"
                              styleId="endDeliveryDate" calendarPicker="true" datePatternKey="${datePattern}"
                              styleClass="dateText" convert="true"/>
            </td>

        </tr>


        <tr>
            <td class="topLabel"><fmt:message key="Company.usersAllowed"/></td>
            <td class="containTop">
                <fmt:message key="Common.from"/>
                &nbsp;
                <html:text property="parameter(initNumberOfUsers_3)" styleClass="numberText" tabindex="3"
                           maxlength="15"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <html:text property="parameter(endNumberOfUsers_3)" styleClass="numberText" tabindex="4" maxlength="15"
                           styleId="amount2"/>
            </td>
            <td class="topLabel"><fmt:message key="Company.finishLicenseDate"/></td>
            <td class="contain">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(initFinishLicenseDate_2)" maxlength="10" tabindex="9"
                              styleId="startFinishDeliveryDate" calendarPicker="true" datePatternKey="${datePattern}"
                              styleClass="dateText" convert="true"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endFinishLicenseDate_2)" maxlength="10" tabindex="10"
                              styleId="endFinishDeliveryDate" calendarPicker="true" datePatternKey="${datePattern}"
                              styleClass="dateText" convert="true"/>
            </td>
        </tr>

        <c:set var="reportFormats" value="${companyReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${companyReportForm.pageSizes}" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="companyName" labelKey="Company"/>
                    <titus:reportGroupSortColumnTag name="login" labelKey="Company.login"/>
                    <titus:reportGroupSortColumnTag name="usersAllowed" labelKey="Company.usersAllowed"/>
                    <titus:reportGroupSortColumnTag name="startLicense" labelKey="Company.startLicenseDate" isDate="true"/>
                    <titus:reportGroupSortColumnTag name="finishLicense" labelKey="Company.finishLicenseDate" isDate="true"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="formReset('companyReportForm')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>


        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="trial"><fmt:message key="Company.trial"/></c:set>
        <c:set var="default"><fmt:message key="Common.default"/></c:set>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="companyReportList" title="Admin.Report.CompanyList"
                         locale="${sessionScope.user.valueMap['locale']}"/>

        <titus:reportFieldTag name="companyName" resourceKey="Company" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="1"/>

        <titus:reportFieldTag name="login" resourceKey="Company.login" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="2"/>

        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="3"/>

        <titus:reportFieldTag name="trial" resourceKey="Company.trial" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getDefault trial [1] [${trial}]"
                              fieldPosition="4"/>

        <titus:reportFieldTag name="usersAllowed" resourceKey="Company.usersAllowed" type="${FIELD_TYPE_INTEGER}"
                              width="10"
                              align="${FIELD_ALIGN_RIGHT}" fieldPosition="5"/>

        <titus:reportFieldTag name="startLicense" resourceKey="Company.startLicenseDate" type="${FIELD_TYPE_DATEINT}"
                              width="15" fieldPosition="6" patternKey="datePattern"/>

        <titus:reportFieldTag name="finishLicense" resourceKey="Company.finishLicenseDate" type="${FIELD_TYPE_DATEINT}"
                              width="15" fieldPosition="7" patternKey="datePattern"/>

    </html:form>
</table>