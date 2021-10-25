<%@ include file="/Includes.jsp" %>
<html:form action="${action}" focus="dto(title)">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(addressId)" value="${param.contactId}" styleId="d"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(isSaleFromContact)" value="true"/>

    <c:if test="${'update'== op || 'delete'== op}">
        <html:hidden property="dto(showNetGrossWarningMessage)" styleId="showNetGrossWarningMessageId"/>
        <html:hidden property="dto(saleId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <c:if test="${null != saleForm.dtoMap['processId']}">
        <html:hidden property="dto(processId)"/>
    </c:if>

    <c:if test="${null != saleForm.dtoMap['contactId']}">
        <html:hidden property="dto(contactId)"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                                     tabindex="15">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="button" tabindex="16">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:set var="invoiceInfo" value="${app2:getOnlyOneSaleInvoiceInfoMap(saleForm.dtoMap['saleId'], pageContext.request)}"/>

                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <app:url var="urlSaleInvoiceList" value="/sales/Sale/Invoiced/List.do?saleId=${saleForm.dtoMap['saleId']}&tabKey=Sale.Tab.saleInvoices" contextRelative="true"/>
                                <html:button property="" styleClass="button" tabindex="17" onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <app:url var="urlDownloadDocument"
                                         value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceInfo.invoiceId}" contextRelative="true"/>
                                <html:button property="" styleClass="button" tabindex="17" onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel styleClass="button" tabindex="18">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="title" width="100%">
                <c:out value="${title}"/>
            </td>
        </tr>
        <c:set var="showSalesProcessOption" value="${false}" scope="request"/>
        <c:set var="readOnlySalesProcessOption" value="${true}" scope="request"/>
        <c:if test="${null != saleForm.dtoMap['contactId'] && null != saleForm.dtoMap['processId']}">
            <c:set var="showSalesProcessOption" value="${true}" scope="request"/>
        </c:if>
        <c:set var="saleObject" value="${saleForm}" scope="request"/>
        <c:import url="/common/contacts/SaleFragment.jsp"/>
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save" styleClass="button"
                                     tabindex="11">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="button" tabindex="12">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <html:button property="" styleClass="button" tabindex="13" onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <html:button property="" styleClass="button" tabindex="13" onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel styleClass="button" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

    <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
        <c:if test="${'update' == op}">
            <iframe name="frame1"
                    src="<app:url value="SalePositionBySale/List.do?saleId=${saleForm.dtoMap['saleId']}&dto(saleId)=${saleForm.dtoMap['saleId']}"/>"
                    class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </c:if>
    </app2:checkAccessRight>


</html:form>