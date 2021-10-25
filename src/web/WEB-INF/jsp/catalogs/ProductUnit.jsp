<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(unitName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <%--if update action or delete action--%>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(unitId)"/>
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
                    <%--Language form field--%>
                    <%--Language name--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="unitName_id">
                        <fmt:message key="ProductUnit.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(unitName)" styleId="unitName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="20" view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PRODUCTUNIT"
                                 styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PRODUCTUNIT"
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
<tags:jQueryValidation formName="productUnitForm"/>

