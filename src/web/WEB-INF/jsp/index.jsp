<%@ page import="com.piramide.elwis.utils.Constants" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page session="false" %>
<%@ include file="/Includes.jsp" %>
<%
    String basePath = request.getContextPath();

    String locale = request.getParameter("locale");
    Locale defaultLocale = request.getLocale();
    if (locale != null) {
        defaultLocale = new Locale(locale);
    }
    pageContext.setAttribute("language", defaultLocale.getLanguage());
    pageContext.setAttribute("defaultLocale", defaultLocale);

    Cookie[] cookies = request.getCookies();
    String lastUser = null;
    String lastCompany = null;

    if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            if ("UserLastLogin".equals(cookies[i].getName())) {
                lastUser = URLDecoder.decode(cookies[i].getValue(), Constants.CHARSET_ENCODING);
            }
            if ("UserLastCompany".equals(cookies[i].getName())) {
                lastCompany = URLDecoder.decode(cookies[i].getValue(), Constants.CHARSET_ENCODING);
            }
        }
    }


%>

<tags:jscript language="JavaScript" src="/js/cacheable/jquery.form-1.0.js"/>
<tags:jscript language="JavaScript" src="/js/external-login-1.1.2.js"/>

<c:set var="customCopyrightClass" value="copyrightLogin" scope="request"/>

<div>
    <div id="loginFormContainer">

        <html:form action="${not empty param['homePage'] ? '/ExternalLogon' : '/Logon'}"
                   focus='<%= lastUser!=null & lastCompany!=null ? "dto(password)" : "dto(login)"%>'
                   styleId="loginForm">

            <div class="row" style="display:none;" id="ajaxProcessId">
                <div class="col-xs-12" style="text-align: right">
                    <img src="<%= basePath %>/layout/ui/img/ajax-loader-bg.gif" border="0" alt=""
                         style="text-align:center" />&nbsp;
                </div>
            </div>

            <div class="form-group">
                <label class="loginLabel" for="loginTextId"><%= JSPHelper.getMessage(defaultLocale, "Logon.username")%></label>

                <html:text property="dto(login)" styleClass="form-control loginInput" maxlength="20"
                           tabindex="1" value='<%= (lastUser!=null) ? lastUser : "" %>' styleId="loginTextId"/>
            </div>

            <div class="form-group">
                <label class="loginLabel" for="passwordTextId"><%= JSPHelper.getMessage(defaultLocale, "Logon.password")%></label>

                <html:password property="dto(password)" styleClass="form-control loginInput" maxlength="20" tabindex="2"
                               redisplay="false" style="width" styleId="passwordTextId"/>
            </div>

            <div class="form-group">
                <label class="loginLabel" for="companyTextId"><%= JSPHelper.getMessage(defaultLocale, "Logon.passwordCia")%></label>

                <html:text property="dto(companyLogin)" styleClass="form-control loginInput" maxlength="20"
                           tabindex="3" value='<%= (lastCompany!=null) ? lastCompany : "" %>'
                           styleId="companyTextId"/>
            </div>

            <html:submit styleClass="btn btn-primary btn-lg btn-block button loginButton" tabindex="4">
                <%= JSPHelper.getMessage(defaultLocale, "Common.logon")%>
            </html:submit>

            <div class="checkbox checkbox-default loginCheckbox loginLabel">
                <html:hidden property="dto(language)" value="${language}"/>
                <c:if test="${not empty param['homePage']}">
                    <input type="hidden" name="homePage" value="1"/>
                    <c:set var="homePageParam" value="&homePage=1"/>
                    <c:if test="${empty applicationScope.homeSitePage}">
                        <c:set var="homeSitePage" value="1" scope="application"/>
                    </c:if>
                    <script type="text/javascript">
                        jQuery.ajaxSetup({contentType: "application/x-www-form-urlencoded; charset=UTF-8"});

                        $(document).ready(function () {
                            var options = {
                                beforeSubmit: showLoading,
                                success: processLoginResponse
                            };
                            $("#loginForm").ajaxForm(options);
                        });
                    </script>
                </c:if>


                <input tabindex="5" name="dto(rememberInfo)" type="checkbox"
                       value="remember" <%= (lastUser!=null & lastCompany!=null) ? "checked" : "" %>
                       id="rememberInfoId">
                <label for="rememberInfoId" style="font-size: 13px;">
                    <%= JSPHelper.getMessage(defaultLocale, "Logon.rememberInfo")%>
                </label>
            </div>

            <c:if test="${'bootstrap' == param.uiMode}">
                <input type="hidden" name="isBootstrapUI" value="true"/>
                <c:set var="isBootstrapUIParam" value="&uiMode=bootstrap"/>
            </c:if>
        </html:form>
    </div>

    <div class="center">
        <c:forEach items="${app2:getSystemLanguages()}" var="systemLanguage">
            <a href="${pageContext.request.contextPath}/index.jsp?locale=${systemLanguage.key}${homePageParam}${isBootstrapUIParam}">
                <img src="${pageContext.request.contextPath}/layout/ui/img/locale/${systemLanguage.key}.gif" border="1"
                     alt='${app2:getMessageByLocale(defaultLocale, systemLanguage.value)}' style="border-color:#FFF"
                     title="${app2:getMessageByLocale(defaultLocale, systemLanguage.value)}">
            </a>
        </c:forEach>
    </div>

    <div class="center">
        <%--
        <c:set var="trialLocale" value="${param.locale}"/>
        <c:if test="${null == param.locale}">
            <c:set var="trialLocale" value="${language}"/>
        </c:if>
        <c:if test="${app2:createTrialCompany() == true}">

            <tr>
                <td align="center">
                    <font color="#FFFFFF">
                        <app:url var="trialCompanyLink" value="/Company/Forward/CreateTrial.do?locale=${language}" enableEncodeURL="false"/>
                        <a href="${trialCompanyLink}" style="color: #FFFFFF">
                            <%= JSPHelper.getMessage(defaultLocale, "Common.createTrialCompany")%>
                        </a>
                    </font>
                </td>
            </tr>
        </c:if>--%>
        <div class="loginLabel downloadLink">
            <a href="${pageContext.request.contextPath}/download/BMWordPlugin-3.1.2.exe">
                <%=  JSPHelper.getMessage(defaultLocale, "Common.download.Word_Plugin", "version 3.1.2")%>
            </a>
        </div>
    </div>

</div>

