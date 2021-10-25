<%@ include file="/Includes.jsp" %>
<c:set var="labelWidth" value="${labelWidth}" scope="request"/>
<c:set var="containWidth" value="${containWidth}" scope="request"/>
<c:set var="operation" value="${operation}" scope="request"/>
<c:set var="generalWidth" value="${generalWidth}" scope="request"/>
<c:set var="formName" value="${formName}" scope="request"/>
<c:import url="/WEB-INF/jsp/catalogs/CategoryTabUtilHeader.jsp"/>
<c:forEach var="singleCategoryId" items="${app2:buildCategoriesWithoutGroups(table,secondTable,pageContext.request)}">
    <c:set var="elementCounter" value="${elementCounter+1}" scope="request"/>
    <c:set var="categoryId" value="${singleCategoryId}" scope="request"/>
    <c:import url="/WEB-INF/jsp/catalogs/RenderCategory.jsp"/>

</c:forEach>
<c:forEach var="singleGroup" items="${app2:buildGroupsWithoutTabs(table, pageContext.request)}">
    <c:choose>
        <c:when test="${true == singleGroup.onlySubCategories}">
            <legend class="title" id="groupId_${singleGroup.categoryGroupId}" style="display:none;">
                    ${singleGroup.label}
            </legend>
            <html:hidden property="dto(groupCounter_${singleGroup.categoryGroupId})"
                         value=""
                         styleId="groupCounter_${singleGroup.categoryGroupId}"/>
        </c:when>
        <c:otherwise>
            <legend class="title">
                    ${singleGroup.label}
            </legend>
        </c:otherwise>
    </c:choose>
    <c:forEach var="categoryId" items="${singleGroup.categories}">
        <c:set var="elementCounter" value="${elementCounter+1}" scope="request"/>
        <c:set var="categoryId" value="${categoryId}" scope="request"/>
        <c:set var="groupId" value="${singleGroup.categoryGroupId}" scope="request"/>
        <c:import url="/WEB-INF/jsp/catalogs/RenderCategory.jsp"/>
    </c:forEach>

</c:forEach>
