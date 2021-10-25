<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/catalogs/CategoryGroup/List.do?index=${param.index}&module=catalogs&category=8"
                 var="jsCategoryGroupUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="parameter(tableId)" value="obj.options[obj.selectedIndex].value"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(tableId)" value="tableid"/>
</app2:jScriptUrl>
<script language="JavaScript">
    function jump(obj) {
        window.location =${jsCategoryGroupUrl};
    }
    function alphabetJump(obj) {
        tableid = document.getElementById('selectId').value;
        window.location = ${jsJumpUrl};
    }
</script>

<c:set var="categoryAllTypeList" value="${app2:getCategoryTableTypes(pageContext.request)}"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/CategoryGroup/List.do" styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="selectId">
                <fmt:message key="CategoryGroup.table"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <html:select property="parameter(tableId)" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                             onchange="jump(this)" styleId="selectId">
                    <html:option value=""/>
                    <html:options collection="categoryAllTypeList"
                                  property="value" labelProperty="label"/>
                </html:select>
            </div>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="CategoryGroup/List.do" parameterName="label"
                            onClick="alphabetJump(this);return false;" mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="CATEGORYGROUP" permission="CREATE">
        <html:form action="/CategoryGroup/Forward/Create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="categoryGroupList" width="100%" styleClass="${app2:getFantabulousTableClases()}"
                     id="categoryGroup" action="CategoryGroup/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="CategoryGroup/Forward/Update.do?dto(categoryGroupId)=${categoryGroup.categoryGroupId}&dto(label)=${app2:encode(categoryGroup.label)}"/>
            <c:set var="deleteAction"
                   value="CategoryGroup/Forward/Delete.do?dto(withReferences)=true&dto(categoryGroupId)=${categoryGroup.categoryGroupId}&dto(label)=${app2:encode(categoryGroup.label)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="CATEGORYGROUP" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CATEGORYGROUP" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="label" action="${editAction}" styleClass="listItem"
                              title="CategoryGroup.label" headerStyle="listHeader" width="30%"
                              orderable="true" maxLength="25"/>
            <fanta:dataColumn name="sequence" styleClass="listItem" title="CategoryGroup.sequence"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="tabLabel" styleClass="listItem" title="CategoryGroup.categoryTabId"
                              headerStyle="listHeader" width="20%" orderable="false"/>
            <fanta:dataColumn name="tableId" styleClass="listItem2"
                              title="CategoryGroup.table" headerStyle="listHeader" width="30%"
                              orderable="false" maxLength="20" renderData="false">
                ${app2:getCategoryTableType(categoryGroup.tableId, pageContext.request)}
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>