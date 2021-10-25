<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
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

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="13">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <c:set var="saleObject" value="${saleBySalesProcessForm}" scope="request"/>
                <c:set var="showSalesProcessOption" value="${true}" scope="request"/>
                <c:set var="readOnlySalesProcessOption" value="${'delete' == op || 'update' == op}"
                       scope="request"/>
                <c:import url="/WEB-INF/jsp/contacts/SaleFragment.jsp"/>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="11">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
