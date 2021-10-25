<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
<c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>

<table cellpadding="0" cellspacing="0" border="0" width="95%" align="center">
    <tr>
        <td>

            <html:form action="${action}" enctype="multipart/form-data">
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

            <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
                <TR>
                    <TD colspan="2" class="button">
                        <app2:checkAccessRight functionality="TEMPLATE" permission="UPDATE">
                            <html:submit property="dto(save)" styleClass="button" tabindex="1">${button}</html:submit>
                        </app2:checkAccessRight>
                        <html:cancel styleClass="button" tabindex="2"><fmt:message key="Common.cancel"/></html:cancel>
                    </TD>
                </TR>

                <TR>
                    <TD colspan="2" class="title"><c:out value="${title}"/></TD>
                </TR>

                <c:if test="${'uploadFile' == op}">
                    <tr>
                        <td class="label" width="40%"><fmt:message key="Template.language"/></td>
                        <td class="contain" width="60%">
                            <fanta:select property="dto(languageId)" listName="templateLanguageTagList"
                                          labelProperty="name" valueProperty="id" firstEmpty="true" styleClass="select"
                                          readOnly="${op == 'delete'}" tabIndex="3">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="templateTextTemplateId"
                                                 value="${not empty fileTemplateForm.dtoMap['templateId']?fileTemplateForm.dtoMap['templateId']:0}"/>
                            </fanta:select>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${'delete' == op || 'update' == op}">
                    <c:if test="${'delete' == op}">
                        <tr>
                            <td class="label" width="40%"><fmt:message key="Template.file"/></td>
                            <td class="contain" width="60%">${dto.templateDescription}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="label" width="40%"><fmt:message key="Template.language"/></td>
                        <td class="contain" width="60%">
                            <c:out value="${fileTemplateForm.dtoMap['languageName']}"/>
                            <html:hidden property="dto(languageId)"/>
                            <html:hidden property="dto(languageName)"/>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${mediatype_WORD eq fileTemplateForm.dtoMap['mediaType'] && 'delete' != op}">
                    <tr>
                        <td class="label" width="40%">
                            <fmt:message key="Template.file"/>
                        </td>
                        <td class="contain" width="60%">
                            <html:file property="dto(file)" accept="application/msword" tabindex="4"/>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${mediatype_HTML eq fileTemplateForm.dtoMap['mediaType'] && 'delete' != op}">

                    <tr>
                        <td class="topLabel" colspan="2">
                            <fmt:message key="Template.htmlText"/>

                            <tags:initTinyMCE4 textAreaId="body_field" addElwisPlugin="true"
                                                   addDefaultBodyStylePlugin="true" addBrowseImagePlugin="true"
                                                   addHtmlSourceEditButton="true"/>
                            <html:textarea property="dto(editor)" styleClass="webmailBody" styleId="body_field"
                                           style="height:480px;width:550px;" tabindex="5"/>
                        </td>
                    </tr>
                </c:if>

                <TR>
                    <TD colspan="2" class="button">
                        <app2:checkAccessRight functionality="TEMPLATE" permission="UPDATE">
                            <html:submit property="dto(save)" styleClass="button" tabindex="6">${button}</html:submit>
                        </app2:checkAccessRight>
                        <html:cancel styleClass="button" tabindex="7"><fmt:message key="Common.cancel"/></html:cancel>
                    </TD>
                </TR>
            </table>
            </html:form>

    </tr>
</table>