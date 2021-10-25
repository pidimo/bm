<%@ include file="/Includes.jsp" %>

<div class="${app2:getFormClasses()}">

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>

            <legend class="title">
                <fmt:message key="Invoice.sendViaEmail.summary"/>
            </legend>

            <div class="${app2:getFormGroupClasses()} row">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Invoice.sendViaEmail.summary.totalProcessed"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <c:out value="${sendInvoicesSummaryMap.total}"/>
                </div>
            </div>

            <div class="${app2:getFormGroupClasses()} row">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Invoice.sendViaEmail.summary.success"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <c:out value="${sendInvoicesSummaryMap.success}"/>
                </div>
            </div>

            <div class="${app2:getFormGroupClasses()} row">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Invoice.sendViaEmail.summary.fail"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <c:out value="${sendInvoicesSummaryMap.fail}"/>
                </div>
            </div>

            <c:if test="${not empty sendInvoicesSummaryMap.errorMessageList}">
                <legend class="title">
                    <fmt:message key="Invoice.sendViaEmail.summary.failMessages"/>
                </legend>
                <c:forEach var="item" items="${sendInvoicesSummaryMap.errorMessageList}">
                    <c:out value="${item}"/><br/>
                </c:forEach>
            </c:if>
        </fieldset>
    </div>

</div>