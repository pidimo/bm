<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="SupportCase.title" scope="request"/>
<c:set var="caseId"><%=request.getParameter("caseId")%></c:set>
<c:set var="tabHeaderValue" scope="request">

    <fanta:label listName="lightCaseList" module="/support" patron="0" columnOrder="title" >
        <fanta:parameter field="caseId" value="${caseId}"/>
    </fanta:label>
</c:set>
<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>

<app2:checkAccessRight functionality="CASE" permission="VIEW">
    <c:set target="${tabItems}" property="Common.detail" value="/Case/Forward/Update.do?dto(caseTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="CASEACTIVITY" permission="VIEW">
    <c:set target="${tabItems}" property="SupportCaseActivity.myTitle.plural" value="/CaseActivity/List.do?n=1&dto(caseTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>

    <c:if test="${sessionScope.user.valueMap.userType != 0}">
<app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
    <c:set target="${tabItems}" property="Contacts.Tab.communications" value="/Communication/List.do?dto(caseTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>
    </c:if>

<app2:checkAccessRight functionality="SUPPORTATTACH" permission="VIEW">
<c:set target="${tabItems}" property="Article.Tab.Attachments" value="/CaseAttach/List.do?dto(caseTitle)=${app2:encode(tabHeaderValue)}"/>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>

<c:import url="${sessionScope.layout}/submenu.jsp"/>



