<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="mediatype_WORD" value="<%=CatalogConstants.MediaType.WORD.getConstantAsString()%>"/>
<c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>

<c:set var="templateId" value="${listForm.params['templateId']}"/>
<c:set var="description" value="${listForm.params['description']}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<%
    request.setAttribute("mediaType", request.getParameter("dto(mediaType)"));
%>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" id="tableId">
    <tr>
        <td>

            <app:url value="/Template/Forward/FileCreate.do?dto(check)=true" var="url"/>
            <table cellpadding="0" cellspacing="0" border="0" width="60%" align="center">
                <TR>
                    <TD colspan="6" align="left">
                        <html:submit styleClass="button" property="new" onclick="window.parent.location='${url}'">
                            <fmt:message key="Common.new"/>
                        </html:submit>
                    </TD>
                </TR>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
                <tr>
                    <td class="title"><fmt:message key="${windowTitle}"/></td>
                </tr>
                <TR>
                    <TD>


                        <fanta:table list="templateFreeTextList" width="100%" id="file"
                                     action="Template/TemplateFileList.do?dto(templateId)=${templateId}&dto(description)=${description}&dto(mediaType)=${mediaType}"
                                     imgPath="${baselayout}">

                            <app:url var="deleteAction"
                                     value="TemplateFile/Forward/Delete.do?dto(op)=deleteFileConfirmation&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(maxAttachSize)=${sessionScope.user.valueMap['maxAttachSize']}&dto(languageId)=${file.languageId}&dto(description)=${app2:encode(description)}&dto(mediaType)=${mediaType}"/>
                            <app:url var="defaultAction"
                                     value="TemplateFile/Default.do?dto(op)=changeDefault&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(maxAttachSize)=${sessionScope.user.valueMap['maxAttachSize']}&dto(languageId)=${file.languageId}&dto(description)=${app2:encode(description)}&dto(mediaType)=${mediaType}"/>
                            <app:url var="updateAction"
                                     value="TemplateFile/Forward/Update.do?dto(op)=read&dto(languageId)=${file.languageId}&dto(description)=${app2:encode(description)}&dto(mediaType)=${mediaType}"/>

                            <app:url var="downloadAction"
                                     value="TemplateFile/Forward/Download.do?dto(templateId)=${templateId}&dto(fid)=${file.fileOfTemplateId}"/>
                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                                <app2:checkAccessRight functionality="TEMPLATE" permission="VIEW">
                                    <fanta:actionColumn name="default" title="TemplateFile.setDefault" useJScript="true"
                                                        action="javascript:goParentURL('${defaultAction}')"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/webmail/prio_high.gif"/>
                                </app2:checkAccessRight>

                                <app2:checkAccessRight functionality="TEMPLATE" permission="UPDATE">
                                    <fanta:actionColumn name="" title="Common.update" useJScript="true"
                                                        action="javascript:goParentURL('${updateAction}')"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>

                                <app2:checkAccessRight functionality="TEMPLATE" permission="DELETE">
                                    <c:if test="${file.templateByDefault != 1}">
                                        <fanta:actionColumn name="del" title="Common.delete" useJScript="true"
                                                            action="javascript:goParentURL('${deleteAction}')"
                                                            styleClass="listItem" headerStyle="listHeader"
                                                            image="${baselayout}/img/delete.gif"/>
                                    </c:if>
                                    <c:if test="${file.templateByDefault == 1}">
                                        <fanta:actionColumn name="del" title="" styleClass="listItem"
                                                            headerStyle="listHeader">&nbsp;</fanta:actionColumn>
                                    </c:if>
                                </app2:checkAccessRight>

                                <c:if test="${mediaType eq mediatype_WORD}">
                                    <app2:checkAccessRight functionality="TEMPLATE" permission="VIEW">
                                        <fanta:actionColumn name="download" title="Common.download" useJScript="true"
                                                            action="javascript:goParentURL('${downloadAction}')"
                                                            styleClass="listItem" headerStyle="listHeader"
                                                            image="${baselayout}/img/openfile.png"/>
                                    </app2:checkAccessRight>
                                </c:if>

                            </fanta:columnGroup>
                            <fanta:dataColumn name="languageOfTemplate" useJScript="true"
                                              action="javascript:goParentURL('${updateAction}')" styleClass="listItem"
                                              title="Template.files" headerStyle="listHeader" width="50%"/>
                            <fanta:dataColumn name="templateByDefault" styleClass="listItem2" title="Template.default"
                                              headerStyle="listHeader" width="45%" renderData="false">
                                <c:if test="${file.templateByDefault == 1}">
                                    <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>
                                </c:if>&nbsp;
                            </fanta:dataColumn>

                        </fanta:table>
                    </TD>
                </TR>
            </table>
        </td>
    </tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>