<%@ include file="/Includes.jsp" %>
<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
    <app2:checkAccessRight functionality="COSTCENTERS" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form styleId="CREATE_NEW_COSTCENTER" action="/CostCenter/Forward/Create.do">
            <TD colspan="6" class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>

    <TABLE id="CostCenterList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center" height="20">
        <TD>
        <fanta:table list="costCenterList" width="100%" id="costCenter" action="CostCenter/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="CostCenter/Forward/Update.do?dto(costCenterId)=${costCenter.id}&dto(costCenterName)=${app2:encode(costCenter.name)}&dto(parentCostCenterId)=${costCenter.itDependantId}"/>
                <c:set var="deleteAction" value="CostCenter/Forward/Delete.do?dto(withReferences)=true&dto(costCenterId)=${costCenter.id}&dto(costCenterName)=${app2:encode(costCenter.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="COSTCENTERS" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="COSTCENTERS" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="CostCenter.name"  headerStyle="listHeader" width="50%" orderable="true" maxLength="25" >
                </fanta:dataColumn>
                <fanta:dataColumn name="itDependant" styleClass="listItem2" title="CostCenter.group"  headerStyle="listHeader" width="50%" orderable="true" >
                </fanta:dataColumn>
            </fanta:table>
        </TD>
    </tr>
    <tr>
        <td <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
    </td>
</tr>
</table>