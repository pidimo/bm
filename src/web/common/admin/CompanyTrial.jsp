<%@ page import="java.util.List" %>
<%@ page session="false" %>
<%@ include file="/Includes.jsp" %>
<%
    String locale = request.getParameter("locale");
    Locale defaultLocale = request.getLocale();
    if (locale != null) {
        defaultLocale = new Locale(locale);
    }
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
    pageContext.setAttribute("language", defaultLocale.getLanguage());
%>
<table border="0" cellpadding="0" cellspacing="0" align="center" width="80%" class="container">
<tr>
<td>
<html:form action="/Company/CreateTrial.do?locale=${language}">
<html:hidden property="dto(locale)" value="${language}"/>
<table align="center" cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
    <td class="button" colspan="2">
        <html:submit property="dto(save)" styleClass="button" tabindex="19">
            <%= JSPHelper.getMessage(defaultLocale, "Common.save")%>
        </html:submit>
        <html:cancel styleClass="button" tabindex="20">
            <%= JSPHelper.getMessage(defaultLocale, "Common.cancel")%>
        </html:cancel>
    </td>
</tr>
<tr>
    <td colspan="2" class="title">
        <%= JSPHelper.getMessage(defaultLocale, "Company.information")%>
    </td>
</tr>
<tr>
    <td class="topLabel" rowspan="3">
        <%= JSPHelper.getMessage(defaultLocale, "Company.name")%>
    </td>
    <td class="contain">
        <app:text property="dto(name1)" styleClass="mediumText"
                  maxlength="60" tabindex="1"/>
    </td>
</tr>
<tr>
    <td class="contain">
        <app:text property="dto(name2)" styleClass="mediumText"
                  maxlength="60" tabindex="2"/>
    </td>
</tr>
<tr>
    <td class="contain">
        <app:text property="dto(name3)" styleClass="mediumText"
                  maxlength="60" tabindex="3"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.login")%>
    </td>
    <td class="contain">
        <app:text property="dto(companyCreateLogin)"
                  styleClass="mediumText" maxlength="20"
                  tabindex="4"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.primaryLanguage")%>
    </td>
    <td class="contain">
        <html:select property="dto(favoriteLanguage)" styleClass="shortSelect"
                     readonly="${op == 'update'}"
                     tabindex="9">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="systemLanguageList" property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>

<tr>
    <td class="title" colspan="2">
        <fmt:message key="Company.userInfo"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.rootName1")%>
    </td>
    <td class="contain">
        <app:text property="dto(rootName1)" styleClass="mediumText" maxlength="20"
                  tabindex="12"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.rootName2")%>
    </td>
    <td class="contain">
        <app:text property="dto(rootName2)" styleClass="mediumText" maxlength="20"
                  tabindex="13"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.userName")%>
    </td>
    <td class="contain">
        <app:text property="dto(userName)" styleClass="mediumText" maxlength="20" tabindex="14"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.userPassword")%>
    </td>
    <td class="contain">
        <html:password property="dto(userPassword)" styleClass="mediumText" maxlength="20" tabindex="15"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.reuserPassword")%>
    </td>
    <td class="contain">
        <html:password property="dto(rePassword)" styleClass="mediumText" maxlength="20" tabindex="16"/>
    </td>
</tr>
<tr>
    <td class="label">
        <%= JSPHelper.getMessage(defaultLocale, "Company.rootMail")%>
    </td>
    <td class="contain">
        <app:text property="dto(email)" styleClass="mediumText" maxlength="60" tabindex="17"/>
    </td>
</tr>
<tr>
    <td class="topLabel">
        <%= JSPHelper.getMessage(defaultLocale, "Company.registrationCode")%>
    </td>
    <td class="contain">
        <app:text property="dto(registrationCode)" styleClass="mediumText" maxlength="60" tabindex="18"/>
        <br/><%= JSPHelper.getMessage(defaultLocale, "Company.registrationCode.title")%><br/>
        <c:url var="urlCaptcha" value="/jcaptcha"/>
        <img src="${urlCaptcha}" height="80">
    </td>
</tr>
<tr>
    <td class="button" colspan="2">
        <html:submit property="dto(save)" styleClass="button" tabindex="21">
            <%= JSPHelper.getMessage(defaultLocale, "Common.save")%>
        </html:submit>
        <html:cancel styleClass="button" tabindex="22">
            <%= JSPHelper.getMessage(defaultLocale, "Common.cancel")%>
        </html:cancel>
    </td>
</tr>

</table>
</html:form>

</td>
</tr>
</table>

