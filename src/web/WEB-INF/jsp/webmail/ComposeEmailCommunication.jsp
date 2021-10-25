<%@ include file="/Includes.jsp" %>
<c:choose>
    <c:when test="${not empty modeShow}">
        <c:set var="modeLabel" value="${app2:getFormLabelClasses()}" scope="request"/>
        <c:set var="modeContain" value="${app2:getFormContainClasses(true)}" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="modeLabel" value="control-label col-sm-2 label-left" scope="request"/>
        <c:set var="modeContain" value="col-sm-9 form-control-static" scope="request"/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${'create' == op || 'true' == showEditor}">
        <script language="JavaScript" type="text/javascript">
            <!--
            function addAddressField(keyId, keyValue, nameId, nameValue) {
                document.getElementById(keyId).value = keyValue;
                document.getElementById(nameId).value = unescape(nameValue);
            }
            //-->
        </script>

        <c:set var="enableSaveSentItem" value="false" scope="request"/>
        <c:set var="enableCreateOutCommunications" value="false" scope="request"/>
        <c:set var="dynamicHiddens" value="${mainCommunicationForm.dtoMap['dynamicHiddens']}" scope="request"/>
        <c:set var="mailAccountId" value="${mainCommunicationForm.dtoMap['mailAccountId']}" scope="request"/>
        <c:set var="attachments" value="${mainCommunicationForm.dtoMap['attachments']}" scope="request"/>
        <c:set var="userMailId" value="${sessionScope.user.valueMap['userId']}" scope="request"/>
        <c:set var="startTabIndex" value="20" scope="request"/>
        <c:set var="composeObjectForm" value="${mainCommunicationForm}" scope="request"/>

        <!--
        when create email communications by default must be create communications and save email in sentItems folder
        -->
        <html:hidden property="dto(createOutCommunication)" value="true"/>
        <html:hidden property="dto(saveSendItem)" value="true"/>

        <!--
        By default all emails created have the field inOut (for communications) equals to out (0)
        -->
        <html:hidden property="dto(inOut)" value="0"/>

        <c:import url="/WEB-INF/jsp/webmail/ComposeFragment.jsp"/>
    </c:when>
    <c:otherwise>
        <html:hidden property="dto(sentDate)"/>
        <html:hidden property="dto(inOut)"/>
        <html:hidden property="dto(mailId)"/>
        <html:hidden property="dto(mailPriority)"/>
        <html:hidden property="dto(fromAddressId)"/>
        <html:hidden property="dto(fromAddressType)"/>
        <html:hidden property="dto(fromContactPersonId)"/>
        <html:hidden property="dto(bodyType)"/>
        <c:set var="emailPriority" value="${mainCommunicationForm.dtoMap['mailPriority']}" scope="request"/>
        <c:set var="fromAddressId" value="${mainCommunicationForm.dtoMap['fromAddressId']}" scope="request"/>
        <c:set var="fromAddressType" value="${mainCommunicationForm.dtoMap['fromAddressType']}" scope="request"/>
        <c:set var="fromContactPersonId" value="${mainCommunicationForm.dtoMap['fromContactPersonId']}"
               scope="request"/>
        <c:set var="sentDate" value="${mainCommunicationForm.dtoMap['sentDate']}" scope="request"/>
        <c:set var="bodyType" value="${mainCommunicationForm.dtoMap['bodyType']}" scope="request"/>
        <c:set var="mailId" value="${mainCommunicationForm.dtoMap['mailId']}" scope="request"/>
        <c:set var="attachments" value="${mainCommunicationForm.dtoMap['attachments']}" scope="request"/>
        <c:choose>
            <c:when test="${isCampGeneration}">
                <c:set var="htmlBodyPage"
                       value="/campaign/Mail/ExternalModule/ViewGenMailBody.do?dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}&dto(op)=readBody"
                       scope="request"/>

                <c:set var="downloadAttachmentUrl" value="/campaign/Download/GenerationAttach.do" scope="request"/>
            </c:when>
            <c:otherwise>
                <c:set var="htmlBodyPage"
                       value="/webmail/Mail/ViewMailBody.do?dto(mailId)=${mainCommunicationForm.dtoMap['mailId']}&dto(op)=readBody"
                       scope="request"/>
            </c:otherwise>
        </c:choose>
        <c:if test="${'' ==  mainCommunicationForm.dtoMap['cc']}">
            <c:set var="showRecipientsCC" value="false" scope="request"/>
        </c:if>


        <c:if test="${empty labelWidthAttach}">
            <c:set var="labelWidthAttach" value="20%" scope="request"/>
        </c:if>
        <c:if test="${empty containWidthAttach}">
            <c:set var="containWidthAttach" value="80%" scope="request"/>
        </c:if>
        <c:import url="/WEB-INF/jsp/webmail/ReadEmailFragment.jsp"/>
    </c:otherwise>
</c:choose>
