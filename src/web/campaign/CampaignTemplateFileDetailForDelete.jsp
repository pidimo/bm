<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<%
    request.setAttribute("templateId", request.getParameter("templateId"));
    request.setAttribute("documentType", request.getParameter("documentType"));
%>
<c:set var="helpResourceKey" value="help.campaign.template.file.delete" scope="request"/>

<c:set var="template_html" scope="request">
    <%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>
</c:set>
<c:set var="template_word" scope="request">
    <%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>
</c:set>

<fmt:message var="button" key="Common.delete" scope="request"/>
<fmt:message var="title" key="TemplateFile.Title.delete" scope="request"/>

<c:set var="action" value="/Template/File/Delete.do" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="TemplateFile.Title.delete" scope="request"/>
<c:set var="pagetitle" value="Campaign.Template.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/CampaignTemplateFile.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/CampaignTemplateFile.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>