<%@ include file="/Includes.jsp" %>

<html:form action="/ReportRole/Delete.do" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(roleId)"/>
        <html:hidden property="dto(reportId)"/>
        <html:hidden property="dto(op)" value="${op}"/>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="ReporRole.name"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                            ${reportRoleDeletedForm.dtoMap['roleName']}
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="REPORTROLE" permission="DELETE">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </html:submit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
