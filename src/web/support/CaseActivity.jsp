<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.supportCase.activity.edit" scope="request"/>

<fmt:message var="title" key="SupportCaseActivity.title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/CaseActivity/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="SupportCaseActivity.title.update" scope="request"/>
<c:set var="funcionality" value="CASEACTIVITY" scope="request"/>
<c:set var="pagetitle" value="Case.plural" scope="request"/>


<c:set var="isExternal" value="${sessionScope.user.valueMap.userType == 0}" scope="request"/>
<c:set var="isAssignedToThisUser"
       value="${caseActivityForm.dtoMap.toUserId == sessionScope.user.valueMap.userId
        && (caseActivityForm.dtoMap.closeDate==null || caseActivityForm.dtoMap.closeDate=='')}" scope="request"/>
<c:set var="externalOnlyView" value="${isExternal && !isAssignedToThisUser}" scope="request"/>
<c:set var="onlyView" value="${op == 'delete' || externalOnlyView || (!isAssignedToThisUser && caseActivityForm.dtoMap.redirectValidation != 'true')}" scope="request"/>
<c:set target="${caseActivityForm.dtoMap}" property="inOut" value="0"/>
<c:choose>
<c:when test="${!onlyView && caseActivityForm.dtoMap.bodyType == '1' && caseActivityForm.dtoMap.type != ''}">
    <tags:initTinyMCE4 textAreaId="description_text"/>
</c:when>
<c:when test="${!onlyView}" >
    <tags:initTinyMCE4 textAreaId="description_text"/>
</c:when>
</c:choose>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/support/CaseActivity.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/support/CaseActivity.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>