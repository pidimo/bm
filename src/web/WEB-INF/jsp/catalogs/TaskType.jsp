<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="AppointmentType.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(name)" styleClass="${app2:getFormInputClasses()} largetext"
                                  styleId="name_id" maxlength="20"
                                  view="${'delete' == op}" tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>


                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(taskTypeId)"/>
                    <html:hidden property="dto(version)"/>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op=='update'}">
                <app2:securitySubmit operation="${op}" functionality="TASKTYPE"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="3">
                    <fmt:message key="Common.save"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="TASKTYPE"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="4">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="TASKTYPE"
                                     styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew" tabindex="5">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="6"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="taskTypeForm"/>