<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.task.edit" scope="request"/>

<fmt:message var="title" key="Task.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Task/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Task.update" scope="request"/>

<c:set var="createTaskUrl" value="/contacts/Task/Forward/Create.do" scope="request"/>
<c:set var="taskTab" value="Scheduler.Tasks" scope="request"/>

<c:set var="createSalesProcessActionUrl" value="/contacts/SalesProcess/Action/Forward/Create.do" scope="request"/>
<c:set var="salesProcessTab" value="Contacts.Tab.salesProcess" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/Task.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/Task.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
