<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="PRODUCTUNIT" permission="VIEW">
        <html:form action="/ProductUnit/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div id="ProductUnitList.jsp" class="table-responsive">
        <fanta:table mode="bootstrap" list="productUnitList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="unit"
                     action="ProductUnit/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="ProductUnit/Forward/Update.do?dto(unitId)=${unit.id}&dto(unitName)=${app2:encode(unit.name)}"/>
            <c:set var="deleteAction"
                   value="ProductUnit/Forward/Delete.do?dto(withReferences)=true&dto(unitId)=${unit.id}&dto(unitName)=${app2:encode(unit.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PRODUCTUNIT" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PRODUCTUNIT" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="ProductUnit.name"
                              headerStyle="listHeader" width="95%" orderable="true" maxLength="25"/>
        </fanta:table>
    </div>

</div>
