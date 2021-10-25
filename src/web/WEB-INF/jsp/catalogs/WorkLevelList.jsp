<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${create}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="WORKLEVEL"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="10">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="workLevelList"
                     width="100%"
                     id="workLevel"
                     action="${action}"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editAction"
                   value="${edit}?dto(workLevelName)=${app2:encode(workLevel.name)}&dto(workLevelId)=${workLevel.id}&dto(langTextId)=${workLevel.langTextId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(workLevelId)=${workLevel.id}&dto(workLevelName)=${app2:encode(workLevel.name)}"/>
            <c:set var="translateAction"
                   value="Support/WorkLevel/Translate.do?dto(op)=read&dto(workLevelName)=${app2:encode(workLevel.name)}&dto(workLevelId)=${workLevel.id}&dto(langTextId)=${workLevel.langTextId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <fanta:actionColumn name="trans"
                                    title="Common.translate"
                                    action="${translateAction}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphRefresh()}"/>

                <app2:checkAccessRight functionality="WORKLEVEL" permission="VIEW">
                    <fanta:actionColumn name="up"
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="WORKLEVEL" permission="DELETE">
                    <fanta:actionColumn name="del"
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
                              title="WorkLevel.name"
                              headerStyle="listHeader"
                              width="65%"
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