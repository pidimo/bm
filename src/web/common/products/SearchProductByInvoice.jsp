<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Product.Title.SimpleSearch"/>
        </td>
    </tr>

    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/SearchProductByInvoice.do" focus="parameter(productName)">
            <td class="contain" nowrap>
                <html:text property="parameter(productName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="SearchProductByInvoice.do?parameter(invoiceId)=${param['parameter(invoiceId)']}" parameterName="productNameSingle"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <br/>
            <fanta:table width="100%" id="product" action="SearchProductByInvoice.do" imgPath="${baselayout}">
                <fmt:formatNumber var="priceFormatted" value="${product.price}" type="number"
                                      pattern="${numberFormat}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn useJScript="true"
                                        action="javascript:opener.selectMultipleField('${product.id}', '${app2:jscriptEncode(product.name)}', '${app2:jscriptEncode(product.versionNumber)}', '${priceFormatted}', '${product.unitId}');"
                                        name="" title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                        width="50%" image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" styleClass="listItem" title="Product.name" headerStyle="listHeader"
                                  width="25%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="type" styleClass="listItem" title="Product.type" headerStyle="listHeader"
                                  width="15%" orderable="true"/>
                <fanta:dataColumn name="group" styleClass="listItem" title="Product.group" headerStyle="listHeader"
                                  width="10%" orderable="true"/>
                <fanta:dataColumn name="unit" styleClass="listItem" title="Product.unit" headerStyle="listHeader"
                                  width="10%" orderable="true"/>
                <fanta:dataColumn name="price" styleClass="listItem2Right" title="Product.priceNet"
                                  headerStyle="listHeader"
                                  width="10%" orderable="true" renderData="false">
                    ${priceFormatted}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>
