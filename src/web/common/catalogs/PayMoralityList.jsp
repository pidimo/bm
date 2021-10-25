<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>
        <app2:checkAccessRight functionality="PAYMORALITY" permission="CREATE">
            <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
                <TR>
                <html:form styleId="CREATE_NEW_PAYMORALITY" action="/PayMorality/Forward/Create.do?op=create">
                    <TD colspan="6" class="button">
                    <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
                    </TD>
                </html:form>
                </TR>
            </table>
        </app2:checkAccessRight>
        <TABLE id="PayMoralityList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
        <tr>
            <td>
            <fanta:table list="payMoralityList" width="100%" id="payMorality" action="PayMorality/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="PayMorality/Forward/Update.do?dto(payMoralityId)=${payMorality.id}&dto(payMoralityName)=${app2:encode(payMorality.name)}"/>
                <c:set var="deleteAction" value="PayMorality/Forward/Delete.do?dto(withReferences)=true&dto(payMoralityId)=${payMorality.id}&dto(payMoralityName)=${app2:encode(payMorality.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PAYMORALITY" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PAYMORALITY" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="PayMorality.name"  headerStyle="listHeader" width="95%" orderable="true" maxLength="50" />
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