<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoice.delete" scope="request"/>

<fmt:message var="title" key="Invoice.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/InvoiceByContract/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="windowTitle" value="Invoice.Title.delete" scope="request"/>

<%--use in ProductContractTabs.jsp to define detail tab url--%>
<c:set var="detailURL"
       value="/ProductContractByContractToInvoice/Forward/Update.do?dto(addressName)=${app2:encode(param['dto(addressName)'])}&dto(productName)=${app2:encode(param['dto(productName)'])}"
       scope="request"/>

<c:choose>
       <c:when test="${sessionScope.isBootstrapUI}">
              <c:set var="body" value="/WEB-INF/jsp/finance/Invoice.jsp" scope="request"/>
              <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductContractTabs.jsp" scope="request"/>
              <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
       </c:when>
       <c:otherwise>
              <c:set var="tabs" value="/ProductContractTabs.jsp" scope="request"/>
              <c:set var="body" value="/common/finance/Invoice.jsp" scope="request"/>
              <c:import url="${sessionScope.layout}/main.jsp"/>
       </c:otherwise>
</c:choose>
