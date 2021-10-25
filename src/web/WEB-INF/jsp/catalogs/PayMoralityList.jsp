<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="PAYMORALITY" permission="CREATE">
        <html:form styleId="CREATE_NEW_PAYMORALITY"
                   action="/PayMorality/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="payMoralityList"
                     width="100%"
                     id="payMorality"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="PayMorality/List.do"
                     imgPath="${baselayout}">

            <c:set var="editAction"
                   value="PayMorality/Forward/Update.do?dto(payMoralityId)=${payMorality.id}&dto(payMoralityName)=${app2:encode(payMorality.name)}"/>
            <c:set var="deleteAction"
                   value="PayMorality/Forward/Delete.do?dto(withReferences)=true&dto(payMoralityId)=${payMorality.id}&dto(payMoralityName)=${app2:encode(payMorality.name)}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PAYMORALITY" permission="VIEW">
                    <fanta:actionColumn name="up"
                                        title="Common.update"
                                        action="${editAction}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>

                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PAYMORALITY" permission="DELETE">
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
                              title="PayMorality.name"
                              headerStyle="listHeader"
                              width="95%"
                              orderable="true"
                              maxLength="50"/>
        </fanta:table>
    </div>
</div>