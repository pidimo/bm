<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/st-xmlhttp.js"/>


<c:set var="CAMP_RECIPIENTS" value="<%=CampaignConstants.CAMPAIGN_RECIPIENTS%>"/>
<c:set var="ASSIGN_MANUAL" value="<%=CampaignConstants.MANUAL_ASSIGN%>"/>
<c:set var="ASSIGN_AUTOMATIC" value="<%=CampaignConstants.AUTOMATIC_ASSIGN%>"/>
<c:set var="TEMPLATE_HTML" value="<%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>"/>
<c:set var="TEMPLATE_WORD" value="<%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>"/>

<!--activityId set in param-->
<%--###A${campaignActivityForm.dtoMap['activityId']}--%>
<app:url var="urlCopyRecp"
         value="/Campaign/Recipients/List/Popup.do?activityId=${campaignActivityForm.dtoMap['activityId']}"/>

<app2:jScriptUrl
        url="/campaign/Campaign/Activity/CampContact/CreateFromActivity.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(activityId)=${campaignActivityForm.dtoMap['activityId']}&campaignId=${param.campaignId}"
        var="jsCopyActivityUrl">
    <app2:jScriptUrlParam param="copyActivityId" value="id_copyActivity"/>
</app2:jScriptUrl>

<app:url var="urlAddContact"
         value="/Campaign/Contact/List/Popup.do?activityId=${campaignActivityForm.dtoMap['activityId']}"/>

<app:url var="urlManualAssign"
         value="/Activity/User/List.do?activityId=${campaignActivityForm.dtoMap['activityId']}"/>
<app:url var="urlAutomaticAssign"
         value="/Activity/User/Forward/AutomaticAssign.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(homogeneously)=on"/>


<c:set var="responsibleType" value="<%=CampaignConstants.CONTACT_RESPONSIBLE%>"/>
<app2:jScriptUrl
        url="/campaign/Campaign/Forward/Email/Send.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(activityId)=${campaignActivityForm.dtoMap['activityId']}&campaignId=${param.campaignId}&dto(campaignId)=${param.campaignId}&dto(senderEmployeeId)=${responsibleType}"
        var="jsEmailUrl">
    <app2:jScriptUrlParam param="dto(documentType)" value="docType.value"/>
</app2:jScriptUrl>

<app2:jScriptUrl
        url="/campaign/Campaign/Forward/Generate/Document.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(activityId)=${campaignActivityForm.dtoMap['activityId']}&campaignId=${param.campaignId}&dto(campaignId)=${param.campaignId}&dto(senderEmployeeId)=${responsibleType}&dto(docCommTitle)=${campaignActivityForm.dtoMap['title']}"
        var="jsDocumentUrl">
    <app2:jScriptUrlParam param="dto(documentType)" value="docType.value"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
<!--
function putCopyUrlPopup(searchName, w, h, scroll) {
    var selectType = document.getElementById("recipientCopy");
    var recipientCopy = selectType.value;

    if (recipientCopy == "${CAMP_RECIPIENTS}") {
        var url = "${urlCopyRecp}";
        showPopup(url, searchName, false, w, h, scroll);
    } else if (recipientCopy != "") {
        var id_copyActivity = recipientCopy;
        location.href = ${jsCopyActivityUrl};
    }
}

function showPopup(url, searchName, submitOnSelect, w, h, scroll) {
    autoSubmit = submitOnSelect;

    var winx = (screen.width) / 2;
    var winy = (screen.height) / 2;
    var posx = winx - w / 2;
    var posy = winy - h / 2;
    searchWindow = window.open(url, searchName, 'resizable=yes,width=' + w + ',height=' + h + ',status,left=' + posx + ',top=' + posy + ',scrollbars=' + scroll);
    searchWindow.focus();
}

function buttonEnable(selectBox, btnId) {
    var btn = document.getElementById(btnId);
    if (selectBox.value != "")
        btn.disabled = false;
    else
        btn.disabled = true;
}

function getAssignLocation() {
    var selectType = document.getElementById("assignType");
    var type = selectType.value;
    var url = "";
    if (type == "${ASSIGN_MANUAL}") {
        url = "${urlManualAssign}";
    } else if (type == "${ASSIGN_AUTOMATIC}") {
        url = "${urlAutomaticAssign}";
    }
    location.href = url;
}

function selectedSubmit() {
    document.forms[0].submit();
}

function getGenerateLocation() {
    var docType = document.getElementById("docTypeSelect");

    if (docType.value == "${TEMPLATE_HTML}" ) {
        location.href = ${jsEmailUrl};
    } else if (docType.value == "${TEMPLATE_WORD}" ) {
        location.href = ${jsDocumentUrl};
    }
}


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
//-->
</script>

<c:set var="percents" value="${app2:defaultProbabilities()}"/>
<c:set var="statusList" value="${app2:getCampaignActivityStatusList(pageContext.request)}"/>
<fmt:message var="datePattern" key="datePattern"/>
<calendar:initialize/>


<c:set var="showCurrentDate"  value="<%=Boolean.valueOf(false)%>"/>
<c:if test="${op ==  'create'}">
    <c:set var="showCurrentDate"  value="<%=Boolean.valueOf(true)%>"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<TR>
<TD>
<html:form action="${action}" focus="dto(title)">
<html:hidden property="dto(op)" value="${op}"/>

<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(activityId)"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(activityId)"/>
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<table class="container" align="center" width="80%" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td class="button" colspan="4">

        <app2:securitySubmit operation="${op}" functionality="CAMPAIGNACTIVITY" styleClass="button">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<tr>
    <td class="title" colspan="4">
            ${title}
    </td>
</tr>
<tr>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.title"/>
    </td>
    <td class="contain" width="30%">
        <app:text property="dto(title)" styleClass="mediumText" tabindex="1" view="${'delete' == op}"/>
    </td>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.startDate"/>
    </td>
    <td class="contain" width="30%">
        <app:dateText
                property="dto(startDate)"
                datePatternKey="${datePattern}"
                view="${'delete' == op}"
                tabindex="5"
                styleId="dateTextId_open"
                styleClass="dateText"
                maxlength="10"
                calendarPicker="true"
                currentDate="${showCurrentDate}"/>
    </td>
</tr>
<tr>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.responsible"/>
    </td>
    <td class="contain" width="30%">
        <fanta:select property="dto(userId)" listName="internalUserList"
                      labelProperty="name" valueProperty="id"
                      styleClass="mediumSelect"
                      tabIndex="2"
                      module="/admin"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.closeDate"/>
    </td>
    <td class="contain" width="30%">
        <app:dateText
                property="dto(closeDate)"
                datePatternKey="${datePattern}"
                view="${'delete' == op}"
                tabindex="6"
                styleId="dateTextId_close" styleClass="dateText" maxlength="10"
                calendarPicker="true" currentDate="${showCurrentDate}"/>
    </td>
</tr>
<tr>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.state"/>
    </td>
    <td class="contain" width="30%">
        <html:select property="dto(state)" styleClass="mediumSelect" readonly="${op == 'delete'}"
                     tabindex="3">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.percent"/>
    </td>
    <td class="contain" width="30%">

        <html:select property="dto(percent)" styleClass="shortSelect" readonly="${op == 'delete'}" tabindex="7">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="percents" property="value" labelProperty="label"/>
        </html:select>
        <fmt:message key="Common.probabilitySymbol"/>
    </td>
</tr>
<tr>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.cost"/>
    </td>
    <td class="contain" width="30%">
        <app:numberText property="dto(cost)" styleClass="numberText" maxlength="12"
                        view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2" tabindex="4"/>
    </td>
    <td class="label" width="20%">
        <fmt:message key="CampaignActivity.numberContact"/>
    </td>
    <td class="contain" width="30%">
        <app:text
                property="dto(numberContact)"
                styleClass="numberText"
                tabindex="8"
                view="true"/>
    </td>
</tr>
<tr>
    <td class="label" colspan="4">
        <fmt:message key="CampaignActivity.detail"/>
    </td>
</tr>
<tr>
    <td class="label" colspan="4">
        <html:textarea property="dto(detail)" styleClass="mediumDetail"
                       style="height:120px;width:99%;" tabindex="9" readonly="${'delete' == op}"/>
        <html:hidden property="dto(descriptionId)"/>
    </td>
</tr>
<tr>
    <td class="button" colspan="4">
        <app2:securitySubmit operation="${op}" functionality="CAMPAIGNACTIVITY" styleClass="button" tabindex="11">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button" tabindex="12">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>
</TD>
</TR>
<c:if test="${'update' == op }">
<tr>
    <td>
        <table border="0" align="center" width="80%" cellpadding="0" cellspacing="0">
            <tr>
                <td class="title" colspan="4">
                    <fmt:message key="Campaign.activity.contactOptions"/>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Campaign.activity.copyContacts"/>
                </td>
                <td class="contain">
                    <c:set var="activities" value="${app2:getActivitiesToCopyContacts(param.campaignId,campaignActivityForm.dtoMap['activityId'],pageContext.request)}"/>
                    <html:select property="recipientCopy" styleId="recipientCopy" value="" styleClass="mediumSelect" tabindex="13"
                            onchange="javascript:buttonEnable(this,'copyButton')" onkeyup="javascript:buttonEnable(this,'copyButton')">
                        <html:option value="">&nbsp;</html:option>
                        <html:option value="${CAMP_RECIPIENTS}">
                            <fmt:message key="CampaignActivity.copyContacts.item">
                                <fmt:param><fmt:message key="CampaignActivity.copyContacts.campaign"/></fmt:param>
                            </fmt:message>
                        </html:option>
                        <html:options collection="activities" property="value" labelProperty="label"/>
                    </html:select>

                    <html:button property="copyContactButton" styleId="copyButton" styleClass="button" disabled="true" tabindex="14"
                                 onclick="JavaScript:putCopyUrlPopup('copyCampRecip',750,500,1)" style="width:75px;">
                        <fmt:message key="Campaign.activity.copy"/>
                    </html:button>
                </td>
                <td class="label">
                    <fmt:message key="Campaign.activity.addSingleContact"/>
                </td>
                <td class="contain">
                    <html:button property="addContactButton" styleClass="button" tabindex="19" style="width:75px;"
                                 onclick="JavaScript:showPopup('${urlAddContact}','addCampRecip','false',750,500,1)">
                        <fmt:message key="Campaign.activity.contactAdd"/>
                    </html:button>
                </td>
            </tr>
            <tr>
                <td class="label" width="22%"><fmt:message key="Campaign.activity.campContact.assignResponsible"/></td>
                <td class="contain" width="42%">
                    <select id="assignType" class="mediumSelect" tabindex="15"
                            onchange="javascript:buttonEnable(this,'assignButton')"
                            onkeyup="javascript:buttonEnable(this,'assignButton')">
                        <option value="">&nbsp;</option>
                        <option value="${ASSIGN_MANUAL}">
                            <fmt:message key="Campaign.activity.campContact.manualAssign"/>
                        </option>
                        <option value="${ASSIGN_AUTOMATIC}">
                            <fmt:message key="Campaign.activity.campContact.automaticAssign"/>
                        </option>
                    </select>

                    <html:button property="assignButton" styleId="assignButton" styleClass="button" tabindex="16" style="width:75px;"
                                 disabled="true"
                                 onclick="javascript:getAssignLocation()">
                        <fmt:message key="Campaign.activity.button.assignResponsible"/>
                    </html:button>
                </td>
                <td class="label" width="23%"><fmt:message key="Campaign.activity.contact.createTask"/></td>
                <td class="contain" width="13%">
                    <app2:checkAccessRight functionality="TASK" permission="CREATE">
                        <c:set var="taskStatus" value="<%=SchedulerConstants.NOTSTARTED%>"/>
                        <c:set var="currentUserId" value="${sessionScope.user.valueMap['userId']}"/>
                        <fmt:message var="datePattern" key="datePattern"/>
                        <c:set var="taskStartDate"
                               value="${app2:parseIntegerToDate(campaignActivityForm.dtoMap['startDate'],datePattern)}"/>
                        <c:set var="taskExpireDate"
                               value="${app2:parseIntegerToDate(campaignActivityForm.dtoMap['closeDate'],datePattern)}"/>

                        <app:url
                                value="/Campaign/Task/Forward/Create.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(activityId)=${campaignActivityForm.dtoMap['activityId']}&dto(campaignId)=${param.campaignId}&dto(date)=true&dto(title)=${campaignActivityForm.dtoMap['title']}&dto(startDate)=${taskStartDate}&dto(expireDate)=${taskExpireDate}&dto(status)=${taskStatus}&dto(percent)=0&dto(taskCreateUserId)=${currentUserId}"
                                var="urlCreateTask"/>
                        <html:button property="createTaskButton" styleId="createTaskButton" styleClass="button" tabindex="20"
                                     onclick="location.href='${urlCreateTask}'" style="width:75px;">
                            <fmt:message key="Campaign.activity.button.createTask"/>
                        </html:button>
                    </app2:checkAccessRight>
                </td>
            </tr>
            <tr>
                <td class="label"><fmt:message key="Campaign.activity.contact.documentGeneration"/></td>
                <td class="contain">
                    <c:set var="templateDocumentList"
                           value="${app2:getCampaignTemplateDocumentType(pageContext.request)}"/>
                    <html:select property="dto(documentType)" styleId="docTypeSelect" styleClass="mediumSelect" value="" tabindex="17"
                            onchange="javascript:buttonEnable(this,'executeButton')" onkeyup="javascript:buttonEnable(this,'executeButton')">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="templateDocumentList" property="value" labelProperty="label"/>
                    </html:select>

                    <app2:securitySubmit property="execute" onclick="JavaScript:getGenerateLocation();" tabindex="18"
                                         styleId="executeButton" operation="execute" functionality="CAMPAIGNACTIVITY"
                                         styleClass="button" style="width:75px;">
                        <fmt:message key="Document.generate"/>
                    </app2:securitySubmit>
                </td>
                <td class="label"><fmt:message key="Campaign.activity.contact.createSalesProcess"/></td>
                <td class="contain">
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                        <app:url
                                value="/SalesProcess/Forward/CreateMany.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(activityId)=${campaignActivityForm.dtoMap['activityId']}&dto(campaignId)=${param.campaignId}"
                                var="urlCreateProcess"/>
                        <html:button property="createSPButton" styleId="createSPButton" styleClass="button" tabindex="21" style="width:75px;"
                                     onclick="location.href='${urlCreateProcess}'">
                            <fmt:message key="Campaign.activity.button.createSalesProcess"/>
                        </html:button>
                    </app2:checkAccessRight>
                </td>
            </tr>
        </table>
        <br/>
    </td>
</tr>
<tr>
<td>
<c:set var="idx" value="${app2:getIndexTab('Campaign.Tab.Communication',pageContext.request)}"/>
<table border="0" align="center" width="80%" cellpadding="2" cellspacing="0">
<tr>
    <td>
        <iframe name="frame1"
                src="<app:url value="Campaign/Activity/CampContact/List.do?activityId=${campaignActivityForm.dtoMap['activityId']}&campaignId=${param.campaignId}&comunicationIndex=${idx}"/>"
                class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </td>
</tr>
</table>
</td>
</tr>
</c:if>
</table>
