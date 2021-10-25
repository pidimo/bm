<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="ADDRESSSOURCE" permission="CREATE">
        <html:form styleId="CREATE_NEW_ADDRESSSOURCE" action="/AddressSource/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="addressSourceList" styleClass="${app2:getFantabulousTableClases()}" id="addressSource"
                     action="AddressSource/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="AddressSource/Forward/Update.do?dto(addressSourceId)=${addressSource.id}&dto(addressSourceName)=${app2:encode(addressSource.name)}"/>
            <c:set var="deleteAction"
                   value="AddressSource/Forward/Delete.do?dto(withReferences)=true&dto(addressSourceId)=${addressSource.id}&dto(addressSourceName)=${app2:encode(addressSource.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="ADDRESSSOURCE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="ADDRESSSOURCE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="AddressSource.name"
                              headerStyle="listHeader" width="95%" orderable="true" maxLength="25"/>
        </fanta:table>
    </div>
</div>


