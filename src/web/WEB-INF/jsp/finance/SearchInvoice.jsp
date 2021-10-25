<%@ include file="/Includes.jsp" %>

<c:set var="url" value="/Invoice/Search.do?chekIfButtonPressed=${param.chekIfButtonPressed}"/>

<%--add openAmount parameter--%>
<c:if test="${null != param['parameter(openAmount)'] && '' != param['parameter(openAmount)']}">
    <c:set var="url" value="${url}&parameter(openAmount)=${param['parameter(openAmount)']}"/>
</c:if>

<%--add type parameter --%>
<c:if test="${null != param['parameter(type)'] && '' != param['parameter(type)']}">
    <c:set var="url" value="${url}&parameter(type)=${param['parameter(type)']}"/>
</c:if>

<c:if test="${null != param['parameter(addressId)'] && '' != param['parameter(addressId)']}">
    <c:set var="url" value="${url}&parameter(addressId)=${param['parameter(addressId)']}"/>
</c:if>

<%--add sendAdditionalInfo parameter--%>
<c:if test="${not empty param['sendAdditionalInfo']}">
    <c:set var="url" value="${url}&sendAdditionalInfo=${param['sendAdditionalInfo']}"/>
</c:if>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<script language="JavaScript" type="text/javascript">
    <!--
    function select(id, name, totalAmountNet, totalAmountGross, openAmount, documentId) {
        <c:if test="${'true' == param.chekIfButtonPressed}">
        parent.copyOfButtonPressed();
        </c:if>

        <c:if test="${'true' == param.sendAdditionalInfo}">
        parent.setAdditionalInvoiceInfo(totalAmountNet, totalAmountGross, openAmount, documentId);
        </c:if>

        parent.selectField('fieldInvoiceId_id', id, 'fieldInvoiceNumber_id', name);
    }
    //-->
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${url}"
               focus="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3)"
               styleClass="form-horizontal">
        <div class="${app2:getFormGroupClasses()}">
            <label class="control-label col-xs-2 label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text
                            property="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3)"
                            styleClass="${app2:getFormInputClasses()} largeText" maxlength="80"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div ${app2:getAlphabetWrapperClasses()}>
        <fanta:alphabet action="${url}" parameterName="alphabetName1" mode="bootstrap"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoiceSearchList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="invoice"
                     action="Invoice/Search.do?chekIfButtonPressed=${param.chekIfButtonPressed}&sendAdditionalInfo=${param.sendAdditionalInfo}"
                     imgPath="${baselayout}"
                     align="center">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                    action="javascript:select('${invoice.invoiceId}', '${app2:jscriptEncode(invoice.number)}', '${invoice.totalAmountNet}', '${invoice.totalAmountGross}', '${invoice.openAmount}', '${invoice.documentId}');"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="100%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="number" styleClass="listItem" title="Invoice.number"
                              headerStyle="listHeader" orderable="true" maxLength="40" width="18%"/>
            <fanta:dataColumn name="invoiceTitle" styleClass="listItem" title="Invoice.title"
                              headerStyle="listHeader" orderable="true" width="13%"/>
            <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                              headerStyle="listHeader" orderable="true" renderData="false" width="10%">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                              headerStyle="listHeader"
                              width="13%" orderable="true"/>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Invoice.contactPerson"
                              headerStyle="listHeader" width="13%" orderable="true"/>
            <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                              headerStyle="listHeader"
                              width="8%" orderable="true"/>
            <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountNet}
            </fanta:dataColumn>
            <fanta:dataColumn name="totalAmountGross" styleClass="listItem2Right" title="Invoice.totalAmountGross"
                              headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountGross}
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>