<%@ include file="/Includes.jsp" %>

<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
<td>
    <app2:checkAccessRight functionality="TELECOMTYPE" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
        <html:form styleId="CREATE_NEW_TELECOMTYPE" action="/TelecomType/Forward/Create.do?op=create">
            <TD class="button">
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </TD>
        </html:form>
    </TR>
    </table>
    </app2:checkAccessRight>
    <TABLE id="TelecomTypeList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <TR align="center">
        <td>
        <fanta:table list="telecomTypeList" width="100%" id="telecomType" action="TelecomType/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="TelecomType/Forward/Update.do?dto(telecomTypeId)=${telecomType.id}&dto(telecomTypeName)=${app2:encode(telecomType.name)}&dto(langTextId)=${telecomType.translationId}"/>
                <c:set var="deleteAction" value="TelecomType/Forward/Delete.do?dto(withReferences)=true&dto(telecomTypeId)=${telecomType.id}&dto(telecomTypeName)=${app2:encode(telecomType.name)}&dto(langTextId)=${telecomType.translationId}"/>

                <c:set var="translateAction" value="TelecomType/Translate.do?dto(telecomTypeId)=${telecomType.id}&dto(op)=read&dto(telecomTypeName)=${app2:encode(telecomType.name)}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader">

                        <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/tournicoti.gif"/>

                    <app2:checkAccessRight functionality="TELECOMTYPE" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="TELECOMTYPE" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="TelecomType.name"  headerStyle="listHeader" width="35%" orderable="true" maxLength="25" />
                <fanta:dataColumn name="position" styleClass="listItemRight" title="TelecomType.position"  headerStyle="listHeader" width="35%" orderable="true" />
                <fanta:dataColumn name="type" styleClass="listItem2" title="TelecomType.type"  headerStyle="listHeader" width="30%" orderable="true"  renderData="false" >
                <c:set var="telecomTypeConstant" value="${telecomType.type}" scope="request"/>
<%
    String constant = (String) request.getAttribute("telecomTypeConstant");
    String key = JSPHelper.getTelecomType(constant,request);
    request.setAttribute("constantKey",key);
%>
                ${constantKey}&nbsp;
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
