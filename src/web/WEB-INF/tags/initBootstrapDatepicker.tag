<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<%--boostrap datepicker scrips--%>

<link rel="stylesheet" href="<c:url value="/js/cacheable/bootstrap/bootstrapdatepicker/1.4.0/css/bootstrap-datepicker3.css"/>"/>

<script src="<c:url value="/js/cacheable/bootstrap/bootstrapdatepicker/1.4.0/js/bootstrap-datepicker.min.js"/>"></script>

<c:choose>
    <c:when test="${'es' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/bootstrap/bootstrapdatepicker/1.4.0/locales/bootstrap-datepicker.es.min.js"/>"></script>
    </c:when>
    <c:when test="${'de' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/bootstrap/bootstrapdatepicker/1.4.0/locales/bootstrap-datepicker.de.min.js"/>"></script>
    </c:when>
    <c:when test="${'fr' eq sessionScope.user.valueMap['locale']}">
        <script src="<c:url value="/js/cacheable/bootstrap/bootstrapdatepicker/1.4.0/locales/bootstrap-datepicker.fr.min.js"/>"></script>
    </c:when>
</c:choose>