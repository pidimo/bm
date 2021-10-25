<%@ page session="false" %>
<%@ include file="/Includes.jsp" %>

<%@ include file="layout/ui/mobileMimeType.jsp" %>

<%


    String locale = request.getParameter("locale");
    Locale defaultLocale = request.getLocale();
    if (locale != null) {
        defaultLocale = new Locale(locale);
    }
    pageContext.setAttribute("language", defaultLocale.getLanguage());


%>

<html:html locale="true" xhtml="true">
    <head>
        <title><%= JSPHelper.getMessage(defaultLocale, "Logon.Title")%>
        </title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/mobile.css"/>">
    </head>

    <body style="margin:0">

    <div class="body">

        <c:choose>
            <c:when test="${param.expired != null}">
                <div class="error">
                    <%= JSPHelper.getMessage(defaultLocale, "Common.sessionExpired")%>
                </div>
            </c:when>
            <c:otherwise>
                <c:import url="/mobile/layout/ui/error.jsp"/>
            </c:otherwise>
        </c:choose>

        <c:url value="/mobile/Logon.do?dto(language)=${language}" var="logonUrl"/>

        <form action="${logonUrl}" method="post">

            <div class="row">
                <div class="label"><%= JSPHelper.getMessage(defaultLocale, "Logon.username")%>:</div>
                <div class="content">
                    <input type="text" name="dto(login)" tabindex="1"
                           value="${app2:filterForHtml(param['dto(login)'])}"/>
                </div>
            </div>

            <div class="row">
                <div class="label"><%= JSPHelper.getMessage(defaultLocale, "Logon.password")%>:</div>
                <div class="content">
                    <input type="password" name="dto(password)" tabindex="2"/>
                </div>
            </div>

            <div class="row">
                <div class="label"><%= JSPHelper.getMessage(defaultLocale, "Logon.passwordCia")%>:</div>
                <div class="content">

                    <input type="text" name="dto(companyLogin)" tabindex="3"
                           value="${app2:filterForHtml(param['dto(companyLogin)'])}"/>

                </div>
            </div>


            <div class="row">
                <input type="hidden" name="dto(language)" value="${language}"/>
                <input type="hidden" name="dto(mobile)" value="true"/>
                <input type="submit" value='<%= JSPHelper.getMessage(defaultLocale, "Common.logon")%>'/>
            </div>
        </form>
    </div>
    </body>
</html:html>
