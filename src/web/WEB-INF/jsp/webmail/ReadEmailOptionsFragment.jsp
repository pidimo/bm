<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<%--This fragment contains urls and parameters for some redirection logic
Parameter: mailSearch => When mailSearch=true then return to mails simple search
Parameter: mailAdvancedSearch => When mailAdvancedSearch=true then return to mails advanced search
--%>
<%
    Object mailFilter = request.getParameter("mailFilter");
    request.setAttribute("mailFilter", ((mailFilter != null && mailFilter.toString().length() > 0) ? mailFilter.toString() : "0"));
    Object mailSearch = request.getParameter("mailSearch");
    Object mailAdvancedSearch = request.getParameter("mailAdvancedSearch");
    request.setAttribute("mailSearch", ((mailSearch != null && mailSearch.toString().length() > 0) ? mailSearch.toString() : "false"));
    request.setAttribute("searchText", request.getParameter("searchText"));
    request.setAttribute("searchFilter", request.getParameter("searchFilter"));
    request.setAttribute("searchFolder", request.getParameter("searchFolder"));
    request.setAttribute("mailAdvancedSearch", ((mailAdvancedSearch != null && mailAdvancedSearch.toString().length() > 0) ? mailAdvancedSearch.toString() : "false"));
%>

<c:set var="mailFilterURLFragment" value="&mailFilter=${mailFilter}"/>
<c:set var="mailSearchURLFragment" value="&mailSearch=${mailSearch}"/>
<c:set var="searchTextURLFragment" value="&searchText=${searchText}"/>
<c:set var="searchFilterURLFragment" value="&searchFilter=${searchFilter}"/>
<c:set var="searchFolderURLFragment" value="&searchFolder=${searchFolder}"/>
<c:set var="mailAdvancedSearchURLFragment" value="&mailAdvancedSearch=${mailAdvancedSearch}"/>
<c:set var="folderIdURLFragment" value="&folderId=${folderView}"/>

<c:set var="navigationURLParams"
       value="${mailFilterURLFragment}${mailSearchURLFragment}${not empty(searchText)?searchTextURLFragment:''}${not empty(searchFilter)?searchFilterURLFragment:''}${not empty(searchFolder)?searchFolderURLFragment:''}${mailAdvancedSearchURLFragment}${(mailSearch=='false' && mailAdvancedSearch=='false')?folderIdURLFragment:''}&returning=true"/>

<c:if test="${empty systemFolders}">
    <c:set var="systemFolders"
           value="${app2:getSystemFolders(sessionScope.user.valueMap['userId'], pageContext.request)}"/>
</c:if>
<c:if test="${empty customFolders}">
    <c:set var="customFolders"
           value="${app2:getCustomFolders(sessionScope.user.valueMap['userId'], pageContext.request)}"/>
</c:if>

<c:set var="FOLDER_SENDITEMS" value="<%=WebMailConstants.FOLDER_SENDITEMS%>"/>
<c:set var="FOLDER_OUTBOX" value="<%=WebMailConstants.FOLDER_OUTBOX%>"/>
<c:set var="FOLDER_TRASH" value="<%=WebMailConstants.FOLDER_TRASH%>"/>
<c:set var="OUT_EMAIL" value="<%=WebMailConstants.OUT_VALUE%>"/>

<app:url value="/Mail/Reply.do?dto(mailId)=${mailId}&replyOperation=REPLY&dto(filterInvalidRecipients)=true"
         var="replyUrl"/>
<app:url value="/Mail/Reply.do?dto(mailId)=${mailId}&replyOperation=REPLYALL&dto(filterInvalidRecipients)=true"
         var="replyAllUrl"/>
<app:url value="/Mail/Reply.do?dto(mailId)=${mailId}&replyOperation=FORWARD&dto(filterInvalidRecipients)=true"
         var="forwardUrl"/>
<app:url
        value="/Mail/ReadOperation.do?dto(mailId)=${mailId}&dto(op)=moveToTrash&nextMailId=${param.nextMailId}&nextMailIndex=${param.nextMailIndex}"
        var="moveToTrash"/>
<app:url value="/Mail/MailTray.do?returning=true" var="cancelUrl"/>
<app:url value="/Mail/SearchResult.do?returning=true" var="searchCancelUrl"/>
<app:url value="/Mail/MailAdvancedSearch.do" var="advancedSearchCancelUrl"/>
<app:url value="/Mail/ReadEmail.do?dto(mailId)=${param.nextMailId}&mailIndex=${param.nextMailIndex}" var="nexEmailUrl"/>
<app:url value="/Mail/ReadEmail.do?dto(mailId)=${param.previousMailId}&mailIndex=${param.previousMailIndex}"
         var="previusEmailUrl"/>
<app:url value="/downloadEmail.do?dto(mailId)=${mailId}" var="downloadEmailUrl"/>


<app2:jScriptUrl
        url="/webmail/Mail/Print.do?dto(mailId)=${mailId}&dto(userMailId)=${sessionScope.user.valueMap['userId']}"
        var="jsPrintUrl" addModuleParams="false"/>
<app2:jScriptUrl
        url="/webmail/Mail/ReadOperation.do?dto(mailId)=${mailId}&dto(op)=moveTo&nextMailId=${param.nextMailId}&nextMailIndex=${param.nextMailIndex}${navigationURLParams}"
        var="jsMoveToUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(folderId)" value="obj.options[obj.selectedIndex].value"/>
</app2:jScriptUrl>

<app2:jScriptUrl
        url="/webmail/Mail/FullScreen.do?dto(mailId)=${mailId}&dto(userMailId)=${sessionScope.user.valueMap['userId']}"
        var="jsFullScreenUrl" addModuleParams="false"/>

<tags:initBootstrapSelectPopup/>
<tags:bootstrapModalPopup styleId="windowModalFullScreen" isLargeModal="true" modalTitleKey="Mail.Title.read"
                          isFullScreen="true"/>

<script type="text/javascript">
    function printMail() {
        window.open(${jsPrintUrl}, 'printMail', 'resizable=yes,width=686, height=650,left=50, top=10, scrollbars=yes');
    }

    function moveTo${moveToSelectId}() {
        var obj = document.getElementById('${moveToSelectId}');
        if (null != obj.options[obj.selectedIndex].value && "" != obj.options[obj.selectedIndex].value) {
            location.href = ${jsMoveToUrl};
        }
    }

    function viewFullScreen() {
        launchBootstrapPopup("windowModalFullScreen", ${jsFullScreenUrl}, 'name', true);
    }

</script>

<div class="row">
    <c:if test="${folderType != FOLDER_OUTBOX}">
        <div class="col-xs-12 col-sm-5 col-md-4">
            <div>
                <div class="col-xs-12 col-sm-6 col-md-5 marginButton">
                    <input type="button"
                           name="moveToButton"
                           value="<fmt:message key="Webmail.tray.moveTo"/>"
                           onclick="moveTo${moveToSelectId}();"
                           class="${app2:getFormButtonClasses()} pull-left"/>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-7 marginButton">
                    <select name="dto(op_parameter)" class="${app2:getFormSelectClasses()} webmailSelect"
                            id="${moveToSelectId}">
                        <option value=""></option>
                        <option value="${systemFolders['inboxId']}"><fmt:message key="Webmail.folder.inbox"/></option>
                        <option value="${systemFolders['sentId']}"><fmt:message
                                key="Webmail.folder.sendItems"/></option>
                        <option value="${systemFolders['draftId']}"><fmt:message
                                key="Webmail.folder.draftItems"/></option>
                        <option value="${systemFolders['trashId']}"><fmt:message key="Webmail.folder.trash"/></option>

                        <c:forEach var="folder" items="${customFolders}">
                            <option value="${folder.folderId}"><c:out value="${folder.folderName}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
    </c:if>
    <div class="col-xs-12 col-sm-5 col-md-6 marginButton">
        <c:if test="${not empty param.mailIndex}">
            <c:choose>
                <c:when test="${null != param.previousMailId}">
                    <a href="${previusEmailUrl}" titleKey="Webmail.previousMail"
                       title="<fmt:message key="Webmail.previousMail"/>" class="btn btn-link">
                        <span class="glyphicon glyphicon-arrow-up"></span>
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="glyphicon glyphicon-arrow-up"></span>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${null != param.nextMailId}">
                    <a href="${nexEmailUrl}" titleKey="Webmail.nextMail"
                       title="<fmt:message key="Webmail.nextMail"/>" class="btn btn-link">
                        <span class="glyphicon glyphicon-arrow-down"></span>
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="glyphicon glyphicon-arrow-down"></span>
                </c:otherwise>
            </c:choose>
        </c:if>
        <a href="javascript:viewFullScreen();" titleKey="Webmail.viewFullscreen"
           title="<fmt:message key="Webmail.viewFullscreen"/>" class="btn btn-link">
            <span class="glyphicon glyphicon-fullscreen"></span>
        </a>
        <a href="javascript:printMail();" titleKey="Common.print"
           title="<fmt:message key="Common.print"/>" class="btn btn-link">
            <span class="glyphicon glyphicon-print"></span>
        </a>
        <c:if test="${folderType != FOLDER_SENDITEMS && folderType != FOLDER_OUTBOX && incomingOutgoing != OUT_EMAIL }">
            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                <a href="${replyUrl}" titleKey="Webmail.reply" title="<fmt:message key="Webmail.reply"/>"
                   class="btn btn-link">
                    <i class="fa fa-reply"></i>
                </a>
                <a href="${replyAllUrl}" titleKey="Webmail.replyAll" title="<fmt:message key="Webmail.replyAll"/>"
                   class="btn btn-link">
                    <i class="fa fa-reply-all"></i>
                </a>
            </app2:checkAccessRight>
        </c:if>
        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
            <a href="${forwardUrl}" titleKey="Webmail.forward"
               title="<fmt:message key="Webmail.forward"/>"
               class="btn btn-link">
                <i class="fa fa-share"></i>
            </a>
        </app2:checkAccessRight>
        <a href="${downloadEmailUrl}" titleKey="Common.download"
           title="<fmt:message key="Common.download"/>"
           class="btn btn-link">
            <span class="glyphicon glyphicon-download-alt"></span>
        </a>
        <c:if test="${folderType != FOLDER_TRASH}">
            <a href="${moveToTrash}" titleKey="Common.delete"
               title="<fmt:message key="Common.delete"/>"
               class="btn btn-link">
                <span class="glyphicon glyphicon-trash"></span>
            </a>
        </c:if>
    </div>
    <div class="col-xs-12 col-sm-2 marginButton">
        <c:choose>
            <c:when test="${true == param.mailAdvancedSearch}">
                <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}"
                             onclick="location.href='${advancedSearchCancelUrl}'">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </c:when>

            <c:when test="${true == param.mailSearch}">
                <input type="button" class="${app2:getFormButtonCancelClasses()}"
                       onclick="location.href='${searchCancelUrl}';"
                       value="<fmt:message key="Common.cancel"/>">
            </c:when>

            <c:otherwise>
                <input type="button" class="${app2:getFormButtonCancelClasses()}"
                       onclick="location.href='${cancelUrl}';"
                       value="<fmt:message key="Common.cancel"/>">
            </c:otherwise>
        </c:choose>
    </div>
</div>
