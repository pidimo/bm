<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
    <tr>
        <td>
            <app2:checkAccessRight functionality="JRXMLREPORT" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/Report/Artifact/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="reportArtifactList" width="100%" id="artifact" action="Report/Artifact/List.do" imgPath="${baselayout}"
                         align="center">

                <app:url var="urlEdit"
                         value="/Report/Artifact/Forward/Update.do?dto(artifactId)=${artifact.artifactId}&dto(fileName)=${app2:encode(artifact.fileName)}&dto(op)=read" enableEncodeURL="false"/>
                <app:url var="urlDelete"
                         value="/Report/Artifact/Forward/Delete.do?dto(artifactId)=${artifact.artifactId}&dto(fileName)=${app2:encode(artifact.fileName)}&dto(op)=read&dto(withReferences)=true" enableEncodeURL="false"/>


                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
                        <fanta:actionColumn name="edit" label="Common.update" title="Common.update" action="${urlEdit}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="JRXMLREPORT" permission="DELETE">
                        <fanta:actionColumn name="delete" label="Common.delete" title="Common.delete" action="${urlDelete}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="fileName" title="Report.artifact.fileName" action="${urlEdit}" styleClass="listItem2"
                                  headerStyle="listHeader" width="95%" orderable="true">
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>
