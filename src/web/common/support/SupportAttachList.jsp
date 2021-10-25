<%@ include file="/Includes.jsp" %>
<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>
            <table width="90%" border="0" align="center" cellpadding="2" cellspacing="0">
                <TR>
                    <html:form action="${attachCreate}">
                        <TD class="button">
                            <app2:securitySubmit operation="create" functionality="SUPPORTATTACH" styleClass="button"
                                                 property="dto(save)">
                                <fmt:message key="Common.new"/>
                            </app2:securitySubmit>
                        </TD>
                    </html:form>
                </TR>
            </table>

            <table border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
                <tr>
                    <td>
                        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
                        <fanta:table width="100%" id="attach" action="${action}" imgPath="${baselayout}">

                            <c:set var="editAction"
                                   value="CaseAttach/Forward/Update.do?dto(attachId)=${attach.attachId}&dto(supportAttachName)=${app2:encode(attach.fileName)}&dto(caseTitle)=${app2:encode(param['dto(caseTitle)'])}&index=${param.index}"/>

                            <c:set var="deleteAction"
                                   value="CaseAttach/Forward/Delete.do?dto(attachId)=${attach.attachId}&dto(supportAttachName)=${app2:encode(attach.fileName)}&dto(caseTitle)=${app2:encode(param['dto(caseTitle)'])}&caseId=${param.caseId}&index=${param.index}"/>

                            <c:set var="download" value="CaseAttach/Download.do?dto(attachId)=${attach.attachId}&dto(supportAttachName)=${app2:encode(attach.fileName)}
&dto(caseTitle)=${app2:encode(param['dto(caseTitle)'])}&caseId=${param.caseId}"/>

                            <fanta:columnGroup title="Common.action" width="10%" headerStyle="listHeader">
                                <c:set var="render"
                                       value="${!(isSupportCase == true) || (isSupportCase == true && attach.uploadUserId == sessionScope.user.valueMap.userId)}"/>

                                <app2:checkAccessRight functionality="SUPPORTATTACH" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        render="${render}" image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="SUPPORTATTACH" permission="DELETE">

                                    <fanta:actionColumn name="" title="Common.delete" styleClass="listItem"
                                                        headerStyle="listHeader">
                                        <c:choose>
                                            <c:when test="${attach.createUserId == sessionScope.user.valueMap['userId'] || attach.uploadUserId == sessionScope.user.valueMap['userId']}">
                                                <html:link action="${deleteAction}" titleKey="Common.delete">
                                                    <html:img src="${baselayout}/img/delete.gif"
                                                              titleKey="Common.delete" border="0"/>
                                                </html:link>
                                            </c:when>
                                            <c:otherwise>
                                                &nbsp;
                                            </c:otherwise>
                                        </c:choose>
                                    </fanta:actionColumn>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="SUPPORTATTACH" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.download" styleClass="listItem"
                                                        headerStyle="listHeader">
                                        <html:link action="${download}" titleKey="Common.download">
                                            <html:img src="${baselayout}/img/openfile.png" titleKey="Common.download"
                                                      border="0"/>
                                        </html:link>
                                    </fanta:actionColumn>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>

                            <fanta:dataColumn name="comment" action="${editAction}" styleClass="listItem"
                                              title="SupportAttach.comment" headerStyle="listHeader" width="30%"
                                              orderable="true" maxLength="15" renderUrl="${render}"/>
                            <fanta:dataColumn name="fileName" styleClass="listItem" title="SupportAttach.fileName"
                                              headerStyle="listHeader" width="15%" orderable="true" maxLength="20"/>
                            <fanta:dataColumn name="size" styleClass="listItem" title="SupportAttach.size"
                                              headerStyle="listHeader" width="10%" orderable="true" maxLength="20"
                                              renderData="false">
                                <c:choose>
                                    <c:when test="${attach.size<1024}">
                                        ${1} <fmt:message key="Webmail.mailTray.Kb"/>
                                    </c:when>
                                    <c:otherwise>
                                        ${fn:substringBefore(attach.size/1024,".")}
                                        <fmt:message key="Webmail.mailTray.Kb"/>
                                    </c:otherwise>
                                </c:choose>
                            </fanta:dataColumn>
                            <fanta:dataColumn name="uploadDateTime" styleClass="listItem"
                                              title="SupportAttach.uploadDate" headerStyle="listHeader" width="15%"
                                              orderable="true" maxLength="25" renderData="false">
                                ${app2:getDateWithTimeZone(attach.uploadDateTime, timeZone, dateTimePattern)}
                            </fanta:dataColumn>
                            <fanta:dataColumn name="userName" styleClass="listItem2" title="SupportAttach.uploadUser"
                                              headerStyle="listHeader" width="20%" orderable="true" maxLength="20"/>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>