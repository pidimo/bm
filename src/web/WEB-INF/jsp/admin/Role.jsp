<%@ include file="/Includes.jsp" %>

<c:set var="path" value="${pageContext.request.contextPath}"/>

<div class="${app2:getFormClasses()}">
    <html:form action="${action}" focus="dto(roleName)" styleClass="form-horizontal">
        <div id="Role.jsp" class="${app2:getFormPanelClasses()}">
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <c:if test="${op == 'update'}">

                <html:hidden property="dto(version)"/>
            </c:if>
            <c:if test="${op != 'create'}">
                <html:hidden property="dto(roleId)"/>
            </c:if>

            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="roleName_id">
                    <fmt:message key="Role.name"/>
                </label>

                <div class="${app2:getFormContainClasses(op == 'delete')}">
                    <app:text property="dto(roleName)"
                              styleId="roleName_id"
                              styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="80"
                              tabindex="1"
                              view="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="roleDescription_id">
                    <fmt:message key="Role.description"/>
                </label>

                <div class="${app2:getFormContainClasses(op == 'delete')}">
                    <html:textarea property="dto(roleDescription)"
                                   styleId="roleDescription_id"
                                   styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                   style="height:120px;width:99%;"
                                   tabindex="2"
                                   readonly="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="ROLE"
                                     styleClass="button ${app2:getFormButtonClasses()}" property="dto(save)" tabindex="3">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="ROLE"
                                         styleClass="button ${app2:getFormButtonClasses()}" property="SaveAndNew"
                                         tabindex="4">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>

                </c:if>
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="5"><fmt:message
                        key="Common.cancel"/></html:cancel>
            </div>
        </div>
    </html:form>
    <tags:jQueryValidation formName="roleForm"/>
</div>



