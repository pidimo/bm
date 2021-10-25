<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.contact.customerInfo" scope="request"/>

<fmt:message    var="title" key="Customer.information" scope="request"/>
<fmt:message   var="delete" key="Common.delete" scope="request"/>
<fmt:message   var="save" key="Common.save" scope="request"/>
<fmt:message   var="add" key="CategoryValue.category_plural" scope="request"/>
<c:set var="windowTitle" value="Customer.information" scope="request"/>
<c:set var="action" value="/Customer/Update.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/Customer.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/contacts/Customer.jsp"  scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>


