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
        parent.selectInvoicePosition(invoicePositionId);
        parent.selectField('fieldProductId_id', id, 'fieldProductName_id', name);
    }
    //-->
</script>

<html:form action="/SearchInvoicePosition.do?parameter(invoiceId)=${param['parameter(invoiceId)']}"
           focus="parameter(productName)" styleClass="form-horizontal">
    <div class="${app2:getSearchWrapperClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(productName)" styleClass="${app2:getFormInputClasses()} largeText"/>
                <div class="input-group-btn">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
        </div>
    </div>
</html:form>


<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="SearchInvoicePosition.do?parameter(invoiceId)=${param['parameter(invoiceId)']}"
                    parameterName="productNameAlpha" mode="bootstrap"/>
</div>

<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%" id="invoicePosition"
                 styleClass="${app2:getFantabulousTableClases()}"
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
                                glyphiconClass="${app2:getClassGlyphImport()}"/>
        </fanta:columnGroup>
        <fanta:dataColumn name="number"
                          styleClass="listItem"
                          title="InvoicePosition.number"
                          headerStyle="listHeader"
                          width="10%"
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
</div>