<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ attribute name="language" %><%@ attribute name="src" %>
<script language="${language}" src="<c:url value="${src}"/>" type="text/javascript"></script>
