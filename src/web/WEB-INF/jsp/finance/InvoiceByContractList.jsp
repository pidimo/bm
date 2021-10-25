<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<div class="${app2:getListWrapperClasses()}">
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoiceByContractList"
                     width="100%"
                     id="invoice"
                     action="InvoiceByContract/List.do"
                     imgPath="${baselayout}"
                     align="center" styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editLink"
                   value="/finance/Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>

            <c:set var="downloadAction"
                   value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoice.documentId}&dto(invoiceId)=${invoice.invoiceId}"/>

            <c:set var="deleteLink"
                   value="InvoiceByContract/Forward/Delete.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&dto(withReferences)=true"/>

            <%--address link--%>
            <tags:addressEditContextRelativeUrl varName="addressEditLink" addressId="${invoice.addressId}" addressType="${invoice.addressType}" name1="${invoice.addressName1}" name2="${invoice.addressName2}" name3="${invoice.addressName3}"/>


            <fanta:columnGroup title="Common.action"
                               headerStyle="listHeader"
                               width="5%">
                <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="33%"
                                        render="false">
                        <fmt:message key="Common.update" var="common_update" />
                        <app:link action="${editLink}" title="${common_update}" contextRelative="true">
                            <span class="${app2:getClassGlyphEdit()}"></span>
                        </app:link>
                    </fanta:actionColumn>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="INVOICE" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="33%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

                <fanta:actionColumn name="download"
                                    title="Common.download"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    width="34%"
                                    render="false">
                    <c:if test="${not empty invoice.documentId}">
                        <fmt:message key="Common.download" var="common_download" />
                        <app:link action="${downloadAction}" title="${common_download}" contextRelative="true">
                            <span class="${app2:getClassGlyphDownload()}"></span>
                        </app:link>
                    </c:if>
                </fanta:actionColumn>
            </fanta:columnGroup>

            <fanta:dataColumn name="number"
                              styleClass="listItem"
                              title="Invoice.number"
                              headerStyle="listHeader"
                              width="16%"
                              orderable="true"
                              maxLength="40"
                              renderData="false">
                <app:link action="${editLink}" contextRelative="true">
                    ${invoice.number}
                </app:link>
            </fanta:dataColumn>

            <fanta:dataColumn name="invoiceTitle" styleClass="listItem" title="Invoice.title"
                              headerStyle="listHeader"
                              width="17%"
                              orderable="true"/>

            <fanta:dataColumn name="type"
                              styleClass="listItem"
                              title="Invoice.type"
                              headerStyle="listHeader"
                              orderable="true"
                              renderData="false"
                              width="3%">
                <c:if test="${invoice.type == invoiceType}">
                    <fmt:message key="Invoice.type.invoice.abbr"/>
                </c:if>
                <c:if test="${invoice.type == creditNoteType}">
                    <fmt:message key="Invoice.type.creditNote.abbr"/>
                </c:if>
            </fanta:dataColumn>

            <fanta:dataColumn name="invoiceDate"
                              styleClass="listItem"
                              title="Invoice.invoiceDate"
                              headerStyle="listHeader"
                              width="9%"
                              orderable="true"
                              renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>

            <fanta:dataColumn name="addressName"
                              styleClass="listItem"
                              title="Invoice.contact"
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

            <fanta:dataColumn name="currencyName"
                              styleClass="listItem"
                              title="Invoice.currency"
                              headerStyle="listHeader"
                              width="8%"
                              orderable="true"/>

            <fanta:dataColumn name="totalAmountNet"
                              styleClass="listItemRight"
                              title="Invoice.totalAmountNet"
                              headerStyle="listHeader"
                              width="9%"
                              orderable="true"
                              renderData="false">
                <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountNet}
            </fanta:dataColumn>

            <fanta:dataColumn name="totalAmountGross"
                              styleClass="listItemRight"
                              title="Invoice.totalAmountGross"
                              headerStyle="listHeader"
                              width="9%"
                              orderable="true"
                              renderData="false">

                <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                  pattern="${numberFormat}"/>
                ${totalAmountGross}
            </fanta:dataColumn>

            <fanta:dataColumn name="openAmount"
                              styleClass="listItem2Right"
                              title="Invoice.openAmount"
                              headerStyle="listHeader"
                              width="9%"
                              orderable="true"
                              renderData="false">

                <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                  pattern="${numberFormat}"/>
                ${openAmount}
            </fanta:dataColumn>

        </fanta:table>
    </div>
</div>