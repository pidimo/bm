<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<script language="JavaScript" type="text/javascript">
    <!--
    function select(id, name) {
        parent.selectProductContract();
        parent.selectField('fieldContractId_id', id, 'fieldContractNumber_id', name);
    }
    //-->
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="SearchProductContract.do?addressId=${param.addressId}&netGross=${param.netGross}"
               focus="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)"
               styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:hidden property="parameter(addressId)" value="${param.addressId}"/>
                    <html:hidden property="parameter(netGross)" value="${param.netGross}"/>
                    <html:text
                            property="parameter(contractAddressName1@_contractAddressName2@_contractAddressName3@_contractNumber)"
                            styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet
                action="/SearchProductContract.do?parameter(addressId)=${param.addressId}&addressId=${param.addressId}&parameter(netGross)=${param.netGross}&netGross=${param.netGross}"
                parameterName="alphabetName1" mode="bootstrap"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%" id="productContract"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="SearchProductContract.do?parameter(addressId)=${param.addressId}&addressId=${param.addressId}&parameter(netGross)=${param.netGross}&netGross=${param.netGross}"
                     imgPath="${baselayout}">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="" title="Common.select"
                                    useJScript="true"
                                    action="javascript:select('${productContract.contractId}', '${app2:jscriptEncode(productContract.contractNumber)}');"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="contractNumber"
                              title="ProductContract.contractNumber"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              width="15%"/>

            <fanta:dataColumn name="addressName"
                              title="Contract.contact"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="20%"/>
            <fanta:dataColumn name="productName"
                              title="Contract.product"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="40"
                              width="20%"/>
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
                              width="24%">
                <c:set var="payMethodLabel" value="${app2:searchLabel(payMethodList, productContract.payMethod)}"/>
                <fanta:textShorter title="${payMethodLabel}">
                    ${payMethodLabel}
                </fanta:textShorter>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>