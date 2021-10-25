<%@ include file="/Includes.jsp" %>

<%--
	the following attributes should be set before including this page:
		- op (create|read|update|delete) : operation of this page
--%>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(organizationId)" value="${param.contactId}"/>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(departmentId)"/>
                </c:if>
                <c:if test="${('update' == op)}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="Contact.Organization.Department.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(name)" styleId="name_id" view="${op == 'delete'}" styleClass="mediumText form-control"
                                  maxlength="30"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="parentId_id">
                        <fmt:message key="Contact.Organization.Department.nameParent"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <fanta:select property="dto(parentId)"
                                      styleId="parentId_id"
                                      listName="${op == 'delete' ? 'departmentBaseList' : 'parentDepartmentList'}"
                                      firstEmpty="true" labelProperty="name" valueProperty="departmentId"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'}" tabIndex="2">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId" value="${ not empty param.contactId?param.contactId:0}"/>
                            <c:if test="${op != 'create'}">
                                <fanta:parameter field="departmentId"
                                                 value="${not empty dto.departmentId?dto.departmentId:0}"/>
                            </c:if>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="managerId_id">
                        <fmt:message key="Contact.Organization.Department.manager"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">   <!-- tag with contact persons-->
                        <fanta:select property="dto(managerId)"
                                      styleId="managerId_id"
                                      listName="searchContactPersonList" firstEmpty="true"
                                      labelProperty="contactPersonName" valueProperty="contactPersonId"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'}" tabIndex="3">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="addressId" value="${ not empty param.contactId?param.contactId:0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <!-- Buttons -->
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="DEPARTMENT"
                                 styleClass="button ${app2:getFormButtonClasses()}" tabindex="4">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="DEPARTMENT"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="4">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="6">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="departmentForm"/>