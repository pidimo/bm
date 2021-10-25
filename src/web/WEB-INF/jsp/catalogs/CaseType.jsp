<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(caseTypeName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(caseTypeId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <c:set var="haveFirstTranslation" value="${caseTypeForm.dtoMap['haveFirstTranslation']}"/>
            <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}"/>
            <html:hidden property="dto(langTextId)"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="caseTypeName_id">
                        <fmt:message key="CaseType.Name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(caseTypeName)" styleId="caseTypeName_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="40"
                                  view="${'delete' == op}" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="CaseType.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete' || op == 'update')}">
                        <fanta:select property="dto(languageId)" styleId="languageId_id" listName="systemLanguageList"
                                      labelProperty="name"
                                      valueProperty="id" tabIndex="2"
                                      firstEmpty="true" styleClass="${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'|| true == haveFirstTranslation}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CASETYPE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="3">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CASETYPE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="4" property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="5">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="caseTypeForm"/>



