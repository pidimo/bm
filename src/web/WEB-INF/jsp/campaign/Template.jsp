<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>


<c:set var="template_html"><%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>
</c:set>
<c:set var="template_word"><%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>
</c:set>
<tags:initBootstrapFile/>
<c:set var="layoutSizeHtml" value="col-xs-12"/>
<c:set var="layoutSizeWord" value="${app2:getFormClasses()}"/>
<script language="JavaScript">
    function selectDocument(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        var panelLayoutSize=$("#panelSizeLayout");
        if ('' == opt.value) {
            document.getElementById("html").style.display = "none";
            document.getElementById("word").style.display = "none";
            panelLayoutSize.removeClass("${layoutSizeHtml}")
            panelLayoutSize.addClass("${layoutSizeWord}");
        } else {
            var sel = opt.value;
            var html = ${template_html};
            var word = ${template_word};
            if (opt.value == html) {
                document.getElementById("html").style.display = '';
                document.getElementById("previewButton0").style.display = '';
                document.getElementById("previewButton1").style.display = '';
                document.getElementById("word").style.display = "none";
                panelLayoutSize.removeClass("${layoutSizeWord}");
                panelLayoutSize.addClass("${layoutSizeHtml}");
            }
            if (opt.value == word) {
                document.getElementById("word").style.display = '';
                document.getElementById("html").style.display = "none";
                document.getElementById("previewButton0").style.display = "none";
                document.getElementById("previewButton1").style.display = "none";
                panelLayoutSize.removeClass("${layoutSizeHtml}");
                panelLayoutSize.addClass("${layoutSizeWord}");
            }
        }
    }

    function callPreviewWindow() {
        document.getElementById("isPreview").value = 'true';
    }
</script>

<c:set var="templateDocumentList" value="${app2:getCampaignTemplateDocumentType(pageContext.request)}"/>

<html:form action="${action}" enctype="multipart/form-data" focus="dto(description)" styleClass="form-horizontal">
    <div class="${'create'==op? layoutSizeHtml : layoutSizeWord}" id="panelSizeLayout">
        <html:hidden property="dto(op)" value="${op}"/>

        <c:if test="${'update'==op || 'delete'==op}">
            <html:hidden property="dto(templateId)"/>
            <c:set var="templateId" value="${campaignTemplateForm.dtoMap['templateId']}" scope="request"/>
            <c:set var="documentType" value="${campaignTemplateForm.dtoMap['documentType']}" scope="request"/>
        </c:if>

        <c:if test="${'update'==op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(documentType)"/>
        </c:if>

        <c:if test="${'delete'==op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="row">
            <div class="col-xs-12">
                <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="CREATE">
                    <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()} marginButton"
                                 tabindex="9">
                        ${button}
                    </html:submit>
                    <c:if test="${null == previewButtonDisplay0}">
                        <c:set var="previewButtonDisplay0" value=""/>
                    </c:if>
                    <c:if test="${'create' == op}">
                        <html:submit property="dto(preview)"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="javascript:callPreviewWindow();"
                                     styleId="previewButton0"
                                     style="${previewButtonDisplay0}" tabindex="10">
                            <fmt:message key="Template.saveAndpreview"/>
                        </html:submit>
                    </c:if>
                </app2:checkAccessRight>
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="11">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="description_id">
                        <fmt:message key="Template.description"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(description)"
                                  styleId="description_id"
                                  styleClass="largetext ${app2:getFormInputClasses()}" maxlength="30"
                                  view="${op == 'delete'}"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="documentType_id">
                        <fmt:message key="Template.documenType"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete' || 'update' == op)}">
                        <html:select property="dto(documentType)"
                                     styleId="documentType_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete' || 'update' == op}"
                                     tabindex="2" onchange="javascript:selectDocument(this);">
                            <html:options collection="templateDocumentList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${'create' == op}">
                    <html:hidden property="dto(typeSelected)" styleId="typeSelected"/>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="Template.language"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <fanta:select property="dto(languageId)"
                                          styleId="languageId_id"
                                          listName="languageList" module="/catalogs"
                                          labelProperty="name"
                                          valueProperty="id" firstEmpty="true"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}" tabIndex="3">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <c:if test="${null == wordDisplay}">
                        <c:set var="wordDisplay" value="display:none"/>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}" id="word" style="${wordDisplay}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="Template.file"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <tags:bootstrapFile property="dto(file)" styleId="file_id"
                                                accept="application/msword" tabIndex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <c:if test="${null == htmlDisplay}">
                        <c:set var="htmlDisplay" value=""/>
                    </c:if>
                    <html:hidden property="dto(isPreview)" styleId="isPreview" value="false"/>
                    <div id="html" style="${htmlDisplay}">
                        <div class="topLabel">
                            <label class="control-label" for="body_field">
                                <fmt:message key="Template.htmlText"/>
                            </label>
                            <tags:initTinyMCE4 textAreaId="body_field"
                                                   addElwisPlugin="true"
                                                   addDefaultBodyStylePlugin="true"
                                                   addBrowseImagePlugin="true"
                                                   addHtmlSourceEditButton="true"/>
                            <html:textarea property="dto(editor)"
                                           tabindex="5"
                                           styleClass="webmailBody ${app2:getFormInputClasses()}"
                                           styleId="body_field" style="height: 480px;"/>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="CREATE">
                    <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()} marginButton"
                                 tabindex="6">
                        ${button}
                    </html:submit>
                    <c:if test="${null == previewButtonDisplay1}">
                        <c:set var="previewButtonDisplay1" value=""/>
                    </c:if>
                    <c:if test="${'create' == op}">
                        <html:submit property="dto(preview)"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="javascript:callPreviewWindow();"
                                     styleId="previewButton1"
                                     style="${previewButtonDisplay1}" tabindex="7">
                            <fmt:message key="Template.saveAndpreview"/>
                        </html:submit>
                    </c:if>
                </app2:checkAccessRight>
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton" tabindex="8">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="campaignTemplateForm"/>

<c:if test="${op=='update'}">
    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <iframe class="embed-responsive-item" name="frame1"
                src="<app:url value="/Template/File/List.do?parameter(templateId)=${templateId}&parameter(documentType)=${documentType}" />"
                scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>
</c:if>