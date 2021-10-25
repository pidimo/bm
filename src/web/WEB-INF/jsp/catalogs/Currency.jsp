<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(currencyName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

                    <%--if update action or delete action--%>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(currencyId)"/>
                </c:if>

                    <%--for the version control if update action--%>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>

                    <%--for the control referencial integrity if delete action--%>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>
                    <%--Currency form fields--%>
                    <%--Currency name--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="currencyName_id">
                        <fmt:message key="Currency.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(currencyName)" styleId="currencyName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="30"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%--Currency label--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="currencyLabel_id">
                        <fmt:message key="Currency.label"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(currencyLabel)" styleId="currencyLabel_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="4"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%--Currency Symbol--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="currencySymbol_id">
                        <fmt:message key="Currency.symbol"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(currencySymbol)" styleId="currencySymbol_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="4"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%--currency unit--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="unit_id">
                        <fmt:message key="Currency.unit"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:numberText property="dto(unit)" styleId="unit_id"
                                        styleClass="${app2:getFormInputClasses()} numberTextMedium" maxlength="34"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        maxFloat="2" maxInt="11"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%--Currency default--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="isBasicCurrency_id">
                        <fmt:message key="Currency.isBasicCurrency"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(isBasicCurrency)"
                                               disabled="${op == 'delete'}"
                                               styleId="isBasicCurrency_id"
                                               styleClass=""/>
                                <c:out value="${sessionScope.listshadow}" escapeXml="false"/>
                                <label for="isBasicCurrency_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()} ">
            <app2:securitySubmit operation="${op}" functionality="CURRENCY"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CURRENCY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<c:remove var="hasBasicCurrency"/>

<tags:jQueryValidation formName="currencyForm"/>