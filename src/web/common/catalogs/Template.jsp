<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
<c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>

<script language="JavaScript">
    function selectDocument(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        if ('' == opt.value) {
            document.getElementById("tr_html").style.display = "none";
            document.getElementById("tr_word").style.display = "none";
        } else {
            var sel = opt.value;
            var html = ${mediatype_HTML};
            var word = ${mediatype_WORD};
            if (opt.value == html) {
                document.getElementById("tr_html").style.display = '';
                document.getElementById("tr_word").style.display = "none";
            }
            if (opt.value == word) {
                document.getElementById("tr_word").style.display = '';
                document.getElementById("tr_html").style.display = "none";
            }
        }
    }

</script>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(description)" enctype="multipart/form-data">

                <c:set var="templateId" value="${templateForm.dtoMap['templateId']}"/>
                <c:set var="description" value="${templateForm.dtoMap['description']}"/>


                <table id="Template.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">
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
                    <TR>
                        <TD colspan="4" class="title"><c:out value="${title}"/></TD>
                    </TR>

                    <TR>
                        <TD class="label" width="40%"><fmt:message key="Template.description"/></TD>
                        <TD colspan="2" class="contain" width="60%">
                            <app:text property="dto(description)" styleClass="largetext" maxlength="30"
                                      view="${op == 'delete'}"/>
                        </TD>
                    </TR>

                    <TR>
                        <TD class="label" width="40%"><fmt:message key="Template.mediaType"/></TD>
                        <TD colspan="2" class="contain" width="60%">
                            <c:set var="templateTypesList" value="${app2:getTemplateMediaType(pageContext.request)}"/>

                            <html:select property="dto(mediaType)" styleClass="mediumSelect"
                                         readonly="${op == 'delete' || 'update' == op}"
                                         onchange="javascript:selectDocument(this);"
                                         onkeyup="javascript:selectDocument(this);">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="templateTypesList" property="value" labelProperty="label"/>
                            </html:select>
                        </TD>
                    </TR>

                    <c:if test="${op == 'create'}">

                        <TR>
                            <TD class="label" width="40%"><fmt:message key="Template.language"/></TD>
                            <TD class="contain" width="60%">
                                <fanta:select property="dto(languageId)" listName="languageList" labelProperty="name"
                                              valueProperty="id" firstEmpty="true" styleClass="select"
                                              readOnly="${op == 'delete'}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                            </TD>
                        </TR>

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

                        <TR id="tr_word" style="${displayWord}">
                            <TD class="label" width="40%"><fmt:message key="Template.file"/></TD>
                            <TD class="contain" width="60%">
                                <html:file property="dto(file)" accept="application/msword"/>
                            </TD>
                        </TR>

                        <tr id="tr_html" style="${displayHtml}">
                            <td class="topLabel" colspan="2">
                                <fmt:message key="Template.htmlText"/>

                                <tags:initTinyMCE4 textAreaId="body_field" addElwisPlugin="true"
                                                       addDefaultBodyStylePlugin="true" addBrowseImagePlugin="true"
                                                       addHtmlSourceEditButton="true"/>
                                <html:textarea property="dto(editor)" styleClass="webmailBody" styleId="body_field"
                                               style="height:480px;width:550px;"/>
                            </td>
                        </tr>
                    </c:if>

                </table>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="TEMPLATE"
                                                 styleClass="button">${button}</app2:securitySubmit>

                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="TEMPLATE" styleClass="button"
                                                     property="SaveAndNew"><fmt:message key="Common.saveAndNew"/>
                                </app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>
        </td>
    </tr>

</table>


<c:if test="${op=='update'}">
    <iframe name="frame1"
            src="<app:url value="/Template/TemplateFileList.do?parameter(templateId)=${templateId}&dto(templateId)=${templateId}&dto(description)=${app2:encode(description)}&parameter(description)=${app2:encode(description)}&dto(mediaType)=${templateForm.dtoMap['mediaType']}" />"
            class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
    </iframe>
</c:if>
