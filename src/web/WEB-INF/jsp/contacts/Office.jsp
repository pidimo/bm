<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div id="Office.jsp">
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
            <c:if test="${('update' == op) || ('delete' == op)}">
                <html:hidden property="dto(officeId)"/>
            </c:if>
            <c:if test="${('update' == op)}">
                <html:hidden property="dto(version)"/>
            </c:if>
            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                        <c:out value="${title}"/>
                    </legend>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="name_id">
                            <fmt:message key="Contact.Organization.Office.name"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                                <%--      <html:text property="dto(name)" readonly="${op == 'delete'}" styleClass="mediumText" maxlength="15" tabindex="1" />--%>
                            <app:text property="dto(name)" styleId="name_id" view="${op == 'delete'}"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="15"
                                      tabindex="1"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="supervisorId_id">
                            <fmt:message key="Contact.Organization.Office.supervisor"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">   <!-- Collection employees-->
                            <fanta:select property="dto(supervisorId)" styleId="supervisorId_id"
                                          listName="employeeBaseList" firstEmpty="true"
                                          labelProperty="employeeName" valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}" tabIndex="2">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <app2:securitySubmit operation="${op}" functionality="OFFICE" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="3">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="OFFICE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="4">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="6">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="officeForm"/>

