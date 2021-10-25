<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/st-xmlhttpV2.jsp"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.blockUI-2.31.js"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.timer.js"/>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/cacheable/jquery-ui-1.10.0.smoothness.css"/>"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery-ui-1.10.0.custom.min.js"/>

<tags:initSelectPopup/>
<c:set var="KEY_SEPART" value="<%=CampaignConstants.KEY_SEPARATOR%>"/>
<c:set var="KEY_VALUE" value="<%=CampaignConstants.KEY_SEPARATOR_VALUE%>"/>
<c:set var="SEND_CURRENT" value="<%=CampaignConstants.CURRENT_USER_SEND%>"/>
<c:set var="ACTIVITY_RESP" value="<%=CampaignConstants.ACTIVITY_RESPONSIBLE%>"/>
<c:set var="CONTACT_RESP" value="<%=CampaignConstants.CONTACT_RESPONSIBLE%>"/>

<c:set var="definedByUserPrefix" value="<%=CampaignConstants.SenderPrefixType.DEFINEDBYUSER.getConstantAsString()%>"/>

<fmt:message var="KB_SIZE" key="Webmail.mailTray.Kb"/>
<c:set var="LIMIT_RECIPIENT" value="${app2:getConfigurationPropertyValue('elwis.campaignMail.background.send.recipient.limit')}" scope="request"/>
<c:set var="LIMIT_BODYSIZE" value="${app2:getConfigurationPropertyValue('elwis.campaignMail.background.send.size.limit')}" scope="request"/>
<c:if test="${empty LIMIT_BODYSIZE}">
    <c:set var="LIMIT_BODYSIZE" value="400" scope="request"/>
</c:if>

<app2:jScriptUrl url="/campaign/Campaign/ResponsibleTelecom.do?campaignId=${param.campaignId}" var="jsAjaxUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="senderEmployeeId" value="senderEmployee_id"/>
    <app2:jScriptUrlParam param="activityId" value="activity_id"/>
    <app2:jScriptUrlParam param="telecomTypeId" value="telecomType_id"/>
    <app2:jScriptUrlParam param="date" value="dateTime"/>
</app2:jScriptUrl>

<app2:jScriptUrl
        url="/campaign${urlWithoutEmail}?campaignId=${param.campaignId}&activityId=${emailSendForm.dtoMap['activityId']}"
        var="jsWithoutEmailUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="telecomTypeId" value="telecomType_id"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/Email/Send/BodySize/Ajax.do" var="jsBodySizeUrl">
    <app2:jScriptUrlParam param="dto(templateId)" value="templateId"/>
    <app2:jScriptUrlParam param="dto(genAttachIds)" value="attachIdsVar"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/Email/Send/BodySize/Ajax.do?generationId=${emailSendForm.dtoMap['generationId']}" var="jsBodySizeGenerationUrl">
</app2:jScriptUrl>

<app2:jScriptUrl url="/campaign/Email/Send/ProgressBar/Ajax.do" var="jsProgressBarUrl">
    <app2:jScriptUrlParam param="dto(generationKey)" value="generation_key"/>
</app2:jScriptUrl>

<c:url var="progressImg" value="/layout/ui/img/busy.gif"/>

<div id="waitMessage" style="display:none; text-align:left;">
    <img src="${progressImg}" alt=""/>
    <span style="font-size:10pt; color:#000000; font-family:Verdana,serif;">
        <fmt:message key="Campaign.mailing.wait"/>
    </span>
    <br/>
    <br/>

    <div id="progressbar"></div>

    <div id="progressSummary" style="font-size:10pt; color:#000000; font-family:Verdana,serif; text-align:left;">
    </div>
</div>

<script language="JavaScript" type="text/javascript">

    //progress bar functions
    function makeProgressBarAjaxRequest(urlAddress) {
        //OBS: async:false because with 'true' value this not work in chrome browser

        $.ajax({
            async:true,
            type: "POST",
            data: " ",
            dataType: "xml",
            url:urlAddress,
            success: function(data, status) {
                processSummaryXMLDoc(data);
            }
        });
    }

    function initTimerSendProgress() {
        var generation_key = document.getElementById("idGenerationKey").value;
        initProgressBar();

        //timer object
        var timer = $.timer(function() {
            makeProgressBarAjaxRequest(${jsProgressBarUrl});
        });
        timer.set({ time : 3000, autostart : true });
    }

    function initProgressBar(){
        //init the progress bar
        $("#progressbar").progressbar({ value: 0 });
    }

    function updateProgressBar(total, success, fail){
        var value = $("#progressbar").progressbar("value");
        var progressValue = parseInt(success) + parseInt(fail);

        if(value == 0) {
            //define the max
            $("#progressbar").progressbar( "option", "max", total );
        }
        $("#progressbar").progressbar("value", progressValue);
    }

    function processSummaryXMLDoc(xmldoc) {
        if (xmldoc.getElementsByTagName('progressBarSummary').length > 0) {
            //process xml document with jQuery
            $(xmldoc).find('summary').each(function() {

                var total = $(this).attr('total');
                var success = $(this).attr('success');
                var fail = $(this).attr('fail');
                var message = $(this).text();

                if(total > 0) {
                    updateProgressBar(total, success, fail);
                    setProgressMessage(message);
                }
            });
        }
    }

    function setProgressMessage(message) {
        if(message != "") {
            document.getElementById("progressSummary").innerHTML = message;
        }
    }

    function sendEmail() {
        document.getElementById('idAttachValues').value = selectAll();
    }
    function showWaitMessage() {
        $.blockUI({ message: $('#waitMessage') , css: {padding: '20px'}});
        initTimerSendProgress();
    }

    function sendButtonPressed() {
        if(!isSendLimitExceeded()) {
            showWaitMessage();
        }
    }

    //show recipients without email
    function showRecipientWithoutEmailPopup() {
        var telecomType_id = document.getElementById("idTelecomType").value;
        var w = 570;
        var h = 650;

        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - w / 2;
        var posy = winy - h / 2;
        poupWindow = window.open(${jsWithoutEmailUrl}, 'myWindow', 'resizable=yes,width=' + w + ',height=' + h + ',status,left=' + posx + ',top=' + posy + ',scrollbars=yes');
        poupWindow.focus();
    }

    //attach selection
    //get object by id or name
    function lib_getObj(id, d) {
        var i,x;
        if (!d) d = document;
        if (!(x = d[id]) && d.all) x = d.all[id];
        for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][id];
        for (i = 0; !x && d.layers && i < d.layers.length; i++) x = lib_getObj(id, d.layers[i].document);
        if (!x && document.getElementById) x = document.getElementById(id);
        return x;
    }

    function removeAttachments() {
        var fbox = document.getElementById("listAttachId");

        for (var i = 0; i < fbox.options.length; i++) {
            if (fbox.options[i].selected && fbox.options[i].value != "") {
                fbox.options[i].value = "";
                fbox.options[i].text = "";
            }
        }
        BumpUp(fbox);

        //recalculate the body sizes
        calculateBodySize();
    }

    function BumpUp(box) {
        for (var i = 0; i < box.options.length; i++) {
            if (box.options[i].value == "") {
                for (var j = i; j < box.options.length - 1; j++) {
                    box.options[j].value = box.options[j + 1].value;
                    box.options[j].text = box.options[j + 1].text;
                }
                var ln = i;
                break;
            }
        }
        if (ln < box.options.length) {
            box.options.length -= 1;
            BumpUp(box);
        }
    }

    function setAllUnSelected(box) {
        for (var i = 0; i < box.options.length; i++) {
            if (box.options[i].selected) {
                box.options[i].selected = false;
            }
        }
    }

    function existThisValue(selectBox, value) {
        for (var i = 0; i < selectBox.options.length; i++) {
            if (selectBox.options[i].value == value) {
                return "true";
            }
        }
        return "false";
    }

    function putSelectedAttach(arrayId, arrayName) {
        var box = document.getElementById("listAttachId");
        setAllUnSelected(box);

        for (var i = 0; i < arrayId.length; i++) {
            var no = new Option();
            no.value = arrayId[i];
            no.text = arrayName[i];

            if (existThisValue(box, no.value) == "false") {
                box.options[box.options.length] = no;
            }
        }

        //recalculate the body sizes
        calculateBodySize();
    }

    //attach ids be separate with ','
    function getAllAttachIds() {
        var selectBox = document.getElementById("listAttachId");
        var idAttachs = "";

        for (var i = 0; i < selectBox.options.length; i++) {
            if (i == 0) {
                idAttachs = selectBox.options[i].value;
            } else {
                idAttachs = idAttachs + "," + selectBox.options[i].value;
            }
        }
        return idAttachs;
    }

    function selectAll() {
        var tbox = document.getElementById("listAttachId");
        var res = "";
        for (var i = 0; i < tbox.options.length; i++) {
            if (i == 0) {
                res = tbox.options[i].text + '${KEY_VALUE}' + tbox.options[i].value;
            } else {
                res = res + '${KEY_SEPART}' + tbox.options[i].text + '${KEY_VALUE}' + tbox.options[i].value;
            }
        }
        return res;
    }

    function getSenderEmployeeTelecoms() {
        doSomething();
    }

    /*ajax management*/
    var ajaxObj = new Ajax();
    ajaxObj.setShowMessageDivId("msgId");
    function doSomething() {
        var dateTime = new Date().getTime();
        var activity_id = document.getElementById("idActivity").value;
        var telecomType_id = document.getElementById("idTelecomType").value;
        var senderEmployee_id = document.getElementById("idSenderEmployee").value;

        if (senderEmployee_id != undefined && telecomType_id != undefined) {
            var url = ${jsAjaxUrl};
            ajaxObj.makeHttpRequest(url, 'ajaxReadDocType', false);
        }
    }

    function ajaxReadDocType(tagSelect) {
        if (tagSelect != null && tagSelect != undefined) {
            document.getElementById("divTelecom").innerHTML = tagSelect;
        } else {
            return false;
        }
        document.getElementById("divTelecom").style.display = '';
    }

    function prefixSelected(prefixBox){
        if(prefixBox.value == '${definedByUserPrefix}'){
            document.getElementById('senderPrefix_id').style.display = '';
        } else {
            document.getElementById('senderPrefix_id').style.display = 'none';
        }
    }

    //Calculate email body size
    function calculateBodySize() {
        var generationId = $("#generationId_key").val();
        //if is from retry generation
        if(generationId != undefined && generationId != '') {
            makeBodySizeAjaxRequest(${jsBodySizeGenerationUrl});

        } else {
            var templateId = $("#templateId_key").val();

            if(templateId == undefined || templateId == '') {
                //if template is read only, search by name
                templateId = $("input[name='dto(templateId)']").val();
            }

            if(templateId != undefined && templateId != '') {
                var attachIdsVar = getAllAttachIds();
                makeBodySizeAjaxRequest(${jsBodySizeUrl});
            }
        }
    }

    function makeBodySizeAjaxRequest(urlAddress) {
        $.ajax({
            async:true,
            type: "POST",
            data: " ",
            dataType: "xml",
            url:urlAddress,
            success: function(data, status) {
                processBodySizeXMLDoc(data);
            }
        });
    }

    function processBodySizeXMLDoc(xmldoc) {

        if (xmldoc.getElementsByTagName('templateBodySize').length > 0) {

            var localAttachSize = calculateLocalAttachSize();
            var maxSendSize = 0;
            var html = '';

            //process xml document with jQuery
            $(xmldoc).find('bodySize').each(function() {

                var bodySize = $(this).attr('size');
                var languageName = $(this).text();

                var totalSize = parseInt(localAttachSize) + parseInt(bodySize);
                var sizeKb = convertToKilobyte(totalSize);
                if(sizeKb > maxSendSize) {
                    maxSendSize = sizeKb;
                }

                html = html + composeHtmlTemplateSize(languageName, sizeKb);
            });

            $("#tdBodySize").html(html);
            setSendEmailSize(maxSendSize);
            showNotificationMail();
        }
    }

    function setSendEmailSize(maxSendSize) {
        if ($('#sendEmailSize_id').length){
            $('#sendEmailSize_id').val(maxSendSize);
        }
    }

    function showNotificationMail() {
        if(isSendLimitExceeded()) {
            document.getElementById("tr_notificationMail").style.display = "";
        } else {
            document.getElementById("tr_notificationMail").style.display = "none";
        }
    }

    function composeHtmlTemplateSize(languageName, sizeKb) {
        var limitWarningSize = ${LIMIT_BODYSIZE};

        var html = "<p>";
        if (sizeKb > limitWarningSize) {
            html = "<p style='font-weight: bold; color: #ff0000'>"
        }

        html = html + languageName + ": " + sizeKb + " ${KB_SIZE} " + "</p>";
        return html;
    }

    function convertToKilobyte(bytes) {
        var kb = 0;
        if(bytes < 1024) {
            kb = 1;
        } else {
            kb = Math.round(bytes / 1024);
        }
        return kb;
    }

    function calculateLocalAttachSize() {
        var localAttachSize = 0;

        //check whether browser fully supports all File API HTML5
        if (window.File && window.FileReader && window.FileList && window.Blob) {

            $("#localAttachTdKey input:file").each(function() {

                if($(this).val() != "" && $(this)[0].files.length > 0) {
                    localAttachSize = localAttachSize + $(this)[0].files[0].size;
                }
            });
        } else {
            //alert("Please upgrade your browser, because your current browser lacks some new features we need!");
        }

        return localAttachSize;
    }

    function isSendLimitExceeded() {
        var limitSize = ${LIMIT_BODYSIZE};
        var limitRecipient = ${LIMIT_RECIPIENT};
        var sendSize = 0;
        var sendRecipients = 0;

        if ($('#sendEmailSize_id').length){
            sendSize = $('#sendEmailSize_id').val();
        }
        if ($('#totalSendRecipients_id').length){
            sendRecipients = $('#totalSendRecipients_id').val();
        }

        if(sendSize > limitSize || sendRecipients > limitRecipient) {
            return true;
        }
        return false;
    }

    $(document).ready(function() {
        //recalculate the body sizes
        calculateBodySize();
    });

</script>
<!--<br>-->


<html:form action="${action}" enctype="multipart/form-data" focus="dto(templateId)" styleId="generateForm"
           onsubmit="sendEmail()">

<html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
<html:hidden property="dto(activityId)" styleId="idActivity"/>

<html:hidden property="dto(generationKey)" value="${app2:getCurrentTimeMillis()}" styleId="idGenerationKey"/>
<html:hidden property="dto(documentType)"/>

<html:hidden property="dto(attachValues)" styleId="idAttachValues"/>

<html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
<html:hidden property="dto(userAddressId)" styleId="idUserAddress"
             value="${sessionScope.user.valueMap['userAddressId']}"/>

<html:hidden property="dto(isCampaignLight)" value="${isCampaignLight}"/>

<html:hidden property="dto(opSend)" value="${opSend}"/>
<c:if test="${'retry'== opSend}">
    <html:hidden property="dto(generationId)" styleId="generationId_key"/>
    <c:set var="readonly" value="${true}"/>
</c:if>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">

            <app2:securitySubmit property="dto(send)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                 styleClass="button" tabindex="1" onclick="sendButtonPressed();">
                <fmt:message key="Common.send"/>
            </app2:securitySubmit>

            <html:cancel styleClass="button" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

            <%--top links--%>
            &nbsp;
            <c:if test="${not empty urlWithoutEmail}">
                <a href="javascript:showRecipientWithoutEmailPopup()"><fmt:message
                        key="Campaign.activity.emailSend.viewRecipientWithoutEmail"/></a>
            </c:if>
        </td>
    </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <tr>
        <td colspan="4" class="title">
                ${title}
        </td>
    </tr>
    <tr>
        <td class="label" width="30%">
            <fmt:message key="Campaign.activity.emailGenerate.template"/>
        </td>
        <td class="contain" colspan="2">
            <fanta:select property="dto(templateId)" styleId="templateId_key" listName="templateList" module="/campaign"
                          styleClass="middleSelect" tabIndex="5"
                          onChange="calculateBodySize()"
                          firstEmpty="${app2:campaignHasOnlyOneTemplate(param.campaignId,emailSendForm.dtoMap['documentType'])?'false':'true'}"
                          labelProperty="description" valueProperty="templateId"
                          readOnly="${readonly || not empty emailSendForm.dtoMap['createAgain']}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="campaignId" value="${not empty param.campaignId ? param.campaignId : 0}"/>
                <fanta:parameter field="documentType"
                                 value="${not empty emailSendForm.dtoMap['documentType'] ? emailSendForm.dtoMap['documentType'] : 0}"/>
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="TelecomType.type.mail"/>
        </td>
        <td class="contain" colspan="2">
            <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
            <html:select property="dto(telecomTypeId)" styleClass="middleSelect" styleId="idTelecomType" tabindex="6"
                         readonly="${readonly}"
                         onchange="javascript:getSenderEmployeeTelecoms();"
                         onkeyup="javascript:getSenderEmployeeTelecoms();">
                <html:options collection="telecomTypes" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.activity.emailGenerate.subject"/>
        </td>
        <td class="contain" colspan="2">
            <app:text property="dto(subject)" styleId="idSubject" styleClass="middleText" maxlength="100" tabindex="7" view="${readonly}"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.activity.emailGenerate.senderEmployee"/>
        </td>
        <td class="contain" colspan="2">
            <c:choose>
                <c:when test="${isCampaignLight}">
                    <html:hidden property="dto(senderEmployeeId)" styleId="idSenderEmployee"/>

                    <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
                        <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:label>
                </c:when>
                <c:otherwise>

                    <c:set var="senderEmployees"
                           value="${app2:getSenderEmployeeList(emailSendForm.dtoMap['activityId'], pageContext.request)}"/>
                    <html:select property="dto(senderEmployeeId)" styleId="idSenderEmployee" styleClass="middleSelect"
                                 tabindex="8" readonly="${readonly}"
                                 onchange="javascript:getSenderEmployeeTelecoms();"
                                 onkeyup="javascript:getSenderEmployeeTelecoms();">
                        <html:options collection="senderEmployees" property="value" labelProperty="label"/>
                    </html:select>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.generate.senderemail"/>
        </td>
        <td class="contain">
            <div id="divTelecom">
                    <%--to dto(employeeMail)--%>
                <c:set var="senderEmployeeEmails"
                       value="${app2:getSenderEmployeeEmails(emailSendForm.dtoMap['activityId'],emailSendForm.dtoMap['senderEmployeeId'] ,emailSendForm.dtoMap['telecomTypeId'],pageContext.request)}"/>
                <html:select property="dto(employeeMail)" styleClass="middleSelect"
                             readonly="${readonly}"
                             tabindex="9">
                    <html:options collection="senderEmployeeEmails" property="value" labelProperty="label"/>
                </html:select>
            </div>

            <!--tooltip-->
            <div style="position:relative;">
                <div id="msgId" class="messageToolTip"
                     style="visibility:hidden; position:absolute; top:-20px; left:0px">
                    <fmt:message key="Common.message.loading"/>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.generate.senderPrefix"/>
        </td>
        <td class="contain">
            <c:set var="senderPrefixTypes" value="${app2:getSenderPrefixType(pageContext.request)}"/>
            <html:select property="dto(senderPrefixType)" styleClass="middleSelect" tabindex="10"
                         readonly="${readonly}"
                         onchange="prefixSelected(this)"
                         onkeyup="prefixSelected(this)">
                <html:options collection="senderPrefixTypes" property="value" labelProperty="label"/>
            </html:select>

            <br/>
            <br/>

            <c:choose>
                <c:when test="${definedByUserPrefix eq emailSendForm.dtoMap['senderPrefixType']}">
                    <c:set var="displayInputPrefix" value=""/>
                </c:when>
                <c:otherwise>
                    <c:set var="displayInputPrefix" value="none"/>
                </c:otherwise>
            </c:choose>
            <app:text property="dto(senderPrefix)" styleId="senderPrefix_id" view="${readonly}" styleClass="middleText" style="display:${displayInputPrefix}" maxlength="100" tabindex="11"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.activity.emailGenerate.attach"/>
        </td>
        <td class="contain" colspan="2">
            <table>
                <tr>
                    <td>
                        <html:select property="listAttach" styleId="listAttachId" multiple="true" value=""
                                     size="15" styleClass="middleMultipleSelect"
                                     tabindex="12">
                            <c:if test="${not empty previousAttachList}">
                                <html:options collection="previousAttachList" property="value" labelProperty="label"/>
                            </c:if>
                        </html:select>
                    </td>
                    <td class="containTop">
                        <table cellpadding="2" cellspacing="0">
                            <tr>
                                <td>
                                    <tags:selectPopup
                                            url="/campaign/Attach/Import/List.do?campaignId=${param.campaignId}"
                                            name="searchTask"
                                            hide="${op == 'delete' || readonly}"
                                            titleKey="Attach.searchAttach"
                                            width="630"
                                            heigth="480"
                                            imgWidth="17"
                                            imgHeight="19"
                                            tabindex="13"
                                            />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <c:if test="${not readonly}">
                                        <input type="button" value="" onclick="removeAttachments();" name="B1"
                                               class="removeButton" title="<fmt:message key="Common.delete"/>"
                                               tabindex="14"/>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.activity.emailGenerate.createCommunication"/>
        </td>
        <td class="contain">
            <html:hidden property="dto(createAgain)"/>
            <html:checkbox property="dto(createComm)" value="true" styleClass="radio" disabled="${readonly}" tabindex="15"/>

            <c:if test="${readonly}">
                <html:hidden property="dto(createComm)"/>
            </c:if>
        </td>
    </tr>

    <tr>
        <td class="label">
            <fmt:message key="Campaign.generate.totalSizeEmailSend"/>
            <html:hidden property="dto(sendEmailSize)" styleId="sendEmailSize_id"/>
        </td>
        <td class="contain" id="tdBodySize">
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Campaign.generate.totalRecipientSend"/>

            <c:set var="totalRecipients" value="${app2:getCampaignGenerationRecipients(param.campaignId, emailSendForm.dtoMap['activityId'])}"/>
            <c:set var="recipientStyle" value=""/>
            <c:set var="notificationStyle" value="display:none"/>
            <c:if test="${totalRecipients >= LIMIT_RECIPIENT}">
                <c:set var="recipientStyle" value="font-weight: bold; color: #ff0000"/>
                <c:set var="notificationStyle" value=""/>
            </c:if>
        </td>
        <td class="contain" style="${recipientStyle}">
            <html:hidden property="dto(totalSendRecipients)" styleId="totalSendRecipients_id"
                         value="${totalRecipients}"/>
            <c:out value="${totalRecipients}"/>
        </td>
    </tr>
    <tr id="tr_notificationMail" style="${notificationStyle}">
        <td class="label">
            <fmt:message key="Campaign.generate.background.notificationMail"/>
        </td>
        <td class="contain">
            <app:text property="dto(notificationMail)" styleClass="middleText" maxlength="200" tabindex="16"
                      view="${readonly}"/>
        </td>
    </tr>

    <%--attach from disk--%>
    <tr>
        <td colspan="2" id="localAttachTdKey">
            <c:set var="enableAddAttachLink" value="true" scope="request"/>
            <c:set var="enableCheckBoxes" value="false" scope="request"/>
            <c:set target="${emailSendForm.dtoMap}" property="attachmentCounter" value=""/>
            <c:set target="${emailSendForm.dtoMap}" property="messageCounter" value=""/>

            <c:if test="${not readonly}">
                <c:set var="inputFileOnChangeJs" value="calculateBodySize();" scope="request"/>
                <c:set var="deleteAttachJs" value="calculateBodySize();" scope="request"/>

                <c:import url="/common/webmail/AttachmentFragment.jsp"/>
            </c:if>
        </td>
    </tr>
</table>
<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <td class="button">

            <app2:securitySubmit property="dto(send)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                 styleClass="button" tabindex="17" onclick="sendButtonPressed();">
                <fmt:message key="Common.send"/>
            </app2:securitySubmit>

            <html:cancel styleClass="button" tabindex="18">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </td>
    </tr>
</table>

</html:form>
