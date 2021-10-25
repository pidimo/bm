<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:set var="windowTitle" value="Campaign.activity.user.searchUser" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/campaign/UserImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/campaign/UserImportList.jsp" scope="request"/>
        <c:import url="/layout/ui/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>

