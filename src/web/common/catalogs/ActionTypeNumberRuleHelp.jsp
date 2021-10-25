<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td>
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td class="button">
                <html:button  property="" onclick="window.close();" styleClass="button" >
                    <fmt:message    key="Common.close"/>
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
                        <td class="label" width="20%">YYYY</td>
                        <td class="contain" height="80%">
                            <fmt:message key="SequenceRule.help.currentYear"/>
                        </td>
                    </TR>
                    <TR>
                        <td class="label">YY</td>
                        <td class="contain">
                            <fmt:message key="SequenceRule.help.lastTwoDigitsYear"/>
                        </td>
                    </TR><TR>
                        <td class="label">MM</td>
                        <td class="contain">
                            <fmt:message key="SequenceRule.help.currentMonth"/>
                        </td>
                    </TR><TR>
                        <td class="label">DD</td>
                        <td class="contain">
                            <fmt:message key="SequenceRule.help.currentDayOfMonth"/>
                        </td>
                    </TR><TR>
                        <td class="label">NNNNNNNNNN</td>
                        <td class="contain">
                            <fmt:message key="SequenceRule.help.ActionTypeNumber"/>
                        </td>
                    </TR><TR>
                        <td class="label">CCCCCCCCCC</td>
                        <td class="contain">
                            <fmt:message key="SequenceRule.help.customerNumber"/>
                        </td>
                    </TR><TR>
                        <td class="label">/ - _</td>
                        <td class="contain">
                            <fmt:message key="SequenceRule.help.separators"/>
                        </td>
                    </TR>
                </table>
            </td>
        </tr>
        <tr>
            <td height="20" class="title">
                <fmt:message key="SequenceRule.format.titleExample"/>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="SequenceRule.format.example"/>
            </td>
        </tr>
    </table>
</TABLE>