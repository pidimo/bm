<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="95%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Product.Report.ProductList"/></td>
</tr>

<html:form action="/Report/ProductList/Execute.do" focus="parameter(productTypeId)" styleId="productStyle">
    <TR>
        <TD class="label" width="15%"><fmt:message key="Product.type"/></TD>
        <TD class="contain" width="30%">
            <fanta:select property="parameter(productTypeId)" listName="productTypeList" firstEmpty="true"
                          labelProperty="name"
                          valueProperty="id" module="/catalogs" styleClass="middleSelect" tabIndex="1"
                          preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
        <TD class="topLabel" width="15%"><fmt:message key="Product.priceNet"/></TD>
        <TD class="containTop" width="40%">
            <fmt:message key="Common.from"/>
            &nbsp;
            <app:numberText property="parameter(price1)" styleClass="numberText" tabindex="4" maxlength="12"
                            numberType="decimal" maxInt="10" maxFloat="2"/>
            &nbsp;
            <fmt:message key="Common.to"/>
            &nbsp;
            <app:numberText property="parameter(price2)" styleId="price2" styleClass="numberText" tabindex="5"
                            maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="Product.unit"/></TD>
        <TD class="contain">
            <fanta:select property="parameter(unitId)" listName="productUnitList" firstEmpty="true"
                          labelProperty="name"
                          valueProperty="id" module="/catalogs" styleClass="middleSelect" tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
        <TD class="topLabel"><fmt:message key="Product.priceGross"/></TD>
        <TD class="containTop">
            <fmt:message key="Common.from"/>
            &nbsp;
            <app:numberText property="parameter(priceGross1)" styleClass="numberText" tabindex="7" maxlength="15"
                            numberType="decimal" maxInt="10" maxFloat="2"/>
            &nbsp;
            <fmt:message key="Common.to"/>
            &nbsp;
            <app:numberText property="parameter(priceGross2)" styleClass="numberText" tabindex="8" maxlength="15"
                            numberType="decimal" maxInt="10" styleId="priceGross2" maxFloat="2"/>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="Product.group"/></TD>
        <TD class="contain">
            <fanta:select property="parameter(productGroupId)" listName="productGroupList" firstEmpty="true"
                          labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                          tabIndex="3">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                            separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
            </fanta:select>
        </TD>

        <TD class="label"><fmt:message key="Product.version"/></TD>
        <TD class="contain">
            <app:text property="parameter(currentVersion)" styleClass="shortText" maxlength="40" tabindex="10"/>
        </TD>
    </TR>
    <tr>
        <td colspan="4">
            <titus:reportGroupSortTag width="100%">
                <titus:reportGroupSortColumnTag name="productName" labelKey="Contact.name" />
                <titus:reportGroupSortColumnTag name="type" labelKey="Product.type"/>
                <titus:reportGroupSortColumnTag name="group" labelKey="Product.group" />
                <titus:reportGroupSortColumnTag name="unit" labelKey="Product.unit" />
                <titus:reportGroupSortColumnTag name="price" labelKey="Product.priceNet" />
                <titus:reportGroupSortColumnTag name="priceGross" labelKey="Product.priceGross" />
            </titus:reportGroupSortTag>
        </td>
    </tr>

    <c:set var="reportFormats" value="${productReportListForm.reportFormats}" scope="request"/>
    <c:set var="pageSizes" value="${productReportListForm.pageSizes}" scope="request"/>
    <tags:reportOptionsTag/>

    <tr>
        <td colspan="4" class="button">
            <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                    key="Campaign.Report.generate"/></html:submit>
            <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('productStyle')">
                <fmt:message
                        key="Common.clear"/></html:button>
        </td>
    </tr>

    <fmt:message var="eventTypeLabel" key="ProductType.item.event"/>
    <c:set var="eventTypeLabelFixed" value="${fn:replace(eventTypeLabel, '[', '(')}" />
    <c:set var="eventTypeLabelFixed" value="${fn:replace(eventTypeLabelFixed, ']', ')')}"/>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="productReportList" title="Product.Report.ProductList"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="productName" resourceKey="Contact.name" type="${FIELD_TYPE_STRING}" width="30"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="type" resourceKey="Product.type" type="${FIELD_TYPE_STRING}" width="18"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getProductTypeNameByCondition type productTypeType [${eventTypeLabelFixed}]"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="group" resourceKey="Product.group" type="${FIELD_TYPE_STRING}" width="18"
                          fieldPosition="3"/>
    <titus:reportFieldTag name="unit" resourceKey="Product.unit" type="${FIELD_TYPE_STRING}" width="14"
                          fieldPosition="4"/>
    <titus:reportFieldTag name="price" resourceKey="Product.priceNet" type="${FIELD_TYPE_DECIMALNUMBER}"
                          patternKey="numberFormat.2DecimalPlaces" width="10" fieldPosition="5"
                          align="${FIELD_ALIGN_RIGHT}"/>
    <titus:reportFieldTag name="priceGross" resourceKey="Product.priceGross" type="${FIELD_TYPE_DECIMALNUMBER}"
                          patternKey="numberFormat.2DecimalPlaces" width="10" fieldPosition="6"
                          align="${FIELD_ALIGN_RIGHT}"/>
</html:form>
</table>
