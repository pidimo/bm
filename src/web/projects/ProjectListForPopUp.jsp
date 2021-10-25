<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="windowTitle" value="Project.Title.search" scope="request"/>
<c:set var="pageTitle" value="Project.Title.search" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/projects/ProjectListForPopUp.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/projects/ProjectListForPopUp.jsp" scope="request"/>
        <c:import url="/layout/ui/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>