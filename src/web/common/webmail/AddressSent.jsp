<%@ page import="com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

<c:import url="/common/webmail/ReturnToMailTrayUrlFragment.jsp"/>

<tags:jscript language="JavaScript" src="/js/webmail/addressSent.jsp"/>

<c:set var="ADDRESSTYPE_PERSON" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="ADDRESSTYPE_ORGANIZATION" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>"/>
<c:set var="BLANK_KEY" value="<%= WebMailConstants.BLANK_KEY%>"/>

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

<html:form action="${action}" focus="${focusAddress}">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(userMailId)" value="${addressAddForm.dtoMap['userMailId']}"/>

    <c:forEach var="listAddr" items="${listAddress}">

        <html:hidden property="tempEmailIds" value="${listAddr.emailIdTemp}"
                     styleId="tempEmailIds_${listAddr.emailIdTemp}_Id"/>
        <html:hidden property="dto(addressId_${listAddr.emailIdTemp})" styleId="addressId_${listAddr.emailIdTemp}_Id"/>
        <html:hidden property="dto(contactPersonId_${listAddr.emailIdTemp})"
                     styleId="contactPersonId_${listAddr.emailIdTemp}_Id"/>
        <html:checkbox property="dto(isUpdate_${listAddr.emailIdTemp})" style="visibility:hidden;position:absolute"/>

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

        <table id="AddressSent.jsp" border="0" cellpadding="0" cellspacing="0" width="95%" align="center"
               class="container">
            <tr>
                <td colspan="2" class="title">
                    <fmt:message key="Common.add"/>
                </td>
            </tr>
            <tr class="listRow">

                <td class="topLabel" width="25%">
                    <app:text property="dto(email_${listAddr.emailIdTemp})" value="${listAddr.address}"
                              styleClass="middleText" view="true"/>
                </td>
                <td>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                        <tr id="trSelect_${listAddr.emailIdTemp}" style="${displaySelect}">
                            <td class="label" width="28%"><fmt:message key="Common.select"/></td>
                            <td class="label">
                                <html:select property="dto(addressType_${listAddr.emailIdTemp})" styleClass="select"
                                             onkeyup="javascript:hiddenTable('${listAddr.emailIdTemp}')"
                                             onchange="javascript:hiddenTable('${listAddr.emailIdTemp}')" tabindex="1">
                                    <html:option value="${BLANK_KEY}">&nbsp;</html:option>
                                    <html:option value="${ADDRESSTYPE_PERSON}"> <fmt:message key="Contact.Person.new"/>
                                    </html:option>
                                    <html:option value="${ADDRESSTYPE_ORGANIZATION}"> <fmt:message
                                            key="Contact.Organization.new"/> </html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <table id="tablePerson_${listAddr.emailIdTemp}" style="${displayPer}" border="0"
                                       cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                                    <tr class="listRow">
                                        <td colspan="2" class="listItem">
                                            <fmt:message key="Contact.Person.new"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <TD class="label" width="28%"><fmt:message key="Contact.Person.lastname"/></TD>
                                        <TD class="contain">
                                            <app:text property="dto(PerName1_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="40" tabindex="1"/>
                                            <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                                <tags:selectPopup
                                                        url="/webmail/Mail/Forward/AddressImportListForUpdate.do"
                                                        name="${listAddr.emailIdTemp}"
                                                        titleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                        width="755" heigth="480"/>
                                            </app2:checkAccessRight>
                                        </TD>
                                    </tr>
                                    <tr>
                                        <TD class="label"><fmt:message key="Contact.Person.firstname"/></TD>
                                        <TD class="contain">
                                            <app:text property="dto(PerName2_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="40" tabindex="1"/>
                                            <html:hidden property="dto(PerName3_${listAddr.emailIdTemp})"
                                                         styleId="PerName3_${listAddr.emailIdTemp}_Id"/>
                                        </TD>
                                    </tr>
                                    <tr>
                                        <TD class="label"><fmt:message key="Contact.Person.searchName"/></TD>
                                        <TD class="contain">
                                            <app:text property="dto(PerSearchName_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="60" tabindex="1"/>
                                        </TD>
                                    </tr>
                                </table>

                                <table id="tableOrg_${listAddr.emailIdTemp}" style="${displayOrg}" border="0"
                                       cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                                    <tr class="listRow">
                                        <td colspan="2" class="listItem">
                                            <fmt:message key="Contact.Organization.new"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <TD class="topLabel" width="28%" rowspan="3"><fmt:message
                                                key="Contact.Organization.name"/></TD>
                                        <TD class="contain">
                                            <app:text property="dto(OrgName1_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="40" tabindex="1"/>
                                            <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                                <tags:selectPopup
                                                        url="/webmail/Mail/Forward/AddressImportListForUpdate.do"
                                                        name="${listAddr.emailIdTemp}"
                                                        titleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                        width="755" heigth="480"/>
                                            </app2:checkAccessRight>
                                        </TD>
                                    </tr>
                                    <tr>
                                        <TD class="contain">
                                            <app:text property="dto(OrgName2_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="40" tabindex="1"/>
                                        </TD>
                                    </tr>
                                    <tr>
                                        <TD class="contain">
                                            <app:text property="dto(OrgName3_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="40" tabindex="1"/>
                                        </TD>
                                    </tr>
                                    <tr>
                                        <TD class="label"><fmt:message key="Contact.Organization.searchName"/></TD>
                                        <TD class="contain">
                                            <app:text property="dto(OrgSearchName_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="60" tabindex="1"/>
                                        </TD>
                                    </tr>
                                </table>

                                <table id="edit_${listAddr.emailIdTemp}" style="${displayEdit}" border="0"
                                       cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                                    <tr class="listRow">
                                        <td colspan="2" class="listItem">
                                            <fmt:message key="Webmail.Address.addInContact"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="label" width="28%"><fmt:message key="Common.name"/></td>
                                        <TD class="contain">
                                            <app:text property="dto(EditName_${listAddr.emailIdTemp})"
                                                      styleClass="middleText" maxlength="40" tabindex="1"
                                                      readonly="true"/>
                                            <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                                <tags:selectPopup
                                                        url="/webmail/Mail/Forward/AddressImportListForUpdate.do"
                                                        name="${listAddr.emailIdTemp}"
                                                        titleKey="Webmail.Contact.searchContactsOrContactPerson"
                                                        width="755" heigth="480"/>
                                                <a href="javascript:clearContactSelectPopup('${listAddr.emailIdTemp}')"
                                                   title="<fmt:message key="Common.clear"/>"><img
                                                        src="${pageContext.request.contextPath}/layout/ui/img/clear.png"
                                                        border="0"></a>
                                            </app2:checkAccessRight>
                                        </TD>
                                    </tr>
                                </table>

                            </td>
                        </tr>
                        <tr id="trTelecomType_${listAddr.emailIdTemp}" style="${displayTelecomType}">
                            <td class="label" width="28%"><fmt:message key="Telecom.telecomType"/></td>
                            <td class="contain">
                                <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                                <html:select property="dto(telecomTypeId_${listAddr.emailIdTemp})"
                                             styleClass="shortSelect" tabindex="1">
                                    <html:option value="">&nbsp;</html:option>
                                    <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                                </html:select>
                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
        </table>
    </c:forEach>

    <table cellSpacing=0 cellPadding=4 width="95%" border=0 align="center">
        <tr>
            <td class="button">
                <app2:securitySubmit operation="create" functionality="CONTACT" styleClass="button"
                                     tabindex="1"><fmt:message key="Common.save"/></app2:securitySubmit>
                <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" tabindex="2">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
    </table>

</html:form>