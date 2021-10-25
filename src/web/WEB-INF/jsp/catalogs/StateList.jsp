<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("stages", JSPHelper.getStageTypesState(request, false));
%>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="STATE" permission="CREATE">
        <html:form action="${create}">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()} "><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%" id="state" styleClass="${app2:getFantabulousTableClases()}" action="${action}"
                     imgPath="${baselayout}">
            <c:set var="editAction" value="${edit}?dto(stateName)=${app2:encode(state.name)}&dto(stateId)=${state.id}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(stateId)=${state.id}&dto(stateName)=${app2:encode(state.name)}"/>
            <c:set var="translateAction"
                   value="Support/State/Translate.do?dto(stateId)=${state.id}&dto(op)=read&dto(stateName)=${app2:encode(state.name)}"/>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphRefresh()}"/>

                <app2:checkAccessRight functionality="STATE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="STATE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="State.name"
                              headerStyle="listHeader" width="50%" orderable="true" maxLength="30"/>
            <fanta:dataColumn name="sequence" styleClass="listItemRight" title="State.sequence"
                              headerStyle="listHeader" width="20%" orderable="true"/>
            <fanta:dataColumn name="" styleClass="listItem2" title="State.stageType" headerStyle="listHeader"
                              width="25%" renderData="false">
                ${stages[state.stageType].label}
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>