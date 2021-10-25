<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.article.attach.delete" scope="request"/>

<fmt:message var="title" key="SupportAttach.Title.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/ArticleAttach/Delete.do" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="actionValue" value="8" scope="request"/>
<c:set var="windowTitle" value="SupportAttach.Title.delete" scope="request"/>
<c:set var="pagetitle" value="SupportAttach.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ArticleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/SupportAttach.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/SupportAttach.jsp" scope="request"/>
        <c:set var="tabs" value="/ArticleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
