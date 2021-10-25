<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>

<tags:initSelectPopup/>
<calendar:initialize/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>
    function check()
    {
        field = document.getElementById('advancedSearchMailForm2').selectedMails;
        guia = document.getElementById('advancedSearchMailForm2').mail;

        var i;
        if (guia.checked)
        {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else
        {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }
    function setHiddenValue(hiddenId, hiddenValue) {
        document.getElementById(hiddenId).value = hiddenValue;
        document.getElementById('advancedSearchMailForm2').submit();
    }

    function moveToFolderAction() {
        document.getElementById('moveToButtonId').value = "true";
        document.getElementById('advancedSearchMailForm2').submit();
    }

    function testSubmit() {
        var test = (document.getElementById('moveToButtonId').value == "true");
        return(test);
    }
</script>

<c:set var="systemFolderCounter"
       value="${app2:getSystemFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>
<c:set var="customFoldersList"
       value="${app2:getCustomFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>

<%--Size of the list--%>
<%
    org.alfacentauro.fantabulous.controller.ResultList resultList = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("advancedSearchMailList");
    if (resultList != null) {
        pageContext.setAttribute("list_size", new Integer(resultList.getResultSize()));
    } else {
        pageContext.setAttribute("list_size", new Integer(0));
    }
%>

<%--Folder_type's--%>
<c:set var="FOLDER_INBOX" value="<%=WebMailConstants.FOLDER_INBOX%>"/>
<c:set var="FOLDER_SENDITEMS" value="<%=WebMailConstants.FOLDER_SENDITEMS%>"/>
<c:set var="FOLDER_DRAFTITEMS" value="<%=WebMailConstants.FOLDER_DRAFTITEMS%>"/>
<c:set var="FOLDER_TRASH" value="<%=WebMailConstants.FOLDER_TRASH%>"/>
<c:set var="FOLDER_OUTBOX" value="<%=WebMailConstants.FOLDER_OUTBOX%>"/>
<c:set var="FOLDER_DEFAULT" value="<%=WebMailConstants.FOLDER_DEFAULT%>"/>

<%--Mail_priority's--%>
<c:set var="MAIL_PRIORITY_DEFAULT" value="<%=WebMailConstants.MAIL_PRIORITY_DEFAULT%>"/>
<c:set var="MAIL_PRIORITY_HIGHT" value="<%=WebMailConstants.MAIL_PRIORITY_HIGHT%>"/>

<c:set var="MAIL_IN" value="<%=String.valueOf(WebMailConstants.IN_VALUE)%>"/>
<c:set var="MAIL_OUT" value="<%=String.valueOf(WebMailConstants.OUT_VALUE)%>"/>

<%--folder column tio show constants--%>
<c:set var="SHOW_TO" value="<%=WebMailConstants.ColumnToShow.TO.getConstantAsString()%>"/>
<c:set var="SHOW_FROM_TO" value="<%=WebMailConstants.ColumnToShow.FROM_TO.getConstantAsString()%>"/>

<c:choose>
    <c:when test="${empty(advancedSearchMailForm.params['mailFolderId'])}">
        <c:set var="folderColumnToShow" value="${SHOW_FROM_TO}"/>
    </c:when>
    <c:otherwise>
        <c:set var="folderColumnToShow"
               value="${app2:getFolderColumnToShow(advancedSearchMailForm.params['mailFolderId'])}"/>
    </c:otherwise>
</c:choose>


<html:form action="/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true" styleId="advancedSearchMailForm"
           focus="parameter(mailSubject)">
    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
        <tr>
            <td height="20" class="title" colspan="4"><fmt:message key="${pageTitle}"/></td>
        </tr>
        <tr>
            <td class="label" width="15%"><fmt:message key="Mail.subject"/></td>
            <td class="contain" width="35%">
                <app:text property="parameter(mailSubject)"
                          styleClass="largeText"
                          maxlength="245" tabindex="1"/>
            </td>
            <td class="label" width="15%"><fmt:message key="Webmail.advancedSearch.folder"/></td>
            <td class="contain" width="35%">
                <html:select property="parameter(searchMailFolderId)" styleClass="webmailSelect" tabindex="10">
                    <html:option value=""/>
                    <html:option value="${systemFolderCounter['inboxId']}">
                        <fmt:message key="Webmail.folder.inbox"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['sentId']}">
                        <fmt:message key="Webmail.folder.sendItems"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['draftId']}">
                        <fmt:message
                                key="Webmail.folder.draftItems"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['trashId']}">
                        <fmt:message key="Webmail.folder.trash"/>
                    </html:option>
                    <html:option value="${systemFolderCounter['outboxId']}">
                        <fmt:message key="Webmail.folder.outbox"/>
                    </html:option>
                    <c:forEach var="customFolder" items="${customFoldersList}">
                        <html:option value="${customFolder.folderId}">${customFolder.folderName}</html:option>
                    </c:forEach>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Mail.from"/></td>
            <td class="contain">
                <app:text property="parameter(mailFrom)" styleClass="largeText" styleId="fromFieldId" maxlength="245"
                          tabindex="2"/>
            </td>
            <td class="label"><fmt:message key="Mail.priority"/></td>
            <td class="contain">
                <c:set var="mailPrioritiesList" value="${app2:getPriorityValues(pageContext.request)}"/>
                <html:select property="parameter(mailPriority)" styleClass="webmailSelect" tabindex="11">
                    <html:options collection="mailPrioritiesList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Mail.to"/></td>
            <td class="contain">
                <app:text property="parameter(mailTo)" styleClass="largeText" styleId="toRecipientId" maxlength="245"
                          tabindex="3"/>
            </td>
            <td class="label"><fmt:message key="Webmail.advancedSearch.mailState"/></td>
            <td class="contain">
                <c:set var="mailStatesList" value="${app2:getMailStates(pageContext.request)}"/>
                <html:select property="parameter(mailStateParam)" styleClass="webmailSelect" tabindex="12">
                    <html:options collection="mailStatesList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Mail.Cc"/></td>
            <td class="contain">
                <app:text property="parameter(mailToCC)" styleClass="largeText" styleId="ccRecipientId" maxlength="245"
                          tabindex="4"/>
            </td>
            <td class="label"><fmt:message key="Mail.attachments"/></td>
            <td class="contain">
                <c:set var="mailAttachStatesList" value="${app2:getAttachStates(pageContext.request)}"/>
                <html:select property="parameter(mailAttach)" styleClass="webmailSelect" tabindex="13">
                    <html:options collection="mailAttachStatesList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>

            <td class="label">
                <fmt:message key="Mail.bodyContent"/>
            </td>
            <td class="contain">
                <app:text property="parameter(body)" styleClass="largeText" styleId="fromFieldId" maxlength="245"
                          tabindex="5"/>
            </td>

            <td class="label"><fmt:message key="Webmail.advancedSearch.isCommunicationRelated"/></td>
            <td class="contain">
                <c:set var="mailCommunicationStatesList" value="${app2:getCommunicationStates(pageContext.request)}"/>
                <html:select property="parameter(mailCommunication)" styleClass="webmailSelect" tabindex="14">
                    <html:options collection="mailCommunicationStatesList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>

            <td class="label"><fmt:message key="Webmail.tray.date"/></td>
            <td class="contain">
                <fmt:message key="datePattern" var="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(startSendDate)" maxlength="10" styleId="startRange"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              parseLongAsDate="true" tabindex="6"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endSendDate)" maxlength="10" styleId="endRange"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              parseLongAsDate="true" tabindex="7"/>
            </td>


            <td class="label"><fmt:message key="Webmail.advancedSearch.incout"/></td>
            <td class="contain">
                <c:set var="mailIncomingOutgoingStatesList" value="${app2:getIncOutValues(pageContext.request)}"/>
                <html:select property="parameter(inOut)" styleClass="webmailSelect" tabindex="15">
                    <html:options collection="mailIncomingOutgoingStatesList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="Mail.size"/>&nbsp;(<fmt:message key="Webmail.mailTray.Kb"/>)
            </td>
            <td class="contain" colspan="3">
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:numberText property="parameter(startSizeRange)" styleClass="numberText" maxlength="10"
                                numberType="integer" tabindex="8"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:numberText property="parameter(endSizeRange)" styleClass="numberText" maxlength="10"
                                numberType="integer" tabindex="9"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="15">
                    <fmt:message key="Common.go"/>
                </html:submit>
                <html:button property="reset1" tabindex="16" styleClass="button"
                             onclick="formReset('advancedSearchMailForm')">
                    <fmt:message key="Common.clear"/>
                </html:button>
                &nbsp;
            </td>
        </tr>
    </table>
</html:form>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>

<c:set var="mail_subject_size" value="${33}"/>
<c:set var="mail_folderName_size" value="${13}"/>
<c:set var="mail_from_size" value="${23}"/>

<fmt:message key="Webmail.updateCommunications" var="communicationMessage"/>
<html:form action="/Mail/MailAdvancedSearchOperation.do?mailSearch=true&mailAdvancedSearch=true"
           styleId="advancedSearchMailForm2" onsubmit="return testSubmit()">
<html:hidden property="moveToButton" styleId="moveToButtonId"/>
<html:hidden property="emailIdentifiers" value="${emailIdentifiersAdvanced}" styleId="emails"/>
<table width="100%" border="0" align="left" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td width="10%" style="text-align:left;">
        <html:hidden property="moveToTrash" styleId="moveToTrashId"/>
        <a href="#" onclick="javascript:setHiddenValue('moveToTrashId','moveToTrash');"
           style="text-decoration: none;">
            <html:img src="${baselayout}/img/delete.gif"
                      titleKey="Webmail.moveToTrash"
                      border="0" styleClass="imageButton" style="vertical-align:middle;"/>
        </a>

        <html:hidden property="emptyFolderToTrash" styleId="emptyFolderToTrashId"/>
        <a href="#"
           onclick="javascript:setHiddenValue('emptyFolderToTrashId','emptyFolderToTrash');"
           style="text-decoration: none;">
            <html:img src="${baselayout}/img/deleteall.gif"
                      titleKey="Webmail.moveAllToTrash"
                      border="0" styleClass="imageButton" style="vertical-align:middle;"/>
        </a>
    </td>

    <TD width="90%" style="text-align:left">
        <html:button styleClass="button" property="dto(button)" onclick="moveToFolderAction()" tabindex="17">
            <fmt:message key="Webmail.tray.moveTo"/>
        </html:button>&nbsp;
        <html:select property="moveToFolderId" styleClass="webmailSelect" tabindex="18" styleId="moveToFolderSelect">
            <html:option value=""/>
            <html:option value="${systemFolderCounter['inboxId']}">
                <fmt:message key="Webmail.folder.inbox"/>
            </html:option>
            <html:option value="${systemFolderCounter['sentId']}">
                <fmt:message key="Webmail.folder.sendItems"/>
            </html:option>
            <html:option value="${systemFolderCounter['draftId']}">
                <fmt:message
                        key="Webmail.folder.draftItems"/>
            </html:option>
            <html:option value="${systemFolderCounter['trashId']}">
                <fmt:message key="Webmail.folder.trash"/>
            </html:option>
            <html:option value="${systemFolderCounter['outboxId']}">
                <fmt:message key="Webmail.folder.outbox"/>
            </html:option>
            <c:forEach var="customFolder" items="${customFoldersList}">
                <html:option value="${customFolder.folderId}">${customFolder.folderName}</html:option>
            </c:forEach>
        </html:select>
    </TD>
</tr>
<tr>
<td colspan="2">
<fanta:table action="/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true" list="advancedSearchMailList"
             id="mailList"
             width="100%" imgPath="${baselayout}" align="center">
<c:set var="readMailAction"
       value="Mail/ReadEmail.do?dto(mailId)=${mailList.MAILID}&mailIndex=${mailList.MAILINDEX}&mailSearch=true&mailAdvancedSearch=true"/>
<c:if test="${list_size >0}">
    <fanta:checkBoxColumn styleClass="radio listItemCenter" name="mail" id="selectedMails" onClick="javascript:check();"
                          property="MAILID" headerStyle="listHeader" width="3%"/>
</c:if>

<app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="2%">

        <c:choose><%--for the mailcontacts--%>
            <c:when test="${app2:isActionMailContact(mailList.ACTIONMAILCONTACTCOUNT,mailList.INOUT)}">
                <c:set var="iconName" value="emailaction.gif"/>
                <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                    <c:set var="iconName" value="emailaction-half.gif"/>
                </c:if>
                <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications" styleClass="listItemCenter"
                                    headerStyle="listHeader"
                                    image="${sessionScope.baselayout}/img/webmail/${iconName}"/>
            </c:when>
            <c:when test="${app2:isSupportActivityMailContact(mailList.SUPPORTMAILCONTACTCOUNT,mailList.INOUT)}">
                <c:set var="iconName" value="emailactivity.gif"/>
                <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                    <c:set var="iconName" value="emailactivity-half.gif"/>
                </c:if>
                <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications" styleClass="listItemCenter"
                                    headerStyle="listHeader"
                                    image="${sessionScope.baselayout}/img/webmail/${iconName}"/>
            </c:when>
            <c:when test="${app2:hasMailContact(mailList.MAILCONTACTCOUNT,mailList.INOUT)}">
                <c:set var="iconName" value="emailcomm.gif"/>
                <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                    <c:set var="iconName" value="emailcomm-half.gif"/>
                </c:if>
                <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications" styleClass="listItemCenter"
                                    headerStyle="listHeader"
                                    render="false">
                    <c:if test="${mailList.INOUT == MAIL_IN || mailList.INOUT ==  MAIL_OUT}">
                        <html:link
                                action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}&mailSearch=true&mailAdvancedSearch=true">
                            <html:img src="${sessionScope.baselayout}/img/webmail/${iconName}" border="0"
                                      title="${communicationMessage}"/>
                        </html:link>
                    </c:if>
                </fanta:actionColumn>
            </c:when>
            <c:otherwise>
                <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications" styleClass="listItemCenter"
                                    headerStyle="listHeader"
                                    render="false">
                    <c:if test="${(mailList.INOUT == MAIL_IN || mailList.INOUT ==  MAIL_OUT) && (1 == mailList.ISCOMPLETEEMAIL || empty mailList.ISCOMPLETEEMAIL)}">
                        <html:link
                                action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}&mailSearch=true&mailAdvancedSearch=true">
                            <html:img src="${sessionScope.baselayout}/img/webmail/emailcommbroke.gif" border="0"
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
        <fanta:dataColumn name="" styleClass="listItem"
                          title="${folderColumnToShow eq SHOW_TO ? 'Webmail.folder.show.to' : 'Webmail.folder.show.fromTo'}"
                          headerStyle="listHeader"
                          width="15%" renderData="false">

            <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">

                <c:if test="${folderColumnToShow eq SHOW_FROM_TO}">
                    <c:choose>
                        <c:when test="${mailList.INOUT == MAIL_OUT}">
                            <html:img src="${baselayout}/img/out_.gif" border="0" altKey="Webmail.folder.show.to"
                                      titleKey="Webmail.folder.show.to"/>
                        </c:when>
                        <c:otherwise>
                            <html:img src="${baselayout}/img/in_.gif" border="0" altKey="Webmail.folder.show.from"
                                      titleKey="Webmail.folder.show.from"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <app:link action="${readMailAction}"
                          title="${mailList.MAILTOFROM}">${app2:filterForHtml(mailList.MAILTOFROM)}</app:link>
            </fanta:textShorter>
        </fanta:dataColumn>
    </c:when>
    <c:otherwise>
        <fanta:dataColumn name="MAILPERSONALFROM" styleClass="listItem" title="Webmail.tray.from"
                          headerStyle="listHeader" width="15%" renderData="false" orderable="true">

            <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">
                <app:link action="${readMailAction}">
                    <c:out value="${mailList.MAILTOFROM}"/>
                </app:link>
            </fanta:textShorter>
        </fanta:dataColumn>
    </c:otherwise>
</c:choose>
<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.answeredOrForwarded" headerStyle="listHeader"
                  width="2%" renderData="false" headerImage="${sessionScope.baselayout}/img/webmail/reply.gif">
    <c:choose>
        <c:when test="${app2:hasMailStateAnswered(mailList.MAILSTATE)}"><!--REPLY,REPLY_ALL-->
            <img src="${sessionScope.baselayout}/img/webmail/reply.gif"
                 title="<fmt:message key="Webmail.tray.answered"/>"
                 alt="<fmt:message key="Webmail.tray.answered"/>"/>
        </c:when>
        <c:when test="${app2:hasMailStateForward(mailList.MAILSTATE)}"><!--FORWARD-->
            <img src="${sessionScope.baselayout}/img/webmail/forward.gif"
                 title="<fmt:message key="Webmail.tray.forwarded"/>"
                 alt="<fmt:message key="Webmail.tray.forwarded"/>"/>
        </c:when>
    </c:choose>
</fanta:dataColumn>
<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.attach" headerStyle="listHeader"
                  width="2%" renderData="false"
                  headerImage="${sessionScope.baselayout}/img/webmail/paperclip.gif">
    <c:if test="${1 == mailList.MAILHASATTACH}">
        <img src="${sessionScope.baselayout}/img/webmail/paperclip.gif"
             title="<fmt:message key="Webmail.tray.attach"/>"
             alt="<fmt:message key="Webmail.tray.attach"/>"/>
    </c:if>
</fanta:dataColumn>
<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.highPriority" headerStyle="listHeader"
                  width="2%" renderData="false"
                  headerImage="${sessionScope.baselayout}/img/webmail/prio_high.gif">
    <c:if test="${mailList.MAILPRIORITY== MAIL_PRIORITY_HIGHT}"><!--High priority-->
        <img src="${sessionScope.baselayout}/img/webmail/prio_high.gif"
             title="<fmt:message key="Webmail.tray.highPriority"/>"
             alt="<fmt:message key="Webmail.tray.highPriority"/>"/>
    </c:if>
</fanta:dataColumn>
<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.readOrUnRead" headerStyle="listHeader"
                  width="2%" renderData="false"
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
<fanta:dataColumn name="MAILSUBJECT" action="${readMailAction}" styleClass="listItem" title="Webmail.tray.subject"
                  headerStyle="listHeader" renderData="false" width="22%" orderable="true">
    <fanta:textShorter title="${mailList.MAILSUBJECT}" fontWeight="${unreadFontWeight}">
        ${mailList.MAILSUBJECT}
    </fanta:textShorter>
</fanta:dataColumn>
<fanta:dataColumn name="SENTDATETIME" styleClass="listItem" title="Webmail.tray.date" headerStyle="listHeader"
                  width="15%" orderable="true" renderData="false" nowrap="nowrap">
    ${app2:getDateWithTimeZone(mailList.SENTDATETIME, timeZone, dateTimePattern)}
</fanta:dataColumn>
<fanta:dataColumn name="FOLDERNAME" styleClass="listItem" title="Webmail.tray.folder" headerStyle="listHeader"
                  width="9%" orderable="true" renderData="false">
    <c:choose>
        <c:when test="${mailList.FOLDERTYPE==FOLDER_INBOX}">
            <fmt:message key="Webmail.folder.inbox" var="folderName"/>
        </c:when>
        <c:when test="${mailList.FOLDERTYPE==FOLDER_SENDITEMS}">
            <fmt:message key="Webmail.folder.sendItems" var="folderName"/>
        </c:when>
        <c:when test="${mailList.FOLDERTYPE==FOLDER_DRAFTITEMS}">
            <fmt:message key="Webmail.folder.draftItems" var="folderName"/>
        </c:when>
        <c:when test="${mailList.FOLDERTYPE==FOLDER_TRASH}">
            <fmt:message key="Webmail.folder.trash" var="folderName"/>
        </c:when>
        <c:when test="${mailList.FOLDERTYPE==FOLDER_OUTBOX}">
            <fmt:message key="Webmail.folder.outbox" var="folderName"/>
        </c:when>
        <c:when test="${mailList.FOLDERTYPE==FOLDER_DEFAULT}">
            <c:set var="folderName" value="${mailList.FOLDERNAME}"/>
        </c:when>
    </c:choose>
    <fanta:textShorter title="${folderName}">${folderName}</fanta:textShorter>
</fanta:dataColumn>
<fanta:dataColumn name="MAILSIZE" styleClass="listItem2Right" title="Webmail.tray.size" headerStyle="listHeader"
                  width="7%" renderData="false" orderable="true" nowrap="true">
    <c:choose>
        <c:when test="${mailList.MAILSIZE<1024}">
            ${1}&nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
        </c:when>
        <c:otherwise>
            <fmt:formatNumber value="${fn:substringBefore(mailList.MAILSIZE/1024,'.')}"
                              type="number" pattern="${numberFormat}"/>&nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
        </c:otherwise>
    </c:choose>
</fanta:dataColumn>
</fanta:table>
</td>
</tr>
</table>
</html:form>