<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.supportCase.atach.list" scope="request"/>

<c:set var="pagetitle" value="SupportAttach.plural" scope="request"/>
<c:set var="windowTitle" value="SupportAttach.Title.search" scope="request"/>

<c:set var="action" value="/CaseAttach/List.do" scope="request"/>
<c:set var="attachEdit" value="CaseAttach/Forward/Update.do" scope="request"/>
<c:set var="attachDelete" value="CaseAttach/Forward/Delete.do" scope="request"/>
<c:set var="attachCreate" value="CaseAttach/Forward/Create.do" scope="request"/>
<c:set var="attachDownload" value="CaseAttach/Download.do" scope="request"/>
<c:set var="isSupportCase" value="${true}" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/SupportAttachList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/SupportAttachList.jsp" scope="request"/>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>