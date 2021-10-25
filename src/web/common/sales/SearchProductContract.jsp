<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<script language="JavaScript" type="text/javascript">
    <!--
    function select(id, name) {
        opener.selectProductContract();
        opener.selectField('fieldContractId_id', id, 'fieldContractNumber_id', name);
    }
    //-->
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="ProductContract.Title.search"/>
        </td>
    </tr>
    <TR>
        <html:form action="SearchProductContract.do?addressId=${param.addressId}&netGross=${param.netGross}"
                   focus="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)">
            <td class="label">
                <fmt:message key="Common.search"/>
            </td>
            <td class="contain" nowrap="nowrap">
                <html:hidden property="parameter(addressId)" value="${param.addressId}"/>
                <html:hidden property="parameter(netGross)" value="${param.netGross}"/>
                <html:text
                        property="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)"
                        styleClass="largeText"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
                &nbsp;
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet
                    action="/SearchProductContract.do?parameter(addressId)=${param.addressId}&addressId=${param.addressId}&parameter(netGross)=${param.netGross}&netGross=${param.netGross}"
                    parameterName="alphabetName1"/>
        </td>
    </tr>
    <TR>
        <td colspan="2" align="center">
            <br/>
            <fanta:table width="100%" id="productContract"
                         action="SearchProductContract.do?parameter(addressId)=${param.addressId}&addressId=${param.addressId}&parameter(netGross)=${param.netGross}&netGross=${param.netGross}"
                         imgPath="${baselayout}">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" title="Common.select"
                                        useJScript="true"
                                        action="javascript:select('${productContract.contractId}', '${app2:jscriptEncode(productContract.contractNumber)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="contractNumber"
                                  title="ProductContract.contractNumber"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  width="10%"/>

                <fanta:dataColumn name="addressName"
                                  title="Contract.contact"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="40"
                                  width="15%"/>
                <fanta:dataColumn name="productName"
                                  title="Contract.product"
                                  styleClass="listItem"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  maxLength="40"
                                  width="10%"/>
                <fanta:dataColumn name="price"
                                  styleClass="listItemRight"
                                  title="Contract.price"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <fmt:formatNumber var="priceFormattedValue" value="${productContract.price}" type="number"
                                      pattern="${numberFormat}"/>
                    ${priceFormattedValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount"
                                  styleClass="listItemRight"
                                  title="Contract.openAmount"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <fmt:formatNumber var="openAmountFormattedValue" value="${productContract.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    <c:if test="${productContract.payMethod != PERIODIC_PAYMETHOD}">
                        ${openAmountFormattedValue}
                    </c:if>
                </fanta:dataColumn>
                <fanta:dataColumn name="payMethod"
                                  styleClass="listItem"
                                  title="Contract.payMethod"
                                  headerStyle="listHeader"
                                  orderable="true"
                                  renderData="false"
                                  width="8%">
                    <c:set var="payMethodLabel" value="${app2:searchLabel(payMethodList, productContract.payMethod)}"/>
                    <fanta:textShorter title="${payMethodLabel}">
                        ${payMethodLabel}
                    </fanta:textShorter>
                </fanta:dataColumn>
            </fanta:table>
        </TD>
    </tr>
</table>