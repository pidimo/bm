<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/catalogs/CategoryTab/List.do?index=${param.index}&module=catalogs&category=8"
                 var="jsCategoryTabUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="parameter(tableId)" value="obj.options[obj.selectedIndex].value"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(tableId)" value="tableid"/>
</app2:jScriptUrl>
<script language="JavaScript">
    function jump(obj) {
        window.location =${jsCategoryTabUrl};
    }
    function alphabetJump(obj) {
        tableid = document.getElementById('selectId').value;
        window.location = ${jsJumpUrl};
    }
</script>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTableTypes(pageContext.request)}"/>
<html:form action="/CategoryTab/List.do">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0" class="container">
        <tr>
            <td class="label" width="30%"><fmt:message key="CategoryTab.table"/></td>
            <td align="left" class="contain" width="70%">
                <html:select property="parameter(tableId)" styleClass="mediumSelect"
                             onchange="jump(this)" styleId="selectId">
                    <html:option value=""/>
                    <html:options collection="categoryAllTypeList"
                                  property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center" class="alpha">
                <fanta:alphabet action="CategoryTab/List.do" parameterName="label"
                                onClick="alphabetJump(this);return false;"/>
            </td>
        </tr>
    </table>
</html:form>
<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <html:form action="/CategoryTab/Forward/Create">
            <td>
                <tags:buttonsTable>
                    <app2:checkAccessRight functionality="CATEGORYTAB" permission="CREATE">

                        <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>

                    </app2:checkAccessRight>
                </tags:buttonsTable>
            </td>
        </html:form>
    </tr>
    <tr>
        <td>

            <fanta:table list="categoryTabList" width="100%" id="categoryTab" action="CategoryTab/List.do"
                         imgPath="${baselayout}">
                <c:set var="editAction"
                       value="CategoryTab/Forward/Update.do?dto(categoryTabId)=${categoryTab.categoryTabId}&dto(label)=${app2:encode(categoryTab.label)}"/>
                <c:set var="deleteAction"
                       value="CategoryTab/Forward/Delete.do?dto(withReferences)=true&dto(categoryTabId)=${categoryTab.categoryTabId}&dto(label)=${app2:encode(categoryTab.label)}"/>
                <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CATEGORYTAB" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="label" action="${editAction}" styleClass="listItem"
                                  title="CategoryTab.label" headerStyle="listHeader" width="40%"
                                  orderable="true" maxLength="25"/>
                <fanta:dataColumn name="sequence" styleClass="listItem" title="CategoryTab.sequence"
                                  headerStyle="listHeader" width="25%" orderable="true"/>
                <fanta:dataColumn name="tableId" styleClass="listItem2"
                                  title="CategoryTab.table" headerStyle="listHeader" width="30%"
                                  orderable="false" maxLength="25" renderData="false">
                    ${app2:getCategoryTableType(categoryTab.tableId, pageContext.request)}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>
