<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%--jquery validations scrips--%>
<script src="<c:url value="/js/cacheable/jqueryvalidation/1.13.1/jquery.validate.min.js"/>"></script>

<c:choose>
    <c:when test="${'es' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/jqueryvalidation/1.13.1/localization/messages_es.min.js"/>"></script>
    </c:when>
    <c:when test="${'de' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/jqueryvalidation/1.13.1/localization/messages_de.min.js"/>"></script>
    </c:when>
    <c:when test="${'fr' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/jqueryvalidation/1.13.1/localization/messages_fr.min.js"/>"></script>
    </c:when>
</c:choose>