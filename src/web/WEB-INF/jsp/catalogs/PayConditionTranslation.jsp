<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focusIndex="1" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(version)" styleId="s"/>
        <html:hidden property="dto(payConditionId)"/>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()} formAddBorderButtom">
                    <label class="control-label controlLabelColor col-xs-12 col-sm-3">
                        <fmt:message key="PayConditionText.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <strong><fmt:message key="PayConditionText.text"/></strong>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:set var="tabIndex" value="${1}"/>
                <c:forEach var="language" items="${app2:getCompanyLanguages(pageContext.request)}" varStatus="index">
                    <c:set var="tabIndex" value="${tabIndex + index.count}"/>
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="${app2:getFormLabelClasses()}">
                            <html:hidden property="dto(uiLanguages)" value="${language.languageId}"
                                         styleId="dto(uiLanguages)"/>
                            <html:hidden property="dto(language_${language.languageId})"
                                         value="${language.languageName}"/>
                            <c:out value="${language.languageName}"/>
                        </div>
                        <div class="${app2:getFormContainClasses(null)}">
                            <html:textarea property="dto(text_${language.languageId})"
                                           styleClass="${app2:getFormInputClasses()} minimumDetail" rows="1"
                                           tabindex="${tabIndex}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:forEach>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="${tabIndex+1}">
                <c:out value="${button}"/>
            </html:submit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="${tabIndex+2}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="payConditionForm"/>