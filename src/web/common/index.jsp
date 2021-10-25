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

<tags:jQuery/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.form-1.0.js"/>
<tags:jscript language="JavaScript" src="/js/external-login-1.1.2.js"/>

<div class="center" style="vertical-align:top; padding-top:2px">


    <div class="center" id="loginFormContainer">


        <table border="0" cellpadding="0" cellspacing="0" align="center" width="220px" style="vertical-align:top">


            <html:form action="${not empty param['homePage'] ? '/ExternalLogon' : '/Logon'}"
                       focus='<%= lastUser!=null & lastCompany!=null ? "dto(password)" : "dto(login)"%>'
                       styleId="loginForm">
                <tr>
                    <td class="title" style="border-right:0;padding-left:5px">
                        <%= JSPHelper.getMessage(defaultLocale, "Logon.Title")%>
                    </td>
                    <td class="title" style="text-align:right;border-left:0">
                        <img src="<%= basePath %>/layout/ui/img/ajax-loader-bg.gif" border="0" alt=""
                             style="display:none;text-align:center" id="ajaxProcessId"/>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td class="label" nowrap><%= JSPHelper.getMessage(defaultLocale, "Logon.username")%>
                    </td>
                    <td class="contain">
                        <html:text property="dto(login)" styleClass="shortText" maxlength="20"
                                   tabindex="1" value='<%= (lastUser!=null) ? lastUser : "" %>' styleId="loginTextId"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"><%= JSPHelper.getMessage(defaultLocale, "Logon.password")%>
                    </td>
                    <td class="contain">
                        <html:password property="dto(password)" styleClass="shortText" maxlength="20" tabindex="2"
                                       redisplay="false" style="width" styleId="passwordTextId"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"><%= JSPHelper.getMessage(defaultLocale, "Logon.passwordCia")%>
                    </td>
                    <td class="contain">
                        <html:text property="dto(companyLogin)" styleClass="shortText" maxlength="20"
                                   tabindex="3" value='<%= (lastCompany!=null) ? lastCompany : "" %>'
                                   styleId="companyTextId"/>
                    </td>
                </tr>

                <tr align="center" class="mainTitle">
                    <td colspan="2" class="logonButton">
                        <html:submit styleClass="button"
                                     tabindex="4"><%= JSPHelper.getMessage(defaultLocale, "Common.logon")%>
                        </html:submit>
                    </td>
                </tr>
                <tr align="center">
                    <td colspan="2">
                        <font color="#FFFFFF">
                            <html:hidden property="dto(language)" value="${language}"/>
                            <c:if test="${not empty param['homePage']}">
                                <input type="hidden" name="homePage" value="1"/>
                                <c:set var="homePageParam" value="&homePage=1"/>
                                <c:if test="${empty applicationScope.homeSitePage}">
                                    <c:set var="homeSitePage" value="1" scope="application"/>
                                </c:if>
                                <script type="text/javascript">
                                    jQuery.ajaxSetup({contentType: "application/x-www-form-urlencoded; charset=UTF-8"});

                                    $(document).ready(function() {
                                        var options = {
                                            beforeSubmit: showLoading,
                                            success:       processLoginResponse
                                        };
                                        $("#loginForm").ajaxForm(options);
                                    });
                                </script>
                            </c:if>
                            <input tabindex="5" name="dto(rememberInfo)" type="checkbox"
                                   value="remember" <%= (lastUser!=null & lastCompany!=null) ? "checked" : "" %>
                                   id="rememberInfoId">
                            <label for="rememberInfoId"><%= JSPHelper.getMessage(defaultLocale, "Logon.rememberInfo")%>
                            </label>
                        </font>
                    </td>
                </tr>

                <c:if test="${'bootstrap' == param.uiMode}">
                    <input type="hidden" name="isBootstrapUI" value="true"/>
                    <c:set var="isBootstrapUIParam" value="&uiMode=bootstrap"/>
                </c:if>
            </html:form>
        </table>

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
    <div class="downloadLink">
        <a href="${pageContext.request.contextPath}/download/BMWordPlugin-3.1.2.exe">
            <%=  JSPHelper.getMessage(defaultLocale, "Common.download.Word_Plugin", "version 3.1.2")%>
        </a>
    </div>
</div>
