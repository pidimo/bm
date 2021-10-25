<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(payConditionName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(payConditionId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="payConditionName_id">
                        <fmt:message key="PayCondition.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(payConditionName)" styleId="payConditionName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="40"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="payDays_id">
                        <fmt:message key="PayCondition.PayDays"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(payDays)" styleId="payDays_id"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="4" view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="payDaysDiscount_id">
                        <fmt:message key="PayCondition.payDaysDiscount"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(payDaysDiscount)" styleId="payDaysDiscount_id"
                                  styleClass="${app2:getFormInputClasses()} numberText" maxlength="4"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="discount_id">
                        <fmt:message key="PayCondition.Discount"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(discount)"
                                        styleId="discount_id"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="8"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        maxInt="3"
                                        maxFloat="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${'create' == op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="PayCondition.languageForTexts"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <fanta:select property="dto(languageId)" styleId="languageId_id" listName="languageBaseList"
                                          labelProperty="name" valueProperty="id" firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()}" readOnly="${'delete' == op}">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="topLabel ${app2:getFormLabelClasses()}" for="payConditionText_id">
                            <fmt:message key="PayConditionText.text"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <html:textarea property="dto(payConditionText)"
                                           styleId="payConditionText_id"
                                           styleClass="${app2:getFormInputClasses()} middleDetail"
                                           readonly="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PAYCONDITION"
                                 styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PAYCONDITION"
                                     styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="payConditionForm"/>