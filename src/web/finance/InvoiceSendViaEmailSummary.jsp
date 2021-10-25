<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoiceSendViaEmail.send.summary" scope="request"/>

<fmt:message var="title" key="Invoice.sendViaEmail.summary" scope="request"/>
<c:set var="windowTitle" value="Invoice.sendViaEmail.summary" scope="request"/>

<c:set var="body" value="/WEB-INF/jsp/finance/InvoiceSendViaEmailSummary.jsp" scope="request"/>
<c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>

