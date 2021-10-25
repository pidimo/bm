<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>

<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>
<script>

    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
</script>
<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="/Report/ContactList/Execute.do" focus="countryId" styleId="contactReportList">
        <tr>
            <td height="20" class="title" colspan="4">
                <fmt:message key="Contact.Report.ContactList"/>
            </td>
        </tr>
        <TR>
            <TD class="label" width="15%"><fmt:message key="Contact.country"/></TD>
            <TD class="contain" width="30%">
                <fanta:select property="parameter(countryId)" styleId="countryId" listName="countryBasicList"
                              firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                              tabIndex="1">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
            <TD class="label" width="20%"><fmt:message key="Contact.type"/></TD>
            <TD class="contain" width="35%">
                <html:select property="parameter(code)" styleClass="mediumSelect" tabindex="6">
                    <html:option value=""/>
                    <html:options collection="contactTypeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Contact.cityZip"/></TD>
            <td class="contain">
                <app:text property="parameter(zip)" styleClass="zipText" maxlength="10" titleKey="Contact.zip"
                          tabindex="2"/>
                &nbsp;<app:text property="parameter(cityName)" styleClass="cityNameText" tabindex="3"/>

            </td>
            <td class="label"><fmt:message key="Contact.status"/></TD>
            <td class="contain">
                <c:choose>
                    <c:when test="${contactReportListForm.params.active==null}">
                        <html:select property="parameter(active)" value="1" styleClass="mediumSelect" tabindex="7">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:select property="parameter(active)" styleClass="mediumSelect" tabindex="7">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Contact.language"/></TD>
            <td class="contain">
                <fanta:select property="parameter(languageId)" listName="languageBaseList" firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="mediumSelect"
                              tabIndex="4">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <td class="label">
                <fmt:message key="Contact.keywords"/>
            </td>
            <td class="contain">
                <html:text property="parameter(keywords)" styleClass="mediumText" maxlength="50" tabindex="8"/>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Common.for"/></td>
            <td class="contain">
                <html:select property="parameter(addressType)" styleClass="mediumSelect" tabindex="5">
                    <html:option value=""/>
                    <html:options collection="addressTypeList" property="value" labelProperty="label"/>
                </html:select>
            </td>
            <td class="label">
                <fmt:message key="Campaign.dateCreation"/>
            </td>
            <td class="contain">
                <fmt:message key="datePattern" var="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(startRange)" maxlength="10" tabindex="9" styleId="startRange"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endRange)" maxlength="10" tabindex="10" styleId="endRange"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
            </td>
        </tr>


        <c:set var="reportFormats" value="${contactReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${contactReportListForm.pageSizes}" scope="request"/>

        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="addressName" labelKey="Contact.name"/>
                    <titus:reportGroupSortColumnTag name="countryCode" labelKey="Contact.countryCode"/>
                    <titus:reportGroupSortColumnTag name="zip" labelKey="Contact.zip"/>
                    <titus:reportGroupSortColumnTag name="cityName" labelKey="Contact.city"/>
                    <titus:reportGroupSortColumnTag name="addressType" labelKey="Contact.type"/>
                    <titus:reportGroupSortColumnTag name="active" labelKey="Common.active"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                        key="Campaign.Report.generate"/></html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="formReset('contactReportList')">
                    <fmt:message key="Common.clear"/></html:button>
            </td>
        </tr>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
        <c:set var="person"><fmt:message key="Contact.person"/></c:set>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="contactReportList" title="Contact.Report.ContactList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="addressName" resourceKey="Contact.name" type="${FIELD_TYPE_STRING}" width="30"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="countryCode" resourceKey="Contact.countryCode" type="${FIELD_TYPE_STRING}"
                              width="10"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="zip" resourceKey="Contact.zip" type="${FIELD_TYPE_STRING}" width="10"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="cityName" resourceKey="Contact.city" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="addressType" resourceKey="Contact.type" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="5"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource addressType [${person}] [${organization}] [1] [0]"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="6"/>
    </html:form>
</table>