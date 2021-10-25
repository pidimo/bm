<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.article.advancedList" scope="request"/>

<c:remove var="index" scope="session"/>
<c:set var="windowTitle" value="Article.Title.advancedSearch" scope="request"/>
<c:set var="pagetitle" value="Article.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/support/ArticleAdvancedSearch.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/ArticleAdvancedSearch.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>