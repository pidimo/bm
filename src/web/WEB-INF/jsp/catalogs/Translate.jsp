<%@ include file="/Includes.jsp" %>
<html:form action="${action}" focusIndex="1" styleClass="form-horizontal">
    <html:hidden property="dto(langTextId)" styleId="1"/>
    <html:hidden property="dto(op)" value="updateUserTranslation" styleId="2"/>
    <html:hidden property="dto(hasDefinedDefaultValue)" styleId="3"/>
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <table class="${app2:getTableClasesIntoForm()}">
                    <tr>
                        <th><fmt:message key="common.translation"/></th>
                        <th><c:out value="${languageTitle}"/></th>
                        <th><fmt:message key="common.defaultTranslation"/></th>
                    </tr>
                    <c:set var="tabIdx" value="${0}"/>
                    <c:forEach var="language" items="${app2:getCompanyLanguages(pageContext.request)}">
                        <tr>
                            <td width="35%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <app:text property="dto(text_${language.languageId})"
                                          styleClass="text ${app2:getFormInputClasses()}" tabindex="${tabIdx}"/>
                            </td>
                            <td width="35%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <html:hidden property="dto(uiLanguages)" value="${language.languageId}"
                                             styleId="dto(uiLanguages)"/>
                                <app:text property="dto(language_${language.languageId})"
                                          value="${language.languageName}"
                                          styleClass="text ${app2:getFormInputClasses()}"
                                          readonly="true"
                                          tabindex="${tabIdx}"/>
                            </td>
                            <td width="30%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <div class="radio radio-default radio-inline">
                                    <html:radio property="dto(isDefault)"
                                                value="${language.languageId}"
                                                styleClass="radio"
                                                disabled="${true ==  translationForm.dtoMap['hasDefinedDefaultValue']}"
                                                tabindex="${tabIdx}"/>
                                    <label><fmt:message key="Common.yes"/></label>
                                </div>
                                <c:if test="${translationForm.dtoMap['isDefault'] == language.languageId}">
                                    <html:hidden property="dto(isDefault)" value="${language.languageId}"/>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="${tabIdx+10}"><fmt:message key="Common.save"/></html:submit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="${tabIdx+11}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
