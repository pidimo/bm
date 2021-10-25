<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="whiteSpace" value=" "/>


<html:form action="${action}" styleId="companyFormId">
<html:hidden property="dto(languageSelected)" styleId="languageSelected"/>
<html:hidden property="dto(companyId)"/>

<html:hidden property="dto(companyName)"
             value="${companyDeleteForm.dtoMap.name1}&nbsp;${not empty companyDeleteForm.dtoMap.name2 ? companyDeleteForm.dtoMap.name2 : whiteSpace}"/>

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
        <html:checkbox property="dto(active)" value="true" styleClass="adminCheckBox" disabled="true"/>
    </td>
</tr>

<tr>
    <td class="topLabel" rowspan="3">
        <fmt:message key="Company.name"/>
    </td>
    <td class="contain">
        <app:text property="dto(name1)"
                  styleClass="mediumText"
                  maxlength="60"
                  view="true"/>
    </td>
</tr>
<tr>
    <td class="contain">
        <app:text property="dto(name2)"
                  styleClass="mediumText"
                  maxlength="60"
                  view="true"/>
    </td>
</tr>
<tr>
    <td class="contain">
        <app:text property="dto(name3)"
                  styleClass="mediumText"
                  maxlength="60"
                  view="true"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.login"/>
    </td>
    <td class="contain">
        <app:text property="dto(login)"
                  styleClass="mediumText"
                  maxlength="20"
                  view="true"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.startLicenseDate"/>
    </td>
    <td class="contain">

        <app:dateText property="dto(startLicenseDate)"
                      maxlength="10"
                      styleId="startLicense"
                      calendarPicker="false"
                      datePatternKey="${datePattern}"
                      styleClass="mediumText"
                      view="true"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.finishLicenseDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(finishLicenseDate)"
                      view="true"
                      maxlength="10"
                      styleId="finishLicense"
                      calendarPicker="true"
                      datePatternKey="${datePattern}"
                      styleClass="mediumText"/>
    </td>
</tr>

<tr>
    <td class="label" width="15%">
        <fmt:message key="Company.trial"/>
    </td>
    <td class="contain" width="35%">
        <table>
            <tr>
                <td class="contain" style="margin:0;">
                    <html:radio property="dto(isTrial)"
                                value="true"
                                styleClass="radio"
                                disabled="true"/>
                    &nbsp;
                    <fmt:message key="Common.yes"/>
                </td>
                <td class="contain" style="margin:0;">
                    <html:radio property="dto(isTrial)"
                                value="false"
                                styleClass="radio"
                                disabled="true"/>
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
        <app:text property="dto(usersAllowed)"
                  styleClass="numberText"
                  maxlength="4"
                  view="true"/>
    </td>
</tr>

<tr>
    <td class="label">
        <fmt:message key="Common.creationDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(recordDate)"
                      view="true"
                      maxlength="10"
                      styleId="finishLicense"
                      calendarPicker="true"
                      datePatternKey="${datePattern}"
                      styleClass="mediumText"/>
    </td>
</tr>


<tr>
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
                  tabindex="12"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.rootName2"/>
    </td>
    <td class="contain">
        <app:text property="dto(rootName2)" view="${op!='create'}" styleClass="mediumText" maxlength="20"
                  tabindex="13"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.userName"/>
    </td>
    <td class="contain">
        <app:text property="dto(userName)" view="${op!='create'}" styleClass="mediumText" maxlength="20"
                  tabindex="14"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Company.rootMail"/>
    </td>
    <td class="contain">
        <app:text property="dto(email)" view="${op!='create'}" styleClass="mediumText" maxlength="60"
                  tabindex="17"/>
    </td>
</tr>


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
<c:forEach var="module" items="${moduleList}">
    <tr>
        <td class="label">
            <html:multibox property="modules" value="${module.moduleId}" tabindex="19" styleClass="radio"
                           disabled="true"/>
            &nbsp;
            <fmt:message key="${module.nameKey}"/>
        </td>
        <td class="contain">
            <app:text property="dto(${fn:replace(module.nameKey,'.','_')}_${module.moduleId})"
                      styleClass="mediumText"
                      maxlength="7" view="true"/>
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
