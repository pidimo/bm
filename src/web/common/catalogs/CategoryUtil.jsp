<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>


<c:set var="labelWidth" value="${labelWidth}" scope="request"/>
<c:set var="containWidth" value="${containWidth}" scope="request"/>
<c:set var="operation" value="${operation}" scope="request"/>
<c:set var="generalWidth" value="${generalWidth}" scope="request"/>
<c:set var="formName" value="${formName}" scope="request"/>

<c:import url="/common/catalogs/CategoryTabUtilHeader.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <c:forEach var="singleCategoryId" items="${app2:buildCategoriesWithoutGroups(table,secondTable,pageContext.request)}">

                <c:set var="elementCounter" value="${elementCounter+1}" scope="request"/>
                <c:set var="categoryId" value="${singleCategoryId}" scope="request"/>
                <c:import url="/common/catalogs/RenderCategory.jsp"/>

    </c:forEach>

    <c:forEach var="singleGroup" items="${app2:buildGroupsWithoutTabs(table, pageContext.request)}">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${true == singleGroup.onlySubCategories}">
                        <div id="groupId_${singleGroup.categoryGroupId}" style="display:none;">
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="title">${singleGroup.label}</td>
                                </tr>
                            </table>
                        </div>
                        <html:hidden property="dto(groupCounter_${singleGroup.categoryGroupId})"
                                     value=""
                                     styleId="groupCounter_${singleGroup.categoryGroupId}"/>
                    </c:when>
                    <c:otherwise>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td class="title">${singleGroup.label}</td>
                            </tr>
                        </table>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>

                <c:forEach var="categoryId" items="${singleGroup.categories}">
                    <c:set var="elementCounter" value="${elementCounter+1}" scope="request"/>
                    <c:set var="categoryId" value="${categoryId}" scope="request"/>
                    <c:set var="groupId" value="${singleGroup.categoryGroupId}" scope="request"/>
                    <c:import url="/common/catalogs/RenderCategory.jsp"/>
                </c:forEach>
        
    </c:forEach>

</table>
