<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:set var="windowTitle" value="Campaign.activity.contactImportList" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/campaign/ContactListPopup.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/campaign/ContactListPopup.jsp" scope="request"/>
        <c:import url="/layout/ui/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>
