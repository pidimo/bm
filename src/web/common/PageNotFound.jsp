<%@ include file="/Includes.jsp" %>

<html:html locale="true">
    <head>
        <title>
            <fmt:message key="Common.pageNotFound"/>
        </title>
        <c:import url="/layout/ui/AppStyleCSS.jsp"/>
    </head>

    <body>
    <br>
    <br>
    <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <TR>
            <td valign="top" align="center">
                <TABLE width="80%" height="100%" border="0" cellpadding="0" cellspacing="0">


                    <tr>
                        <td height="100%" valign="top" align="center">

                            <table width="95%" align="center" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td align="center" class="error">
                                        <div style="text-align:center;">
                                        <span class="error" style="text-align:center;font-size:20px;">
                                                          <fmt:message key="Common.pageNotFound"/>
                                          </span>
                                        </div>

                                        <div align="center" style="font-size:17px;padding:10px;">
                                            <fmt:message key="msg.pageNotFoundError"/>
                                        </div>
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



