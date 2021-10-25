<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.supportCase.activity.list" scope="request"/>

<c:set var="pagetitle" value="SupportCaseActivity.title.plural" scope="request"/>
<c:set var="windowTitle" value="SupportCaseActivity.title.search" scope="request"/>
<c:set var="action" value="CaseActivity/List.do" scope="request"/>
<c:set var="edit" value="CaseActivity/Forward/Update.do" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/CaseActivityList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/CaseActivityList.jsp" scope="request"/>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

