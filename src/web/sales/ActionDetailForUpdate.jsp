<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salesProcess.action.edit" scope="request"/>


<fmt:message  var="title" key="SalesProcessAction.update" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/Action/Update" scope="request"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:choose>
    <c:when test="${actionForm.dtoMap['type']==email}">
        <c:set var="op" value="delete" scope="request"/>
        <c:set var="op1" value="update" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="op" value="update" scope="request"/>
    </c:otherwise>
</c:choose>

<c:set var="isSalesProcess" value="${true}" scope="request" />
<c:set var="windowTitle" value="SalesProcessAction.update" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/Action.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/Action.jsp"  scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>