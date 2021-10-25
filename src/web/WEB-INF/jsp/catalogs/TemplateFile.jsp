<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>

<c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
<c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
<c:choose>
    <c:when test="${mediatype_HTML eq fileTemplateForm.dtoMap['mediaType'] && 'delete' != op}">
        <c:set var="layoutFormSize" value="col-xs-12"/>
    </c:when>
    <c:otherwise>
        <c:set var="layoutFormSize" value="${app2:getFormClasses()}"/>
    </c:otherwise>
</c:choose>
<html:form action="${action}" enctype="multipart/form-data" styleClass="form-horizontal">
    <div class="${layoutFormSize}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="TEMPLATE" permission="UPDATE">
                <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}" tabindex="1">
                    ${button}
                </html:submit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <html:hidden property="dto(templateId)" value="${fileTemplateForm.dtoMap['templateId']}"/>
        <html:hidden property="dto(maxAttachSize)" value="${sessionScope.user.valueMap['maxAttachSize']}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(description)" value="${fileTemplateForm.dtoMap['description']}"/>
            <%--<html:hidden property="dto(editor)" value="${fileTemplateForm.dtoMap['editor']}"/>--%>
        <html:hidden property="dto(mediaType)"/>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <c:if test="${'uploadFile' == op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="Template.language"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <fanta:select property="dto(languageId)" styleId="languageId_id"
                                          listName="templateLanguageTagList"
                                          labelProperty="name" valueProperty="id" firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}" tabIndex="3">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="templateTextTemplateId"
                                                 value="${not empty fileTemplateForm.dtoMap['templateId']?fileTemplateForm.dtoMap['templateId']:0}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${'delete' == op || 'update' == op}">
                    <c:if test="${'delete' == op}">
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Template.file"/>
                            </label>

                            <div class="${app2:getFormContainViewClasses()}">
                                    ${dto.templateDescription}
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Template.language"/>
                        </label>

                        <div class="${app2:getFormContainViewClasses()}">
                            <c:out value="${fileTemplateForm.dtoMap['languageName']}"/>
                            <html:hidden property="dto(languageId)"/>
                            <html:hidden property="dto(languageName)"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${mediatype_WORD eq fileTemplateForm.dtoMap['mediaType'] && 'delete' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="Template.file"/>
                        </label>

                        <div class="${app2:getFormContainViewClasses()}">
                            <tags:bootstrapFile property="dto(file)"
                                                styleId="file_id"
                                                accept="application/msword"
                                                tabIndex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${mediatype_HTML eq fileTemplateForm.dtoMap['mediaType'] && 'delete' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="col-xs-12">
                            <label class="topLabel control-label" for="body_field">
                                <fmt:message key="Template.htmlText"/>
                            </label>

                            <tags:initTinyMCE4 textAreaId="body_field" addElwisPlugin="true"
                                                   addDefaultBodyStylePlugin="true" addBrowseImagePlugin="true"
                                                   addHtmlSourceEditButton="true"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                            <html:textarea property="dto(editor)"
                                           styleClass="webmailBody ${app2:getFormInputClasses()}"
                                           styleId="body_field"
                                           tabindex="5" style="height:480px;"/>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
            <%--CANCEL, SAVE buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="TEMPLATE" permission="UPDATE">
                <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}" tabindex="6">
                    ${button}
                </html:submit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="fileTemplateForm"/>