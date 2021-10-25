<%@ include file="/mobile/layout/ui/mobileMimeType.jsp" %>
<%@ include file="/Includes.jsp" %>

<html:html xhtml="true">
    <head>
        <title>
            <c:if test="${windowTitle != null}">
                <fmt:message key="${windowTitle}"/>
            </c:if>
        </title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/mobile.css"/>"/>
    </head>

    <body>

    <div class="page">


        <div class="body ${not empty tabs ? 'tabBody' : ''}">
            <c:if test="${not empty tabs}">
                <c:import url="/mobile/layout/tiles${tabs}" var="tabsMenu"/>
                <c:out value="${tabHeader}" escapeXml="false"/>
            </c:if>


            <c:import url="/mobile/layout/ui/error.jsp"/>


            <c:if test="${body != null}">
                <c:import url="${body}"/>
            </c:if>
            <c:if test="${not empty tabs}">
                <c:out value="${tabsMenu}" escapeXml="false"/>
            </c:if>

        </div>
        <c:if test="${not empty body}">
            <hr/>
        </c:if>

        <c:if test="${shortcuts != null}">
            <div class="shortcuts">
                <c:import url="/mobile/layout/tiles${shortcuts}"/>
                <hr/>
            </div>
        </c:if>

        <c:import url="/mobile/layout/ui/moduleMenu.jsp"/>
        <hr/>
        <c:import url="/mobile/layout/ui/footer.jsp"/>
    </div>
    </body>
</html:html>