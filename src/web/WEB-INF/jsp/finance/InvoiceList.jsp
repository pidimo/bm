<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="/Invoice/SingleList.do"
                   focus="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3)"
                   styleClass="form-horizontal">
            <fieldset>
                <legend class="title" colspan="2">
                    <fmt:message key="Invoice.singleSearch"/>
                </legend>
                <div class="${app2:getSearchWrapperClasses()}">
                    <label class="${app2:getFormLabelOneSearchInput()} label-left">
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
                    <div class="pull-left">
                        <app:link action="/Invoice/AdvancedList.do?advancedListForward=InvoiceAdvancedSearch"
                                  styleClass="btn btn-link">
                            <fmt:message key="Common.advancedSearch"/>
                        </app:link>
                    </div>
                </div>
            </fieldset>
        </html:form>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/Invoice/SingleList.do" parameterName="alphabetName1" mode="bootstrap"/>
        </div>
    </div>

    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
        <html:form action="/Invoice/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoiceSingleList"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
                     width="100%"
                     id="invoice"
                     action="Invoice/SingleList.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>
            <c:set var="deleteLink"
                   value="Invoice/Forward/Delete.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&dto(withReferences)=true&index=0"/>
            <c:set var="downloadAction"
                   value="Download/Invoice/Document.do?dto(freeTextId)=${invoice.documentId}&dto(invoiceId)=${invoice.invoiceId}"/>

            <%--address link--%>
            <c:choose>
                <c:when test="${personType == invoice.addressType}">
                    <c:set var="addressEditLink"
                           value="/contacts/Person/Forward/Update.do?contactId=${invoice.addressId}&dto(addressId)=${invoice.addressId}&dto(addressType)=${invoice.addressType}&dto(name1)=${app2:encode(invoice.addressName1)}&dto(name2)=${app2:encode(invoice.addressName2)}&dto(name3)=${app2:encode(invoice.addressName3)}&index=0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="addressEditLink"
                           value="/contacts/Organization/Forward/Update.do?contactId=${invoice.addressId}&dto(addressId)=${invoice.addressId}&dto(addressType)=${invoice.addressType}&dto(name1)=${app2:encode(invoice.addressName1)}&dto(name2)=${app2:encode(invoice.addressName2)}&dto(name3)=${app2:encode(invoice.addressName3)}&index=0"/>
                </c:otherwise>
            </c:choose>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="33%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download" title="Common.download"
                                    action="${downloadAction}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="34%"
                                    render="${not empty invoice.documentId}"
                                    glyphiconClass="${app2:getClassGlyphDownload()}"/>

            </fanta:columnGroup>
            <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem" title="Invoice.number"
                              headerStyle="listHeader"
                              width="16%"
                              orderable="true"
                              maxLength="40"/>
            <fanta:dataColumn name="invoiceTitle" styleClass="listItem" title="Invoice.title"
                              headerStyle="listHeader"
                              width="17%"
                              orderable="true"/>
            <fanta:dataColumn name="type" styleClass="listItem" title="Invoice.type"
                              headerStyle="listHeader" orderable="true" renderData="false" width="3%">
                <c:if test="${invoice.type == invoiceType}">
                    <fmt:message key="Invoice.type.invoice.abbr"/>
                </c:if>
                <c:if test="${invoice.type == creditNoteType}">
                    <fmt:message key="Invoice.type.creditNote.abbr"/>
                </c:if>
            </fanta:dataColumn>
            <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              renderData="false">
                <fanta:textShorter title="${invoice.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${invoice.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                              headerStyle="listHeader"
                              width="8%"
                              orderable="true"/>
            <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountNet}
            </fanta:dataColumn>
            <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight"
                              title="Invoice.totalAmountGross"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountGross}
            </fanta:dataColumn>
            <fanta:dataColumn name="openAmount" styleClass="listItem2Right" title="Invoice.openAmount"
                              headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                  pattern="${numberFormat}"/>
                ${openAmount}
            </fanta:dataColumn>
        </fanta:table>

    </div>

    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
        <html:form action="/Invoice/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

</div>