<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">

    <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="CREATE">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:form action="/ProjectActivity/Forward/Create.do">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </html:form>
        </div>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="projectActivityList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="projectActivity"
                     action="ProjectActivity/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="ProjectActivity/Forward/Update.do?dto(activityId)=${projectActivity.projectActivityId}&dto(name)=${app2:encode(projectActivity.activityName)}"/>
            <c:set var="deleteLink"
                   value="ProjectActivity/Forward/Delete.do?dto(activityId)=${projectActivity.projectActivityId}&dto(name)=${app2:encode(projectActivity.activityName)}&dto(withReferences)=true"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="activityName" action="${editLink}"
                              styleClass="listItem" title="ProjectActivity.name"
                              headerStyle="listHeader" orderable="true"
                              maxLength="40"
                              width="95%"/>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="CREATE">
        <html:form action="/ProjectActivity/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>