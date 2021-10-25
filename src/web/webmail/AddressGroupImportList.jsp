<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="windowTitle" value="Webmail.mailContact.plural" scope="request"/>
<c:set var="pagetitle" value="Webmail.mailContact.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/webmail/AddressGroupImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/webmail/AddressGroupImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>