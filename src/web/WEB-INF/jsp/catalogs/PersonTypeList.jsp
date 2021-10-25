<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="PERSONTYPE" permission="CREATE">
        <html:form styleId="CREATE_NEW_PERSONTYPE" action="/PersonType/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive" id="PersonTypeList.jsp">
        <fanta:table mode="bootstrap" list="personTypeList" styleClass="${app2:getFantabulousTableClases()}"
                     id="personType" action="PersonType/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="PersonType/Forward/Update.do?dto(personTypeId)=${personType.id}&dto(personTypeName)=${app2:encode(personType.name)}"/>
            <c:set var="deleteAction"
                   value="PersonType/Forward/Delete.do?dto(withReferences)=true&dto(personTypeId)=${personType.id}&dto(personTypeName)=${app2:encode(personType.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PERSONTYPE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PERSONTYPE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2"
                              title="PersonType.name" headerStyle="listHeader" width="95%"
                              orderable="true" maxLength="25">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>
