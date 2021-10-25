<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>

<script type="text/javascript">
    function submitForm(obj) {
        document.forms[0].submit();

    }
</script>

<c:choose>
    <c:when test="${op == 'delete'}">
        <c:set var="setFocus" value="dto(supplierName)"/>
    </c:when>
    <c:otherwise>
        <c:set var="setFocus" value="dto(contactPersonId)"/>
    </c:otherwise>
</c:choose>
<html:form action="${action}" focus="${setFocus}" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'add'}">
                <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="CREATE">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" styleId="8">
                        <fmt:message key="Common.save"/>
                    </html:submit>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op != 'add'}">
                <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="dto(save)" tabindex="8">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="9">
                <fmt:message
                        key="Common.cancel"/>
            </html:cancel>
        </div>


        <div id="Supplier.jsp">
            <html:hidden property="dto(productId)" value="${param.productId}"/>
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(isProduct)" value="true"/>

                <%--for the version control if update action--%>
            <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
                <html:hidden property="dto(supplierId)"/>
            </c:if>
                <%--for the control referencial integrity if delete action--%>
            <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
                <html:hidden property="dto(supplierId)"/>
            </c:if>
            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                        <c:out value="${title}"/>
                    </legend>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="fieldAddressName_id">
                            <fmt:message key="Supplier"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(op == 'delete' || op == 'update')}">

                                <%--<fanta:select property="dto(supplierId)" listName="supplierSearchList"
                                              labelProperty="supplierName" valueProperty="supplierId" styleClass="mediumSelect"
                                              readOnly="${op == 'delete' || op == 'update'}"
                                              module="/products" tabIndex="1" firstEmpty="true" styleId="fieldAddressId_id" onChange="submitForm(this);">
                                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>--%>

                            <div class="input-group">
                                <app:text property="dto(supplierName)" styleId="fieldAddressName_id"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          readonly="true" tabindex="1" view="${op == 'delete' || op == 'update'}"/>
                                <html:hidden property="dto(supplierId)" styleId="fieldAddressId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup styleId="SearchSupplier_id"
                                                               url="/contacts/SearchAddress.do?allowCreation=2"
                                                               name="SearchSupplier"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Contact.Title.search"
                                                               hide="${op != 'create'}"
                                                               submitOnSelect="true" isLargeModal="true" tabindex="1"/>

                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                    nameFieldId="fieldAddressName_id"
                                                                    titleKey="Common.clear" hide="${op != 'create'}"
                                                                    submitOnClear="true" tabindex="1"/>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="contactPersonId_id">
                            <fmt:message key="ContactPerson"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                            <fanta:select property="dto(contactPersonId)" styleId="contactPersonId_id" tabIndex="1"
                                          listName="searchContactPersonList"
                                          module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                                          valueProperty="contactPersonId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId"
                                                 value="${not empty supplierForm.dtoMap['supplierId']?supplierForm.dtoMap['supplierId']:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="unitId_id">
                            <fmt:message key="Product.unit"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                            <fanta:select property="dto(unitId)" styleId="unitId_id" listName="productUnitList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}" tabIndex="2">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="partNumber_id">
                            <fmt:message key="SupplierProduct.orderNumber"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                            <app:text property="dto(partNumber)" styleId="partNumber_id"
                                      styleClass="${app2:getFormInputClasses()} numberText"
                                      maxlength="40"
                                      view="${'delete' == op }"
                                      tabindex="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="price_id">
                            <fmt:message key="SupplierProduct.price"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                            <app:numberText property="dto(price)" styleId="price_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="20"
                                            view="${'delete' == op}"
                                            numberType="decimal" maxInt="10" maxFloat="2" tabindex="3"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="discount_id">
                            <fmt:message key="SupplierProduct.discount"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                            <app:numberText property="dto(discount)" styleId="discount_id"
                                            styleClass="${app2:getFormInputClasses()} numberText" maxlength="20"
                                            view="${'delete' == op}"
                                            numberType="decimal" maxInt="10" maxFloat="2" tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <c:if test="${op != 'add'}">
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelRenderCategory()}">
                                <fmt:message key="Common.active"/>
                            </label>

                            <div class="${app2:getFormContainRenderCategory(view)}">
                                <div class="radiocheck">
                                    <div class="checkbox checkbox-default">
                                        <html:checkbox property="dto(active)" styleId="active_id"
                                                       disabled="${op == 'delete'}"
                                                       tabindex="5"/>
                                        <label for="active_id"></label>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                </fieldset>
            </div>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'add'}">
                <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="CREATE">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="6">
                        <fmt:message key="Common.save"/>
                    </html:submit>
                </app2:checkAccessRight>
            </c:if>
            <c:if test="${op != 'add'}">
                <app2:securitySubmit operation="${op}" functionality="PRODUCTSUPPLIER"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="dto(save)" tabindex="6">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="supplierForm"/>