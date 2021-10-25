<%@ include file="/Includes.jsp" %>
<c:if test="${op == 'create' || op=='update'}">
    <tags:initJQueryMiniColors/>
</c:if>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">

            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="AppointmentType.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(name)"
                                  styleClass="${app2:getFormInputClasses()} largetext"
                                  maxlength="20"
                                  view="${'delete' == op}"
                                  tabindex="1"
                                  styleId="name_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="color">
                        <fmt:message key="AppointmentType.color"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)} ">
                        <tags:jQueryMiniColors property="dto(color)"
                                               styleId="color"
                                               view="${'delete' == op}"
                                               tabIndex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op=='update'}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="3">
                    <fmt:message key="Common.save"/>
                </html:submit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="4">
                    <fmt:message key="Common.delete"/>
                </html:submit>
            </c:if>
            <c:if test="${op == 'create'}">
                <html:submit property="SaveAndNew" styleClass="${app2:getFormButtonClasses()}" tabindex="3">
                    <fmt:message key="Common.saveAndNew"/>
                </html:submit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="5">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${op=='update' || op=='delete'}">
            <html:hidden property="dto(appointmentTypeId)"/>
            <html:hidden property="dto(version)"/>
        </c:if>

    </div>
</html:form>
<tags:jQueryValidation formName="appointmentTypeForm"/>