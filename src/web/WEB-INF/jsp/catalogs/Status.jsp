<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(statusName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(statusId)"/>
        </c:if>
            <%--for the version control if update action--%>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="statusName_id">
                        <fmt:message key="Status.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(statusName)" styleId="statusName_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="80" view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="isFinal_id">
                        <fmt:message key="Status.isFinal"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(isFinal)" styleId="isFinal_id" value="true"
                                            disabled="${'delete' == op}"/>
                                <label><fmt:message key="Common.yes"/></label>
                            </div>
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(isFinal)" value="false" disabled="${'delete' == op}"/>
                                <label><fmt:message key="Common.no"/></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="STATUS"
                                 styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="STATUS"
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
<tags:jQueryValidation formName="statusForm"/>



