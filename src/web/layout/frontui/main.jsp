<%@ page import="com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher" %>
<%@ page language="java" errorPage="/JSPErrorPage.jsp" %>
<%@ include file="/Includes.jsp" %>
<%
    String locale = request.getParameter("locale");
    Locale defaultLocale = request.getLocale();
    if (locale != null) {
        defaultLocale = new Locale(locale);
    }
    pageContext.setAttribute("language", defaultLocale.getLanguage());
%>
<html:html locale="true">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="expires" content="0">
        <title>
            <fmt:message key="Common.applicationName"/>
        </title>
        <link rel="STYLESHEET" type="text/css" href="<%= request.getContextPath() %>/style.css">
        <link rel="icon" href="<c:url value="/layout/ui/img/bmIcon.gif"/>" type="image/gif"/>
            <%--Allowing to override the login/external page(s) background color--%>
        <style type="text/css">
            BODY {
                background-color: <c:out value="${app2:getConfigurationPropertyValue('elwis.loginPage.backgroundColor')};"/>
            }

            TD.title {
                background-color: <c:out value="${app2:getConfigurationPropertyValue('elwis.loginPage.backgroundColor')};"/>
            }
        </style>

    </head>

    <body style="margin:0;padding:0;text-align:center;"
            <c:out value="${jsLoad}" escapeXml="false"/>
            >

    <div align="center">

        <c:set var="errorContainerOutput" scope="request">
            <c:choose>
                <c:when test="${param.expired != null}">
                    <!--<div id="errorContainer" align="center">-->
                    <div align="center">
                        <table cellpadding="0" cellspacing="0" align="center" class="expiredMessage">
                            <tr>
                                <td>
                                    <%= JSPHelper.getMessage(defaultLocale, "Common.sessionExpired")%>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <!--</div>-->
                </c:when>
                <c:when test="${hasErrors=='true'}">
                    <!--<div id="errorContainer" align="center">-->
                    <div class="center">
                        <table cellpadding="0" cellspacing="0" align="center" class="expiredMessage">
                            <tr>
                                <td>
                                    <html:messages id="message" message="false">
                                        <bean:define id="messageHtml" name="message"/>
                                        <% out.println(messageHtml);%>
                                    </html:messages>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <!--</div>-->
                </c:when>
                <c:otherwise>
                    <logic:messagesPresent message="false">
                        <!--<div id="errorContainer" align="center">-->
                        <div class="center">
                            <TABLE border="0" cellpadding="3" cellspacing="0" align="center" width="100%">
                                <c:import url="/layout/ui/simpleerror.jsp"/>
                            </TABLE>
                        </div>
                        <!--</div>-->
                    </logic:messagesPresent>
                </c:otherwise>
            </c:choose>
        </c:set>
        <div id="errorContainer" align="center">
            <c:choose>
                <c:when test="${not empty param['message'] and empty errorContainerOutput}">
                    <% try {
                        out.print(UrlEncryptCipher.i.decrypt(request.getParameter("message")));
                    } catch (Exception e) {
                        //nothing to print in this case.
                    }
                    %>
                </c:when>
                <c:otherwise>
                    <c:out value="${errorContainerOutput}" escapeXml="false"/>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="center">
            <c:import url="${body}"/>
        </div>
        <div class="copyright">
            <font style="font-weight:bold;"><fmt:message key="Common.applicationName"/></font> <br> @@VERSION@@
        </div>
        <div class="copyright" style="border:0;">
            <jsp:useBean id="currentDate" class="java.util.Date" scope="page"/>
            &#169; Copyright 2004 -
            <fmt:formatDate value="${currentDate}" pattern="yyyy"/><br/>
                ${app2:getConfigurationPropertyValue('elwis.copyright.html')}
        </div>

    </div>
    </body>
    <c:remove var="jsLoad" scope="session"/>

</html:html>

