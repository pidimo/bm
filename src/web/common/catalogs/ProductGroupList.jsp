<%@ include file="/Includes.jsp" %>

<table width="80%" border="0" align="center" cellpadding="10" cellspacing="0">
  <tr>
    <td align="center"> <br>
    <app2:checkAccessRight functionality="PRODUCTGROUP" permission="CREATE">
        <table width="97%" border="0" cellpadding="2" cellspacing="0">
        <tr>
        <html:form styleId="CREATE_NEW_PRODUCTGROUP" action="/ProductGroup/Forward/Create">
            <td class="button"><!--Button create up -->
                <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
            </td>
        </html:form>
        </tr>
        </table>
    </app2:checkAccessRight>
    <TABLE id="ProductGroupList.jsp" border="0" cellpadding="0" cellspacing="0" width="97%" class="container" align="center">
    <TR align="center" height="20">
        <td>
        <fanta:table list="productGroupList" width="100%" id="productGroup" action="ProductGroup/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="ProductGroup/Forward/Update.do?dto(groupId)=${productGroup.id}&dto(groupName)=${app2:encode(productGroup.name)}"/>
                <c:set var="deleteAction" value="ProductGroup/Forward/Delete.do?dto(groupId)=${productGroup.id}&dto(groupName)=${app2:encode(productGroup.name)}&dto(withReferences)=true"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="PRODUCTGROUP" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PRODUCTGROUP" permission="DELETE">
                        <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="ProductGroup.name"  headerStyle="listHeader" width="50%" orderable="true" maxLength="25" />
                <fanta:dataColumn name="parentProductgroupName" styleClass="listItem2" title="ProductGroup.parentName" headerStyle="listHeader" width="50%" orderable="true"/>
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
