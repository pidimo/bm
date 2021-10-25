<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.campaign.create" scope="request"/>

<fmt:message var="title" key="Campaign.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Campaign/Create" scope="request"/>
<c:set var="copy" value="/Campaign/Forward/Copy" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Campaign.new" scope="request"/>

<c:set var="pagetitle" value="Campaign.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/campaign/Campaign.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/campaign/Campaign.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
