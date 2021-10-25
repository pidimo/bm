<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>

<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="invoiceNetGross" value="${app2:getInvoiceNetGross(param['parameter(invoiceId)'], pageContext.request)}"/>

<c:set var="useNetPrice" value="${netPrice == invoiceNetGross}"/>

<c:set var="useGrossPrice" value="${grossPrice == invoiceNetGross}"/>

<script language="JavaScript" type="text/javascript">
    <!--
    function select(id, name, invoicePositionId) {
        opener.selectInvoicePosition(invoicePositionId);
        opener.selectField('fieldProductId_id', id, 'fieldProductName_id', name);
    }
    //-->
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="InvoicePosition.Title.search"/>
        </td>
    </tr>

    <TR>
        <td class="label">
            <fmt:message key="Common.search"/>
        </td>
        <html:form action="/SearchInvoicePosition.do?parameter(invoiceId)=${param['parameter(invoiceId)']}"
                   focus="parameter(productName)">

            <td class="contain" nowrap>
                <html:text property="parameter(productName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>&nbsp;
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="SearchInvoicePosition.do?parameter(invoiceId)=${param['parameter(invoiceId)']}"
                            parameterName="productNameAlpha"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
            <br/>
            <fanta:table width="100%" id="invoicePosition"
                         action="SearchInvoicePosition.do?parameter(invoiceId)=${param['parameter(invoiceId)']}"
                         imgPath="${baselayout}">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn useJScript="true"
                                        action="javascript:select('${invoicePosition.productId}', '${app2:jscriptEncode(invoicePosition.productName)}','${app2:jscriptEncode(invoicePosition.positionId)}');"
                                        name=""
                                        title="Common.select"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="number"
                                  styleClass="listItem"
                                  title="InvoicePosition.number"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true"
                                  maxLength="40"/>

                <fanta:dataColumn name="productName"
                                  styleClass="listItem"
                                  title="InvoicePosition.product"
                                  headerStyle="listHeader"
                                  width="25%"
                                  orderable="true"
                                  maxLength="40"/>

                <fanta:dataColumn name="unit"
                                  styleClass="listItem"
                                  title="Product.unit"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>

                <fanta:dataColumn name="quantity"
                                  styleClass="listItemRight"
                                  title="InvoicePosition.quantity"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="quantityFormatted" value="${invoicePosition.quantity}" type="number"
                                      pattern="${numberFormat}"/>
                    ${quantityFormatted}
                </fanta:dataColumn>

                <c:if test="${useNetPrice}">
                    <fanta:dataColumn name="unitPrice"
                                      styleClass="listItemRight"
                                      title="InvoicePosition.unitPrice"
                                      headerStyle="listHeader"
                                      width="20%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="unitPriceFormatted" value="${invoicePosition.unitPrice}" type="number"
                                          pattern="${numberFormat}"/>
                        ${unitPriceFormatted}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalPrice"
                                      styleClass="listItem2Right"
                                      title="InvoicePosition.totalPrice"
                                      headerStyle="listHeader"
                                      width="20%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="totalPriceFormatted" value="${invoicePosition.totalPrice}" type="number"
                                          pattern="${numberFormat}"/>
                        ${totalPriceFormatted}
                    </fanta:dataColumn>
                </c:if>

                <c:if test="${useGrossPrice}">
                    <fanta:dataColumn name="unitPriceGross"
                                      styleClass="listItemRight"
                                      title="InvoicePosition.unitPriceGross"
                                      headerStyle="listHeader"
                                      width="20%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="unitPriceGrossFormatted" value="${invoicePosition.unitPriceGross}"
                                          type="number"
                                          pattern="${numberFormat}"/>
                        ${unitPriceGrossFormatted}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalPriceGross"
                                      styleClass="listItem2Right"
                                      title="InvoicePosition.totalPriceGross"
                                      headerStyle="listHeader"
                                      width="20%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="totalPriceGrossFormatted" value="${invoicePosition.totalPriceGross}"
                                          type="number"
                                          pattern="${numberFormat}"/>
                        ${totalPriceGrossFormatted}
                    </fanta:dataColumn>
                </c:if>
            </fanta:table>
        </td>
    </tr>
</table>