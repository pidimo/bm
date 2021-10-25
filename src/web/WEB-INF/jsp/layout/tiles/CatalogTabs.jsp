<%@ include file="/Includes.jsp" %>


<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>


<c:choose>
    <c:when test="${param.category == '1'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.contact" scope="request"/>
    </c:when>
    <c:when test="${param.category == '2'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.customer" scope="request"/>
    </c:when>
    <c:when test="${param.category == '3'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.communication" scope="request"/>
    </c:when>
    <c:when test="${param.category == '4'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.products" scope="request"/>
    </c:when>
    <c:when test="${param.category == '5'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.Sales" scope="request"/>
    </c:when>
    <c:when test="${param.category == '6'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.Scheduler" scope="request"/>
    </c:when>
    <c:when test="${param.category == '7'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.Support" scope="request"/>
    </c:when>
    <c:when test="${param.category == '8'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.General" scope="request"/>
    </c:when>
    <c:when test="${param.category == '9'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.Campaign" scope="request"/>
    </c:when>
    <c:when test="${param.category == '10'}">
        <c:set var="tabHeaderLabel" value="Catalog.Title.Finance" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabHeaderLabel" value="Catalog.Title.contact" scope="request"/>
    </c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${category == '1' || param.category == null}">
        <c:set var="tabItems" value="${configurationMenuMap['1']}" scope="request"/>
    </c:when>
    <c:when test="${category == '2'}">
        <c:set var="tabItems" value="${configurationMenuMap['2']}" scope="request"/>
    </c:when>
    <c:when test="${category == '3'}">
        <c:set var="tabItems" value="${configurationMenuMap['3']}" scope="request"/>
    </c:when>
    <c:when test="${category == '4'}">
        <c:set var="tabItems" value="${configurationMenuMap['4']}" scope="request"/>
    </c:when>
    <c:when test="${category == '5'}">
        <c:set var="tabItems" value="${configurationMenuMap['5']}" scope="request"/>
    </c:when>
    <c:when test="${category == '6'}">
        <c:set var="tabItems" value="${configurationMenuMap['6']}" scope="request"/>
    </c:when>
    <c:when test="${category == '7'}">
        <c:set var="tabItems" value="${configurationMenuMap['7']}" scope="request"/>
    </c:when>
    <c:when test="${category == '8'}">
        <c:set var="tabItems" value="${configurationMenuMap['8']}" scope="request"/>
    </c:when>
    <c:when test="${category == '9'}">
        <c:set var="tabItems" value="${configurationMenuMap['9']}" scope="request"/>
    </c:when>
    <c:when test="${category == '10'}">
        <c:set var="tabItems" value="${configurationMenuMap['10']}" scope="request"/>
    </c:when>
</c:choose>




