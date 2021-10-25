<%@ include file="/Includes.jsp" %>

<html:form action="/TelecomType/Translate/Save.do" styleClass="form-horizontal translate">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(telecomTypeId)" value="${translateTelecomTypeForm.dtoMap['telecomTypeId']}"/>
        <html:hidden property="dto(langTextId)" value="${translateTelecomTypeForm.dtoMap['langTextId']}"/>
        <html:hidden property="dto(telecomTypeName)" value="${translateTelecomTypeForm.dtoMap['telecomTypeName']}"/>
        <c:set var="disableChecks" value=""/>
        <c:if test="${translateTelecomTypeForm.dtoMap['haveDefaultTranslation'] == true}">
            <c:set var="disableChecks" value="disabled"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="TelecomType.Title.translate"/>
                </legend>
            </fieldset>
            <div class="table-responsive">
                <table class="${app2:getTableClasesIntoForm()}">
                    <thead>
                    <tr>
                        <th>
                            <fmt:message key="TelecomType.translation"/>
                        </th>
                        <th>
                            <fmt:message key="TelecomType.language"/>
                        </th>
                        <th>
                            <fmt:message key="TelecomType.defaultTranslation"/>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="langText" items="${translateTelecomTypeForm.dtoMap['translatedSystemLanguages']}"
                               varStatus="counter">
                        <tr>
                            <td>
                                <c:if test="${langText.text == null || ' ' == langtext.text}">
                                    <app:text property="dto(text${counter.count})"
                                              styleClass="${app2:getFormInputClasses()} text"/>
                                </c:if>
                                <c:if test="${langText.text != null}">
                                    <app:text property="dto(text${counter.count})" value="${langText.text}"
                                              styleClass="${app2:getFormInputClasses()} text"/>
                                </c:if>
                                    <%--<app:text property="dto(text${counter.count})" value="${langText.text}" styleClass="text" />--%>
                            </td>
                            <td>
                                <html:hidden property="dto(languageId${counter.count})" value="${langText.languageId}"/>
                                <app:text property="dto(languageName${counter.count})" value="${langText.languageName}"
                                          readonly="true" styleClass="${app2:getFormInputClasses()} text"/>
                            </td>
                            <td>
                                <c:set var="isChecked" value=""/>
                                <c:if test="${langText.isDefault == true}">
                                    <c:set var="isChecked" value="checked"/>
                                </c:if>
                                <c:if test="${'checked' == isChecked}">
                                    <html:hidden property="dto(isDefault)"
                                                 value="${langText.languageId}_${counter.count}"/>
                                </c:if>
                                <div class="radio radio-default">
                                    <input class="radio" type="radio" name="dto(isDefault)"
                                           value="${langText.languageId}_${counter.count}" ${disableChecks} ${isChecked}>
                                    <label></label>
                                </div>
                            </td>
                        </tr>
                        <c:set var="translations" value="${counter.count}"/>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
            <%--SAVE AND CANCEL buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:hidden property="dto(numberOfTranslations)" value="${translations}"/>
            <html:submit styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.save"/>
            </html:submit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="translateTelecomTypeForm"/>
