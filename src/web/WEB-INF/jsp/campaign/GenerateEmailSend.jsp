<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/cacheable/jquery.blockUI-2.70.0.js"/>
<tags:initBootstrapSelectPopup/>
<tags:bootstrapModalPopup styleId="recipients_popup_id" isLargeModal="true" modalTitleKey="hola"/>

<tags:jscript language="JavaScript" src="/js/st-xmlhttpV2.jsp"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery.timer.js"/>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/cacheable/jquery-ui-1.10.0.smoothness.css"/>"/>
<tags:jscript language="JavaScript" src="/js/cacheable/jquery-ui-1.10.0.custom.min.js"/>

<c:set var="KEY_SEPART" value="<%=CampaignConstants.KEY_SEPARATOR%>"/>
<c:set var="KEY_VALUE" value="<%=CampaignConstants.KEY_SEPARATOR_VALUE%>"/>
<c:set var="SEND_CURRENT" value="<%=CampaignConstants.CURRENT_USER_SEND%>"/>
<c:set var="ACTIVITY_RESP" value="<%=CampaignConstants.ACTIVITY_RESPONSIBLE%>"/>
<c:set var="CONTACT_RESP" value="<%=CampaignConstants.CONTACT_RESPONSIBLE%>"/>
<c:set var="definedByUserPrefix" value="<%=CampaignConstants.SenderPrefixType.DEFINEDBYUSER.getConstantAsString()%>"/>
<fmt:message key="Campaign.activity.emailSend.popup.recipientWithoutEmail" var="recipientWithoutEmailTitlePopup"/>

<fmt:message var="KB_SIZE" key="Webmail.mailTray.Kb"/>
<c:set var="LIMIT_RECIPIENT"
       value="${app2:getConfigurationPropertyValue('elwis.campaignMail.background.send.recipient.limit')}"
       scope="request"/>
<c:set var="LIMIT_BODYSIZE"
       value="${app2:getConfigurationPropertyValue('elwis.campaignMail.background.send.size.limit')}" scope="request"/>
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

<app2:jScriptUrl url="/campaign/Email/Send/BodySize/Ajax.do?generationId=${emailSendForm.dtoMap['generationId']}"
                 var="jsBodySizeGenerationUrl">
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
            async: true,
            type: "POST",
            data: " ",
            dataType: "xml",
            url: urlAddress,
            success: function (data, status) {
                processSummaryXMLDoc(data);
            }
        });
    }

    function initTimerSendProgress() {
        var generation_key = document.getElementById("idGenerationKey").value;
        initProgressBar();

        //timer object
        var timer = $.timer(function () {
            makeProgressBarAjaxRequest(${jsProgressBarUrl});
        });
        timer.set({time: 3000, autostart: true});
    }

    function initProgressBar() {
        //init the progress bar
        $("#progressbar").progressbar({value: 0});
    }

    function updateProgressBar(total, success, fail) {
        var value = $("#progressbar").progressbar("value");
        var progressValue = parseInt(success) + parseInt(fail);

        if (value == 0) {
            //define the max
            $("#progressbar").progressbar("option", "max", total);
        }
        $("#progressbar").progressbar("value", progressValue);
    }

    function processSummaryXMLDoc(xmldoc) {
        if (xmldoc.getElementsByTagName('progressBarSummary').length > 0) {
            //process xml document with jQuery
            $(xmldoc).find('summary').each(function () {

                var total = $(this).attr('total');
                var success = $(this).attr('success');
                var fail = $(this).attr('fail');
                var message = $(this).text();

                if (total > 0) {
                    updateProgressBar(total, success, fail);
                    setProgressMessage(message);
                }
            });
        }
    }

    function setProgressMessage(message) {
        if (message != "") {
            document.getElementById("progressSummary").innerHTML = message;
        }
    }

    function sendEmail() {
        document.getElementById('idAttachValues').value = selectAll();
    }
    function showWaitMessage() {
        $.blockUI({message: $('#waitMessage'), css: {padding: '20px'}});
        initTimerSendProgress();
    }

    function sendButtonPressed() {
        if (!isSendLimitExceeded()) {
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

        launchBootstrapPopup("recipients_popup_id",${jsWithoutEmailUrl},'myWindow',true);
        setModalTitle("recipients_popup_id",'${recipientWithoutEmailTitlePopup}');
    }

    //attach selection
    //get object by id or name
    function lib_getObj(id, d) {
        var i, x;
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

    function prefixSelected(prefixBox) {
        if (prefixBox.value == '${definedByUserPrefix}') {
            document.getElementById('senderPrefix_id').style.display = '';
        } else {
            document.getElementById('senderPrefix_id').style.display = 'none';
        }
    }

    //Calculate email body size
    function calculateBodySize() {
        var generationId = $("#generationId_key").val();
        //if is from retry generation
        if (generationId != undefined && generationId != '') {
            makeBodySizeAjaxRequest(${jsBodySizeGenerationUrl});

        } else {
            var templateId = $("#templateId_key").val();

            if (templateId == undefined || templateId == '') {
                //if template is read only, search by name
                templateId = $("input[name='dto(templateId)']").val();
            }

            if (templateId != undefined && templateId != '') {
                var attachIdsVar = getAllAttachIds();
                makeBodySizeAjaxRequest(${jsBodySizeUrl});
            }
        }
    }

    function makeBodySizeAjaxRequest(urlAddress) {
        $.ajax({
            async: true,
            type: "POST",
            data: " ",
            dataType: "xml",
            url: urlAddress,
            success: function (data, status) {
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
            $(xmldoc).find('bodySize').each(function () {

                var bodySize = $(this).attr('size');
                var languageName = $(this).text();

                var totalSize = parseInt(localAttachSize) + parseInt(bodySize);
                var sizeKb = convertToKilobyte(totalSize);
                if (sizeKb > maxSendSize) {
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
        if ($('#sendEmailSize_id').length) {
            $('#sendEmailSize_id').val(maxSendSize);
        }
    }

    function showNotificationMail() {
        if (isSendLimitExceeded()) {
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
        if (bytes < 1024) {
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

            $("#localAttachTdKey input:file").each(function () {

                if ($(this).val() != "" && $(this)[0].files.length > 0) {
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

        if ($('#sendEmailSize_id').length) {
            sendSize = $('#sendEmailSize_id').val();
        }
        if ($('#totalSendRecipients_id').length) {
            sendRecipients = $('#totalSendRecipients_id').val();
        }

        if (sendSize > limitSize || sendRecipients > limitRecipient) {
            return true;
        }
        return false;
    }

    $(document).ready(function () {
        //recalculate the body sizes
        calculateBodySize();
    });

</script>

<html:form action="${action}"
           enctype="multipart/form-data"
           focus="dto(templateId)"
           styleId="generateForm"
           onsubmit="sendEmail()"
           styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">


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

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(send)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="1"
                                 onclick="sendButtonPressed();">
                <fmt:message key="Common.send"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

                <%--top links--%>
            &nbsp;
            <c:if test="${not empty urlWithoutEmail}">
                <a href="javascript:showRecipientWithoutEmailPopup()"><fmt:message
                        key="Campaign.activity.emailSend.viewRecipientWithoutEmail"/></a>
            </c:if>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                        ${title}
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="templateId_key">
                        <fmt:message key="Campaign.activity.emailGenerate.template"/>
                    </label>

                    <div class="${app2:getFormContainClasses(readonly || not empty emailSendForm.dtoMap['createAgain'])}">
                        <fanta:select property="dto(templateId)"
                                      styleId="templateId_key"
                                      listName="templateList"
                                      module="/campaign"
                                      styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="5"
                                      onChange="calculateBodySize()"
                                      firstEmpty="${app2:campaignHasOnlyOneTemplate(param.campaignId,emailSendForm.dtoMap['documentType'])?'false':'true'}"
                                      labelProperty="description"
                                      valueProperty="templateId"
                                      readOnly="${readonly || not empty emailSendForm.dtoMap['createAgain']}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="campaignId"
                                             value="${not empty param.campaignId ? param.campaignId : 0}"/>
                            <fanta:parameter field="documentType"
                                             value="${not empty emailSendForm.dtoMap['documentType'] ? emailSendForm.dtoMap['documentType'] : 0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="idTelecomType">
                        <fmt:message key="TelecomType.type.mail"/>
                    </label>

                    <div class="${app2:getFormContainClasses(readonly)}">
                        <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                        <html:select property="dto(telecomTypeId)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     styleId="idTelecomType"
                                     tabindex="6"
                                     readonly="${readonly}"
                                     onchange="javascript:getSenderEmployeeTelecoms();"
                                     onkeyup="javascript:getSenderEmployeeTelecoms();">
                            <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="idSubject">
                        <fmt:message key="Campaign.activity.emailGenerate.subject"/>
                    </label>

                    <div class="${app2:getFormContainClasses(readonly)}">
                        <app:text property="dto(subject)"
                                  styleId="idSubject"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="100"
                                  tabindex="7"
                                  view="${readonly}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="idSenderEmployee">
                        <fmt:message key="Campaign.activity.emailGenerate.senderEmployee"/>
                    </label>

                    <c:choose>
                        <c:when test="${isCampaignLight}">
                            <div class="${app2:getFormContainClasses(true)}">
                                <html:hidden property="dto(senderEmployeeId)" styleId="idSenderEmployee"/>

                                <fanta:label listName="userBaseList"
                                             module="/admin"
                                             patron="0"
                                             columnOrder="name"
                                             labelStyleClass="form-control">
                                    <fanta:parameter field="userId"
                                                     value="${sessionScope.user.valueMap['userId']}"/>
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:label>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="${app2:getFormContainClasses(readonly)}">
                                <c:set var="senderEmployees"
                                       value="${app2:getSenderEmployeeList(emailSendForm.dtoMap['activityId'], pageContext.request)}"/>
                                <html:select property="dto(senderEmployeeId)"
                                             styleId="idSenderEmployee"
                                             styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                             tabindex="8"
                                             readonly="${readonly}"
                                             onchange="javascript:getSenderEmployeeTelecoms();"
                                             onkeyup="javascript:getSenderEmployeeTelecoms();">
                                    <html:options collection="senderEmployees" property="value"
                                                  labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="employeeMail_id">
                        <fmt:message key="Campaign.generate.senderemail"/>
                    </label>

                    <div class="${app2:getFormContainClasses(readonly)}">
                        <div id="divTelecom">
                                <%--to dto(employeeMail)--%>
                            <c:set var="senderEmployeeEmails"
                                   value="${app2:getSenderEmployeeEmails(emailSendForm.dtoMap['activityId'],emailSendForm.dtoMap['senderEmployeeId'] ,emailSendForm.dtoMap['telecomTypeId'],pageContext.request)}"/>
                            <html:select property="dto(employeeMail)"
                                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                         readonly="${readonly}"
                                         styleId="employeeMail_id"
                                         tabindex="9">
                                <html:options collection="senderEmployeeEmails" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </div>

                        <!--tooltip-->
                        <div style="position:relative;">
                            <div id="msgId" class="messageToolTip"
                                 style="visibility:hidden; position:absolute; top:-30px; left:0px">
                                <fmt:message key="Common.message.loading"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="senderPrefix_id">
                        <fmt:message key="Campaign.generate.senderPrefix"/>
                    </label>

                    <div class="${app2:getFormContainClasses(readonly)}">
                        <c:set var="senderPrefixTypes" value="${app2:getSenderPrefixType(pageContext.request)}"/>
                        <html:select property="dto(senderPrefixType)"
                                     styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                     tabindex="10"
                                     readonly="${readonly}"
                                     onchange="prefixSelected(this)"
                                     onkeyup="prefixSelected(this)">
                            <html:options collection="senderPrefixTypes" property="value" labelProperty="label"/>
                        </html:select>

                        <c:choose>
                            <c:when test="${definedByUserPrefix eq emailSendForm.dtoMap['senderPrefixType']}">
                                <c:set var="displayInputPrefix" value=""/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="displayInputPrefix" value="none"/>
                            </c:otherwise>
                        </c:choose>

                        <app:text property="dto(senderPrefix)"
                                  styleId="senderPrefix_id"
                                  view="${readonly}"
                                  styleClass="middleText ${app2:getFormInputClasses()} marginTopInput"
                                  style="display:${displayInputPrefix}"
                                  maxlength="100"
                                  tabindex="11"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="listAttachId">
                        <fmt:message key="Campaign.activity.emailGenerate.attach"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <table class="col-xs-12">
                            <tr>
                                <td class="col-xs-11 paddingLeftRemove paddingRight">

                                    <html:select property="listAttach"
                                                 styleId="listAttachId"
                                                 multiple="true"
                                                 value=""
                                                 size="15"
                                                 styleClass="middleMultipleSelect ${app2:getFormSelectClasses()} heightSelect"
                                                 tabindex="12">
                                        <c:if test="${not empty previousAttachList}">
                                            <html:options collection="previousAttachList" property="value"
                                                          labelProperty="label"/>
                                        </c:if>
                                    </html:select>
                                </td>
                                <td class="col-xs-1 paddingLeftRemove paddingRightRemove">
                                    <tags:bootstrapSelectPopup
                                            url="/campaign/Attach/Import/List.do?campaignId=${param.campaignId}"
                                            name="searchTask"
                                            styleId="selectPopupSearchAttachID"
                                            hide="${op == 'delete' || readonly}"
                                            titleKey="Attach.searchAttach"
                                            isLargeModal="true"
                                            tabindex="13"/>
                                    <c:if test="${not readonly}">
                                        <br/>
                                        <br/>
                                        <button type="button"
                                                onclick="removeAttachments();"
                                                name="B1"
                                                class="${app2:getFormButtonClasses()}"
                                                title="<fmt:message key="Common.delete"/>"
                                                tabindex="14">
                                            <span class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="createComm_id">
                        <fmt:message key="Campaign.activity.emailGenerate.createCommunication"/>
                    </label>

                    <div class="${app2:getFormContainClasses(false)}">
                        <html:hidden property="dto(createAgain)"/>
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(createComm)"
                                               value="true"
                                               styleId="createComm_id"
                                               styleClass="radio"
                                               disabled="${readonly}"
                                               tabindex="15"/>
                                <label for="active_id"></label>
                            </div>
                        </div>

                        <c:if test="${readonly}">
                            <html:hidden property="dto(createComm)"/>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Campaign.generate.totalSizeEmailSend"/>
                        <html:hidden property="dto(sendEmailSize)" styleId="sendEmailSize_id"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}" id="tdBodySize">

                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Campaign.generate.totalRecipientSend"/>

                        <c:set var="totalRecipients"
                               value="${app2:getCampaignGenerationRecipients(param.campaignId, emailSendForm.dtoMap['activityId'])}"/>
                        <c:set var="recipientStyle" value=""/>
                        <c:set var="notificationStyle" value="display:none"/>
                        <c:if test="${totalRecipients >= LIMIT_RECIPIENT}">
                            <c:set var="recipientStyle" value="font-weight: bold; color: #ff0000"/>
                            <c:set var="notificationStyle" value=""/>
                        </c:if>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}" style="${recipientStyle}">
                        <html:hidden property="dto(totalSendRecipients)"
                                     styleId="totalSendRecipients_id"
                                     value="${totalRecipients}"/>
                        <c:out value="${totalRecipients}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}" style="${notificationStyle}" id="tr_notificationMail">
                    <label class="${app2:getFormLabelClasses()}" for="notificationMail_id">
                        <fmt:message key="Campaign.generate.background.notificationMail"/>
                    </label>

                    <div class="${app2:getFormContainClasses(readonly)}">
                        <app:text property="dto(notificationMail)"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="200"
                                  styleId="notificationMail_id"
                                  tabindex="17"
                                  view="${readonly}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                    <%--attach from disk--%>
                <div id="localAttachTdKey">
                    <c:set var="enableAddAttachLink" value="true" scope="request"/>
                    <c:set var="enableCheckBoxes" value="false" scope="request"/>
                    <c:set target="${emailSendForm.dtoMap}" property="attachmentCounter" value=""/>
                    <c:set target="${emailSendForm.dtoMap}" property="messageCounter" value=""/>

                    <c:if test="${not readonly}">
                        <c:set var="inputFileOnChangeJs" value="calculateBodySize();" scope="request"/>
                        <c:set var="deleteAttachJs" value="calculateBodySize();" scope="request"/>

                        <c:import url="/WEB-INF/jsp/webmail/AttachmentFragment.jsp"/>
                    </c:if>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(send)"
                                 operation="execute"
                                 functionality="CAMPAIGNACTIVITY"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="17"
                                 onclick="sendButtonPressed();">
                <fmt:message key="Common.send"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="18">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="emailSendForm"/>