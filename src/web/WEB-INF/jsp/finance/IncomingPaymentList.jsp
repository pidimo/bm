<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<div class="${app2:getListWrapperClasses()}">

    <app2:checkAccessRight functionality="INCOMINGPAYMENT" permission="CREATE">
        <html:form
                action="/IncomingPayment/Forward/Create.do?incomingInvoiceId=${incomingInvoiceId}">
            <c:choose>
                <c:when test="${not dichargedIncomingInvoice}">
                    <div class="${app2:getFormButtonWrapperClasses()}">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.new"/>
                        </html:submit>
                    </div>
                </c:when>
                <c:otherwise>
                    &nbsp;&nbsp;
                </c:otherwise>
            </c:choose>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="incomingPaymentList"
                     width="100%"
                     id="incomingPayment"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="/IncomingPayment/List.do?incomingInvoiceId=${incomingInvoiceId}"
                     imgPath="${baselayout}" align="center">
            <app:url
                    value="/IncomingPayment/Forward/Update.do?dto(paymentId)=${incomingPayment.PAYMENTID}&dto(op)=read&incomingInvoiceId=${incomingInvoiceId}"
                    var="urlUpdate" enableEncodeURL="false"/>
            <app:url
                    value="/IncomingPayment/Forward/Delete.do?dto(paymentId)=${incomingPayment.PAYMENTID}&dto(op)=read&incomingInvoiceId=${incomingInvoiceId}"
                    var="urlDelete" enableEncodeURL="false"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INCOMINGPAYMENT" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INCOMINGPAYMENT" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="PAYDATE" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Finance.incomingPayment.payDate" headerStyle="listHeader" width="60%"
                              orderable="true"
                              maxLength="25" renderData="false">
                <fmt:formatDate var="payDateString" value="${app2:intToDate(incomingPayment.PAYDATE)}"
                                pattern="${datePattern}"/>
                ${payDateString}
            </fanta:dataColumn>

            <fanta:dataColumn name="AMOUNT" styleClass="listItem2Right"
                              title="Finance.incomingPayment.amount" headerStyle="listHeader" width="35%"
                              orderable="true" maxLength="15" renderData="false">
                <fmt:formatNumber var="amountNumber" value="${incomingPayment.AMOUNT}" type="number"
                                  pattern="${numberFormat}"/>
                ${amountNumber}
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="INCOMINGPAYMENT" permission="CREATE">

        <html:form
                action="/IncomingPayment/Forward/Create.do?incomingInvoiceId=${incomingInvoiceId}">
            <c:choose>
                <c:when test="${not dichargedIncomingInvoice}">
                    <div class="${app2:getFormButtonWrapperClasses()}">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.new"/>
                        </html:submit>
                    </div>
                </c:when>
                <c:otherwise>
                    &nbsp;&nbsp;
                </c:otherwise>
            </c:choose>
        </html:form>
    </app2:checkAccessRight>
</div>