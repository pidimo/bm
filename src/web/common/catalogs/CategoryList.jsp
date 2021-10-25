<%@ include file="/Includes.jsp" %>


<app2:jScriptUrl url="/catalogs/Category/List.do?index=${param.index}&module=catalogs&category=8" var="jsCategoryUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="parameter(tableId)" value="obj.options[obj.selectedIndex].value"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(tableId)" value="tableid"/>
</app2:jScriptUrl>
<script language="JavaScript">
    <!--
    function jump(obj) {
        window.location =${jsCategoryUrl};
    }

    function myJump(obj) {
        tableid = document.getElementById('selectId').value;
        window.location = ${jsJumpUrl};
    }
    //-->
</script>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTablesListExtended(pageContext.request)}"/>


<table width="97%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
    <html:form action="/Category/List.do">
        <table width="80%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
            <tr>
                <td>
                    <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0"
                           class="container">
                        <TR>
                            <td class="label">
                                <fmt:message key="Category.label"/>
                            </td>
                            <td align="left" class="contain">
                                <html:select property="parameter(tableId)" styleClass="mediumSelect"
                                             onchange="jump(this)" styleId="selectId">
                                    <html:option value=""/>
                                    <html:options collection="categoryAllTypeList" property="value"
                                                  labelProperty="label"/>
                                </html:select>
                            </td>
                        </TR>
                        <tr>
                            <td colspan="2" align="center" class="alpha">
                                <fanta:alphabet action="Category/List.do" parameterName="categoryName"
                                                onClick="myJump(this);return false;"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <app2:checkAccessRight functionality="CATEGORY" permission="CREATE">
                    <TD colspan="6" class="button">
                        <html:submit styleClass="button" property="parameter(new)">
                            <fmt:message key="Common.new"/>
                        </html:submit>
                    </TD>
                </app2:checkAccessRight>
            </tr>
        </table>
    </html:form>
    <TABLE id="CategoryList.jsp" border="0" cellpadding="0" cellspacing="0" width="80%" class="container"
           align="center">

        <TR align="center">
            <td>
                <fanta:table list="categoryList" width="100%" id="categorya" action="Category/List.do"
                             imgPath="${baselayout}">
                    <c:set var="editAction"
                           value="Category/Forward/Update.do?dto(categoryId)=${categorya.id}&dto(categoryName)=${app2:encode(categorya.name)}"/>
                    <c:set var="deleteAction"
                           value="Category/Forward/Delete.do?dto(withReferences)=true&dto(categoryId)=${categorya.id}&dto(categoryName)=${app2:encode(categorya.name)}&dto(checkUssages)=true"/>

                    <c:set var="translateAction"
                           value="Category/Translate.do?dto(categoryId)=${categorya.id}&dto(op)=read&dto(categoryName)=${app2:encode(categorya.name)}"/>

                    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                        <fanta:actionColumn name="trans" title="Common.translate" action="${translateAction}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/tournicoti.gif"/>

                        <app2:checkAccessRight functionality="CATEGORY" permission="VIEW">
                            <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                                styleClass="listItem" headerStyle="listHeader"
                                                image="${baselayout}/img/edit.gif"/>
                        </app2:checkAccessRight>
                        <app2:checkAccessRight functionality="CATEGORY" permission="DELETE">
                            <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                                styleClass="listItem" headerStyle="listHeader"
                                                image="${baselayout}/img/delete.gif"/>
                        </app2:checkAccessRight>
                    </fanta:columnGroup>
                    <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem"
                                      title="Category.name" headerStyle="listHeader" width="18%"
                                      orderable="true" maxLength="25"/>
                    <fanta:dataColumn name="label" styleClass="listItem" title="Category.label"
                                      headerStyle="listHeader" width="10%" renderData="false">
                        <c:set var="tableLabel" value="${app2:getCategoryTableType(categorya.label, pageContext.request)}"/>
                        <fanta:textShorter title="${tableLabel}">
                            <c:out value="${tableLabel}"/>
                        </fanta:textShorter>
                    </fanta:dataColumn>
                    <fanta:dataColumn name="categoryType" styleClass="listItem" title="Category.type"
                                      headerStyle="listHeader" renderData="false" orderable="false" width="14%">
                        ${app2:getCategoryType(categorya.categoryType,pageContext.request)}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="parentCategoryName" styleClass="listItem"
                                      title="Category.parentCategory" headerStyle="listHeader" width="18%"
                                      orderable="true" maxLength="25"/>
                    
                    <fanta:dataColumn name="sequence" styleClass="listItemRight"
                                      title="Category.sequence" headerStyle="listHeader" width="12%"
                                      orderable="true" maxLength="10"/>

                    <fanta:dataColumn name="groupName" styleClass="listItem2"
                                      title="Category.Group" headerStyle="listHeader" width="18%"
                                      orderable="true" maxLength="25"/>
                </fanta:table>
            </td>
        </tr>

    </table>
</td>
</tr>
</table>
