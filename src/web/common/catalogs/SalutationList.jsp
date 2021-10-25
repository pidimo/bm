<%@ include file="/Includes.jsp" %>


<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

<app2:checkAccessRight functionality="SALUTATION" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
<html:form styleId="CREATE_NEW_SALUTATION" action="/Salutation/Forward/Create.do?op=create">
        <TD class="button">
        <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </TD>
</html:form>
    </TR>
    </table>
</app2:checkAccessRight>
    <TABLE id="SalutationList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td>
        <fanta:table list="salutationList" width="100%" id="salutation" action="Salutation/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="Salutation/Forward/Update.do?dto(salutationId)=${salutation.id}&dto(salutationLabel)=${app2:encode(salutation.label)}"/>
                <c:set var="deleteAction" value="Salutation/Forward/Delete.do?dto(withReferences)=true&dto(salutationId)=${salutation.id}&dto(salutationLabel)=${app2:encode(salutation.label)}"/>
                <c:set var="translationAction" value="Salutation/Forward/translation.do?op=create&dto(salutationId)=${salutation.id}&dto(salutationLabel)=${app2:encode(salutation.label)}&dto(letterTextId)=${salutation.letterPartId}&dto(addressTextId)=${salutation.addressPartId}&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALUTATION" permission="VIEW">
                        <fanta:actionColumn name="trans" title="Common.translate" action="${translationAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALUTATION" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALUTATION" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="label" action="${editAction}" styleClass="listItem2" title="Salutation.label"  headerStyle="listHeader" width="95%" orderable="true" maxLength="25" >
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    </TABLE>
    </td>
</tr>
</table>

