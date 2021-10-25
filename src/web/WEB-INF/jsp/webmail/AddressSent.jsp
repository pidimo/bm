<%@ page import="com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>

<c:import url="/WEB-INF/jsp/webmail/ReturnToMailTrayUrlFragment.jsp"/>

<tags:jscript language="JavaScript" src="/WEB-INF/jsp/js/webmail/addressSent.jsp"/>

<c:set var="ADDRESSTYPE_PERSON" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="ADDRESSTYPE_ORGANIZATION" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>"/>
<c:set var="BLANK_KEY" value="<%= WebMailConstants.BLANK_KEY%>"/>

<script type="text/javascript">
    function setEmailIdForSelectPopup(emailIdTemp) {
        var elem = document.getElementById("emailIdSelectPopup");
        elem.value = emailIdTemp;
    }

    function readEmailIdTemp() {
        var readElem = document.getElementById("emailIdSelectPopup");
        return readElem.value;
    }
</script>

<c:choose>
    <c:when test="${listAddress[0].isUpdate == 'true'}">
        <c:set var="focusAddress" value="dto(telecomTypeId_${listAddress[0].emailIdTemp})"/>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${ listAddress[0].typeAddress == ADDRESSTYPE_PERSON}"> <!-- address type is person -->
                <c:set var="focusAddress" value="dto(PerName1_${listAddress[0].emailIdTemp})"/>
            </c:when>
            <c:when test="${ listAddress[0].typeAddress == ADDRESSTYPE_ORGANIZATION}"> <!-- address type is Organization -->
                <c:set var="focusAddress" value="dto(OrgName1_${listAddress[0].emailIdTemp})"/>
            </c:when>
            <c:when test="${ listAddress[0].typeAddress == BLANK_KEY}">
                <c:set var="focusAddress" value="dto(addressType_${listAddress[0].emailIdTemp})"/>
            </c:when>
        </c:choose>
    </c:otherwise>
</c:choose>

<html:form action="${action}" focus="${focusAddress}" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(userMailId)" value="${addressAddForm.dtoMap['userMailId']}"/>
    <html:hidden property="dto(emaiIdForSelectPopup)" styleId="emailIdSelectPopup"/>

    <c:forEach var="listAddr" items="${listAddress}">

        <html:hidden property="tempEmailIds" value="${listAddr.emailIdTemp}"
                     styleId="tempEmailIds_${listAddr.emailIdTemp}_Id"/>
        <html:hidden property="dto(addressId_${listAddr.emailIdTemp})"
                     styleId="addressId_${listAddr.emailIdTemp}_Id"/>
        <html:hidden property="dto(contactPersonId_${listAddr.emailIdTemp})"
                     styleId="contactPersonId_${listAddr.emailIdTemp}_Id"/>
        <html:checkbox property="dto(isUpdate_${listAddr.emailIdTemp})"
                       style="visibility:hidden;position:absolute"/>

        <c:choose>
            <c:when test="${listAddr.isUpdate == 'true'}">
                <c:set var="displayEdit" value=""/>
                <c:set var="displayPer" value="display:none"/>
                <c:set var="displayOrg" value="display:none"/>
                <c:set var="displaySelect" value="display:none"/>
                <c:set var="displayTelecomType" value=""/>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${ listAddr.typeAddress == ADDRESSTYPE_PERSON}"> <!-- address type is person -->
                        <c:set var="displayEdit" value="display:none"/>
                        <c:set var="displayPer" value=""/>
                        <c:set var="displayOrg" value="display:none"/>
                        <c:set var="displaySelect" value=""/>
                        <c:set var="displayTelecomType" value=""/>
                    </c:when>
                    <c:when test="${ listAddr.typeAddress == ADDRESSTYPE_ORGANIZATION}"> <!-- address type is Organization -->
                        <c:set var="displayEdit" value="display:none"/>
                        <c:set var="displayPer" value="display:none"/>
                        <c:set var="displayOrg" value=""/>
                        <c:set var="displaySelect" value=""/>
                        <c:set var="displayTelecomType" value=""/>
                    </c:when>
                    <c:when test="${ listAddr.typeAddress == BLANK_KEY}"> <!-- address type is blank -->
                        <c:set var="displayEdit" value="display:none"/>
                        <c:set var="displayPer" value="display:none"/>
                        <c:set var="displayOrg" value="display:none"/>
                        <c:set var="displaySelect" value=""/>
                        <c:set var="displayTelecomType" value="display:none"/>
                    </c:when>
                </c:choose>
            </c:otherwise>
        </c:choose>

        <div id="AddressSent.jsp" class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Common.add"/>
                </legend>
                <div class="col-xs-12 col-sm-4 col-md-3">
                    <app:text property="dto(email_${listAddr.emailIdTemp})" value="${listAddr.address}"
                              styleClass="${app2:getFormInputClasses()} middleText" view="true"/>
                </div>
                <div class="col-xs-12 col-sm-8 col-md-9">
                    <div id="trSelect_${listAddr.emailIdTemp}" style="${displaySelect}"
                         class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Common.select"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <html:select property="dto(addressType_${listAddr.emailIdTemp})"
                                         styleClass="${app2:getFormSelectClasses()}"
                                         onkeyup="javascript:hiddenTable('${listAddr.emailIdTemp}')"
                                         onchange="javascript:hiddenTable('${listAddr.emailIdTemp}')"
                                         tabindex="1">
                                <html:option value="${BLANK_KEY}">&nbsp;</html:option>
                                <html:option value="${ADDRESSTYPE_PERSON}">
                                    <fmt:message key="Contact.Person.new"/>
                                </html:option>
                                <html:option value="${ADDRESSTYPE_ORGANIZATION}">
                                    <fmt:message key="Contact.Organization.new"/>
                                </html:option>
                            </html:select>
                        </div>
                    </div>

                    <div id="tablePerson_${listAddr.emailIdTemp}" style="${displayPer}">
                        <legend class="title listItem">
                            <fmt:message key="Contact.Person.new"/>
                        </legend>

                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Contact.Person.lastname"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <div class="input-group">
                                    <app:text property="dto(PerName1_${listAddr.emailIdTemp})"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                              tabindex="1"/>
                                        <span class="input-group-btn">
                                            <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                                <tags:bootstrapSelectPopup styleId="${listAddr.emailIdTemp}_person_id"
                                                                           url="/webmail/Mail/Forward/AddressImportListForUpdate.do"
                                                                           name="${listAddr.emailIdTemp}"
                                                                           titleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                                           modalTitleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                                           tabindex="2"
                                                                           onShowJSFunction="setEmailIdForSelectPopup(${listAddr.emailIdTemp})"
                                                                           isLargeModal="true"
                                                        />
                                            </app2:checkAccessRight>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Contact.Person.firstname"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <app:text property="dto(PerName2_${listAddr.emailIdTemp})"
                                          styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                          tabindex="3"/>
                                <html:hidden property="dto(PerName3_${listAddr.emailIdTemp})"
                                             styleId="PerName3_${listAddr.emailIdTemp}_Id"/>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Contact.Person.searchName"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <app:text property="dto(PerSearchName_${listAddr.emailIdTemp})"
                                          styleClass="${app2:getFormInputClasses()} middleText" maxlength="60"
                                          tabindex="4"/>
                            </div>
                        </div>
                    </div>

                    <div id="tableOrg_${listAddr.emailIdTemp}" style="${displayOrg}">
                        <legend class="title listItem">
                            <fmt:message key="Contact.Organization.new"/>
                        </legend>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Contact.Organization.name"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <div class="input-group">
                                    <app:text property="dto(OrgName1_${listAddr.emailIdTemp})"
                                              styleClass="${app2:getFormSelectClasses()} middleText" maxlength="40"
                                              tabindex="1"/>
                                        <span class="input-group-btn">
                                            <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                                <tags:bootstrapSelectPopup
                                                        styleId="${listAddr.emailIdTemp}_organization_id"
                                                        url="/webmail/Mail/Forward/AddressImportListForUpdate.do"
                                                        name="${listAddr.emailIdTemp}"
                                                        titleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                        modalTitleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                        onShowJSFunction="setEmailIdForSelectPopup(${listAddr.emailIdTemp})"
                                                        isLargeModal="true"
                                                        tabindex="1"/>
                                            </app2:checkAccessRight>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <div class="${app2:getFormContainClasses(null)} col-sm-offset-3">
                                <app:text property="dto(OrgName2_${listAddr.emailIdTemp})"
                                          styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                          tabindex="2"/>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <div class="${app2:getFormContainClasses(null)} col-sm-offset-3">
                                <app:text property="dto(OrgName3_${listAddr.emailIdTemp})"
                                          styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                          tabindex="3"/>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Contact.Organization.searchName"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <app:text property="dto(OrgSearchName_${listAddr.emailIdTemp})"
                                          styleClass="${app2:getFormInputClasses()} middleText" maxlength="60"
                                          tabindex="4"/>
                            </div>
                        </div>
                    </div>

                    <div id="edit_${listAddr.emailIdTemp}" style="${displayEdit}">
                        <legend class="title listItem">
                            <fmt:message key="Webmail.Address.addInContact"/>
                        </legend>
                        <div class="${app2:getFormGroupClasses()}">
                            <label class="${app2:getFormLabelClasses()}">
                                <fmt:message key="Common.name"/>
                            </label>

                            <div class="${app2:getFormContainClasses(null)}">
                                <div class="input-group">
                                    <app:text property="dto(EditName_${listAddr.emailIdTemp})"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                              tabindex="5"
                                              readonly="true"/>
                                    <span class="input-group-btn">
                                        <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                            <tags:bootstrapSelectPopup styleId="${listAddr.emailIdTemp}_id"
                                                                       url="/webmail/Mail/Forward/AddressImportListForUpdate.do"
                                                                       name="${listAddr.emailIdTemp}"
                                                                       tabindex="5"
                                                                       titleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                                       modalTitleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                                       onShowJSFunction="setEmailIdForSelectPopup(${listAddr.emailIdTemp})"
                                                                       isLargeModal="true"/>
                                            <a tabindex="5"
                                               href="javascript:clearContactSelectPopup('${listAddr.emailIdTemp}')"
                                               title="<fmt:message key="Common.clear"/>"
                                               class="${app2:getFormButtonClasses()}">
                                                <span class="glyphicon glyphicon-erase"></span>
                                            </a>
                                        </app2:checkAccessRight>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="trTelecomType_${listAddr.emailIdTemp}" style="${displayTelecomType}"
                         class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Telecom.telecomType"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <c:set var="telecomTypes"
                                   value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                            <html:select property="dto(telecomTypeId_${listAddr.emailIdTemp})"
                                         styleClass="${app2:getFormSelectClasses()} shortSelect" tabindex="6">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="telecomTypes" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

            </fieldset>
        </div>
    </c:forEach>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="create" functionality="CONTACT" styleClass="${app2:getFormButtonClasses()}"
                             tabindex="7">
            <fmt:message key="Common.save"/>
        </app2:securitySubmit>
        <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}"
                     onclick="location.href='${urlCancel}'" tabindex="8">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>
</html:form>