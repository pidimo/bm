<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="TITLE" permission="CREATE">
        <html:form styleId="CREATE_NEW_TITLE" action="/Title/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="titleList" styleClass="${app2:getFantabulousTableClases()}"
                     id="title"
                     action="Title/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Title/Forward/Update.do?dto(titleId)=${title.id}&dto(titleName)=${app2:encode(title.name)}"/>
            <c:set var="deleteAction"
                   value="Title/Forward/Delete.do?dto(withReferences)=true&dto(titleId)=${title.id}&dto(titleName)=${app2:encode(title.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="TITLE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="TITLE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="Title.name"
                              headerStyle="listHeader" width="95%" orderable="true" maxLength="25">
            </fanta:dataColumn>
        </fanta:table>
        <c:out value="${sessionScope.listshadow}" escapeXml="false"/><img
            src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
    </div>
</div>