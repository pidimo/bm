<%@ include file="/Includes.jsp" %>


<app2:checkAccessRight functionality="CONTACT" permission="VIEW">
    <div>
        <app:link action="/contacts/Search.do" addModuleParams="false" addModuleName="false">
            <fmt:message key="Common.search"/>
        </app:link>
    </div>
</app2:checkAccessRight>

