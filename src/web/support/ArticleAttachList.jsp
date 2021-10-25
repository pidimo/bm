<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.article.attach.list" scope="request"/>

<c:set var="pagetitle" value="SupportAttach.plural" scope="request"/>
<c:set var="windowTitle" value="SupportAttach.Title.search" scope="request"/>
<c:set var="action" value="/ArticleAttach/Forward/Create.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/ArticleAttachList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/support/ArticleAttachList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>