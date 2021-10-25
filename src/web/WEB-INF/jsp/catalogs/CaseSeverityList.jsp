<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${create}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="CASESEVERITY"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="caseSeverityList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="severity"
                     action="${action}" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="${edit}?dto(severityName)=${app2:encode(severity.name)}&dto(severityId)=${severity.id}&dto(langTextId)=${severity.langTextId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(severityId)=${severity.id}&dto(severityName)=${app2:encode(severity.name)}"/>
            <c:set var="translateAction"
                   value="Support/CaseSeverity/Translate.do?dto(op)=read&dto(severityId)=${severity.id}&dto(severityName)=${app2:encode(severity.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphRefresh()}"/>

                <app2:checkAccessRight functionality="CASESEVERITY" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CASESEVERITY" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="CaseSeverity.name"
                              headerStyle="listHeader" width="65%" orderable="true" maxLength="30"/>
            <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="Priority.sequence"
                              headerStyle="listHeader" width="30%" orderable="true"/>
        </fanta:table>
    </div>
</div>