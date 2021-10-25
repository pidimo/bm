<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/webmail/PrintMail.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="/common/webmail/PrintMail.jsp"/>
    </c:otherwise>
</c:choose>