<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>

<html:form action="${action}" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <html:hidden property="dto(op)" value="${op}"/>

            <c:if test="${('update' == op) || ('delete' == op)}">
                <html:hidden property="dto(viewUserId)"/>
            </c:if>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="fieldViewUserName_id">
                    <fmt:message key="Scheduler.grantAccess.userName"/>
                </label>
                <div class="${app2:getFormContainClasses('delete' == op || 'update' == op)}">
                    <div class="col-sm-8">
                        <div class="input-group">
                            <app:text property="dto(viewUserName)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="20"
                                      readonly="true"
                                      view="${'delete' == op || 'update' == op}"
                                      styleId="fieldViewUserName_id"/>
                            <c:if test="${'create' == op}">
                                <html:hidden property="dto(viewUserId)" styleId="fieldViewUserId_id"/>
                    <span class="input-group-btn">
                        <tags:bootstrapSelectPopup
                                styleId="searchUser_id"
                                isLargeModal="true"
                                url="/scheduler/GrantAccess/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(userId)=${sessionScope.user.valueMap['userId']}&parameter(ownUserId)=${sessionScope.user.valueMap['userId']}"
                                name="searchUser"
                                modalTitleKey="Scheduler.grantAccess.searchUser"
                                titleKey="Scheduler.grantAccess.searchUser"/>
                        <tags:clearBootstrapSelectPopup keyFieldId="fieldViewUserId_id"
                                                        nameFieldId="fieldViewUserName_id"
                                                        titleKey="Common.clear"
                                                        hide="${op == 'delete' || op=='update' }"/>
                    </span>
                            </c:if>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <legend class="title">
                <fmt:message key="Scheduler.grantAccess.permissions"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Scheduler.grantAccess.publicAppointment"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <div class="row">
                        <div class="col-sm-3 col-md-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(anonym)" styleId="anonym_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="1">
                                        <label for="anonym_id"><fmt:message key="Scheduler.grantAccess.anonym"/></label>
                                    </html:checkbox>

                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(read)" styleId="read_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="2">
                                        <label for="read_id"><fmt:message key="Scheduler.grantAccess.read"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-3 col-md-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(add)" styleId="add_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="3">
                                        <label for="add_id"><fmt:message key="Scheduler.grantAccess.add"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(edit)" styleId="edit_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="4">
                                        <label for="edit_id"><fmt:message key="Scheduler.grantAccess.edit"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(delete)" styleId="delete_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="5">
                                        <label for="delete_id"><fmt:message key="Scheduler.grantAccess.delete"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Scheduler.grantAccess.privateAppointment"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <div class="row">
                        <div class="col-sm-3 col-md-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(privateAnonym)" styleId="privateAnonym_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="6">
                                        <label for="privateAnonym_id"><fmt:message key="Scheduler.grantAccess.anonym"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(privateRead)" styleId="privateRead_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="7">
                                        <label for="privateRead_id"><fmt:message key="Scheduler.grantAccess.read"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-3 col-md-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(privateAdd)" styleId="privateAdd_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="8">
                                        <label for="privateAdd_id"><fmt:message key="Scheduler.grantAccess.add"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(privateEdit)" styleId="privateEdit_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="9">
                                        <label for="privateEdit_id"><fmt:message key="Scheduler.grantAccess.edit"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(privateDelete)" styleId="privateDelete_id" styleClass="radio"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="10">
                                        <label for="privateDelete_id"><fmt:message key="Scheduler.grantAccess.delete"/></label>
                                    </html:checkbox>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
    <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}" functionality="GRANTACCESS" styleClass="button ${app2:getFormButtonClasses()}"
                             tabindex="11">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="12">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>

<tags:jQueryValidation formName="grantAccessForm"/>
