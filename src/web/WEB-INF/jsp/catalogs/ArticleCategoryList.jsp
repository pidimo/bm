<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="CREATE">
        <html:form action="Support/ArticleCategory/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit operation="create" functionality="SUPPORTCATEGORY"
                                     styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="articleCategoryList" id="articleCategory"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="Support/ArticleCategoryList.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Support/ArticleCategory/Forward/Update.do?dto(categoryId)=${articleCategory.id}&dto(categoryName)=${app2:encode(articleCategory.name)}"/>
            <c:set var="deleteAction"
                   value="Support/ArticleCategory/Forward/Delete.do?dto(categoryId)=${articleCategory.id}&dto(categoryName)=${app2:encode(articleCategory.name)}&dto(withReferences)=true"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem"
                              title="ArticleCategory.categoryName" headerStyle="listHeader" width="50%"
                              orderable="true" maxLength="25"/>
            <fanta:dataColumn name="parentName" styleClass="listItem2"
                              title="ArticleCategory.parentCategory" headerStyle="listHeader"
                              width="45%" orderable="true"/>
        </fanta:table>
    </div>
</div>