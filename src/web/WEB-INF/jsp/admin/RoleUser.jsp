<%@ include file="/Includes.jsp" %>
<div class="${app2:getFormClasses()}">
    <html:form action="/RoleUser/Delete.do" styleClass="form-horizontal">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(roleId)"/>
        <html:hidden property="dto(userId)"/>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Contact.Person.lastname"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                            ${dto.name1}
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Contact.Person.firstname"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                            ${dto.name2}
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
                <%--<html:submit styleClass="button">${button}</html:submit>--%>
            <app2:securitySubmit operation="UPDATE" functionality="USER"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </html:form>
</div>

