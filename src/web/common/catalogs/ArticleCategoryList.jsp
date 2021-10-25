<%@ include file="/Includes.jsp" %>

<table width="80%" border="0" align="center" cellpadding="10" cellspacing="0">
    <tr>
        <td align="center"><br>
            <app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="CREATE">
                <table width="97%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="Support/ArticleCategory/Forward/Create">
                            <td class="button"><!--Button create up -->

                                <app2:securitySubmit operation="create" functionality="SUPPORTCATEGORY"
                                                     styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </app2:securitySubmit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
            <TABLE border="0" cellpadding="0" cellspacing="0" width="97%" class="container" align="center">
                <TR align="center" height="20">
                    <td>
                        <fanta:table list="articleCategoryList" width="100%" id="articleCategory"
                                     action="Support/ArticleCategoryList.do" imgPath="${baselayout}">
                            <c:set var="editAction"
                                   value="Support/ArticleCategory/Forward/Update.do?dto(categoryId)=${articleCategory.id}&dto(categoryName)=${app2:encode(articleCategory.name)}"/>
                            <c:set var="deleteAction"
                                   value="Support/ArticleCategory/Forward/Delete.do?dto(categoryId)=${articleCategory.id}&dto(categoryName)=${app2:encode(articleCategory.name)}&dto(withReferences)=true"/>
                            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                                <app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="VIEW">
                                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="DELETE">
                                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem"
                                              title="ArticleCategory.categoryName" headerStyle="listHeader" width="50%"
                                              orderable="true" maxLength="25"/>
                            <fanta:dataColumn name="parentName" styleClass="listItem2"
                                              title="ArticleCategory.parentCategory" headerStyle="listHeader"
                                              width="45%" orderable="true"/>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>