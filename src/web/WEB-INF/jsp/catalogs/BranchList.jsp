<%@ include file="/Includes.jsp" %>


<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="BRANCH" permission="CREATE">
        <html:form styleId="CREATE_NEW_BRANCH" action="/Branch/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive" id="BranchList.jsp">
        <fanta:table mode="bootstrap" list="branchList" styleClass="${app2:getFantabulousTableClases()}" id="branch"
                     action="Branch/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Branch/Forward/Update.do?dto(branchName)=${app2:encode(branch.name)}&dto(branchId)=${branch.id}"/>
            <c:set var="deleteAction"
                   value="Branch/Forward/Delete.do?dto(withReferences)=true&dto(branchId)=${branch.id}&dto(branchName)=${app2:encode(branch.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="BRANCH" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="BRANCH" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Branch.name"
                              headerStyle="listHeader" width="50%" orderable="true" maxLength="25"/>
            <fanta:dataColumn name="group" styleClass="listItem2" title="Branch.group" headerStyle="listHeader"
                              width="50%" orderable="true"/>
        </fanta:table>
        <c:out value="${sessionScope.listshadow}" escapeXml="false"/>
        <img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
    </div>
</div>