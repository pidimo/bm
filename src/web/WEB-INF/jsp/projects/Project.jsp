<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<div class="${app2:getFormWrapperTwoColumns()}">

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces" scope="request"/>
    <fmt:message var="datePattern" key="datePattern"/>

    <html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
        <html:hidden property="dto(op)" value="${op}"/>

        <c:if test="${'update' == op || op == 'delete'}">
            <html:hidden property="dto(projectId)"/>

        </c:if>

        <c:set var="canEditPage" value="${true}"/>
        <c:if test="${'update' == op || 'delete' == op}">
            <c:set var="canEditPage"
                   value="${app2:hasProjectUserPermission(projectForm.dtoMap['projectId'], 'ADMIN', pageContext.request)}"/>
            <html:hidden property="dto(version)"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:choose>
                <c:when test="${'update' == op || 'delete' == op}">
                    <c:if test="${canEditPage}">
                        <app2:securitySubmit operation="${op}" functionality="PROJECT"
                                             property="saveButton"
                                             tabindex="19"
                                             styleClass="${app2:getFormButtonClasses()}"
                                             styleId="saveButtonId">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="PROJECT"
                                         property="saveButton"
                                         tabindex="20"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         styleId="saveButtonId">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">

            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="name_id">
                        <fmt:message key="Project.name"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || !canEditPage)}">
                        <app:text property="dto(name)"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="80"
                                  styleId="name_id"
                                  tabindex="1"
                                  view="${op == 'delete' || !canEditPage}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="toBeInvoiced_id">
                        <fmt:message key="Project.toBeInvoiced"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || !canEditPage)}">
                        <c:set var="toBeInvoicedOptions"
                               value="${app2:getToBeInvoicedTypes(pageContext.request)}"/>
                        <html:select property="dto(toBeInvoiced)"
                                     styleId="toBeInvoiced_id"
                                     styleClass="select ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete' || !canEditPage}"
                                     tabindex="2">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="toBeInvoicedOptions" property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="responsibleId_id">
                        <fmt:message key="Project.responsible"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || !canEditPage)}">
                        <fanta:select property="dto(responsibleId)"
                                      listName="userBaseList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleId="responsibleId_id"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      module="/admin" firstEmpty="true" tabIndex="3"
                                      readOnly="${op == 'delete' || !canEditPage}"
                                      value="${sessionScope.user.valueMap['userId']}">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="hasTimeLimit_id">
                        <fmt:message key="Project.hasTimeLimit"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(hasTimeLimit)"
                                               disabled="${op == 'delete' || !canEditPage}"
                                               tabindex="4"
                                               styleId="hasTimeLimit_id"
                                               styleClass="radio"
                                               value="true"/>
                                <label for=""></label>
                            </div>
                        </div>

                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                        <fmt:message key="Project.startDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || !canEditPage)}">
                        <div class="input-group date">
                            <app:dateText property="dto(startDate)"
                                          styleId="startDate"
                                          calendarPicker="${op != 'delete' && canEditPage}"
                                          datePatternKey="${datePattern}"
                                          styleClass="dateText ${app2:getFormInputClasses()}"
                                          view="${op == 'delete' || !canEditPage}"
                                          maxlength="10"
                                          tabindex="5"
                                          mode="bootstrap"
                                          currentDate="true"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="accountId_id">
                        <fmt:message key="Project.account"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || !canEditPage)}">
                        <fanta:select property="dto(accountId)"
                                      listName="accountList"
                                      labelProperty="name"
                                      styleId="accountId_id"
                                      valueProperty="accountId"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      module="/catalogs"
                                      firstEmpty="true"
                                      readOnly="${'delete' == op || !canEditPage}"
                                      tabIndex="6">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="endDate">
                        <fmt:message key="Project.endDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || !canEditPage)}">
                        <div class="input-group date">
                            <app:dateText property="dto(endDate)"
                                          styleId="endDate"
                                          calendarPicker="${op != 'delete' && canEditPage}"
                                          datePatternKey="${datePattern}"
                                          styleClass="dateText ${app2:getFormInputClasses()}"
                                          view="${op == 'delete' || !canEditPage}"
                                          maxlength="10"
                                          mode="bootstrap"
                                          tabindex="7"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Project.customer"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || !canEditPage)}">
                        <c:if test="${not empty projectForm.dtoMap['customerId'] and 'update' == op}">
                            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
                                </c:set>
                                <c:set var="addressMap"
                                       value="${app2:getAddressMap(projectForm.dtoMap['customerId'])}"/>
                                <c:choose>
                                    <c:when test="${personType == addressMap['addressType']}">
                                        <c:set var="addressEditLink"
                                               value="/contacts/Person/Forward/Update.do?contactId=${projectForm.dtoMap['customerId']}&dto(addressId)=${projectForm.dtoMap['customerId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="addressEditLink"
                                               value="/contacts/Organization/Forward/Update.do?contactId=${projectForm.dtoMap['customerId']}&dto(addressId)=${projectForm.dtoMap['customerId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                    </c:otherwise>
                                </c:choose>
                            </app2:checkAccessRight>
                        </c:if>

                        <c:if test="${canEditPage}">
                            <div class="input-group">
                        </c:if>

                            <app:text property="dto(customerName)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      readonly="true"
                                      styleId="fieldAddressName_id"
                                      view="${'delete' == op || !canEditPage}"
                                      tabindex="8"/>
                            <html:hidden property="dto(customerId)" styleId="fieldAddressId_id"/>

                            <c:if test="${canEditPage}">
                                <span class="input-group-btn">
                            </c:if>
                                <c:if test="${not empty addressEditLink and !canEditPage}">
                                    <app:link action="${addressEditLink}"
                                              titleKey="Common.edit"
                                              contextRelative="true"
                                              tabindex="11"
                                              styleClass="btn-link pull-right">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:if>
                                <c:if test="${not empty addressEditLink and canEditPage}">
                                    <app:link action="${addressEditLink}"
                                              titleKey="Common.edit"
                                              contextRelative="true"
                                              tabindex="11"
                                              styleClass="btn btn-default">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </app:link>
                                </c:if>

                                <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                                           name="searchAddress"
                                                           styleId="contactSearchPopupId"
                                                           modalTitleKey="Contact.Title.search"
                                                           titleKey="Common.search"
                                                           submitOnSelect="true"
                                                           hide="${'delete' == op || !canEditPage}"
                                                           tabindex="8"
                                                           isLargeModal="true"/>

                                <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                nameFieldId="fieldAddressName_id"
                                                                titleKey="Common.clear"
                                                                submitOnClear="true"
                                                                hide="${'delete' == op || !canEditPage}"
                                                                tabindex="8"
                                                                styleClass="${app2:getFormButtonClasses()}"/>
                            <c:if test="${canEditPage}">
                                </span>
                            </c:if>
                        <c:if test="${canEditPage}">
                            </div>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                        <fmt:message key="Project.status"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || !canEditPage)}">
                        <c:set var="statusesOptions" value="${app2:getProjectStatuses(pageContext.request)}"/>
                        <html:select property="dto(status)"
                                     styleId="status_id"
                                     styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete' || !canEditPage}"
                                     tabindex="9">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="statusesOptions" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                        <fmt:message key="Project.contactPerson"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || !canEditPage)}">
                        <fanta:select property="dto(contactPersonId)"
                                      listName="searchContactPersonList"
                                      module="/contacts"
                                      firstEmpty="true"
                                      styleId="contactPersonId_id"
                                      labelProperty="contactPersonName"
                                      valueProperty="contactPersonId"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${'delete' == op || !canEditPage}" tabIndex="10">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId"
                                             value="${not empty projectForm.dtoMap['customerId']? projectForm.dtoMap['customerId']:0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="plannedInvoice_id">
                        <fmt:message key="Project.plannedInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || !canEditPage)}">
                        <app:numberText property="dto(plannedInvoice)"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="6"
                                        numberType="decimal"
                                        maxInt="6"
                                        styleId="plannedInvoice_id"
                                        maxFloat="1"
                                        view="${'delete' == op || !canEditPage}"
                                        tabindex="11"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalInvoice_id">
                        <fmt:message key="Project.totalInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:numberText property="dto(totalInvoice)"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="6"
                                        numberType="decimal"
                                        maxInt="6"
                                        styleId="totalInvoice_id"
                                        maxFloat="1"
                                        view="true"
                                        tabindex="12"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="plannedNoInvoice_id">
                        <fmt:message key="Project.plannedNoInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op || !canEditPage)}">
                        <app:numberText property="dto(plannedNoInvoice)"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="6"
                                        numberType="decimal"
                                        styleId="plannedNoInvoice_id"
                                        maxInt="6"
                                        maxFloat="1"
                                        view="${'delete' == op || !canEditPage}" tabindex="13"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="totalNoInvoice_id">
                        <fmt:message key="Project.totalNoInvoice"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:numberText property="dto(totalNoInvoice)"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="6"
                                        numberType="decimal"
                                        maxInt="6"
                                        styleId="totalNoInvoice_id"
                                        maxFloat="1"
                                        view="true"
                                        tabindex="14"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="form-group col-xs-12 col-sm-12 col-md-12">
                    <div class="col-xs-12 col-sm-12 col-md-12">
                        <html:textarea property="dto(description)"
                                       styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                       style="height:120px;width:100%;"
                                       tabindex="15" readonly="${op == 'delete' || !canEditPage}"/>
                    </div>
                </div>
            </div>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">

            <c:choose>
                <c:when test="${'update' == op || 'delete' == op}">
                    <c:if test="${canEditPage}">
                        <app2:securitySubmit operation="${op}" functionality="PROJECT"
                                             property="saveButton"
                                             styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId"
                                             tabindex="16">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="PROJECT"
                                         property="saveButton"
                                         styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId"
                                         tabindex="17">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="18">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

        </div>

    </html:form>
    <br/>

</div>

<tags:jQueryValidation formName="projectForm"/>
