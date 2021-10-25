<%@ include file="/Includes.jsp" %>


<fanta:label var="tabNameLabel"
             listName="invoiceSingleList" module="/finance"
             patron="0" columnOrder="number">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="invoiceId" value="${param.invoiceId}"/>
</fanta:label>
<c:set var="tabHeaderLabel" value="Invoice.headerLabel" scope="request"/>
<c:set var="tabHeaderValue" value="${tabNameLabel}" scope="request"/>
<c:set var="invoiceNumber" value="${tabNameLabel}" scope="request"/>

<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="INVOICE" permission="VIEW">
    <c:set target="${tabItems}" property="Invoice.Tab.Detail" value="/Invoice/Forward/Update.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="INVOICEPOSITION" permission="VIEW">
    <c:set target="${tabItems}" property="Invoice.Tab.Position" value="/InvoicePosition/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
    <c:set target="${tabItems}" property="Invoice.Tab.Reminder" value="/InvoiceReminder/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="INVOICEPAYMENT" permission="VIEW">
    <c:set target="${tabItems}" property="Invoice.Tab.Payment" value="/InvoicePayment/List.do"/>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(invoiceId)" value="${param.invoiceId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>