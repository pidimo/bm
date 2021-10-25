<%@ include file="/Includes.jsp" %>
<fmt:message key="datePattern" var="datePattern"/>

<tags:initBootstrapDatepicker/>

<div class="${app2:getFormClasses()}">
    <html:form action="/Reminder/Bulk/Create.do"
               focus="dto(date)"
               styleClass="form-horizontal">

        <c:forEach var="id" items="${invoiceIdList}">
            <html:hidden property="idInvoice" value="${id}" styleId="invoiceId${id}"/>
        </c:forEach>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICEREMINDER"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="1">
                <fmt:message key="Reminder.bulkCreation.button"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Reminder.bulkCreation"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankName_id">
                        <fmt:message key="Reminder.bulkCreation.date"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(date)"
                                          maxlength="10"
                                          styleId="bulkDate"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} dateText"
                                          convert="true"
                                          mode="bootstrap"
                                          currentDate="true"
                                          tabindex="4"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(create)" operation="create" functionality="INVOICEREMINDER"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="6">
                <fmt:message key="Reminder.bulkCreation.button"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </html:form>
</div>