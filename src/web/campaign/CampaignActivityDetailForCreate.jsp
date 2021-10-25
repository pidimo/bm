<%@ page import="net.java.dev.strutsejb.web.DefaultForm" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.campaign.activity.create" scope="request"/>

<c:set var="acualUserId" value="${sessionScope.user.valueMap['userId']}" scope="request"/>

<%
    DefaultForm f = (DefaultForm) request.getAttribute("campaignActivityForm");
    f.setDto("userId", request.getAttribute("acualUserId"));
    /*Integer campaignId = new Integer(request.getParameter("campaignId"));
    CampaignReadCmd cmd = new CampaignReadCmd();
    cmd.setOp("checkIsInternalUser");
    cmd.putParam("campaignId", campaignId);
    ResultDTO rDto = BusinessDelegate.i.execute(cmd, request);

    Boolean isInternalUser = (Boolean) rDto.get("isInternalUser");
    if (isInternalUser.booleanValue()) {
        f.setDto("userId", rDto.get("userId"));
    } else {
        f.setDto("userId", request.getAttribute("acualUserId"));
    }*/
    f.setDto("percent", "0");
%>
<fmt:message var="title" key="CampaignActivity.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/CampaignAvtivity/Create.do" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="CampaignActivity.Title.create" scope="request"/>
<c:set var="pagetitle" value="Campaign.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/CampaignActivity.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/campaign/CampaignActivity.jsp" scope="request"/>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>