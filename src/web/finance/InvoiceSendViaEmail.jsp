<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoiceSendViaEmail.send" scope="request"/>

<fmt:message var="title" key="Invoice.sendViaEmail.title" scope="request"/>
<c:set var="windowTitle" value="Invoice.sendViaEmail.title" scope="request"/>

<c:set var="body" value="/WEB-INF/jsp/finance/InvoiceSendViaEmail.jsp" scope="request"/>
<c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>

