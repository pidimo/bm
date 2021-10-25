<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="selectedFolderId" value="${folderView}" scope="request"/>

<c:if test="${true == hasViewWebmailFolders}">
    <div id="emailfolderReplacementId">
        <c:import url="/common/webmail/WebmailFolderFragment.jsp"/>
    </div>
</c:if>
<c:if test="${true == hasViewWebmailList}">
    <div id="emailListIdReplacementId">
        <c:import url="/webmail/Mail/ReadList.do?folderId=${selectedFolderId}"/>
    </div>
</c:if>


<c:if test="${null != form.dtoMap['popErrorMessages']}">

    <c:set var="pop_service" value="<%=WebMailConstants.EmailAccountErrorType.POP_SERVICE.getConstantAsString()%>"/>
    <c:set var="pop_authentication"
           value="<%=WebMailConstants.EmailAccountErrorType.POP_AUTHENTICATION.getConstantAsString()%>"/>
    <c:set var="pop_provider" value="<%=WebMailConstants.EmailAccountErrorType.POP_PROVIDER.getConstantAsString()%>"/>

    <div id="errorNotificationReplacementId">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td class="notifierTitleClass">
                    <div class="notifierTitle">
                        <fmt:message key="Webmail.errorNotifier.title"/>
                    </div>
                <span id="closeErrorNotificationId">
                    <img alt="" src="<c:out value="${sessionScope.baselayout}"/>/img/notifierclose.gif" border="0"/>
             </span>
                </td>
            </tr>
        </table>

        <div class="modalbody modalbodyScroll">
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td>
                        <ul>
                            <c:forEach var="popErrorMessage" items="${form.dtoMap['popErrorMessages']}">
                                <li>
                                    <c:choose>
                                        <c:when test="${pop_authentication == popErrorMessage.errorType}">
                                            <fmt:message key="Webmail.popNotifier.authenticationError">
                                                <fmt:param value="${popErrorMessage.accountEmail}"/>
                                            </fmt:message>
                                        </c:when>
                                        <c:when test="${pop_provider == popErrorMessage.errorType}">
                                            <fmt:message key="Webmail.popNotifier.providerError">
                                                <fmt:param value="${popErrorMessage.accountEmail}"/>
                                            </fmt:message>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="Webmail.popNotifier.serviceError">
                                                <fmt:param value="${popErrorMessage.accountEmail}"/>
                                            </fmt:message>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:if test="${not empty popErrorMessage.causedBy}">
                                        <br>
                                        <fmt:message key="Webmail.errorNotifier.causedBy">
                                            <fmt:param value="${popErrorMessage.causedBy}"/>
                                        </fmt:message>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</c:if>

<c:if test="${null != form.dtoMap['smtpErrorMessages']}">
    <c:set var="smtp_authentication" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_AUTHENTICATION.getConstantAsString()%>"/>
    <c:set var="smtp_provider" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_PROVIDER.getConstantAsString()%>"/>
    <c:set var="smtp_service" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_SERVICE.getConstantAsString()%>"/>
    <c:set var="smtp_sendFail" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_SENDFAILED.getConstantAsString()%>"/>

    <div id="errorSmtpNotificationReplacementId">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td class="notifierTitleClass">
                    <div class="notifierTitle">
                        <fmt:message key="Webmail.errorNotifier.title"/>
                    </div>
                <span id="closeErrorSmtpNotificationId">
                    <img alt="" src="<c:out value="${sessionScope.baselayout}"/>/img/notifierclose.gif" border="0"/>
             </span>
                </td>
            </tr>
        </table>

        <div class="modalbody modalbodyScroll">
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td>
                        <ul>
                            <c:forEach var="smtpErrorMessage" items="${form.dtoMap['smtpErrorMessages']}">
                                <li>
                                    <c:choose>
                                        <c:when test="${smtp_authentication == smtpErrorMessage.errorType}">
                                            <fmt:message key="Webmail.smtpNotifier.authenticationError">
                                                <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                                            </fmt:message>
                                        </c:when>
                                        <c:when test="${smtp_sendFail == smtpErrorMessage.errorType}">
                                            <fmt:message key="Webmail.smtpNotifier.sendFailedError">
                                                <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                                            </fmt:message>

                                        </c:when>
                                        <c:when test="${smtp_provider == smtpErrorMessage.errorType}">
                                            <fmt:message key="Webmail.smtpNotifier.providerError">
                                                <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                                            </fmt:message>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="Webmail.smtpNotifier.serviceError">
                                                <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                                            </fmt:message>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:if test="${not empty smtpErrorMessage.causedBy}">
                                        <br>
                                        <fmt:message key="Webmail.errorNotifier.causedBy">
                                            <fmt:param value="${smtpErrorMessage.causedBy}"/>
                                        </fmt:message>
                                    </c:if>

                                    <c:if test="${smtp_sendFail == smtpErrorMessage.errorType and not empty smtpErrorMessage.errorDetailList}">
                                        <ul type="square" style="padding:3px; padding-left:20px;">
                                            <c:forEach var="errorDetail" items="${smtpErrorMessage.errorDetailList}">
                                                <li>
                                                    <c:out value="${errorDetail.subject}"/>
                                                    <c:if test="${not empty errorDetail.emails}">
                                                        <ul type="circle" style="padding:3px; padding-left:8px;">
                                                            <li><c:out value="${errorDetail.emails}"/></li>
                                                        </ul>
                                                    </c:if>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</c:if>

<c:if test="${null != form.dtoMap['newEmails']}">
    <div id="newEmailNotificationReplacementId">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td class="notifierTitleClass">
                    <div class="notifierTitle">
                        <fmt:message key="Webmail.popNotifier.title"/>
                    </div>
                <span id="closePopNotificationId">
                    <img alt="" src="<c:out value="${sessionScope.baselayout}"/>/img/notifierclose.gif" border="0"/>
                </span>
                </td>
            </tr>
        </table>
        <div class="modalbody">
            <div style="overflow:auto; width:288px; height:119px; padding:0px 0px 0px 0px;">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td style="padding: 1px 1px 1px 4px;">
                            <fmt:message key="Webmail.newEmail.notification"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <ul>
                                <c:forEach var="folderMessage" items="${form.dtoMap['newEmails']}">
                                    <li>
                                        <app:link action="/Mail/MailTray.do?folderId=${folderMessage.folderId}&index=0">
                                            <c:out value="${folderMessage.folderName}"/>
                                        </app:link>
                                        (${folderMessage.size})
                                    </li>
                                </c:forEach>
                            </ul>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div id="emailCounterId">${form.dtoMap['emailCounter']}</div>
</c:if>
