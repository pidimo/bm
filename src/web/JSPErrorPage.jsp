<%@ include file="/Includes.jsp" %>
<c:set var="windowTitle" value="Common.globalError" scope="request"/>




<html:html locale="true">
    <head>
        <title>
            <fmt:message key="Common.globalError"/>
        </title>
        <c:import url="/layout/ui/AppStyleCSS.jsp"/>
    </head>

    <body topmargin="0" leftmargin="0">
    <br>
    <br>
    <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <TR>
            <td valign="top" align="center">
                <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">


                    <tr>
                        <td height="100%" valign="top" align="center">

                            <table width="50%" align="center" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td align="left" nowrap class="error">
                                        <table width="95%" align="center" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td colspan="2">
                                                    <span class="error">
                                                        <fmt:message key="Common.globalError"/>
                                                    </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="left" valign="top" width="10">
                                                    <img src="<c:out value="${sessionScope.baselayout}"/>/img/arrowerror.gif"
                                                         align="top" alt=""/>
                                                </td>
                                                <td align="rigth">
                                                    <fmt:message key="msg.ServerError"/>
                                                </td>
                                            </tr>
                                        </table>

                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </TR>
    </TABLE>
    </body>
</html:html>



