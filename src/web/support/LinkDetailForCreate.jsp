<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.article.link.create" scope="request"/>

<fmt:message var="title" key="Link.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Article/Link/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="actionHistory" value="3" scope="request"/>
<c:set var="pagetitle" value="Link.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/Link.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/Link.jsp" scope="request"/>
        <c:set var="tabs" value="/ArticleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

