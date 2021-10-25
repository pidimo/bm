<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(bankName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(contactId)" value="${param.contactId}"/>
        <html:hidden property="dto(bankAccountId)" value="${param.bankAccountId}"/>

            <%--if update action or delete action--%>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(bankId)"/>
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
                    <label class="${app2:getFormLabelClasses()}" for="bankName_id">
                        <fmt:message key="Bank.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(bankName)" styleId="bankName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="40"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankCode_id">
                        <fmt:message key="Bank.code"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(bankCode)" styleId="bankCode_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="8"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankLabel">
                        <fmt:message key="Bank.label"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">

                        <app:text property="dto(bankLabel)" styleId="bankLabel"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="20"
                                  view="${'delete' == op}"/>

                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankInternationalCode_id">
                        <fmt:message key="Bank.internationalCode"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(bankInternationalCode)" styleId="bankInternationalCode_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="20"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="BANK" styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:choose>
                <c:when test="${op == 'create' && !isFromContacts}">
                    <app2:securitySubmit operation="${op}" functionality="BANK"
                                         styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                    <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </c:when>
                <c:otherwise>
                    <c:if test="${op != 'create'}">
                        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                            <fmt:message key="Common.cancel"/>
                        </html:cancel>
                    </c:if>
                    <c:if test="${op == 'create'}">
                        <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" onclick="parent.hideBootstrapPopup()">
                            <fmt:message key="Common.cancel"/>
                        </html:cancel>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="bankForm"/>