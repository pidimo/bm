<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<table cellpadding="0" cellspacing="0" border="0" id="tableId" width="95%" align="center">
<tr>
    <td align="left">
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <app:url
                    value="SalePositionBySale/Forward/Create.do"
                    var="url"/>
            <html:button styleClass="button" property="dto(new)"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:button>
        </app2:checkAccessRight>
    </td>
</tr>
<tr>
    <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%"
               class="container" align="center">
            <tr>
                <td class="title">
                    <fmt:message key="${windowTitle}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <app:url value="SalePositionBySale/List" var="listUrl" enableEncodeURL="false" context="request"/>
                    <c:set var="useJavaScript" value="true" scope="request"/>
                    <c:set var="editUrl" value="SalePositionBySale/Forward/Update.do" scope="request"/>
                    <c:set var="deleteUrl" value="SalePositionBySale/Forward/Delete.do" scope="request"/>
                    <c:import url="/common/contacts/SalePositionBySaleFragmentList.jsp"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>