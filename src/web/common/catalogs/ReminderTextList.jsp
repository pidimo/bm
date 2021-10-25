<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<app2:checkAccessRight functionality="REMINDERLEVEL" permission="UPDATE">
    <table cellpadding="0" cellspacing="0" border="0" width="60%" align="center">
        <app:url value="/ReminderText/Forward/Create.do"
                 var="url"/>
        <TR>
            <TD colspan="6" align="left">
                <html:button styleClass="button"
                             property="dto(new)"
                             onclick="window.parent.location='${url}'">
                    <fmt:message key="Common.new"/>
                </html:button>
            </TD>
        </TR>
    </table>
</app2:checkAccessRight>

<TABLE border="0" cellpadding="0" cellspacing="0" width="60%"
       class="container" align="center" id="tableId">
    <tr>
        <td class="title">
            <fmt:message key="${windowTitle}"/>
        </td>
    </tr>
    <TR align="center">
        <td>
            <app:url var="listURL" value="/ReminderText/List.do" enableEncodeURL="false"/>
            <fanta:table list="reminderTextList"
                         width="100%"
                         id="reminderText"
                         action="${listURL}"
                         imgPath="${baselayout}" addModuleParams="true">

                <app:url var="editAction"
                         value="ReminderText/Forward/Update.do?dto(reminderLevelId)=${reminderText.reminderLevelId}&dto(languageId)=${reminderText.languageId}"/>
                <app:url var="deleteAction"
                         value="ReminderText/Forward/Delete.do?dto(reminderLevelId)=${reminderText.reminderLevelId}&dto(languageId)=${reminderText.languageId}&dto(enableLastElementValidation)=true"/>
                <app:url var="downloadAction"
                         value="ReminderText/Forward/Download.do?dto(reminderLevelId)=${reminderText.reminderLevelId}&dto(languageId)=${reminderText.languageId}&dto(freeTextId)=${reminderText.freetextId}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="REMINDERLEVEL" permission="VIEW">
                        <fanta:actionColumn name="up" title="Common.update"
                                            action="javascript:goParentURL('${editAction}')"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"
                                            useJScript="true"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="REMINDERLEVEL" permission="DELETE">
                        <c:choose>
                            <c:when test="${1 != reminderText.isDefault}">
                                <fanta:actionColumn name="" title="Common.delete"
                                                    useJScript="true"
                                                    action="javascript:goParentURL('${deleteAction}')"
                                                    styleClass="listItem" headerStyle="listHeader"
                                                    image="${baselayout}/img/delete.gif"/>
                            </c:when>
                            <c:otherwise>
                                <fanta:actionColumn name="" title="" styleClass="listItem"
                                                    headerStyle="listHeader">
                                    &nbsp;
                                </fanta:actionColumn>
                            </c:otherwise>
                        </c:choose>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="REMINDERLEVEL" permission="VIEW">
                        <fanta:actionColumn name="download" title="Common.download" useJScript="true"
                                            action="javascript:goParentURL('${downloadAction}')"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/openfile.png"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="languageName" styleClass="listItem" title="ReminderText.language"
                                  action="javascript:goParentURL('${editAction}')"
                                  headerStyle="listHeader" width="75%"
                                  orderable="true" useJScript="true"/>
                <fanta:dataColumn name="isDefault" styleClass="listItem2" title="ReminderText.isDefault"
                                  headerStyle="listHeader" width="20%" renderData="false">
                    <c:if test="${reminderText.isDefault == '1'}">
                        <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>&nbsp;
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>