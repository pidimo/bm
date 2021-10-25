<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.product.translate" scope="request"/>

<c:set var="pagetitle" value="Product.plural" scope="request"/>
<c:set var="windowTitle" value="Product.title.translate" scope="request"/>
<fmt:message var="title" key="Product.title.translate" scope="request"/>
<fmt:message var="languageTitle" key="Product.Language" scope="request"/>
<c:set var="action" value="/Product/Translate.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/Translate.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/Translate.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>