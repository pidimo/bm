<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="ACTIONTYPE" permission="CREATE">

        <html:form action="/ActionType/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="actionTypeList" styleClass="${app2:getFantabulousTableClases()}" id="actionType"
                     action="ActionType/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="ActionType/Forward/Update.do?dto(actionTypeName)=${app2:encode(actionType.name)}&dto(actionTypeId)=${actionType.id}"/>
            <c:set var="deleteAction"
                   value="ActionType/Forward/Delete.do?dto(withReferences)=true&dto(actionTypeId)=${actionType.id}&dto(actionTypeName)=${app2:encode(actionType.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="ACTIONTYPE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="ACTIONTYPE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="ActionType.name"
                              headerStyle="listHeader" width="50%" orderable="true" maxLength="30"/>
            <fanta:dataColumn name="probability" styleClass="listItemRight" title="ActionType.probability"
                              headerStyle="listHeader" width="30%" orderable="true" maxLength="30" renderData="false">
                ${actionType.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="ActionType.sequence"
                              headerStyle="listHeader" width="20%" orderable="true"/>
        </fanta:table>
    </div>
</div>