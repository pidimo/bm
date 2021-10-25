<%@ include file="/Includes.jsp" %>

<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="CREATE">
            <html:form action="/ProjectActivity/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
    <tr>
        <td>
            <fanta:table list="projectActivityList"
                         width="100%"
                         id="projectActivity"
                         action="ProjectActivity/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="ProjectActivity/Forward/Update.do?dto(activityId)=${projectActivity.projectActivityId}&dto(name)=${app2:encode(projectActivity.activityName)}"/>
                <c:set var="deleteLink"
                       value="ProjectActivity/Forward/Delete.do?dto(activityId)=${projectActivity.projectActivityId}&dto(name)=${app2:encode(projectActivity.activityName)}&dto(withReferences)=true"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="activityName" action="${editLink}"
                                  styleClass="listItem" title="ProjectActivity.name"
                                  headerStyle="listHeader" orderable="true"
                                  maxLength="40"
                                  width="95%"/>
            </fanta:table>

        </td>
    </tr>
    <tr>
        <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="CREATE">
            <html:form action="/ProjectActivity/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>
</table>