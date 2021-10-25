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

<!DOCTYPE html>
<html>
<head>

    <c:import url="/WEB-INF/jsp/layout/ui/head.jsp"/>
    <%--add custom css--%>
    <link rel="stylesheet" href="<c:url value="/css/frontui.css"/>"/>

    <title>
        <c:choose>
            <c:when test="${windowTitle != null}">
                <fmt:message key="${windowTitle}"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="Common.applicationName"/>
            </c:otherwise>
        </c:choose>
    </title>
</head>

<body class="bg-body" <c:out value="${jsLoad}" escapeXml="false"/>>

    <c:set var="errorContainerOutput" scope="request">
        <c:choose>
            <c:when test="${param.expired != null}">
                <!--<div id="errorContainer" align="center">-->
                <div class="col-md-8 col-md-offset-2">
                    <div class="alert alert-danger alert-dismissible fade in" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        &nbsp;
                        <%= JSPHelper.getMessage(defaultLocale, "Common.sessionExpired")%>
                    </div>
                </div>

                <!--</div>-->
            </c:when>
            <c:when test="${hasErrors=='true'}">
                <!--<div id="errorContainer" align="center">-->

                <div class="col-md-8 col-md-offset-2">
                    <div class="alert alert-danger alert-dismissible fade in" role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        &nbsp;

                        <html:messages id="message" message="false">
                            <bean:define id="messageHtml" name="message"/>
                            <% out.println(messageHtml);%>
                        </html:messages>
                    </div>
                </div>

                <!--</div>-->
            </c:when>
            <c:otherwise>
                <logic:messagesPresent message="false">
                    <!--<div id="errorContainer" align="center">-->
                    <div class="container">
                        <c:import url="/WEB-INF/jsp/layout/ui/simpleerror.jsp"/>
                    </div>
                    <!--</div>-->
                </logic:messagesPresent>
            </c:otherwise>
        </c:choose>
    </c:set>

    <div>
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

        <div class="container">
            <c:import url="${body}"/>
        </div>

        <c:import url="/WEB-INF/jsp/layout/ui/footer.jsp"/>

    </div>
</body>
<c:remove var="jsLoad" scope="session"/>

</html>

