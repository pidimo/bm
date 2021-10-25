<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.article.delete" scope="request"/>

<fmt:message    var="title" key="Article.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/Article/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="Article.delete" scope="request"/>
<c:set var="pagetitle" value="Article.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/Article.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>

    <c:otherwise>
        <c:set var="tabs" value="/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/support/Article.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>