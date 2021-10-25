<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(op)" value="${op}"/>
                    <%--if update action or delete action--%>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(campaignTypeId)"/>
                </c:if>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="CampaignType.title"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete'==op)}">
                        <app:text property="dto(title)" styleClass="${app2:getFormInputClasses()} text" maxlength="20"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CAMPAIGNTYPE"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="CAMPAIGNTYPE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="campaignTypeForm"/>