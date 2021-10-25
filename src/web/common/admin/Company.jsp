<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<script type="text/javascript">
    function updateCopyConfigurationSelect(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        document.getElementById("languageSelected").value = opt.value;
        document.getElementById("updateLanguage").value = 'true';
        document.getElementById("companyFormId").submit();
    }

    function updateTimeZoneSelect(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        document.getElementById("templateSelected").value = opt.value;
        document.getElementById("updateLanguage").value = 'true';
        document.getElementById("companyFormId").submit();
    }
</script>

<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>
<c:set var="companyTemplateTypes" value="${app2:getCompanyTemplateType(pageContext.request)}"/>
<html:form action="${action}" styleId="companyFormId">
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(updateLanguage)" value="" styleId="updateLanguage"/>
<html:hidden property="dto(languageSelected)" styleId="languageSelected"/>
<html:hidden property="dto(templateSelected)" styleId="templateSelected"/>
<c:if test="${('update' == op)}">
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(companyId)"/>
</c:if>
<html:hidden property="dto(creation_)"/>
<table cellSpacing=0 cellPadding=0 width="500" border=0 align="center">
<tr>
    <td class="button">

        <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="button" property="dto(save)">
            ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>

    </td>
</tr>
<tr>
    <td colspan="2" class="title">
        <fmt:message key="Company.information"/>
    </td>
</tr>

<tr>
<td>
<table border="0" cellpadding="0" cellspacing="0" width="500" align="center" class="container">
<tr>
    <td class="topLabel" align="top" width="35%">
        <fmt:message key="Company.active"/>
    </td>
    <td class="contain" width="65%">
        <html:checkbox property="dto(active)" value="true" styleClass="adminCheckBox"/>
    </td>
</tr>

<tr>
    <td class="topLabel" rowspan="3">
        <fmt:message key="Company.name"/>
    </td>
    <td class="contain">
        <app:text property="dto(name1)" view="${op!='create'}" styleClass="mediumText" maxlength="60" tabindex="1"/>
    </td>
</tr>
<tr>
        <%--<td >&nbsp;</td>--%>
    <td class="contain">
        <app:text property="dto(name2)" view="${op!='create'}" styleClass="mediumText" maxlength="60" tabindex="2"/>
    </td>
</tr>
<tr>
        <%--<td class="label">&nbsp;</td>--%>
    <td class="contain">
        <app:text property="dto(name3)" view="${op!='create'}" styleClass="mediumText" maxlength="60" tabindex="3"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.login"/>
    </td>
    <td class="contain">
        <app:text property="dto(companyCreateLogin)" view="${op=='update'}" styleClass="mediumText" maxlength="20"
                  tabindex="4"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.startLicenseDate"/>
    </td>
    <td class="contain">
        <fmt:message var="datePattern" key="datePattern"/>
        <app:dateText property="dto(startLicenseDate)" view="${op=='delete'}" maxlength="10" styleId="startLicense"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="mediumText" tabindex="5"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.finishLicenseDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(finishLicenseDate)" view="${op=='delete'}" maxlength="10" styleId="finishLicense"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="mediumText" tabindex="6"/>
    </td>
</tr>

<tr>
    <td class="label" width="15%">
        <fmt:message key="Company.trial"/>
    </td>
    <td class="contain" width="35%">
        <%--this field is only to preserve data in old UI--%>
        <html:hidden property="dto(companyType)"/>
        <table>
            <tr>
                <td class="contain" style="margin:0;">
                    <html:radio property="dto(trial)" value="true" styleClass="radio" tabindex="7"/>
                    &nbsp;
                    <fmt:message key="Common.yes"/>
                </td>
                <td class="contain" style="margin:0;">
                    <html:radio property="dto(trial)" value="false" styleClass="radio" tabindex="7"/>
                    &nbsp;
                    <fmt:message key="Common.no"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.usersAllowed"/>
    </td>
    <td class="contain">
        <app:text property="dto(usersAllowed)" styleClass="numberText" maxlength="4" tabindex="8"/>
    </td>
</tr>

<tr>
    <td class="label">
        <fmt:message key="Company.defaultUILanguage"/>
    </td>
    <td class="contain">
        <c:choose>
            <c:when test="${op == 'create'}">
                <html:select property="dto(favoriteLanguage)" styleClass="shortSelect"
                             tabindex="9" onchange="javascript:updateCopyConfigurationSelect(this);">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="systemLanguageList" property="value" labelProperty="label"/>
                </html:select>
            </c:when>
            <c:otherwise>
                <html:select property="dto(favoriteLanguage)" styleClass="shortSelect"
                             tabindex="9">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="systemLanguageList" property="value" labelProperty="label"/>
                </html:select>
            </c:otherwise>
        </c:choose>
    </td>
</tr>

<tr>
    <td class="label">
        <fmt:message key="Company.maxMaxAttachSize"/>
    </td>
    <td class="contain">
        <app:text property="dto(maxMaxAttachSize)" styleClass="numberText" maxlength="2" tabindex="10"/>
        &nbsp; (<fmt:message key="Common.megabytes"/>)
    </td>
</tr>

<c:if test="${op == 'create'}">
    <tr>
        <td class="label">
            <fmt:message key="Company.copyConfigurationFrom"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(templateCampaignId)"
                          listName="templateCompanyList"
                          labelProperty="companyName"
                          valueProperty="templateCompanyId"
                          firstEmpty="true"
                          styleClass="mediumSelect"
                          tabIndex="10"
                          onChange="javascript:updateTimeZoneSelect(this);">
                <fanta:parameter field="language" value="${companyForm.dtoMap['languageSelected']}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>
<tr>
    <TD class="label">
        <fmt:message key="Company.timeZone"/>
    </TD>
    <td class="contain">
        <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
        <html:select property="dto(timeZone)" styleClass="mediumSelect" tabindex="11">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="timeZonesConstants" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.setAsTemplate"/>
    </td>
    <td class="contain">
        <html:select property="dto(copyTemplate)" styleClass="mediumSelect" tabindex="12">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="companyTemplateTypes" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<c:if test="${('create' != op)}">
    <tr>
        <td class="label">
            <fmt:message key="Common.creationDate"/>
        </td>
        <td class="contain">
            <html:hidden property="dto(creation_Date)" write="true"/>
        </td>
    </tr>
</c:if>
<!--     user - Information      -->
<tr>
    <!--<td colspan="2" class="title">-->
    <td class="title" colspan="2">
        <fmt:message key="Company.userInfo"/>
    </td>

</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.rootName1"/>
    </td>
    <td class="contain">
        <app:text property="dto(rootName1)" view="${op!='create'}" styleClass="mediumText" maxlength="20"
                  tabindex="13"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.rootName2"/>
    </td>
    <td class="contain">
        <app:text property="dto(rootName2)" view="${op!='create'}" styleClass="mediumText" maxlength="20"
                  tabindex="14"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.userName"/>
    </td>
    <td class="contain">
        <app:text property="dto(userName)" view="${op!='create'}" styleClass="mediumText" maxlength="20" tabindex="15"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.userPassword"/>
    </td>
    <td class="contain">
        <html:password property="dto(userPassword)" styleClass="mediumText" maxlength="20" tabindex="16"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.reuserPassword"/>
    </td>
    <td class="contain">
        <html:password property="dto(rePassword)" styleClass="mediumText" maxlength="20" tabindex="17"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.rootMail"/>
    </td>
    <td class="contain">
        <app:text property="dto(email)" view="${op!='create'}" styleClass="mediumText" maxlength="60" tabindex="18"/>
    </td>
</tr>
<c:if test="${op == 'create'}">
    <tr>
        <td class="label">
            <fmt:message key="Company.sentNotification"/>
        </td>
        <td class="contain">
            <html:checkbox property="dto(sentNotification)" value="true" styleClass="adminCheckBox" tabindex="19"/>
        </td>
    </tr>
</c:if>


<tr>
    <td colspan="2" class="title">
        <fmt:message key="Company.modules"/>
    </td>
</tr>
<tr>
    <td class="label">&nbsp;</td>
    <td class="label">
        <fmt:message key="Company.entriesLimit"/>
    </td>
</tr>
<c:forEach var="module" items="${app2:getSystemModules(pageContext.request)}">
    <tr>
        <td class="label">
            <html:multibox property="modules" value="${module.moduleId}" tabindex="20" styleClass="radio"/>
            &nbsp;
            <fmt:message key="${module.nameKey}"/>
        </td>
        <td class="contain">
            <app:text property="dto(${fn:replace(module.nameKey,'.','_')}_${module.moduleId})" styleClass="mediumText"
                      maxlength="7" tabindex="20"/>
        </td>
    </tr>
</c:forEach>

    <tr>
        <td colspan="2" class="title"><fmt:message key="Company.mobileAccess"/></td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Company.mobile.active"/></td>
        <td class="contain">
            <html:checkbox property="dto(mobileActive)" disabled="${op == 'delete'}" tabindex="21" styleClass="radio"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Company.mobile.usersAllowed"/>
        </td>
        <td class="contain">
            <app:text property="dto(mobileUserAllowed)" view="${op == 'delete'}" styleClass="numberText" maxlength="4" tabindex="22"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Company.mobile.startLicense"/>
        </td>
        <td class="contain">
            <app:dateText property="dto(mobileStartLicense)" view="${op == 'delete'}" maxlength="10" styleId="startLicenseMobile"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="mediumText" tabindex="23"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Company.mobile.finishLicense"/>
        </td>
        <td class="contain">
            <app:dateText property="dto(mobileEndLicense)" view="${op == 'delete'}" maxlength="10" styleId="finishLicenseMobile"
                          calendarPicker="true" datePatternKey="${datePattern}" styleClass="mediumText" tabindex="24"/>
        </td>
    </tr>
</table>
</td>
</tr>
<tr>
    <td class="button">
        <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="button" property="dto(save)"
                             indexed="31">
            ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>


