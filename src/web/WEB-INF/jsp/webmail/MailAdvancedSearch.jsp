<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<app:url var="emailComposeUrl" value="/webmail/Mail/Forward/ComposeEmail.do" contextRelative="true"
         addModuleParams="false" addModuleName="false"/>

<script>
    function check() {
        field = document.getElementById('advancedSearchMailForm2').selectedMails;
        guia = document.getElementById('advancedSearchMailForm2').mail;

        var i;
        if (guia.checked) {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }
    function setHiddenValue(hiddenId, hiddenValue) {
        document.getElementById(hiddenId).value = hiddenValue;
        document.getElementById('advancedSearchMailForm2').submit();
    }

    function moveToFolderAction() {
        document.getElementById('moveToButtonId').value = "true";
        document.getElementById('advancedSearchMailForm2').submit();
    }

    function testSubmit() {
        var test = (document.getElementById('moveToButtonId').value == "true");
        return (test);
    }

    function redirectComposeEmail() {
        location.href = '${emailComposeUrl}';
    }

</script>

<c:set var="systemFolderCounter"
       value="${app2:getSystemFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>
<c:set var="customFoldersList"
       value="${app2:getCustomFolders(sessionScope.user.valueMap['userId'], pageContext.request)}" scope="request"/>

<%--Size of the list--%>
<%
    org.alfacentauro.fantabulous.controller.ResultList resultList = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("advancedSearchMailList");
    if (resultList != null) {
        pageContext.setAttribute("list_size", new Integer(resultList.getResultSize()));
    } else {
        pageContext.setAttribute("list_size", new Integer(0));
    }
%>

<%--Folder_type's--%>
<c:set var="FOLDER_INBOX" value="<%=WebMailConstants.FOLDER_INBOX%>"/>
<c:set var="FOLDER_SENDITEMS" value="<%=WebMailConstants.FOLDER_SENDITEMS%>"/>
<c:set var="FOLDER_DRAFTITEMS" value="<%=WebMailConstants.FOLDER_DRAFTITEMS%>"/>
<c:set var="FOLDER_TRASH" value="<%=WebMailConstants.FOLDER_TRASH%>"/>
<c:set var="FOLDER_OUTBOX" value="<%=WebMailConstants.FOLDER_OUTBOX%>"/>
<c:set var="FOLDER_DEFAULT" value="<%=WebMailConstants.FOLDER_DEFAULT%>"/>

<%--Mail_priority's--%>
<c:set var="MAIL_PRIORITY_DEFAULT" value="<%=WebMailConstants.MAIL_PRIORITY_DEFAULT%>"/>
<c:set var="MAIL_PRIORITY_HIGHT" value="<%=WebMailConstants.MAIL_PRIORITY_HIGHT%>"/>

<c:set var="MAIL_IN" value="<%=String.valueOf(WebMailConstants.IN_VALUE)%>"/>
<c:set var="MAIL_OUT" value="<%=String.valueOf(WebMailConstants.OUT_VALUE)%>"/>

<%--folder column tio show constants--%>
<c:set var="SHOW_TO" value="<%=WebMailConstants.ColumnToShow.TO.getConstantAsString()%>"/>
<c:set var="SHOW_FROM_TO" value="<%=WebMailConstants.ColumnToShow.FROM_TO.getConstantAsString()%>"/>

<c:choose>
    <c:when test="${empty(advancedSearchMailForm.params['mailFolderId'])}">
        <c:set var="folderColumnToShow" value="${SHOW_FROM_TO}"/>
    </c:when>
    <c:otherwise>
        <c:set var="folderColumnToShow"
               value="${app2:getFolderColumnToShow(advancedSearchMailForm.params['mailFolderId'])}"/>
    </c:otherwise>
</c:choose>

<html:form action="/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true"
           styleId="advancedSearchMailForm"
           focus="parameter(mailSubject)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="${pageTitle}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.subject"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(mailSubject)"
                                  styleClass="${app2:getFormInputClasses()} largeText"
                                  maxlength="245" tabindex="1"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Webmail.advancedSearch.folder"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(searchMailFolderId)"
                                     styleClass="${app2:getFormSelectClasses()} webmailSelect"
                                     tabindex="2">
                            <html:option value=""/>
                            <html:option value="${systemFolderCounter['inboxId']}">
                                <fmt:message key="Webmail.folder.inbox"/>
                            </html:option>
                            <html:option value="${systemFolderCounter['sentId']}">
                                <fmt:message key="Webmail.folder.sendItems"/>
                            </html:option>
                            <html:option value="${systemFolderCounter['draftId']}">
                                <fmt:message
                                        key="Webmail.folder.draftItems"/>
                            </html:option>
                            <html:option value="${systemFolderCounter['trashId']}">
                                <fmt:message key="Webmail.folder.trash"/>
                            </html:option>
                            <html:option value="${systemFolderCounter['outboxId']}">
                                <fmt:message key="Webmail.folder.outbox"/>
                            </html:option>
                            <c:forEach var="customFolder" items="${customFoldersList}">
                                <html:option
                                        value="${customFolder.folderId}">${customFolder.folderName}</html:option>
                            </c:forEach>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.from"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(mailFrom)"
                                  styleClass="${app2:getFormInputClasses()} largeText" styleId="fromFieldId"
                                  maxlength="245"
                                  tabindex="3"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.priority"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="mailPrioritiesList" value="${app2:getPriorityValues(pageContext.request)}"/>
                        <html:select property="parameter(mailPriority)"
                                     styleClass="${app2:getFormSelectClasses()} webmailSelect" tabindex="4">
                            <html:options collection="mailPrioritiesList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.to"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(mailTo)" styleClass="${app2:getFormInputClasses()} largeText"
                                  styleId="toRecipientId"
                                  maxlength="245"
                                  tabindex="5"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Webmail.advancedSearch.mailState"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="mailStatesList" value="${app2:getMailStates(pageContext.request)}"/>
                        <html:select property="parameter(mailStateParam)"
                                     styleClass="${app2:getFormSelectClasses()} webmailSelect" tabindex="6">
                            <html:options collection="mailStatesList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.Cc"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(mailToCC)"
                                  styleClass="${app2:getFormInputClasses()} largeText" styleId="ccRecipientId"
                                  maxlength="245"
                                  tabindex="7"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.attachments"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="mailAttachStatesList" value="${app2:getAttachStates(pageContext.request)}"/>
                        <html:select property="parameter(mailAttach)"
                                     styleClass="${app2:getFormSelectClasses()} webmailSelect" tabindex="8">
                            <html:options collection="mailAttachStatesList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.bodyContent"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(body)" styleClass="${app2:getFormInputClasses()} largeText"
                                  styleId="fromFieldId"
                                  maxlength="245"
                                  tabindex="9"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Webmail.advancedSearch.isCommunicationRelated"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="mailCommunicationStatesList"
                               value="${app2:getCommunicationStates(pageContext.request)}"/>
                        <html:select property="parameter(mailCommunication)"
                                     styleClass="${app2:getFormSelectClasses()} webmailSelect"
                                     tabindex="10">
                            <html:options collection="mailCommunicationStatesList" property="value"
                                          labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Webmail.tray.date"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <fmt:message key="Common.from" var="from"/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <div class="input-group date">
                                    <app:dateText property="parameter(startSendDate)" maxlength="10"
                                                  styleId="startRange"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  parseLongAsDate="true" tabindex="11"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endSendDate)" maxlength="10"
                                                  styleId="endRange"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  parseLongAsDate="true" tabindex="11"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" style="word-wrap: break-word;">
                        <fmt:message key="Webmail.advancedSearch.incout"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="mailIncomingOutgoingStatesList"
                               value="${app2:getIncOutValues(pageContext.request)}"/>
                        <html:select property="parameter(inOut)"
                                     styleClass="${app2:getFormSelectClasses()} webmailSelect" tabindex="12">
                            <html:options collection="mailIncomingOutgoingStatesList" property="value"
                                          labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Mail.size"/>&nbsp;(<fmt:message key="Webmail.mailTray.Kb"/>)
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <app:numberText property="parameter(startSizeRange)"
                                                styleClass="${app2:getFormInputClasses()}"
                                                maxlength="10"
                                                placeHolder="${from}"
                                                numberType="integer" tabindex="13"/>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <app:numberText property="parameter(endSizeRange)"
                                                styleClass="${app2:getFormInputClasses()}"
                                                maxlength="10"
                                                placeHolder="${to}"
                                                numberType="integer" tabindex="14"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="15">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                    <html:button property="reset1" tabindex="16" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="formReset('advancedSearchMailForm')">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </div>
            </div>
        </fieldset>
    </div>
</html:form>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="numberFormat" key="numberFormat.1DecimalPlacesOptional"/>

<c:set var="mail_subject_size" value="${33}"/>
<c:set var="mail_folderName_size" value="${13}"/>
<c:set var="mail_from_size" value="${23}"/>

<fmt:message key="Webmail.updateCommunications" var="communicationMessage"/>
<html:form action="/Mail/MailAdvancedSearchOperation.do?mailSearch=true&mailAdvancedSearch=true"
           styleId="advancedSearchMailForm2" onsubmit="return testSubmit()">
    <html:hidden property="moveToButton" styleId="moveToButtonId"/>
    <html:hidden property="emailIdentifiers" value="${emailIdentifiersAdvanced}" styleId="emails"/>
    <div class="row marginButton">
        <div class="col-xs-6 col-sm-2 pull-left">
            <div class="col-xs-4 paddingRemove">
                <html:hidden property="moveToTrash" styleId="moveToTrashId"/>
                <html:link styleClass="btn btn-link" href="#"
                           onclick="javascript:setHiddenValue('moveToTrashId','moveToTrash');"
                           style="text-decoration: none;" titleKey="Webmail.moveToTrash">
                    <span class="glyphicon glyphicon-trash"></span>
                </html:link>
            </div>

            <div class="col-xs-8 paddingRemove">
                <html:hidden property="emptyFolderToTrash" styleId="emptyFolderToTrashId"/>
                <html:link styleClass="btn btn-link" href="#" titleKey="Webmail.moveAllToTrash"
                           onclick="javascript:setHiddenValue('emptyFolderToTrashId','emptyFolderToTrash');"
                           style="text-decoration: none;">
                    <span class="glyphicon glyphicon-trash"></span>
                    <span class="glyphicon glyphicon-trash"></span>
                </html:link>
            </div>
        </div>

        <div class="col-xs-6 col-sm-3 text-center marginButton">
            <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(emailComposeLink)"
                         onclick="redirectComposeEmail()" style="white-space: normal;">
                <fmt:message key="Webmail.compose"/>
            </html:button>
        </div>

        <div class="col-xs-12 col-sm-7">
                <div class="col-xs-5">
                    <html:button styleClass="${app2:getFormButtonClasses()} pull-right" property="dto(button)"
                                 onclick="moveToFolderAction()"
                                 tabindex="17">
                        <fmt:message key="Webmail.tray.moveTo"/>
                    </html:button>
                </div>
                <div class="col-xs-7">
                    <html:select property="moveToFolderId" styleClass="${app2:getFormSelectClasses()} webmailSelect"
                                 tabindex="18"
                                 styleId="moveToFolderSelect">
                        <html:option value=""/>
                        <html:option value="${systemFolderCounter['inboxId']}">
                            <fmt:message key="Webmail.folder.inbox"/>
                        </html:option>
                        <html:option value="${systemFolderCounter['sentId']}">
                            <fmt:message key="Webmail.folder.sendItems"/>
                        </html:option>
                        <html:option value="${systemFolderCounter['draftId']}">
                            <fmt:message
                                    key="Webmail.folder.draftItems"/>
                        </html:option>
                        <html:option value="${systemFolderCounter['trashId']}">
                            <fmt:message key="Webmail.folder.trash"/>
                        </html:option>
                        <html:option value="${systemFolderCounter['outboxId']}">
                            <fmt:message key="Webmail.folder.outbox"/>
                        </html:option>
                        <c:forEach var="customFolder" items="${customFoldersList}">
                            <html:option value="${customFolder.folderId}">${customFolder.folderName}</html:option>
                        </c:forEach>
                    </html:select>
                </div>
        </div>
    </div>

    <tags:dragAndDropMailsToFolder/>

    <div class="table-responsive">
        <fanta:table action="/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true"
                     list="advancedSearchMailList"
                     id="mailList"
                     mode="bootstrap"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" imgPath="${baselayout}" align="center">
            <c:set var="readMailAction"
                   value="Mail/ReadEmail.do?dto(mailId)=${mailList.MAILID}&mailIndex=${mailList.MAILINDEX}&mailSearch=true&mailAdvancedSearch=true"/>
            <c:if test="${list_size >0}">
                <fanta:checkBoxColumn styleClass="listItemCenter" name="mail" id="selectedMails"
                                      onClick="javascript:check();"
                                      property="MAILID" headerStyle="listHeader" width="3%"/>
            </c:if>

            <app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="2%">

                    <c:choose><%--for the mailcontacts--%>
                        <c:when test="${app2:isActionMailContact(mailList.ACTIONMAILCONTACTCOUNT,mailList.INOUT)}">
                            <c:set var="iconName" value="emailaction.gif"/>
                            <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                                <c:set var="iconName" value="emailaction-half.gif"/>
                            </c:if>
                            <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                                styleClass="listItemCenter"
                                                headerStyle="listHeader"
                                                image="${sessionScope.baselayout}/img/webmail/${iconName}"/>
                        </c:when>
                        <c:when test="${app2:isSupportActivityMailContact(mailList.SUPPORTMAILCONTACTCOUNT,mailList.INOUT)}">
                            <c:set var="iconName" value="emailactivity.gif"/>
                            <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                                <c:set var="iconName" value="emailactivity-half.gif"/>
                            </c:if>
                            <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                                styleClass="listItemCenter"
                                                headerStyle="listHeader"
                                                image="${sessionScope.baselayout}/img/webmail/${iconName}"/>
                        </c:when>
                        <c:when test="${app2:hasMailContact(mailList.MAILCONTACTCOUNT,mailList.INOUT)}">
                            <c:set var="iconName" value="emailcomm.gif"/>
                            <c:if test="${not app2:allRecipientsWithMailContact(mailList.MAILCONTACTCOUNT,mailList.RECIPIENTCOUNT,mailList.INOUT)}">
                                <c:set var="iconName" value="emailcomm-half.gif"/>
                            </c:if>
                            <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                                styleClass="listItemCenter"
                                                headerStyle="listHeader"
                                                render="false">
                                <c:if test="${mailList.INOUT == MAIL_IN || mailList.INOUT ==  MAIL_OUT}">
                                    <html:link
                                            action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}&mailSearch=true&mailAdvancedSearch=true">
                                        <html:img src="${sessionScope.baselayout}/img/webmail/${iconName}" border="0"
                                                  title="${communicationMessage}"/>
                                    </html:link>
                                </c:if>
                            </fanta:actionColumn>
                        </c:when>
                        <c:otherwise>
                            <fanta:actionColumn name="editContacts" title="Webmail.updateCommunications"
                                                styleClass="listItemCenter"
                                                headerStyle="listHeader"
                                                render="false">
                                <c:if test="${(mailList.INOUT == MAIL_IN || mailList.INOUT ==  MAIL_OUT) && (1 == mailList.ISCOMPLETEEMAIL || empty mailList.ISCOMPLETEEMAIL)}">
                                    <html:link
                                            action="/Mail/Forward/MailRecipientList.do?dto(mailId)=${mailList.MAILID}&mailSearch=true&mailAdvancedSearch=true">
                                        <html:img src="${sessionScope.baselayout}/img/webmail/emailcommbroke.gif"
                                                  border="0"
                                                  title="${communicationMessage}"/>
                                    </html:link>
                                </c:if>
                            </fanta:actionColumn>
                        </c:otherwise>
                    </c:choose>
                </fanta:columnGroup>
            </app2:checkAccessRight>

            <c:set var="hasReadState" value="${app2:hasMailStateRead(mailList.MAILSTATE)}"/>
            <c:set var="unreadFontWeight" value="${!hasReadState ? 'bold' : ''}"/>
            <c:choose>
                <c:when test="${folderColumnToShow eq SHOW_TO || folderColumnToShow eq SHOW_FROM_TO}">
                    <fanta:dataColumn name="" styleClass="listItem tdDragCls"
                                      title="${folderColumnToShow eq SHOW_TO ? 'Webmail.folder.show.to' : 'Webmail.folder.show.fromTo'}"
                                      headerStyle="listHeader"
                                      width="15%" renderData="false">

                        <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">

                            <c:if test="${folderColumnToShow eq SHOW_FROM_TO}">
                                <c:choose>
                                    <c:when test="${mailList.INOUT == MAIL_OUT}">
                                        <span class="${app2:getClassGlyphOpen()}"
                                              title="<fmt:message key="Webmail.folder.show.to"/>"></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="${app2:getClassGlyphSave()}"
                                              title="<fmt:message key="Webmail.folder.show.from"/>"></span>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>

                            <app:link action="${readMailAction}"
                                      title="${mailList.MAILTOFROM}">${app2:filterForHtml(mailList.MAILTOFROM)}</app:link>
                        </fanta:textShorter>
                        <input type="hidden" name="dragMail" class="dragMailCls" value="${mailList.MAILID}"/>
                    </fanta:dataColumn>
                </c:when>
                <c:otherwise>
                    <fanta:dataColumn name="MAILPERSONALFROM" styleClass="listItem tdDragCls" title="Webmail.tray.from"
                                      headerStyle="listHeader" width="15%" renderData="false" orderable="true">

                        <fanta:textShorter title="${mailList.MAILTOFROM}" fontWeight="${unreadFontWeight}">
                            <app:link action="${readMailAction}">
                                <c:out value="${mailList.MAILTOFROM}"/>
                            </app:link>
                        </fanta:textShorter>

                        <input type="hidden" name="dragMail" class="dragMailCls" value="${mailList.MAILID}"/>
                    </fanta:dataColumn>
                </c:otherwise>
            </c:choose>
            <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.answeredOrForwarded"
                              headerStyle="listHeader"
                              width="2%" renderData="false" glyphiconClass="${app2:getClassGlyphReply()}">
                <c:choose>
                    <c:when test="${app2:hasMailStateAnswered(mailList.MAILSTATE)}"><!--REPLY,REPLY_ALL-->
                        <span class="${app2:getClassGlyphReply()}"
                              title="<fmt:message key="Webmail.tray.answered"/>"></span>
                    </c:when>
                    <c:when test="${app2:hasMailStateForward(mailList.MAILSTATE)}"><!--FORWARD-->
                        <span class="${app2:getClassGlyphForward()}"
                              title="<fmt:message key="Webmail.tray.forwarded"/>"></span>
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.attach" headerStyle="listHeader"
                              width="2%" renderData="false"
                              glyphiconClass="${app2:getClassGlyphPaperClip()}">
                <c:if test="${1 == mailList.MAILHASATTACH}">
                    <span class="${app2:getClassGlyphPaperClip()}"
                          title="<fmt:message key="Webmail.tray.attach"/>"></span>
                </c:if>
            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.highPriority"
                              headerStyle="listHeader"
                              width="2%" renderData="false"
                              glyphiconClass="${app2:getClassGlyphPrioHigh()}">
                <c:if test="${mailList.MAILPRIORITY== MAIL_PRIORITY_HIGHT}"><!--High priority-->
                    <span class="${app2:getClassGlyphPrioHigh()}"
                          title="<fmt:message key="Webmail.tray.highPriority"/>"></span>
                </c:if>
            </fanta:dataColumn>
            <%--
                        <fanta:dataColumn name="" styleClass="listItemCenter" title="Webmail.tray.readOrUnRead" headerStyle="listHeader"
                                          width="2%" renderData="false"
                                          headerImage="${sessionScope.baselayout}/img/webmail/msg_unread.gif">
                            <c:if test="${!hasReadState}"><!--UNREAD-->
                                <img src="${sessionScope.baselayout}/img/webmail/msg_unread.gif"
                                     title="<fmt:message key="Webmail.tray.unRead"/>"
                                     alt="<fmt:message key="Webmail.tray.unRead"/>"/>
                            </c:if>
                            <c:if test="${hasReadState}">
                                <!--READ-->
                                <img src="${sessionScope.baselayout}/img/webmail/msg_read.gif"
                                     title="<fmt:message key="Webmail.tray.read"/>"
                                     alt="<fmt:message key="Webmail.tray.read"/>"/>
                            </c:if>
                        </fanta:dataColumn>
            --%>
            <fanta:dataColumn name="MAILSUBJECT" action="${readMailAction}" styleClass="listItem"
                              title="Webmail.tray.subject"
                              headerStyle="listHeader" renderData="false" width="24%" orderable="true">
                <fanta:textShorter title="${mailList.MAILSUBJECT}" fontWeight="${unreadFontWeight}">
                    ${mailList.MAILSUBJECT}
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="SENTDATETIME" styleClass="listItem" title="Webmail.tray.date"
                              headerStyle="listHeader"
                              width="15%" orderable="true" renderData="false" nowrap="nowrap">
                ${app2:getDateWithTimeZone(mailList.SENTDATETIME, timeZone, dateTimePattern)}
            </fanta:dataColumn>
            <fanta:dataColumn name="FOLDERNAME" styleClass="listItem" title="Webmail.tray.folder"
                              headerStyle="listHeader"
                              width="9%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${mailList.FOLDERTYPE==FOLDER_INBOX}">
                        <fmt:message key="Webmail.folder.inbox" var="folderName"/>
                    </c:when>
                    <c:when test="${mailList.FOLDERTYPE==FOLDER_SENDITEMS}">
                        <fmt:message key="Webmail.folder.sendItems" var="folderName"/>
                    </c:when>
                    <c:when test="${mailList.FOLDERTYPE==FOLDER_DRAFTITEMS}">
                        <fmt:message key="Webmail.folder.draftItems" var="folderName"/>
                    </c:when>
                    <c:when test="${mailList.FOLDERTYPE==FOLDER_TRASH}">
                        <fmt:message key="Webmail.folder.trash" var="folderName"/>
                    </c:when>
                    <c:when test="${mailList.FOLDERTYPE==FOLDER_OUTBOX}">
                        <fmt:message key="Webmail.folder.outbox" var="folderName"/>
                    </c:when>
                    <c:when test="${mailList.FOLDERTYPE==FOLDER_DEFAULT}">
                        <c:set var="folderName" value="${mailList.FOLDERNAME}"/>
                    </c:when>
                </c:choose>
                <fanta:textShorter title="${folderName}">${folderName}</fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="MAILSIZE" styleClass="listItem2Right" title="Webmail.tray.size"
                              headerStyle="listHeader"
                              width="7%" renderData="false" orderable="true" nowrap="true">
                <c:choose>
                    <c:when test="${mailList.MAILSIZE<1024}">
                        ${1}&nbsp;<fmt:message key="Webmail.mailTray.Kb"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:formatNumber value="${fn:substringBefore(mailList.MAILSIZE/1024,'.')}"
                                          type="number" pattern="${numberFormat}"/>&nbsp;<fmt:message
                            key="Webmail.mailTray.Kb"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>

    </div>
</html:form>
