<%@ include file="/Includes.jsp" %>

<fanta:label var="tabNameLabel"
             listName="importProfileList" module="/contacts"
             patron="0" columnOrder="label">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="profileId" value="${param.profileId}"/>
</fanta:label>
<c:set var="tabHeaderLabel" value="dataImport.headerLabel" scope="request"/>
<c:set var="tabHeaderValue" value="${tabNameLabel}" scope="request"/>

<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
    <c:set target="${tabItems}" property="dataImport.Tab.Detail" value="/DataImport/Forward/Update.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
    <c:set target="${tabItems}" property="dataImport.tab.duplicates" value="/ImportRecord/Duplicate/List.do"/>
</app2:checkAccessRight>


<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(profileId)" value="${param.profileId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>

