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
    <c:set var="canPullDownMenuBeShown" value="true" scope="request"/>
    <div style="padding:3px 10px 3px 3px;">
        <app:link page="${action}" addModuleParams="false" titleKey="${titleKey}"
                  contextRelative="${not empty contextRelative ? contextRelative : 'false'}">
            <fmt:message key="${titleKey}"/>
        </app:link>
    </div>
</app2:checkAccessRight>