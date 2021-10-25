<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="STATUS" permission="CREATE">
        <html:form action="/Status/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="statusList" styleClass="${app2:getFantabulousTableClases()}" width="100%" id="status"
                     action="Status/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Status/Forward/Update.do?dto(statusId)=${status.statusId}&dto(statusName)=${app2:encode(status.statusName)}"/>
            <c:set var="deleteAction"
                   value="Status/Forward/Delete.do?dto(withReferences)=true&dto(statusId)=${status.statusId}&dto(statusName)=${app2:encode(status.statusName)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="STATUS" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="STATUS" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="statusName" action="${editAction}" styleClass="listItem" title="Status.name"
                              headerStyle="listHeader" width="50%" orderable="true" maxLength="80"/>
            <fanta:dataColumn name="" styleClass="listItem2" title="Status.isFinal" headerStyle="listHeader" width="50%"
                              orderable="false" renderData="false">
                <c:if test="${status.isFinal == '1'}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>


