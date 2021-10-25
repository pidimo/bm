<%@ page import="com.jatun.common.web.JavaScriptEncoder" %>
<%@ page import="com.piramide.elwis.utils.CatalogConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
<%
    //constant messages
    request.setAttribute("EXPIRED", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.sessionExpired")));
    request.setAttribute("ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "error.tooltip.unexpected")));
    request.setAttribute("LOADING", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.message.loading")));
    request.setAttribute("RECIPIENT_ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Mail.importTemplate.invalidRecipients")));
%>

<c:set var="companyId" value="${sessionScope.user.valueMap['companyId']}"/>

<app2:jScriptUrl url="/webmail/Mail/Ajax/ImportTemplate.do?dto(companyId)=${companyId}" var="jsImportUrl">
    <app2:jScriptUrlParam param="dto(templateId)" value="idTemplate"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="/webmail/Mail/Ajax/ImportTemplate.do?dto(companyId)=${companyId}&dto(op)=onlyTemplate" var="jsImportLanguageUrl">
    <app2:jScriptUrlParam param="dto(templateId)" value="idTemplate"/>
    <app2:jScriptUrlParam param="dto(languageId)" value="idLanguage"/>
</app2:jScriptUrl>


<script type="text/javascript">

    //ajax management
    function makeImportPOSTAjaxRequest(urlAddress, parameters) {
        $.ajax({
            async:true,
            type: "POST",
            data:parameters,
            url:urlAddress,
            beforeSend:setLoadMessage,
            success: function(data, status) {
                //alert(data);
                if (typeof data == 'object') {
                    processXMLDoc(data);
                } else {
                    processHTMLDoc(data);
                }
            },
            error: function(ajaxRequest) {
                ajaxImportErrorProcess(ajaxRequest);
            }
        });
    }

    function processXMLDoc(xmldoc) {
        
        if (xmldoc.getElementsByTagName('languageTemplates').length > 0) {
            processLanguageXMLDoc(xmldoc);
        } else if(xmldoc.getElementsByTagName('importErrors').length > 0){
            processErrorXMLDoc(xmldoc);
        }
        enableButtons();
    }

    function processHTMLDoc(htmldoc) {

        //get global tineMCE instance by ID 
        tinyMCE.get('body_field').setContent(htmldoc);
        clearImportTemplate();
    }

    function processLanguageXMLDoc(xmldoc) {
        cleanLanguageSelect();

        //process xml document with jQuery
        $(xmldoc).find('template').each(function() {

            //save template selected
            var templateId = $(this).attr('id');
            document.getElementById("selectedTemplateId").value = templateId;
            var templateBox = document.getElementById("bodyTemplateBox");
            document.getElementById("div_selectedTemplate").innerHTML = templateBox.options[templateBox.selectedIndex].text;


            //process langueges
            $(this).find('language').each(function() {
                var languageId = $(this).attr('id');
                var languageName = $(this).text();
                putLanguageInSelect(languageId, languageName);
            });
        });

        enableButtons();
        showComposeTemplate();
        hideToolTip();
    }

    function processErrorXMLDoc(xmldoc) {
        //process xml document with jQuery
        $(xmldoc).find('message').each(function() {
            showToolTip($(this).text());
        });
    }

    function enableButtons() {
        document.getElementById("b1").disabled = false;
        document.getElementById("b2").disabled = false;
        document.getElementById("b3").disabled = false;
        document.getElementById("b4").disabled = false;
    }

    function disableButtons() {
        document.getElementById("b1").disabled = true;
        document.getElementById("b2").disabled = true;
        document.getElementById("b3").disabled = true;
        document.getElementById("b4").disabled = true;
    }

    function clearImportTemplate() {
        cleanLanguageSelect();
        document.getElementById("selectedTemplateId").value = "";
        document.getElementById("div_selectedTemplate").innerHTML = "";

        var templateBox = document.getElementById("bodyTemplateBox");
        templateBox.options[0].selected = true;

        enableButtons();
        showSimpleTemplate();
        hideToolTip();
    }

    function showComposeTemplate() {
        document.getElementById("singleTemplate").style.display = "none";
        document.getElementById("composeTemplate").style.display = "";
    }

    function showSimpleTemplate() {
        document.getElementById("singleTemplate").style.display = "";
        document.getElementById("composeTemplate").style.display = "none";
    }


    /*clean all options*/
    function cleanSelectOptions(box) {
        for (var i = 0; i < box.options.length; i++) {
            box.options[i].value = "";
            box.options[i].text = "";
        }
        box.options.length = 0;
    }

    function cleanLanguageSelect() {
        var box = document.getElementById("languageBox");
        cleanSelectOptions(box);
    }

    function putLanguageInSelect(languageId, languageName) {
        var box = document.getElementById("languageBox");

        var opt = new Option();
        opt.text = unescape(languageName);
        opt.value = languageId;
        box.options[box.options.length] = opt;
    }


    function setLoadMessage() {
        showToolTip('${LOADING}');
        disableButtons();
    }

    function ajaxImportErrorProcess(ajaxRequest) {
        var status = ajaxRequest.status;

        if (status == 404 || status == 302) {
            showToolTip('${EXPIRED}');
        } else if (status == 400) {
            showToolTip('${RECIPIENT_ERROR}');
        } else {
            showToolTip('${ERROR}');
        }

        enableButtons();
    }


    function showToolTip(message) {
        var msgDiv = document.getElementById("messageId");
        msgDiv.innerHTML = unescape(message);
        msgDiv.style.visibility = "visible";
    }

    function hideToolTip() {
        var msgDiv = document.getElementById("messageId");
        msgDiv.style.visibility = "hidden";
    }


    function importTemplate() {

        var templateSelect = document.getElementById("bodyTemplateBox");
        var idTemplate = templateSelect.options[templateSelect.selectedIndex].value;

        if (idTemplate != "") {
            var params = getFormData();
            makeImportPOSTAjaxRequest(${jsImportUrl}, params);
        }
    }

    function importComposeTemplate() {

        var langBox = document.getElementById("languageBox");
        var idTemplate = document.getElementById("selectedTemplateId").value;
        var idLanguage = langBox.options[langBox.selectedIndex].value;

        if (idLanguage != "") {
            var params = getFormData();
            makeImportPOSTAjaxRequest(${jsImportLanguageUrl}, params);
        }
    }

    function getFormData() {
        var formParam = "";
        var obj = document;
        for (i = 0; i < obj.getElementsByTagName("input").length; i++) {
            if (obj.getElementsByTagName("input")[i].type == "text") {
                formParam += obj.getElementsByTagName("input")[i].name + "=" +
                             encodeURIComponent(obj.getElementsByTagName("input")[i].value) + "&";
            }
            if (obj.getElementsByTagName("input")[i].type == "hidden") {
                formParam += obj.getElementsByTagName("input")[i].name + "=" +
                             encodeURIComponent(obj.getElementsByTagName("input")[i].value) + "&";
            }
        }
        return formParam;
    }


</script>

<tr>
    <td class="label">
        <fmt:message key="Mail.importTemplate.template"/>
    </td>
    <td class="contain">
        <div style="position:relative;">
            <div id="messageId" class="messageToolTip" style="visibility:hidden; position:absolute; top:2px; left:-5px">
                <fmt:message key="Common.message.loading"/>
            </div>
        </div>

        <table id="singleTemplate" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
            <tr>
                <td>
                    <fanta:select property="dto(bodyTemplateId)"
                                  styleId="bodyTemplateBox"
                                  listName="templateList"
                                  module="/catalogs"
                                  labelProperty="description"
                                  valueProperty="id"
                                  firstEmpty="true"
                                  styleClass="largeSelect"
                                  value=""
                                  tabIndex="${startTabIndex}">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="mediaType" value="${mediatype_HTML}"/>
                    </fanta:select>
                    <html:button property="import" styleId="b1" styleClass="button" onclick="javascrip:importTemplate()"
                                 tabindex="${startTabIndex}">
                        <fmt:message key="Common.import"/>
                    </html:button>
                    <html:button property="clear" styleId="b2" styleClass="button"
                                 onclick="javascrip:clearImportTemplate()" tabindex="${startTabIndex}">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </td>
            </tr>
        </table>
        <table id="composeTemplate" style="display:none;" border="0" cellpadding="0" cellspacing="0" width="100%"
               align="center">
            <tr>
                <td>
                    <html:hidden property="dto(selectedTemplateId)" styleId="selectedTemplateId"/>
                    <div id="div_selectedTemplate"></div>
                </td>
                <td align="left">
                    <html:select property="dto(templateLanguageId)" styleId="languageBox" styleClass="select"
                                 tabindex="${startTabIndex}">
                        <html:option value="">&nbsp;</html:option>
                    </html:select>
                    <html:button property="import" styleId="b3" styleClass="button"
                                 onclick="javascrip:importComposeTemplate()" tabindex="${startTabIndex}">
                        <fmt:message key="Common.import"/>
                    </html:button>
                    <html:button property="clear" styleId="b4" styleClass="button"
                                 onclick="javascrip:clearImportTemplate()" tabindex="${startTabIndex}">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </td>
            </tr>
        </table>
    </td>
</tr>
