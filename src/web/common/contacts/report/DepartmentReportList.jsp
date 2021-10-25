<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>
    function goSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.forms[0].submit();
        }
    }
</script>
<br>
<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="/Report/DepartmentList/Execute.do" focus="parameter(address)" styleId="departmentReportList">
        <tr>
            <td height="20" class="title" colspan="4">
                <fmt:message key="Contact.Report.DepartmentList"/>
            </td>
        </tr>
        <TR>
            <TD class="label" width="15%"><fmt:message key="Contact"/></TD>
            <TD class="contain" width="35%">
                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="mediumText"
                          maxlength="40" tabindex="1" readonly="true"/>
                <tags:selectPopup url="/contacts/OrganizationSearch.do" name="searchAddress" titleKey="Common.search"
                                  hide="false" submitOnSelect="true"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear" submitOnClear="true"
                                       hide="false"/>
            </TD>
            <TD class="label" width="15%"><fmt:message key="Department.departmentParent"/></TD>
            <TD class="contain" width="35%">
                <fanta:select property="parameter(parentId)" listName="departmentBaseList" firstEmpty="true"
                              labelProperty="name" valueProperty="departmentId" styleClass="middleSelect" tabIndex="3">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${not empty departmentReportListForm.params.addressId?departmentReportListForm.params.addressId:0}"/>
                </fanta:select>
            </TD>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Contact.Organization.Department.manager"/></td>
            <td class="contain" colspan="3">
                <fanta:select property="parameter(contactPersonId)" listName="contactPersonSimpleList" tabIndex="2"
                              firstEmpty="true"
                              labelProperty="contactPersonName" valueProperty="contactPersonId"
                              styleClass="mediumSelect"
                              module="/contacts">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId" value="${not empty departmentReportListForm.params.addressId?departmentReportListForm.params.addressId:0}"/>
                </fanta:select>
            </td>
        </tr>

        <c:set var="reportFormats" value="${departmentReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${departmentReportListForm.pageSizes}" scope="request"/>

        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="contactName" labelKey="Contact" isDefault="true" isDefaultGrouping="true" defaultOrder="true"/>
                    <titus:reportGroupSortColumnTag name="name" labelKey="Department.name"/>
                    <titus:reportGroupSortColumnTag name="parentName" labelKey="Department.parentName"/>
                    <titus:reportGroupSortColumnTag name="managerName" labelKey="Contact.Organization.Department.manager"/>
                </titus:reportGroupSortTag>
            </td>
        </tr>

        <tags:reportOptionsTag/>
        
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                        key="Campaign.Report.generate"/></html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="formReset('departmentReportList')"><fmt:message
                        key="Common.clear"/></html:button>
            </td>
        </tr>
        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="departmentReportList" title="Contact.Report.DepartmentList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="contactName" resourceKey="Contact" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="name" resourceKey="Department.name" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="parentName" resourceKey="Department.parentName" type="${FIELD_TYPE_STRING}"
                              width="25" fieldPosition="3"/>
        <titus:reportFieldTag name="managerName" resourceKey="Contact.Organization.Department.manager"
                              type="${FIELD_TYPE_STRING}" width="30" fieldPosition="4"/>
    </html:form>
</table>