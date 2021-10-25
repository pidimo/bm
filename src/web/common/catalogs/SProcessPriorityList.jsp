<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
    <app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
<html:form styleId="CREATE_NEW_PRIORITY" action="/SProcessPriority/Forward/Create.do?op=create">
        <TD class="button">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </TD>
</html:form>
    </TR>
    </table>
    </app2:checkAccessRight>
    <TABLE id="SProcessPriorityList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="sProcessPriorityList" width="100%" id="priority" action="SProcessPriority/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="SProcessPriority/Forward/Update.do?dto(priorityName)=${app2:encode(priority.name)}&dto(priorityId)=${priority.id}"/>
                <c:set var="deleteAction" value="SProcessPriority/Forward/Delete.do?dto(withReferences)=true&dto(priorityId)=${priority.id}&dto(priorityName)=${app2:encode(priority.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Priority.name"  headerStyle="listHeader" width="70%" orderable="true" maxLength="30" />
                <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="Priority.sequence"  headerStyle="listHeader" width="30%" orderable="true" />
            </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>
