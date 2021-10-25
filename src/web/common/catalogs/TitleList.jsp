<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >

<tr>
    <td>

<app2:checkAccessRight functionality="TITLE" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
    <html:form styleId="CREATE_NEW_TITLE" action="/Title/Forward/Create.do?op=create">
        <TD class="button">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </TD>
    </html:form>
    </TR>
    </table>
</app2:checkAccessRight>

    <TABLE id="TitleList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR>
        <td>
        <fanta:table list="titleList" width="100%" id="title" action="Title/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="Title/Forward/Update.do?dto(titleId)=${title.id}&dto(titleName)=${app2:encode(title.name)}"/>
                <c:set var="deleteAction" value="Title/Forward/Delete.do?dto(withReferences)=true&dto(titleId)=${title.id}&dto(titleName)=${app2:encode(title.name)}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="TITLE" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="TITLE" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem2" title="Title.name"  headerStyle="listHeader" width="95%" orderable="true" maxLength="25" >
                </fanta:dataColumn>
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
