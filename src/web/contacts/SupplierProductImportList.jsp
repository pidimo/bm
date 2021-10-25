<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:set var="pagetitle" value="Contact.plural" scope="request"/>
<c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
<c:set var="windowTitle" value="Product.Title.SimpleSearch" scope="request"/>
<c:set var="body" value="/common/contacts/SupplierProductImportList.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>