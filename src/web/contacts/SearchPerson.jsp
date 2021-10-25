<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:set var="windowTitle" value="ContactPerson.add" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>
<c:set var="body" value="/common/contacts/SearchPerson.jsp"  scope="request"/>
<c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
