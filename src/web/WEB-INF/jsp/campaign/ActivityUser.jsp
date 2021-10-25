<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(userName)" styleClass="form-horizontal">


    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${ op=='update' || op=='delete' }">
        <html:hidden property="dto(activityId)"/>
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(version)"/>

        <!--only to preserve automatic assign criteria-->
        <html:hidden property="dto(homogeneously)"/>
        <html:hidden property="dto(customerPriority)"/>
    </c:if>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.activity.campContact.assign.responsible"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:text property="dto(userName)" styleClass="middleText" maxlength="40" tabindex="1" view="true"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="percent_id">
                        <fmt:message key="Campaign.activity.user.percent"/>
                        (<fmt:message key="Common.probabilitySymbol"/>)
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:text property="dto(percent)"
                                  styleId="percent_id"
                                  styleClass="numberText ${app2:getFormInputClasses()}" maxlength="40" tabindex="2"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit property="dto(${op})" styleClass="button ${app2:getFormButtonClasses()}" tabindex="3">
            <c:out value="${button}"/>
        </html:submit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="4">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>
<tags:jQueryValidation formName="activityUserForm"/>
