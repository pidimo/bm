<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="MAIL_PRIORITY_HIGHT" value="<%=WebMailConstants.MAIL_PRIORITY_HIGHT%>"/>
<c:set var="BODY_TYPE_HTML" value="<%=WebMailConstants.BODY_TYPE_HTML%>"/>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>


<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
    <tr>
        <td colspan="3">
            <c:set var="folderType" value="${emailForm.dtoMap['folderType']}" scope="request"/>
            <c:set var="mailId" value="${emailForm.dtoMap['mailId']}" scope="request"/>
            <c:set var="moveToSelectId" value="topMoveToSelectId" scope="request"/>
            <c:set var="systemFolders" value="${systemFolderCounter}" scope="request"/>
            <c:set var="customFolders" value="${customFoldersList}" scope="request"/>
            <c:set var="incomingOutgoing" value="${emailForm.dtoMap['incomingOutgoing']}" scope="request"/>
            <c:import url="/common/webmail/ReadEmailOptionsFragment.jsp"/>
        </td>
    </tr>
    <html:form action="/Mail/Forward/ReadToCompose.do">
        <tr>
            <td class="title" colspan="3">
                <fmt:message key="Mail.Title.read"/>
            </td>
        </tr>
        <c:set var="emailPriority" value="${emailForm.dtoMap['mailPriority']}" scope="request"/>
        <c:set var="fromAddressId" value="${emailForm.dtoMap['fromAddressId']}" scope="request"/>
        <c:set var="fromAddressType" value="${emailForm.dtoMap['fromAddressType']}" scope="request"/>
        <c:set var="fromContactPersonId" value="${emailForm.dtoMap['fromContactPersonId']}" scope="request"/>
        <c:set var="sentDate" value="${emailForm.dtoMap['sentDate']}" scope="request"/>
        <c:set var="bodyType" value="${emailForm.dtoMap['bodyType']}" scope="request"/>
        <c:set var="mailId" value="${emailForm.dtoMap['mailId']}" scope="request"/>
        <c:set var="attachments" value="${emailForm.dtoMap['attachments']}" scope="request"/>
        <c:set var="htmlBodyPage"
               value="/webmail/Mail/ViewMailBody.do?dto(mailId)=${emailForm.dtoMap['mailId']}&dto(op)=readBody"
               scope="request"/>
        <c:if test="${'' == emailForm.dtoMap['cc']}">
            <c:set var="showRecipientsCC" value="false" scope="request"/>
        </c:if>
        <c:import url="/common/webmail/ReadEmailFragment.jsp"/>

    </html:form>
    <tr>
        <td colspan="3">
            <c:set var="folderType" value="${emailForm.dtoMap['folderType']}" scope="request"/>
            <c:set var="mailId" value="${emailForm.dtoMap['mailId']}" scope="request"/>
            <c:set var="moveToSelectId" value="bottomMoveToSelectId" scope="request"/>
            <c:set var="systemFolders" value="${systemFolderCounter}" scope="request"/>
            <c:set var="customFolders" value="${customFoldersList}" scope="request"/>
            <c:set var="incomingOutgoing" value="${emailForm.dtoMap['incomingOutgoing']}" scope="request"/>
            <c:import url="/common/webmail/ReadEmailOptionsFragment.jsp"/>
        </td>
    </tr>
</TABLE>