<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(onlyActiveContract)" value="document.getElementById('idActive').value"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
    function jumpAlphabet(obj) {
        window.location = ${jsAlphabetUrl};
    }
</script>

<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="ProductContract.singleSearch"/>
        </td>
    </tr>
    <TR>
        <html:form action="/ProductContract/List.do"
                   focus="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)">
            <td class="label"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain">
                <html:text
                        property="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)"
                        styleClass="largeText" maxlength="80"/>

                &nbsp;
                <html:select property="parameter(onlyActiveContract)" styleClass="shortSelect" styleId="idActive">
                    <html:option value=""><fmt:message key="ProductContract.filter.all"/></html:option>
                    <html:option value="true"><fmt:message key="ProductContract.filter.active"/></html:option>
                </html:select>

                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
                &nbsp;&nbsp;
                <app:link action="/ProductContract/AdvancedList.do?advancedListForward=ContractAdvancedSearch">
                    <fmt:message key="Common.advancedSearch"/>
                </app:link>
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="/ProductContract/List.do" parameterName="alphabetName1" onClick="jumpAlphabet(this);return false;"/>
        </td>
    </tr>
</table>
<br/>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td>
        <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
            <tr>
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
                    <html:form action="/ProductContract/Forward/Create.do">
                        <td class="button">
                            <%--<html:submit styleClass="button">
                                <fmt:message key="Common.new"/>
                            </html:submit>--%>
                        </td>
                    </html:form>
                </app2:checkAccessRight>
            </tr>
        </table>
    </td>
</tr>
<tr>
<td>
<fanta:table list="productContractMainSingleList"
             width="100%"
             id="productContract"
             action="ProductContract/List.do"
             imgPath="${baselayout}"
             align="center">

<c:set var="editLink"
       value="ProductContract/Forward/Update.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&index=0"/>
<c:set var="deleteLink"
       value="ProductContract/Forward/Delete.do?contractId=${productContract.contractId}&dto(contractId)=${productContract.contractId}&dto(addressName)=${app2:encode(productContract.addressName)}&dto(productName)=${app2:encode(productContract.productName)}&dto(withReferences)=true&index=0"/>

<fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
        <fanta:actionColumn name="edit" title="Common.update"
                            action="${editLink}"
                            styleClass="listItem"
                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="DELETE">
        <fanta:actionColumn name="delete" title="Common.delete"
                            action="${deleteLink}"
                            styleClass="listItem" headerStyle="listHeader" width="50%"
                            image="${baselayout}/img/delete.gif"/>
    </app2:checkAccessRight>
</fanta:columnGroup>
<fanta:dataColumn name="contractNumber"
                  action="${editLink}"
                  title="ProductContract.contractNumber"
                  styleClass="listItem"
                  headerStyle="listHeader"
                  orderable="true"
                  width="10%"/>

<fanta:dataColumn name="addressName"
                  action="${editLink}"
                  title="Contract.contact"
                  styleClass="listItem"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="40"
                  width="15%"/>
<fanta:dataColumn name="customerName"
                  title="ProductContract.customer"
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
<fanta:dataColumn name="discount"
                  styleClass="listItemRight"
                  title="Contract.discount"
                  headerStyle="listHeader"
                  orderable="true"
                  renderData="false"
                  width="5%">
    <fmt:formatNumber var="discountFormattedValue" value="${productContract.discount}" type="number"
                      pattern="${numberFormat}"/>
    ${discountFormattedValue}
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
<fanta:dataColumn name="orderDate"
                  styleClass="listItem"
                  title="Contract.orderDate"
                  headerStyle="listHeader"
                  orderable="true"
                  renderData="false"
                  width="8%">
    <fmt:formatDate var="orderDateValue" value="${app2:intToDate(productContract.orderDate)}"
                    pattern="${datePattern}"/>
    ${orderDateValue}&nbsp;
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
<fanta:dataColumn name="contractTypeName"
                  styleClass="listItem2"
                  title="Contract.contractType"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="40"
                  width="8%"/>
</fanta:table>

</td>
</tr>
<tr>
    <td>
        <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
            <tr>
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
                <html:form action="/ProductContract/Forward/Create.do">
                <td class="button">
                    <%--<html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>--%>
                </td>
                </html:form>
                </app2:checkAccessRight>
        </table>
    </td>
</tr>
</table>