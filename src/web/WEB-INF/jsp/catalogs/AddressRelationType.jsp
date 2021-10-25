<%@ include file="/Includes.jsp" %>
<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <html:hidden property="dto(op)" value="${op}"/>
                    <%--if update action or delete action--%>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(relationTypeId)"/>
                </c:if>
                    <%--for the version control if update action--%>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                    <%--for the control referencial integrity if delete action--%>
                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="title_id">
                        <fmt:message key="AddressRelationType.title"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(title)" styleId="title_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="60"
                                  view="${'delete' == op}" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="relationType_id">
                        <fmt:message key="AddressRelationType.relationType"/>
                        <c:set var="relationTypes" value="${app2:getAddressRelationTypeTypeList(pageContext.request)}"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <html:select property="dto(relationType)" styleId="relationType_id"
                                     styleClass="${app2:getFormSelectClasses()} middleSelect"
                                     tabindex="2"
                                     readonly="${'delete' == op}">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="relationTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="ADDRESSRELATIONTYPE" tabindex="5"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="ADDRESSRELATIONTYPE"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="6"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="addressRelationTypeForm"/>