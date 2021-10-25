<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title"><c:out value="${title}"/></legend>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(contractTypeId)"/>
                </c:if>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="ContractType.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(name)" styleId="name_id" styleClass="${app2:getFormInputClasses()} text"
                                  maxlength="99"
                                  view="${'delete' == op}" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="tobeInvoiced_id">
                        <fmt:message key="ContractType.tobeInvoiced"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(tobeInvoiced)"
                                               disabled="${'delete' == op}"
                                               value="true"
                                               styleId="tobeInvoiced_id"
                                               styleClass="" tabindex="2"/>
                                <label for="tobeInvoiced_id"></label>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="CONTRACTTYPE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="3">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="CONTRACTTYPE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"
                                     tabindex="4">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"
                         tabindex="5">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="contractTypeForm"/>
