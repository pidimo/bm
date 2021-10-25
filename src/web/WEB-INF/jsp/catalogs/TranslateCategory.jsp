<%@ include file="/Includes.jsp" %>

<html:form action="/Category/Translate/Save.do" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(op)" value="update"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(categoryId)" value="${translateCategoryForm.dtoMap['categoryId']}"/>
                <html:hidden property="dto(langTextId)" value="${translateCategoryForm.dtoMap['langTextId']}"/>
                <html:hidden property="dto(categoryName)" value="${translateCategoryForm.dtoMap['categoryName']}"/>

                <c:set var="disableChecks" value=""/>
                <c:if test="${translateCategoryForm.dtoMap['haveDefaultTranslation'] == true}">
                    <c:set var="disableChecks" value="disabled"/>
                </c:if>
                <legend class="title">
                    <fmt:message key="Category.Title.translate"/>
                </legend>
                <table class="${app2:getTableClasesIntoForm()}">
                    <tr>
                        <th><fmt:message key="Category.translation"/></th>
                        <th><fmt:message key="Category.language"/></th>
                        <th><fmt:message key="Category.defaultTranslation"/></th>
                    </tr>
                    <c:forEach var="langText" items="${translateCategoryForm.dtoMap['translatedSystemLanguages']}"
                               varStatus="counter">
                        <tr>
                            <td width="30%">

                                <c:if test="${langText.text == null || ' ' == langtext.text}">
                                    <app:text property="dto(text${counter.count})" styleClass="text form-control"/>
                                </c:if>
                                <c:if test="${langText.text != null}">
                                    <app:text property="dto(text${counter.count})" value="${langText.text}"
                                              styleClass="text ${app2:getFormInputClasses()}"/>
                                </c:if>
                            </td>
                            <td width="40%">
                                <html:hidden property="dto(languageId${counter.count})" value="${langText.languageId}"/>
                                <app:text property="dto(languageName${counter.count})" value="${langText.languageName}"
                                          readonly="true" styleClass="text ${app2:getFormInputClasses()}"/>
                            </td>
                            <td width="30%">
                                <c:set var="isChecked" value=""/>
                                <c:if test="${langText.isDefault == true}">
                                    <c:set var="isChecked" value="checked"/>
                                </c:if>

                                <c:if test="${'checked' == isChecked}">
                                    <html:hidden property="dto(isDefault)"
                                                 value="${langText.languageId}_${counter.count}"/>
                                </c:if>
                                <div class="radio radio-default radio-inline">
                                    <input class="radio" type="radio" name="dto(isDefault)"
                                           value="${langText.languageId}_${counter.count}" ${disableChecks} ${isChecked}>
                                    <label></label>
                                </div>


                                    <%--<html:radio property="dto(isDefaultId${counter.count})" value="${langText.isDefault}" disabled="${dto.haveDefaultTranslation}" />--%>
                            </td>
                        </tr>
                        <c:set var="translations" value="${counter.count}"/>
                    </c:forEach>
                </table>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:hidden property="dto(numberOfTranslations)" value="${translations}"/>
            <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                    key="Common.save"/></html:submit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
