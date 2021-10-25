<%@ page import="com.piramide.elwis.utils.AdminConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<calendar:initialize/>
<fmt:message var="datePattern" key="datePattern"/>

<tags:jscript language="JavaScript" src="/js/admin/admin.jsp"/>
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
        if('${hasViewPermissionWVAPP}' == 'true'){
            send($("[name='defineRoles']").get(0), $("[name='undefineRoles']").get(0));
        }
    }

    function clearOrganizations() {
        if('${hasViewPermissionWVAPP}' == 'true'){
            $('#organizationBox_id option').remove().append('<option selected="selected" value="">&nbsp;</option>');
        }
    }

</script>

<table width="50%" border="0" align="center" cellspacing="0" cellpadding="10">
<tr>
<td align="left">
<html:form action="${action}" focus="dto(active)">
<table width="100%" border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td class="button">
            <app2:securitySubmit operation="${op}" onclick="send(defineRoles, undefineRoles)" functionality="USER"
                                 tabindex="18" styleClass="button" property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="19"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
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

    <tr>
        <td colspan="4" class="title"><c:out value="${title}"/></td>
    </tr>
    <tr>
        <td width="30%" class="label"><fmt:message key="Common.active"/></td>
        <td width="70%" class="contain">

            <c:if test="${'update' == op}">
                <c:set var="userIsRoot" value="${app2:isRootUser(userInfoUpdateForm.dtoMap['userId'])}" scope="request"/>
            </c:if>

            <html:checkbox property="dto(active)" disabled="${op == 'delete' || userIsRoot}" tabindex="1" styleClass="radio"/>
        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="User.typeUser"/></td>
        <td class="contain">
            <html:select property="dto(type)" readonly="${op != 'create'}" styleClass="mediumSelect"
                         onchange="userTypeChange()" styleId="type" tabindex="2">
                <html:option value=""/>
                <html:options collection="typeUserList" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr id="employeeAreaTitle" ${!(userInfoUpdateForm.dtoMap.type == 1) ? "style=\"display: none;\"":""}>
        <td class="label"><fmt:message key="Contact.employee"/></td>
        <td class="contain">
            <html:hidden property="dto(employeeId)" styleId="fieldEmployeeId_id"/>
            <app:text property="dto(employeeName)" styleId="fieldEmployeeName_id" styleClass="mediumText" maxlength="35"
                      tabindex="3" view="${'create' != op}" readonly="true"/>

            <tags:selectPopup url="/admin/User/Create/ImportEmployee.do" name="ImportEmployee" titleKey="Common.search"
                              hide="${op != 'create'}" submitOnSelect="${hasViewPermissionWVAPP}" onSelectJSFunction="reWriteUserRoles()"/>
            <tags:clearSelectPopup keyFieldId="fieldEmployeeId_id" nameFieldId="fieldEmployeeName_id"
                                   titleKey="Common.clear"
                                   hide="${op != 'create'}" onClearJSFunction="clearOrganizations()"/>
        </td>
    </tr>
    <tr id="addressAreaTitle" ${!(userInfoUpdateForm.dtoMap.type == 0) ? "style=\"display: none;\"":""}>
        <td class="label"><fmt:message key="Appointment.contact"/></td>
        <td class="contain">

            <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
            <app:text property="dto(addressName)" styleId="fieldAddressName_id" styleClass="mediumText" maxlength="35"
                      tabindex="3" view="${'create' != op}" readonly="true"/>

            <tags:selectPopup url="/contacts/PersonSearch.do" name="personSearchList" titleKey="Common.search"
                              hide="${op != 'create'}" submitOnSelect="${hasViewPermissionWVAPP}" onSelectJSFunction="reWriteUserRoles()"/>
            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                   titleKey="Common.clear"
                                   hide="${op != 'create'}" onClearJSFunction="clearOrganizations()"/>

        </td>
    </tr>

    <c:if test="${'delete' != op}">
        <tr>
            <td class="label"><fmt:message key="User.language"/></td>
            <td class="contain">
                <html:select property="dto(favoriteLanguage)" styleClass="mediumSelect" tabindex="4">
                    <html:options collection="languageList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
    </c:if>

    <tr>
        <TD class="label"><fmt:message key="User.timeZone"/></TD>
        <td class="contain">
            <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
            <html:select property="dto(timeZone)"
                         styleClass="mediumSelect"
                         tabindex="5"
                         readonly="${'delete' == op}">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="timeZonesConstants" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="User.login"/></td>
        <td class="contain">
            <app:text property="dto(userLogin)" view="${'delete' == op}" styleClass="mediumText" maxlength="20"
                      tabindex="5"/>
        </td>
    </tr>

    <c:if test="${'create' == op}">
        <tr>
            <td class="label"><fmt:message key="User.password"/></td>
            <td class="contain">
                <html:password property="dto(userPassword)" styleClass="mediumText" maxlength="20" tabindex="6"
                               value=""/>
            </td>
        </tr>
        <tr>
            <td class="label"><fmt:message key="User.passConfir"/></td>
            <td class="contain">
                <html:password property="dto(passwordConfir)" styleClass="mediumText" maxlength="20" tabindex="7"
                               value=""/>
            </td>
        </tr>
    </c:if>

    <!--for IP-->
    <tr>
        <td class="label"><fmt:message key="User.accessIp"/></td>
        <td class="contain">
            <app:text property="dto(accessIp)" styleClass="mediumText" maxlength="200" tabindex="8"
                      view="${'delete' == op}"/>
                <%--        <fmt:message key="User.egAccessIp"/>--%>
        </td>
    </tr>

    <tr>
        <td class="contain"></td>
        <td class="contain">* <fmt:message key="User.egAccessIp"/></td>
    </tr>

    <c:if test="${app2:isCompanyEnabledToMobileAccess(pageContext.request)}">
        <tr>
            <td class="label"><fmt:message key="User.mobile.enableAccess"/></td>
            <td class="contain">
                <html:checkbox property="dto(mobileActive)" styleId="mobileActive_id" disabled="${op == 'delete'}" tabindex="8" styleClass="radio"/>
            </td>
        </tr>

        <c:set var="userFormTemp" value="${userInfoForm}"/>
        <c:if test="${not empty userInfoUpdateForm}">
            <c:set var="userFormTemp" value="${userInfoUpdateForm}"/>
        </c:if>

        <c:if test="${hasViewPermissionWVAPP}">
            <tr>
                <td colspan="2" class="title"><fmt:message key="User.mobile.wvapp.title"/></td>
            </tr>
            <tr>
                <td class="label"><fmt:message key="User.mobile.wvapp.enableApp"/></td>
                <td class="contain">
                    <html:checkbox property="dto(enableMobileWVApp)" styleId="enableWVApp_id" onclick="enableWVAppClick()" disabled="${op == 'delete'}" tabindex="8" styleClass="radio"/>
                </td>
            </tr>

            <c:set var="isWVAppEnabled" value="${userFormTemp.dtoMap['enableMobileWVApp'] eq 'true'}"/>

            <c:set var="wvapp_display" value="display: none"/>
            <c:if test="${isWVAppEnabled}">
                <c:set var="wvapp_display" value="display: "/>
            </c:if>

            <tr id="tr_app_visible" style="${wvapp_display}">
                <td class="label"><fmt:message key="User.mobile.wvapp.visible"/></td>
                <td class="contain">
                    <html:checkbox property="dto(visibleMobileApp)" disabled="${op == 'delete'}" tabindex="8" styleClass="radio"/>
                </td>
            </tr>
            <tr id="tr_app_org" style="${wvapp_display}">
                <td class="label">
                    <fmt:message key="User.mobile.wvapp.organization"/>
                </td>
                <td class="contain">
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

                    <fanta:select property="dto(mobileOrganizationId)" styleId="organizationBox_id" listName="organizationContactPersonList" labelProperty="organizationName"
                                  valueProperty="addressId" module="/contacts" styleClass="mediumSelect" firstEmpty="true"
                                  readOnly="${op == 'delete'}" tabIndex="8">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="contactPersonId" value="${not empty contactPersonIdVar ? contactPersonIdVar : -1}"/>
                    </fanta:select>
                </td>
            </tr>
        </c:if>

    </c:if>

    <tr>
        <td colspan="3">
            <table width="100%" border="0" cellpadding="4" cellspacing="0">
                <tr>
                    <td class="label"><fmt:message key="User.available"/></td>
                    <td class="label"></td>
                    <td class="label"><fmt:message key="User.assigned"/></td>
                </tr>
                <tr>
                    <td class="contain">

                        <c:if test="${dto.availableRoles != null }">
                            <c:set var="availableRoles" value="${dto.availableRoles}" scope="request"/>
                        </c:if>

                        <html:select property="undefineRoles" styleClass="multipleSelect" tabindex="8" size="20"
                                     multiple="true" ondblclick="move(this.form.undefineRoles,this.form.defineRoles)"
                                     disabled="${op == 'delete'}">
                            <c:forEach var="item" items="${availableRoles}">
                                <html:option value="${item.roleId}">${item.roleName}</html:option>
                            </c:forEach>
                        </html:select>
                    </td>

                    <td class="label">

                        <html:button onclick="move(this.form.undefineRoles, this.form.defineRoles)" styleClass="button"
                                     property="B1" style="height:20px;width:40px;">
                            >
                        </html:button>
                        <br>
                        <html:button onclick="alls(this.form.undefineRoles, this.form.defineRoles)" styleClass="button"
                                     property="B11" style="height:20px;width:40px;">
                            >>
                        </html:button>
                        <br>
                        <html:button onclick="move(this.form.defineRoles, this.form.undefineRoles)" styleClass="button"
                                     property="B2" style="height:20px;width:40px;">
                            <
                        </html:button>
                        <br>
                        <html:button onclick="alls(this.form.defineRoles, this.form.undefineRoles)" styleClass="button"
                                     property="B22" style="height:20px;width:40px;">
                            <<
                        </html:button>
                    </td>

                    <td class="contain">

                        <c:if test="${dto.defineRoles != null }">
                            <c:set var="defineRoles" value="${dto.defineRoles}" scope="request"/>
                        </c:if>

                        <html:select property="defineRoles" size="20" multiple="true" styleClass="multipleSelect"
                                     indexed="10"
                                     disabled="${op == 'delete'}">
                            <c:forEach var="item" items="${defineRoles}">
                                <html:option value="${item.roleId}">${item.roleName}</html:option>
                            </c:forEach>
                        </html:select>
                    </td>
                </tr>
            </table>

        </td>
    </tr>

</table>
<table width="100%" border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td class="button">
            <app2:securitySubmit operation="${op}" onclick="send(defineRoles, undefineRoles)" functionality="USER"
                                 tabindex="15"
                                 styleClass="button" property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="16"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</table>
</html:form>

</td>
</tr>
</table>