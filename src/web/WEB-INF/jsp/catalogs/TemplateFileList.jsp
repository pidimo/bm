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

<div class="${app2:getListWrapperClasses()}">
    <div id="tableId">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app:url value="/Template/Forward/FileCreate.do?dto(check)=true" var="url"/>
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="new"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:submit>
        </div>
        <fieldset>
            <legend class="title">
                <fmt:message key="${windowTitle}"/>
            </legend>
        </fieldset>
        <div class="table-responsive">
            <fanta:table mode="bootstrap" list="templateFreeTextList" width="100%" id="file"
                         styleClass="${app2:getFantabulousTableClases()}"
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
                                            glyphiconClass="${app2:getClassGlyphPrioHigh()}"/>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="TEMPLATE" permission="UPDATE">
                        <fanta:actionColumn name="" title="Common.update" useJScript="true"
                                            action="javascript:goParentURL('${updateAction}')"
                                            styleClass="listItem" headerStyle="listHeader"
                                            glyphiconClass="${app2:getClassGlyphEdit()}"/>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="TEMPLATE" permission="DELETE">
                        <c:if test="${file.templateByDefault != 1}">
                            <fanta:actionColumn name="del" title="Common.delete" useJScript="true"
                                                action="javascript:goParentURL('${deleteAction}')"
                                                styleClass="listItem" headerStyle="listHeader"
                                                glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
                                                glyphiconClass="${app2:getClassGlyphOpenFolder()}"/>
                        </app2:checkAccessRight>
                    </c:if>

                </fanta:columnGroup>
                <fanta:dataColumn name="languageOfTemplate" useJScript="true"
                                  action="javascript:goParentURL('${updateAction}')" styleClass="listItem"
                                  title="Template.files" headerStyle="listHeader" width="50%"/>
                <fanta:dataColumn name="templateByDefault" styleClass="listItem2" title="Template.default"
                                  headerStyle="listHeader" width="45%" renderData="false">
                    <c:if test="${file.templateByDefault == 1}">
                        <span class="${app2:getClassGlyphOk()}"></span>
                    </c:if>&nbsp;
                </fanta:dataColumn>
            </fanta:table>
        </div>
    </div>
</div>