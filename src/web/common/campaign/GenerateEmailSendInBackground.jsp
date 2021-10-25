<%@ include file="/Includes.jsp" %>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="location.href='${urlGenerateEmailReturn}'">
                ${button}
            </html:button>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="2" class="title">
            <fmt:message key="Campaign.emailSend.inBackground.title"/>
        </td>
    </tr>
    <tr>
        <td class="label2" colspan="2">
            <fmt:message key="Campaign.emailSend.inBackground">
                <fmt:param value="${notificationMail}"/>
            </fmt:message>
        </td>
    </tr>
</table>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="location.href='${urlGenerateEmailReturn}'">
                ${button}
            </html:button>
        </td>
    </tr>
</table>
