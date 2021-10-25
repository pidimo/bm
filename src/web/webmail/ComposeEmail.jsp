<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.compose" scope="request"/>

<c:set var="windowTitle" value="Mail.Title.compose" scope="request"/>
<c:set var="pagetitle" value="Common.webmail" scope="request"/>

<c:set var="webTabs" value="/WebmailTabs.jsp" scope="request"/>


<c:set var="bodyHTMLConstant" value="<%=WebMailConstants.BODY_TYPE_HTML%>"/>
<c:set var="bodyTEXTConstant" value="<%=WebMailConstants.BODY_TYPE_TEXT%>"/>
<c:set var="useHtmlEditor" value="false" scope="request"/>

<c:set var="enableHtmlEditor" value="false"/>
<c:set var="isConfiguratedHtmlEditor" value="${app2:useHtmlEditor(sessionScope.user.valueMap['userId'], pageContext.request)}"/>

<c:if test="${not empty emailForm.dtoMap['bodyType']}">
    <c:choose>
        <c:when test="${bodyHTMLConstant == emailForm.dtoMap['bodyType']}">
            <c:set var="enableHtmlEditor" value="true"/>
        </c:when>
        <c:when test="${bodyTEXTConstant == emailForm.dtoMap['bodyType'] && true == emailForm.dtoMap['replyMode']}">
            <c:set var="enableHtmlEditor" value="false"/>
        </c:when>
        <c:when test="${bodyTEXTConstant == emailForm.dtoMap['bodyType'] && true == isConfiguratedHtmlEditor}">
            <c:set var="enableHtmlEditor" value="true"/>
        </c:when>
    </c:choose>
</c:if>
<c:if test="${empty emailForm.dtoMap['bodyType']}">
    <c:choose>
        <c:when test="${true == isConfiguratedHtmlEditor}">
            <c:set var="enableHtmlEditor" value="true"/>
        </c:when>
        <c:otherwise>
            <c:set var="enableHtmlEditor" value="false"/>
        </c:otherwise>
    </c:choose>
</c:if>

<c:if test="${true == enableHtmlEditor}">
    <tags:initTinyMCE4 textAreaId="body_field" addUserEmailStylePlugin="true" addBrowseImagePlugin="true"/>
    <c:set var="useHtmlEditor" value="true" scope="request"/>
    <c:set var="complementOperations" value="true"/>
    <c:set var="html" value="${true}"/>
</c:if>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="mailBody" value="/WEB-INF/jsp/webmail/ComposeEmail.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/WebMail.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="mailBody" value="/common/webmail/ComposeEmail.jsp" scope="request"/>
        <c:set var="body" value="/common/webmail/WebMail.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>