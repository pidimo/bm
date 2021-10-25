<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="body" value="/common/PlannedUserPasswordChange.jsp" scope="request"/>
<c:set var="urlBody" value="/layout/frontui/main.jsp"/>
<c:import url="${urlBody}"/>
