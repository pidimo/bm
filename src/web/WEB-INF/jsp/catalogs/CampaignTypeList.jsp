<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="CREATE">
        <html:form action="/Campaign/CampaignType/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="campaignTypeList" styleClass="${app2:getFantabulousTableClases()}" id="campaignType"
                     action="Campaign/CampaignTypeList.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Campaign/CampaignType/Forward/Update.do?dto(campaignTypeId)=${campaignType.id}&dto(title)=${app2:encode(campaignType.title)}"/>
            <c:set var="deleteAction"
                   value="Campaign/CampaignType/Forward/Delete.do?dto(withReferences)=true&dto(campaignTypeId)=${campaignType.id}&dto(title)=${app2:encode(campaignType.title)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem"
                              title="CampaignType.title" headerStyle="listHeader" width="95%"
                              orderable="true" maxLength="25"/>
        </fanta:table>
    </div>
</div>