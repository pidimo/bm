<%@ include file="/Includes.jsp" %>


<fmt:message var="title" key="SalesProcessAction.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/Action/Update.do" scope="request"/>

<c:set var="createSaleLink" value="/contacts/SalesProcess/Sale/Forward/Create.do" scope="request"/>
<c:set var="tabName" value="Contacts.Tab.sale" scope="request"/>

<c:set var="createTaskLink" value="/contacts/Task/Forward/Create.do" scope="request"/>
<c:set var="taskTabName" value="Scheduler.Tasks" scope="request"/>

<c:set var="createActionLink" value="/contacts/SalesProcess/Action/Forward/Create.do" scope="request"/>
<c:set var="actionTabName" value="Contacts.Tab.salesProcess" scope="request"/>

<!--If type==email then the data is not editable, is readonly-->
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

<c:set var="isSalesProcess" value="${false}" scope="request"/>
<c:set var="windowTitle" value="SalesProcessAction.update" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/Action.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/Action.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>