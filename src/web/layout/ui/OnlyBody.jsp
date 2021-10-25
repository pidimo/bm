<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/JSPErrorPage.jsp" %>
<%@ include file="/Includes.jsp" %>
<html:html locale="true">
    <head>
        <meta http-equiv="expires" content="0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <c:out value="${meta_download}" escapeXml="false"/>
        <title>

            <c:if test="${windowTitle != null}">
                <fmt:message key="${windowTitle}"/>
            </c:if>

        </title>
        <c:import url="/layout/ui/AppStyleCSS.jsp"/>
    </head>

    <body style="margin:0"
            <c:out value="${jsLoad}" escapeXml="false"/>
            >
    <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <TR>
            <td valign="top" align="center">
                <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                    <c:import url="/layout/ui/simpleerror.jsp"/>
                    <tr>
                        <td height="100%" valign="top">
                            <c:catch>
                                <c:import url="${body}"/>
                            </c:catch>
                            <br>
                        </td>
                    </tr>
                </table>
            </td>
        </TR>
    </TABLE>
    </body>
</html:html>

