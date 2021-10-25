<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

    <app2:checkAccessRight functionality="CUSTOMERTYPE" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form styleId="CREATE_NEW_CUSTOMERTYPE" action="/CustomerType/Forward/Create.do?op=create">
                <TD colspan="6" class="button">
                    <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
                </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>

    <TABLE id="CustomerTypeList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="customerTypeList" width="100%" id="customerType" action="CustomerType/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="CustomerType/Forward/Update.do?dto(customerTypeId)=${customerType.id}&dto(customerTypeName)=${app2:encode(customerType.name)}"/>
                <c:set var="deleteAction" value="CustomerType/Forward/Delete.do?dto(withReferences)=true&dto(customerTypeId)=${customerType.id}&dto(customerTypeName)=${app2:encode(customerType.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="CUSTOMERTYPE" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CUSTOMERTYPE" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="CustomerType.name"  headerStyle="listHeader" width="95%" orderable="true" maxLength="25" />
            </fanta:table>
        </td>
    </tr>    
    </table>
    </td>
</tr>
</table>
