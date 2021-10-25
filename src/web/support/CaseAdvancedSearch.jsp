<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.supportCase.advancedList" scope="request"/>


<c:set var="windowTitle" value="Case.Title.advancedSearch" scope="request"/>

<c:set var="action" value="CaseList.do" scope="request"/>
<c:set var="create" value="Case/Forward/Create.do" scope="request"/>
<c:set var="edit" value="Case/Forward/Update.do" scope="request"/>
<c:set var="delete" value="Case/Forward/Delete.do" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/support/CaseAdvancedSearch.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/support/CaseAdvancedSearch.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>