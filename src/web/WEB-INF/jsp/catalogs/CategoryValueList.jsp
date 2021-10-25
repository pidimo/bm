<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="categoryId" value="${param['dto(categoryId)']}"/>
<c:set var="categoryName" value="${param['dto(categoryName)']}"/>
<c:set var="categoryType" value="${param['dto(categoryType)']}"/>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="CATEGORYVALUE" permission="CREATE">
        <app:url
                value="/CategoryValue/Forward/Create.do?dto(categoryId)=${categoryId}&dto(categoryName)=${app2:encode(categoryName)}&dto(categoryType)=${categoryType}"
                var="url"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:button styleClass="${app2:getFormButtonClasses()}"
                         property="dto(new)"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:button>
        </div>
    </app2:checkAccessRight>
    <form>
        <fieldset>
            <legend class="title">
                <fmt:message key="${windowTitle}"/>
            </legend>
        </fieldset>
    </form>
    <div id="tableId" class="table-responsive">
        <fanta:table mode="bootstrap" list="categoryValuesList" width="100%" styleClass="${app2:getFantabulousTableClases()}"
                     id="categoryValue"
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
                                        glyphiconClass="${app2:getClassGlyphEdit()}"
                                        useJScript="true" width="50%"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CATEGORYVALUE" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete"
                                        action="javascript:goParentURL('${deleteAction}')"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"
                                        useJScript="true" width="50%"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" styleClass="listItem2" title="CategoryValue.name"
                              action="javascript:goParentURL('${editAction}')"
                              headerStyle="listHeader" width="95%"
                              orderable="true" useJScript="true"/>
        </fanta:table>
    </div>
</div>