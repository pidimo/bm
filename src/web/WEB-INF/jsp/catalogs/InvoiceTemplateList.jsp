<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="CREATE">
        <html:form action="/InvoiceTemplate/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoiceTemplateList" width="100%" styleClass="${app2:getFantabulousTableClases()}"
                     id="invoiceTemplate"
                     action="InvoiceTemplate/List.do"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="InvoiceTemplate/Forward/Update.do?templateId=${invoiceTemplate.templateId}&dto(templateId)=${invoiceTemplate.templateId}&dto(title)=${app2:encode(invoiceTemplate.title)}"/>
            <c:set var="deleteAction"
                   value="InvoiceTemplate/Forward/Delete.do?templateId=${invoiceTemplate.templateId}&dto(withReferences)=true&dto(templateId)=${invoiceTemplate.templateId}&dto(title)=${app2:encode(invoiceTemplate.title)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="INVOICETEMPLATE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="title"
                              action="${editAction}" styleClass="listItem2"
                              title="InvoiceTemplate.title" headerStyle="listHeader" width="95%"
                              orderable="true" maxLength="25">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>
