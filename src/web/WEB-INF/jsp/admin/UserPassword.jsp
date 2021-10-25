<%@ include file="/Includes.jsp" %>

<html:form action="/User/PasswordUpdate.do" focus="dto(userPassword)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(opPreference)" value="upPassword"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS"
                                 styleClass="${app2:getFormButtonClasses()}">
                ${save}
            </app2:securitySubmit>
                <%--<html:submit property="dto(save)" styleClass="button" ><c:out value="${save}"/></html:submit>--%>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <div id="User.jsp">
                <fieldset>

                    <legend class="title">
                        <fmt:message key="User.passChange"/>
                    </legend>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="userPassword_id">
                            <fmt:message key="User.pass"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(view)}">
                            <html:password property="dto(userPassword)" styleId="userPassword_id"
                                           styleClass="${app2:getFormInputClasses()} mediumText" maxlength="20"
                                           tabindex="1" value=""/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="password1_id">
                            <fmt:message key="User.passNew"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(view)}">
                            <html:password property="dto(password1)" styleId="password1_id"
                                           styleClass="${app2:getFormInputClasses()} mediumText"
                                           tabindex="2" value=""
                                           maxlength="20"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="password2_id">
                            <fmt:message key="User.passConfir"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(view)}">
                            <html:password property="dto(password2)" styleId="password2_id"
                                           styleClass="${app2:getFormInputClasses()} mediumText"
                                           tabindex="3" value=""
                                           maxlength="20"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="4">
                ${save}
            </app2:securitySubmit>
                <%--<html:submit property="dto(save)" styleClass="button" tabindex="4" ><c:out value="${save}"/></html:submit>--%>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="passForm"/>
