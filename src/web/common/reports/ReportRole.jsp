<%@ include file="/Includes.jsp" %>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="/ReportRole/Delete.do">
                <html:hidden property="dto(roleId)"/>
                <html:hidden property="dto(reportId)"/>
                <html:hidden property="dto(op)" value="${op}"/>

                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">
                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="label" width="40%"><fmt:message key="ReporRole.name"/></td>
                        <td class="contain" width="60%">${reportRoleDeletedForm.dtoMap['roleName']}</td>
                    </tr>
                    <tr>
                        <td class="button" colspan="2">
                            <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                                <html:submit
                                        styleClass="button">${button}</html:submit>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </td>
                    </tr>
                </table>

            </html:form>
        </td>
    </tr>
</table>