<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="button">
                        <html:button property="" onclick="window.close();" styleClass="button">
                            <fmt:message key="Common.close"/>
                        </html:button>
                    </td>
                </tr>
                <tr>
                    <td height="20" class="title">
                        <fmt:message key="SequenceRule.format.formatHelp"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
                            <TR>
                                <td class="label">NNNNNNNNNN</td>
                                <td class="contain">
                                    <fmt:message key="SequenceRule.help.CustomerNumberFormat"/>
                                </td>
                            </TR>
                        </table>
                    </td>
                </tr>
            </table>
</TABLE>