<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.WebMailConstants,
                 java.util.List" %>
<c:import url="/common/webmail/ReturnToMailTrayUrlFragment.jsp"/>

<c:set var="mailFilterURLFragment" value="&mailFilter=${mailFilter}"/>
<c:set var="mailSearchURLFragment" value="&mailSearch=${mailSearch}"/>
<c:set var="searchTextURLFragment" value="&searchText=${searchText}"/>
<c:set var="searchFilterURLFragment" value="&searchFilter=${searchFilter}"/>
<c:set var="searchFolderURLFragment" value="&searchFolder=${searchFolder}"/>
<c:set var="mailAdvancedSearchURLFragment" value="&mailAdvancedSearch=${mailAdvancedSearch}"/>
<c:set var="folderIdURLFragment" value="&folderId=${folderView}"/>

<c:set var="navigationURLParams"
       value="${mailFilterURLFragment}${mailSearchURLFragment}${not empty(searchText)?searchTextURLFragment:''}${not empty(searchFilter)?searchFilterURLFragment:''}${not empty(searchFolder)?searchFolderURLFragment:''}${mailAdvancedSearchURLFragment}${(mailSearch=='false' && mailAdvancedSearch=='false')?folderIdURLFragment:''}&returning=true"/>

<script>
    function check()
    {
        field = document.getElementById('searchMailFormX').selectedMails;
        guia = document.getElementById('searchMailFormX').mail;

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
        document.getElementById('searchMailFormX').submit();
    }
    function moveToFolderAction() {
        document.getElementById('moveToButtonId').value = "true";
        document.getElementById('searchMailFormX').submit();
    }

    function testSubmit() {
        var test = (document.getElementById('moveToButtonId').value == "true");
        return(test);
    }
</script>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>
<%--Size of the list--%>
<%
    org.alfacentauro.fantabulous.controller.ResultList resultList = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("searchMailList");
    pageContext.setAttribute("list_size", new Integer(resultList.getResultSize()));
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

<%--MarkAs Constants--%>
<% List markAsList = JSPHelper.getMarkAsList(request);
    request.setAttribute("markAsList", markAsList);%>
<%--Filter Constants--%>
<% List mailTrayFilterList = JSPHelper.getMailTrayFilterList(request);
    request.setAttribute("mailTrayFilterList", mailTrayFilterList);%>


<html:form action="/Mail/SearchResult.do" styleId="searchMailFormX" onsubmit="return testSubmit()">
<table id="SearchResult.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
    <html:hidden property="emailIdentifiers" value="${emailIdentifiers}" styleId="test"/>
    <html:hidden property="searchFolder" value="${searchMailForm.searchFolder}"/>
    <html:hidden property="searchText" value="${searchMailForm.searchText}"/>
    <html:hidden property="searchFilter" value="${searchMailForm.searchFilter}"/>
    <TR>
        <td>
            <table id="SearchResult.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" heigth="50%"
                   align="center">
                <TR>
                    <TD width="100%" class="title">
                        &nbsp;<fmt:message key="Webmail.search.searchResults"/>
                    </TD>
                </TR>
            </table>
        </td>
    </TR>
    <TR>
        <TD>
            <table id="SearchResult.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center"
                   class="container">
                <TR>
                    <td class="contain" nowrap="true">
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

                    <TD width="9%" class="label" style="text-align:right;">
                        <c:set var="valueList3" value="${searchMailForm.dto.folderConstants}"/>
                        <html:button styleClass="button" property="dto(button)" onclick="moveToFolderAction()">
                            <fmt:message key="Webmail.tray.moveTo"/>
                        </html:button>
                    </TD>
                    <TD width="24%" class="contain">
                        <html:select property="moveTo" styleClass="webmailSelect">
                            <option value=""></option>
                            <html:option value="${systemFolderCounter['inboxId']}"><fmt:message
                                    key="Webmail.folder.inbox"/></html:option>
                            <html:option value="${systemFolderCounter['sentId']}"><fmt:message
                                    key="Webmail.folder.sendItems"/></html:option>
                            <html:option value="${systemFolderCounter['draftId']}"><fmt:message
                                    key="Webmail.folder.draftItems"/></html:option>
                            <html:option value="${systemFolderCounter['trashId']}"><fmt:message
                                    key="Webmail.folder.trash"/></html:option>
                            <html:options collection="valueList3" property="mailFolderId"
                                          labelProperty="mailFolderName"/>
                        </html:select>
                    </TD>
                    <TD width="9%" class="label">
                        <fmt:message key="Webmail.tray.filter"/>
                    </TD>
                    <TD width="24%" class="contain">
                        <html:select property="mailFilter" styleClass="webmailSelect" onchange="submit()">
                            <html:options collection="mailTrayFilterList" property="value" labelProperty="label"/>
                        </html:select>
                    </TD>
                    <TD width="9%" class="label">
                        <c:set var="valueList2" value="${searchMailForm.dto.actionConstants}"/>
                        <fmt:message key="Webmail.tray.markAs"/>
                    </TD>
                    <TD width="24" class="contain">
                        <html:select property="markAs" styleClass="webmailSelect" onchange="submit()">
                            <option value=""></option>
                            <html:options collection="markAsList" property="value" labelProperty="label"/>
                        </html:select>
                    </TD>
                </TR>
                <TR>
                    <TD colspan="7" width="100%">&nbsp;</TD>
                </TR>
            </table>
        </TD>
    </TR>
</table>
<c:set var="mail_subject_size" value="${33}"/>
<c:set var="mail_folderName_size" value="${13}"/>
<c:set var="mail_from_size" value="${23}"/>

<c:set var="SEARCHALLFOLDERS" value="<%=WebMailConstants.SEARCH_ALL_FOLDERS%>"/>
<c:choose>
    <c:when test="${searchMailForm.searchFolder eq SEARCHALLFOLDERS}">
        <c:set var="folderColumnToShow" value="${SHOW_FROM_TO}"/>
    </c:when>
    <c:otherwise>
        <c:set var="folderColumnToShow" value="${app2:getFolderColumnToShow(searchMailForm.searchFolder)}"/>
    </c:otherwise>
</c:choose>

<fmt:message key="Webmail.updateCommunications" var="communicationMessage"/>

<fanta:table list="searchMailList" width="100%" id="mailList"
             action="Mail/SearchResult.do?mailFilter=${app2:encode(searchMailForm.mailFilter)}&searchFolder=${app2:encode(searchMailForm.searchFolder)}&searchText=${app2:encode(searchMailForm.searchText)}&searchFilter=${app2:encode(searchMailForm.searchFilter)}&mailSearch=${true}"
             imgPath="${baselayout}" align="center">
<c:set var="mailAction"
       value="Mail/ReadEmail.do?dto(mailId)=${mailList.MAILID}&mailSearch=${true}&searchFolder=${app2:encode(searchMailForm.searchFolder)}&searchFilter=${app2:encode(searchMailForm.searchFilter)}&searchText=${app2:encode(searchMailForm.searchText)}&mailFilter=${app2:encode(mailFilter)}&mailIndex=${mailList.MAILINDEX}"/>
<c:if test="${list_size >0}">
    <fanta:checkBoxColumn styleClass="radio listItemCenter" name="mail" id="selectedMails" onClick="javascript:check();"
                          property="MAILID" headerStyle="listHeader" width="3%"/>
</c:if>


<app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="4%">

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
                                action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}${navigationURLParams}">
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
                                action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}${navigationURLParams}">
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
                          width="20%" renderData="false">

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

                <app:link action="${mailAction}"
                          title="${mailList.MAILTOFROM}">${app2:filterForHtml(mailList.MAILTOFROM)}</app:link>
            </fanta:textShorter>
        </fanta:dataColumn>
    </c:when>
    <c:otherwise>
        <fanta:dataColumn name="MAILPERSONALFROM" styleClass="listItem" title="Webmail.tray.from"
                          headerStyle="listHeader" width="20%" renderData="false" orderable="true">

            <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">
                <app:link action="${mailAction}">
                    <c:out value="${mailList.MAILTOFROM}"/>
                </app:link>
            </fanta:textShorter>
        </fanta:dataColumn>
    </c:otherwise>
</c:choose>


<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.answeredOrForwarded" headerStyle="listHeader"
                  width="3%" renderData="false" headerImage="${sessionScope.baselayout}/img/webmail/reply.gif">
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
                  width="3%" renderData="false"
                  headerImage="${sessionScope.baselayout}/img/webmail/paperclip.gif">

    <c:if test="${1 == mailList.MAILHASATTACH}">
        <img src="${sessionScope.baselayout}/img/webmail/paperclip.gif"
             title="<fmt:message key="Webmail.tray.attach"/>"
             alt="<fmt:message key="Webmail.tray.attach"/>"/>
    </c:if>
</fanta:dataColumn>
<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.highPriority" headerStyle="listHeader"
                  width="3%" renderData="false"
                  headerImage="${sessionScope.baselayout}/img/webmail/prio_high.gif">
    <c:if test="${mailList.MAILPRIORITY== MAIL_PRIORITY_HIGHT}"><!--High priority-->
        <img src="${sessionScope.baselayout}/img/webmail/prio_high.gif"
             title="<fmt:message key="Webmail.tray.highPriority"/>"
             alt="<fmt:message key="Webmail.tray.highPriority"/>"/>
    </c:if>
</fanta:dataColumn>
<fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.readOrUnRead" headerStyle="listHeader"
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
<fanta:dataColumn name="MAILSUBJECT" action="${mailAction}" styleClass="listItem" title="Webmail.tray.subject"
                  headerStyle="listHeader" renderData="false" width="30%" orderable="true">
    <fanta:textShorter title="${mailList.MAILSUBJECT}" fontWeight="${unreadFontWeight}">
        ${mailList.MAILSUBJECT}
    </fanta:textShorter>
</fanta:dataColumn>
<fanta:dataColumn name="SENTDATETIME" styleClass="listItem" title="Webmail.tray.date" headerStyle="listHeader"
                  width="15%" orderable="true" renderData="false" nowrap="nowrap">
    ${app2:getDateWithTimeZone(mailList.SENTDATETIME, timeZone, dateTimePattern)}
</fanta:dataColumn>
<fanta:dataColumn name="FOLDERNAME" styleClass="listItem" title="Webmail.tray.folder" headerStyle="listHeader"
                  width="10%" orderable="true" renderData="false">
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
                  width="8%" renderData="false" orderable="true" nowrap="true">
    <c:choose>
        <c:when test="${mailList.MAILSIZE<1024}">
            ${1}&nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
        </c:when>
        <c:otherwise>
            <fmt:formatNumber value="${fn:substringBefore(mailList.MAILSIZE/1024,'.')}"
                              type="number" pattern="${numberFormat}"/>
            &nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
        </c:otherwise>
    </c:choose>
</fanta:dataColumn>
</fanta:table>
<html:hidden property="moveToButton" styleId="moveToButtonId"/>
</html:form>