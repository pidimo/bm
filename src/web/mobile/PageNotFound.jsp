<%@ include file="/mobile/layout/ui/mobileMimeType.jsp" %>
<%@ include file="/Includes.jsp" %>
<html:html xhtml="true">
    <head>
        <title>
            <fmt:message key="Common.pageNotFound"/>
        </title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/mobile.css"/>"/>
    </head>

    <body>
    <div class="page">
        <div class="error">
            <fmt:message key="msg.pageNotFoundError"/>
        </div>
    </div>
    </body>
</html:html>