<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="SUPPLIERTYPE" permission="CREATE">
        <html:form styleId="CREATE_NEW_SUPPLIERTYPE" action="/SupplierType/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive" id="SupplierTypeList.jsp">

        <fanta:table mode="bootstrap" list="supplierTypeList"
                     width="100%"
                     id="supplierType"
                     action="SupplierType/List.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editAction"
                   value="SupplierType/Forward/Update.do?dto(supplierTypeId)=${supplierType.id}&dto(supplierTypeName)=${app2:encode(supplierType.name)}"/>
            <c:set var="deleteAction"
                   value="SupplierType/Forward/Delete.do?dto(withReferences)=true&dto(supplierTypeId)=${supplierType.id}&dto(supplierTypeName)=${app2:encode(supplierType.name)}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SUPPLIERTYPE" permission="VIEW">
                    <fanta:actionColumn name="up"
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SUPPLIERTYPE" permission="DELETE">
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
                              styleClass="listItem2"
                              title="SupplierType.name"
                              headerStyle="listHeader"
                              width="95%"
                              orderable="true"
                              maxLength="25">
            </fanta:dataColumn>

        </fanta:table>

    </div>
</div>