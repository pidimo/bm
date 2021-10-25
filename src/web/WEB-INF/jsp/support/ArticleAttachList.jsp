<%@ include file="/Includes.jsp" %>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>

<div class="${app2:getListWrapperClasses()}">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:form action="${action}" styleClass="form-horizontal">

            <app2:securitySubmit operation="create" functionality="SUPPORTATTACH"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>


        </html:form>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="attach"
                     action="ArticleAttach/List.do" imgPath="${baselayout}">

            <c:set var="editAction"
                   value="ArticleAttach/Forward/Update.do?dto(attachId)=${attach.attachId}&dto(supportAttachName)=${app2:encode(attach.fileName)}&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}"/>

            <c:set var="deleteAction"
                   value="ArticleAttach/Forward/Delete.do?index=3&dto(attachId)=${attach.attachId}&dto(supportAttachName)=${app2:encode(attach.fileName)}
&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}&articleId=${param.articleId}"/>

            <c:set var="download"
                   value="ArticleAttach/Download.do?index=3&dto(attachId)=${attach.attachId}&dto(supportAttachName)=${app2:encode(attach.fileName)}
&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}&articleId=${param.articleId}"/>

            <fanta:columnGroup title="Common.action" width="10%" headerStyle="listHeader">
                <c:set var="render"
                       value="${!(isSupportCase == true) || (isSupportCase == true && attach.uploadUserId == sessionScope.user.valueMap.userId)}"/>
                <app2:checkAccessRight functionality="SUPPORTATTACH" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        render="${render}"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SUPPORTATTACH" permission="DELETE">

                    <fanta:actionColumn name="" title="Common.delete" styleClass="listItem"
                                        headerStyle="listHeader">
                        <c:choose>
                            <c:when test="${attach.createUserId == sessionScope.user.valueMap['userId'] || attach.uploadUserId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteAction}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
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
                            <span class="${app2:getClassGlyphDownload()}"></span>
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
    </div>

</div>