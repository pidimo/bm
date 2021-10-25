<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<html:form action="${action}" focus="dto(assigneeId)" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(projectId)" value="${param.projectId}"/>

    <c:if test="${'update'== op || 'delete'== op}">
        <html:hidden property="dto(timeLimitId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>


            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" property="save"
                                     styleClass="button ${app2:getFormButtonClasses()}" tabindex="13">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" styleClass="button ${app2:getFormButtonClasses()}"
                                         property="SaveAndNew" tabindex="14">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="15">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.assignee"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <fanta:select property="dto(assigneeId)"
                                          listName="projectUserList"
                                          labelProperty="userName"
                                          valueProperty="addressId"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          module="/projects"
                                          firstEmpty="true"
                                          readOnly="${op == 'delete'}"
                                          tabIndex="1">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="projectId"
                                                 value="${param.projectId}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.hasTimeLimit"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(hasTimeLimit)" styleId="hasTimeLimit_id" disabled="${op == 'delete'}" tabindex="2"
                                                   styleClass="radio" value="true"/>
                                    <label for="hasTimeLimit_id"></label>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.subProject"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <fanta:select property="dto(subProjectId)"
                                          listName="subProjectList"
                                          labelProperty="subProjectName"
                                          valueProperty="subProjectId"
                                          firstEmpty="true"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          module="/projects"
                                          readOnly="${'delete' == op}"
                                          tabIndex="3">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="projectId" value="${param.projectId}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.invoiceLimit"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <app:numberText property="dto(invoiceLimit)" styleClass="numberText ${app2:getFormInputClasses()}" maxlength="6"
                                            numberType="decimal" maxInt="6" maxFloat="1"
                                            view="${'delete' == op}"
                                            tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.totalInvoiceLimit"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <c:set var="totalTimeMap" value="${app2:calculateProjectTimesByAssigneeSubProject(projectTimeLimitForm.dtoMap['projectId'], projectTimeLimitForm.dtoMap['assigneeId'], projectTimeLimitForm.dtoMap['subProjectId'])}"/>

                            <fmt:formatNumber var="invoiceTotalFormated" value="${totalTimeMap.totalInvoiceTime}" type="number" pattern="${numberFormat}"/>
                                ${invoiceTotalFormated}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.noInvoiceLimit"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <app:numberText property="dto(noInvoiceLimit)" styleClass="numberText ${app2:getFormInputClasses()}"
                                            maxlength="6" numberType="decimal" maxInt="6" maxFloat="1"
                                            view="${'delete' == op}" tabindex="5"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="ProjectTimeLimit.totalNoInvoiceLimit"/>
                        </label>
                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <fmt:formatNumber var="noInvoiceTotalFormated" value="${totalTimeMap.totalNoInvoiceTime}" type="number" pattern="${numberFormat}"/>
                                ${noInvoiceTotalFormated}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" property="save"
                                 styleClass="button ${app2:getFormButtonClasses()}" tabindex="10">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="11">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

</html:form>

<tags:jQueryValidation formName="projectTimeLimitForm"/>