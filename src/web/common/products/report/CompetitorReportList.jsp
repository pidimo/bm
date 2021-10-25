<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="97%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Product.Report.CompetitorList"/></td>
    </tr>

    <html:form action="/Report/CompetitorList/Execute.do" focus="parameter(productName)" styleId="productStyle">
        <TR>
            <TD class="label" width="13%"><fmt:message key="Product.title"/></TD>
            <TD class="contain" width="34%">
                <html:hidden property="parameter(productId)" styleId="field_key"/>
                <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                <html:hidden property="parameter(2)" styleId="field_unitId"/>
                <html:hidden property="parameter(3)" styleId="field_price"/>

                <app:text property="parameter(productName)" styleId="field_name" styleClass="mediumText" maxlength="40"
                          readonly="true" tabindex="1"/>
                <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"/>
                <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"/>
            </TD>
            <td class="label" width="15%"><fmt:message key="Competitor.entryDate"/></td>
            <td class="contain" width="38%">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(startRange)" maxlength="10" tabindex="5" styleId="startRange"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endRange)" maxlength="10" tabindex="6" styleId="endRange"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
            </td>
        </TR>
        <TR>
            <TD class="topLabel"><fmt:message key="Product.price"/></TD>
            <TD class="containTop" colspan="3">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(price1)" styleClass="numberText" tabindex="2" maxlength="12"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(price2)" styleId="price2" styleClass="numberText" tabindex="3"
                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
            </TD>
        </TR>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="product_Name" labelKey="Competitor.product" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="productName" labelKey="Competitor.competitorProduct"/>
                    <titus:reportGroupSortColumnTag name="competitorName" labelKey="Competitor.contact" />
                    <titus:reportGroupSortColumnTag name="entryDate" labelKey="Competitor.entryDate" isDate="true"/>
                    <titus:reportGroupSortColumnTag name="price" labelKey="Competitor.price" />
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <c:set var="reportFormats" value="${competitorReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${competitorReportListForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                        key="Campaign.Report.generate"/></html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('productStyle')">
                    <fmt:message key="Common.clear"/></html:button>
            </td>
        </tr>
        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="competitorReportList" title="Product.Report.CompetitorList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="product_Name" resourceKey="Competitor.product" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="1" />
        <titus:reportFieldTag name="productName" resourceKey="Competitor.competitorProduct" type="${FIELD_TYPE_STRING}"
                              width="25"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="competitorName" resourceKey="Competitor.contact" type="${FIELD_TYPE_STRING}"
                              width="25" fieldPosition="3"/>
        <titus:reportFieldTag name="entryDate" resourceKey="Competitor.entryDate" type="${FIELD_TYPE_DATEINT}"
                              patternKey="datePattern" width="15" fieldPosition="4"/>
        <titus:reportFieldTag name="price" resourceKey="Competitor.price" type="${FIELD_TYPE_DECIMALNUMBER}"
                              patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="15"
                              fieldPosition="5"/>
    </html:form>
</table>
