<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.invoiceToRemind.generate" scope="request"/>

<c:set var="pagetitle" value="Invoice.plural" scope="request"/>
<c:set var="windowTitle" value="Reminder.bulkCreation.title" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/InvoiceToRemind.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/InvoiceToRemind.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

<%--ariel delete--%>

<%--<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>--%>
<%--<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>--%>

<%--<c:set var="pagetitle" value="Invoice.plural" scope="request"/>--%>
<%--<c:set var="windowTitle" value="Reminder.bulkCreation.title" scope="request"/>--%>
<%--<c:set var="body" value="/common/finance/InvoiceToRemind.jsp" scope="request"/>--%>
<%--<c:import url="${sessionScope.layout}/main.jsp"/>--%>