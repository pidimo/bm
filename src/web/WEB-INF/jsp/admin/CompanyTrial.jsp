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
<html:form action="/Company/CreateTrial.do?locale=${language}" styleClass="form-horizontal">
    <html:hidden property="dto(locale)" value="${language}"/>
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit property="dto(save)" styleClass="button" tabindex="19">
                <%= JSPHelper.getMessage(defaultLocale, "Common.save")%>
            </html:submit>
            <html:cancel styleClass="button" tabindex="20">
                <%= JSPHelper.getMessage(defaultLocale, "Common.cancel")%>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <%= JSPHelper.getMessage(defaultLocale, "Company.information")%>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.name")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(name1)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="60" tabindex="1"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class=${app2:getFormLabelClasses()}l">

                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(name2)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="60" tabindex="2"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">

                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(name3)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="60" tabindex="3"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.login")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(companyCreateLogin)"
                              styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="20"
                              tabindex="4"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.primaryLanguage")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:select property="dto(favoriteLanguage)"
                                 styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                 readonly="${op == 'update'}"
                                 tabindex="9">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="systemLanguageList" property="value"
                                      labelProperty="label"/>
                    </html:select>
                </div>
            </div>
            <legend class="title">
                <fmt:message key="Company.userInfo"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.rootName1")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(rootName1)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="20"
                              tabindex="12"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.rootName2")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(rootName2)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="20"
                              tabindex="13"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.userName")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(userName)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="20" tabindex="14"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.userPassword")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:password property="dto(userPassword)" styleClass="mediumText ${app2:getFormInputClasses()}"
                                   maxlength="20"
                                   tabindex="15"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.reuserPassword")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:password property="dto(rePassword)" styleClass="mediumText ${app2:getFormInputClasses()}"
                                   maxlength="20"
                                   tabindex="16"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.rootMail")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(email)" styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="60"
                              tabindex="17"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <%= JSPHelper.getMessage(defaultLocale, "Company.registrationCode")%>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(registrationCode)" styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="60"
                              tabindex="18"/>
                    <br/><%= JSPHelper.getMessage(defaultLocale, "Company.registrationCode.title")%><br/>
                    <c:url var="urlCaptcha" value="/jcaptcha"/>
                    <img src="${urlCaptcha}" height="80">
                </div>
            </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit property="dto(save)" styleClass="button ${app2:getFormButtonClasses()}" tabindex="21">
                <%= JSPHelper.getMessage(defaultLocale, "Common.save")%>
            </html:submit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="22">
                <%= JSPHelper.getMessage(defaultLocale, "Common.cancel")%>
            </html:cancel>
        </div>
    </div>
</html:form>
