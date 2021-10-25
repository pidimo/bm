<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
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
    <div class="${app2:getFormClasses()}">
        <div class="row">
            <div class="col-xs-12 ">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     tabindex="15">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="16">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:set var="invoiceInfo"
                               value="${app2:getOnlyOneSaleInvoiceInfoMap(saleForm.dtoMap['saleId'], pageContext.request)}"/>

                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <app:url var="urlSaleInvoiceList"
                                         value="/sales/Sale/Invoiced/List.do?saleId=${saleForm.dtoMap['saleId']}&tabKey=Sale.Tab.saleInvoices"
                                         contextRelative="true"/>
                                <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="17"
                                             onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <app:url var="urlDownloadDocument"
                                         value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceInfo.invoiceId}"
                                         contextRelative="true"/>
                                <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="17"
                                             onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="18">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <c:set var="showSalesProcessOption" value="${false}" scope="request"/>
                <c:set var="readOnlySalesProcessOption" value="${true}" scope="request"/>
                <c:if test="${null != saleForm.dtoMap['contactId'] && null != saleForm.dtoMap['processId']}">
                    <c:set var="showSalesProcessOption" value="${true}" scope="request"/>
                </c:if>
                <c:set var="saleObject" value="${saleForm}" scope="request"/>
                <c:import url="/WEB-INF/jsp/contacts/SaleFragment.jsp"/>
            </fieldset>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     tabindex="11">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="12">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="13"
                                             onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton" tabindex="13"
                                             onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>

<c:if test="${'update' == op}">
    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <iframe name="frame1"
                src="<app:url value="SalePositionBySale/List.do?saleId=${saleForm.dtoMap['saleId']}&dto(saleId)=${saleForm.dtoMap['saleId']}"/>"
                class="embed-responsive-item Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>
</c:if>
<tags:jQueryValidation formName="saleForm"/>
