<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<%
    pageContext.setAttribute("contactTypeList", JSPHelper.getContactTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>

<script>
    function goSubmit() {
        document.forms[0].submit();
    }
</script>

<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Contact.Report.SupplierList"/></td>
    </tr>

    <html:form action="/Report/SupplierList/Execute.do" focus="parameter(countryId)" styleId="supplierReport">
        <tr>
            <td class="label" width="15%"><fmt:message key="Contact.country"/></td>
            <td class="contain" width="35%">
                <fanta:select property="parameter(countryId)" styleId="countryId" listName="countryBasicList"
                              firstEmpty="true" labelProperty="name" valueProperty="id" module="/catalogs"
                              styleClass="middleSelect" tabIndex="1">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>

            <td class="label" width="15%"><fmt:message key="Contact.status"/></TD>
            <td class="contain" width="35%">
                <c:choose>
                    <c:when test="${listForm.params.active==null}">
                        <html:select property="parameter(active)" value="1" styleClass="mediumSelect" tabindex="5">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:select property="parameter(active)" styleClass="mediumSelect" tabindex="5">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>
        </TR>
        <TR>
            <TD class="label"><fmt:message key="Contact.cityZip"/></TD>
            <TD class="contain">

                <app:text property="parameter(zip)" styleClass="zipText" maxlength="10" titleKey="Contact.zip"
                          tabindex="2"/>
                &nbsp;<app:text property="parameter(cityName)" styleClass="cityNameText" tabindex="3"/>

            </TD>
            <TD class="label"><fmt:message key="Supplier.type"/></TD>
            <TD class="contain">
                <app:catalogSelect property="parameter(supplierTypeId)" catalogTable="suppliertype"
                                   idColumn="suppliertypeid" labelColumn="suppliertypename" styleClass="mediumSelect"
                                   tabindex="6"/>
            </TD>
        </tr>

        <TR>
            <td class="label"><fmt:message key="Contact.language"/></TD>
            <td class="contain">
                <fanta:select property="parameter(languageId)" listName="languageBaseList" firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                              tabIndex="4">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <TD class="label"><fmt:message key="Supplier.branch"/></TD>
            <TD class="contain">
                <app:catalogSelect property="parameter(branchId)" catalogTable="branch" idColumn="branchid"
                                   labelColumn="branchname" styleClass="mediumSelect" tabindex="7"/>

            </TD>
        </tr>

        <c:set var="reportFormats" value="${supplierReportForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${supplierReportForm.pageSizes}" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="supplierName" labelKey="Supplier"/>
                    <titus:reportGroupSortColumnTag name="branchName" labelKey="Supplier.branch"/>
                    <titus:reportGroupSortColumnTag name="supplierTypeName" labelKey="Supplier.type"/>
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
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('supplierReport')">
                    <fmt:message key="Common.clear"/></html:button>
            </td>
        </tr>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
        <c:set var="person"><fmt:message key="Contact.person"/></c:set>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="supplierReportList" title="Product.Report.SupplierList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="supplierName" resourceKey="Supplier" type="${FIELD_TYPE_STRING}" width="26"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="branchName" resourceKey="Supplier.branch" type="${FIELD_TYPE_STRING}" width="12"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="supplierTypeName" resourceKey="Supplier.type" type="${FIELD_TYPE_STRING}" width="15"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="countryCode" resourceKey="Contact.countryCode" type="${FIELD_TYPE_STRING}"
                              width="10" fieldPosition="4"/>
        <titus:reportFieldTag name="zip" resourceKey="Contact.zip" type="${FIELD_TYPE_STRING}" width="7"
                              fieldPosition="5"/>
        <titus:reportFieldTag name="cityName" resourceKey="Contact.city" type="${FIELD_TYPE_STRING}" width="8"
                              fieldPosition="6"/>
        <titus:reportFieldTag name="addressType" resourceKey="Contact.type" type="${FIELD_TYPE_STRING}"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource addressType [${person}] [${organization}] [1] [0]"
                              width="12" fieldPosition="7"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              width="8" fieldPosition="8"/>
    </html:form>
</table>
