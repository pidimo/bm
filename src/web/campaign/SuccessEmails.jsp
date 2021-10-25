<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="pagetitle" value="Campaign.plural" scope="request"/>

<fmt:message  var="title" key="Campaign.resultList" scope="request"/>
<c:set var="body" value="/common/campaign/SuccessEmails.jsp" scope="request"/>

<c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
