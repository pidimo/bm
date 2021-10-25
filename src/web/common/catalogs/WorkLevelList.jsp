<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>

    <html:form  action="${create}">
        <TD class="button">
        <app2:securitySubmit operation="create" functionality="WORKLEVEL" styleClass="button" tabindex="10" >
            <fmt:message    key="Common.new"/>
        </app2:securitySubmit>


        </TD>
    </html:form>
    </TR>
    </table>

    <TABLE  border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="workLevelList" width="100%" id="workLevel" action="${action}" imgPath="${baselayout}"  >
                <c:set var="editAction" value="${edit}?dto(workLevelName)=${app2:encode(workLevel.name)}&dto(workLevelId)=${workLevel.id}&dto(langTextId)=${workLevel.langTextId}"/>
                <c:set var="deleteAction" value="${delete}?dto(withReferences)=true&dto(workLevelId)=${workLevel.id}&dto(workLevelName)=${app2:encode(workLevel.name)}"/>
                <c:set var="translateAction" value="Support/WorkLevel/Translate.do?dto(op)=read&dto(workLevelName)=${app2:encode(workLevel.name)}&dto(workLevelId)=${workLevel.id}&dto(langTextId)=${workLevel.langTextId}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                    <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>

                    <app2:checkAccessRight functionality="WORKLEVEL" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="WORKLEVEL" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="WorkLevel.name"  headerStyle="listHeader" width="65%" orderable="true" maxLength="30" />
                <fanta:dataColumn name="sequence" styleClass="listItem2Right" title="Priority.sequence"  headerStyle="listHeader" width="30%" orderable="true" />

        </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>
