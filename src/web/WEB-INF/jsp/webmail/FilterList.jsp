<%@ include file="/Includes.jsp" %>

<html:form action="/Mail/Forward/CreateFilter.do">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="create" functionality="WEBMAILFILTER"
                             styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                key="Common.new"/></app2:securitySubmit>
    </div>
</html:form>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="filterList" width="100%" id="listF"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="Mail/Forward/ListFilter.do" imgPath="${baselayout}"
                 align="center">

        <app:url var="urlEdit" value="Mail/Forward/UpdateFilter.do?dto(filterId)=${listF.id_filter}&dto(op)=read"
                 enableEncodeURL="false"/>
        <app:url var="urlDelete" value="Mail/Forward/DeleteFilter.do?dto(filterId)=${listF.id_filter}&dto(op)=read"
                 enableEncodeURL="false"/>

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="WEBMAILFILTER" permission="VIEW">
                <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="WEBMAILFILTER" permission="DELETE">
                <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete" action="${urlDelete}"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>

        </fanta:columnGroup>

        <fanta:dataColumn name="name_filter" title="Webmail.filter.tiltle.name" action="${urlEdit}"
                          styleClass="listItem"
                          headerStyle="listHeader" width="50%" orderable="true">
        </fanta:dataColumn>

        <c:choose>
            <c:when test="${listF.type_folder==4}">
                <fanta:dataColumn name="" title="Webmail.filter.connectedToFolder" styleClass="listItem2"
                                  renderData="false"
                                  headerStyle="listHeader" width="45%" orderable="true">
                    <fmt:message key="Webmail.folder.trash"/>
                </fanta:dataColumn>
            </c:when>
            <c:otherwise>
                <fanta:dataColumn name="name_folder" title="Webmail.filter.connectedToFolder" styleClass="listItem2"
                                  headerStyle="listHeader" width="45%" orderable="true">
                </fanta:dataColumn>
            </c:otherwise>
        </c:choose>
    </fanta:table>
</div>

