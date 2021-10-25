<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
    <app2:checkAccessRight functionality="${function}" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>

    <html:form styleId="CREATE_NEW_PRIORITY" action="${create}?op=create">
        <TD class="button">
        <app2:checkAccessRight functionality="${function}" permission="CREATE">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </app2:checkAccessRight>
        </TD>
    </html:form>
    </TR>
    </table>
    </app2:checkAccessRight>
    <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="priorityList" width="100%" id="priority" action="${action}" imgPath="${baselayout}"  >
                <c:set var="editAction" value="${edit}?dto(priorityName)=${app2:encode(priority.name)}&dto(priorityId)=${priority.id}&dto(langTextId)=${priority.langTextId}"/>
                <c:set var="deleteAction" value="${delete}?dto(withReferences)=true&dto(priorityId)=${priority.id}&dto(priorityName)=${app2:encode(priority.name)}"/>

                <c:set var="translateAction" value="Support/Priority/Translate.do?dto(priorityId)=${priority.id}&dto(op)=read&dto(priorityName)=${app2:encode(priority.name)}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                    <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>

                    <app2:checkAccessRight functionality="${function}" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="${function}" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Priority.name"  headerStyle="listHeader" width="65%" orderable="true" maxLength="30" />
                <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="Priority.sequence"  headerStyle="listHeader" width="30%" orderable="true" />
            </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>