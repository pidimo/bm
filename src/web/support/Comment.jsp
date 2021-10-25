<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.article.comment" scope="request"/>

<fmt:message var="title" key="Article.Tab.Comments" scope="request"/>
<fmt:message var="button" key="Comment.send" scope="request"/>

<c:set var="windowTitle" value="Article.Tab.Comments" scope="request"/>
<c:set var="createAction" value="/Article/Comment/Create" scope="request"/>
<c:set var="updateAction" value="/Article/Comment/Forward/Update" scope="request"/>
<c:set var="deleteAction" value="/Article/Comment/Forward/Delete" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="actionHistory" value="12" scope="request"/>
<c:set var="pagetitle" value="Article.Tab.Comments" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/Comment.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/Comment.jsp" scope="request"/>
        <c:set var="tabs" value="/ArticleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

