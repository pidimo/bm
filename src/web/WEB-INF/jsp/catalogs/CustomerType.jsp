<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(customerTypeName)" styleClass="form-horizontal">

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(customerTypeId)"/>
                </c:if>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="customerTypeName_id">
                        <fmt:message key="CustomerType.name"/>
                    </label>
                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(customerTypeName)"
                                  styleId="customerTypeName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="40"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

            <%--SAVE, SAVE AND NEW, CANCEL buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="CUSTOMERTYPE"
                                 styleClass="${app2:getFormButtonClasses()} button">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="CUSTOMERTYPE"
                                     styleClass="${app2:getFormButtonClasses()} button"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()} button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>

</html:form>

<tags:jQueryValidation formName="customerTypeForm"/>