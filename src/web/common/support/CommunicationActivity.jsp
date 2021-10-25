<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<tags:initSelectPopup/>
<calendar:initialize/>
<fmt:message var="datePattern" key="datePattern" scope="request"/>

<%--<c:set var="onlyView" value="${op == 'delete' || dto.status == '1'}"/>--%>

<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<%--<html:hidden property="dto(addressId)" value="${isFromSalesProcess ? param.addressId : param.contactId}"/>--%>
<html:hidden property="dto(isHtml)"/>
<html:hidden property="dto(redirect)"/>
<html:hidden property="dto(mailId)"/>
<html:hidden property="dto(complementOperation)" value="${complementOperations}"/>

<c:choose>
    <c:when test="${op == 'create'}">
        <html:hidden property="dto(status)" value="0"/>
    </c:when>
    <c:otherwise>
        <html:hidden property="dto(status)"/>

        <html:hidden property="dto(commVersion)"/>

    </c:otherwise>
</c:choose>
<c:if test="${caseActivityForm.dtoMap.type != email}">
    <tr>
        <td class="label" width="15%"><fmt:message key="Document.subject"/></td>
        <td class="contain" width="35%">
            <app:text property="dto(note)" onchange="checkComm()" styleClass="mediumText" maxlength="40"
                      view="${onlyView || !isNewMail}" tabindex="13"/>
        </td>
        <td class="label" width="15%"><fmt:message key="Contact.title"/></td>
        <td class="contain" width="35%">

            <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
            <app:text property="dto(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                      tabindex="17" readonly="true" view="${onlyView || !isNewMail}"/>

            <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                              hide="${onlyView || !isNewMail}" submitOnSelect="true" tabindex="18"/>
            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id" tabindex="19"
                                   titleKey="Common.clear" submitOnClear="true" hide="${onlyView || !isNewMail}"/>

        </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Common.date"/></td>
        <td class="contain">
            <app:dateText property="dto(dateStart)" styleId="dateStart"
                          onchange="checkComm()"
                          calendarPicker="${onlyView && caseActivityForm.dtoMap.type!=email}"
                          datePatternKey="${datePattern}" styleClass="mediumText" view="${onlyView || !isNewMail}"
                          maxlength="10" tabindex="14" currentDate="true"
                          readonly="${caseActivityForm.dtoMap.type==email}"/>
        </td>

        <td class="label" width="15%"><fmt:message key="ContactPerson.title"/></td>
        <td class="contain" width="35%">
            <fanta:select property="dto(contactPersonId)" tabIndex="20" listName="searchContactPersonList"
                          onChange="checkCommSubmit(${isNewMail && caseActivityForm.dtoMap.type == email})"
                          module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                          valueProperty="contactPersonId" styleClass="middleSelect"
                          readOnly="${onlyView || !isNewMail}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty caseActivityForm.dtoMap.addressId?caseActivityForm.dtoMap.addressId:0}"/>
            </fanta:select>
        </td>
    </tr>
</c:if>
<tr>
    <td class="label"><fmt:message key="Document.employee"/></td>
    <td class="contain">
        <fanta:select property="dto(employeeId)" listName="employeeBaseList"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                      readOnly="${onlyView || caseActivityForm.dtoMap.type == email}"
                      value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts" tabIndex="15">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label"><fmt:message key="Document.inout"/></td>
    <td class="contain">
        <html:radio property="dto(inOut)" onchange="checkComm()" styleId="inout1" value="1" styleClass="radio"
                    disabled="${onlyView || !isNewMail || (caseActivityForm.dtoMap.type == email)}" tabindex="21">&nbsp;<fmt:message
                key="Document.in"/>
        </html:radio>&nbsp;
        <html:radio property="dto(inOut)" onchange="checkComm()" styleId="inout2" value="0" styleClass="radio"
                    disabled="${onlyView || !isNewMail || (caseActivityForm.dtoMap.type == email)}" tabindex="22">&nbsp;<fmt:message
                key="Document.out"/>
        </html:radio>
        <c:choose>
            <c:when test="${onlyView || (!isNewMail && caseActivityForm.dtoMap.type == email)}">
            </c:when>
            <c:otherwise>
                <html:hidden property="dto(inOutValidate)" value="true"/>
            </c:otherwise>
        </c:choose>

    </td>
</tr>

<c:choose>
    <c:when test="${caseActivityForm.dtoMap.type == fax || caseActivityForm.dtoMap.type == letter}">
        <c:import url="/common/support/CommunicationFaxLetterFragment.jsp"/>
    </c:when>
    <c:when test="${caseActivityForm.dtoMap.type == phone || caseActivityForm.dtoMap.type== meeting || caseActivityForm.dtoMap.type== other}">
        <tr>
            <td class="topLabel" colspan="4"><fmt:message key="Document.content"/><br>
                <html:textarea property="dto(freeText)" onchange="checkComm()" styleClass="mediumDetail"
                               readonly="${onlyView}" tabindex="24"
                               style="height:120px;width:100%;"/>&nbsp;
            </td>
        </tr>
    </c:when>
    <c:when test="${document == caseActivityForm.dtoMap.type}">
        <TR>
            <TD class="label">
                <fmt:message key="Communication.document"/>
            </TD>
            <TD class="contain" colspan="3">
                <fmt:message key="Common.download" var="downloadMsg"/>
                <c:if test="${!onlyView}">
                    <html:file property="dto(documentFile)"/>&nbsp;
                </c:if>
                <c:if test="${null != caseActivityForm.dtoMap.contactId}">
                    <html:hidden property="dto(documentFileName)"/>

                    <app:link
                            action="/CaseActivity/Document/Download.do?communicationId=${caseActivityForm.dtoMap.contactId}&dto(activityId)=${caseActivityForm.dtoMap.activityId}&caseId=${caseActivityForm.dtoMap.caseId}"
                            title="${downloadMsg}"
                            addModuleParams="true">
                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/openfile.png"
                             alt="${downloadMsg}" border="0" align="middle"/>
                    </app:link>
                </c:if>
                <c:if test="${onlyView}">
                    <c:out value="${caseActivityForm.dtoMap['documentFileName']}"/>
                </c:if>
            </TD>
        </TR>
    </c:when>
    <c:when test="${caseActivityForm.dtoMap.type == email}">
        <tr>
            <td colspan="4">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <c:set var="mainCommunicationForm" value="${caseActivityForm}" scope="request"/>
                    <c:if test="${'update' == op && 'true' == caseActivityForm.dtoMap['redirectValidation'] && (null == caseActivityForm.dtoMap.mailId || empty caseActivityForm.dtoMap.mailId)}">
                        <c:set var="showEditor" value="true" scope="request"/>
                        <c:set var="isSupportActivity" value="true" scope="request"/>
                    </c:if>
                    <c:import url="/webmail/ComposeEmailCommunication.jsp"/>
                </table>
            </td>
        </tr>
    </c:when>
</c:choose>
