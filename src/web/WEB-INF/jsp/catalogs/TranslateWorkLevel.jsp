<%@ include file="/Includes.jsp" %>

<html:form action="/Support/WorkLevel/Translate/Save.do" styleClass="form-horizontal translate">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(workLevelId)" value="${workLevelTranslateForm.dtoMap['workLevelId']}"/>
        <html:hidden property="dto(langTextId)" value="${workLevelTranslateForm.dtoMap['langTextId']}"/>
        <html:hidden property="dto(workLevelName)" value="${workLevelTranslateForm.dtoMap['workLevelName']}"/>

        <c:set var="disableChecks" value=""/>
        <c:if test="${workLevelTranslateForm.dtoMap['haveDefaultTranslation'] == true}">
            <c:set var="disableChecks" value="disabled"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="WorkLevel.title.translate"/>
                </legend>
            </fieldset>

            <div class="table-responsive">
                <table class="${app2:getTableClasesIntoForm()}">
                    <thead>
                    <tr>
                        <th>
                            <fmt:message key="common.translation"/>
                        </th>
                        <th>
                            <fmt:message key="common.language"/>
                        </th>
                        <th>
                            <fmt:message key="common.defaultTranslation"/>
                        </th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="langText" items="${workLevelTranslateForm.dtoMap['translatedSystemLanguages']}"
                               varStatus="counter">
                        <tr>
                            <td>

                                <c:if test="${langText.text == null || ' ' == langtext.text}">
                                    <app:text property="dto(text${counter.count})"
                                              styleClass="${app2:getFormInputClasses()} text"/>
                                </c:if>
                                <c:if test="${langText.text != null}">
                                    <app:text property="dto(text${counter.count})"
                                              value="${langText.text}"
                                              styleClass="${app2:getFormInputClasses()} text"/>
                                </c:if>


                            </td>
                            <td>
                                <html:hidden property="dto(languageId${counter.count})"
                                             value="${langText.languageId}"/>
                                <app:text property="dto(languageName${counter.count})"
                                          value="${langText.languageName}"
                                          readonly="true"
                                          styleClass="${app2:getFormInputClasses()} text"/>
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
                                    <input class="radio"
                                           type="radio"
                                           name="dto(isDefault)"
                                           value="${langText.languageId}_${counter.count}" ${disableChecks} ${isChecked}>
                                    <label></label>
                                </div>
                                    <%--<html:radio property="dto(isDefaultId${counter.count})" value="${langText.isDefault}" disabled="${dto.haveDefaultTranslation}" />--%>
                            </td>
                        </tr>
                        <c:set var="translations" value="${counter.count}"/>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:hidden property="dto(numberOfTranslations)" value="${translations}"/>
            <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.save"/></html:submit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>

    </div>
</html:form>
