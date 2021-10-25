<%@ include file="/Includes.jsp" %>
<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<calendar:initialize/>

<c:set var="isCampGeneration" value="${false}" scope="request"/>
<c:if test="${not empty mainCommunicationForm.dtoMap['contactId']}">
    <c:set var="isCampGeneration"
           value="${app2:isCampaignGenerationCommunication(mainCommunicationForm.dtoMap['contactId'])}"
           scope="request"/>
</c:if>

<c:set var="view"
       value="${'delete' == op || (email == mainCommunicationForm.dtoMap['type'] && 'update' == op) || isCampGeneration}"/>

<c:url value="MainCommunication/ReadToCompose.do" var="redirectEmail">
    <c:param name="dto(redirect)" value="true"/>
    <c:param name="dto(contactId)" value="${mainCommunicationForm.dtoMap.contactId}"/>
    <c:param name="dto(type)" value="${email}"/>
    <c:param name="dto(note)" value="${mainCommunicationForm.dtoMap.note}"/>
    <c:param name="dto(note)" value="${mainCommunicationForm.dtoMap.note}"/>
</c:url>

<app2:jScriptUrl
        url="/webmail/Mail/Print.do?dto(mailId)=${mainCommunicationForm.dtoMap['mailId']}&dto(userMailId)=${sessionScope.user.valueMap['userId']}"
        var="jsPrintUrl" addModuleParams="false"/>


<app2:jScriptUrl
        url="/contacts/MainCommunication/Print.do?dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}"
        var="jsPrintCommunicationUrl" addModuleParams="false"/>

<app:url
        value="/MainCommunication/Reply.do?dto(mailId)=${mainCommunicationForm.dtoMap['mailId']}&dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}&dto(type)=${email}&dto(note)=${mainCommunicationForm.dtoMap['note']}&replyOperation=REPLY&dto(filterInvalidRecipients)=true"
        var="replyUrl"/>
<app:url
        value="/MainCommunication/Reply.do?dto(mailId)=${mainCommunicationForm.dtoMap['mailId']}&dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}&dto(type)=${email}&dto(note)=${mainCommunicationForm.dtoMap['note']}&replyOperation=REPLYALL&dto(filterInvalidRecipients)=true"
        var="replyAllUrl"/>
<app:url
        value="/MainCommunication/Reply.do?dto(mailId)=${mainCommunicationForm.dtoMap['mailId']}&dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}&dto(type)=${email}&dto(note)=${mainCommunicationForm.dtoMap['note']}&replyOperation=FORWARD&dto(filterInvalidRecipients)=true"
        var="forwardUrl"/>

<%--url to send invoice via email--%>
<app2:jScriptUrl url="/contacts/MainCommunication/WebDocument/SendViaEmail.do?dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}"
                 var="jsWebDocSendViaEmailUrl">
    <app2:jScriptUrlParam param="telecomId" value="telecomId"/>
</app2:jScriptUrl>


<script type="text/javascript">
    function reply() {
        location.href = '${replyUrl}';
    }

    function replyAll() {
        location.href = '${replyAllUrl}';
    }
    function forwardEmail() {
        location.href = '${forwardUrl}';
    }

    function changeCommunicationType(obj) {
        document.getElementById("changeCommunicationTypeId").value = "true";
        document.forms[0].submit();
    }

    function resend() {
        window.location = '<app:url value="${redirectEmail}" addModuleName="true"/>';
    }

    function printMail() {
        window.open(${jsPrintUrl}, 'printMail', 'resizable=yes,width=686, height=650,left=50, top=10, scrollbars=yes');
    }

    function printCommunication() {
        window.open(${jsPrintCommunicationUrl}, 'printCommunicationUrl', 'resizable=yes,width=686, height=650,left=50, top=10, scrollbars=yes');
    }

    function sendWebDocumentViaEmail(emailBoxId) {
        var telecomId = document.getElementById(emailBoxId).value;
        location.href = ${jsWebDocSendViaEmailUrl};
    }

</script>


<html:form action="${action}" enctype="multipart/form-data" styleId="composeEmailId" focus="dto(type)">

    <html:hidden property="dto(op)" value="${op}" styleId="op"/>
    <html:hidden property="dto(changeCommunicationType)" value="false" styleId="changeCommunicationTypeId"/>
    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

    <c:if test="${'create' == op}">
        <html:hidden property="dto(status)" value="0"/>
        <html:hidden property="dto(redirect)" styleId="redirectId"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(contactId)"/>
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(status)"/>
        <html:hidden property="dto(isAction)"/>
        <html:hidden
                property="dto(rebuildDocument)"/> <!--this field is as flag, set in true when the document (fax,letter) should be regererated-->
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(contactId)"/>
        <html:hidden property="dto(status)"/>
        <html:hidden property="dto(isAction)"/>
    </c:if>

    <table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="2">
                <c:set var="showAditionalLinks" value="true" scope="request"/>
                <c:set var="communicationButtonsTabIndex" value="40" scope="request"/>
                <c:import url="${communicationButtons}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="title">${title}</td>
        </tr>
        <tr>
            <td class="label" width="20%">
                <fmt:message key="Communication.type"/>
            </td>
            <td class="contain" width="80%">
                <c:set var="communicationTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                <html:select property="dto(type)" styleClass="mediumSelect" onchange="changeCommunicationType(this)"
                             readonly="${op != 'create' || true == mainCommunicationForm.dtoMap['redirect']}"
                             tabindex="1">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="communicationTypes" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <c:if test="${email != mainCommunicationForm.dtoMap['type']}">
            <tr>
                <fmt:message var="datePattern" key="datePattern"/>
                <td class="label">
                    <fmt:message key="Document.date"/>
                </td>
                <td class="contain">
                    <c:choose>
                        <c:when test="${email == mainCommunicationForm.dtoMap['type']}">
                            <app:dateText property="dto(dateStart)" styleId="dateStart"
                                          calendarPicker="false"
                                          datePatternKey="${datePattern}" styleClass="mediumText"
                                          view="true"
                                          maxlength="10" tabindex="2" currentDate="true"
                                          readonly="true"/>
                        </c:when>
                        <c:otherwise>
                            <app:dateText property="dto(dateStart)" styleId="dateStart"
                                          calendarPicker="${false == view}"
                                          datePatternKey="${datePattern}" styleClass="mediumText"
                                          view="${view}"
                                          maxlength="10" tabindex="2" currentDate="true"
                                          readonly="${view}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Document.subject"/>
                </td>
                <td class="contain">
                    <app:text property="dto(note)"
                              styleClass="mediumText"
                              maxlength="${40}" view="${view}" tabindex="3"/>
                </td>
            </tr>
        </c:if>
        <tr>
            <td class="label">
                <fmt:message key="Document.employee"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(employeeId)" listName="employeeBaseList"
                              labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                              readOnly="${op == 'delete' || email == mainCommunicationForm.dtoMap['type'] || isCampGeneration}"
                              value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts" tabIndex="4">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>

        <c:import url="${moduleCommunication}"/>

        <tr>
            <td colspan="2">
                <c:set var="showAditionalLinks" value="false" scope="request"/>
                <c:set var="communicationButtonsTabIndex" value="25" scope="request"/>
                <c:import url="${communicationButtons}"/>
            </td>
        </tr>
    </table>
</html:form>