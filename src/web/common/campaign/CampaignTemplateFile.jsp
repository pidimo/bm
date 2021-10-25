<%@ include file="/Includes.jsp" %>

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

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>

            <html:form action="${action}" enctype="multipart/form-data"
                       focus="${'create'==op ? 'dto(languageId)' : 'dto(byDefault)'}">
                <html:hidden property="dto(templateId)" value="${templateId}"/>
                <html:hidden property="dto(documentType)" value="${documentType}"/>
                <html:hidden property="dto(campaignId)"/>

                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>

                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
                    <TR>
                        <TD colspan="6" class="button">
                            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="UPDATE">
                                <html:submit property="dto(save)" styleClass="button"
                                             tabindex="8">${button}</html:submit>

                                <c:if test="${template_html == documentType && 'delete' != op}">
                                    <html:submit property="dto(preview)"
                                                 styleClass="button"
                                                 onclick="javascript:callPreviewWindow();" tabindex="9">
                                        <fmt:message key="Template.saveAndpreview"/>
                                    </html:submit>
                                </c:if>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button" tabindex="10">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>

                    <TR>
                        <TD colspan="4" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="label" width="40%">
                            <fmt:message key="Template.language"/>
                        </td>
                        <td class="contain" width="60%">
                            <c:if test="${'create'==op}">
                                <fanta:select property="dto(languageId)" listName="campaignLanguageTagList"
                                              labelProperty="name"
                                              valueProperty="id" firstEmpty="true" styleClass="mediumSelect"
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
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Template.byDefault"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(byDefault)" styleClass="radio" tabindex="2" value="true"
                                           disabled="${'delete'==op}"/>
                        </td>
                    </tr>


                    <c:if test="${template_word == documentType && 'delete' != op}">
                        <tr>
                            <td class="label" width="40%">
                                <fmt:message key="Template.file"/>
                            </td>
                            <td class="contain" width="60%">
                                <html:file property="dto(file)" accept="application/msword" tabindex="3"/>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${template_html == documentType && 'delete' != op}">

                        <tr>
                            <td class="topLabel" colspan="2" width="100%">
                                <html:hidden property="dto(isPreview)" styleId="isPreview" value="false"/>
                                <fmt:message key="Template.htmlText"/>
                                <br/>
                                <table cellpadding="0" cellspacing="0" border="0" width="100%" class="container">
                                    <tr>
                                        <td>
                                            <tags:initTinyMCE4 textAreaId="body_field" addElwisPlugin="true"
                                                                   addDefaultBodyStylePlugin="true"
                                                                   addBrowseImagePlugin="true"
                                                                   addHtmlSourceEditButton="true"/>
                                            <html:textarea property="dto(editor)" tabindex="4" styleId="body_field"
                                                           styleClass="webmailBody" style="height:480px;width:550px;"/>
                                        </td>
                                    </tr>
                                </table>

                            </td>
                        </tr>
                    </c:if>

                    <TR>
                        <TD colspan="6" class="button">
                            <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="UPDATE">
                                <html:submit property="dto(save)" styleClass="button"
                                             tabindex="5">${button}</html:submit>

                                <c:if test="${template_html == documentType && 'delete' != op}">
                                    <html:submit property="dto(preview)"
                                                 styleClass="button"
                                                 onclick="javascript:callPreviewWindow();" tabindex="6">
                                        <fmt:message key="Template.saveAndpreview"/>
                                    </html:submit>
                                </c:if>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button" tabindex="7">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>