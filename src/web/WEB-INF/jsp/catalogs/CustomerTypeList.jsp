<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="CUSTOMERTYPE" permission="CREATE">
        <html:form styleId="CREATE_NEW_CUSTOMERTYPE" action="/CustomerType/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()} button"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="customerTypeList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="customerType"
                     action="CustomerType/List.do"
                     imgPath="${baselayout}">

            <c:set var="editAction"
                   value="CustomerType/Forward/Update.do?dto(customerTypeId)=${customerType.id}&dto(customerTypeName)=${app2:encode(customerType.name)}"/>
            <c:set var="deleteAction"
                   value="CustomerType/Forward/Delete.do?dto(withReferences)=true&dto(customerTypeId)=${customerType.id}&dto(customerTypeName)=${app2:encode(customerType.name)}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CUSTOMERTYPE" permission="VIEW">
                    <fanta:actionColumn name="up"
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="CUSTOMERTYPE" permission="DELETE">
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
                              title="CustomerType.name"
                              headerStyle="listHeader"
                              width="95%"
                              orderable="true"
                              maxLength="25"/>
        </fanta:table>
    </div>
</div>