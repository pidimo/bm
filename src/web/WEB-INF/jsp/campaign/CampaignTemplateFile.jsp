<%@ include file="/Includes.jsp" %>

<tags:initBootstrapFile/>

<app2:jScriptUrl
        url="/campaign/Campaign/Forward/BuildPreview.do?module=campaign&templateId=${templateId}&dto(templateId)=${templateId}&campaignId=${templateFileForm.dtoMap['campaignId']}&dto(campaignId)=${templateFileForm.dtoMap['campaignId']}&documentType=${documentType}&dto(documentType)=${documentType}&dto(languageId)=${templateFileForm.dtoMap['languageId']}&dto(requestLocale)=${pageContext.request.locale.language}"
        var="locationJsUrl" addModuleParams="false"/>

<script type="text/javascript">
    function callPreviewWindow() {
        document.getElementById("isPreview").value = 'true';
    }

    function showPreviewWindow() {
        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - 570 / 2;
        var posy = winy - 650 / 2;
        var searchWindow = window.open(${locationJsUrl}, 'myWindow', 'resizable=yes, width=570, height=650, left=' + posx + ', top=' + posy + ', scrollbars=yes, status=0');
    }
</script>
<c:choose>
    <c:when test="${template_html == documentType && 'delete' != op}">
        <c:set var="layoutSizeTemplate" value="col-xs-12"/>
    </c:when>
    <c:otherwise>
        <c:set var="layoutSizeTemplate" value="${app2:getFormClasses()}"/>
    </c:otherwise>
</c:choose>
<html:form action="${action}"
           enctype="multipart/form-data"
           styleClass="form-horizontal"
           focus="${'create'==op ? 'dto(languageId)' : 'dto(byDefault)'}">

    <div class="${layoutSizeTemplate}">
        <html:hidden property="dto(templateId)" value="${templateId}"/>
        <html:hidden property="dto(documentType)" value="${documentType}"/>
        <html:hidden property="dto(campaignId)"/>
        <html:hidden property="dto(op)" value="${op}"/>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="UPDATE">
                <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}"
                             tabindex="8">${button}</html:submit>

                <c:if test="${template_html == documentType && 'delete' != op}">
                    <html:submit property="dto(preview)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 onclick="javascript:callPreviewWindow();" tabindex="9">
                        <fmt:message key="Template.saveAndpreview"/>
                    </html:submit>
                </c:if>
            </app2:checkAccessRight>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="10">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                        <fmt:message key="Template.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete'== op || 'update' == op)}">
                        <c:if test="${'create'==op}">
                            <fanta:select property="dto(languageId)"
                                          listName="campaignLanguageTagList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleId="languageId_id"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          tabIndex="1">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="templateTextTemplateId"
                                                 value="${empty templateId? 0:templateId}"/>
                            </fanta:select>
                        </c:if>
                        <c:if test="${'delete'== op || 'update' == op}">
                            ${templateFileForm.dtoMap['languageName']}
                            <html:hidden property="dto(languageId)"/>
                            <html:hidden property="dto(languageName)"
                                         value="${templateFileForm.dtoMap['languageName']}"/>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="byDefault_id">
                        <fmt:message key="Template.byDefault"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(byDefault)"
                                               styleClass="radio"
                                               tabindex="2"
                                               styleId="byDefault_id"
                                               value="true"
                                               disabled="${'delete'==op}"/>
                                <label for="byDefault_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${template_word == documentType && 'delete' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="Template.file"/>
                        </label>

                        <div class="${app2:getFormContainClasses(template_word == documentType && 'delete' != op)}">
                            <tags:bootstrapFile property="dto(file)"
                                                accept="application/msword"
                                                tabIndex="3"
                                                styleId="file_id"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <c:if test="${template_html == documentType && 'delete' != op}">
                    <div class="topLabel">
                        <label class="control-label" for="body_field">
                            <fmt:message key="Template.htmlText"/>
                        </label>

                        <html:hidden property="dto(isPreview)" styleId="isPreview" value="false"/>

                        <tags:initTinyMCE4 textAreaId="body_field"
                                               addElwisPlugin="true"
                                               addDefaultBodyStylePlugin="true"
                                               addBrowseImagePlugin="true"
                                               addHtmlSourceEditButton="true"/>
                        <html:textarea property="dto(editor)"
                                       tabindex="4"
                                       styleId="body_field"
                                       style="height: 480px"
                                       styleClass="webmailBody ${app2:getFormInputClasses()}"/>
                    </div>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="UPDATE">
                <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}"
                             tabindex="5">${button}</html:submit>

                <c:if test="${template_html == documentType && 'delete' != op}">
                    <html:submit property="dto(preview)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 onclick="javascript:callPreviewWindow();" tabindex="6">
                        <fmt:message key="Template.saveAndpreview"/>
                    </html:submit>
                </c:if>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="templateFileForm"/>