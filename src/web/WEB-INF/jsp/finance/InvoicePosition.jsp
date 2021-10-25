<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>

<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="invoiceNetGross" value="${app2:getInvoiceNetGross(param.invoiceId, pageContext.request)}"/>

<c:set var="useNetPrice" value="${netPrice == invoiceNetGross}"/>

<c:set var="useGrossPrice" value="${grossPrice == invoiceNetGross}"/>

<c:set var="invoiceIsCreditNote" value="${app2:invoiceIsCreditNote(param.invoiceId, pageContext.request)}"/>

<c:set var="productSearchUrl" value="/products/SearchProduct.do"/>
<c:set var="modalTitle" value="Product.Title.SimpleSearch"/>
<c:if test="${invoiceIsCreditNote && null != invoiceDTO}">
    <c:set var="productSearchUrl"
           value="/finance/SearchInvoicePosition.do?parameter(invoiceId)=${invoiceDTO.invoiceId}"/>
    <c:set var="modalTitle" value="InvoicePosition.Title.search"/>
</c:if>


<script type="text/javascript">
    function selectInvoicePosition(invoicePositionId) {
        $("#relatedInvoicePositionId").attr("value", invoicePositionId);
        $("#selectInvoicePositionId").attr("value", "true");
    }

    function unSelectInvoicePosition() {
        var hiddenElement = $("#relatedInvoicePositionId");
        if (null != hiddenElement) {
            $("#relatedInvoicePositionId").attr("value", "");
        }
    }

    function selectProductContract() {
        $("#selectProductContractId").attr("value", "true");
    }

    function unSelectProductContract() {
        $("#unSelectProductContractId").attr("value", "true");
        document.forms[0].submit();
    }

    function selectPaymentStep() {
        $("#selectPaymentStepId").attr("value", "true");
        document.forms[0].submit();
    }
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${action}" focus="dto(number)" styleClass="form-horizontal">
        <div class="${app2:getFormClasses()}">
            <html:hidden property="dto(op)" value="${op}"/>

            <html:hidden property="dto(invoiceId)" value="${param.invoiceId}" styleId="invoiceId"/>
            <html:hidden property="dto(invoiceNetGross)" value="${invoiceNetGross}" styleId="invoiceNetGrossId"/>

            <html:hidden property="dto(discountValue)" styleId="discountValueId"/>
            <html:hidden property="dto(discount)" styleId="discountId"/>


            <c:if test="${'update' == op || 'delete' == op}">
                <html:hidden property="dto(positionId)"/>
                <html:hidden property="dto(modifiedTotalPrice)" styleId="modifiedTotalPriceId"/>
                <html:hidden property="dto(oldTotalPrice)" styleId="oldTotalPriceId"/>
                <html:hidden property="dto(oldTotalPriceGross)" styleId="oldTotalPriceGrossId"/>
                <html:hidden property="dto(oldDiscountValue)" styleId="oldDiscountValueId"/>


            </c:if>

            <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
            </c:if>

            <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
            </c:if>

            <c:if test="${invoiceIsCreditNote &&  null != invoiceDTO}">
                <html:hidden property="dto(relatedInvoicePositionId)" styleId="relatedInvoicePositionId"/>
                <html:hidden property="dto(selectInvoicePosition)" styleId="selectInvoicePositionId" value="false"/>

                <html:hidden property="dto(contractId)"/>
                <html:hidden property="dto(payStepId)"/>
                <html:hidden property="dto(oldContractId)"/>
            </c:if>

                <%-- this is hidden, fix this when it implemented to user view--%>
                <%--<c:if test="${not empty invoicePositionForm.dtoMap['discount']}">
                    <html:hidden property="dto(discount)"/>
                </c:if>--%>


            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEPOSITION"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="save"
                                     tabindex="15">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEPOSITION"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="SaveAndNew"
                                         tabindex="16">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="17">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>

            </div>
            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                        <c:out value="${title}"/>
                    </legend>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="number_id">
                            <fmt:message key="InvoicePosition.number"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:numberText property="dto(number)"
                                            styleId="number_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="10"
                                            numberType="integer"
                                            view="${'delete' == op}"
                                            tabindex="1"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                        <c:if test="${not empty invoicePositionForm.dtoMap['contractId']}">
                            <c:set var="contractEditLink"
                                   value="/sales/ProductContract/Forward/Update.do?contractId=${invoicePositionForm.dtoMap['contractId']}&dto(contractId)=${invoicePositionForm.dtoMap['contractId']}&index=0"/>
                        </c:if>
                    </app2:checkAccessRight>
                    <c:if test="${invoiceIsCreditNote && 'create' != op && not empty invoicePositionForm.dtoMap['contractId']}">
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="InvoicePosition.productContract"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <html:hidden property="dto(contractId)"/>
                                <html:hidden property="dto(contractNumber)"/>

                                <c:out value="${invoicePositionForm.dtoMap['contractNumber']}"/>

                                <c:if test="${not empty contractEditLink}">
                                    <app:link action="${contractEditLink}" styleClass="${app2:getFormButtonClasses()}"
                                              contextRelative="true" titleKey="Common.edit">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${!invoiceIsCreditNote}">
                        <c:set var="isInvoiceRelatedToCreditNote"
                               value="${app2:isInvoiceRelatedToCreditNote(param.invoiceId, pageContext.request )}"/>

                        <c:set var="productContractSearchUrl"
                               value="/sales/SearchProductContract.do?addressId=${invoiceDTO.addressId}&netGross=${invoiceDTO.netGross}&parameter(addressId)=${invoiceDTO.addressId}&parameter(netGross)=${invoiceDTO.netGross}"/>


                        <%--used to detect when user press product contract search link--%>
                        <html:hidden property="dto(selectProductContract)" styleId="selectProductContractId"
                                     value="false"/>

                        <%--used to detect when user press clear link on selected product contract--%>
                        <html:hidden property="dto(unSelectProductContract)" styleId="unSelectProductContractId"
                                     value="false"/>

                        <%--used to detect when user change payment step for some produc contract--%>
                        <html:hidden property="dto(selectPaymentStep)" styleId="selectPaymentStepId" value="false"/>

                        <%--to detect if the selected contract is partial fixed--%>
                        <html:hidden property="dto(isRelatedWithPartialFixedContract)"/>

                        <%--to detect if quantity field is enable or disable--%>
                        <html:hidden property="dto(disableQuantityField)" styleId="disableQuantityFieldId"/>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="fieldContractNumber_id">
                                <fmt:message key="InvoicePosition.productContract"/>
                            </label>

                            <div class="${app2:getFormContainClasses('delete' == op)}">
                                <html:hidden property="dto(oldContractId)"/>

                                <html:hidden property="dto(contractId)" styleId="fieldContractId_id"/>

                                <c:choose>
                                    <c:when test="${'delete' != op && true == isInvoiceRelatedToCreditNote}">
                                        <html:hidden property="dto(contractNumber)"/>
                                        <div class="form-control-static">
                                            <c:out value="${invoicePositionForm.dtoMap['contractNumber']}"/>

                                            <c:if test="${not empty contractEditLink}">
                                                    <span class="pull-right">
                                                    <app:link action="${contractEditLink}" styleClass=""
                                                              contextRelative="true" titleKey="Common.edit">
                                                        <span class="glyphicon glyphicon-edit"></span>
                                                    </app:link>
                                                </span>
                                            </c:if>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="input-group">
                                            <app:text property="dto(contractNumber)"
                                                      styleClass="${app2:getFormInputClasses()} middleText"
                                                      maxlength="40"
                                                      readonly="true"
                                                      styleId="fieldContractNumber_id"
                                                      view="${'delete' == op}"
                                                      tabindex="2"/>

                                            <span class="input-group-btn">
                                                <c:if test="${'update' == op and not empty contractEditLink}">
                                                    <app:link
                                                            action="${contractEditLink}"
                                                            styleClass="${app2:getFormButtonClasses()}"
                                                            contextRelative="true"
                                                            tabindex="2" titleKey="Common.edit">
                                                        <span class="glyphicon glyphicon-edit"></span>
                                                    </app:link>
                                                </c:if>

                                                <tags:bootstrapSelectPopup styleId="SearchContractProduct_id"
                                                                           url="${productContractSearchUrl}"
                                                                           name="SearchContractProduct"
                                                                           titleKey="Common.search"
                                                                           modalTitleKey="ProductContract.Title.search"
                                                                           hide="${'delete' == op}"
                                                                           submitOnSelect="true"
                                                                           tabindex="2"
                                                                           isLargeModal="true"/>

                                                <tags:clearBootstrapSelectPopup keyFieldId="fieldContractId_id"
                                                                                styleClass="${app2:getFormButtonClasses()}"
                                                                                nameFieldId="fieldContractNumber_id"
                                                                                titleKey="Common.clear"
                                                                                hide="${'delete' == op}"
                                                                                tabindex="2"
                                                                                onclick="javascript:unSelectProductContract();"
                                                                                glyphiconClass="glyphicon-erase"/>
                                            </span>
                                        </div>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <c:choose>
                            <c:when test="${'true' == invoicePositionForm.dtoMap['isRelatedWithPartialFixedContract']}">
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelClasses()}" for="payStepId_id">
                                        <fmt:message key="InvoicePosition.payment"/>
                                    </label>

                                        <%--<div class="${app2:getFormContainClasses('true' == invoicePositionForm.dtoMap['isRelatedWithPartialFixedContract'])}">--%>
                                    <c:choose>
                                        <c:when test="${'create' == op}">
                                            <div class="${app2:getFormContainClasses(view)}">
                                                <c:set var="unInvoicedPaymentSteps"
                                                       value="${app2:getUnInvoicedPayments(invoicePositionForm.dtoMap['contractId'], pageContext.request)}"/>

                                                <html:select property="dto(payStepId)" styleId="payStepId_id"
                                                             styleClass="${app2:getFormSelectClasses()} middleSelect"
                                                             tabindex="3"
                                                             onchange="javascript:selectPaymentStep();">
                                                    <html:option value=""/>
                                                    <html:options collection="unInvoicedPaymentSteps" property="value"
                                                                  labelProperty="label"/>
                                                </html:select>
                                                <span class="glyphicon form-control-feedback iconValidation"></span>
                                            </div>
                                        </c:when>

                                        <c:when test="${'update' == op && invoicePositionForm.dtoMap['oldContractId'] != invoicePositionForm.dtoMap['contractId']}">
                                            <div class="${app2:getFormContainClasses(view)}">
                                                <c:set var="unInvoicedPaymentSteps"
                                                       value="${app2:getUnInvoicedPayments(invoicePositionForm.dtoMap['contractId'],pageContext.request)}"/>

                                                <html:select property="dto(payStepId)" styleId="payStepId_id"
                                                             styleClass="${app2:getFormSelectClasses()} middleSelect"
                                                             tabindex="3"
                                                             onchange="javascript:selectPaymentStep();">
                                                    <html:option value=""/>
                                                    <html:options collection="unInvoicedPaymentSteps" property="value"
                                                                  labelProperty="label"/>
                                                </html:select>
                                                <span class="glyphicon form-control-feedback iconValidation"></span>
                                            </div>
                                        </c:when>

                                        <c:otherwise>
                                            <div class="${app2:getFormContainClasses(true)}">
                                                <html:hidden property="dto(payStepId)"/>
                                                <c:set var="paymentLabel"
                                                       value="${app2:getFormattedPaymentStepLabel(invoicePositionForm.dtoMap['payStepId'],pageContext.request)}"/>
                                                <c:out value="${paymentLabel}"/>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                        <%--</div>--%>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <html:hidden property="dto(payStepId)" value=""/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="fieldProductName_id">
                            <fmt:message key="InvoicePosition.product"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote))}">
                            <c:set var="showModeEditButton"
                                   value="${(not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"/>
                            <div class="${showModeEditButton ? 'row col-xs-11' : 'input-group'}">
                                <app:text property="dto(productName)"
                                          styleClass="${app2:getFormInputClasses()} middleText"
                                          maxlength="40"
                                          readonly="true"
                                          styleId="fieldProductName_id"
                                          view="${'delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"
                                          tabindex="4"/>
                                <html:hidden property="dto(productId)" styleId="fieldProductId_id"/>
                                <html:hidden property="dto(1)" styleId="field_versionNumber"/>
                                <html:hidden property="dto(2)" styleId="field_unitId"/>
                                <html:hidden property="dto(3)" styleId="field_price"/>

                                <c:if test="${showModeEditButton}">
                            </div>
                            <div>
                                </c:if>
                                <span class="${showModeEditButton ? 'pull-right' : 'input-group-btn'} ">
                                    <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                                        <c:if test="${'update' == op && not empty invoicePositionForm.dtoMap['productId']}">
                                            <c:set var="productEditLink"
                                                   value="/products/Product/Forward/Update.do?productId=${invoicePositionForm.dtoMap['productId']}&dto(productId)=${invoicePositionForm.dtoMap['productId']}&dto(productName)=${app2:encode(invoicePositionForm.dtoMap['productName'])}&index=0"/>
                                            <app:link action="${productEditLink}"
                                                      styleClass="${showModeEditButton ? '' : app2:getFormButtonClasses()}"
                                                      contextRelative="true" tabindex="4"
                                                      titleKey="Common.edit">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </app:link>
                                        </c:if>

                                    </app2:checkAccessRight>

                                    <tags:bootstrapSelectPopup styleId="SearchProduct_id" url="${productSearchUrl}"
                                                               name="SearchProduct"
                                                               titleKey="Common.search"
                                                               modalTitleKey="${modalTitle}"
                                                               submitOnSelect="true"
                                                               hide="${'delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"
                                                               tabindex="5" isLargeModal="true"/>

                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldProductId_id"
                                                                    nameFieldId="fieldProductName_id"
                                                                    styleClass="${app2:getFormButtonClasses()}"
                                                                    titleKey="Common.clear"
                                                                    submitOnClear="true"
                                                                    hide="${'delete' == op || (not empty invoicePositionForm.dtoMap['contractId'] && !invoiceIsCreditNote)}"
                                                                    onclick="javascript:unSelectInvoicePosition();"
                                                                    tabindex="5"
                                                                    glyphiconClass="glyphicon-erase"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="unit_id">
                            <fmt:message key="InvoicePosition.unit"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(unit)" styleId="unit_id"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="40"
                                      view="${'delete' == op}" tabindex="6"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <c:if test="${useNetPrice}">
                            <label class="${app2:getFormLabelClasses()}" for="unitPrice_id">
                                <fmt:message key="InvoicePosition.unitPrice"/>
                            </label>

                            <div class="${app2:getFormContainClasses('delete' == op)}">
                                <html:hidden property="dto(unitPriceGross)"/>
                                <app:numberText property="dto(unitPrice)"
                                                styleId="unitPrice_id"
                                                styleClass="${app2:getFormInputClasses()} numberText"
                                                maxlength="18"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="4"
                                                view="${'delete' == op}"
                                                tabindex="7"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:if>
                        <c:if test="${useGrossPrice}">
                            <label class="${app2:getFormLabelClasses()}" for="unitPriceGross_id">
                                <fmt:message key="InvoicePosition.unitPriceGross"/>
                            </label>

                            <div class="${app2:getFormContainClasses('delete' == op)}">
                                <html:hidden property="dto(unitPrice)"/>
                                <app:numberText property="dto(unitPriceGross)"
                                                styleId="unitPriceGross_id"
                                                styleClass="${app2:getFormInputClasses()} numberText"
                                                maxlength="18"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="4"
                                                view="${'delete' == op}"
                                                tabindex="7"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:if>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="quantity_id">
                            <fmt:message key="InvoicePosition.quantity"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op || true == invoicePositionForm.dtoMap['disableQuantityField'])}">
                            <app:numberText property="dto(quantity)"
                                            styleId="quantity_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="12"
                                            numberType="decimal"
                                            maxInt="10"
                                            maxFloat="2"
                                            view="${'delete' == op || true == invoicePositionForm.dtoMap['disableQuantityField']}"
                                            tabindex="8"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="vatId_id">
                            <fmt:message key="InvoicePosition.vat"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <fanta:select property="dto(vatId)"
                                          styleId="vatId_id"
                                          listName="vatList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleClass="${app2:getFormInputClasses()} middleSelect"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          readOnly="${'delete' == op}"
                                          tabIndex="9">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="accountId_id">
                            <fmt:message key="InvoicePosition.account"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <fanta:select property="dto(accountId)"
                                          styleId="accountId_id"
                                          listName="accountList"
                                          labelProperty="name"
                                          valueProperty="accountId"
                                          styleClass="${app2:getFormInputClasses()} middleSelect"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          readOnly="${'delete' == op}"
                                          tabIndex="10">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <c:if test="${useNetPrice}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="InvoicePosition.totalPrice"/>
                            </label>

                            <div class="${app2:getFormContainClasses(true)}">
                                <html:hidden property="dto(totalPriceGross)"/>
                                <html:hidden property="dto(totalPrice)"/>
                                <fmt:formatNumber var="totalPriceFormatted"
                                                  value="${invoicePositionForm.dtoMap['totalPrice']}"
                                                  type="number"
                                                  pattern="${numberFormat}"/>
                                    ${totalPriceFormatted}
                            </div>
                        </c:if>
                        <c:if test="${useGrossPrice}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="InvoicePosition.totalPriceGross"/>
                            </label>

                            <div class="${app2:getFormContainClasses(true)}">
                                <html:hidden property="dto(totalPrice)"/>
                                <html:hidden property="dto(totalPriceGross)"/>
                                <fmt:formatNumber var="totalPriceGrossFormatted"
                                                  value="${invoicePositionForm.dtoMap['totalPriceGross']}"
                                                  type="number"
                                                  pattern="${numberFormat}"/>
                                    ${totalPriceGrossFormatted}
                            </div>
                        </c:if>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="text_id">
                            <fmt:message key="InvoicePosition.text"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <html:textarea property="dto(text)"
                                           styleId="text_id"
                                           styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                           style="height:120px;"
                                           readonly="${'delete'== op}"
                                           tabindex="11"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>

                    </div>
                </fieldset>
            </div>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICEPOSITION"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="save"
                                     tabindex="12">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICEPOSITION"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="SaveAndNew"
                                         tabindex="13">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </html:form>
</div>
<tags:jQueryValidation formName="invoicePositionForm"/>