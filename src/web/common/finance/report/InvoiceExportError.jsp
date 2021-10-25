<%@ include file="/Includes.jsp" %>

<br>
<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="${action}">
        <c:if test="${not empty customerWithoutNumberList}">
            <tr>
                <td height="20" class="title">
                    <fmt:message key="InvoiceExport.customerWithoutNumber.title"/>
                </td>
            </tr>
            <c:forEach var="customerName" items="${customerWithoutNumberList}">
                <tr>
                    <td class="label">
                        <c:out value="${customerName}"/>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        <c:if test="${not empty sequenceRuleWithoutPartnerList}">
            <tr>
                <td height="20" class="title">
                    <fmt:message key="InvoiceExport.ruleWithoutPartner.title"/>
                </td>
            </tr>
            <c:forEach var="ruleLabel" items="${sequenceRuleWithoutPartnerList}">
                <tr>
                    <td class="label">
                        <c:out value="${ruleLabel}"/>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        <c:if test="${not empty productWithoutAccountList}">
            <tr>
                <td height="20" class="title">
                    <fmt:message key="InvoiceExport.productWithoutAccount.title"/>
                </td>
            </tr>
            <c:forEach var="productName" items="${productWithoutAccountList}">
                <tr>
                    <td class="label">
                        <c:out value="${productName}"/>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        <c:if test="${not empty bankAccountWithoutAccountList}">
            <tr>
                <td height="20" class="title">
                    <fmt:message key="InvoiceExport.bankAccountWithoutAccount.title"/>
                </td>
            </tr>
            <c:forEach var="bankAccountLabel" items="${bankAccountWithoutAccountList}">
                <tr>
                    <td class="label">
                        <c:out value="${bankAccountLabel}"/>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        <c:if test="${not empty paymentWithoutBankAccountList}">
            <tr>
                <td height="20" class="title">
                    <fmt:message key="InvoiceExport.paymentWithoutBankAccount.title"/>
                </td>
            </tr>
            <c:forEach var="invoiceNumber" items="${paymentWithoutBankAccountList}">
                <tr>
                    <td class="label">
                        <c:out value="${invoiceNumber}"/>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        <TR>
            <TD class="button">
                <html:cancel styleClass="button" tabindex="1">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </TR>
    </html:form>
</table>