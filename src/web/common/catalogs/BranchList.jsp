<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

    <app2:checkAccessRight functionality="BRANCH" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
        <html:form styleId="CREATE_NEW_BRANCH" action="/Branch/Forward/Create.do?op=create">
            <TD colspan="6" class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>

    <TABLE id="BranchList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="branchList" width="100%" id="branch" action="Branch/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="Branch/Forward/Update.do?dto(branchName)=${app2:encode(branch.name)}&dto(branchId)=${branch.id}"/>
                <c:set var="deleteAction" value="Branch/Forward/Delete.do?dto(withReferences)=true&dto(branchId)=${branch.id}&dto(branchName)=${app2:encode(branch.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="BRANCH" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="BRANCH" permission="DELETE">        
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Branch.name"  headerStyle="listHeader" width="50%" orderable="true" maxLength="25" />
                <fanta:dataColumn name="group" styleClass="listItem2" title="Branch.group"  headerStyle="listHeader" width="50%" orderable="true" />
        </fanta:table>
        </td>
    </tr>
    <tr>
        <td <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
    </td>
</tr>
</table>
