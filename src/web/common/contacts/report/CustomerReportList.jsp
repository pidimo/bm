<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="93%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Contact.Report.CustomerList"/></td>
    </tr>

    <html:form action="/Report/CustomerList/Execute.do" focus="parameter(countryId)" styleId="customerReport">
        <TR>
            <td class="label" width="15%"><fmt:message key="Contact.country"/></td>
            <td class="contain" width="32%">
                <fanta:select property="parameter(countryId)" styleId="countryId" listName="countryBasicList"
                              firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                              tabIndex="1">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <TD class="label" width="18%"><fmt:message key="Customer.source"/></TD>
            <TD class="contain" width="35%">
                <app:catalogSelect property="parameter(sourceId)" catalogTable="addresssource" idColumn="sourceid"
                                   labelColumn="sourcename" styleClass="mediumSelect" tabindex="8"/>
            </TD>
        </tr>

        <tr>
            <TD class="label"><fmt:message key="Contact.cityZip"/></TD>
            <TD class="contain">
                <app:text property="parameter(zip)" styleClass="zipText" maxlength="10" titleKey="Contact.zip"
                          tabindex="2"/>
                &nbsp;<app:text property="parameter(cityName)" styleClass="cityNameText" tabindex="3"/>

            </TD>
            <TD class="label"><fmt:message key="Customer.priority"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(priorityId)" listName="priorityList"
                              labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                              module="/catalogs" firstEmpty="true" tabIndex="9">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="type" value="CUSTOMER"/>
                </fanta:select>
            </td>
        </tr>
        <TR>
            <TD class="label"><fmt:message key="Customer.partner"/></TD>
            <TD class="contain">
                <html:hidden property="parameter(partnerId)" styleId="fieldAddressId_id"/>
                <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="mediumText"
                          maxlength="40"
                          tabindex="4" readonly="true"/>
                <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                                  hide="false"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear"
                                       hide="false"/>
            </TD>
            <TD class="label"><fmt:message key="Customer.payCondition"/></TD>
            <TD class="contain">
                <app:catalogSelect property="parameter(payConditionId)" catalogTable="paycondition"
                                   idColumn="payconditionid"
                                   labelColumn="conditionname" styleClass="mediumSelect" tabindex="10"/>
            </TD>
        </tr>
        <tr>
            <TD class="label"><fmt:message key="Appointment.responsible"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(employeeId)" listName="employeeBaseList"
                              labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                              module="/contacts" tabIndex="5" firstEmpty="true">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
            <TD class="label"><fmt:message key="Customer.payMorality"/></TD>
            <TD class="contain">
                <app:catalogSelect property="parameter(payMoralityId)" catalogTable="paymorality"
                                   idColumn="paymoralityid"
                                   labelColumn="paymoralityname" styleClass="mediumSelect" tabindex="11"/>
            </TD>
        </tr>
        <tr>
            <TD class="label"><fmt:message key="Customer.type"/></TD>
            <TD class="contain">
                <app:catalogSelect property="parameter(customerTypeId)" catalogTable="customertype"
                                   idColumn="customertypeid"
                                   labelColumn="customertypename" styleClass="mediumSelect" tabindex="6"/>
            </TD>
            <TD class="label"><fmt:message key="Customer.expectedTurnOver"/></TD>
            <TD class="containTop">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(turnOver1)" styleClass="numberText" tabindex="12" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(turnOver2)" styleClass="numberText" tabindex="13" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
            </TD>
        </tr>
        <tr>
            <TD class="label"><fmt:message key="Customer.branch"/></TD>
            <TD class="contain">
                <app:catalogSelect property="parameter(branchId)" catalogTable="branch" idColumn="branchid"
                                   labelColumn="branchname" styleClass="mediumSelect" tabindex="7"/>
            </TD>
            <TD class="label"><fmt:message key="Customer.numberOfEmpoyees"/></TD>
            <TD class="containTop">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:text property="parameter(numberEmployee1)" styleClass="numberText" tabindex="14" maxlength="15"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:text property="parameter(numberEmployee2)" styleId="numberEmployee2" styleClass="numberText"
                          tabindex="15"
                          maxlength="15"/>
            </TD>
        </tr>

        <c:set var="reportFormats" value="${customerReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${customerReportListForm.pageSizes}" scope="request"/>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="name" labelKey="ProductCustomer.customer"/>
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
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('customerReport')">
                    <fmt:message key="Common.clear"/></html:button>
            </td>
        </tr>

        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="organization"><fmt:message key="Contact.organization"/></c:set>
        <c:set var="person"><fmt:message key="Contact.person"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="customerReportList" title="Contact.Report.CustomerList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="name" resourceKey="ProductCustomer.customer" type="${FIELD_TYPE_STRING}" width="30"
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
