<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>

<html:form action="${action}" focus="dto(title)" enctype="multipart/form-data" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(templateId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="title_id">
                        <fmt:message key="InvoiceTemplate.title"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(title)" styleId="title_id"
                                  styleClass="${app2:getFormInputClasses()} text" maxlength="149"
                                  view="${'delete' == op}" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${'create' == op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="InvoiceTemplate.language"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <fanta:select property="dto(languageId)"
                                          styleId="languageId_id"
                                          listName="languageList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          tabIndex="2">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="InvoiceTemplate.template"/>
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
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICETEMPLATE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="4">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICETEMPLATE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"
                                     tabindex="5">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"
                         tabindex="6">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<c:if test="${'update' == op}">
    <div class="embed-responsive embed-responsive-4by3 col-xs-12">
        <c:set var="templateId" value="${invoiceTemplateForm.dtoMap['templateId']}"/>
        <c:set var="title" value="${invoiceTemplateForm.dtoMap['title']}"/>
        <iframe name="frame1"
                src="<app:url value="/InvoiceText/List.do?templateId=${templateId}&dto(templateId)=${templateId}&dto(title)=${title}"/>"
                class="embed-responsive-item Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>
</c:if>

<tags:jQueryValidation formName="invoiceTemplateForm"/>