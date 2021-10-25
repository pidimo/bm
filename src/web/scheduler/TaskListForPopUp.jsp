<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="windowTitle" value="Task.Title.SimpleSearch" scope="request"/>

<c:set var="isPopUp" value="${true}" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/scheduler/TaskListPopUp.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/scheduler/TaskListPopUp.jsp" scope="request"/>
        <c:import url="/layout/ui/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>

