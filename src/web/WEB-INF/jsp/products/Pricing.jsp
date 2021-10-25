<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(quantity)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <div id="Pricing.jsp">
            <html:hidden property="dto(unitName)"/>
            <html:hidden property="dto(productId)" value="${param.productId}"/>
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <c:if test="${'create' == op}">
                <html:hidden property="dto(productId)" value="${param.productId}"/>
            </c:if>

                <%--for the version control if update action--%>
            <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
            </c:if>
                <%--for the control referencial integrity if delete action--%>
            <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
            </c:if>

            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                        <c:out value="${title}"/>
                    </legend>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="quantity_id">
                            <fmt:message key="Product.quantity"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op || 'update'== op)}">
                            <app:text property="dto(quantity)" styleId="quantity_id" styleClass="${app2:getFormInputClasses()} numberText"
                                      style="text-align:right"
                                      maxlength="11"
                                      view="${'delete' == op || 'update'== op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="unitName_id">
                            <fmt:message key="Product.unit"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op || 'update'== op || 'create'== op)}">
                            <app:text property="dto(unitName)" styleId="unitName_id" styleClass="${app2:getFormInputClasses()} numberText"
                                      maxlength="40"
                                      view="${'delete' == op || 'update'== op || 'create'== op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="price_id">
                            <fmt:message key="Product.price"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                            <app:numberText property="dto(price)" styleId="price_id" styleClass="${app2:getFormInputClasses()} numberText"
                                            maxlength="20"
                                            view="${'delete' == op}"
                                            numberType="decimal" maxFloat="2" maxInt="10"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                </fieldset>
            </div>

        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PRICING" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                ${button}
            </app2:securitySubmit>

            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PRICING"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="pricingForm"/>
