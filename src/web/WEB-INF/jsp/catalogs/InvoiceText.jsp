<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>

<html:form action="${action}"
           focus="${'create' == op ? 'dto(languageId)' : 'dto(isDefault)' }"
           enctype="multipart/form-data"
           styleClass="form-horizontal">

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">

                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="InvoiceText.language"/>
                    </label>
                    <c:choose>
                        <c:when test="${'create' == op}">
                            <div class="${app2:getFormContainClasses(null)}">
                                <fanta:select property="dto(languageId)"
                                              listName="unselectedLanguageList"
                                              labelProperty="name"
                                              valueProperty="id"
                                              firstEmpty="true"
                                              styleClass="${app2:getFormSelectClasses()} select"
                                              tabIndex="1"
                                              styleId="languageId_id">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="templateId"
                                                     value="${param.templateId}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="${app2:getFormContainClasses('delete' == op || 'update' == op) }">
                                <fanta:select property="dto(languageId)"
                                              listName="languageList"
                                              labelProperty="name"
                                              valueProperty="id"
                                              firstEmpty="true"
                                              styleClass="${app2:getFormSelectClasses()} select"
                                              tabIndex="1"
                                              readOnly="true"
                                              styleId="languageId_id">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="isDefault_id">
                        <fmt:message key="InvoiceText.isDefault"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(isDefault)"
                                           disabled="${'delete' == op}"
                                           value="true"
                                           styleClass="radio"
                                           tabindex="2"
                                           styleId="isDefault_id"/>
                            <label></label>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${'delete' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="InvoiceText.template"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <tags:bootstrapFile property="dto(file)"
                                                accept="application/msword"
                                                tabIndex="3"
                                                styleId="file_id"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

            </fieldset>
        </div>

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(templateId)" value="${param.templateId}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(languageId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="UPDATE">
                <html:submit styleClass="${app2:getFormButtonClasses()}"
                             tabindex="4">${button}</html:submit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"
                         tabindex="5">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>
<tags:jQueryValidation formName="invoiceTextForm"/>
