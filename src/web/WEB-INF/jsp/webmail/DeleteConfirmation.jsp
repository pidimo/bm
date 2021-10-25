<%@ page import="javax.servlet.jsp.jstl.core.Config" %>
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
<html:form action="${action}?folderId=${folderView}&mailFilter=${mailFilter}&returning=${true}" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="3"><fmt:message key="Common.delete"/></app2:securitySubmit>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}"
                         onclick="location.href='${urlCancel}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <c:choose>
                <c:when test="${mailTrayForm.dto.toEmptyFolder==null}">
                    <%--<table cellSpacing=0 cellPadding=4 width="90%" border=0 align="center" class="${app2:getFantabulousTableClases()}">--%>
                </c:when>
                <c:otherwise>
                    <%--<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">--%>
                </c:otherwise>
            </c:choose>
                <%--</table>--%>
            <c:choose>
                <%--<c:when test="true">--%>
                <c:when test="${mailTrayForm.dto.toEmptyFolder==null}">
                    <html:hidden property="dto(op)" value="deleteMails"/>
                    <legend class="title">
                        <c:out value="${title}"/>
                    </legend>
                    <div class="table-responsive">
                        <table id="DeleteConfirmation.jsp" width="90%" class="${app2:getFantabulousTableClases()}">
                            <tr>
                                <th class="listHeader" width="22%"><fmt:message key="Webmail.tray.from"/></th>
                                <th class="listHeader" width="30%"><fmt:message key="Webmail.tray.to"/></th>
                                <th class="listHeader" width="30%"><fmt:message key="Webmail.tray.subject"/></th>
                                <th class="listHeader" width="18%"><fmt:message key="Webmail.tray.date"/></th>
                            </tr>
                            <c:forEach var="mails" items="${mailTrayForm.dto.readMails}">
                                <html:hidden property="dto(mailId)" value="${mails.mailId}" styleId="mailStyleId"/>
                                <tr>
                                    <td>
                                        <c:choose><%--for the mailcontacts--%>
                                            <c:when test="${mails.isAction=='true'}">
                                                <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailaction.gif"
                                                     title="<fmt:message key="Webmail.tray.isAction"/>"/>
                                            </c:when>
                                            <c:when test="${mails.isSupportActivity=='true'}">
                                                <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailactivity.gif"
                                                     title="<fmt:message key="Webmail.tray.isActivity"/>"/>
                                            </c:when>
                                            <c:when test="${mails.haveMailContacts=='true'}">
                                                <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailcomm.gif"
                                                     title="<fmt:message key="Webmail.tray.isCommunication"/>"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/emailcommbroke.gif"
                                                     title="<fmt:message key="Webmail.tray.isntCommunication"/>"/>
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
                                    <td>
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
                                    <td>
                                        <table>
                                            <tr>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${mails.hasAttachs=='true'}">
                                                            <img align="center"
                                                                 src="<c:out value="${sessionScope.baselayout}"/>/img/webmail/paperclip.gif"
                                                                 title="<fmt:message key="Webmail.tray.attach"/>"/>
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
                                    <td>
                                            ${app2:getDateWithTimeZone(mails.mailDate, timeZone, dateTimePattern)}
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <html:hidden property="dto(op)" value="emptyFolder"/>
                    <html:hidden property="dto(toEmptyFolder)" value="${param['toEmptyFolder']}"/>
                    <c:set var="numOfMails" value="${mailTrayForm.dto.folderMails}" scope="request"/>
                    <c:set var="numOfMailContacts" value="${mailTrayForm.dto.folderMailContacts}" scope="request"/>
                    <c:set var="numOfMailActions" value="${mailTrayForm.dto.folderMailActions}" scope="request"/>
                    <c:set var="numOfMailActivities" value="${mailTrayForm.dto.folderMailActivities}" scope="request"/>
                    <legend class="title">
                        <c:out value="${title}"/>
                    </legend>
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="${app2:getFormContainClasses(null)}">
                            <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMails", request.getAttribute("numOfMails"))%>
                            <br>
                            <c:if test="${mailTrayForm.dto.folderMailContacts>0}">
                                <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMailContacts", request.getAttribute("numOfMailContacts"))%>
                                <br>
                            </c:if>
                            <c:if test="${mailTrayForm.dto.folderMailActions>0}">
                                <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMailActions", request.getAttribute("numOfMailActions"))%>
                                <br>
                            </c:if>
                            <c:if test="${mailTrayForm.dto.folderMailActivities>0}">
                                <%= JSPHelper.getMessage(defaultLocale, "Webmail.deleteFolderMailActivities", request.getAttribute("numOfMailActivities"))%>
                                <br>
                            </c:if>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${mailTrayForm.dto.toEmptyFolder==null}">
                    <%--<table cellSpacing=0 cellPadding=4 width="90%" border=0 align="center">--%>
                </c:when>
                <c:otherwise>
                    <%--<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">--%>
                </c:otherwise>
            </c:choose>

                <%--</table>--%>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="3"><fmt:message key="Common.delete"/></app2:securitySubmit>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}"
                         onclick="location.href='${urlCancel}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>