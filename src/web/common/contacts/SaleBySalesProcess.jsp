<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(title)">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(addressId)" value="${param.contactId}" styleId="d"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(processId)"/>

    <c:if test="${'update'== op || 'delete'== op}">
        <html:hidden property="dto(saleId)"/>

        <html:hidden property="dto(showNetGrossWarningMessage)" styleId="showNetGrossWarningMessageId"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                                     tabindex="13">
                    ${button}
                </app2:securitySubmit>
                <html:cancel styleClass="button" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="title" width="100%">
                <c:out value="${title}"/>
            </td>
        </tr>
        <c:set var="saleObject" value="${saleBySalesProcessForm}" scope="request"/>
        <c:set var="showSalesProcessOption" value="${true}" scope="request"/>
        <c:set var="readOnlySalesProcessOption" value="${'delete' == op || 'update' == op}" scope="request"/>
        <c:import url="/common/contacts/SaleFragment.jsp"/>
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                                     tabindex="11">
                    ${button}
                </app2:securitySubmit>
                <html:cancel styleClass="button" tabindex="12">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>