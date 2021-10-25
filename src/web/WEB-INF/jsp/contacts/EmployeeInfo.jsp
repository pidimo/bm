<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<c:set var="id" value="${employeeForm.dtoMap['employeeId']}"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="dto(initials)" styleClass="form-horizontal">
        <fmt:message var="datePattern" key="datePattern"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <c:choose>
                <c:when test="${id != sessionScope.user.valueMap['userAddressId']}">
                    <app2:securitySubmit operation="update" functionality="EMPLOYEE"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="dto(save)" tabindex="18">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                    <app2:securitySubmit operation="delete" functionality="EMPLOYEE"
                                         styleClass="${app2:getFormButtonCancelClasses()}"
                                         property="dto(delete)" tabindex="19">
                        <fmt:message key="Common.delete"/>
                    </app2:securitySubmit>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="update" functionality="EMPLOYEE"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="dto(save)" tabindex="18">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <c:choose>
                <c:when test="${op == null || op == '' }">
                    <html:hidden property="dto(op)" value="update"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="dto(op)" value="${op}"/>
                </c:otherwise>
            </c:choose>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
            <html:hidden property="dto(name1)" value="${name1}"/>
            <html:hidden property="dto(name2)" value="${name2}"/>
            <html:hidden property="dto(employeeId)"/>
            <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(fromInfo)" value="ok"/>
            <html:hidden property="dto(userTypeValue)" value="1"/>

            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="initials_id">
                            <fmt:message key="Contact.Organization.Employee.initials"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(initials)" styleId="initials_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText" maxlength="20" tabindex="1"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="socialSecurityNumber_id">
                            <fmt:message key="Contact.Organization.Employee.socialSecure"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(socialSecurityNumber)" styleId="socialSecurityNumber_id"
                                      view="${op == 'delete'}"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="20" tabindex="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="function_id">
                            <fmt:message key="Contact.Organization.Employee.function"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(function)" styleId="function_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText" view="${op == 'delete'}"
                                      maxlength="20"
                                      tabindex="3"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Contact.Organization.Employee.healthFund"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <!-- search Health Fundations-->
                            <html:hidden property="dto(healthFundId)" styleId="fieldAddressId_id"/>
                            <div class="input-group">
                                <app:text property="dto(healthFundName)"
                                          styleClass="${app2:getFormInputClasses()} mediumText" maxlength="40"
                                          view="${op == 'delete'}"
                                          tabindex="4" styleId="fieldAddressName_id" readonly="true"/>
                                <span class="input-group-btn">
                                    <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                                        <tags:bootstrapSelectPopup styleId="organizationSearchList_id"
                                                                   url="/contacts/OrganizationSearch.do?allowCreation=0"
                                                                   name="organizationSearchList"
                                                                   titleKey="Common.search"
                                                                   tabindex="5"
                                                                   hide="${op == 'delete' || dto.status == '1'}"/>
                                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                        nameFieldId="fieldAddressName_id"
                                                                        titleKey="Common.clear"
                                                                        tabindex="6"
                                                                        glyphiconClass="glyphicon-erase"
                                                                        hide="${op == 'delete' || dto.status == '1'}"/>
                                    </app2:checkAccessRight>
                                </span>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="departmentId_id">
                            <fmt:message key="Contact.Organization.Employee.department"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <!-- Collection departments-->
                            <fanta:select property="dto(departmentId)" styleId="departmentId_id"
                                          listName="departmentBaseList" firstEmpty="true"
                                          labelProperty="name" valueProperty="departmentId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}" tabIndex="7">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="costCenterId_id">
                            <fmt:message key="Contact.Organization.Employee.costCenter"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <!-- Collection costcenters-->
                            <fanta:select property="dto(costCenterId)" styleId="costCenterId_id"
                                          listName="costCenterBaseList" firstEmpty="true"
                                          labelProperty="name" valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          readOnly="${op == 'delete'}" tabIndex="8">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="officeId_id">
                            <fmt:message key="Contact.Organization.Employee.office"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <!-- Collection offices-->
                            <fanta:select property="dto(officeId)" styleId="officeId_id" listName="officeBaseList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="officeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op == 'delete'}"
                                          tabIndex="9">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="addressId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="costPosition_id">
                            <fmt:message key="Contact.Organization.Employee.costPosition"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:numberText property="dto(costPosition)" styleId="costPosition_id"
                                            styleClass="${app2:getFormSelectClasses()} numberTextMedium" maxlength="14"
                                            view="${'delete' == op}" numberType="decimal" tabindex="10" maxInt="11"
                                            maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="hireDate">
                            <fmt:message key="Contact.Organization.Employee.hireDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="input-group date">
                                <app:dateText property="dto(hireDate)" styleId="hireDate"
                                              mode="bootstrap"
                                              calendarPicker="${op != 'delete'}"
                                              datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} datepicker mediumText"
                                              view="${op == 'delete'}"
                                              maxlength="10"
                                              tabindex="11"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="costHour_id">
                            <fmt:message key="Contact.Organization.Employee.costHour"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:numberText property="dto(costHour)" styleId="costHour_id"
                                            styleClass="${app2:getFormInputClasses()} numberTextMedium" maxlength="14"
                                            view="${'delete' == op}"
                                            numberType="decimal" tabindex="12" maxInt="11" maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateEnd">
                            <fmt:message key="Contact.Organization.Employee.endDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <div class="input-group date">
                                <app:dateText property="dto(dateEnd)" styleId="dateEnd"
                                              mode="bootstrap"
                                              calendarPicker="${op != 'delete'}"
                                              datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} datepicker mediumText"
                                              view="${op == 'delete'}"
                                              maxlength="10"
                                              tabindex="13"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="hourlyRate_id">
                            <fmt:message key="Contact.Organization.Employee.hourlyRate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                            <app:numberText property="dto(hourlyRate)" styleId="hourlyRate_id"
                                            styleClass="${app2:getFormInputClasses()} numberTextMedium" maxlength="14"
                                            view="${'delete' == op}" numberType="decimal" tabindex="14" maxInt="11"
                                            maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <!-- Buttons -->
            <c:choose>
                <c:when test="${id != sessionScope.user.valueMap['userAddressId']}">
                    <app2:securitySubmit operation="update" functionality="EMPLOYEE"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="dto(save)" tabindex="15">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                    <app2:securitySubmit operation="delete" functionality="EMPLOYEE"
                                         styleClass="${app2:getFormButtonCancelClasses()}"
                                         property="dto(delete)" tabindex="16">
                        <fmt:message key="Common.delete"/>
                    </app2:securitySubmit>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="update" functionality="EMPLOYEE"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="dto(save)" tabindex="17">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="employeeForm"/>


