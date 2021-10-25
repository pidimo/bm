<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<fmt:message    var="title" key="Campaign.resultList" scope="request"/>
<c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
<c:set var="pagetitle" value="Campaign.plural" scope="request"/>
<c:set var="windowTitle" value="Campaign.resultList" scope="request"/>
<c:set var="body" value="/common/campaign/ResultList.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>