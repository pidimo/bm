<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="categoryId" value="${param['dto(categoryId)']}"/>
<c:set var="categoryName" value="${param['dto(categoryName)']}"/>
<c:set var="categoryType" value="${param['dto(categoryType)']}"/>

<table cellpadding="0" cellspacing="0" border="0" width="97%" align="center">
    <tr>
        <td>
            <table cellpadding="0" cellspacing="0" border="0" width="60%" align="center">

                <app2:checkAccessRight functionality="CATEGORYVALUE" permission="CREATE">
                    <app:url
                            value="/CategoryValue/Forward/Create.do?dto(categoryId)=${categoryId}&dto(categoryName)=${app2:encode(categoryName)}&dto(categoryType)=${categoryType}"
                            var="url"/>
                    <TR>
                        <TD colspan="6" align="left">
                            <html:button styleClass="button"
                                         property="dto(new)"
                                         onclick="window.parent.location='${url}'">
                                <fmt:message key="Common.new"/>
                            </html:button>
                        </TD>
                    </TR>
                </app2:checkAccessRight>

            </table>
        </td>
    </tr>

    <tr>
        <td>

            <TABLE id="tableId" border="0" cellpadding="0" cellspacing="0" width="60%"
                   class="container" align="center">
                <tr>
                    <td class="title">
                        <fmt:message key="${windowTitle}"/>
                    </td>
                </tr>
                <TR align="center">
                    <td>

                        <fanta:table list="categoryValuesList" width="100%" id="categoryValue"
                                     action="CategoryValue/List.do?dto(categoryId)=${categoryId}&dto(categoryName)=${app2:encode(categoryName)}&dto(categoryType)=${categoryType}"
                                     imgPath="${baselayout}">

                            <app:url var="editAction"
                                     value="CategoryValue/Forward/Update.do?dto(categoryValueId)=${categoryValue.id}&dto(categoryValueName)=${app2:encode(categoryValue.name)}&dto(categoryId)=${categoryValue.categoryId}&dto(categoryType)=${categoryType}&dto(categoryName)=${app2:encode(categoryName)}"/>
                            <app:url var="deleteAction"
                                     value="CategoryValue/Forward/Delete.do?dto(withReferences)=true&dto(categoryValueId)=${categoryValue.id}&dto(categoryValueName)=${app2:encode(categoryValue.name)}&dto(categoryId)=${categoryValue.categoryId}&dto(categoryType)=${categoryType}&dto(categoryName)=${app2:encode(categoryName)}"/>

                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                                <app2:checkAccessRight functionality="CATEGORYVALUE" permission="VIEW">
                                    <fanta:actionColumn name="up" title="Common.update"
                                                        action="javascript:goParentURL('${editAction}')"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"
                                                        useJScript="true" width="50%"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CATEGORYVALUE" permission="DELETE">
                                    <fanta:actionColumn name="del" title="Common.delete"
                                                        action="javascript:goParentURL('${deleteAction}')"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"
                                                        useJScript="true" width="50%"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="name" styleClass="listItem2" title="CategoryValue.name"
                                              action="javascript:goParentURL('${editAction}')"
                                              headerStyle="listHeader" width="95%"
                                              orderable="true" useJScript="true"/>
                        </fanta:table>
                    </td>
                </tr>
            </table>


        </td>
    </tr>
</table>
<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>
