<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>

<table cellpadding="0" cellspacing="0" border="0" id="tableId" width="95%" align="center">
<tr>
    <td align="left">
        <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
            <app:url
                    value="/SalesProcess/ProductContractBySalePosition/Forward/Create.do?salePositionId=${param.salePositionId}&productName=${app2:encode(param.productName)}&customerName=${app2:encode(param.customerName)}"
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
                    <app:url value="SalesProcess/ProductContractBySalePosition/List.do" var="listUrl" enableEncodeURL="false" context="request"/>
                    <c:set var="editUrl" value="SalesProcess/ProductContractBySalePosition/Forward/Update.do" scope="request"/>
                    <c:set var="deleteUrl" value="SalesProcess/ProductContractBySalePosition/Forward/Delete.do" scope="request"/>
                    <c:import url="/common/sales/ProductContractFragmentList.jsp"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>
