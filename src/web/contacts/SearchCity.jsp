<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="windowTitle" value="City.Title.search" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/SearchCity.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/SearchCity.jsp" scope="request"/>
        <c:import url="/layout/ui/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>