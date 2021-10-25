<%@include file="/Includes.jsp"%>
<c:set var="helpResourceKey" value="help.passwordChange.delete" scope="request"/>

<fmt:message var="title" key="PasswordChange.title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/PasswordChange/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="windowTitle" value="PasswordChange.title.delete" scope="request"/>
<c:set var="pagetitle" value="PasswordChange.title.list" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/admin/PasswordChange.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/PasswordChange.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
