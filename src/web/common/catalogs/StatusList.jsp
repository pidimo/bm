<%@ include file="/Includes.jsp" %>


<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

    <app2:checkAccessRight functionality="STATUS" permission="CREATE">
        <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <TR>
            <html:form action="/Status/Forward/Create.do">
            <TD class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
            </html:form>
        </TR>
        </table>
    </app2:checkAccessRight>

    <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td>
            <fanta:table list="statusList" width="100%" id="status" action="Status/List.do" imgPath="${baselayout}">
                <c:set var="editAction" value="Status/Forward/Update.do?dto(statusId)=${status.statusId}&dto(statusName)=${app2:encode(status.statusName)}"/>
                <c:set var="deleteAction" value="Status/Forward/Delete.do?dto(withReferences)=true&dto(statusId)=${status.statusId}&dto(statusName)=${app2:encode(status.statusName)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="STATUS" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="STATUS" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="statusName" action="${editAction}" styleClass="listItem" title="Status.name"  headerStyle="listHeader" width="50%" orderable="true" maxLength="80"/>
                <fanta:dataColumn name="" styleClass="listItem2" title="Status.isFinal"  headerStyle="listHeader" width="50%" orderable="false" renderData="false">
                    <c:if test="${status.isFinal == '1'}">
                        <img align="middle" alt="" src="<c:out value="${baselayout}"/>/img/check.gif"/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>

