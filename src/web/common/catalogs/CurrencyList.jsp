<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="/Currency/List.do">
    <table width="70%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain">
        <html:text property="parameter(currencyName)" styleClass="largeText" />
        <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
    </td>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
        <fanta:alphabet action="Currency/List.do" parameterName="currencyNameAlpha"/>
        </td>
    </tr>
    </table>
</html:form>
    </td>
</tr>
<tr>
    <td>
<app2:checkAccessRight functionality="CURRENCY" permission="CREATE">
    <table width="70%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
    <html:form styleId="CREATE_NEW_CURRENCY" action="/Currency/Forward/Create.do?op=create">
        <TD colspan="6" class="button">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </TD>
    </html:form>
    </TR>
    </table>
</app2:checkAccessRight>
    </td>
</tr>

<tr>
    <td>

    <TABLE id="CurrencyList.jsp" border="0" cellpadding="0" cellspacing="0" width="70%" class="container" align="center">
    <tr>
        <td align="center">
            <fmt:message   var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fanta:table list="currencyList" width="100%" id="currency" action="Currency/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="Currency/Forward/Update.do?dto(currencyId)=${currency.id}&dto(currencyName)=${app2:encode(currency.name)}"/>
                <c:set var="deleteAction" value="Currency/Forward/Delete.do?dto(withReferences)=true&dto(currencyId)=${currency.id}&dto(currencyName)=${app2:encode(currency.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="CURRENCY" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CURRENCY" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Currency.name"  headerStyle="listHeader" width="25%" orderable="true" maxLength="25" />
                <fanta:dataColumn name="label" styleClass="listItem" title="Currency.label"  headerStyle="listHeader" width="15%" orderable="true" />
                <fanta:dataColumn name="symbol" styleClass="listItem" title="Currency.symbol"  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="unit" styleClass="listItem" title="Currency.unit" headerStyle="listHeader" width="15%" orderable="true" style="text-align:right" renderData="false" >
                    <c:set var="numberValue" value=""/>
                    <c:if test="${currency.unit != null}">
                        <fmt:formatNumber var="numberValue" value="${currency.unit}" type="number" pattern="${numberFormat}" />
                    </c:if>
                    ${numberValue}&nbsp;
                </fanta:dataColumn>
                <fanta:dataColumn name="" styleClass="listItem2" title="Currency.status" headerStyle="listHeader" width="25%" renderData="false" >
                    <c:if test="${currency.basic == '1'}">
                    <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>&nbsp;
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>

