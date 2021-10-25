<%@ include file="/Includes.jsp" %>

<div class="${app2:getFormGroupClasses()}">
    <html:form action="/Attach/List.do" focus="parameter(fileName)" styleClass="form-horizontal">
        <fieldset>

            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="fileName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(fileName)"
                               styleId="fileName_id"
                               styleClass="largeText ${app2:getFormInputClasses()}"/>
                                <span class="input-group-btn">
                                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                                        <fmt:message key="Common.go"/>
                                    </html:submit>
                                </span>
                </div>
            </div>
        </fieldset>
    </html:form>
</div>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Attach/List.do"
                    mode="bootstrap"
                    parameterName="fileName"/>
</div>


<div class="${app2:getFormButtonWrapperClasses()}">
    <html:form action="/Attach/Forward/Create.do">
        <app2:securitySubmit operation="create"
                             functionality="CAMPAIGNATTACH"
                             styleClass="button ${app2:getFormButtonClasses()}">
            <fmt:message key="Common.new"/>
        </app2:securitySubmit>
    </html:form>
</div>


<div class="table-responsive">
    <fanta:table mode="bootstrap" list="attachList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%"
                 id="attach"
                 action="Attach/List.do"
                 imgPath="${baselayout}">

        <c:set var="editAction"
               value="Attach/Forward/Update.do?dto(attachId)=${attach.attachId}&dto(filename)=${app2:encode(attach.fileName)}"/>
        <c:set var="deleteAction"
               value="Attach/Forward/Delete.do?dto(withReferences)=true&dto(attachId)=${attach.attachId}&dto(filename)=${app2:encode(attach.fileName)}"/>
        <c:set var="downloadAction"
               value="Attach/Download.do?dto(fileName)=${app2:encode(attach.fileName)}&dto(freeTextId)=${attach.freeTextId}"/>
        <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
            <app2:checkAccessRight functionality="CAMPAIGNATTACH" permission="VIEW">
                <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGNATTACH" permission="DELETE">
                <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGNATTACH" permission="VIEW">
                <fanta:actionColumn name="" title="Common.download" action="${downloadAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphDownload()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>

        <fanta:dataColumn name="comment" action="${editAction}" styleClass="listItem"
                          title="Attach.comment" headerStyle="listHeader" width="45%"
                          orderable="true" maxLength="25">
        </fanta:dataColumn>
        <fanta:dataColumn name="fileName" action="${editAction}" styleClass="listItem"
                          title="Attach.fileName" headerStyle="listHeader" width="30%"
                          orderable="true" maxLength="25">
        </fanta:dataColumn>

        <fanta:dataColumn name="size" styleClass="listItem"
                          title="Attach.size" headerStyle="listHeader" width="25%"
                          orderable="true" maxLength="25" style="text-align:right"
                          renderData="false">
            <c:set var="e" value="${app2:divide(attach.size,(1024))}"/>
            <c:choose>
                <c:when test="${e > 0}">
                    ${e}
                </c:when>
                <c:otherwise>
                    1
                </c:otherwise>
            </c:choose>
            <fmt:message key="Webmail.mailTray.Kb"/>
        </fanta:dataColumn>
    </fanta:table>

</div>


