<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="CREATE">
        <html:form styleId="CREATE_NEW_PRIORITY" action="/SProcessPriority/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()} button"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive" id="SProcessPriorityList.jsp">
        <fanta:table mode="bootstrap" list="sProcessPriorityList"
                     width="100%" id="priority"
                     action="SProcessPriority/List.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editAction"
                   value="SProcessPriority/Forward/Update.do?dto(priorityName)=${app2:encode(priority.name)}&dto(priorityId)=${priority.id}"/>
            <c:set var="deleteAction"
                   value="SProcessPriority/Forward/Delete.do?dto(withReferences)=true&dto(priorityId)=${priority.id}&dto(priorityName)=${app2:encode(priority.name)}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="VIEW">
                    <fanta:actionColumn name=""
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="DELETE">
                    <fanta:actionColumn name=""
                                        title="Common.delete"
                                        action="${deleteAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name"
                              action="${editAction}"
                              styleClass="listItem"
                              title="Priority.name"
                              headerStyle="listHeader"
                              width="70%"
                              orderable="true"
                              maxLength="30"/>

            <fanta:dataColumn name="sequence"
                              styleClass="listItem2Right"
                              title="Priority.sequence"
                              headerStyle="listHeader"
                              width="30%"
                              orderable="true"/>
        </fanta:table>

    </div>
</div>