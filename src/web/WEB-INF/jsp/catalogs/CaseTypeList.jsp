<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${create}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="CASETYPE" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="10">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="caseTypeList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="caseType"
                     action="${action}"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="${edit}?dto(caseTypeName)=${app2:encode(caseType.name)}&dto(caseTypeId)=${caseType.id}&dto(langTextId)=${caseType.langTextId}"/>
            <c:set var="deleteAction"
                   value="${delete}?dto(withReferences)=true&dto(caseTypeId)=${caseType.id}&dto(caseTypeName)=${app2:encode(caseType.name)}"/>

            <c:set var="translateAction"
                   value="Support/CaseType/Translate.do?dto(caseTypeId)=${caseType.id}&dto(caseTypeName)=${app2:encode(caseType.name)}&dto(op)=read"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphRefresh()}"/>

                <app2:checkAccessRight functionality="CASETYPE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CASETYPE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2"
                              title="CaseType.Name" headerStyle="listHeader" width="95%"
                              orderable="true" maxLength="30"/>
        </fanta:table>
    </div>
</div>
