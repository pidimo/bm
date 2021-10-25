<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(priorityName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div id="Priority.jsp">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(type)" value="${type}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(priorityId)"/>
                    </c:if>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>

                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <c:set var="haveFirstTranslation"
                               value="${supportPriorityForm.dtoMap['haveFirstTranslation']}"/>
                        <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}"/>
                        <html:hidden property="dto(langTextId)"/>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="priorityName_id">
                            <fmt:message key="Priority.name"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(priorityName)" styleId="priorityName_id"
                                      styleClass="largetext ${app2:getFormInputClasses()}" maxlength="20"
                                      view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="sequence_id">
                            <fmt:message key="Priority.sequence"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(sequence)" styleId="sequence_id"
                                      styleClass="${app2:getFormInputClasses()} numberText"
                                      maxlength="4" view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="Priority.language"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete'|| true == haveFirstTranslation)}">
                            <fanta:select property="dto(languageId)" styleId="languageId_id"
                                          listName="systemLanguageList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true" styleClass="select ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'|| true == haveFirstTranslation}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="${function}"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="${function}"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="supportPriorityForm"/>

