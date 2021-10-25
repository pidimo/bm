<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
   <td>
   <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
       <tr>
            <html:form action="${create}">
                <TD class="button">
                  <app2:checkAccessRight functionality="TASKTYPE" permission="CREATE">
                     <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                   </app2:checkAccessRight>
                </TD>
            </html:form>
       </tr>
   </table>

   <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
   <tr align="center">
       <td>
       <fanta:table list="taskTypeList" width="100%" id="taskType" action="${action}" imgPath="${baselayout}">
            <c:set var="editAction" value="${edit}?dto(name)=${app2:encode(taskType.taskTypeName)}&dto(taskTypeId)=${taskType.taskTypeId}"/>
            <c:set var="deleteAction" value="${delete}?dto(withReferences)=true&dto(name)=${app2:encode(taskType.taskTypeName)}&dto(taskTypeId)=${taskType.taskTypeId}"/>
             <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="TASKTYPE" permission="VIEW">
                   <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="TASKTYPE" permission="DELETE">
                   <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                </app2:checkAccessRight>
                </fanta:columnGroup>
             <fanta:dataColumn name="taskTypeName" action="${editAction}" styleClass="listItem2" title="TaskType.name"  headerStyle="listHeader" width="95%" orderable="true" maxLength="30" />
        </fanta:table>
       </td>
   </tr>
   </table>
   </td>
</tr>
</table>
