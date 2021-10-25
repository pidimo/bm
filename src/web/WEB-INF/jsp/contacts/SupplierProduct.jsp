<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<html:form action="${action}" focus="dto(productName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="12" property="dto(save)">${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="13">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div id="SupplierProduct.jsp" class="${app2:getFormPanelClasses()}">
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(addressId)" value="${param.contactId}"/>
            <html:hidden property="dto(isProduct)" value="false"/>
            <c:if test="${('update' == op) || ('delete' == op)}">
                <html:hidden property="dto(supplierId)"/>
            </c:if>
            <c:if test="${('update' == op)}">
                <html:hidden property="dto(version)"/>
            </c:if>
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_name">
                        <fmt:message key="Product.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete' || op == 'update')}">
                        <div class="input-group">
                            <!-- search products-->
                            <app:text property="dto(productName)" styleId="field_name"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40" readonly="true" tabindex="1"
                                      view="${op == 'delete' || op == 'update'}"/>
                            <html:hidden property="dto(productId)" styleId="field_key"/>
                            <span class="input-group-btn">
                                 <tags:bootstrapSelectPopup
                                         url="/products/SearchProduct.do?contactId=${param.contactId}"
                                         styleId="SearchProduct_id"
                                         name="SearchProduct"
                                         titleKey="Common.search"
                                         modalTitleKey="Product.Title.SimpleSearch"
                                         tabindex="2"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         hide="${op != 'create'}"
                                         isLargeModal="true"/>
                                 <tags:clearBootstrapSelectPopup keyFieldId="field_key" nameFieldId="field_name"
                                                                 titleKey="Common.clear" tabindex="3"
                                                                 hide="${op != 'create'}"
                                                                 glyphiconClass="glyphicon-erase"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="contactPersonId_id">
                        <fmt:message key="SupplierProduct.ContactPerson"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <!-- search contact person -->
                        <fanta:select property="dto(contactPersonId)" styleId="contactPersonId_id"
                                      listName="searchContactPersonList"
                                      firstEmpty="true"
                                      labelProperty="contactPersonName" valueProperty="contactPersonId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}" tabIndex="4">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty param.contactId? param.contactId:0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_unitId">
                        <fmt:message key="CustomerProduct.Unitname"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <fanta:select property="dto(unitId)" styleId="field_unitId" listName="productUnitList"
                                      firstEmpty="true"
                                      labelProperty="name" valueProperty="id" module="/catalogs"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}" tabIndex="5">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="partNumber_id">
                        <fmt:message key="SupplierProduct.orderNumber"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(partNumber)" styleId="partNumber_id" view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="20" tabindex="6"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_price">
                        <fmt:message key="SupplierProduct.price"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:numberText property="dto(price)" styleId="field_price"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="12"
                                        tabindex="7"
                                        view="${'delete' == op}" numberType="decimal" maxInt="7" maxFloat="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="discount_id">
                        <fmt:message key="SupplierProduct.discount"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:numberText property="dto(discount)" styleId="discount_id"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="12"
                                        tabindex="8"
                                        view="${'delete' == op}"
                                        numberType="decimal" maxInt="7" maxFloat="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>

                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="active_id">
                        <fmt:message key="SupplierProduct.active"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(active)" styleId="active_id"
                                               tabindex="9"
                                               disabled="${op == 'delete'}"/>
                                <label for="active_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <html:hidden property="dto(numberVersion)" styleId="field_versionNumber"/>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="10" property="dto(save)">${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="11">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="supplierProductForm"/>