<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="COSTCENTERS" permission="CREATE">
        <html:form styleId="CREATE_NEW_COSTCENTER" action="/CostCenter/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/>
            </html:submit>
        </html:form>
        </div>
    </app2:checkAccessRight>

    <div id="CostCenterList.jsp" class="table-responsive">
        <fanta:table mode="bootstrap" list="costCenterList" width="100%" id="costCenter" styleClass="${app2:getFantabulousTableClases()}"
                     action="CostCenter/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="CostCenter/Forward/Update.do?dto(costCenterId)=${costCenter.id}&dto(costCenterName)=${app2:encode(costCenter.name)}&dto(parentCostCenterId)=${costCenter.itDependantId}"/>
            <c:set var="deleteAction"
                   value="CostCenter/Forward/Delete.do?dto(withReferences)=true&dto(costCenterId)=${costCenter.id}&dto(costCenterName)=${app2:encode(costCenter.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="COSTCENTERS" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="COSTCENTERS" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="CostCenter.name"
                              headerStyle="listHeader" width="50%" orderable="true" maxLength="25">
            </fanta:dataColumn>
            <fanta:dataColumn name="itDependant" styleClass="listItem2" title="CostCenter.group"
                              headerStyle="listHeader" width="50%" orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>