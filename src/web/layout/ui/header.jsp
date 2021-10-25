<%@ include file="/Includes.jsp" %>


<table width="100%" border="0" cellpadding="1" cellspacing="0">
    <tr>
        <td align="left" valign="middle">
            <%--company logo--%>
            <c:set var="labelCompanyLogoId" value="companyLogoId_${sessionScope.user.valueMap['companyId']}"/>
            <c:set var="logoId" value="${applicationScope[labelCompanyLogoId]}"/>
            <c:if test="${(not empty logoId)}">
                <c:set var="companyLogoChange" value="companyLogoStatus_${sessionScope.user.valueMap['companyId']}"/>
                <c:url var="urlCompanyLogo"
                       value="/Company/DownloadLogoImage.do?dto(freeTextId)=${logoId}&logoChangeCount=${applicationScope[companyLogoChange]}&isCompanyLogo=true"/>
                <img src="${urlCompanyLogo}" border="0" alt=""/>
            </c:if>
        </td>
        <td align="right" valign="top">
            <font style="font-size: 9px;">
                ${app2:getUserLocalizedCurrentDate(pageContext.request)} -
                <b>(${sessionScope.user.valueMap['login']})</b>
                [
                <app2:checkAccessRight functionality="USERSETTINGS" permission="VIEW">
                    <app:link action="/UserPreferences/User.do?dto(userId)=${sessionScope.user.valueMap['userId']}"
                              addModuleParams="false" addModuleName="false" contextRelative="true"
                              style="text-decoration:underline">
                        <fmt:message key="Common.userSettings"/>
                    </app:link>
                    ,
                </app2:checkAccessRight>

                <a href="${app2:getConfigurationPropertyValue('elwis.help.URL')}" style="text-decoration:underline">
                    <fmt:message key="Common.help"/>
                </a>,

                <app:link action="/Logoff.do?locale=${sessionScope.user.valueMap['locale']}" contextRelative="true"
                          addModuleName="false" addModuleParams="false" style="text-decoration:underline">
                    <fmt:message key="Common.logoff"/>
                </app:link>
                ]&nbsp;
            </font>

        </td>
    </tr>
</table>


