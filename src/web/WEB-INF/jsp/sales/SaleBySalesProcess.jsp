<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<fmt:message var="datePattern" key="datePattern"/>

<script language="JavaScript" type="text/javascript">
    function changeAction() {
        document.forms[0].submit();
    }
</script>

<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(processId)"/>

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
        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                     tabindex="13">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="${app2:getFormButtonInlineClasses()}"
                                     tabindex="14">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:set var="invoiceInfo"
                               value="${app2:getOnlyOneSaleInvoiceInfoMap(saleBySalesProcessForm.dtoMap['saleId'], pageContext.request)}"/>

                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <app:url var="urlSaleInvoiceList"
                                         value="/Sale/Invoiced/List.do?tabKey=Sale.Tab.saleInvoices"/>
                                <html:button property="" styleClass="${app2:getFormButtonInlineClasses()}" tabindex="14"
                                             onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <app:url var="urlDownloadDocument"
                                         value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoiceInfo.documentId}&dto(invoiceId)=${invoiceInfo.invoiceId}"
                                         contextRelative="true"/>
                                <html:button property="" styleClass="${app2:getFormButtonInlineClasses()}" tabindex="14"
                                             onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel styleClass="${app2:getFormButtonCancelInlineClasses()}" tabindex="15">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="title_id">
                        <fmt:message key="Sale.title"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(title)"
                                  styleId="title_id"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="40"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="contactId_id">
                        <fmt:message key="Sale.salesProcessAction"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || 'update' == op)}">
                        <fanta:select property="dto(contactId)"
                                      styleId="contactId_id"
                                      listName="actionSelectList"
                                      module="/sales"
                                      firstEmpty="true"
                                      labelProperty="note"
                                      valueProperty="contactId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op || 'update' == op}"
                                      onChange="javascript:changeAction();">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="processId" value="${saleBySalesProcessForm.dtoMap['processId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="fieldAddressName_id">
                        <fmt:message key="Sale.customer"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                        <app:text property="dto(addressName)"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="40"
                                  readonly="true"
                                  styleId="fieldAddressName_id"
                                  view="true"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="contactPersonId_id">
                        <fmt:message key="Sale.contactPerson"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(contactPersonId)"
                                      styleId="contactPersonId_id"
                                      listName="searchContactPersonList"
                                      module="/contacts" firstEmpty="true"
                                      labelProperty="contactPersonName"
                                      valueProperty="contactPersonId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleBySalesProcessForm.dtoMap['addressId']?saleBySalesProcessForm.dtoMap['addressId']:0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sellerId_id">
                        <fmt:message key="Sale.seller"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(sellerId)"
                                      styleId="sellerId_id"
                                      listName="employeeBaseList"
                                      labelProperty="employeeName"
                                      valueProperty="employeeId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      value="${sessionScope.user.valueMap['userAddressId']}"
                                      module="/contacts"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sentAddressId_id">
                        <fmt:message key="Sale.sentAddress"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(sentAddressId)"
                                      styleId="sentAddressId_id"
                                      listName="invoiceAddressRelationList"
                                      labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'}"
                                      onChange="javascript:changeAction();"
                                      module="/contacts" firstEmpty="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleBySalesProcessForm.dtoMap['addressId'] ? saleBySalesProcessForm.dtoMap['addressId'] : 0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="sentContactPersonId_id">
                        <fmt:message key="Sale.sentContactPerson"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(sentContactPersonId)"
                                      styleId="sentContactPersonId_id"
                                      listName="searchContactPersonList"
                                      module="/contacts" firstEmpty="true"
                                      labelProperty="contactPersonName"
                                      valueProperty="contactPersonId"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleBySalesProcessForm.dtoMap['sentAddressId'] ? saleBySalesProcessForm.dtoMap['sentAddressId'] : 0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="additionalAddressId_id">
                        <fmt:message key="Sale.additionalAddress"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(additionalAddressId)"
                                      styleId="additionalAddressId_id"
                                      listName="additionalAddressSelectList"
                                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                      firstEmpty="true" styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty saleBySalesProcessForm.dtoMap['addressId'] ? saleBySalesProcessForm.dtoMap['addressId'] : 0}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="startDate">
                        <fmt:message key="Sale.saleDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == o)}">
                        <div class="input-group date">
                            <app:dateText property="dto(saleDate)"
                                          styleId="startDate"
                                          calendarPicker="${op != 'delete'}"
                                          mode="bootstrap"
                                          datePatternKey="${datePattern}"
                                          styleClass="text ${app2:getFormInputClasses()}"
                                          maxlength="10"
                                          currentDate="true"
                                          view="${'delete' == op}"/>
                        </div>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="currencyId_id">
                        <fmt:message key="Sale.currency"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(currencyId)"
                                      styleId="currencyId_id"
                                      listName="basicCurrencyList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      module="/catalogs"
                                      readOnly="${op == 'delete'}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="netGross_id">
                        <fmt:message key="Sale.netGross"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
                        <html:select property="dto(netGross)"
                                     styleId="netGross_id"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     readonly="${'delete' == op}">
                            <html:option value=""/>
                            <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="text_id">
                        <fmt:message key="Sale.text"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <html:textarea property="dto(text)"
                                       styleId="text_id"
                                       styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                       rows="5"
                                       readonly="${'delete'== op}"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="SALE" property="save"
                                     styleClass="${app2:getFormButtonInlineClasses()}">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${'update' == op}">
                    <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                        <html:submit property="invoiceFromSale" styleClass="${app2:getFormButtonInlineClasses()}">
                            <fmt:message key="Sale.invoice"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <c:choose>
                            <c:when test="${invoiceInfo.saleInvoiceSize > 1}">
                                <html:button property="" styleClass="${app2:getFormButtonInlineClasses()}"
                                             onclick="location.href='${urlSaleInvoiceList}'">
                                    <fmt:message key="Sale.viewInvoices"/>
                                </html:button>
                            </c:when>
                            <c:when test="${not empty invoiceInfo.documentId}">
                                <html:button property="" styleClass="${app2:getFormButtonInlineClasses()}"
                                             onclick="location.href='${urlDownloadDocument}'">
                                    <fmt:message key="Sale.openInvoice"/>
                                </html:button>
                            </c:when>
                        </c:choose>
                    </app2:checkAccessRight>

                </c:if>

                <html:cancel styleClass="${app2:getFormButtonCancelInlineClasses()}">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>

<c:if test="${'update' == op}">
    <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
        <div class="embed-responsive embed-responsive-16by9 col-xs-12">
            <iframe class="embed-responsive-item" name="frame1"
                    src="<app:url value="SalesProcess/SalePosition/List.do?processId=${saleBySalesProcessForm.dtoMap['processId']}&saleId=${saleBySalesProcessForm.dtoMap['saleId']}"/>"
                    class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </div>
    </app2:checkAccessRight>
</c:if>

<tags:jQueryValidation formName="saleBySalesProcessForm"/>