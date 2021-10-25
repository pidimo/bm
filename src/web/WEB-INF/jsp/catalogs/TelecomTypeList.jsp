<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="TELECOMTYPE" permission="CREATE">
        <html:form styleId="CREATE_NEW_TELECOMTYPE" action="/TelecomType/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive" id="TelecomTypeList.jsp">
        <fanta:table mode="bootstrap" list="telecomTypeList" styleClass="${app2:getFantabulousTableClases()}" id="telecomType"
                     action="TelecomType/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="TelecomType/Forward/Update.do?dto(telecomTypeId)=${telecomType.id}&dto(telecomTypeName)=${app2:encode(telecomType.name)}&dto(langTextId)=${telecomType.translationId}"/>
            <c:set var="deleteAction"
                   value="TelecomType/Forward/Delete.do?dto(withReferences)=true&dto(telecomTypeId)=${telecomType.id}&dto(telecomTypeName)=${app2:encode(telecomType.name)}&dto(langTextId)=${telecomType.translationId}"/>

            <c:set var="translateAction"
                   value="TelecomType/Translate.do?dto(telecomTypeId)=${telecomType.id}&dto(op)=read&dto(telecomTypeName)=${app2:encode(telecomType.name)}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader">

                <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphRefresh()}"/>

                <app2:checkAccessRight functionality="TELECOMTYPE" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="TELECOMTYPE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="TelecomType.name"
                              headerStyle="listHeader" width="35%" orderable="true" maxLength="25"/>
            <fanta:dataColumn name="position" styleClass="listItemRight" title="TelecomType.position"
                              headerStyle="listHeader" width="35%" orderable="true"/>
            <fanta:dataColumn name="type" styleClass="listItem2" title="TelecomType.type" headerStyle="listHeader"
                              width="30%" orderable="true" renderData="false">
                <c:set var="telecomTypeConstant" value="${telecomType.type}" scope="request"/>
                <%
                    String constant = (String) request.getAttribute("telecomTypeConstant");
                    String key = JSPHelper.getTelecomType(constant, request);
                    request.setAttribute("constantKey", key);
                %>
                ${constantKey}&nbsp;
            </fanta:dataColumn>
        </fanta:table>
        <c:out value="${sessionScope.listshadow}" escapeXml="false"/><img
            src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
    </div>
</div>