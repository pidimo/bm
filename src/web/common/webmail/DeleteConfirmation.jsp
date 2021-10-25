<%@ page import="javax.servlet.jsp.jstl.core.Config"%>
<%@ include file="/Includes.jsp" %>
<%
    Object mailFilter = request.getParameter("mailFilter");
    request.setAttribute("mailFilter", (mailFilter != null ? mailFilter.toString() : "0"));

    //String locale = request.getParameter("locale");
    Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
    Locale defaultLocale = locale;
    
%>
<c:url var="urlCancel" value="/webmail/Mail/MailTray.do">
    <c:param name="folderId" value="${folderView}"/>
    <c:param name="index" value="${0}"/>
    <c:param name="mailFilter" value="${mailFilter}"/>
    <c:param name="returning" value="true"/>
</c:url>
<c:set var="mail_subject_size" value="40"/>
<c:set var="mail_from_size" value="50"/>
<c:set var="mail_to_size" value="50"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<html:form action="${action}?folderId=${folderView}&mailFilter=${mailFilter}&returning=${true}">
     <c:choose>
        <c:when test="${mailTrayForm.dto.toEmptyFolder==null}">
            <table cellSpacing=0 cellPadding=4 width="90%" border=0 align="center">
        </c:when>
        <c:otherwise>
            <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        </c:otherwise>
    </c:choose>
        <TR>
            <TD class="button">
               <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL" styleClass="button" tabindex="3"><fmt:message   key="Common.delete"/></app2:securitySubmit>&nbsp;
               <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" >
                   <fmt:message   key="Common.cancel"/>
               </html:button>
            </TD>
        </TR>
   </table>
    <c:choose>
        <c:when test="${mailTrayForm.dto.toEmptyFolder==null}">
            <html:hidden property="dto(op)" value="deleteMails"/>
           
            <table id="DeleteConfirmation.jsp" border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="container">
                <tr>
                        <td colspan="4" class="title">
                            <c:out value="${title}"/>
                        </td>
                </tr>
                <tr>
                    <td class="listHeader" width="22%"><fmt:message key="Webmail.tray.from"/></td>
                    <td class="listHeader" width="30%"><fmt:message key="Webmail.tray.to"/></td>
                    <td class="listHeader" width="30%"><fmt:message key="Webmail.tray.subject"/></td>
                    <td class="listHeader" width="18%"><fmt:message key="Webmail.tray.date"/></td>
                </tr>
                <c:forEach var="mails" items="${mailTrayForm.dto.readMails}">
                    <html:hidden property="dto(mailId)" value="${mails.mailId}" styleId="mailStyleId"/>
                    <tr class="listRow">
                        <td class="listItem">
                            <c:choose><%--for the mailcontacts--%>
                                <c:when test="${mails.isAction=='true'}">
                                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailaction.gif" title="<fmt:message key="Webmail.tray.isAction"/>"/>
                                </c:when>
                                <c:when test="${mails.isSupportActivity=='true'}">
                                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailactivity.gif" title="<fmt:message key="Webmail.tray.isActivity"/>"/>
                                </c:when>
                                <c:when test="${mails.haveMailContacts=='true'}">
                                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailcomm.gif" title="<fmt:message key="Webmail.tray.isCommunication"/>"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailcommbroke.gif" title="<fmt:message key="Webmail.tray.isntCommunication"/>"/>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${fn:length(mails.mailFrom)>mail_from_size}">
                                    ${fn:substring(mails.mailFrom,0,mail_from_size-3)}...
                                </c:when>
                                <c:when test="${fn:length(mails.mailFrom)==0}">
                                    <fmt:message key="Webmail.mailTray.none"/>
                                </c:when>
                                <c:otherwise>
                                    ${mails.mailFrom}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="listItem">
                            <c:choose>
                                <c:when test="${fn:length(mails.mailTos)>mail_to_size}">
                                    ${fn:substring(mails.mailTos,0,mail_to_size-3)}...
                                </c:when>
                                <c:when test="${fn:length(mails.mailTos)==0}">
                                    <fmt:message key="Webmail.mailTray.none"/>
                                </c:when>
                                <c:otherwise>
                                    ${mails.mailTos}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="listItem">
                            <table border="0">
                                <tr>
                                    <td width="14">
                                        <c:choose>
                                            <c:when test="${mails.hasAttachs=='true'}">
                                                <img align="center" src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/paperclip.gif" title="<fmt:message key="Webmail.tray.attach"/>"/>
                                            </c:when>
                                            <c:otherwise>&nbsp;</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:length(mails.subject)>mail_subject_size}">
                                                ${fn:substring(mails.subject,0,mail_subject_size-3)}...
                                            </c:when>
                                            <c:when test="${fn:length(mails.subject)==0}">
                                                <fmt:message key="Webmail.mailTray.none"/>
                                            </c:when>
                                            <c:otherwise>
                                                ${mails.subject}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td class="listItem2">
                            ${app2:getDateWithTimeZone(mails.mailDate, timeZone, dateTimePattern)}
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <html:hidden property="dto(op)" value="emptyFolder"/>
            <html:hidden property="dto(toEmptyFolder)" value="${param['toEmptyFolder']}"/>
            <c:set var="numOfMails" value="${mailTrayForm.dto.folderMails}" scope="request"/>
            <c:set var="numOfMailContacts" value="${mailTrayForm.dto.folderMailContacts}" scope="request"/>
            <c:set var="numOfMailActions" value="${mailTrayForm.dto.folderMailActions}" scope="request"/>
            <c:set var="numOfMailActivities" value="${mailTrayForm.dto.folderMailActivities}" scope="request"/>

            <table id="DeleteConfirmation.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
                <tr>
                    <td colspan="4" class="title">
                        <c:out value="${title}"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="contain" >
                        <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMails",request.getAttribute("numOfMails"))%><br>
                        <c:if test="${mailTrayForm.dto.folderMailContacts>0}">
                            <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMailContacts",request.getAttribute("numOfMailContacts"))%><br>
                        </c:if>
                        <c:if test="${mailTrayForm.dto.folderMailActions>0}">
                            <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMailActions",request.getAttribute("numOfMailActions"))%><br>
                        </c:if>
                        <c:if test="${mailTrayForm.dto.folderMailActivities>0}">
                            <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMailActivities",request.getAttribute("numOfMailActivities"))%><br>
                        </c:if>
                    </td>
                </tr>
            </table>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${mailTrayForm.dto.toEmptyFolder==null}">
            <table cellSpacing=0 cellPadding=4 width="90%" border=0 align="center">
        </c:when>
        <c:otherwise>
            <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        </c:otherwise>
    </c:choose>
        <TR>
            <TD class="button">
               <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL" styleClass="button" tabindex="3"><fmt:message   key="Common.delete"/></app2:securitySubmit>&nbsp;
               <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" >
                   <fmt:message   key="Common.cancel"/>
               </html:button>
            </TD>
        </TR>
   </table>
</html:form>