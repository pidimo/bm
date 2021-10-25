<%@ include file="/Includes.jsp" %>

<script type="text/javascript">
    function submitOnChange(obj) {
        document.getElementById('mainFormId').submit();
    }
</script>

<c:if test="${empty workOnPerson}">
    <c:set var="workOnPerson" value="false"/>
</c:if>

<tags:initBootstrapSelectPopup/>

<c:set var="focusValue" value="dto(new)"/>
<c:if test="${'create' ==  op && workOnPerson}">
    <c:set var="focusValue" value="dto(userName)"/>
</c:if>
<c:if test="${'create' ==  op && !workOnPerson}">
    <c:set var="focusValue" value="dto(addressId)"/>
</c:if>

<html:form action="${action}" focus="${focusValue}" styleId="mainFormId" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>

    <html:hidden property="dto(projectId)" value="${param.projectId}"/>

    <html:hidden property="dto(responsibleAddressId)"/>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <div class="${app2:getFormClassesLarge()}">
        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="PROJECTUSER" property="saveButton"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="11">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${'create' ==  op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="PROJECTUSER"
                                         styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                         property="SaveAndNew"
                                         tabindex="12">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" tabindex="13">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="addressId_id">
                    <fmt:message key="ProjectAssignee.userName"/>
                </label>

                <div class="${app2:getFormContainClasses('create' != op)}">
                    <c:choose>
                        <c:when test="${'create' == op}">
                            <c:choose>
                                <c:when test="${workOnPerson}">
                                    <div class="input-group">
                                        <app:text property="dto(userName)" styleId="fieldAddressName_id"
                                                  styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="35"
                                                  view="false"
                                                  readonly="true"
                                                  tabindex="1"/>
                                        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/contacts/PersonSearch.do"
                                                               name="personSearchList"
                                                               styleId="personSearchList_id"
                                                               titleKey="Common.search"
                                                               modalTitleKey="Contact.Title.search"
                                                               isLargeModal="true"
                                                               hide="false"
                                                               submitOnSelect="false" tabindex="2"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                    nameFieldId="fieldAddressName_id"
                                                                    titleKey="Common.clear"
                                                                    hide="false"
                                                                    submitOnClear="false" tabindex="3"/>
                                </span>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <html:hidden property="dto(userName)" styleId="fieldAddressName_id"/>
                                    <fanta:select property="dto(addressId)"
                                                  styleId="addressId_id"
                                                  listName="userBaseList"
                                                  labelProperty="name"
                                                  valueProperty="addressId"
                                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                                  module="/admin"
                                                  firstEmpty="true"
                                                  readOnly="${op == 'delete'}"
                                                  onChange="javascript:submitOnChange(this);"
                                                  tabIndex="1">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="dto(addressId)"/>
                            <html:hidden property="dto(userName)" styleId="fieldAddressName_id"/>
                            <c:out value="${projectUserForm.dtoMap['userName']}"/>
                        </c:otherwise>
                    </c:choose>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="new_id">
                    <fmt:message key="ProjectAssignee.permission"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <div class="row">
                        <div class="col-lg-3 col-sm-6 col-xs-12">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(new)"
                                                   styleId="new_id"
                                                   styleClass=""
                                                   disabled="${op == 'delete'}"
                                                   tabindex="4"/>
                                    <label for="new_id"><fmt:message key="ProjectAssignee.permission.new"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 col-xs-12">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(view)"
                                                   styleId="view_id"
                                                   styleClass=""
                                                   disabled="${op == 'delete'}"
                                                   tabindex="5"/>
                                    <label for="view_id"><fmt:message key="ProjectAssignee.permission.view"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 col-xs-12">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(confirm)"
                                                   styleId="confirm_id"
                                                   styleClass=""
                                                   disabled="${op == 'delete'}"
                                                   tabindex="6"/>
                                    <label for="confirm_id"> <fmt:message
                                            key="ProjectAssignee.permission.confirm"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 col-xs-12">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <c:set var="disableAdminPermission"
                                           value="${'delete' == op || (not empty projectUserForm.dtoMap['admin'] && not empty projectUserForm.dtoMap['responsibleAddressId'] && projectUserForm.dtoMap['responsibleAddressId'] == projectUserForm.dtoMap['addressId'])}"/>
                                    <c:if test="${disableAdminPermission}">
                                        <html:hidden property="dto(admin)"/>
                                    </c:if>
                                    <html:checkbox property="dto(admin)"
                                                   styleId="admin_id"
                                                   styleClass=""
                                                   disabled="${disableAdminPermission}"
                                                   tabindex="7"/>
                                    <label for="admin_id"><fmt:message key="ProjectAssignee.permission.admin"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}" functionality="PROJECTUSER" property="saveButton"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="8">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${'create' ==  op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="PROJECTUSER"
                                         styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                         property="SaveAndNew" tabindex="9">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" tabindex="10">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="projectUserForm"/>
