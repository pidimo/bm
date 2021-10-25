<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
<td>
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td height="20" class="title">
            <%--<fmt:message   key="Campaign.msg.SuccessSendEmails"/>--%>
            </td>
        </tr>
<%--        <tr>
            <td align="center" >
            </br>
            <table width="70%" >
            <tr><td><fmt:message   key="Campaign.msg.FailedRecipients"/>
            </td></tr>
            </table>
            </br>
            </td>
        </tr>--%>
        <tr>
            <td>

            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="searchContainer">

                <tr >
                    <td class="contain" >
                    <h3><fmt:message  key="Campaign.msg.SuccessSendEmails"/></h3>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
        <tr>
        <td align="center" >
        </br>
        <%--<html:link action="/Campaign/Cancel" ><fmt:message   key="Common.cancel"/></html:link>--%>
        <html:button styleClass="button" property="" onclick="window.close()" ><fmt:message   key="Common.close"/></html:button>
        </td>
        </tr>
    </table>
<c:remove var="noHasEmail" />