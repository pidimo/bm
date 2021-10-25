<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>
<%
    request.setAttribute("templateId", request.getParameter("parameter(templateId)"));
    request.setAttribute("documentType", request.getParameter("parameter(documentType)"));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<c:set var="word_template"><%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>
</c:set>
<c:set var="html_template"><%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>
</c:set>

<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>

<div class="${app2:getListWrapperClasses()}" id="tableId">

    <app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="CREATE">
        <app:url
                value="/Template/File/Forward/Create.do?templateId=${templateId}&dto(templateId)=${templateId}&documentType=${documentType}"
                var="url"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="new"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:submit>
        </div>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="campaignTemplateList" width="100%" id="file"
                     action="Template/File/List.do"
                     styleClass="${app2:getFantabulousTableClases()}"
                     imgPath="${baselayout}">

            <app:url var="editAction"
                     value="Template/File/Forward/Update.do?templateId=${file.templateId}&documentType=${documentType}&dto(templateId)=${file.templateId}&dto(languageId)=${file.languageId}&dto(documentType)=${documentType}"/>

            <app:url var="deleteAction"
                     value="Template/File/Forward/Delete.do?templateId=${file.templateId}&documentType=${documentType}&dto(templateId)=${file.templateId}&dto(languageId)=${file.languageId}&dto(documentType)=${documentType}&dto(checkDefaultTemplate)=true"/>

            <app:url var="downloadAction"
                     value="Template/File/Download.do?templateId=${file.templateId}&documentType=${documentType}&dto(templateId)=${file.templateId}&dto(languageId)=${file.languageId}&dto(documentType)=${documentType}&dto(freeTextId)=${file.freeTextId}&dto(mod)=campaignTemplate"/>

            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">

                <fanta:actionColumn name="" title="Common.update"
                                    useJScript="true"
                                    action="javascript:goParentURL('${editAction}')"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}" />

                <c:if test="${file.isDefault != 1}">
                    <fanta:actionColumn name="" title="Common.delete"
                                        useJScript="true"
                                        action="javascript:goParentURL('${deleteAction}')"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}" />
                </c:if>
                <c:if test="${file.isDefault == 1}">
                    <fanta:actionColumn name="" title="" styleClass="listItem"
                                        headerStyle="listHeader">
                        &nbsp;
                    </fanta:actionColumn>
                </c:if>

                <c:if test="${word_template == documentType}">
                    <fanta:actionColumn name="download" title="Common.download" useJScript="true"
                                        action="javascript:goParentURL('${downloadAction}')"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphDownload()}" />
                </c:if>
            </fanta:columnGroup>

            <fanta:dataColumn name="languageName" useJScript="true"
                              action="javascript:goParentURL('${editAction}')"
                              styleClass="listItem"
                              title="Template.files" headerStyle="listHeader" width="40%"
                              orderable="true" maxLength="25"/>

            <fanta:dataColumn name="isDefault" styleClass="listItem"
                              title="Template.byDefault" headerStyle="listHeader" width="40%"
                              maxLength="25" renderData="false">
                <c:if test="${file.isDefault == 1}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
                &nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="size" styleClass="listItem2Right" title="Template.size"
                              headerStyle="listHeader"
                              width="15%" renderData="false" orderable="true" nowrap="true">
                <c:if test="${not empty file.size}">
                    <c:choose>
                        <c:when test="${file.size < 1024}">
                            ${1}
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${fn:substringBefore(file.size/1024,'.')}"
                                              type="number" pattern="${numberFormat}"/>
                        </c:otherwise>
                    </c:choose>
                    &nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
                </c:if>

            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>