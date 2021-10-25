<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/campaign/CampaignTemplatePreview.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/campaign/CampaignTemplatePreview.jsp"/>
    </c:otherwise>
</c:choose>