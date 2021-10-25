<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>
    function goSubmit() {
        document.forms[0].submit();
    }
</script>

<table border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Product.Report.SupplierList"/></td>
    </tr>

    <html:form action="/Report/SupplierList/Execute.do" focus="parameter(active)" styleId="productStyle">
        <TR>
            <TD class="label" width="15%"><fmt:message key="Product.title"/></TD>
            <TD class="contain" width="35%">
                <html:hidden property="parameter(productId)" styleId="field_key"/>
                <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
                <html:hidden property="parameter(2)" styleId="field_unitId"/>
                <html:hidden property="parameter(3)" styleId="field_price"/>

                <app:text property="parameter(productName)" styleId="field_name" styleClass="mediumText" maxlength="40"
                          readonly="true"/>
                <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"/>
                <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"/>
            </TD>
            <TD class="topLabel" width="15%"><fmt:message key="SupplierProduct.discount"/></TD>
            <TD class="containTop" width="35%">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(discount1)" styleClass="numberText" tabindex="2" maxlength="15"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(discount2)" styleClass="numberText" styleId="discount2"
                                tabindex="3" maxlength="15" numberType="decimal" maxInt="10" maxFloat="2"/>
            </TD>
        </TR>
        <TR>
            <td class="label"><fmt:message key="Contact.status"/></TD>
            <td class="contain">
                <c:choose>
                    <c:when test="${supplierReportListForm.params.active==null}">
                        <html:select property="parameter(active)" value="1" styleClass="mediumSelect" tabindex="1">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:select property="parameter(active)" styleClass="mediumSelect" tabindex="2">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>

            <TD class="topLabel"><fmt:message key="Product.price"/></TD>
            <TD class="containTop">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(price1)" styleClass="numberText" tabindex="4" maxlength="15"
                                numberType="decimal" maxInt="10" maxFloat="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(price2)" styleClass="numberText" styleId="price2" tabindex="5"
                                maxlength="15" numberType="decimal" maxInt="10" maxFloat="2"/>
            </TD>
        </TR>
        <tr>
            <td colspan="4">
                <titus:reportGroupSortTag width="100%">
                    <titus:reportGroupSortColumnTag name="productName" labelKey="Article.productName" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
                    <titus:reportGroupSortColumnTag name="supplierName" labelKey="Supplier"/>
                    <titus:reportGroupSortColumnTag name="contact" labelKey="ContactPerson" />
                    <titus:reportGroupSortColumnTag name="price" labelKey="Product.price" />
                    <titus:reportGroupSortColumnTag name="partNumber" labelKey="SupplierProduct.orderNumber" />
                    <titus:reportGroupSortColumnTag name="active" labelKey="Common.active" />
                </titus:reportGroupSortTag>
            </td>
        </tr>
        <c:set var="reportFormats" value="${supplierReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${supplierReportListForm.pageSizes}" scope="request"/>

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
        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="supplierReportList" title="Product.Report.SupplierList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        <titus:reportFieldTag name="productName" resourceKey="Article.productName" type="${FIELD_TYPE_STRING}"
                              width="20"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="supplierName" resourceKey="Supplier" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="contact" resourceKey="ContactPerson" type="${FIELD_TYPE_STRING}" width="20"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="price" resourceKey="Product.price" align="${FIELD_ALIGN_RIGHT}"
                              patternKey="numberFormat.2DecimalPlaces" type="${FIELD_TYPE_DECIMALNUMBER}" width="15"
                              fieldPosition="4" />
        <titus:reportFieldTag name="partNumber" resourceKey="SupplierProduct.orderNumber" type="${FIELD_TYPE_STRING}"
                              width="15"
                              fieldPosition="5"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="6"/>
    </html:form>
</table>
