<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ attribute name="action" required="true" %>
<%@ attribute name="titleKey" required="true" %>
<%@ attribute name="contextRelative" %>
<%@ attribute name="functionality" required="true" %>
<%@ attribute name="permission" required="true" %>

<app2:checkAccessRight functionality="${functionality}" permission="${permission}">
    <li>
        <app:link page="${action}" addModuleParams="false"
                  contextRelative="${not empty contextRelative ? contextRelative : 'false'}">
            <fmt:message key="${titleKey}"/>
        </app:link>
    </li>
</app2:checkAccessRight>