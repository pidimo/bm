<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.supportCase.create" scope="request"/>

<fmt:message var="title" key="SupportCase.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Case/Create.do?index=${param.index}" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="funcionality" value="CASE" scope="request"/>
<c:set var="windowTitle" value="SupportCase.create" scope="request"/>
<c:set var="pagetitle" value="SupportCase.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/support/Case.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/Case.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>