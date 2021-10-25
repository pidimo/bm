<%@ include file="/Includes.jsp" %>

<div class="navbar navbar-default hidden-xs navbar-fixed-top navbarLogoCompany" id="navbarLogoCompany">
    <div class="bg-divider-header"></div>
    <div class="container-fluid">
        <div class="navbar-header">
            <%--company logo--%>
            <c:set var="labelCompanyLogoId" value="companyLogoId_${sessionScope.user.valueMap['companyId']}"/>
            <c:set var="logoId" value="${applicationScope[labelCompanyLogoId]}"/>
            <c:if test="${(not empty logoId)}">
                <c:set var="companyLogoChange"
                       value="companyLogoStatus_${sessionScope.user.valueMap['companyId']}"/>
                <c:url var="urlCompanyLogo"
                       value="/Company/DownloadLogoImage.do?dto(freeTextId)=${logoId}&logoChangeCount=${applicationScope[companyLogoChange]}&isCompanyLogo=true"/>
                <img src="${urlCompanyLogo}" border="0" alt="" height="40"/>
            </c:if>

                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#navbar-main" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="#" style="text-decoration: none; cursor: default">${app2:getUserLocalizedCurrentDate(pageContext.request)} - <b>(${sessionScope.user.valueMap['login']})</b></a>
                </li>
                <li>
                    <a href="${app2:getContextDependantHelpUrl(helpResourceKey, pageContext.request)}" style="text-decoration:underline">
                        <fmt:message key="Common.help"/>
                    </a>
                </li>
                <li>
                    <app:link action="/Logoff.do?locale=${sessionScope.user.valueMap['locale']}" contextRelative="true"
                              addModuleName="false" addModuleParams="false" style="text-decoration:underline">
                        <fmt:message key="Common.logoff"/>
                    </app:link>
                </li>
            </ul>
        </div>
    </div>
</div>
