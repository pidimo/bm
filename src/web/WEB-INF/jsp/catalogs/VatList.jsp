<%@ include file="/Includes.jsp" %>
<!-- Latest compiled and minified CSS -->

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="VAT" permission="CREATE">
        <html:form action="/Vat/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div id="VatList.jsp" class="table-responsive">
        <fanta:table mode="bootstrap" list="vatList" styleClass="${app2:getFantabulousTableClases()}" id="vat" action="Vat/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Vat/Forward/Update.do?dto(vatId)=${vat.id}&dto(vatLabel)=${app2:encode(vat.name)}"/>
            <c:set var="deleteAction"
                   value="Vat/Forward/Delete.do?dto(withReferences)=true&dto(vatId)=${vat.id}&dto(vatLabel)=${app2:encode(vat.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="VAT" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="VAT" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Vat.name"
                              headerStyle="listHeader" width="30%" orderable="true" maxLength="25"/>
            <fanta:dataColumn name="description" styleClass="listItem2" title="Vat.description"
                              headerStyle="listHeader" width="65%" orderable="true" maxLength="40"/>
        </fanta:table>
    </div>
</div>