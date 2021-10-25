<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.supportCase.edit" scope="request"/>

<fmt:message    var="title" key="SupportCase.title.edit" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Case/Update?index=${param.index}" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="SupportCase.title.edit" scope="request"/>
<c:set var="funcionality" value="CASE" scope="request"/>
<c:set var="pagetitle" value="Case.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/Case.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/support/Case.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>