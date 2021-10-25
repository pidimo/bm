<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(enabled)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="APPLICATIONSIGNATURE"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.save"/>
            </app2:securitySubmit>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="ApplicationSignature.enabled"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(enabled)" styleId="enabled_id" value="true"/>
                                <label for="enabled_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:forEach var="systemLanguage" items="${applicationSignatureForm.dtoMap['systemLanguages']}">
                    <html:hidden property="dto(languageName_${systemLanguage.key})"
                                 value="${systemLanguage.key}"/>

                    <legend class="title">
                        <fmt:message key="${systemLanguage.value}"/>
                    </legend>
                    <div class="${app2:getFormGroupClasses()} topLabel">
                        <div class="col-xs-12">
                            <label class="control-label">
                                <fmt:message key="ApplicationSignature.text"/>
                            </label>
                            <html:textarea property="dto(text_${systemLanguage.key})"
                                           styleClass="${app2:getFormInputClasses()} minimumDetail"
                                           style="width:100%;height: 80px;"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()} topLabel">
                        <div class="col-xs-12">
                            <label class="control-label">
                                <fmt:message key="ApplicationSignature.html"/>
                            </label>
                            <html:textarea property="dto(html_${systemLanguage.key})"
                                           styleClass="${app2:getFormInputClasses()} minimumDetail"
                                           styleId="signature_field_${systemLanguage.key}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:forEach>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="APPLICATIONSIGNATURE"
                                 styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.save"/>
            </app2:securitySubmit>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="applicationSignatureForm"/>