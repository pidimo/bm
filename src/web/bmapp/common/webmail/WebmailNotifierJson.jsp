<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="pop_service" value="<%=WebMailConstants.EmailAccountErrorType.POP_SERVICE.getConstantAsString()%>"/>
<c:set var="pop_authentication" value="<%=WebMailConstants.EmailAccountErrorType.POP_AUTHENTICATION.getConstantAsString()%>"/>
<c:set var="pop_provider" value="<%=WebMailConstants.EmailAccountErrorType.POP_PROVIDER.getConstantAsString()%>"/>

<c:set var="smtp_authentication" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_AUTHENTICATION.getConstantAsString()%>"/>
<c:set var="smtp_provider" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_PROVIDER.getConstantAsString()%>"/>
<c:set var="smtp_service" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_SERVICE.getConstantAsString()%>"/>
<c:set var="smtp_sendFail" value="<%=WebMailConstants.EmailAccountErrorType.SMTP_SENDFAILED.getConstantAsString()%>"/>


{
    "popErrorArray" : [
        <c:if test="${null != form.dtoMap['popErrorMessages']}">
            <c:forEach var="popErrorMessage" items="${form.dtoMap['popErrorMessages']}" varStatus="statusVar">
                <c:choose>
                    <c:when test="${pop_authentication == popErrorMessage.errorType}">
                        <fmt:message var="messageVar" key="Webmail.popNotifier.authenticationError">
                            <fmt:param value="${popErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:when>
                    <c:when test="${pop_provider == popErrorMessage.errorType}">
                        <fmt:message var="messageVar" key="Webmail.popNotifier.providerError">
                            <fmt:param value="${popErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:when>
                    <c:otherwise>
                        <fmt:message var="messageVar" key="Webmail.popNotifier.serviceError">
                            <fmt:param value="${popErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:otherwise>
                </c:choose>

                <c:set var="causedByVar" value=""/>
                <c:if test="${not empty popErrorMessage.causedBy}">
                    <fmt:message var="causedByVar" key="Webmail.errorNotifier.causedBy">
                        <fmt:param value="${popErrorMessage.causedBy}"/>
                    </fmt:message>
                </c:if>

                <c:if test="${statusVar.index > 0}">, </c:if>
                {
                    "message" : "${app2:escapeJSON(messageVar)}",
                    "causedBy" : "${app2:escapeJSON(causedByVar)}"
                }
            </c:forEach>
        </c:if>
    ],

    "smtpErrorArray" : [
        <c:if test="${null != form.dtoMap['smtpErrorMessages']}">
            <c:forEach var="smtpErrorMessage" items="${form.dtoMap['smtpErrorMessages']}" varStatus="statusVar">
                <c:choose>
                    <c:when test="${smtp_authentication == smtpErrorMessage.errorType}">
                        <fmt:message var="messageVar" key="Webmail.smtpNotifier.authenticationError">
                            <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:when>
                    <c:when test="${smtp_sendFail == smtpErrorMessage.errorType}">
                        <fmt:message var="messageVar" key="Webmail.smtpNotifier.sendFailedError">
                            <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:when>
                    <c:when test="${smtp_provider == smtpErrorMessage.errorType}">
                        <fmt:message var="messageVar" key="Webmail.smtpNotifier.providerError">
                            <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:when>
                    <c:otherwise>
                        <fmt:message var="messageVar" key="Webmail.smtpNotifier.serviceError">
                            <fmt:param value="${smtpErrorMessage.accountEmail}"/>
                        </fmt:message>
                    </c:otherwise>
                </c:choose>

                <c:set var="causedByVar" value=""/>
                <c:if test="${not empty smtpErrorMessage.causedBy}">
                    <fmt:message var="causedByVar" key="Webmail.errorNotifier.causedBy">
                        <fmt:param value="${smtpErrorMessage.causedBy}"/>
                    </fmt:message>
                </c:if>

                <c:if test="${statusVar.index > 0}">, </c:if>
                {
                    "message" : "${app2:escapeJSON(messageVar)}",
                    "causedBy" : "${app2:escapeJSON(causedByVar)}",
                    "detailArray" : [
                        <c:if test="${smtp_sendFail == smtpErrorMessage.errorType and not empty smtpErrorMessage.errorDetailList}">
                            <c:forEach var="errorDetail" items="${smtpErrorMessage.errorDetailList}" varStatus="statusVar2">
                                <c:if test="${statusVar2.index > 0}">, </c:if>
                                {
                                    "subject" : "${app2:escapeJSON(errorDetail.subject)}",
                                    "emails" : "${app2:escapeJSON(errorDetail.emails)}"
                                }
                            </c:forEach>
                        </c:if>
                    ]
                }
            </c:forEach>
        </c:if>
    ],

    "newEmails" : {
        "emailCounter" : "${ not empty form.dtoMap['emailCounter'] ? form.dtoMap['emailCounter'] : '0'}",
        "emailFolderArray" : [
            <c:if test="${null != form.dtoMap['newEmails']}">
                <c:forEach var="folderMessage" items="${form.dtoMap['newEmails']}" varStatus="statusVar">
                    <c:if test="${statusVar.index > 0}">, </c:if>
                    {
                    "folderId" : "${folderMessage.folderId}",
                    "newEmailsSize" : "${folderMessage.size}",
                    "folderName" : "${app2:escapeJSON(folderMessage.folderName)}"
                    }
                </c:forEach>
            </c:if>
        ]
    }
}
