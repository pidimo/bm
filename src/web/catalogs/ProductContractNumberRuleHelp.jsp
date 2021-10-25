<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="pagetitle" value="Common.help" scope="request"/>
<c:set var="windowTitle" value="SequenceRule.format.formatHelp" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/ProductContractNumberRuleHelp.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/ProductContractNumberRuleHelp.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>