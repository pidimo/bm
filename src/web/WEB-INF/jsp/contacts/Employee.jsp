<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<c:set var="newEmployee" value="${employeeForm.dtoMap['newEmployee']}"/>
<c:set var="importEmployee" value="${employeeForm.dtoMap['importEmployee']}"/>

<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="${action}" focus="dto(function)" styleClass="form-horizontal">

        <fmt:message var="datePattern" key="datePattern"/>

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(userTypeValue)" value="1"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(employeeId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${('create' == op)}">
            <html:hidden property="dto(employeeAddresId)"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="EMPLOYEE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)"
                                 tabindex="1">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

                <%--top links--%>
            &nbsp;
            <c:if test="${('update' == op)}">
                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                    <html:link tabindex="3" styleClass="btn btn-link folderTabLink"
                               page="/Person/Forward/Update.do?contactId=${employeeForm.dtoMap['employeeId']}&index=0&dto(addressId)=${employeeForm.dtoMap['employeeId']}&dto(sourceEmployeeId)=${employeeForm.dtoMap['employeeId']}&dto(onlyViewDetail)=true">
                        <span class="glyphicon glyphicon-edit"></span>
                        <fmt:message key="ContactPerson.personalInfo"/>
                    </html:link>
                </app2:checkAccessRight>
            </c:if>
        </div>

        <div class="${app2:getFormPanelClasses()}">

            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="name1_id">
                        <fmt:message key="Contact.Person.lastname"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:text property="dto(name1)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60"
                                  tabindex="4"
                                  view="true"
                                  styleId="name1_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="function_id">
                        <fmt:message key="Contact.Organization.Employee.function"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:text property="dto(function)"
                                  view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="40"
                                  tabindex="5"
                                  styleId="function_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="name2_id">
                        <fmt:message key="Contact.Person.firstname"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:text property="dto(name2)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60"
                                  tabindex="6"
                                  view="true"
                                  styleId="name2_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="socialSecurityNumber_id">
                        <fmt:message key="Contact.Organization.Employee.socialSecure"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:text property="dto(socialSecurityNumber)"
                                  view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20"
                                  tabindex="7"
                                  styleId="socialSecurityNumber_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="initials_id">
                        <fmt:message key="Contact.Organization.Employee.initials"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:text property="dto(initials)"
                                  view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="10"
                                  tabindex="8"
                                  styleId="initials_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Contact.Organization.Employee.healthFund"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <html:hidden property="dto(healthFundId)" styleId="fieldAddressId_id"/>
                        <div class="input-group">
                            <app:text property="dto(healthFundName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      view="${op == 'delete'}"
                                      tabindex="9"
                                      styleId="fieldAddressName_id"
                                      readonly="true"/>
                    <span class="input-group-btn">
                        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                            <tags:bootstrapSelectPopup styleId="ss"
                                                       url="/contacts/OrganizationSearch.do?allowCreation=0"
                                                       name="organizationSearchList"
                                                       titleKey="Common.search"
                                                       isLargeModal="true"
                                                       modalTitleKey="Contact.Title.search"
                                                       hide="${op == 'delete'}"/>

                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                            titleKey="Common.clear"
                                                            nameFieldId="fieldAddressName_id"
                                                            hide="${op == 'delete'}"/>

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

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(departmentId)"
                                      listName="departmentBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="departmentId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}"
                                      tabIndex="10"
                                      styleId="departmentId_id">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty param.contactId?param.contactId:0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="costCenterId_id">
                        <fmt:message key="Contact.Organization.Employee.costCenter"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(costCenterId)"
                                      listName="costCenterBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      module="/catalogs"
                                      readOnly="${op == 'delete'}"
                                      tabIndex="11"
                                      styleId="costCenterId_id">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
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

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(officeId)"
                                      listName="officeBaseList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="officeId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}"
                                      tabIndex="12"
                                      styleId="officeId_id">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty param.contactId?param.contactId:0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="costPosition_id">
                        <fmt:message key="Contact.Organization.Employee.costPosition"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:numberText property="dto(costPosition)"
                                        styleClass="${app2:getFormInputClasses()} mediumText"
                                        maxlength="14"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        tabindex="13"
                                        maxInt="11"
                                        maxFloat="2"
                                        styleId="costPosition_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="hireDate">
                        <fmt:message key="Contact.Organization.Employee.hireDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(hireDate)"
                                          styleId="hireDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          view="${op == 'delete'}"
                                          maxlength="10"
                                          tabindex="14"
                                          mode="bootstrap"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="costHour_id">
                        <fmt:message key="Contact.Organization.Employee.costHour"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:numberText property="dto(costHour)"
                                        styleClass="${app2:getFormInputClasses()} mediumText"
                                        maxlength="14"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        tabindex="15"
                                        maxInt="11"
                                        maxFloat="2"
                                        styleId="costHour_id"/>
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
                            <app:dateText property="dto(dateEnd)"
                                          styleId="dateEnd"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          view="${op == 'delete'}"
                                          maxlength="10"
                                          tabindex="16"
                                          mode="bootstrap"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="hourlyRate_id">
                        <fmt:message key="Contact.Organization.Employee.hourlyRate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:numberText property="dto(hourlyRate)"
                                        styleClass="${app2:getFormInputClasses()} mediumText"
                                        maxlength="14"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        tabindex="17"
                                        maxInt="11"
                                        maxFloat="2"
                                        styleId="hourlyRate_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="EMPLOYEE" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)" tabindex="18"><c:out
                    value="${button}"/></app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="19"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>

    </html:form>

</div>
<tags:jQueryValidation formName="employeeForm"/>
