<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="WEBDOCUMENT" permission="CREATE">
        <html:form action="/WebDocument/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="webDocumentList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="webDocument"
                     action="WebDocument/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="WebDocument/Forward/Update.do?dto(webDocumentId)=${webDocument.webDocumentId}&dto(name)=${app2:encode(webDocument.name)}"/>
            <c:set var="deleteLink"
                   value="WebDocument/Forward/Delete.do?dto(webDocumentId)=${webDocument.webDocumentId}&dto(name)=${app2:encode(webDocument.name)}&dto(withReferences)=true"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="WEBDOCUMENT" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="WEBDOCUMENT" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editLink}"
                              styleClass="listItem" title="WebDocument.name"
                              headerStyle="listHeader" orderable="true"
                              maxLength="60"
                              width="50%"/>
            <fanta:dataColumn name="url"
                              styleClass="listItem2" title="WebDocument.url"
                              headerStyle="listHeader" orderable="true"
                              maxLength="100"
                              width="45%"/>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="WEBDOCUMENT" permission="CREATE">
        <html:form action="/WebDocument/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
