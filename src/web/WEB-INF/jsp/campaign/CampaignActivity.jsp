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

<tags:initBootstrapSelectPopup/>
<tags:bootstrapModalPopup styleId="recipientCopy_popup_id"
                          isLargeModal="true" modalTitleKey="Campaign.recipient.plural"/>

<tags:bootstrapModalPopup styleId="ContactCopy_popup_id"
                          isLargeModal="true" modalTitleKey="Contact.search.ContactsOrContactPerson"/>

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
            showPopupCopy(url, searchName, false, w, h, scroll);
        } else if (recipientCopy != "") {
            var id_copyActivity = recipientCopy;
            location.href = ${jsCopyActivityUrl};
        }
    }

    function showPopupCopy(url, searchName, submitOnSelect, w, h, scroll) {
        autoSubmit = submitOnSelect;
        launchBootstrapPopup("recipientCopy_popup_id", url, searchName, autoSubmit);
    }
    function showPopup(url, searchName, submitOnSelect, w, h, scroll) {
        autoSubmit = submitOnSelect;
        launchBootstrapPopup("ContactCopy_popup_id", url, searchName, autoSubmit);
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

        if (docType.value == "${TEMPLATE_HTML}") {
            location.href = ${jsEmailUrl};
        } else if (docType.value == "${TEMPLATE_WORD}") {
            location.href = ${jsDocumentUrl};
        }
    }


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
    //-->
</script>

<c:set var="percents" value="${app2:defaultProbabilities()}"/>
<c:set var="statusList" value="${app2:getCampaignActivityStatusList(pageContext.request)}"/>
<fmt:message var="datePattern" key="datePattern"/>
<tags:initBootstrapDatepicker/>

<c:set var="showCurrentDate" value="<%=Boolean.valueOf(false)%>"/>
<c:if test="${op ==  'create'}">
    <c:set var="showCurrentDate" value="<%=Boolean.valueOf(true)%>"/>
</c:if>
<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(activityId)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(activityId)"/>
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <div class="${app2:getFormButtonWrapperClasses()}">

        <app2:securitySubmit operation="${op}" functionality="CAMPAIGNACTIVITY"
                             styleClass="button ${app2:getFormButtonClasses()}">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                    ${title}
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                        <fmt:message key="CampaignActivity.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:text property="dto(title)"
                                  styleId="title_id"
                                  styleClass="mediumText ${app2:getFormInputClasses()}"
                                  tabindex="1"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateTextId_open">
                        <fmt:message key="CampaignActivity.startDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText
                                    property="dto(startDate)"
                                    datePatternKey="${datePattern}"
                                    view="${'delete' == op}"
                                    mode="bootstrap"
                                    tabindex="2"
                                    styleId="dateTextId_open"
                                    styleClass="dateText ${app2:getFormInputClasses()}"
                                    maxlength="10"
                                    calendarPicker="true"
                                    currentDate="${showCurrentDate}"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="userId_id">
                        <fmt:message key="CampaignActivity.responsible"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(userId)"
                                      styleId="userId_id"
                                      listName="internalUserList"
                                      labelProperty="name" valueProperty="id"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      tabIndex="3"
                                      module="/admin"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateTextId_close">
                        <fmt:message key="CampaignActivity.closeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText
                                    property="dto(closeDate)"
                                    datePatternKey="${datePattern}"
                                    view="${'delete' == op}"
                                    mode="bootstrap"
                                    tabindex="4"
                                    styleId="dateTextId_close" styleClass="dateText ${app2:getFormInputClasses()}"
                                    maxlength="10"
                                    calendarPicker="true" currentDate="${showCurrentDate}"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="state_id">
                        <fmt:message key="CampaignActivity.state"/>
                    </label>

                    <div class=" ${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <html:select property="dto(state)"
                                     styleId="state_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete'}"
                                     tabindex="5">
                            <html:option value=""/>
                            <html:options collection="statusList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="percent_id">
                        <fmt:message key="CampaignActivity.percent"/>
                        ( <fmt:message key="Common.probabilitySymbol"/>)
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)} ">

                        <html:select property="dto(percent)"
                                     styleId="percent_id"
                                     styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete'}"
                                     tabindex="6">
                            <html:option value=""></html:option>
                            <html:options collection="percents" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="cost_id">
                        <fmt:message key="CampaignActivity.cost"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:numberText property="dto(cost)"
                                        styleId="cost_id"
                                        styleClass="numberText ${app2:getFormInputClasses()}"
                                        maxlength="12"
                                        view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2"
                                        tabindex="7"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="numberContact_id">
                        <fmt:message key="CampaignActivity.numberContact"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:text
                                property="dto(numberContact)"
                                styleId="numberContact_id"
                                styleClass="numberText"
                                tabindex="8"
                                view="true"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 threeSmallGrid">
                    <label class="control-label" for="detail_id">
                        <fmt:message key="CampaignActivity.detail"/>
                    </label>
                    <html:textarea property="dto(detail)"
                                   styleId="detail_id"
                                   styleClass="mediumDetail ${app2:getFormInputClasses()}"
                                   style="height:120px;width:99%;" tabindex="9" readonly="${'delete' == op}"/>
                    <html:hidden property="dto(descriptionId)"/>
                </div>
            </div>
        </fieldset>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}" functionality="CAMPAIGNACTIVITY"
                             styleClass="button ${app2:getFormButtonClasses()}"
                             tabindex="11">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="12">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>

<c:if test="${'update' == op }">

    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Campaign.activity.contactOptions"/>
        </legend>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.copyContacts"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <c:set var="activities"
                           value="${app2:getActivitiesToCopyContacts(param.campaignId,campaignActivityForm.dtoMap['activityId'],pageContext.request)}"/>
                    <div class="row">
                        <div class="col-xs-6 col-md-8 col-lg-8">
                            <html:select property="recipientCopy" styleId="recipientCopy" value=""
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="13"
                                         onchange="javascript:buttonEnable(this,'copyButton')"
                                         onkeyup="javascript:buttonEnable(this,'copyButton')">
                                <html:option value="">&nbsp;</html:option>
                                <html:option value="${CAMP_RECIPIENTS}">
                                    <fmt:message key="CampaignActivity.copyContacts.item">
                                        <fmt:param><fmt:message
                                                key="CampaignActivity.copyContacts.campaign"/></fmt:param>
                                    </fmt:message>
                                </html:option>
                                <html:options collection="activities" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="col-xs-3 col-md-4 col-lg-4 ">
                            <html:button property="copyContactButton" styleId="copyButton"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left"
                                         disabled="true" tabindex="14"
                                         onclick="JavaScript:putCopyUrlPopup('copyCampRecip',750,500,1)">
                                <fmt:message key="Campaign.activity.copy"/>
                            </html:button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.addSingleContact"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <html:button property="addContactButton" styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="15"
                                 onclick="JavaScript:showPopup('${urlAddContact}','addCampRecip','false',750,500,1)">
                        <fmt:message key="Campaign.activity.contactAdd"/>
                    </html:button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.campContact.assignResponsible"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">

                    <div class="row">
                        <div class="col-xs-6 col-md-8 col-lg-8">
                            <select id="assignType" class="mediumSelect ${app2:getFormSelectClasses()}" tabindex="16"
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
                        </div>
                        <div class="col-xs-3 col-md-4 col-lg-4">
                            <html:button property="assignButton" styleId="assignButton"
                                         styleClass="button ${app2:getFormButtonClasses()} pull-left"
                                         tabindex="17"
                                         disabled="true"
                                         onclick="javascript:getAssignLocation()">
                                <fmt:message key="Campaign.activity.button.assignResponsible"/>
                            </html:button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.contact.createTask"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
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
                        <html:button property="createTaskButton" styleId="createTaskButton"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     tabindex="18"
                                     onclick="location.href='${urlCreateTask}'" style="width:75px;">
                            <fmt:message key="Campaign.activity.button.createTask"/>
                        </html:button>
                    </app2:checkAccessRight>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.contact.documentGeneration"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="row">
                        <div class="col-xs-6 col-md-8 col-lg-8">
                            <c:set var="templateDocumentList"
                                   value="${app2:getCampaignTemplateDocumentType(pageContext.request)}"/>
                            <html:select property="dto(documentType)" styleId="docTypeSelect"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         value="" tabindex="19"
                                         onchange="javascript:buttonEnable(this,'executeButton')"
                                         onkeyup="javascript:buttonEnable(this,'executeButton')">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="templateDocumentList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <div class="col-xs-3 col-md-4 col-lg-4 ">
                            <app2:securitySubmit property="execute" onclick="JavaScript:getGenerateLocation();"
                                                 tabindex="20"
                                                 styleId="executeButton" operation="execute"
                                                 functionality="CAMPAIGNACTIVITY"
                                                 styleClass="button ${app2:getFormButtonClasses()} pull-left">
                                <fmt:message key="Document.generate"/>
                            </app2:securitySubmit>
                        </div>
                    </div>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.contact.createSalesProcess"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                        <app:url
                                value="/SalesProcess/Forward/CreateMany.do?activityId=${campaignActivityForm.dtoMap['activityId']}&dto(activityId)=${campaignActivityForm.dtoMap['activityId']}&dto(campaignId)=${param.campaignId}"
                                var="urlCreateProcess"/>
                        <html:button property="createSPButton" styleId="createSPButton"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     tabindex="21" style="width:75px;"
                                     onclick="location.href='${urlCreateProcess}'">
                            <fmt:message key="Campaign.activity.button.createSalesProcess"/>
                        </html:button>
                    </app2:checkAccessRight>
                </div>
            </div>
        </div>
    </div>

    <c:set var="idx" value="${app2:getIndexTab('Campaign.Tab.Communication',pageContext.request)}"/>

    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <iframe name="frame1"
                src="<app:url value="Campaign/Activity/CampContact/List.do?activityId=${campaignActivityForm.dtoMap['activityId']}&campaignId=${param.campaignId}&comunicationIndex=${idx}"/>"
                class="embed-responsive-item" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>

</c:if>

<tags:jQueryValidation formName="campaignActivityForm"/>
