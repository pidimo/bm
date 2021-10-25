<%@ include file="/Includes.jsp" %>
<table cellpadding="0" cellspacing="0" border="0" align="center" width="50%">
    <tr>
        <td>
            <table cellpadding="0" cellspacing="0" border="0" class="container" width="100%" align="center">
                <tr>
                    <td colspan="3" class="title"><c:out value="${title}"/></td>
                </tr>
                <tr>
                    <td class="label"><fmt:message key="common.translation"/></td>
                    <td class="label"><c:out value="${languageTitle}"/></td>
                    <td class="label"><fmt:message key="common.defaultTranslation"/></td>
                </tr>
                <html:form action="${action}" focusIndex="1">
                    <html:hidden property="dto(langTextId)" styleId="1"/>
                    <html:hidden property="dto(op)" value="updateUserTranslation" styleId="2"/>
                    <html:hidden property="dto(hasDefinedDefaultValue)" styleId="3"/>

                    <c:set var="tabIdx" value="${0}"/>
                    <c:forEach var="language" items="${app2:getCompanyLanguages(pageContext.request)}">
                        <tr>
                            <td class="contain" width="40%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <app:text property="dto(text_${language.languageId})"
                                          styleClass="text" tabindex="${tabIdx}"/>
                            </td>
                            <td class="contain" width="40%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <html:hidden property="dto(uiLanguages)" value="${language.languageId}"
                                             styleId="dto(uiLanguages)"/>
                                <app:text property="dto(language_${language.languageId})"
                                          value="${language.languageName}"
                                          styleClass="text"
                                          readonly="true"
                                          tabindex="${tabIdx}"/>
                            </td>
                            <td class="contain" width="20%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <html:radio property="dto(isDefault)"
                                            value="${language.languageId}"
                                            styleClass="radio"
                                            disabled="${true ==  translationForm.dtoMap['hasDefinedDefaultValue']}"
                                            tabindex="${tabIdx}"/>
                                <c:if test="${translationForm.dtoMap['isDefault'] == language.languageId}">
                                    <html:hidden property="dto(isDefault)" value="${language.languageId}"/>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="3" class="button">
                            <html:submit styleClass="button" tabindex="${tabIdx+10}"><fmt:message key="Common.save"/></html:submit>
                            <html:cancel styleClass="button" tabindex="${tabIdx+11}"><fmt:message key="Common.cancel"/></html:cancel>
                        </td>
                    </tr>
                </html:form>
            </table>
        </td>
    </tr>
</table>