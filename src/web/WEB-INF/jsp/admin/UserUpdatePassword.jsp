<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<html:form action="${action}" focus="dto(userPassword)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="USER" styleClass="button ${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                ${button}
            </app2:securitySubmit>

        </div>
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(employeeName)"/>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="userLogin_id">
                    <fmt:message key="User.login"/>
                </label>
                <div class="${app2:getFormContainClasses(op == 'update')}">
                    <app:text property="dto(userLogin)"
                              view="${op == 'update'}" styleId="userLogin_id" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="20" tabindex="4"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="userPassword_id">
                    <fmt:message key="User.password"/>
                </label>
                <div class="${app2:getFormContainClasses(null)}">
                    <html:password property="dto(userPassword)"
                                   styleId="userPassword_id"
                                   styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="20"
                                   tabindex="2" redisplay="false"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="passwordConfir_id">
                    <fmt:message key="User.passConfir"/>
                </label>
                <div class="${app2:getFormContainClasses(null)}">
                    <html:password property="dto(passwordConfir)"
                                   styleId="passwordConfir_id"
                                   styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="20"
                                   tabindex="2" redisplay="false"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="USER" styleClass="button ${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                ${button}
            </app2:securitySubmit>

        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="passForm"/>