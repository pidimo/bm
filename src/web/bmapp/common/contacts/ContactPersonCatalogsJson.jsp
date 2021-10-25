<%@ include file="/Includes.jsp" %>

{
<c:set var="organizationId" value="${param.contactId}" scope="request"/>
<c:import url="/bmapp/common/contacts/ContactPersonCatalogsJsonFragment.jsp"/>
}
