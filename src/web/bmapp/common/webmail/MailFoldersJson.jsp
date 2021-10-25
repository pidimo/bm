<%@ include file="/Includes.jsp" %>

<fmt:message var="inboxName" key="Webmail.folder.inbox"/>
<fmt:message var="sendName" key="Webmail.folder.sendItems"/>
<fmt:message var="draftName" key="Webmail.folder.draftItems"/>
<fmt:message var="trashName" key="Webmail.folder.trash"/>
<fmt:message var="outboxName" key="Webmail.folder.outbox"/>

<c:set var="systemFolderCounter" value="${app2:getSystemFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>
<c:set var="customFoldersJSONArray" value="${app2:getCustomFoldersAsJSONArray(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>

{
"systemFolders" : {
    "inboxId" : "${systemFolderCounter['inboxId']}",
    "inboxSize" : "${systemFolderCounter['inboxSize']}",
    "inboxUnreadSize" : "${systemFolderCounter['unreadSize']}",
    "inboxName" : "${app2:escapeJSON(inboxName)}",

    "sentId" : "${systemFolderCounter['sentId']}",
    "sentSize" : "${systemFolderCounter['sentSize']}",
    "sendName" : "${app2:escapeJSON(sendName)}",

    "draftId" : "${systemFolderCounter['draftId']}",
    "draftSize" : "${systemFolderCounter['draftSize']}",
    "draftName" : "${app2:escapeJSON(draftName)}",

    "trashId" : "${systemFolderCounter['trashId']}",
    "trashSize" : "${systemFolderCounter['trashSize']}",
    "trashUnreadSize" : "${systemFolderCounter['trashUnreadSize']}",
    "trashName" : "${app2:escapeJSON(trashName)}",

    "outboxId" : "${systemFolderCounter['outboxId']}",
    "outboxSize" : "${systemFolderCounter['outboxSize']}",
    "outboxName" : "${app2:escapeJSON(outboxName)}"
    },

"customFolderArray": ${customFoldersJSONArray}

}
