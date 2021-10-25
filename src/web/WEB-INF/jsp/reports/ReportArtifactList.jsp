<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="JRXMLREPORT" permission="CREATE">
        <html:form action="/Report/Artifact/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>


    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="reportArtifactList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="artifact"
                     action="Report/Artifact/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <app:url var="urlEdit"
                     value="/Report/Artifact/Forward/Update.do?dto(artifactId)=${artifact.artifactId}&dto(fileName)=${app2:encode(artifact.fileName)}&dto(op)=read"
                     enableEncodeURL="false"/>
            <app:url var="urlDelete"
                     value="/Report/Artifact/Forward/Delete.do?dto(artifactId)=${artifact.artifactId}&dto(fileName)=${app2:encode(artifact.fileName)}&dto(op)=read&dto(withReferences)=true"
                     enableEncodeURL="false"/>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
                    <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="JRXMLREPORT" permission="DELETE">
                    <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete" action="${urlDelete}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="fileName" title="Report.artifact.fileName" action="${urlEdit}" styleClass="listItem2"
                              headerStyle="listHeader" width="95%" orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>


