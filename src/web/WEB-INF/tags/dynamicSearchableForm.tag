<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ attribute name="addForm" required="true" type="java.lang.Boolean"%>

<c:choose>
    <c:when test="${addForm}">
        <html:form action="/DynamicSearch/Ajax/FillValue.do">
            <jsp:doBody/>
        </html:form>
    </c:when>
    <c:otherwise>
        <jsp:doBody/>
    </c:otherwise>
</c:choose>

