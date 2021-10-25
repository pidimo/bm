<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>

<c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
<c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
<c:set var="layoutHtml" value="col-xs-12"/>

<c:set var="layoutWord" value="${app2:getFormClasses()}"/>
<script language="JavaScript">
    function selectDocument(obj) {
        var layoutPanelSize=$("#layoutPanelSize");
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        if ('' == opt.value) {
            document.getElementById("tr_html").style.display = "none";
            document.getElementById("tr_word").style.display = "none";
            layoutPanelSize.removeClass("${layoutHtml}");
            layoutPanelSize.addClass("${layoutWord}");
        } else {
            var sel = opt.value;
            var html = ${mediatype_HTML};
            var word = ${mediatype_WORD};
            if (opt.value == html) {
                document.getElementById("tr_html").style.display = '';
                document.getElementById("tr_word").style.display = "none";
                layoutPanelSize.removeClass("${layoutWord}");
                layoutPanelSize.addClass("${layoutHtml}");
            }
            if (opt.value == word) {
                document.getElementById("tr_word").style.display = '';
                document.getElementById("tr_html").style.display = "none";
                layoutPanelSize.removeClass("${layoutHtml}");
                layoutPanelSize.addClass("${layoutWord}");
            }
        }
    }

</script>
<html:form action="${action}" focus="dto(description)" enctype="multipart/form-data" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}" id="layoutPanelSize">
        <c:set var="templateId" value="${templateForm.dtoMap['templateId']}"/>
        <c:set var="description" value="${templateForm.dtoMap['description']}"/>

        <html:hidden property="dto(maxAttachSize)" value="${sessionScope.user.valueMap['maxAttachSize']}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(templateId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(operation)" value="update"/>
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
                    <label class="${app2:getFormLabelClasses()}" for="description_id">
                        <fmt:message key="Template.description"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(description)" styleId="description_id"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="30" view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="mediaType_id">
                        <fmt:message key="Template.mediaType"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete' || 'update' == op)}">
                        <c:set var="templateTypesList" value="${app2:getTemplateMediaType(pageContext.request)}"/>
                        <html:select property="dto(mediaType)" styleId="mediaType_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     readonly="${op == 'delete' || 'update' == op}"
                                     onchange="javascript:selectDocument(this);"
                                     onkeyup="javascript:selectDocument(this);">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="templateTypesList" property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${op == 'create'}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="languageId_id">
                            <fmt:message key="Template.language"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <fanta:select property="dto(languageId)" styleId="languageId_id" listName="languageList"
                                          labelProperty="name"
                                          valueProperty="id" firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <c:set var="displayWord" value="display:none"/>
                    <c:set var="displayHtml" value="display:none"/>
                    <c:choose>
                        <c:when test="${mediatype_WORD eq templateForm.dtoMap['mediaType']}">
                            <c:set var="displayWord" value=""/>
                        </c:when>
                        <c:when test="${mediatype_HTML eq templateForm.dtoMap['mediaType']}">
                            <c:set var="displayHtml" value=""/>
                        </c:when>
                    </c:choose>
                    <div id="tr_word" style="${displayWord}" class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="Template.file"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <tags:bootstrapFile property="dto(file)"
                                                accept="application/msword"
                                                tabIndex="5"
                                                styleId="file_id"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClasses()}" id="tr_html" style="${displayHtml}">
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
                                           styleId="body_field" style="height:480px;"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="TEMPLATE" styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="TEMPLATE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<c:if test="${op=='update'}">
    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <iframe name="frame1"
                src="<app:url value="/Template/TemplateFileList.do?parameter(templateId)=${templateId}&dto(templateId)=${templateId}&dto(description)=${app2:encode(description)}&parameter(description)=${app2:encode(description)}&dto(mediaType)=${templateForm.dtoMap['mediaType']}" />"
                class="embed-responsive-item Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>
</c:if>
<tags:jQueryValidation formName="templateForm"/>