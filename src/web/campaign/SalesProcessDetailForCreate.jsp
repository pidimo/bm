<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.campaign.activity.contact.salesProcess.create" scope="request"/>

<fmt:message var="title" key="SalesProcess.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="isSalesProcess" value="${false}" scope="request"/>
<c:set var="subCategoriesAjaxAction" value="/campaign/SalesProcess/Category/ReadChildCategory.do" scope="request"/>
<c:set var="isCampaign" value="${true}" scope="request"/>
<c:set var="windowTitle" value="SalesProcess.Title.new" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/SalesProcess.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/SalesProcess.jsp" scope="request"/>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
>