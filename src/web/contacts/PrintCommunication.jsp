<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/contacts/PrintCommunication.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/contacts/PrintCommunication.jsp"/>
    </c:otherwise>
</c:choose>
