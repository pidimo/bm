<%@ include file="/Includes.jsp" %>
<html:form action="${action}" focus="dto(salutationLabel)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <!--<table id="Salutation.jsp">-->
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <%--if update action or delete action--%>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(salutationId)"/>
                    <html:hidden property="dto(letterTextId)"/>
                    <html:hidden property="dto(addressTextId)"/>
                </c:if>
                    <%--for the version control if update action--%>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                    <%--for the control referencial integrity if delete action--%>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="salutationLabel_id">
                        <fmt:message key="Salutation.label"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(salutationLabel)" styleId="salutationLabel_id" styleClass="form-control"
                                  maxlength="20"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="letterText_id">
                        <fmt:message key="Salutation.letterText"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(letterText)" styleId="letterText_id" styleClass="form-control"
                                  maxlength="60"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="addressText_id">
                        <fmt:message key="Salutation.addressText"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(addressText)" styleId="addressText_id" styleClass="form-control"
                                  maxlength="60"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="Salutation.languageForTexts"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op || (null != salutationForm.dtoMap['languageId'] && '' != salutationForm.dtoMap['languageId']))}">
                        <fanta:select property="dto(languageId)"
                                      listName="languageBaseList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true" styleId="languageId_id"
                                      styleClass="${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op || (null != salutationForm.dtoMap['languageId'] && '' != salutationForm.dtoMap['languageId'])}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--<html:hidden property="dto(languageId)"/>--%>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="SALUTATION"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="SALUTATION"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()} cancel"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="salutationForm"/>