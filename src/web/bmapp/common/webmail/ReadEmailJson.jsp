<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="BODY_TYPE_TEXT" value="<%=WebMailConstants.BODY_TYPE_TEXT%>"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
<fmt:message var="dateTimePattern" key="Webmail.mail.dateTimePatternNoTimezone"/>

{
"entity" : {
    "mailId" : "${emailForm.dtoMap['mailId']}",
    "companyId" : "${emailForm.dtoMap['companyId']}",
    "folderId" : "${emailForm.dtoMap['folderId']}",
    "folderType" : "${emailForm.dtoMap['folderType']}",
    "mailAccountId" : "${emailForm.dtoMap['mailAccountId']}",
    "mailPriority" : "${emailForm.dtoMap['mailPriority']}",
    "attachment" : "${emailForm.dtoMap['attachment']}",
    "sentDate" : "${app2:getLocaleFormattedDateTime(emailForm.dtoMap['sentDate'], timeZone, dateTimePattern, locale)}",

    "mailState" : "${emailForm.dtoMap['mailState']}",
    "replyMode" : "${emailForm.dtoMap['replyMode']}",

    "from" : "${app2:escapeJSON(emailForm.dtoMap['from'])}",
    "mailFrom" : "${app2:escapeJSON(emailForm.dtoMap['mailFrom'])}",
    "mailPersonalFrom" : "${app2:escapeJSON(emailForm.dtoMap['mailPersonalFrom'])}",

    "to" : "${app2:escapeJSON(emailForm.dtoMap['to'])}",
    "cc" : "${app2:escapeJSON(emailForm.dtoMap['cc'])}",
    "bcc" : "${app2:escapeJSON(emailForm.dtoMap['bcc'])}",
    "toWithOutAccount" : "${app2:escapeJSON(emailForm.dtoMap['toWithOutAccount'])}",
    "ccWithOutAccount" : "${app2:escapeJSON(emailForm.dtoMap['ccWithOutAccount'])}",
    "mailSubject" : "${app2:escapeJSON(emailForm.dtoMap['mailSubject'])}",
    "bodyType" : "${emailForm.dtoMap['bodyType']}",
    "body" : "${(BODY_TYPE_TEXT eq emailForm.dtoMap['bodyType']) ? app2:escapeJSON(emailForm.dtoMap['body']) : ""}",
    "htmlBodyUrl" : "/webmail/Mail/ViewMailBody.do?dto(mailId)=${emailForm.dtoMap['mailId']}&dto(op)=readBody",

    <c:set var="attachments" value="${emailForm.dtoMap['attachments']}" scope="request"/>
    <c:import url="/bmapp/common/webmail/AttachmentJsonFragment.jsp"/>
    },

<c:import url="/bmapp/common/webmail/ComposeMailCatalogsJsonFragment.jsp"/>
}
