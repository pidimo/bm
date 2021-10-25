<%@ include file="/Includes.jsp" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="left" width="40%" class="moduleShortCut" nowrap>
            <app2:checkAccessRight functionality="DASHBOARD" permission="VIEW">
                &nbsp;|&nbsp;
                <html:link action="/Dashboard/Container/Read.do">
                    <fmt:message key="dashboard.configuration"/>
                </html:link>
            </app2:checkAccessRight>
            <c:if test="${app2:hasAccessRight(pageContext.request, 'USERSETTINGS','VIEW') || app2:hasAccessRight(pageContext.request, 'USERINTERFACE','VIEW') || app2:hasAccessRight(pageContext.request, 'COMPANYLOGO','VIEW')}">
                &nbsp;|&nbsp;
                <app:link action="/UserPreferences/User.do?dto(userId)=${user.valueMap['userId']}"
                          addModuleParams="false" addModuleName="false" contextRelative="true">
                    <fmt:message key="Common.userSettings"/>
                </app:link>
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
                &nbsp;|&nbsp;
                <app:link page="${pageUrl}"
                          contextRelative="true" addModuleParams="false">
                    <fmt:message key="Company.preferences"/>
                </app:link>
            </c:if>
            &nbsp;|

            <%--todo: thi is only to test--%>
            <%--<app:link action="/Migration/Layout/User.do?dto(userId)=${user.valueMap['userId']}&dto(companyId)=${user.valueMap['companyId']}&dto(op)=userMigration"
                      addModuleParams="false" addModuleName="false" contextRelative="true">
                [[TEST USER MIGRATION]]
            </app:link>--%>
            <%--todo: thi is only to test--%>

        </td>
    </tr>
</table>