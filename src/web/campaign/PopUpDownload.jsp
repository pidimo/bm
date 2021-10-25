<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="pagetitle" value="Campaign.plural" scope="request"/>

<fmt:message  var="title" key="Campaign.resultList" scope="request"/>
<c:set var="body" value="/common/campaign/PopUpDownload.jsp" scope="request"/>

<c:url value="Campaign/Document/Generate.do" var="newUrl">
    <c:param name="dto(languageName)" value="${param['languageName']}"/>
    <c:param name="dto(language)" value="${param['language']}"/>
    <c:param name="dto(page)" value="${param['page']}"/>
    <c:param name="dto(templateId)" value="${param['templateId']}"/>
    <c:param name="activityId" value="${param['activityId']}"/>
    <c:param name="dto(responsibleType)" value="${param['responsibleType']}"/>
</c:url>

<c:set var="jsLoad" scope="request">
onLoad="window.location='<app:url value="${newUrl}" />'"
</c:set>

<c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
