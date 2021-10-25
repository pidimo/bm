<%@ include file="/Includes.jsp" %>

<fmt:message   var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<fanta:label var="startTimeLabel"
             listName="dedupliContactList" module="/contacts"
             patron="0" columnOrder="startTime">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="dedupliContactId" value="${param.dedupliContactId}"/>
</fanta:label>

<c:set var="tabHeaderLabel" value="DedupliContact.tab.headerLabel" scope="request"/>
<c:set var="tabHeaderValue" value="${app2:getDateWithTimeZone(startTimeLabel, timeZone, dateTimePattern)}" scope="request"/>

<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
    <c:set target="${tabItems}" property="DedupliContact.tab.duplicates" value="/DedupliContact/Duplicate/List.do"/>
</app2:checkAccessRight>


<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(dedupliContactId)" value="${param.dedupliContactId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>
