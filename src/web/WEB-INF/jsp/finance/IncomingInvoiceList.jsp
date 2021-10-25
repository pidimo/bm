<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<c:set var="INVOICE_TYPE" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="/IncomingInvoice/List.do" focus="parameter(invoiceNumber@_supplierName1@_supplierName2)"
                   styleClass="form-horizontal">
            <fieldset>
                <legend class="title">
                    <fmt:message key="${pagetitle}"/>
                </legend>
                <div class="${app2:getSearchWrapperClasses()}">
                    <label class="${app2:getFormLabelOneSearchInput()} label-left">
                        <fmt:message key="Common.search"/>
                    </label>

                    <div class="${app2:getFormOneSearchInput()}">
                        <div class="input-group">
                            <html:text property="parameter(invoiceNumber@_supplierName1@_supplierName2)"
                                       styleClass="${app2:getFormInputClasses()} largeText" maxlength="80"/>
                            <span class="input-group-btn">
                               <html:submit styleClass="${app2:getFormButtonClasses()}">
                                   <fmt:message key="Common.go"/>
                               </html:submit>
                            </span>
                        </div>
                    </div>

                    <c:if test="${fromFinance}">
                        <div class="pull-left">
                            <app:link
                                    action="/IncomingInvoice/AdvancedSearch.do?advancedListForward=IncomingInvoiceAdvancedSearch"
                                    styleClass="btn btn-link">
                                <fmt:message key="Common.advancedSearch"/>
                            </app:link>
                        </div>
                    </c:if>

                </div>
            </fieldset>
        </html:form>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/IncomingInvoice/List.do" parameterName="invoiceNumber" mode="bootstrap"/>
        </div>
    </div>

    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="CREATE">
        <html:form action="/IncomingInvoice/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="incomingInvoiceList" width="100%"
                     styleClass="${app2:getFantabulousTableLargeClases()}" id="incomingInvoice"
                     action="/IncomingInvoice/List.do"
                     imgPath="${baselayout}" align="center">
            <c:set value="/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}"
                   var="urlUpdate"/>
            <c:set value="/IncomingInvoice/Forward/Delete.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}"
                   var="urlDelete"/>

            <c:set var="downloadAction"
                   value="/finance/Download/IncomingInvoice/Document.do?dto(freeTextId)=${incomingInvoice.DOCUMENTID}&dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}"/>

            <%--address link--%>
            <tags:addressEditContextRelativeUrl varName="supplierEditLink" addressId="${incomingInvoice.supplierAddressId}" addressType="${incomingInvoice.supplierAddressType}" name1="${incomingInvoice.supplierName1}" name2="${incomingInvoice.supplierName2}" name3="${incomingInvoice.supplierName3}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        width="33%" glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        width="33%" glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download" title="Common.download"
                                    styleClass="listItem" headerStyle="listHeader" width="34%"
                                    render="false">
                    <c:choose>
                        <c:when test="${not empty incomingInvoice.DOCUMENTID}">
                            <app:link action="${downloadAction}" contextRelative="true" titleKey="Common.download">
                                <span class="${app2:getClassGlyphDownload()}"></span>
                            </app:link>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </fanta:actionColumn>
            </fanta:columnGroup>
            <fanta:dataColumn name="INVOICENUMBER" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingInvoice.invoiceNumber" headerStyle="listHeader" width="14%"
                              orderable="true" maxLength="40"/>

            <fanta:dataColumn name="TYPE" styleClass="listItem"
                              title="Finance.incomingInvoice.type" headerStyle="listHeader" width="5%"
                              orderable="true" renderData="false">
                <c:choose>

                    <c:when test="${incomingInvoice.TYPE==INVOICE_TYPE}">
                        <fmt:message key="Invoice.type.invoice.abbr"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="Invoice.type.creditNote.abbr"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>

            <fanta:dataColumn name="INVOICEDATE" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingInvoice.invoiceDate" headerStyle="listHeader" width="10%"
                              orderable="true"
                              renderData="false">
                <fmt:formatDate var="invoiceDateString" value="${app2:intToDate(incomingInvoice.INVOICEDATE)}"
                                pattern="${datePattern}"/>
                ${invoiceDateString}
            </fanta:dataColumn>

            <fanta:dataColumn name="RECEIPTDATE" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingInvoice.receiptDate" headerStyle="listHeader" width="11%"
                              orderable="true"
                              renderData="false">
                <fmt:formatDate var="receiptDateString" value="${app2:intToDate(incomingInvoice.RECEIPTDATE)}"
                                pattern="${datePattern}"/>
                ${receiptDateString}
            </fanta:dataColumn>

            <fanta:dataColumn name="SUPPLIERNAME" styleClass="listItem"
                              title="Finance.incomingInvoice.supplierName" headerStyle="listHeader" width="15%"
                              orderable="true" renderData="false">
                <fanta:textShorter title="${incomingInvoice.SUPPLIERNAME}">
                    <app:link action="${supplierEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${incomingInvoice.SUPPLIERNAME}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>

            <fanta:dataColumn name="CURRENCYLABEL" styleClass="listItem"
                              title="Finance.incomingInvoice.currency"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="true"/>

            <fanta:dataColumn name="AMOUNTNET" styleClass="listItemRight"
                              title="Finance.incomingInvoice.amountNet" headerStyle="listHeader" width="10%"
                              orderable="true" renderData="false">
                <fmt:formatNumber var="amountNetNumber" value="${incomingInvoice.AMOUNTNET}" type="number"
                                  pattern="${numberFormat}"/>
                ${amountNetNumber}
            </fanta:dataColumn>

            <fanta:dataColumn name="AMOUNTGROSS" styleClass="listItemRight"
                              title="Finance.incomingInvoice.amountGross" headerStyle="listHeader" width="10%"
                              orderable="true" renderData="false">
                <fmt:formatNumber var="amountGrossNumber" value="${incomingInvoice.AMOUNTGROSS}" type="number"
                                  pattern="${numberFormat}"/>
                ${amountGrossNumber}
            </fanta:dataColumn>

            <fanta:dataColumn name="OPENAMOUNT" styleClass="listItem2Right"
                              title="Finance.incomingInvoice.openAmount" headerStyle="listHeader" width="10%"
                              orderable="true" renderData="false">
                <fmt:formatNumber var="openAmountNumber" value="${incomingInvoice.OPENAMOUNT}" type="number"
                                  pattern="${numberFormat}"/>
                ${openAmountNumber}
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="CREATE">
        <html:form action="/IncomingInvoice/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>