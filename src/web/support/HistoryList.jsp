<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.article.history.list" scope="request"/>

<c:set var="pagetitle" value="Article.Tab.links" scope="request"/>
<c:set var="windowTitle" value="History.Title.search" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/HistoryList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/HistoryList.jsp" scope="request"/>
        <c:set var="tabs" value="/ArticleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>