<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<%--<tags:initSelectPopupAdvanced fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>--%>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initSelectPopupEven fields="user_key, user_name"/>
<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveOrNoList(request));
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

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="SalesProcess.Report.ActionPositionList"/>
    </td>
</tr>
<html:form action="/Report/ActionPositionList/Execute.do" focus="parameter(contactId)" styleId="processStyle">
<tr>
    <td class="label" width="15%"><fmt:message key="Document.salesAsociated"/></TD>
    <td class="contain" width="33%">
        <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
        <html:hidden property="parameter(2)" styleId="field_price"/>
        <html:hidden property="parameter(3)" styleId="field_unitId"/>
        <html:hidden property="parameter(processId)" styleId="user_key"/>
        <html:hidden property="parameter(processName)" styleId="user_name"/>

        <app:text property="parameter(processName)" styleClass="middleText" maxlength="30" styleId="user_name"
                  tabindex="1" disabled="true"/>
        <tags:selectPopup url="/sales/SalesProcess/SimpleSearchProcess.do" name="searchSalesProcess"
                          titleKey="Common.search" width="900" heigth="480" scrollbars="false" submitOnSelect="true"/>
        <tags:clearSelectPopup keyFieldId="user_key" nameFieldId="user_name" titleKey="Common.clear"
                               submitOnClear="true"/>
    </td>
    <TD class="topLabel" width="15%"><fmt:message key="Product.price"/></TD>
    <TD class="containTop" width="37%">
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
</tr>
<tr>
    <TD class="label"><fmt:message key="Common.action"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(contactId)" listName="actionList" firstEmpty="true" labelProperty="note"
                      valueProperty="contactId"
                      module="/sales" styleClass="middleSelect" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="processId" value="${not empty actionPositionReportForm.params.processId?actionPositionReportForm.params.processId:0}"/>
        </fanta:select>
    </TD>

    <TD class="topLabel"><fmt:message key="ActionPosition.quantity"/></TD>
    <TD class="containTop">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amount1)" styleClass="numberText" tabindex="7" maxlength="12"
                        numberType="integer"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amount2)" styleId="amount2" styleClass="numberText" tabindex="8"
                        maxlength="12" numberType="integer"/>
    </TD>
</tr>

<tr>
    <TD class="label"><fmt:message key="Product.title"/></TD>
    <TD class="contain">
        <html:hidden property="parameter(productId)" styleId="field_key"/>
        <app:text property="parameter(productName)" styleClass="middleText" tabindex="3" readonly="true"
                  styleId="field_name"/>
        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"/>

    </TD>
    <TD class="label"><fmt:message key="ActionPosition.totalPrice"/></TD>
    <TD class="containTop" width="36%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(totalPrice1)" styleClass="numberText" tabindex="10" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(totalPrice2)" styleId="totalPrice2" styleClass="numberText" tabindex="11"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </TD>
</tr>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="processName" labelKey="SalesProcess.name" isDefault="true" defaultOrder="true" isDefaultGrouping="true"/>
            <titus:reportGroupSortColumnTag name="note" labelKey="SalesProcessAction.title"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="ActionPosition.product" />
            <titus:reportGroupSortColumnTag name="unit" labelKey="ActionPosition.unit"/>
            <titus:reportGroupSortColumnTag name="amount" labelKey="ActionPosition.quantity" />
            <titus:reportGroupSortColumnTag name="price" labelKey="Competitor.price" />
            <titus:reportGroupSortColumnTag name="totalPrice" labelKey="ActionPosition.totalPrice" />
        </titus:reportGroupSortTag>
    </td>
</tr>
<c:set var="reportFormats" value="${actionPositionReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${actionPositionReportForm.pageSizes}" scope="request"/>
<tags:reportOptionsTag />

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('processStyle')">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>

</tr>

<c:set var="simple"><fmt:message key="contact.payment.payMethod.single"/></c:set>
<c:set var="partial"><fmt:message key="contact.payment.payMethod.partial"/></c:set>
<c:set var="periodic"><fmt:message key="contact.payment.payMethod.periodic"/></c:set>


<titus:reportInitializeConstantsTag/>
<titus:reportTag id="actionPositionReportList" title="SalesProcess.Report.ActionPositionList"
                 locale="${sessionScope.user.valueMap['locale']}" pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
<titus:reportFieldTag name="processName" resourceKey="SalesProcess.processName" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="1"/>
<titus:reportFieldTag name="note" resourceKey="SalesProcessAction.title" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="2"/>
<titus:reportFieldTag name="productName" resourceKey="ActionPosition.product" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="3"/>
<%--<titus:reportFieldTag name="productVersion" resourceKey="ActionPosition.version" type="${FIELD_TYPE_STRING}" width="10"
                      fieldPosition="4"/>--%>
<titus:reportFieldTag name="unit" resourceKey="ActionPosition.unit" type="${FIELD_TYPE_STRING}" width="9"
                      fieldPosition="5"/>
<titus:reportFieldTag name="amount" resourceKey="ActionPosition.quantity" type="${FIELD_TYPE_DECIMALNUMBER}"
                      patternKey="numberFormat.2DecimalPlaces"
                      align="${FIELD_ALIGN_RIGHT}"
                      width="7" fieldPosition="6"/>
<titus:reportFieldTag name="price" resourceKey="Competitor.price" type="${FIELD_TYPE_DECIMALNUMBER}"
                      patternKey="numberFormat.4DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="7"
                      fieldPosition="7"/>
<titus:reportFieldTag name="totalPrice" resourceKey="ActionPosition.totalPrice" type="${FIELD_TYPE_DECIMALNUMBER}"
                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" width="7"
                      fieldPosition="8"/>
</table>
</html:form>