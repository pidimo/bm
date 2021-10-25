<%@ include file="/Includes.jsp" %>
<div class="${app2:getFormClassesLarge()}">
    <html:form action="/Notification/Update.do" focus="dto(notificationAppointmentEmail)" styleClass="form-horizontal">
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(companyId)"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE"
                                 functionality="USERSETTINGS"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="6">${save}</app2:securitySubmit>
                <%--<html:submit styleClass="button" tabindex="6" >
                  <fmt:message key="Common.save" />
                </html:submit>--%>
        </div>

        <div class="${app2:getFormPanelClasses()}">

            <legend class="title">
                <fmt:message key="Admin.Notification"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Admin.AppointmentEmail"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(notificationAppointmentEmail)"
                              styleClass="largeText ${app2:getFormInputClasses()}"
                              maxlength="200"
                              tabindex="3"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Admin.TaskEmail"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(notificationSchedulerTaskEmail)"
                              styleClass="largeText ${app2:getFormInputClasses()}"
                              maxlength="200"
                              tabindex="3"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Admin.CaseEmail"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(notificationSupportCaseEmail)"
                              styleClass="largeText ${app2:getFormInputClasses()}" maxlength="200"
                              tabindex="3"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Admin.QuestionEmail"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <app:text property="dto(notificationQuestionEmail)" styleClass="largeText ${app2:getFormInputClasses()}"
                              maxlength="200"
                              tabindex="3"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <div class="col-xs-12">
                    <fmt:message key="Admin.messageMail"/><br>
                    <fmt:message key="Admin.comma"/>
                </div>
            </div>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE"
                                 functionality="USERSETTINGS"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="6">${save}</app2:securitySubmit>
                <%--<html:submit styleClass="button" tabindex="6" >
                  <fmt:message key="Common.save" />
                </html:submit>--%>
        </div>
    </html:form>
</div>


