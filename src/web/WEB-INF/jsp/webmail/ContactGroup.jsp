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
                        <fmt:message key="Webmail.contactGroup.name"/>
                    </label>
                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(name)"
                                  styleId="name_id"
                                  styleClass="middleText ${app2:getFormInputClasses()}" maxlength="50" tabindex="1"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(mailGroupAddrId)"/>
                </c:if>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILGROUP" property="dto(save)" styleClass="button ${app2:getFormButtonClasses()}"
                                     styleId="saveButtonId"><c:out value="${button}"/></app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <html:submit property="dto(delete)" styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.delete"/></html:submit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="contactGroupForm"/>