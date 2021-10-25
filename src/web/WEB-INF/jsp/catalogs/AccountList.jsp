<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="ACCOUNT" permission="CREATE">
        <html:form action="/Account/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="accountList" width="100%" id="account" styleClass="${app2:getFantabulousTableClases()}"
                     action="Account/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Account/Forward/Update.do?dto(accountId)=${account.accountId}&dto(name)=${app2:encode(account.name)}"/>
            <c:set var="deleteAction"
                   value="Account/Forward/Delete.do?dto(withReferences)=true&dto(accountId)=${account.accountId}&dto(name)=${app2:encode(account.name)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="ACCOUNT" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="ACCOUNT" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name"
                              action="${editAction}" styleClass="listItem"
                              title="Account.name" headerStyle="listHeader" width="65%"
                              orderable="true" maxLength="25">
            </fanta:dataColumn>
            <fanta:dataColumn name="number"
                              action="${editAction}" styleClass="listItem2"
                              title="Account.number" headerStyle="listHeader" width="30%"
                              orderable="true" maxLength="25">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

