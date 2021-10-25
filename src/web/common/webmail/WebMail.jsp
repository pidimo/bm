<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

<c:set var="systemFolderCounter"
       value="${app2:getSystemFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>
<c:set var="customFoldersList"
       value="${app2:getCustomFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>

<table cellpadding="0" cellspacing="0" align="center" width="100%" border="0" class="container">
    <tr>
        <td valign="top" width="10%" style="padding-right:8px;">
            <app2:checkAccessRight functionality="MAIL" permission="VIEW">
                <table width="100%" cellpadding="0" cellspacing="0" align="left" border="0">
                    <tr>
                        <td><c:import url="/common/webmail/MailFolders.jsp"/></td>
                    </tr>
                    <%--<tr>
                        <td><c:import url="/common/webmail/CustomFolders.jsp"/></td>
                    </tr>--%>
                    <tr>
                        <td><c:import url="/common/webmail/Search.jsp"/></td>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
        <td valign="top" width="90%">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                    <td valign="top" align="center" width="100%">
                        <app2:checkAccessRight functionality="MAIL" permission="VIEW">
                            <c:import url="${mailBody}"/>
                        </app2:checkAccessRight>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
