<%@ include file="/Includes.jsp" %>

<html:form action="${action}" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <c:if test="${not empty customerWithoutNumberList}">
                    <legend class="title">
                        <fmt:message key="InvoiceExport.customerWithoutNumber.title"/>
                    </legend>

                    <c:forEach var="customerName" items="${customerWithoutNumberList}">
                        <h5>
                            <c:out value="${customerName}"/>
                        </h5>
                    </c:forEach>
                </c:if>

                <c:if test="${not empty sequenceRuleWithoutPartnerList}">
                    <legend class="title">
                        <fmt:message key="InvoiceExport.ruleWithoutPartner.title"/>
                    </legend>

                    <c:forEach var="ruleLabel" items="${sequenceRuleWithoutPartnerList}">
                        <h5>
                            <c:out value="${ruleLabel}"/>
                        </h5>
                    </c:forEach>
                </c:if>

                <c:if test="${not empty productWithoutAccountList}">
                    <legend class="title">
                        <fmt:message key="InvoiceExport.productWithoutAccount.title"/>
                    </legend>

                    <c:forEach var="productName" items="${productWithoutAccountList}">
                        <h5>
                            <c:out value="${productName}"/>
                        </h5>
                    </c:forEach>
                </c:if>

                <c:if test="${not empty bankAccountWithoutAccountList}">
                    <legend class="title">
                        <fmt:message key="InvoiceExport.bankAccountWithoutAccount.title"/>
                    </legend>

                    <c:forEach var="bankAccountLabel" items="${bankAccountWithoutAccountList}">
                        <h5>
                            <c:out value="${bankAccountLabel}"/>
                        </h5>
                    </c:forEach>
                </c:if>

                <c:if test="${not empty paymentWithoutBankAccountList}">
                    <legend class="title">
                        <fmt:message key="InvoiceExport.paymentWithoutBankAccount.title"/>
                    </legend>

                    <c:forEach var="invoiceNumber" items="${paymentWithoutBankAccountList}">
                        <h5>
                            <c:out value="${invoiceNumber}"/>
                        </h5>

                    </c:forEach>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="1">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
