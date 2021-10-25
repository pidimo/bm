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
<br>
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="/Report/ContactPersonList/Execute.do" focus="parameter(addressId)" styleId="contactReportList">
        <tr>
            <td height="20" class="title" colspan="4">
                <fmt:message key="Contact.Report.ContactPersonList"/>
            </td>
        </tr>
        <TR>
            <TD class="label" width="15%"><fmt:message key="Appointment.contact"/></TD>
            <TD class="contain" width="32%">

                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="mediumText"
                          maxlength="40"
                          tabindex="1" readonly="true"/>
                <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                                  hide="false" submitOnSelect="true"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear"
                                       submitOnClear="true"
                                       hide="false"/>
            </TD>
            <td class="label" width="15%"><fmt:message key="Contact.language"/></TD>
            <td class="contain" width="38%">
                <fanta:select property="parameter(languageId)" listName="languageBaseList" firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                              tabIndex="6">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>

        </tr>
        <tr>
            <TD class="label"><fmt:message key="ContactPerson.department"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(department_Id)" listName="departmentBaseList" firstEmpty="true"
                              labelProperty="name" valueProperty="departmentId" styleClass="mediumSelect" tabIndex="2">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                     value="${not empty contactReportListForm.params.addressId?contactReportListForm.params.addressId:0}"/>
                </fanta:select>
            </TD>
            <TD class="label"><fmt:message key="Contact.country"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(countryId)" styleId="countryId" listName="countryBasicList"
                              firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                              tabIndex="7">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
        </tr>
        <tr>
            <TD class="label"><fmt:message key="ContactPerson.personType"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(personTypeId)" listName="personTypeList" firstEmpty="true"
                              labelProperty="name" module="/catalogs" valueProperty="id" styleClass="mediumSelect"
                              tabIndex="3">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
            <td class="label"><fmt:message key="Contact.cityZip"/></TD>
            <td class="contain">
                <app:text property="parameter(zip)" styleClass="zipText" maxlength="10" titleKey="Contact.zip"
                          tabindex="8"/>
                &nbsp;<app:text property="parameter(cityName)" styleClass="cityNameText" tabindex="8"/>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Contact.status"/></td>
            <td class="contain">
                <c:choose>
                    <c:when test="${contactReportListForm.params.active==null}">
                        <html:select property="parameter(active)" value="1" styleClass="mediumSelect" tabindex="4">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:select property="parameter(active)" styleClass="mediumSelect" tabindex="4">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="label">
                <fmt:message key="Common.creationDate"/>
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
        <tr>
            <td class="label">
                <fmt:message key="Contact.keywords"/>
            </td>
            <td class="contain" colspan="3">
                <html:text property="parameter(keywords)" styleClass="mediumText" maxlength="50" tabindex="5"/>
            </td>
        </tr>

        <c:set var="reportFormats" value="${contactReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${contactReportListForm.pageSizes}" scope="request"/>

        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="contactName" labelKey="Contact" isDefault="true"
                                                    defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="contactPersonName" labelKey="ContactPerson"/>
                    <titus:reportGroupSortColumnTag name="department" labelKey="ContactPerson.department"/>
                    <titus:reportGroupSortColumnTag name="function" labelKey="ContactPerson.function"/>
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
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="contactPersonReportList" title="Contact.Report.ContactPersonList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="contactName" resourceKey="Contact" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="contactPersonName" resourceKey="ContactPerson" type="${FIELD_TYPE_STRING}"
                              width="25"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="department" resourceKey="ContactPerson.department" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="function" resourceKey="ContactPerson.function" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="4"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="5"/>
    </html:form>
</table>