<%@ include file="/Includes.jsp" %>

<app2:checkAccessRight functionality="SALUTATION" permission="CREATE">
    <html:form styleId="CREATE_NEW_SALUTATION" action="/Salutation/Forward/Create.do?op=create">
        <div class="form-group">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                    key="Common.new"/></html:submit>
        </div>
    </html:form>
</app2:checkAccessRight>
<div id="SalutationList.jsp" class="table-responsive">
    <fanta:table mode="bootstrap" list="salutationList" styleClass="${app2:getFantabulousTableClases()}"  id="salutation" action="Salutation/List.do" imgPath="${baselayout}">
        <c:set var="editAction"
               value="Salutation/Forward/Update.do?dto(salutationId)=${salutation.id}&dto(salutationLabel)=${app2:encode(salutation.label)}"/>
        <c:set var="deleteAction"
               value="Salutation/Forward/Delete.do?dto(withReferences)=true&dto(salutationId)=${salutation.id}&dto(salutationLabel)=${app2:encode(salutation.label)}"/>
        <c:set var="translationAction"
               value="Salutation/Forward/translation.do?op=create&dto(salutationId)=${salutation.id}&dto(salutationLabel)=${app2:encode(salutation.label)}&dto(letterTextId)=${salutation.letterPartId}&dto(addressTextId)=${salutation.addressPartId}&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="SALUTATION" permission="VIEW">
                <fanta:actionColumn name="trans" title="Common.translate" action="${translationAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphRefresh()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALUTATION" permission="VIEW">
                <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALUTATION" permission="DELETE">
                <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="label" action="${editAction}" styleClass="listItem2" title="Salutation.label"
                          headerStyle="listHeader" width="95%" orderable="true" maxLength="25">
        </fanta:dataColumn>
    </fanta:table>
</div>


