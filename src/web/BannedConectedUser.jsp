<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<fmt:message var="button" key="Common.save" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/BannedConectedUser.jsp" scope="request"/>
        <c:set var="urlBody" value="/WEB-INF/jsp/layout/frontui/main.jsp"/>
        <c:import url="${urlBody}"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/BannedConectedUser.jsp" scope="request"/>
        <c:set var="urlBody" value="/layout/frontui/main.jsp"/>
        <c:import url="${urlBody}"/>
    </c:otherwise>
</c:choose>