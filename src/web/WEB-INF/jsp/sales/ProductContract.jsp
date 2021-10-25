<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/cacheable/bootstrap/sexycombojqueryplugin/jquery.bgiframe.3.0.1.js"/>
<tags:jscript language="JavaScript"
              src="/js/cacheable/bootstrap/sexycombojqueryplugin/jquery-jatun-sexy-combo-1.0.4.1.js"/>

<script type="text/javascript">
    function reSubmit() {
        document.forms[0].submit();
    }
</script>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<!--define module name to build AJAX url-->
<c:if test="${empty fromModule}">
    <c:set var="fromModule" value="sales" scope="request"/>
</c:if>

<!--set view only address field-->
<c:if test="${empty readOnlyForAddress}">
    <c:set var="readOnlyForAddress" value="false" scope="request"/>
</c:if>

<!--if contract is related with saleposition and sale, show this information in page-->
<c:if test="${empty showSaleInformation}">
    <c:set var="showSaleInformation" value="false" scope="request"/>
</c:if>
<!--url to show saleposition related with product-->
<c:if test="${empty productSalePositionAction}">
    <c:set var="productSalePositionAction" value="products/FromSaleModule/SalePosition/Forward/Update.do"
           scope="request"/>
</c:if>

<c:if test="${empty saleTabKey}">
    <c:set var="saleTabKey" value="Sale.Tab.Detail" scope="request"/>
</c:if>

<c:if test="${empty salePositionTabKey}">
    <c:set var="salePositionTabKey" value="Sale.Tab.SalePosition" scope="request"/>
</c:if>

<c:set var="single" value="<%=SalesConstants.PayMethod.Single.getConstantAsString()%>"/>
<c:set var="periodic" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>
<c:set var="partialPeriodic" value="<%=SalesConstants.PayMethod.PartialPeriodic.getConstantAsString()%>"/>
<c:set var="partialFixed" value="<%=SalesConstants.PayMethod.PartialFixed.getConstantAsString()%>"/>

<c:set var="amount" value="<%=SalesConstants.AmounType.AMOUNT.getConstantAsString()%>"/>
<c:set var="percentage" value="<%=SalesConstants.AmounType.PERCENTAGE.getConstantAsString()%>"/>

<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
<c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>

<div class="col-xs-12">
    <html:form action="${action}"
               focus="dto(contractNumber)"
               styleClass="form-horizontal"
               styleId="mainFormId">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(salePositionId)" value="${productContractForm.dtoMap['salePositionId']}"/>

        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(contractId)"/>
            <html:hidden property="dto(hasInvoicePositions)" styleId="hasInvoicePositionsId"/>
            <html:hidden property="dto(totalPaid)" styleId="totalPaidId"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="PRODUCTCONTRACT"
                                 styleClass="${app2:getFormButtonClasses()}" property="save" tabindex="103">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="104">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

            <c:set var="isContractCancelable" value="${'update' == op and not productContractForm.dtoMap['cancelledContract'] and (single == productContractForm.dtoMap['payMethod'] || partialFixed == productContractForm.dtoMap['payMethod'] || partialPeriodic == productContractForm.dtoMap['payMethod'])}"/>

            <c:if test="${isContractCancelable}">
                <app2:securitySubmit operation="UPDATE"
                                     functionality="PRODUCTCONTRACT"
                                     styleClass="${app2:getFormButtonClasses()}" property="cancelContractButton" tabindex="105">
                    <fmt:message key="ProductContract.button.cancelContract"/>
                </app2:securitySubmit>
            </c:if>
        </div>

        <c:set var="isReadOnly" value="${'delete' == op || productContractForm.dtoMap['cancelledContract']}"/>

        <div class="${app2:getFormPanelClasses()}">

            <c:set var="relatedWithSale"
                   value="${null != productContractForm.dtoMap['saleId'] && not empty productContractForm.dtoMap['saleId']}"/>
            <c:set var="relatedWithSalePosition"
                   value="${null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId']}"/>

            <c:if test="${'true' == showSaleInformation && relatedWithSalePosition}">
                <html:hidden property="dto(salePositionId)"/>
                <html:hidden property="dto(salePositionCustomerId)"/>
                <html:hidden property="dto(productName)"/>
                <html:hidden property="dto(customerName)" styleId="customerName"/>
                <fieldset>
                    <legend class="title">
                        <fmt:message key="ProductContract.title.saleInformation"/>
                    </legend>

                    <c:choose>
                        <c:when test="${relatedWithSale}">
                            <html:hidden property="dto(saleId)"/>
                            <html:hidden property="dto(saleName)"/>

                            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                                <div class="form-group col-xs-12 paddingRemove">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="ProductContract.sale"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        <c:out value="${productContractForm.dtoMap['saleName']}"/>

                                        <c:if test="${app2:hasAccessRight(pageContext.request,'SALE' ,'VIEW' )}">
                                            <app:link contextRelative="true" styleClass="pull-right"
                                                      titleKey="Common.edit"
                                                      action="${fromModule}/Sale/Forward/Update.do?contactId=${productContractForm.dtoMap['salePositionCustomerId']}&saleId=${productContractForm.dtoMap['saleId']}&dto(saleId)=${productContractForm.dtoMap['saleId']}&dto(title)=${app2:encode(productContractForm.dtoMap['saleName'])}&tabKey=${saleTabKey}">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </app:link>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="form-group col-xs-12 paddingRemove">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="ProductContract.customer"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        <c:out value="${productContractForm.dtoMap['customerName']}"/>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                                <div class="form-group col-xs-12 paddingRemove">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="ProductContract.salePosition"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        <c:out value="${productContractForm.dtoMap['productName']}"/>

                                        <c:if test="${app2:hasAccessRight(pageContext.request,'SALEPOSITION' ,'VIEW' )}">
                                            <app:link contextRelative="true" titleKey="Common.edit"
                                                      styleClass="pull-right"
                                                      action="${fromModule}/SalePosition/Forward/Update.do?contactId=${productContractForm.dtoMap['salePositionCustomerId']}&saleId=${productContractForm.dtoMap['saleId']}&dto(saleId)=${productContractForm.dtoMap['saleId']}&dto(title)=${app2:encode(productContractForm.dtoMap['saleName'])}&salePositionId=${productContractForm.dtoMap['salePositionId']}&dto(salePositionId)=${productContractForm.dtoMap['salePositionId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&tabKey=${salePositionTabKey}">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </app:link>
                                        </c:if>
                                    </div>
                                </div>
                            </div>

                        </c:when>

                        <c:otherwise>
                            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                                <div class="form-group col-xs-12 paddingRemove">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="ProductContract.salePosition"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        <c:out value="${productContractForm.dtoMap['productName']}"/>

                                        <c:choose>
                                            <c:when test="${'sales' == fromModule && !relatedWithSale && app2:hasAccessRight(pageContext.request,'SALEPOSITION' ,'VIEW' ) && app2:hasAccessRight(pageContext.request,'PRODUCT' ,'VIEW' )}">
                                                <app:link contextRelative="true" styleClass="pull-right"
                                                          titleKey="Common.edit"
                                                          action="${productSalePositionAction}?contractId=${productContractForm.dtoMap['contractId']}&dto(contractId)=${productContractForm.dtoMap['contractId']}&productId=${productContractForm.dtoMap['productId']}&dto(productId)=${productContractForm.dtoMap['productId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&salePositionId=${productContractForm.dtoMap['salePositionId']}&dto(salePositionId)=${productContractForm.dtoMap['salePositionId']}&dto(customerName)=${app2:encode(productContractForm.dtoMap['customerName'])}&tabKey=Product.Tab.SalePositions">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                </app:link>
                                            </c:when>
                                            <c:when test="${'sales' != fromModule && !relatedWithSale && app2:hasAccessRight(pageContext.request,'SALEPOSITION' ,'VIEW' )}">
                                                <app:link contextRelative="true" styleClass="pull-right"
                                                          titleKey="Common.edit"
                                                          action="${fromModule}/SalePosition/Forward/Update.do?saleId=${productContractForm.dtoMap['saleId']}&dto(saleId)=${productContractForm.dtoMap['saleId']}&dto(title)=${app2:encode(productContractForm.dtoMap['saleName'])}&salePositionId=${productContractForm.dtoMap['salePositionId']}&dto(salePositionId)=${productContractForm.dtoMap['salePositionId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&tabKey=${salePositionTabKey}">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                </app:link>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                                <div class="form-group col-xs-12 paddingRemove">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="ProductContract.customer"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        <c:out value="${productContractForm.dtoMap['customerName']}"/>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </fieldset>
            </c:if>
            <fieldset>

                <legend class="title">
                    <div id="ajaxMessageId" class="messageToolTip" style="display:none; position:absolute; left:7%">
                        <fmt:message key="Common.message.loading"/>
                    </div>

                    <c:out value="${title}"/>
                </legend>


                <c:set var="isPeriodic" value="${periodic == productContractForm.dtoMap['payMethod']}" scope="request"/>
                <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractNumber_id">
                            <fmt:message key="ProductContract.contractNumber"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <app:text property="dto(contractNumber)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      styleId="contractNumber_id"
                                      view="${isReadOnly}"
                                      tabindex="1"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Contract.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == readOnlyForAddress)}">

                            <c:if test="${not empty productContractForm.dtoMap['addressId'] and 'update' == op}">
                                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                                    <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
                                    </c:set>
                                    <c:set var="addressMap"
                                           value="${app2:getAddressMap(productContractForm.dtoMap['addressId'])}"/>
                                    <c:choose>
                                        <c:when test="${personType == addressMap['addressType']}">
                                            <c:set var="addressEditLink"
                                                   value="/contacts/Person/Forward/Update.do?contactId=${productContractForm.dtoMap['addressId']}&dto(addressId)=${productContractForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                        </c:when>

                                        <c:otherwise>
                                            <c:set var="addressEditLink"
                                                   value="/contacts/Organization/Forward/Update.do?contactId=${productContractForm.dtoMap['addressId']}&dto(addressId)=${productContractForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                        </c:otherwise>
                                    </c:choose>
                                </app2:checkAccessRight>
                            </c:if>

                            <c:if test="${!(isReadOnly || 'true' == readOnlyForAddress)}">
                            <div class="input-group">
                                </c:if>

                                <app:text property="dto(addressName)"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40"
                                          readonly="true"
                                          styleId="fieldAddressName_id"
                                          view="${isReadOnly || 'true' == readOnlyForAddress}"
                                          tabindex="2"/>

                                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>

                                <c:if test="${!(isReadOnly || 'true' == readOnlyForAddress)}">
                    <span class="input-group-btn">
                </c:if>
                    <c:if test="${not empty addressEditLink && not isReadOnly}">
                        <%--change class icon view--%>
                        <c:if test="${isReadOnly || 'true' == readOnlyForAddress}">
                            <app:link action="${addressEditLink}"
                                      titleKey="Common.edit"
                                      contextRelative="true"
                                      tabindex="2"
                                      styleClass="pull-right">
                                <span class="glyphicon glyphicon-edit"></span>
                            </app:link>

                        </c:if>

                        <c:if test="${!(isReadOnly || 'true' == readOnlyForAddress)}">
                            <app:link action="${addressEditLink}"
                                      titleKey="Common.edit"
                                      contextRelative="true"
                                      tabindex="2"
                                      styleClass="${app2:getFormButtonClasses()}">
                                <span class="glyphicon glyphicon-edit"></span>
                            </app:link>
                        </c:if>

                    </c:if>

                    <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                               name="searchAddress"
                                               styleId="contactSelectPopup_id"
                                               styleClass="${app2:getFormButtonClasses()}"
                                               titleKey="Common.search"
                                               modalTitleKey="Contact.Title.search"
                                               submitOnSelect="true"
                                               isLargeModal="true"
                                               hide="${isReadOnly || 'true' == readOnlyForAddress}"
                                               tabindex="3"/>

                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                    nameFieldId="fieldAddressName_id"
                                                    name="searchAddress"
                                                    titleKey="Common.clear"
                                                    submitOnClear="true"
                                                    styleClass="${app2:getFormButtonClasses()}"
                                                    hide="${isReadOnly || 'true' == readOnlyForAddress}"
                                                    tabindex="3"/>
                <c:if test="${!(isReadOnly || 'true' == readOnlyForAddress)}">
                    </span>
                                </c:if>


                                <c:if test="${!(isReadOnly || 'true' == readOnlyForAddress)}">
                            </div>
                            </c:if>

                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                            <fmt:message key="ProductContract.contactPerson"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(contactPersonId)"
                                          listName="searchContactPersonList"
                                          module="/contacts"
                                          firstEmpty="true"
                                          styleId="contactPersonId_id"
                                          labelProperty="contactPersonName"
                                          valueProperty="contactPersonId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${isReadOnly}"
                                          tabIndex="3">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty productContractForm.dtoMap['addressId']?productContractForm.dtoMap['addressId']:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentAddressId_id">
                            <fmt:message key="ProductContract.sentAddress"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(sentAddressId)"
                                          listName="invoiceAddressRelationList"
                                          labelProperty="relatedAddressName"
                                          valueProperty="relatedAddressId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${isReadOnly}"
                                          styleId="sentAddressId_id"
                                          onChange="reSubmit()"
                                          module="/contacts" tabIndex="4" firstEmpty="true">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty productContractForm.dtoMap['addressId'] ? productContractForm.dtoMap['addressId'] : 0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentContactPersonId_id">
                            <fmt:message key="ProductContract.sentContactPerson"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                                          module="/contacts" firstEmpty="true"
                                          labelProperty="contactPersonName"
                                          valueProperty="contactPersonId"
                                          styleId="sentContactPersonId_id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          tabIndex="5"
                                          readOnly="${isReadOnly}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty productContractForm.dtoMap['sentAddressId'] ? productContractForm.dtoMap['sentAddressId'] : 0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
                            <fmt:message key="ProductContract.additionalAddress"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(additionalAddressId)"
                                          listName="additionalAddressSelectList"
                                          labelProperty="name"
                                          valueProperty="additionalAddressId"
                                          module="/contacts"
                                          styleId="additionalAddressId_id"
                                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${isReadOnly}"
                                          tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty productContractForm.dtoMap['addressId'] ? productContractForm.dtoMap['addressId'] : 0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractTypeId_id">
                            <fmt:message key="Contract.contractType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(contractTypeId)"
                                          listName="contractTypeList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          styleId="contractTypeId_id"
                                          valueProperty="contractTypeId"
                                          module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${isReadOnly}"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldProductName_id">
                            <fmt:message key="Contract.product"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId']))}">
                            <html:hidden property="dto(productId)" styleId="fieldProductId_id"/>
                            <html:hidden property="dto(productVersionNumber)" styleId="field_versionNumber"/>
                            <html:hidden property="dto(productUnitId)" styleId="field_unitId"/>

                            <app:text property="dto(productName)"
                                      styleClass="mediumText"
                                      maxlength="40"
                                      readonly="true"
                                      styleId="fieldProductName_id"
                                      view="${isReadOnly || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId'])}"
                                      tabindex="7"/>

                            <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                                <c:if test="${'update' == op && not empty productContractForm.dtoMap['productId'] && not isReadOnly}">
                                    <c:set var="productEditLink"
                                           value="/products/Product/Forward/Update.do?productId=${productContractForm.dtoMap['productId']}&dto(productId)=${productContractForm.dtoMap['productId']}&dto(productName)=${app2:encode(productContractForm.dtoMap['productName'])}&index=0"/>
                                    <app:link action="${productEditLink}" contextRelative="true" titleKey="Common.edit"
                                              styleClass="pull-right">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:if>
                            </app2:checkAccessRight>

                            <tags:selectPopup url="/products/SearchProduct.do"
                                              name="SearchProduct"
                                              titleKey="Common.search"
                                              hide="${isReadOnly || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId'])}"
                                              tabindex="7"/>
                            <tags:clearSelectPopup keyFieldId="fieldProductId_id"
                                                   nameFieldId="fieldProductName_id"
                                                   titleKey="Common.clear"
                                                   hide="${isReadOnly || (null != productContractForm.dtoMap['salePositionId'] && not empty productContractForm.dtoMap['salePositionId'])}"
                                                   tabindex="7"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sellerId_id">
                            <fmt:message key="ProductContract.seller"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(sellerId)"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          firstEmpty="true"
                                          module="/contacts"
                                          styleId="sellerId_id"
                                          readOnly="${isReadOnly}"
                                          tabIndex="8">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payMethodId">
                            <fmt:message key="Contract.payMethod"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == productContractForm.dtoMap['hasInvoicePositions'])}">
                            <!--is used in form to detect if paymethod has changed-->
                            <html:hidden property="dto(changePayMethod)" value="false" styleId="changePayMethodId"/>

                            <c:if test="${'true' == productContractForm.dtoMap['hasInvoicePositions']}">
                                <html:hidden property="dto(payMethodBK)"
                                             value="${productContractForm.dtoMap['payMethod']}"
                                             styleId="payMethodId"/>
                            </c:if>

                            <html:select property="dto(payMethod)"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         styleId="payMethodId"
                                         readonly="${isReadOnly || 'true' == productContractForm.dtoMap['hasInvoicePositions']}"
                                         onchange="javascript:selectPayMethodUI(this);"
                                         tabindex="9">
                                <html:option value=""/>
                                <html:options collection="payMethodList"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="netGross_id">
                            <fmt:message key="ProductContract.netGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions']))}">
                            <html:hidden property="dto(changeNetGross)" value="false" styleId="changeNetGrossId"/>

                            <html:select property="dto(netGross)"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         readonly="${isReadOnly || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'])}"
                                         tabindex="10"
                                         styleId="netGross_id"
                                         onchange="javascript:selectNetGross();">
                                <html:option value=""/>
                                <html:options collection="netGrossOptions"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${isPeriodic}">
                            <div class="form-group col-xs-12 paddingRemove">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_price">
                                    <fmt:message key="Contract.periodicPrice"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(isReadOnly  || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod']))}">
                                    <app:numberText property="dto(price)"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_price"
                                                    view="${isReadOnly  || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod'])}"
                                                    tabindex="11"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>

                            <div class="form-group col-xs-12 paddingRemove">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="pricePeriod_id">
                                    <fmt:message key="ProductContract.pricePeriod"/>
                                    <c:out value="("/><fmt:message key="ProductContract.months"/><c:out value=")"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                                    <app:numberText property="dto(pricePeriod)"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="2"
                                                    styleId="pricePeriod_id"
                                                    numberType="integer"
                                                    view="${isReadOnly}"
                                                    tabindex="11"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>

                            <div class="form-group col-xs-12 paddingRemove">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="pricePerMonth_id">
                                    <fmt:message key="ProductContract.pricePerMonth"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <app:numberText property="dto(pricePerMonth)"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    styleId="pricePerMonth_id"
                                                    tabindex="5"
                                                    maxFloat="2"
                                                    view="true"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="form-group col-xs-12 paddingRemove">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_price">
                                    <fmt:message key="Contract.price"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(isReadOnly  || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod']))}">
                                    <app:numberText property="dto(price)"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleId="field_price"
                                                    view="${isReadOnly  || ('update' == op && 'true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod'])}"
                                                    tabindex="11"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="discount_id">
                            <fmt:message key="Contract.discount"/>
                            <c:choose>
                                <c:when test="${'create' != op && null != productContractForm.dtoMap['discount'] && not empty productContractForm.dtoMap['discount']}">
                                    <c:out value="("/><c:out value="%"/><c:out value=")"/>
                                </c:when>
                                <c:when test="${'create' == op}">
                                    <c:out value="("/><c:out value="%"/><c:out value=")"/>
                                </c:when>
                                <c:otherwise>
                                    &nbsp;
                                </c:otherwise>
                            </c:choose>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || ('true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod']))}">
                            <app:numberText property="dto(discount)"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="12"
                                            numberType="decimal"
                                            maxInt="10"
                                            styleId="discount_id"
                                            maxFloat="2"
                                            view="${isReadOnly || ('true' == productContractForm.dtoMap['hasInvoicePositions'] && single == productContractForm.dtoMap['payMethod'])}"
                                            tabindex="12"/>

                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                            <fmt:message key="ProductContract.currency"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(currencyId)"
                                          listName="basicCurrencyList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleId="currencyId_id"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          readOnly="${isReadOnly}"
                                          tabIndex="13">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="vatId_id">
                            <fmt:message key="ProductContract.vat"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(vatId)"
                                          listName="vatList"
                                          labelProperty="name"
                                          styleId="vatId_id"
                                          valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          readOnly="${isReadOnly}"
                                          tabIndex="14">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payConditionId">
                            <fmt:message key="ProductContract.payCondition"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <fanta:select property="dto(payConditionId)"
                                          listName="payConditionList"
                                          styleId="payConditionId"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          firstEmpty="true"
                                          module="/catalogs"
                                          readOnly="${isReadOnly}"
                                          tabIndex="15">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-12 paddingRemove">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                            <fmt:message key="Contract.orderDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                            <div class="input-group date">
                                <app:dateText property="dto(orderDate)"
                                              styleId="startDate"
                                              calendarPicker="${op != 'delete'}"
                                              datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} text"
                                              maxlength="10"
                                              currentDate="${'create' == op}"
                                              view="${isReadOnly}"
                                              mode="bootstrap"
                                              tabindex="16"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div id="paymentOptionsId" class="col-xs-12 paddingRemove">
                </div>

                <div class="col-xs-12 col-sm-12">
                    <div class="form-group">
                        <label class="control-label col-xs-12 col-sm-12 label-left row">
                            <fmt:message key="Contract.note"/>
                        </label>

                        <div class="col-xs-12 col-sm-12 row">
                            <html:textarea property="dto(noteText)"
                                           styleClass="form-control middleDetail"
                                           tabindex="100"
                                           style="height:120px;width:100%;"
                                           readonly="${isReadOnly}"/>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="PRODUCTCONTRACT"
                                 styleClass="${app2:getFormButtonClasses()}" property="save" tabindex="101">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="102">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

            <c:if test="${isContractCancelable}">
                <app2:securitySubmit operation="UPDATE"
                                     functionality="PRODUCTCONTRACT"
                                     styleClass="${app2:getFormButtonClasses()}" property="cancelContractButton" tabindex="102">
                    <fmt:message key="ProductContract.button.cancelContract"/>
                </app2:securitySubmit>
            </c:if>
        </div>

        <c:set var="parameters" value="${app2:buildParametersForAjaxRequest(productContractForm)}"/>
        <app2:jScriptUrl url="/${fromModule}/ProductContract/PaymentOptions.do?op=${op}" var="jsReadPayMethodUrl"
                         addModuleParams="false">
            <app2:jScriptUrlParam param="payMethod" value="payMethodId"/>
        </app2:jScriptUrl>

        <app2:jScriptUrl url="/${fromModule}/ProductContract/PaymentOptions.do?op=${op}" var="jsAddRowUrl"
                         addModuleParams="false">
            <app2:jScriptUrlParam param="payMethod" value="payMethodId"/>
            <app2:jScriptUrlParam param="installment" value="installmentId"/>
            <app2:jScriptUrlParam param="amounType" value="amounTypeId"/>
            <app2:jScriptUrlParam param="payConditionId" value="payConditionId"/>
        </app2:jScriptUrl>
        <script type="text/javascript">

            function makePOSTAjaxRequest(urlAddress, parameters) {
                $.ajax({
                    async: true,
                    type: "POST",
                    dataType: "html",
                    data: parameters,
                    url: urlAddress,
                    beforeSend: setLoadMessage,
                    success: function (data) {
                        document.getElementById('paymentOptionsId').innerHTML = data;
                        var messagesDiv = document.getElementById('ajaxMessageId');
                        messagesDiv.style.display = 'none';
                        setSexyGroupingCombo();
                        enableDatePicker();
                    },
                    error: function (ajaxRequest) {
                        ajaxErrorProcess(ajaxRequest.status);
                    }
                });
            }

            function firstExecution() {
                var payMethodId = '${productContractForm.dtoMap['payMethod']}';
                makePOSTAjaxRequest(${jsReadPayMethodUrl}, ${parameters});
            }

            function selectPayMethodUI(obj) {
                document.getElementById('changePayMethodId').value = 'true';
                document.getElementById('mainFormId').submit();
            }

            function selectNetGross() {
                document.getElementById('changeNetGrossId').value = 'true';
                document.getElementById('mainFormId').submit();
            }

            function setLoadMessage() {
                var messagesDiv = document.getElementById('ajaxMessageId');
                messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.message.loading',pageContext.request)}';
                messagesDiv.style.display = 'inline';
            }

            function ajaxErrorProcess(status) {
                var messagesDiv = document.getElementById('ajaxMessageId');
                if (status == 404 || status == 302) {
                    messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.sessionExpired',pageContext.request)}';
                } else {
                    messagesDiv.innerHTML = '${app2:buildAJAXMessage('error.tooltip.unexpected',pageContext.request)}';
                }
                messagesDiv.style.display = 'inline';
            }

            function addRow(obj) {
                var installmentId = new Number(obj.value);
                var payMethodId = document.getElementById('payMethodId').value;
                var amounTypeId = document.getElementById('amounTypeId').value;
                var payConditionId = document.getElementById('payConditionId').value;
                var steptsInvoicedCounterId = document.getElementById('steptsInvoicedCounterId').value;

                if (installmentId < steptsInvoicedCounterId)
                    installmentId = steptsInvoicedCounterId;

                if (installmentId.toString().indexOf("NaN") == -1) {
                    var jsParams = buildJsParameters(installmentId);

                    if ('' != jsParams) {
                        makePOSTAjaxRequest(${jsAddRowUrl}, jsParams + ${parameters});
                    } else
                        makePOSTAjaxRequest(${jsAddRowUrl}, ${parameters});
                }
            }

            function showPercentageSymbol(obj) {
                var amounTypeId = obj.value;
                var payMethodId = document.getElementById('payMethodId').value;
                var installmentId = new Number(document.getElementById('installmentId').value);
                var payConditionId = document.getElementById('payConditionId').value;

                if (installmentId > 0) {
                    var jsParams = buildJsParameters(installmentId);

                    if ('' != jsParams) {
                        makePOSTAjaxRequest(${jsAddRowUrl}, jsParams + ${parameters});
                    } else
                        makePOSTAjaxRequest(${jsAddRowUrl}, ${parameters});
                }
            }
            function buildJsParameters(counter) {
                var params = '';
                for (var i = 1; i <= counter; i++) {
                    var amountIdentifier = 'payAmountId_' + i;
                    var dateIdentifier = 'payDateId_' + i;

                    if (null != document.getElementById(amountIdentifier)) {
                        var payAmount_i = document.getElementById(amountIdentifier);
                        if ('' != payAmount_i.value) {
                            params = params + 'payAmount_' + i + '=' + encodeURI(payAmount_i.value) + '&';
                        }

                        var payDate_i = document.getElementById(dateIdentifier);
                        if ('' != payDate_i.value) {
                            params = params + 'payDate_' + i + '=' + encodeURI(payDate_i.value) + '&';
                        }
                    }
                }
                return params;
            }

            function setSexyGroupingCombo() {
                $("#groupingId").sexyCombo({
                    <c:if test="${not empty productContractForm.dtoMap['grouping']}">
                    defaultValue: "${productContractForm.dtoMap['grouping']}",
                    </c:if>
                    suffix: "dto(grouping)",
                    maxlength: "50",
                    hiddenSuffix: "dto(groupingHidden)"
                });

            }

            firstExecution();
        </script>
    </html:form>
</div>

<tags:jQueryValidation formName="productContractForm"/>

