<%@ include file="/Includes.jsp"%>
<c:set var="helpResourceKey" value="help.passwordChange.create" scope="request"/>

<fmt:message  var="title" key="PasswordChange.title.create" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/PasswordChange/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="windowTitle" value="PasswordChange.title.create" scope="request"/>
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