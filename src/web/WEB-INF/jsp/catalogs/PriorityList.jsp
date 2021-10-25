<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="${function}" permission="CREATE">
        <html:form styleId="CREATE_NEW_PRIORITY" action="${create}?op=create">
            <app2:checkAccessRight functionality="${function}" permission="CREATE">
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </div>
            </app2:checkAccessRight>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="priorityList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="priority"
                     action="${action}" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="${edit}?dto(priorityName)=${app2:encode(priority.name)}&dto(priorityId)=${priority.id}&dto(langTextId)=${priority.langTextId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(priorityId)=${priority.id}&dto(priorityName)=${app2:encode(priority.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="${function}" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="${function}" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Priority.name"
                              headerStyle="listHeader" width="65%" orderable="true" maxLength="30"/>
            <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="Priority.sequence"
                              headerStyle="listHeader" width="30%" orderable="true"/>
        </fanta:table>
    </div>
</div>

