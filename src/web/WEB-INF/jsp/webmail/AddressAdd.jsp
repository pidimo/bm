<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>

<tags:jscript language="JavaScript" src="/js/webmail/addressAdd.jsp"/>

<c:set var="ADDRESSTYPE_PERSON" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="ADDRESSTYPE_ORGANIZATION" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>"/>

<%--set focus--%>
<c:set var="addr" value="${addressData}"/>
<c:choose>
    <c:when test="${addr.isUpdate == 'true'}">
        <c:set var="focusAddress" value="dto(email)"/>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${ addr.typeAddress == ADDRESSTYPE_PERSON}"> <!-- address type is person -->
                <c:set var="focusAddress" value="dto(PerName1)"/>
            </c:when>
            <c:when test="${ addr.typeAddress == ADDRESSTYPE_ORGANIZATION}"> <!-- address type is Organization -->
                <c:set var="focusAddress" value="dto(OrgName1)"/>
            </c:when>
            <c:otherwise>
                <c:set var="focusAddress" value="dto(PerName1)"/>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<html:form action="${action}?dto(mailGroupAddrId)=${param['dto(mailGroupAddrId)']}" focus="${focusAddress}"
           styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(userMailId)" value="${sessionScope.user.valueMap['userId']}"/>
    <html:hidden property="dto(addressId)"/>
    <html:hidden property="dto(contactPersonId)"/>
    <html:checkbox property="dto(isUpdate)" style="visibility:hidden;position:absolute"/>
    <%-- data of contact groups  --%>
    <html:hidden property="dto(mailGroupAddrId)"/>

    <c:choose>
        <c:when test="${addr.isUpdate == 'true'}">
            <c:set var="displayEdit" value=""/>
            <c:set var="displayPer" value="display:none"/>
            <c:set var="displayOrg" value="display:none"/>
            <c:set var="displaySelect" value="display:none"/>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${ addr.typeAddress == ADDRESSTYPE_PERSON}"> <!-- address type is person -->
                    <c:set var="displayEdit" value="display:none"/>
                    <c:set var="displayPer" value=""/>
                    <c:set var="displayOrg" value="display:none"/>
                    <c:set var="displaySelect" value=""/>
                </c:when>
                <c:when test="${ addr.typeAddress == ADDRESSTYPE_ORGANIZATION}"> <!-- address type is Organization -->
                    <c:set var="displayEdit" value="display:none"/>
                    <c:set var="displayPer" value="display:none"/>
                    <c:set var="displayOrg" value=""/>
                    <c:set var="displaySelect" value=""/>
                </c:when>
                <c:otherwise>
                    <c:set var="displayEdit" value="display:none"/>
                    <c:set var="displayPer" value=""/>
                    <c:set var="displayOrg" value="display:none"/>
                    <c:set var="displaySelect" value=""/>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

    <div class="${app2:getFormClasses()}">
        <div id="AddressSent.jsp" class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="WebMail.contact.new"/>
                </legend>

                <div id="trSelect" style="${displaySelect}" class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="addressType_id">
                        <fmt:message key="Common.select"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <html:select property="dto(addressType)" styleId="addressType_id"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     onkeyup="javascript:hiddenTable()" onchange="javascript:hiddenTable()"
                                     tabindex="1">
                            <html:option value="${ADDRESSTYPE_PERSON}">
                                <fmt:message key="Contact.Person.new"/>
                            </html:option>
                            <html:option value="${ADDRESSTYPE_ORGANIZATION}"> <fmt:message
                                    key="Contact.Organization.new"/>
                            </html:option>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div>
                    <div id="tablePerson" style="${displayPer}">
                        <legend class="title listItem">
                            <fmt:message key="Contact.Person.new"/>
                        </legend>

                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="PerName1_id">
                                <fmt:message key="Contact.Person.lastname"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <app:text property="dto(PerName1)" styleId="PerName1_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="2"/>
                                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                    <%--<tags:selectPopup url="/webmail/Mail/Forward/AddressImportListForUpdate.do"  name="selectPopup" titleKey="Webmail.Contact.searchContactsOrContactPerson" width="755" heigth="480"/>--%>
                                </app2:checkAccessRight>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="PerName2_id">
                                <fmt:message key="Contact.Person.firstname"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <app:text property="dto(PerName2)" styleId="PerName2_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="3"/>
                                <html:hidden property="dto(PerName3)" styleId="PerName3_Id"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="PerSearchName_id">
                                <fmt:message key="Contact.Person.searchName"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <app:text property="dto(PerSearchName)"
                                          styleId="PerSearchName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="60"
                                          tabindex="4"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div id="tableOrg" style="${displayOrg}">
                        <legend class="title listItem">
                            <fmt:message key="Contact.Organization.new"/>
                        </legend>

                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="OrgName1_id">
                                <fmt:message key="Contact.Organization.name"/>
                            </label>

                            <div class="${app2:getFormContainClasses(view)}">
                                <app:text property="dto(OrgName1)" styleId="OrgName1_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="2"/>
                                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                    <%--<tags:selectPopup url="/webmail/Mail/Forward/AddressImportListForUpdate.do"  name="selectPopup" titleKey="Webmail.Contact.searchContactsOrContactPerson" width="755" heigth="480"/>--%>
                                </app2:checkAccessRight>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <div class="${app2:getFormContainClasses(true)} col-sm-offset-3">
                                <app:text property="dto(OrgName2)" styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="3"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <div class="${app2:getFormContainClasses(view)} col-sm-offset-3">
                                <app:text property="dto(OrgName3)" styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          tabindex="4"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="OrgSearchName_id">
                                <fmt:message key="Contact.Organization.searchName"/>
                            </label>

                            <div class="${app2:getFormContainClasses(view)}">
                                <app:text property="dto(OrgSearchName)"
                                          styleId="OrgSearchName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="60"
                                          tabindex="5"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div id="tableEdit" style="${displayEdit}">
                        <legend class="title listItem">
                            <fmt:message key="Webmail.Address.addInContact"/>
                        </legend>

                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}" for="EditName_id">
                                <fmt:message key="Common.name"/>
                            </label>

                            <div class="${app2:getFormContainClasses(view)}">
                                <app:text property="dto(EditName)" styleId="EditName_id"
                                          styleClass="middleText ${app2:getFormInputClasses()}"
                                          maxlength="60"
                                          tabindex="2" readonly="true"/>
                                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                    <%--<tags:selectPopup url="/webmail/Mail/Forward/AddressImportListForUpdate.do"  name="selectPopup" titleKey="Webmail.Contact.searchContactsOrContactPerson" width="755" heigth="480"/>
                                    <a href="javascript:clearContactSelectPopup()" title="<fmt:message key="Common.clear"/>" ><img src="${pageContext.request.contextPath}/layout/ui/img/clear.gif" border="0"></a>--%>
                                </app2:checkAccessRight>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="email_id">
                        <fmt:message key="Common.email"/>
                    </label>

                    <div class="${app2:getFormContainClasses(view)}">
                        <app:text property="dto(email)" styleId="email_id"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="80" tabindex="7"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="telecomTypeId_id">
                        <fmt:message key="Telecom.telecomType"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                        <html:select property="dto(telecomTypeId)" styleId="telecomTypeId_id"
                                     styleClass="shortSelect ${app2:getFormSelectClasses()}" tabindex="8">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="CONTACT" property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="20">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:submit property="dto(cancel)" styleClass="${app2:getFormButtonCancelClasses()}" tabindex="21">
                <fmt:message key="Common.cancel"/>
            </html:submit>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="contactAddForm"/>
