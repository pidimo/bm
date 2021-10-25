<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
<td>
    <app2:checkAccessRight functionality="TEMPLATE" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form styleId="CREATE_NEW_TEMPLATE" action="/Template/Forward/Create.do?op=create">
            <TD class="button">
                <html:hidden property="dto(operation)" value="create"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>
    
    <TABLE id="TemplateList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
    <td>
        <fanta:table list="templateList" width="100%" id="template" action="Template/List.do" imgPath="${baselayout}"  >

            <c:set var="editAction" value="Template/Forward/Update.do?op=update&dto(templateId)=${template.id}&dto(description)=${app2:encode(template.description)}"/>
                <c:set var="deleteAction" value="Template/Forward/Delete.do?op=delete&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(operation)=delete&dto(templateId)=${template.id}&dto(description)=${app2:encode(template.description)}"/>
                <c:set var="listTemplateFiles" value="Template/Forward/Update.do?op=update&dto(templateId)=${template.id}&dto(description)=${app2:encode(template.description)}" />
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="TEMPLATE" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${listTemplateFiles}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="TEMPLATE" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="description" action="${editAction}" styleClass="listItem" title="Template.description"  headerStyle="listHeader" width="75%" orderable="true" maxLength="35" />
                <fanta:dataColumn name="mediaType" styleClass="listItem2" title="Template.mediaType"  headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                    <c:out value="${app2:getMediaTypeMessage(template.mediaType, pageContext.request)}"/>
                </fanta:dataColumn>
        </fanta:table>
    </td>
    </tr>
    </table>
</td>
</tr>
</table>
