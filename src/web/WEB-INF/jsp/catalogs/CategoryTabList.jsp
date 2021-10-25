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
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/CategoryTab/List.do" styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="selectId">
                <fmt:message key="CategoryTab.table"/>
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
            <fanta:alphabet action="CategoryTab/List.do" parameterName="label"
                            onClick="alphabetJump(this);return false;" mode="bootstrap"/>
        </div>
    </html:form>

    <html:form action="/CategoryTab/Forward/Create">
        <app2:checkAccessRight functionality="CATEGORYTAB" permission="CREATE">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </app2:checkAccessRight>
    </html:form>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="categoryTabList" width="100%" styleClass="${app2:getFantabulousTableClases()}"
                     id="categoryTab"
                     action="CategoryTab/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="CategoryTab/Forward/Update.do?dto(categoryTabId)=${categoryTab.categoryTabId}&dto(label)=${app2:encode(categoryTab.label)}"/>
            <c:set var="deleteAction"
                   value="CategoryTab/Forward/Delete.do?dto(withReferences)=true&dto(categoryTabId)=${categoryTab.categoryTabId}&dto(label)=${app2:encode(categoryTab.label)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CATEGORYTAB" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
    </div>
</div>

