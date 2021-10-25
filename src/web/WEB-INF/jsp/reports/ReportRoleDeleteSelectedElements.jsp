<%@ include file="/Includes.jsp" %>

<html:form action="/ReportRole/DeleteSelectedElements.do" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(reportId)" value="${param.reportId}"/>

        <html:hidden property="dto(op)" value="${op}"/>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="control-label col-xs-12 col-sm-4">
                        <fmt:message key="ReporRole.name"/>
                    </label>

                    <div class="col-xs-12 col-sm-8 paddingTop">
                        <c:forEach var="role" items="${selectedRoles}">
                            <div class="wrapperButton">
                                    ${role.roleName}
                                <html:hidden property="dto(roleId_${role.roleId})" value="${role.roleId}"/>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                <html:submit property="dto(delete)"
                             styleClass="${app2:getFormButtonClasses()}">${button}</html:submit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
