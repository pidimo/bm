<%@ page language="java" errorPage="/WEB-INF/jsp/JSPErrorPage.jsp" %>

<%@ include file="/Includes.jsp" %>
<!DOCTYPE html>
<html>

<%--firts evaluate before write header, this is required to initialize some script, like html editor--%>
<c:import url="/WEB-INF/jsp/layout/ui/header.jsp" var="headerFragment"/>
<c:import url="/WEB-INF/jsp/layout/ui/ModuleMenuLayout.jsp" var="moduleMenuFragment"/>

<c:if test="${tabs != null}">
    <c:import url="/WEB-INF/jsp/layout/ui/DetailTabs.jsp" var="tabsFragment"/>
</c:if>

<c:choose>
    <c:when test="${param['errorPage'] == 'true'}">
        <c:import url="/WEB-INF/jsp/layout/ui/simpleerror.jsp" var="errorFragment"/>
    </c:when>
    <c:otherwise>
        <c:import url="/WEB-INF/jsp/layout/ui/generalerror.jsp" var="errorFragment"/>
    </c:otherwise>
</c:choose>

<c:if test="${body != null}">
    <c:import url="${body}" var="bodyFragment"/>
</c:if>

<head>
        <c:import url="/WEB-INF/jsp/layout/ui/head.jsp"/>
        <title>
            <c:if test="${windowTitle != null}">
                <fmt:message key="${windowTitle}"/>
            </c:if>
        </title>
    </head>
    <body class="bg-body" id="body" <c:out value="${jsLoad}" escapeXml="false"/>>
        <div>
                <div>
                    <c:out value="${headerFragment}" escapeXml="false"/>
                </div>

                <c:out value="${moduleMenuFragment}" escapeXml="false"/>
            <c:if test="${not empty bodyHeaderFragment}">
                <c:out value="${bodyHeaderFragment}" escapeXml="false"/>
            </c:if>

            <c:out value="${tabsFragment}" escapeXml="false"/>

            <div class="container">
                <c:out value="${errorFragment}" escapeXml="false"/>
            </div>

            <c:set var="bodyContainerClass" value="container main-container"/>
            <c:if test="${'true' eq isBodyInAllWidth}">
                <c:set var="bodyContainerClass" value=""/>
            </c:if>
            <div class="${bodyContainerClass}">
                <c:out value="${bodyFragment}" escapeXml="false"/>
            </div>
        </div>
    <!--@@webmailNotifierJspFragment@@-->

        <%--bootstrap reaponsive tabs addon--%>
    <script type="text/javascript">
        $(document).ready(function() {
            //dropdown exceeded tabs
            $('.nav-tabs').tabdrop({text:'<i class="glyphicon glyphicon-align-justify"></i>'});
            //dropdown exceeded menuBar collapce
            $('.myUL1').menuResponsive({container:'.manuCustonCollapce', UL1:'#myUL1', ul1List:'#myUL1>li'});
        });
        //initiating jQuery

        $(document).ready(function(){
            if($( window ).width()>768){
                $("#stickUpNavBar").removeClass("navbar-fixed-top");
            }else{
                $("#stickUpNavBar").addClass("navbar-fixed-top");
            }
        });
        $(window).resize(function(){
            if($( window ).width()>768){
                $("#stickUpNavBar").removeClass("navbar-fixed-top");
            }else{
                $("#stickUpNavBar").addClass("navbar-fixed-top");
            }
        });
    </script>
    </body>
    <c:remove var="jsLoad" scope="session"/>
</html>
