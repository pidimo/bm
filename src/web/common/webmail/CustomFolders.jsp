<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    if (request.getAttribute("fromAError") == null) {
        Object mailSearch = request.getParameter("mailSearch");
        request.setAttribute("mailSearch", (mailSearch != null ? mailSearch.toString() : "false"));
    }
%>

<table border="0" cellpadding="0" cellspacing="0" align="center" class="container" width="100%">
    <tr>
        <td colspan="2" class="title" nowrap="nowrap" style="padding-right:2px;">
            <fmt:message key="Webmail.customFolder"/>&nbsp;
            <app2:checkAccessRight functionality="WEBMAILFOLDER" permission="CREATE">
                <app:link action="/Mail/Forward/CreateFolder.do?tabKey=Webmail.folder.plural&inCompose=compose">[
                    <fmt:message key="Common.add"/> ]</app:link>
            </app2:checkAccessRight>
        </td>
    </tr>

    <c:set var="actualFolderId" value="${folderView}"/>

    <c:forEach var="customFolder" items="${customFoldersList}">
        <c:set value="/Mail/MailTray.do?folderId=${customFolder.folderId}&index=0" var="urlFolder"/>
        <c:choose>
            <c:when test="${customFolder.folderId == actualFolderId && mailSearch == 'false'}">
                <c:set var="tdClassId" value="selectedContain"/>
                <c:set var="imgFolderPath" value="${sessionScope.baselayout}/img/webmail/sent.gif"/>
            </c:when>
            <c:otherwise>
                <c:set var="tdClassId" value=""/>
                <c:set var="imgFolderPath" value="${sessionScope.baselayout}/img/webmail/folder.gif"/>
            </c:otherwise>
        </c:choose>

        <c:set var="boldStyle" value=""/>
        <c:if test="${customFolder.unReadMails > 0}">
            <c:set var="boldStyle" value="font-weight:bold;"/>
        </c:if>
        <tr>
            <td class="contain" id="${tdClassId}" width="10%"><img height="18" width="23" border="0"
                                                                   src="${imgFolderPath}" alt=""></td>
            <td class="contain" id="${tdClassId}" width="90%" style="${boldStyle}">
                <app:link action="${urlFolder}">${customFolder.folderName}</app:link>
                <c:if test="${customFolder.unReadMails > 0}">
                    &nbsp;&nbsp;(${customFolder.unReadMails})
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
