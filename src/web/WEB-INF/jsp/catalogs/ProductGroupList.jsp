<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="PRODUCTGROUP" permission="CREATE">
        <html:form styleId="CREATE_NEW_PRODUCTGROUP" action="/ProductGroup/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="productGroupList"
                     width="100%"
                     id="productGroup"
                     action="ProductGroup/List.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editAction"
                   value="ProductGroup/Forward/Update.do?dto(groupId)=${productGroup.id}&dto(groupName)=${app2:encode(productGroup.name)}"/>
            <c:set var="deleteAction"
                   value="ProductGroup/Forward/Delete.do?dto(groupId)=${productGroup.id}&dto(groupName)=${app2:encode(productGroup.name)}&dto(withReferences)=true"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="PRODUCTGROUP" permission="VIEW">
                    <fanta:actionColumn name="up"
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PRODUCTGROUP" permission="DELETE">
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
                              title="ProductGroup.name"
                              headerStyle="listHeader"
                              width="50%"
                              orderable="true"
                              maxLength="25"/>

            <fanta:dataColumn name="parentProductgroupName"
                              styleClass="listItem2"
                              title="ProductGroup.parentName"
                              headerStyle="listHeader"
                              width="50%"
                              orderable="true"/>

        </fanta:table>

    </div>
</div>