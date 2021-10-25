<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.campaign.template.edit" scope="request"/>

<fmt:message var="button" key="Common.save" scope="request"/>
<fmt:message var="title" key="Template.Title.update" scope="request"/>
<c:set var="action" value="/Template/Update.do" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="Template.Title.update" scope="request"/>
<c:set var="pagetitle" value="Campaign.Template.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/Template.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/Template.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>