<%@ include file="/Includes.jsp" %>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="/ReportRole/DeleteSelectedElements.do">
                <html:hidden property="dto(reportId)" value="${param.reportId}"/>

                <html:hidden property="dto(op)" value="${op}"/>

                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">
                    <TR>
                        <TD class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="contain"><fmt:message key="ReporRole.name"/></td>
                    </tr>
                    <c:forEach var="role" items="${selectedRoles}">
                    <tr>
                        <td class="label">${role.roleName}</td>
                        <html:hidden property="dto(roleId_${role.roleId})" value="${role.roleId}"/>
                    </tr>
                    </c:forEach>
                    <tr>
                        <td class="button" colspan="2">
                            <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                                <html:submit property="dto(delete)" styleClass="button">${button}</html:submit>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </td>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>