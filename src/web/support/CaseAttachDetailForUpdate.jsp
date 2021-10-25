<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.supportCase.attach.edit" scope="request"/>

<fmt:message var="title" key="SupportAttach.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/CaseAttach/Update.do" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="SupportAttach.Title.update" scope="request"/>
<c:set var="pagetitle" value="SupportAttach.plural" scope="request"/>
<c:set var="attachDownload" value="/CaseAttach/Download.do?index=3" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/SupportAttach.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/SupportAttach.jsp" scope="request"/>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>