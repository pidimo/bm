<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
    <app2:checkAccessRight functionality="ACTIONTYPE" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
<html:form action="/ActionType/Forward/Create.do">
        <TD colspan="6" class="button">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </TD>
</html:form>
    </TR>
    </table>
    </app2:checkAccessRight>
    <TABLE id="ActionTypeList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="actionTypeList" width="100%" id="actionType" action="ActionType/List.do" imgPath="${baselayout}">
                <c:set var="editAction" value="ActionType/Forward/Update.do?dto(actionTypeName)=${app2:encode(actionType.name)}&dto(actionTypeId)=${actionType.id}"/>
                <c:set var="deleteAction" value="ActionType/Forward/Delete.do?dto(withReferences)=true&dto(actionTypeId)=${actionType.id}&dto(actionTypeName)=${app2:encode(actionType.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="ACTIONTYPE" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="ACTIONTYPE" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="ActionType.name"  headerStyle="listHeader" width="50%" orderable="true" maxLength="30" />
                <fanta:dataColumn name="probability" styleClass="listItemRight" title="ActionType.probability"  headerStyle="listHeader" width="30%" orderable="true" maxLength="30" renderData="false" >
                     ${actionType.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
                </fanta:dataColumn>
                <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="ActionType.sequence"  headerStyle="listHeader" width="20%" orderable="true"/>
            </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>
