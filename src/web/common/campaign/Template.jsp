<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>


<c:set var="template_html"><%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>
</c:set>
<c:set var="template_word"><%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>
</c:set>

<script language="JavaScript">
    function selectDocument(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        if ('' == opt.value) {
            document.getElementById("html").style.display = "none";
            document.getElementById("word").style.display = "none";
        } else {
            var sel = opt.value;
            var html = ${template_html};
            var word = ${template_word};
            if (opt.value == html) {
                document.getElementById("html").style.display = '';
                document.getElementById("previewButton0").style.display = '';
                document.getElementById("previewButton1").style.display = '';
                document.getElementById("word").style.display = "none";
            }
            if (opt.value == word) {
                document.getElementById("word").style.display = '';
                document.getElementById("html").style.display = "none";
                document.getElementById("previewButton0").style.display = "none";
                document.getElementById("previewButton1").style.display = "none";
            }
        }
    }

    function callPreviewWindow() {
        document.getElementById("isPreview").value = 'true';
    }
</script>

<c:set var="templateDocumentList" value="${app2:getCampaignTemplateDocumentType(pageContext.request)}"/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>

            <html:form action="${action}" enctype="multipart/form-data" focus="dto(description)">
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


                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
                    <tr>
                        <td class="button" colspan="2">
                            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="CREATE">
                                <html:submit property="dto(save)" styleClass="button" tabindex="9">
                                    ${button}
                                </html:submit>
                                <c:if test="${null == previewButtonDisplay0}">
                                    <c:set var="previewButtonDisplay0" value=""/>
                                </c:if>
                                <c:if test="${'create' == op}">
                                    <html:submit property="dto(preview)"
                                                 styleClass="button"
                                                 onclick="javascript:callPreviewWindow();"
                                                 styleId="previewButton0"
                                                 style="${previewButtonDisplay0}" tabindex="10">
                                        <fmt:message key="Template.saveAndpreview"/>
                                    </html:submit>
                                </c:if>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button" tabindex="11">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="title">${title}</td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Template.description"/>
                        </td>
                        <td class="contain">
                            <app:text property="dto(description)" styleClass="largetext" maxlength="30"
                                      view="${op == 'delete'}"
                                      tabindex="1"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Template.documenType"/>
                        </td>
                        <td class="contain">
                            <html:select property="dto(documentType)" styleClass="mediumSelect"
                                         readonly="${op == 'delete' || 'update' == op}"
                                         tabindex="2" onchange="javascript:selectDocument(this);">
                                <html:options collection="templateDocumentList" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>

                    <c:if test="${'create' == op}">
                        <html:hidden property="dto(typeSelected)" styleId="typeSelected"/>
                        <tr>
                            <td class="label">
                                <fmt:message key="Template.language"/>
                            </td>
                            <td class="contain">
                                <fanta:select property="dto(languageId)" listName="languageList" module="/catalogs"
                                              labelProperty="name"
                                              valueProperty="id" firstEmpty="true" styleClass="mediumSelect"
                                              readOnly="${op == 'delete'}" tabIndex="3">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                            </td>
                        </tr>

                        <c:if test="${null == wordDisplay}">
                            <c:set var="wordDisplay" value="display:none"/>
                        </c:if>
                        <tr id="word" style="${wordDisplay}">
                            <td class="label" width="30%">
                                <fmt:message key="Template.file"/>
                            </td>
                            <td class="contain" width="70%">
                                <html:file property="dto(file)" accept="application/msword" tabindex="4"
                                           style="largeText"/>
                            </td>
                        </tr>
                        <c:if test="${null == htmlDisplay}">
                            <c:set var="htmlDisplay" value=""/>
                        </c:if>
                        <html:hidden property="dto(isPreview)" styleId="isPreview" value="false"/>
                        <tr id="html" style="${htmlDisplay}">
                            <td class="topLabel" colspan="2">
                                <fmt:message key="Template.htmlText"/>

                                <tags:initTinyMCE4 textAreaId="body_field" addElwisPlugin="true"
                                                       addDefaultBodyStylePlugin="true" addBrowseImagePlugin="true"
                                                       addHtmlSourceEditButton="true"/>
                                <html:textarea property="dto(editor)" tabindex="5" styleClass="webmailBody"
                                               styleId="body_field" style="height:480px;width:550px;"/>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="button" colspan="2">
                            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="CREATE">
                                <html:submit property="dto(save)" styleClass="button" tabindex="6">
                                    ${button}
                                </html:submit>
                                <c:if test="${null == previewButtonDisplay1}">
                                    <c:set var="previewButtonDisplay1" value=""/>
                                </c:if>
                                <c:if test="${'create' == op}">
                                    <html:submit property="dto(preview)"
                                                 styleClass="button"
                                                 onclick="javascript:callPreviewWindow();"
                                                 styleId="previewButton1"
                                                 style="${previewButtonDisplay1}" tabindex="7">
                                        <fmt:message key="Template.saveAndpreview"/>
                                    </html:submit>
                                </c:if>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button" tabindex="8">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                </table>
            </html:form>


            <c:if test="${op=='update'}">
                <iframe name="frame1"
                        src="<app:url value="/Template/File/List.do?parameter(templateId)=${templateId}&parameter(documentType)=${documentType}" />"
                        class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </c:if>

        </td>
    </tr>
</table>