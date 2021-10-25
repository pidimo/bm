<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


<fmt:message    var="title" key="Template.Title.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/TemplateFile/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="TemplateFile.Title.delete" scope="request"/>
<c:set var="body" value="/common/catalogs/TemplateFileDelete.jsp" scope="request"/>
<c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>