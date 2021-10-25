<%@ include file="/Includes.jsp"%>

<c:set var="op" value="create" scope="request"/>

<%--enable in bootstrap mode--%>
<c:set var="body" value="/WEB-INF/jsp/admin/DemoAccountFront.jsp" scope="request"/>
<c:set var="urlBody" value="/WEB-INF/jsp/layout/frontui/main.jsp"/>


<c:import url="${urlBody}"/>

