<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="bodyHTMLConstant" value="<%=WebMailConstants.BODY_TYPE_HTML%>"/>
<c:set var="bodyTEXTConstant" value="<%=WebMailConstants.BODY_TYPE_TEXT%>"/>
<c:set var="useHtmlEditor" value="false" scope="request"/>

<c:set var="enableHtmlEditor" value="false"/>
<c:set var="isConfiguratedHtmlEditor" value="${app2:useHtmlEditor(sessionScope.user.valueMap['userId'], pageContext.request)}"/>

<c:if test="${not empty mainCommunicationForm.dtoMap['bodyType']}">
    <c:choose>
        <c:when test="${bodyHTMLConstant == mainCommunicationForm.dtoMap['bodyType'] && 'create' == op}">
            <c:set var="enableHtmlEditor" value="true"/>
        </c:when>
        <c:when test="${bodyTEXTConstant == mainCommunicationForm.dtoMap['bodyType'] && true == mainCommunicationForm.dtoMap['replyMode']  && 'create' == op}">
            <c:set var="enableHtmlEditor" value="false"/>
        </c:when>
        <c:when test="${bodyTEXTConstant == mainCommunicationForm.dtoMap['bodyType'] && true == isConfiguratedHtmlEditor  && 'create' == op}">
            <c:set var="enableHtmlEditor" value="true"/>
        </c:when>
    </c:choose>
</c:if>
<c:if test="${empty mainCommunicationForm.dtoMap['bodyType']}">
    <c:choose>
        <c:when test="${true == isConfiguratedHtmlEditor && 'create' == op}">
            <c:set var="enableHtmlEditor" value="true"/>
        </c:when>
        <c:when test="${true == isConfiguratedHtmlEditor && 'update' == op && true == isSupportActivity}">
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
        <c:import url="/WEB-INF/jsp/webmail/ComposeEmailCommunication.jsp"/>
        <c:set var="modeShow" value="small" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/webmail/ComposeEmailCommunication.jsp"/>
    </c:otherwise>
</c:choose>

