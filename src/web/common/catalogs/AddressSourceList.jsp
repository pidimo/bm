<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

    <app2:checkAccessRight functionality="ADDRESSSOURCE" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form styleId="CREATE_NEW_ADDRESSSOURCE" action="/AddressSource/Forward/Create.do?op=create">
            <TD colspan="6" class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>

    <TABLE id="AddressSourceList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="addressSourceList" width="100%" id="addressSource" action="AddressSource/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="AddressSource/Forward/Update.do?dto(addressSourceId)=${addressSource.id}&dto(addressSourceName)=${app2:encode(addressSource.name)}"/>
                <c:set var="deleteAction" value="AddressSource/Forward/Delete.do?dto(withReferences)=true&dto(addressSourceId)=${addressSource.id}&dto(addressSourceName)=${app2:encode(addressSource.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="ADDRESSSOURCE" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="ADDRESSSOURCE" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="AddressSource.name"  headerStyle="listHeader" width="95%" orderable="true" maxLength="25" />
        </fanta:table>
        </td>
    </tr>
    </table>
 </td>
</tr>
</table>

