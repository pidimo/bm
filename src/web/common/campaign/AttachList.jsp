<%@ include file="/Includes.jsp" %>


<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="80%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr>
                    <td class="label">
                        <fmt:message key="Common.search"/>
                    </td>
                    <html:form action="/Attach/List.do" focus="parameter(fileName)">
                        <td class="contain">
                            <html:text property="parameter(fileName)" styleClass="largeText"/>
                            &nbsp;
                            <html:submit styleClass="button">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </td>
                    </html:form>
                </tr>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="Attach/List.do" parameterName="fileName"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="80%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr>
                    <td class="button">
                        <html:form action="/Attach/Forward/Create.do">
                            <app2:securitySubmit operation="create" functionality="CAMPAIGNATTACH" styleClass="button">
                                <fmt:message key="Common.new"/>
                            </app2:securitySubmit>
                        </html:form>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fanta:table list="attachList" width="100%" id="attach"
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
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CAMPAIGNATTACH" permission="DELETE">
                                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CAMPAIGNATTACH" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.download" action="${downloadAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/openfile.png"/>
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

                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
