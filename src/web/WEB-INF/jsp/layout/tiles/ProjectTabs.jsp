<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="Project" scope="request"/>

<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="projectBaseList" module="/projects" patron="0" columnOrder="projectName">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="projectId" value="${param.projectId}"/>
    </fanta:label>
</c:set>


<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="PROJECT" permission="VIEW">
    <c:set target="${tabItems}" property="Project.Tab.detail" value="/Project/Forward/Update.do"/>
</app2:checkAccessRight>

<c:if test="${app2:isUserOfProject(param.projectId, pageContext.request)}">
    <app2:checkAccessRight functionality="PROJECTTIME" permission="VIEW">
        <c:set target="${tabItems}" property="Project.Tab.times" value="/ProjectTime/List.do"/>
    </app2:checkAccessRight>
</c:if>
<c:if test="${app2:hasProjectUserPermission(param.projectId, 'ADMIN', pageContext.request)}">
    <app2:checkAccessRight functionality="PROJECTUSER" permission="VIEW">
        <c:set target="${tabItems}" property="Project.Tab.assignees" value="/ProjectUser/List.do"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PROJECTACTIVITY" permission="VIEW">
        <c:set target="${tabItems}" property="Project.Tab.activities" value="/ProjectActivity/List.do"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PROJECTSUBPROJECT" permission="VIEW">
        <c:set target="${tabItems}" property="Project.Tab.subProjects" value="/SubProject/List.do"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PROJECTTIMELIMIT" permission="VIEW">
        <c:set target="${tabItems}" property="Project.Tab.timeLimit" value="/ProjectTimeLimit/List.do"/>
    </app2:checkAccessRight>
</c:if>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(projectId)" value="${param.projectId}"/>




