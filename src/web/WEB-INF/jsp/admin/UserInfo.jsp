<%@ page import="com.piramide.elwis.utils.AdminConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>
<fmt:message var="datePattern" key="datePattern"/>

<tags:jscript language="JavaScript" src="/WEB-INF/jsp/js/admin/admin.jsp"/>
<%
    pageContext.setAttribute("languageList", JSPHelper.getLanguageList(request));
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<c:set var="INTERNAL_USER" value="<%=AdminConstants.INTERNAL_USER%>"/>
<c:set var="EXTERNAL_USER" value="<%=AdminConstants.EXTERNAL_USER%>"/>
<app2:checkAccessRight var="hasViewPermissionWVAPP" functionality="WVAPPVIEW" permission="VIEW"/>

<script language="JavaScript">

    function enableWVAppClick() {
        if('${hasViewPermissionWVAPP}' == 'true'){

            if($("#enableWVApp_id").prop("checked")) {

                $("#tr_app_visible").css("display", "");
                $("#tr_app_org").css("display", "");
            } else {
                $("#tr_app_visible").css("display", "none");
                $("#tr_app_org").css("display", "none");
            }
        }
    }

    function reWriteUserRoles() {
        if ('${hasViewPermissionWVAPP}' == 'true') {
            send($("[name='defineRoles']").get(0), $("[name='undefineRoles']").get(0));
        }
    }

    function clearOrganizations() {
        if ('${hasViewPermissionWVAPP}' == 'true') {
            $('#organizationBox_id option').remove().append('<option selected="selected" value="">&nbsp;</option>');
        }
    }
</script>

<html:form action="${action}" focus="dto(active)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" onclick="send(defineRoles, undefineRoles)"
                                 functionality="USER"
                                 tabindex="18" styleClass="button ${app2:getFormButtonClasses()}" property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="19"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">

            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

            <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
                <html:hidden property="dto(userId)"/>
                <html:hidden property="dto(employeeName)"/>
            </c:if>

            <c:if test="${'delete' == op}">
                <html:hidden property="dto(userId)"/>
                <html:hidden property="dto(employeeName)"/>
            </c:if>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Common.active"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <c:if test="${'update' == op}">
                        <c:set var="userIsRoot" value="${app2:isRootUser(userInfoUpdateForm.dtoMap['userId'])}"
                               scope="request"/>
                    </c:if>

                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(active)"
                                           styleId="active_id"
                                           disabled="${op == 'delete' || userIsRoot}"
                                           tabindex="1" styleClass=""/>
                            <label for="active_id"></label>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.typeUser"/>
                </label>

                <div class="${app2:getFormContainClasses(op != 'create')}">
                    <html:select property="dto(type)" readonly="${op != 'create'}"
                                 styleClass="type_contact_company mediumSelect ${app2:getFormSelectClasses()}"
                                 onchange="userTypeChange()" styleId="type" tabindex="2">
                        <html:option value=""/>
                        <html:options collection="typeUserList" property="value" labelProperty="label"/>
                    </html:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}" id="employeeAreaTitle" ${!(userInfoUpdateForm.dtoMap.type == 1) ? "style=\"display: none;\"":""}>
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Contact.employee"/>
                </label>

                <div class="${app2:getFormContainClasses('create' != op)}">
                    <div class="input-group">
                        <app:text property="dto(employeeName)" styleId="fieldEmployeeName_id"
                                  styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="35"
                                  tabindex="3" view="${'create' != op}" readonly="true"/>
                        <html:hidden property="dto(employeeId)" styleId="fieldEmployeeId_id"/>
                        <div class="input-group-btn">
                            <tags:bootstrapSelectPopup url="/admin/User/Create/ImportEmployee.do"
                                                       styleId="ImportEmployee_id"
                                                       name="ImportEmployee"
                                                       isLargeModal="true"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Contact.Organization.User.searchEmployee"
                                                       hide="${op != 'create'}"
                                                       submitOnSelect="${hasViewPermissionWVAPP}"
                                                       onSelectJSFunction="reWriteUserRoles()"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldEmployeeId_id"
                                                            nameFieldId="fieldEmployeeName_id"
                                                            titleKey="Common.clear"
                                                            hide="${op != 'create'}"
                                                            onClearJSFunction="clearOrganizations()"/>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}" id="addressAreaTitle"${!(userInfoUpdateForm.dtoMap.type == 0) ? "style=\"display: none;\"":""}>
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Appointment.contact"/>
                </label>

                <div class="${app2:getFormContainClasses('create' != op)}">
                    <div class="input-group">
                        <app:text property="dto(addressName)" styleId="fieldAddressName_id"
                                  styleClass="mediumText ${app2:getFormInputClasses()}"
                                  maxlength="35"
                                  tabindex="3" view="${'create' != op}" readonly="true"/>
                        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                        <div class="input-group-btn">
                            <tags:bootstrapSelectPopup url="/contacts/PersonSearch.do" name="personSearchList"
                                                       styleId="personSearchList_id"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Contact.Title.search"
                                                       isLargeModal="true"
                                                       hide="${op != 'create'}"
                                                       submitOnSelect="${hasViewPermissionWVAPP}"
                                                       onSelectJSFunction="reWriteUserRoles()"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                            nameFieldId="fieldAddressName_id"
                                                            titleKey="Common.clear"
                                                            hide="${op != 'create'}"
                                                            onClearJSFunction="clearOrganizations()"/>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>

                </div>
            </div>

            <c:if test="${'delete' != op}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="User.language"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <html:select property="dto(favoriteLanguage)"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     tabindex="4">
                            <html:options collection="languageList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </c:if>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.timeZone"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
                    <html:select property="dto(timeZone)"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                 tabindex="5"
                                 readonly="${'delete' == op}">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="timeZonesConstants" property="value" labelProperty="label"/>
                    </html:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.login"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <app:text property="dto(userLogin)" view="${'delete' == op}"
                              styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="20"
                              tabindex="5"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>

            <c:if test="${'create' == op}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="User.password"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <html:password property="dto(userPassword)"
                                       styleClass="mediumText ${app2:getFormInputClasses()}"
                                       maxlength="20"
                                       tabindex="6"
                                       value=""/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="User.passConfir"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <html:password property="dto(passwordConfir)"
                                       styleClass="mediumText ${app2:getFormInputClasses()}"
                                       maxlength="20"
                                       tabindex="7"
                                       value=""/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </c:if>

            <!--for IP-->
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.accessIp"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <app:text property="dto(accessIp)"
                              styleClass="mediumText ${app2:getFormInputClasses()}"
                              maxlength="200" tabindex="8"
                              view="${'delete' == op}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">

                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    * <fmt:message key="User.egAccessIp"/>
                </div>
            </div>

            <c:if test="${app2:isCompanyEnabledToMobileAccess(pageContext.request)}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="mobileActive_id">
                        <fmt:message key="User.mobile.enableAccess"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(mobileActive)" styleId="mobileActive_id"
                                               disabled="${op == 'delete'}" tabindex="8"
                                               styleClass=""/>
                                <label for="mobileActive_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:set var="userFormTemp" value="${userInfoForm}"/>
                <c:if test="${not empty userInfoUpdateForm}">
                    <c:set var="userFormTemp" value="${userInfoUpdateForm}"/>
                </c:if>

                <c:if test="${hasViewPermissionWVAPP}">
                    <legend class="title">
                        <fmt:message key="User.mobile.wvapp.title"/>
                    </legend>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="enableWVApp_id">
                            <fmt:message key="User.mobile.wvapp.enableApp"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(enableMobileWVApp)" styleId="enableWVApp_id"
                                                   onclick="enableWVAppClick()" disabled="${op == 'delete'}" tabindex="8"
                                                   styleClass=""/>
                                    <label for="enableWVApp_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <c:set var="isWVAppEnabled" value="${userFormTemp.dtoMap['enableMobileWVApp'] eq 'true'}"/>

                    <c:set var="wvapp_display" value="display: none"/>
                    <c:if test="${isWVAppEnabled}">
                        <c:set var="wvapp_display" value="display: "/>
                    </c:if>

                    <div class="${app2:getFormGroupClasses()}" id="tr_app_visible" style="${wvapp_display}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="User.mobile.wvapp.visible"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(visibleMobileApp)"
                                                   styleId="visibleMobileApp_id"
                                                   disabled="${op == 'delete'}"
                                                   tabindex="8" styleClass=""/>
                                    <label for="visibleMobileApp_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}" id="tr_app_org" style="${wvapp_display}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="User.mobile.wvapp.organization"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete'==op)}">
                            <c:choose>
                                <c:when test="${op != 'create'}">
                                    <c:set var="contactPersonIdVar" value="${userFormTemp.dtoMap['addressId']}"/>
                                </c:when>
                                <c:when test="${EXTERNAL_USER eq userFormTemp.dtoMap['type']}">
                                    <c:set var="contactPersonIdVar" value="${userFormTemp.dtoMap['addressId']}"/>
                                </c:when>
                                <c:when test="${INTERNAL_USER eq userFormTemp.dtoMap['type']}">
                                    <c:set var="contactPersonIdVar" value="${userFormTemp.dtoMap['employeeId']}"/>
                                </c:when>
                            </c:choose>

                            <fanta:select property="dto(mobileOrganizationId)" styleId="organizationBox_id"
                                          listName="organizationContactPersonList" labelProperty="organizationName"
                                          valueProperty="addressId" module="/contacts"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          firstEmpty="true"
                                          readOnly="${op == 'delete'}" tabIndex="8">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="contactPersonId"
                                                 value="${not empty contactPersonIdVar ? contactPersonIdVar : -1}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

            </c:if>

            <table class="col-xs-12">
                <tr>
                    <td class="control-label label-left">
                        <fmt:message key="User.available"/>
                    </td>
                    <td></td>
                    <td class="control-label label-left">
                        <fmt:message key="User.assigned"/>
                    </td>
                </tr>
                <tr>
                    <td width="47%">

                        <c:if test="${dto.availableRoles != null }">
                            <c:set var="availableRoles" value="${dto.availableRoles}" scope="request"/>
                        </c:if>

                        <html:select property="undefineRoles" styleClass="multipleSelect ${app2:getFormSelectClasses()}"
                                     tabindex="8"
                                     size="10"
                                     multiple="true"
                                     ondblclick="move(this.form.undefineRoles,this.form.defineRoles)"
                                     disabled="${op == 'delete'}">
                            <c:forEach var="item" items="${availableRoles}">
                                <html:option value="${item.roleId}">${item.roleName}</html:option>
                            </c:forEach>
                        </html:select>
                    </td>

                    <td>
                        <div class="btn-group-vertical">
                            <html:button onclick="move(this.form.undefineRoles, this.form.defineRoles)"
                                         styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                         value="&#xf054;"
                                         style="border-radius: 4px;"
                                         property="B1">
                            </html:button>
                            <html:button onclick="alls(this.form.undefineRoles, this.form.defineRoles)"
                                         styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                         value="&#xf054;&#xf054;"
                                         style="border-radius: 4px;"
                                         property="B11">
                            </html:button>
                            <html:button onclick="move(this.form.defineRoles, this.form.undefineRoles)"
                                         styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                         value="&#xf053;"
                                         style="border-radius: 4px;"
                                         property="B2">
                            </html:button>
                            <html:button onclick="alls(this.form.defineRoles, this.form.undefineRoles)"
                                         styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                         value="&#xf053;&#xf053;"
                                         style="border-radius: 4px;"
                                         property="B22">
                            </html:button>
                        </div>
                    </td>

                    <td width="47%">

                        <c:if test="${dto.defineRoles != null }">
                            <c:set var="defineRoles" value="${dto.defineRoles}" scope="request"/>
                        </c:if>

                        <html:select property="defineRoles" size="10" multiple="true"
                                     styleClass="multipleSelect ${app2:getFormSelectClasses()}"
                                     indexed="10"
                                     disabled="${op == 'delete'}">
                            <c:forEach var="item" items="${defineRoles}">
                                <html:option value="${item.roleId}">${item.roleName}</html:option>
                            </c:forEach>
                        </html:select>
                    </td>
                </tr>
            </table>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" onclick="send(defineRoles, undefineRoles)"
                                 functionality="USER"
                                 tabindex="15"
                                 styleClass="button ${app2:getFormButtonClasses()}" property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="16"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="userInfoForm"/>
