<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(projectId)" value="${param.projectId}"/>

        <c:if test="${'update'== op || 'delete'== op}">
            <html:hidden property="dto(activityId)"/>
        </c:if>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY" property="save"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="5">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="6">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">

            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="number_id">
                        <fmt:message key="ProjectActivity.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(name)"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="40"
                                  view="${'delete' == op}"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY" property="save"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="2">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="3">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonClasses()}" tabindex="4">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>