<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="TEMPLATE" permission="CREATE">
        <html:form styleId="CREATE_NEW_TEMPLATE" action="/Template/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:hidden property="dto(operation)" value="create"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div id="TemplateList.jsp" class="table-responsive">
        <fanta:table mode="bootstrap" list="templateList" width="100%" id="template" styleClass="${app2:getFantabulousTableClases()}"
                     action="Template/List.do"
                     imgPath="${baselayout}">

            <c:set var="editAction"
                   value="Template/Forward/Update.do?op=update&dto(templateId)=${template.id}&dto(description)=${app2:encode(template.description)}"/>
            <c:set var="deleteAction"
                   value="Template/Forward/Delete.do?op=delete&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(operation)=delete&dto(templateId)=${template.id}&dto(description)=${app2:encode(template.description)}"/>
            <c:set var="listTemplateFiles"
                   value="Template/Forward/Update.do?op=update&dto(templateId)=${template.id}&dto(description)=${app2:encode(template.description)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="TEMPLATE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${listTemplateFiles}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="TEMPLATE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="description" action="${editAction}" styleClass="listItem"
                              title="Template.description" headerStyle="listHeader" width="75%" orderable="true"
                              maxLength="35"/>
            <fanta:dataColumn name="mediaType" styleClass="listItem2" title="Template.mediaType"
                              headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                <c:out value="${app2:getMediaTypeMessage(template.mediaType, pageContext.request)}"/>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>
