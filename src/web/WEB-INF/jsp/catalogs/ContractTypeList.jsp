<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="CONTRACTTYPE" permission="CREATE">
        <html:form action="/ContractType/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="contractTypeList" styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" id="contractType"
                     action="ContractType/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="ContractType/Forward/Update.do?dto(contractTypeId)=${contractType.contractTypeId}&dto(name)=${app2:encode(contractType.name)}"/>
            <c:set var="deleteAction"
                   value="ContractType/Forward/Delete.do?dto(withReferences)=true&dto(contractTypeId)=${contractType.contractTypeId}&dto(name)=${app2:encode(contractType.name)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="CONTRACTTYPE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CONTRACTTYPE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name"
                              action="${editAction}" styleClass="listItem"
                              title="ContractType.name" headerStyle="listHeader" width="70%"
                              orderable="true" maxLength="25">
            </fanta:dataColumn>

            <fanta:dataColumn name="tobeInvoiced" styleClass="listItem2"
                              title="ContractType.tobeInvoiced"
                              headerStyle="listHeader" width="25%" renderData="false">
                <c:if test="${contractType.tobeInvoiced == '1'}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

