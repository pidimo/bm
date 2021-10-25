<%@ include file="/Includes.jsp" %>
<table class="contain" width="100%">
    <tr>
        <td align="center">
        <fmt:message key="Webmail.registerUserMail"/>
        <%--webmailuserSettings--%>
        <app:link page="/admin/NewUserMail.do?module=webmail&dto(userId)=${user.valueMap['userId']}&dto(companyId)=${user.valueMap['companyId']}" contextRelative="true" addModuleParams="false" addModuleName="false">
            <font color="#353C54"><fmt:message   key="Webmail.Common.users"/></font>
        </app:link>
        <%--webmailuserSettings--%>
        </td>
    </tr>
</table>