<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="creditNoteIsRelatedWithInvoice"
       value="${app2:creditNoteIsRelatedWithInvoice(param.invoiceId, pageContext.request)}"/>

<div class="${app2:getListWrapperClasses()}">

    <app2:checkAccessRight functionality="INVOICEPAYMENT" permission="CREATE">
        <c:if test="${!app2:hasZeroOpenAmount(param.invoiceId, pageContext.request)}">
            <html:form action="/InvoicePayment/Forward/Create.do">
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </div>
            </html:form>
        </c:if>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoicePaymentList"
                     width="100%"
                     id="invoicePayment"
                     action="InvoicePayment/List.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editLink"
                   value="InvoicePayment/Forward/Update.do?paymentId=${invoicePayment.paymentId}&dto(paymentId)=${invoicePayment.paymentId}"/>
            <c:set var="deleteLink"
                   value="/finance/InvoicePayment/Forward/Delete.do?paymentId=${invoicePayment.paymentId}&dto(paymentId)=${invoicePayment.paymentId}&dto(withReferences)=true"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INVOICEPAYMENT" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICEPAYMENT" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        render="false">
                        <c:choose>
                            <c:when test="${creditNoteIsRelatedWithInvoice}">
                                &nbsp;
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${empty invoicePayment.creditNoteId}">
                                        <fmt:message key="Common.delete" var="common_delete"/>
                                        <app:link action="${deleteLink}" title="${common_delete}"
                                                  contextRelative="true">
                                            <span class="${app2:getClassGlyphTrash()}"></span>
                                        </app:link>
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="payDate" action="${editLink}"
                              styleClass="listItem" title="InvoicePayment.payDate"
                              headerStyle="listHeader" width="70%" orderable="true"
                              maxLength="70" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(invoicePayment.payDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="amount" styleClass="listItem2Right" title="InvoicePayment.amount"
                              headerStyle="listHeader"
                              width="25%" orderable="true" renderData="false">
                <fmt:formatNumber var="amountFormatted" value="${invoicePayment.amount}" type="number"
                                  pattern="${numberFormat}"/>
                ${amountFormatted}
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="INVOICEPAYMENT" permission="CREATE">
        <c:if test="${!app2:hasZeroOpenAmount(param.invoiceId, pageContext.request)}">
            <html:form action="/InvoicePayment/Forward/Create.do">
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </div>
            </html:form>
        </c:if>
    </app2:checkAccessRight>

</div>