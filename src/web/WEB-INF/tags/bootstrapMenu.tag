<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="titleKey" required="true" description="menu title's resource key" %>
<%@ attribute name="addSeparator" required="false" description="add separator before write menu" %>

<jsp:doBody var="reportMenuBody" scope="request"/>

<c:if test="${fn:contains(reportMenuBody,'<')}">
    <c:if test="${'true' eq addSeparator}">
        <li role="separator" class="divider"></li>
    </c:if>

    <li>
        <a href="#">
            <fmt:message key="${titleKey}"/>
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
            <jsp:doBody/>
        </ul>
    </li>
</c:if>
