<%@ include file="/Includes.jsp" %>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="REMINDERLEVEL" permission="CREATE">
        <html:form action="/ReminderLevel/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="reminderLevelList"
                     width="100%"
                     id="reminderLevel"
                     action="ReminderLevel/List.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editAction"
                   value="ReminderLevel/Forward/Update.do?reminderLevelId=${reminderLevel.reminderLevelId}&dto(reminderLevelId)=${reminderLevel.reminderLevelId}&dto(name)=${app2:encode(reminderLevel.name)}"/>
            <c:set var="deleteAction"
                   value="ReminderLevel/Forward/Delete.do?reminderLevelId=${reminderLevel.reminderLevelId}&dto(withReferences)=true&dto(reminderLevelId)=${reminderLevel.reminderLevelId}&dto(name)=${app2:encode(reminderLevel.name)}"/>

            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="REMINDERLEVEL" permission="VIEW">
                    <fanta:actionColumn name=""
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="REMINDERLEVEL" permission="UPDATE">
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
                              title="ReminderLevel.title"
                              headerStyle="listHeader"
                              width="30%"
                              orderable="true"
                              maxLength="25"/>

            <fanta:dataColumn name="level"
                              styleClass="listItemRight"
                              title="ReminderLevel.level"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              maxLength="25"/>

            <fanta:dataColumn name="numberOfDays"
                              styleClass="listItemRight"
                              title="ReminderLevel.numberOfDays"
                              headerStyle="listHeader"
                              width="25%"
                              orderable="true"
                              maxLength="25"/>

            <fanta:dataColumn name="fee"
                              styleClass="listItem2Right"
                              title="ReminderLevel.fee"
                              headerStyle="listHeader"
                              width="25%"
                              orderable="true"
                              maxLength="25"
                              renderData="false">
                <fmt:formatNumber value="${reminderLevel.fee}"
                                  type="number"
                                  pattern="${numberFormat}"/>
            </fanta:dataColumn>
        </fanta:table>

    </div>
</div>