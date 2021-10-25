<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<%--Size of the list--%>
<%
    org.alfacentauro.fantabulous.controller.ResultList resultList = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("mailTrayList");
    pageContext.setAttribute("list_size", new Integer(resultList.getResultSize()));
%>

<c:set var="FOLDER_DRAFTITEMS" value="<%=WebMailConstants.FOLDER_DRAFTITEMS%>"/>

<%--Mail_priority's--%>
<c:set var="MAIL_PRIORITY_DEFAULT" value="<%=WebMailConstants.MAIL_PRIORITY_DEFAULT%>"/>
<c:set var="MAIL_PRIORITY_HIGHT" value="<%=WebMailConstants.MAIL_PRIORITY_HIGHT%>"/>

<c:set var="MAIL_IN" value="<%=String.valueOf(WebMailConstants.IN_VALUE)%>"/>
<c:set var="MAIL_OUT" value="<%=String.valueOf(WebMailConstants.OUT_VALUE)%>"/>


<%--folder column tio show constants--%>
<c:set var="SHOW_TO" value="<%=WebMailConstants.ColumnToShow.TO.getConstantAsString()%>"/>
<c:set var="SHOW_FROM_TO" value="<%=WebMailConstants.ColumnToShow.FROM_TO.getConstantAsString()%>"/>


<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<c:set var="mail_subject_size" value="${38}" scope="request"/>
<c:set var="mail_from_size" value="${25}" scope="request"/>

<c:set var="folderColumnToShow" value="${app2:getFolderColumnToShow(mailTrayForm.dto.folderId)}" scope="request"/>

<c:set var="isDraftFolder" value="${mailTrayForm.dto.folderType == FOLDER_DRAFTITEMS}"/>

<fmt:message key="Webmail.updateCommunications" var="communicationMessage" scope="request"/>

<tags:dragAndDropMailsToFolder/>

<div class="table-responsive">
    <fanta:table list="mailTrayList" width="100%" id="mailList"
                 mode="bootstrap"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="Mail/MailTray.do?mailFilter=${mailTrayForm.mailFilter}&folderId=${mailTrayForm.dto.folderId}"
                 imgPath="${baselayout}" align="center">
        <c:choose>
            <c:when test="${isDraftFolder}">
                <c:set var="mailAction"
                       value="Mail/Forward/EditDraftEmail.do?dto(mailId)=${mailList.MAILID}&dto(isDraft)=${true}&mailIndex=${mailList.MAILINDEX}&dto(draftId)=${mailTrayForm.dto.folderId}"/>
            </c:when>
            <c:otherwise>
                <c:set var="mailAction"
                       value="Mail/ReadEmail.do?dto(mailId)=${mailList.MAILID}&mailFilter=${mailFilter}&mailIndex=${mailList.MAILINDEX}&folderId=${mailTrayForm.dto.folderId}"/>
            </c:otherwise>
        </c:choose>

        <c:if test="${list_size >0}">
            <fanta:checkBoxColumn styleClass="listItemCenter" name="mail" id="selectedMails"
                                  onClick="javascript:check();"
                                  property="MAILID" headerStyle="listHeader" width="3%"/>
        </c:if>

        <app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="4%">
                <c:choose>
                    <c:when test="${app2:isActionMailContact(mailList.ACTIONMAILCONTACTCOUNT,mailList.INOUT)}">
                        <c:set var="iconName" value="emailaction.gif"/>
                        <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                            <c:set var="iconName" value="emailaction-half.gif"/>
                        </c:if>
                        <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                            styleClass="listItemCenter"
                                            headerStyle="listHeader"
                                            image="${sessionScope.baselayout}/img/webmail/${iconName}"/>
                    </c:when>
                    <c:when test="${app2:isSupportActivityMailContact(mailList.SUPPORTMAILCONTACTCOUNT,mailList.INOUT)}">
                        <c:set var="iconName" value="emailactivity.gif"/>
                        <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                            <c:set var="iconName" value="emailactivity-half.gif"/>
                        </c:if>
                        <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                            styleClass="listItemCenter"
                                            headerStyle="listHeader"
                                            image="${sessionScope.baselayout}/img/webmail/${iconName}"/>
                    </c:when>
                    <c:when test="${app2:hasMailContact(mailList.MAILCONTACTCOUNT,mailList.INOUT)}">
                        <c:set var="iconName" value="emailcomm.gif"/>
                        <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                            <c:set var="iconName" value="emailcomm-half.gif"/>
                        </c:if>
                        <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                            styleClass="listItemCenter"
                                            headerStyle="listHeader"
                                            render="false">
                            <c:if test="${mailList.INOUT == MAIL_IN || mailList.INOUT ==  MAIL_OUT}">
                                <html:link
                                        action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}">
                                    <html:img src="${sessionScope.baselayout}/img/webmail/${iconName}"
                                              border="0"
                                              title="${communicationMessage}"/>
                                </html:link>
                            </c:if>
                        </fanta:actionColumn>
                    </c:when>
                    <c:otherwise>
                        <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                            styleClass="listItemCenter"
                                            headerStyle="listHeader"
                                            render="false">
                            <c:if test="${(mailList.INOUT == MAIL_IN || mailList.INOUT ==  MAIL_OUT) && (1 == mailList.ISCOMPLETEEMAIL || empty mailList.ISCOMPLETEEMAIL)}">
                                <html:link
                                        action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}">
                                    <html:img src="${sessionScope.baselayout}/img/webmail/emailcommbroke.gif"
                                              border="0"
                                              title="${communicationMessage}"/>
                                </html:link>
                            </c:if>
                        </fanta:actionColumn>
                    </c:otherwise>
                </c:choose>
            </fanta:columnGroup>
        </app2:checkAccessRight>

        <c:set var="hasReadState" value="${app2:hasMailStateRead(mailList.MAILSTATE)}"/>
        <c:set var="unreadFontWeight" value="${!hasReadState ? 'bold' : ''}"/>
        <c:choose>
            <c:when test="${folderColumnToShow eq SHOW_TO || folderColumnToShow eq SHOW_FROM_TO}">
                <fanta:dataColumn name="" styleClass="listItem tdDragCls"
                                  title="${folderColumnToShow eq SHOW_TO ? 'Webmail.folder.show.to' : 'Webmail.folder.show.fromTo'}"
                                  headerStyle="listHeader"
                                  width="26%" renderData="false">

                    <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">

                        <c:if test="${folderColumnToShow eq SHOW_FROM_TO}">
                            <c:choose>
                                <c:when test="${mailList.INOUT == MAIL_OUT}">
                                    <span class="${app2:getClassGlyphOpen()}" title="Webmail.folder.show.to"></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="${app2:getClassGlyphSave()}" title="Webmail.folder.show.from"></span>
                                </c:otherwise>
                            </c:choose>
                        </c:if>

                        <app:link action="${mailAction}"
                                  title="${mailList.MAILTOFROM}">${app2:filterForHtml(mailList.MAILTOFROM)}</app:link>
                    </fanta:textShorter>

                    <input type="hidden" name="dragMail" class="dragMailCls" value="${mailList.MAILID}"/>
                </fanta:dataColumn>
            </c:when>
            <c:otherwise>
                <fanta:dataColumn name="MAILPERSONALFROM" styleClass="listItem tdDragCls" title="Webmail.tray.from"
                                  headerStyle="listHeader" width="26%" renderData="false" orderable="true">

                    <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">
                        <app:link action="${mailAction}">
                            <c:out value="${mailList.MAILTOFROM}"/>
                        </app:link>
                    </fanta:textShorter>

                    <input type="hidden" name="dragMail" class="dragMailCls" value="${mailList.MAILID}"/>
                </fanta:dataColumn>
            </c:otherwise>
        </c:choose>
        <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.answeredOrForwarded"
                          headerStyle="listHeader"
                          width="3%" renderData="false"
                          glyphiconClass="${app2:getClassGlyphReply()}">
            <c:choose>
                <c:when test="${app2:hasMailStateAnswered(mailList.MAILSTATE)}"><!--REPLY,REPLY_ALL-->
                    <span class="${app2:getClassGlyphReply()}" title="<fmt:message key="Webmail.tray.answered"/>"></span>
                </c:when>
                <c:when test="${app2:hasMailStateForward(mailList.MAILSTATE)}"><!--FORWARD-->
                    <span class="${app2:getClassGlyphForward()}" title="<fmt:message key="Webmail.tray.forwarded"/>"></span>
                </c:when>
            </c:choose>
        </fanta:dataColumn>
        <fanta:dataColumn name="" styleClass="listItemCenter" title="${titlePaper}"
                          headerStyle="listHeader"
                          width="3%" renderData="false"
                          glyphiconClass="${app2:getClassGlyphPaperClip()}">

            <c:if test="${1 == mailList.MAILHASATTACH}">
                <span class="${app2:getClassGlyphPaperClip()}" title="<fmt:message key="Webmail.tray.attach"/>"></span>
            </c:if>
        </fanta:dataColumn>
        <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.highPriority"
                          headerStyle="listHeader"
                          width="3%" renderData="false"
                          glyphiconClass="${app2:getClassGlyphPrioHigh()}">
            <c:if test="${mailList.MAILPRIORITY== MAIL_PRIORITY_HIGHT}"><!--High priority-->
                <span class="${app2:getClassGlyphPrioHigh()}" title="<fmt:message key="Webmail.tray.highPriority"/>"></span>
            </c:if>
        </fanta:dataColumn>
<%--
        <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.readOrUnRead"
                          headerStyle="listHeader"
                          width="3%" renderData="false"
                          headerImage="${sessionScope.baselayout}/img/webmail/msg_unread.gif">
            <c:if test="${!hasReadState}"><!--UNREAD-->
                <img src="${sessionScope.baselayout}/img/webmail/msg_unread.gif"
                     title="<fmt:message key="Webmail.tray.unRead"/>"
                     alt="<fmt:message key="Webmail.tray.unRead"/>"/>
            </c:if>
            <c:if test="${hasReadState}">
                <!--READ-->
                <img src="${sessionScope.baselayout}/img/webmail/msg_read.gif"
                     title="<fmt:message key="Webmail.tray.read"/>"
                     alt="<fmt:message key="Webmail.tray.read"/>"/>
            </c:if>
        </fanta:dataColumn>
--%>
        <fanta:dataColumn name="MAILSUBJECT" action="${mailAction}" styleClass="listItem"
                          title="Webmail.tray.subject"
                          headerStyle="listHeader" renderData="false" width="35%" orderable="true">

            <c:set var="subjectValueVar" value="${mailList.MAILSUBJECT}"/>

            <c:if test="${empty subjectValueVar && isDraftFolder}">
                <fmt:message var="subjectValueVar" key="Webmail.tray.draft.emptySubject"/>
            </c:if>

            <fanta:textShorter title="${subjectValueVar}" fontWeight="${unreadFontWeight}"
                               enableOutputEncode="true">
                ${subjectValueVar}
            </fanta:textShorter>
        </fanta:dataColumn>

        <fanta:dataColumn name="SENTDATETIME" styleClass="listItem" title="Webmail.tray.date"
                          headerStyle="listHeader"
                          width="14%" orderable="true" renderData="false" nowrap="nowrap">
            ${app2:getDateWithTimeZone(mailList.SENTDATETIME, timeZone, dateTimePattern)}
        </fanta:dataColumn>
        <fanta:dataColumn name="MAILSIZE" styleClass="listItem2Right" title="Webmail.tray.size"
                          headerStyle="listHeader"
                          width="9%" renderData="false" orderable="true" nowrap="true">
            <c:choose>
                <c:when test="${mailList.MAILSIZE < 1024}">
                    ${1}
                </c:when>
                <c:otherwise>
                    <fmt:formatNumber value="${fn:substringBefore(mailList.MAILSIZE/1024,'.')}"
                                      type="number" pattern="${numberFormat}"/>
                </c:otherwise>
            </c:choose>
            &nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
        </fanta:dataColumn>
    </fanta:table>
</div>
