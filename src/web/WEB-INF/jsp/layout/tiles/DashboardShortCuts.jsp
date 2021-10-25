<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">

    <li>
        <app:link action="/Dashboard/Container/DrawContainer.do" contextRelative="true" addModuleParams="false" addModuleName="false">
            <fmt:message key="Common.home"/>
        </app:link>
    </li>
    <app2:checkAccessRight functionality="DASHBOARD" permission="VIEW">
        <li>
            <app:link action="/Dashboard/Container/Read.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="dashboard.configuration"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <c:if test="${app2:hasAccessRight(pageContext.request, 'USERSETTINGS','VIEW') || app2:hasAccessRight(pageContext.request, 'USERINTERFACE','VIEW') || app2:hasAccessRight(pageContext.request, 'COMPANYLOGO','VIEW')}">
        <li>
            <app:link action="/UserPreferences/User.do?dto(userId)=${user.valueMap['userId']}"
                      addModuleParams="false" addModuleName="false" contextRelative="true">
                <fmt:message key="Common.userSettings"/>
            </app:link>
        </li>
    </c:if>
    <c:set var="hasCompanyPreferencess"
           value="${app2:hasAccessRight(pageContext.request, 'COMPANYPREFERENCES','VIEW')}"/>
    <c:set var="hasCompanyInterface"
           value="${app2:hasAccessRight(pageContext.request, 'COMPANYINTERFACE','VIEW')}"/>
    <c:set var="hasCompanyLogo" value="${app2:hasAccessRight(pageContext.request, 'COMPANYLOGO','VIEW')}"/>

    <c:if test="${hasCompanyPreferencess || hasCompanyInterface || hasCompanyLogo}">
        <c:set var="pageUrl"
               value="/Company/Forward/Update.do?contactId=${sessionScope.user.valueMap['companyId']}&dto(addressId)=${sessionScope.user.valueMap['companyId']}&flagCompany=true"/>
        <c:if test="${!hasCompanyPreferencess}">
            <c:set var="listSect" value="${app2:getSectionListOfXmlFile(pageContext.request)}"/>
            <c:set var="firstSection" value="${listSect[0].value}"/>
            <c:set var="pageUrl"
                   value="/UIManager/Forward/CompanyStyleConfigurable.do?paramSection=${app2:encode(firstSection)}"/>
        </c:if>
        <c:if test="${!hasCompanyPreferencess && !hasCompanyInterface}">
            <c:set var="pageUrl"
                   value="/CompanyLogo/Forward/Update.do?contactId=${sessionScope.user.valueMap['companyId']}&dto(addressId)=${sessionScope.user.valueMap['companyId']}&flagCompany=true&isLogoCompany=true"/>
        </c:if>
        <li>
            <app:link page="${pageUrl}"
                      contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Company.preferences"/>
            </app:link>
        </li>

        <li class="hidden-lg hidden-md hidden-sm">
            <a href="${app2:getContextDependantHelpUrl(helpResourceKey, pageContext.request)}">
                <fmt:message key="Common.help"/>
            </a>
        </li>
        <li class="hidden-lg hidden-md hidden-sm">
            <app:link action="/Logoff.do?locale=${sessionScope.user.valueMap['locale']}" contextRelative="true"
                      addModuleName="false" addModuleParams="false">
                <fmt:message key="Common.logoff"/>
            </app:link>
        </li>
    </c:if>
</ul>



