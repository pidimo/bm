<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${create}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="TASKTYPE" permission="CREATE">
                <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </app2:checkAccessRight>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="taskTypeList"
                     width="100%"
                     id="taskType"
                     action="${action}"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editAction"
                   value="${edit}?dto(name)=${app2:encode(taskType.taskTypeName)}&dto(taskTypeId)=${taskType.taskTypeId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(name)=${app2:encode(taskType.taskTypeName)}&dto(taskTypeId)=${taskType.taskTypeId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="TASKTYPE" permission="VIEW">
                    <fanta:actionColumn name=""
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>

                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="TASKTYPE" permission="DELETE">
                    <fanta:actionColumn name=""
                                        title="Common.delete"
                                        action="${deleteAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>

                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="taskTypeName"
                              action="${editAction}"
                              styleClass="listItem2"
                              title="TaskType.name"
                              headerStyle="listHeader"
                              width="95%"
                              orderable="true"
                              maxLength="30"/>
        </fanta:table>

    </div>
</div>