<%@ include file="/Includes.jsp" %>

<div class="footer">
    <app:link action="/mobile/Logoff.do?locale=${sessionScope.user.valueMap['locale']}"
              addModuleName="false" addModuleParams="false" style="text-decoration:underline" contextRelative="true">
        <fmt:message key="Common.logoff"/>
    </app:link>
</div>
