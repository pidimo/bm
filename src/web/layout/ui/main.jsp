<%--<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/JSPErrorPage.jsp" %>--%>
<%@ page language="java" errorPage="/JSPErrorPage.jsp" %>

<%@ include file="/Includes.jsp" %>

<html:html locale="true">
    <%--firts evaluate before write header, this is required to initialize some script, like html editor--%>
    <c:import url="/layout/ui/header.jsp" var="headerFragment"/>
    <c:import url="/layout/ui/ModuleMenuLayout.jsp" var="moduleMenuFragment"/>

    <c:if test="${tabs != null}">
        <c:import url="/layout/tiles${tabs}" var="tabsFragment"/>
    </c:if>
    <c:choose>
        <c:when test="${param['errorPage'] == 'true'}">
            <c:import url="/layout/ui/simpleerror.jsp" var="errorFragment"/>
        </c:when>
        <c:otherwise>
            <c:import url="/layout/ui/generalerror.jsp" var="errorFragment"/>
        </c:otherwise>
    </c:choose>

    <c:if test="${body != null}">
        <c:import url="${body}" var="bodyFragment"/>
    </c:if>

    <head>
        <meta http-equiv="expires" content="0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>

            <c:if test="${windowTitle != null}">
                <fmt:message key="${windowTitle}"/>
            </c:if>
        </title>

        <link rel="icon" href="<c:url value="/layout/ui/img/bmIcon.gif"/>" type="image/gif"/>
            <%--Insert the CSS styles available for the application--%>
        <c:import url="/layout/ui/AppStyleCSS.jsp"/>
        <tags:jscript language="JavaScript" src="/js/pulldownmenu.js"/>
            <%--pull down menu script--%>
        <tags:jQuery/>


            <%--write tinyMCE init scripts--%>
        <c:if test="${not empty initHtmlEditorItems}">
            <c:forEach items="${initHtmlEditorItems}" var="item">
                <c:out value="${item.value}" escapeXml="false"/>
            </c:forEach>
        </c:if>

        <%--write tinyMCE 4 init scripts--%>
        <c:if test="${not empty tinyMCE4EditorItems}">
            <c:forEach items="${tinyMCE4EditorItems}" var="item">
                <c:out value="${item.value}" escapeXml="false"/>
            </c:forEach>
        </c:if>

    </head>

    <body style="margin:0;padding:0;"
            <c:out value="${jsLoad}" escapeXml="false"/>
            >


    <TABLE style="width:100%;height:100%;margin:0;padding:0;">
        <TR>
            <TD valign="top" height="23">
                <c:out value="${headerFragment}" escapeXml="false"/>
            </TD>
        </TR>
        <c:out value="${moduleMenuFragment}" escapeXml="false"/>
            <%--insert the rows of tab modules and shortcuts--%>

        <TR>
            <TD height="100%">
                <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                    <TR>
                        <td valign="top" align="center" class="principal" style="background-color:transparent;">

                            <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                                <c:out value="${tabsFragment}" escapeXml="false"/>
                                <c:out value="${errorFragment}" escapeXml="false"/>

                                <tr>
                                    <td height="100%" valign="top">
                                        <c:choose>
                                            <c:when test="${index != null && param['errorPage'] == null}">
                                                <table width="100%" align="center" cellpadding="0" cellspacing="0"
                                                       height="98%">
                                                    <tr>
                                                        <td
                                                                <c:if test="${!hasNoSubMenuTabs}">class="folderTabBorder
                                                                    folderTabContent"
                                                        </c:if> style="border-top:0px;">
                                                            <br/>
                                                            <c:out value="${bodyFragment}" escapeXml="false"/>
                                                            <br/>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </c:when>
                                            <c:otherwise>
                                                <br/>
                                                <c:out value="${bodyFragment}" escapeXml="false"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </TABLE>
                            <br/>
                        </td>
                    </TR>
                </TABLE>
            </td>
        </tr>
    </TABLE>

    <!--@@webmailNotifierJspFragment@@-->
    </body>
    <c:remove var="jsLoad" scope="session"/>
</html:html>