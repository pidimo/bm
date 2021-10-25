<%@ page import="java.util.Map,
                 java.util.HashMap"%>
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
            <fmt:message   key="Campaign.msg.confirmationGenerate"/>
            </td>
        </tr>
        <tr>
            <td align="center" >
            <br>
            <table width="70%" >
            <tr><td><fmt:message   key="Campaign.msg.Generate"/>
            </td></tr>
            </table>
            <br>
            </td>
        </tr>
        <tr>
            <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="searchContainer">
                <tr>

                    <td align="center" >
                        <a href="javascript:window.close()"><fmt:message   key="Common.close"/></a>
                        <%--<html:link action="/Campaign/Generate" name="recipientForm" property="dtoMap">
                        <fmt:message   key="Campaign.generate"/>
                        </html:link>--%>
                    </td>
                    <td align="center">

                        <html:link action="/Campaign/Cancel" ><fmt:message   key="Common.cancel"/></html:link>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
    </table>
    </td>
    </tr>
</table>
