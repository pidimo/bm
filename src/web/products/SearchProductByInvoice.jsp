<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="windowTitle" value="Product.Title.SimpleSearch" scope="request"/>
<c:set var="pagetitle" value="Product.plural" scope="request"/>
<c:set var="body" value="/common/products/SearchProductByInvoice.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
